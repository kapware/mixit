package com.example.mixit.game;

import com.example.mixit.game.service.GamePlayerDTO;
import com.example.mixit.game.service.PlayerJoinedGameEvent;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GamePlayerIT {


    private CountDownLatch latch = new CountDownLatch(1);

    private PlayerJoinedGameEvent playerJoinedGameEvent;

    @JmsListener(destination = "gamePlayer.q")
    public void receivePlayerJoinedGame(PlayerJoinedGameEvent playerJoinedGameEvent) {
        this.playerJoinedGameEvent = playerJoinedGameEvent;
        latch.countDown();
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void playerCanJoinAGame() throws Exception {
        // given:
        GamePlayerDTO gamePlayer = GamePlayerDTO.builder()
                .gameId(1L)
                .playerId(1L)
                .build();

        // when:
        restTemplate.postForLocation("/api/v1/gameplayer/", gamePlayer);

        // then:
        latch.await(10, TimeUnit.SECONDS);

        assertThat(latch.getCount()).isEqualTo(0L);
        assertThat(playerJoinedGameEvent.getGameId()).isEqualTo(1L);
        assertThat(playerJoinedGameEvent.getPlayerId()).isEqualTo(1L);

    }
}
