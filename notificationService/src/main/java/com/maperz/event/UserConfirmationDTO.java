package com.maperz.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @AllArgsConstructor
@NoArgsConstructor
public class UserConfirmationDTO {
    private String name;
    private String email;
    private String token;
}
