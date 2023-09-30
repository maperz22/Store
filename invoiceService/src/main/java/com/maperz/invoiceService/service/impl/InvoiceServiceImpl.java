package com.maperz.invoiceService.service.impl;

import com.maperz.invoiceService.dto.*;
import com.maperz.invoiceService.event.InvoiceEvent;
import com.maperz.invoiceService.model.Invoice;
import com.maperz.invoiceService.repository.InvoiceRepository;
import com.maperz.invoiceService.service.InvoiceRequestService;
import com.maperz.invoiceService.service.InvoiceService;
import com.maperz.invoiceService.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final UserInfoService userInfoService;
    private final InvoiceRequestService invoiceRequestService;

    private final String shopName = "Maperz Sp. z o.o. \n" +
            "ul. Pierwszalepsza 123 \n" +
            "12-345, Wrocław \n" +
            "NIP: 1234567890";

    private final String logo = "https://images.unsplash.com/photo-1624797432677-6f803a98acb3?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1972&q=80";

    public String findInvoice(String orderNumber) {
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
        UserDTO user = userInfoService.getUserInfoByOrderNumber(event.orderNumber());

        Item[] items =
                event.orderItems()
                .stream()
                .map(orderItem -> new Item(
                        orderItem.skuCode(),
                        orderItem.quantity(),
                        orderItem.price().doubleValue()))
                .toArray(Item[]::new);
        InvoiceFields fields = new InvoiceFields("%", false, true);
        InvoiceMaker invoice = new InvoiceMaker(
                shopName,
                onInvoiceName(user),
                logo,
                invoiceNumberGenerator(),
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")),
                items,
                "Thank you for your business.",
                "VAT",
                fields,
                23,
                15);


        Invoice invoiceEntity = Invoice.builder()
                .orderNumber(event.orderNumber())
                .invoiceNumber(invoice.number())
                .path("C:\\Users\\Maciek\\OneDrive\\Pulpit\\Projekt na Juniora\\invoices\\invoice-" +
                        invoice.number() +
                        ".pdf")
                .build();

        try {
            FileOutputStream fos = new FileOutputStream(invoiceEntity.getPath());
            byte[] bytes = invoiceRequestService.requestInvoice(invoice);
            assert bytes != null;
            fos.write(bytes);
            fos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        invoiceRepository.save(invoiceEntity);
        log.info("Invoice for order number: " + event.orderNumber() + " has been created");
    }

    private String invoiceNumberGenerator() {
        return "INV-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMM-yyyy-HHmm"));
    }

    private String onInvoiceName(UserDTO user) {
            String invoiceName = user.name() + "\n" +
                    user.address() + "\n" +
                    user.zip() + ", " +
                    user.city() + "\n";

            if (user.nip() != null)
                    invoiceName = "NIP: " + user.name();

            return invoiceName;
    }
}
