package com.maperz.invoiceService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class Item {
    private String name;
    private String description;
    private int quantity;
    private double unit_cost;
}
