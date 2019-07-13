package com.example.mixit.notification;

import com.example.mixit.player.service.PlayerCreatedEvent;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;

import java.util.concurrent.Callable;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "mail.server.api=http://localhost:${wiremock.server.port}/send/")
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
public class NotificationIT {
    @Autowired
    JmsTemplate jmsTemplate;

    static Callable<Integer> receivedRequestsNo(String url) {
        return () -> WireMock.findAll(postRequestedFor(urlEqualTo(url))).size();
    }

    @Test
    public void sendWelcomeEmail() {
        // given:
        stubFor(post("/send/").willReturn(aResponse().withStatus(201)));

        // when:
        jmsTemplate.convertAndSend("player.q",
                PlayerCreatedEvent.builder().email("gimli@moria.example.com").id(1L).build());

        await().atMost(5, SECONDS).until(receivedRequestsNo("/send/"), greaterThanOrEqualTo(1));

        // then:
        verify(1, postRequestedFor(urlEqualTo("/send/"))
                .withRequestBody(matchingJsonPath( "$.to", equalTo("gimli@moria.example.com")))
                .withRequestBody(matchingJsonPath( "$.subject", equalTo("Welcome to Mixit!")))
                .withRequestBody(matchingJsonPath( "$.body", equalTo("Great to have you here!")))
        );
    }
}