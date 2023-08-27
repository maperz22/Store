package com.maperz.invoiceService.service;

import com.maperz.invoiceService.dto.*;
import com.maperz.invoiceService.event.InvoiceEvent;
import com.maperz.invoiceService.model.Invoice;
import com.maperz.invoiceService.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceService {

    private final RestTemplate restTemplate;
    private final InvoiceRepository invoiceRepository;

    public String findInvoice(String orderNumber){
        log.info("Looking for invoice for order number: " + orderNumber);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return invoiceRepository
                        .findByOrderNumber(orderNumber)
                        .orElseThrow(() -> new RuntimeException("Invoice not found"))
                        .getPath();
    }

    public void createInvoice(InvoiceEvent event) {

        // Call user service to get user details
        UserDTO user = restTemplate.getForEntity("http://localhost:8080/user/api/" + event.getOrderNumber(), UserDTO.class).getBody();


        Item[] items =
                event.getOrderItems()
                .stream()
                .map(orderItem -> Item.builder()
                        .name(orderItem.getSkuCode())
                        .quantity(orderItem.getQuantity())
                        .unit_cost(orderItem.getPrice().doubleValue())
                        .build())
                .toArray(Item[]::new);
        InvoiceFields fields = InvoiceFields.builder()
                .tax("%")
                .discounts(false)
                .shipping(true)
                .build();

        InvoiceMaker invoice = InvoiceMaker.builder()
                .from("Maperz Sp. z o.o. \n" +
                        "ul. Graniczna 2E/15 \n" +
                        "54-516, Wrocław \n" +
                        "NIP: 1234567890")
                .to(onInvoiceName(user))
                .number(invoiceNumberGenerator())
                .logo("https://images.unsplash.com/photo-1624797432677-6f803a98acb3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1972&q=80")
                .date(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")))
                .items(items)
                .notes("Thank you for your business.")
                .tax_title("VAT")
                .fields(fields)
                .tax(23)
                .shipping(15)
                .build();

        Invoice invoiceEntity = Invoice.builder()
                .orderNumber(event.getOrderNumber())
                .invoiceNumber(invoice.getNumber())
                .path("C:\\Users\\Maciek\\OneDrive\\Pulpit\\Projekt na Juniora\\invoices\\invoice-" +
                        invoice.getNumber() +
                        ".pdf")
                .build();

        try {
            FileOutputStream fos = new FileOutputStream(invoiceEntity.getPath());
            byte[] bytes = restTemplate.postForEntity("https://invoice-generator.com", invoice, byte[].class).getBody();
            assert bytes != null;
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invoiceRepository.save(invoiceEntity);
        log.info("Invoice for order number: " + event.getOrderNumber() + " has been created");
    }

    private String invoiceNumberGenerator() {
        return "INV-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMM-yyyy-HHmm"));
    }

    private String onInvoiceName(UserDTO user) {
            String invoiceName = user.getName() + "\n" +
                    user.getAddress() + "\n" +
                    user.getZip() + ", " +
                    user.getCity() + "\n";

            if (user.getNip() != null)
                    invoiceName = "NIP: " + user.getNip();

            return invoiceName;
    }
}
