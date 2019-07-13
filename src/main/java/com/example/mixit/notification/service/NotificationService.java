package com.example.mixit.notification.service;

import com.example.mixit.player.service.PlayerCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final RestTemplate restTemplate;

    @Value("${mail.server.api}")
    private String mailServerApi;

    @JmsListener(destination = "player.q")
    public void playerCreatedHandler(PlayerCreatedEvent event) {
        MailServerRequest mail = MailServerRequest.builder()
                .to(event.getEmail())
                .subject("Welcome to Mixit!")
                .body("Great to have you here!")
                .build();

        restTemplate.postForLocation(mailServerApi, mail);
    }
}
