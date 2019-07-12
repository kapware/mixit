package com.example.mixit.player;

import com.example.mixit.player.model.Player;
import com.example.mixit.player.model.PlayerRepository;
import com.example.mixit.player.service.PlayerDTO;
import com.example.mixit.player.service.PlayerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class PlayerServiceTest {
    @Mock
    PlayerRepository playerRepository;

    @Test
    public void register_ok() {
        // given:
        PlayerDTO playerDTO = PlayerDTO.builder()
                .alias("Gandalf")
                .email("gandalf@whitemagic.example.com")
                .build();

        // when:
        PlayerService playerService = new PlayerService(playerRepository);
        playerService.register(playerDTO);

        // then:
        Player expectedPlayer = Player.builder()
                .alias("Gandalf")
                .email("gandalf@whitemagic.example.com")
                .build();
        verify(playerRepository, times(1)).save(eq(expectedPlayer));
    }
}
