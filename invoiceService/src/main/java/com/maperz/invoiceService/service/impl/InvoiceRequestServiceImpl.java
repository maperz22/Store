package com.maperz.invoiceService.service.impl;

import com.maperz.invoiceService.dto.InvoiceMaker;
import com.maperz.invoiceService.service.InvoiceRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoiceRequestServiceImpl implements InvoiceRequestService {

    private final WebClient webClient;

    public byte[] requestInvoice(InvoiceMaker invoice) {
        return webClient
                .post()
                .uri("https://invoice-generator.com")
                .bodyValue(invoice)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }
}
