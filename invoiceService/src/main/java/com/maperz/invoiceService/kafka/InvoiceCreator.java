package com.maperz.invoiceService.kafka;

import com.maperz.invoiceService.dto.UserDTO;
import com.maperz.invoiceService.event.InvoiceEvent;
import com.maperz.invoiceService.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j @Component @RequiredArgsConstructor
public class InvoiceCreator {

    private final InvoiceService invoiceService;

    @KafkaListener(topics = "InvoiceTopic")
    public void createInvoiceEvent(InvoiceEvent event){
        // create an invoice
        invoiceService.createInvoice(event);
        log.info("Received Notification for Order -" + event.getOrderNumber());
    }

}
