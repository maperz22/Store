package com.maperz.orderService.dto;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor @NoArgsConstructor
public class OrderDTO {
    private String userEmail;
    private List<OrderItemDTO> orderItemsList;
}
