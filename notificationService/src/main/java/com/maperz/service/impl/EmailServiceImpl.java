package com.maperz.service;

import com.maperz.dto.UserDTO;
import com.maperz.dto.UserConfirmationDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final WebClient.Builder webClient;

    @Async
    @Override
    public void sendOrderConfirmation(String orderNumber) {

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        UserDTO user = webClient
                .build()
                .get()
                .uri("http://user-service/user/api/" + orderNumber)
                .retrieve()
                .bodyToMono(UserDTO.class)
                .block();

                //restTemplate.getForEntity("http://localhost:8080/user/api/" + orderNumber, UserDTO.class).getBody();

        String invoice = webClient
                .build()
                .get()
                .uri("http://invoice-service/api/invoice/" + orderNumber)
                .retrieve()
                .bodyToMono(String.class)
                .block();

                //restTemplate.getForEntity("http://localhost:8080/api/invoice/" + orderNumber, String.class).getBody();


        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setSubject("Potwierdzenie zamówienia");
            helper.setTo(user.email());
            helper.setText("Dziękujemy za zakupy w naszym sklepie. Poniżej znajduje się faktura za zamówienie nr " + orderNumber + ".\n" +
                    "W razie pytań prosimy o kontakt z naszym działem obsługi klienta.\n" +
                    "Pozdrawiamy,\n" +
                    "Zespół Maperz");
            FileSystemResource file = new FileSystemResource(invoice);
            helper.addAttachment("invoice.pdf", file);
            javaMailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

    @Async
    @Override
    public void sendUserConfirmation(UserConfirmationDTO userConfirmationDTO) {

        try {
            Context context = new Context();
            context.setVariables(Map.of(
                    "name", userConfirmationDTO.name(),
                    "url", "http://localhost:8080/user/api/confirm?token=" + userConfirmationDTO.token()));
            String text = templateEngine.process("emailConfirm", context);
            MimeMessage msg = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
            helper.setFrom("megaczader@gmail.com");
            helper.setSubject("Potwierdzenie rejestracji");
            helper.setTo(userConfirmationDTO.email());
            helper.setText(text, true);
            javaMailSender.send(msg);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
