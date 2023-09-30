package com.maperz.kafka;

import com.maperz.event.OrderPlacedEvent;
import com.maperz.dto.UserConfirmationDTO;
import com.maperz.service.EmailService;
import com.maperz.service.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class Responder {
    private final EmailService emailService;

    @KafkaListener(topics = "notificationTopic", properties = {"spring.json.value.default.type=com.maperz.event.OrderPlacedEvent"})
    public void handleNotification(OrderPlacedEvent event){
        // send out an email notification
        emailService.sendOrderConfirmation(event.orderNumber());
        log.info("Received Notification for Order -" + event.orderNumber());
    }

    @KafkaListener(topics = "userConfirmationTopic", properties = {"spring.json.value.default.type=com.maperz.dto.UserConfirmationDTO"})
    public void handleUserConfirmation(@Payload UserConfirmationDTO event){
        // send out an email notification
        emailService.sendUserConfirmation(event);
    }



}
