package com.maperz.orderService.controller;

import com.maperz.orderService.dto.OrderDTO;
import com.maperz.orderService.dto.OrderResponse;
import com.maperz.orderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping("/{currency}")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeAnOrderFallback")
    @TimeLimiter(name = "inventory")
    public CompletableFuture<Void> placeAnOrder(@RequestBody OrderDTO request, @PathVariable String currency){
        return service.placeAnOrder(request, currency);
    }

    @GetMapping("/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeAnOrderFallback")
    public OrderResponse getOrder(@PathVariable String orderNumber){
        return service.getOrder(orderNumber);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @CircuitBreaker(name = "inventory", fallbackMethod = "placeAnOrderFallback")
    public List<OrderResponse> getAllOrders(){
        return service.getAllOrders();
    }

    // Fallback method

    public CompletableFuture<Void> placeAnOrderFallback(OrderDTO request, String currency, Throwable throwable){
        return CompletableFuture.runAsync(() -> System.out.println("Fallback method"));
    }



}
