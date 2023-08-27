package com.maperz.invoiceService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class InvoiceFields {
    String tax;
    Boolean discounts;
    Boolean shipping;
}
