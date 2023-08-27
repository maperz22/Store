package com.maperz.invoiceService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class InvoiceMaker {
    String from;
    String to;
    String logo;
    String number;
    String date;
    Item[] items;
    String notes;
    String tax_title;
    InvoiceFields fields;
    Integer tax;
    Integer shipping;
}
