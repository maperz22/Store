package com.maperz.invoiceService.event;

import com.maperz.invoiceService.dto.OrderItem;
import lombok.Data;

import java.util.List;


@Data
public class InvoiceEvent {
    private String orderNumber;
    private List<OrderItem> orderItems;
}
