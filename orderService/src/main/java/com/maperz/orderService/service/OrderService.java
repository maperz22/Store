package com.maperz.orderService.service;

import com.maperz.orderService.dto.Currency;
import com.maperz.orderService.dto.*;
import com.maperz.orderService.event.InvoiceEvent;
import com.maperz.orderService.event.OrderPlacedEvent;
import com.maperz.orderService.model.Order;
import com.maperz.orderService.model.OrderItem;
import com.maperz.orderService.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final WebClient.Builder webClient;
    private final WebClient webClient2;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Async
    @Transactional
    public CompletableFuture<Void> placeAnOrder(OrderDTO request, String currency){

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        order.setOrderItemsList(request.getOrderItemsList()
                .stream()
                .map(this::mapToOrderList)
                .toList());

        // Call inventory service to check if the items are available
        List<String> skuCodes = order.getOrderItemsList()
                .stream()
                .map(OrderItem::getSkuCode)
                .toList();

        InventoryDTO[] inventoryDTOS = webClient.build().get()
                .uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes)
                                .build())
                .retrieve()
                .bodyToMono(InventoryDTO[].class)
                .block();

        assert inventoryDTOS != null;
        boolean productsInStock = Arrays.stream(inventoryDTOS).allMatch(InventoryDTO::isInStock);
        if (!productsInStock) {
            throw new RuntimeException("Some of the products are not in stock");
        }
        else {
        // Calculate the total price
            BigDecimal totalPrice = order.getOrderItemsList()
                    .stream()
                    .map(orderItem -> orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            if (currency.equals("PLN")) {
                log.info("Total price of the order is {}", totalPrice);
            } else {
            Currency block = webClient2.get()
                    .uri("http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/?format=json")
                    .retrieve()
                    .bodyToMono(Currency.class)
                    .block();


                    // restTemplate.getForObject("http://api.nbp.pl/api/exchangerates/rates/a/" + currency + "/?format=json", Currency.class);

                double mid = block.getRates()[0].getMid();

                totalPrice = totalPrice.multiply(BigDecimal.valueOf(mid));
                log.info("Total price of the order is {}", totalPrice);
            }

            // Call payment service to make the payment

            // Call user service to update the user's order history
            updateUserHistory(OrderHistoryDTO.builder()
                    .orderNumber(order.getOrderNumber())
                    .userEmail(request.getUserEmail())
                    .build());
            // Call inventory service to update the inventory
                Map<String, Integer> requests = new HashMap<>();
                order.getOrderItemsList()
                        .forEach(orderItem -> requests.put(orderItem.getSkuCode(), orderItem.getQuantity()));

                webClient.build().put()
                        .uri("http://inventory-service/api/inventory/remove",
                                UriBuilder::build)
                        .bodyValue(requests)
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();

            // Call invoice service to generate the invoice
            createInvoice(new InvoiceEvent(order.getOrderNumber(), order.getOrderItemsList()));

            // Call shipping service to ship the order

            // Save the order in the database
            repository.save(order);
            log.info("Order {} has been placed", order.getOrderNumber());

            // Call notification service to notify the user
            sendOrderConfirmation(new OrderPlacedEvent(order.getOrderNumber()));

        }
        return CompletableFuture.completedFuture(null);
    }

    private void createInvoice(InvoiceEvent event) {
        Message<InvoiceEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "InvoiceTopic")
                .build();

        log.info("Sending notification to Invoice Service");
        kafkaTemplate.send(message);
    }

    private void updateUserHistory(OrderHistoryDTO event) {
        webClient.build().put()
                .uri("http://user-service/user/api/updateOrderHistory",
                        UriBuilder::build)
                .bodyValue(event)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }



    private void sendOrderConfirmation(OrderPlacedEvent event) {

        Message<OrderPlacedEvent> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "notificationTopic")
                .build();

        log.info("Sending notification to Email Service");
        kafkaTemplate.send(message);
    }

    public OrderResponse getOrder(String orderNumber) {
        if (orderNumber == null || orderNumber.isEmpty()) {
            throw new IllegalArgumentException("Order number cannot be empty");
        } else if (!repository.existsByOrderNumber(orderNumber)) {
            throw new RuntimeException("Order not found");
        } else {
        Order order = repository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        log.info("Order {} has been found", orderNumber);
        return mapToOrderResponse(order);
        }

    }

    private OrderResponse mapToOrderResponse(Order order) {
        return OrderResponse.builder()
                .orderNumber(order.getOrderNumber())
                .orderItemsList(order.getOrderItemsList().stream().map(this::mapToOrderItemDTO).toList())
                .build();
    }

    private OrderItemDTO mapToOrderItemDTO(OrderItem orderItem) {
        return OrderItemDTO.builder()
                .price(orderItem.getPrice())
                .quantity(orderItem.getQuantity())
                .SkuCode(orderItem.getSkuCode())
                .build();
    }

    private OrderItem mapToOrderList(OrderItemDTO orderItemDTO) {
        return OrderItem.builder()
                .price(orderItemDTO.getPrice())
                .quantity(orderItemDTO.getQuantity())
                .SkuCode(orderItemDTO.getSkuCode())
                .build();
    }


    public List<OrderResponse> getAllOrders() {
        return repository.findAll()
                .stream()
                .map(this::mapToOrderResponse)
                .toList();
    }
}
