package com.maperz.orderService.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data @Builder
public class OrderItemDTO {
    private String SkuCode;
    private BigDecimal price;
    private Integer quantity;
}
