package com.maperz.invoiceService.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItem {
    private String SkuCode;
    private Integer quantity;
    private BigDecimal price;
}
