package com.maperz.invoiceService.dto;

public record InvoiceMaker(
        String from,
        String to,
        String logo,
        String number,
        String date,
        Item[] items,
        String notes,
        String tax_title,
        InvoiceFields fields,
        Integer tax,
        Integer shipping) {
}
