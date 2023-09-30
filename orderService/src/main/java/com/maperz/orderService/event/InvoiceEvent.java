package com.maperz.orderService.event;

import com.maperz.orderService.model.OrderItem;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public record InvoiceEvent(String orderNumber, List<OrderItem> orderItems) {
}
