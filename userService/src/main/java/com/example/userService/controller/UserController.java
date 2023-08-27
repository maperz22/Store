package com.example.userService.controller;

import com.example.userService.dto.OrderDTO;
import com.example.userService.dto.UserDTO;
import com.example.userService.dto.UserInvoiceDTO;
import com.example.userService.model.User;
import com.example.userService.response.HttpResponse;
import com.example.userService.service.UserService;
import jakarta.servlet.annotation.HttpConstraint;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/user/api")
@Transactional @RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<HttpResponse> createUser(@RequestBody UserDTO userDTO){
        // create user
        User newUser = userService.createUser(userDTO);
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("user", newUser))
                        .message("User created")
                        .httpStatus(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @GetMapping("/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public UserInvoiceDTO getUser(@PathVariable("orderNumber") String orderNumber){
        // create user
        return userService.findUserByOrderNumber(orderNumber);

                /*
                ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("user", user))
                        .message("User found")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );

                     */
    }

    @GetMapping("/confirm")
    public ResponseEntity<HttpResponse> createUser(@RequestParam("token") String token){
        // create user
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", isSuccess))
                        .message("Account confirmed")
                        .httpStatus(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .build()
        );
    }

    @PutMapping("/updateOrderHistory")
    public ResponseEntity<HttpResponse> updateOrderHistory(@RequestBody OrderDTO orderDTO){
        userService.updateOrderHistory(orderDTO);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(LocalDateTime.now().toString())
                        .data(Map.of("Success", true))
                        .message("Order history updated")
                        .httpStatus(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }

}
