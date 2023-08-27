package com.maperz.invoiceService.controller;


import com.maperz.invoiceService.dto.InvoicePath;
import com.maperz.invoiceService.service.InvoiceService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;

    @GetMapping("/{OrderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public String getInvoice(@PathVariable String OrderNumber){
        return service.findInvoice(OrderNumber);
    }

}
