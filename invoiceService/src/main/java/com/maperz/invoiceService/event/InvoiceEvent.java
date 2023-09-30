package com.maperz.invoiceService.event;

import com.maperz.invoiceService.dto.OrderItem;
import lombok.Data;

import java.util.List;


public record InvoiceEvent(String orderNumber, List<OrderItem> orderItems) {
}
