package com.maperz.kafka;

import com.maperz.event.OrderPlacedEvent;
import com.maperz.event.UserConfirmationDTO;
import com.maperz.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
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
        emailService.sendOrderConfirmation(event.getOrderNumber());
        log.info("Received Notification for Order -" + event.getOrderNumber());
    }

    @KafkaListener(topics = "userConfirmationTopic", properties = {"spring.json.value.default.type=com.maperz.event.UserConfirmationDTO"})
    public void handleUserConfirmation(@Payload UserConfirmationDTO event){
        // send out an email notification
        emailService.sendUserConfirmation(event);
    }



}
