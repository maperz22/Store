package com.example.userService.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserInvoiceDTO {
    private String email;
    private String name;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String nip;
}
