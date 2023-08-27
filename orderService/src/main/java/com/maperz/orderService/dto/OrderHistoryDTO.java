package com.maperz.orderService.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class OrderHistoryDTO {
    private String orderNumber;
    private String userEmail;
}
