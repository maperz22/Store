package com.example.userService.dto;

import lombok.Builder;
import lombok.Data;

@Data @Builder
public class UserConfirmationDTO {
    private String name;
    private String email;
    private String token;
}
