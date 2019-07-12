package com.example.mixit.player;

import com.example.mixit.player.model.Player;
import com.example.mixit.player.model.PlayerRepository;
import com.example.mixit.player.service.PlayerCreatedEvent;
import com.example.mixit.player.service.PlayerDTO;
import com.example.mixit.player.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jms.core.JmsTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
    @Mock
    PlayerRepository playerRepository;

    @Mock
    JmsTemplate jmsTemplate;

    @Test
    public void register_ok() {
        // given:
        PlayerDTO playerDTO = PlayerDTO.builder()
                .alias("Gandalf")
                .email("gandalf@whitemagic.example.com")
                .build();

        // when:
        PlayerService playerService = new PlayerService(playerRepository, jmsTemplate);
        playerService.register(playerDTO);

        // then:
        Player expectedPlayer = Player.builder()
                .alias("Gandalf")
                .email("gandalf@whitemagic.example.com")
                .build();
        PlayerCreatedEvent expectedEvent = PlayerCreatedEvent.builder()
                .email("gandalf@whitemagic.example.com")
                .build();
        verify(playerRepository, times(1)).save(eq(expectedPlayer));
        verify(jmsTemplate, times(1)).
                convertAndSend(eq("player.q"), eq(expectedEvent));
    }
}
