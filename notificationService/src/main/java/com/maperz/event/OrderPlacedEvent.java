package com.maperz.event;

import lombok.Data;

@Data
public class OrderPlacedEvent {
    private String orderNumber;
}
