package com.maperz.orderService.event;

import com.maperz.orderService.model.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data @Builder
public class InvoiceEvent {
    private String orderNumber;
    private List<OrderItem> orderItems;
}
