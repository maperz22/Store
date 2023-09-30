package com.maperz.invoiceService.dto;

import java.math.BigDecimal;

public record OrderItem(String skuCode, Integer quantity, BigDecimal price) {
}
