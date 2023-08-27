package com.maperz.invoiceService.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String nip;
}
