package com.example.userService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder @AllArgsConstructor
public class UserPersonalInfoDTO {
    private String address;
    private String city;
    private String state;
    private String zip;
    private String phone;
}
