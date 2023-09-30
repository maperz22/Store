package com.maperz.service;

import com.maperz.dto.UserConfirmationDTO;

public interface EmailService {

    // Sends an order confirmation email to the user with invoice attached
    void sendOrderConfirmation(String orderNumber);

    // Sends a verification email to the user with a link to verify their email address
    void sendUserConfirmation(UserConfirmationDTO userConfirmationDTO);

}
