package com.maperz.invoiceService.service.impl;

import com.maperz.invoiceService.dto.UserDTO;
import com.maperz.invoiceService.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserInfoServiceImpl implements UserInfoService {

    private final WebClient.Builder webClient;

    public UserDTO getUserInfoByOrderNumber(String orderNumber) {
        return webClient
                .build()
                .get()
                .uri("http://user-service/user/api/" + orderNumber)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();
    }

}
