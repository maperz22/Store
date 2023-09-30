package com.example.userService.service;

import com.example.userService.dto.OrderDTO;
import com.example.userService.dto.UserConfirmationDTO;
import com.example.userService.dto.UserDTO;
import com.example.userService.dto.UserInvoiceDTO;
import com.example.userService.model.Confirmation;
import com.example.userService.model.Order;
import com.example.userService.model.User;
import com.example.userService.model.UserPersonalInfo;
import com.example.userService.repository.ConfirmationRepository;
import com.example.userService.repository.OrderRepository;
import com.example.userService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@Slf4j @RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ConfirmationRepository confirmationRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public User createUser(UserDTO userDTO){
        if (userRepository.existsByEmailIgnoreCase(userDTO.getEmail())){
            log.error("User already exists");
            throw new RuntimeException("User already exists");
        }

        UserPersonalInfo personalInfo = UserPersonalInfo.builder()
                .address(userDTO    .getUserPersonalInfo().getAddress())
                .city(userDTO       .getUserPersonalInfo().getCity())
                .state(userDTO      .getUserPersonalInfo().getState())
                .zip(userDTO        .getUserPersonalInfo().getZip())
                .phone(userDTO      .getUserPersonalInfo().getPhone())
                .build();

        // create user
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .isActive(false)
                .userPersonalInfo(personalInfo)
                .build();



        userRepository.save(user);

        Confirmation confirmation = new Confirmation(user);
        confirmationRepository.save(confirmation);


        log.info("User created");

        // Call notification service to send email with token
        sendConfirmation(UserConfirmationDTO.builder()
                .name(user.getName())
                .email(user.getEmail())
                .token(confirmation.getToken())
                .build());


        return user;
    }

    public Boolean verifyToken(String token){
        if (!confirmationRepository.existsByToken(token) && !confirmationRepository.findByToken(token).get().getUser().getIsActive()){
            log.error("Invalid token");
            throw new RuntimeException("Invalid token");
        } else {
        Confirmation confirmation = confirmationRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid token"));
        User user = confirmation.getUser();
        user.setIsActive(true);
        userRepository.save(user);
        confirmationRepository.delete(confirmation);
        log.info("User confirmed");
        return Boolean.TRUE;
        }
    }

    public void updateOrderHistory(OrderDTO orderDTO){
        User user = userRepository.findByEmailIgnoreCase(orderDTO.getUserEmail()).orElseThrow();
        Order order = Order.builder()
                .orderNumber(orderDTO.getOrderNumber())
                .user(user)
                .build();
        user.getOrders().add(order);
        orderRepository.save(order);
        userRepository.save(user);
        log.info("Order history updated");
    }


    public UserInvoiceDTO findUserByOrderNumber(String orderNumber) {

        User user = userRepository.findByOrdersIsContaining(
                orderRepository.findByOrderNumber(orderNumber).orElseThrow()
        ).orElseThrow();
        log.info("User found");
        return UserInvoiceDTO.builder()
                .email(user.getEmail())
                .name(user.getName())
                .address(user.getUserPersonalInfo().getAddress())
                .city(user.getUserPersonalInfo().getCity())
                .state(user.getUserPersonalInfo().getState())
                .zip(user.getUserPersonalInfo().getZip())
                .build();

    }


    private void sendConfirmation(UserConfirmationDTO event) {

        Message<UserConfirmationDTO> message = MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.TOPIC, "userConfirmationTopic")
                .build();

        log.info("Sending notification to Email Service");
        kafkaTemplate.send(message);
    }

}
