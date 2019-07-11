package com.example.mixit.game;


import com.example.mixit.game.model.Game;
import com.example.mixit.game.model.GameRepository;
import com.example.mixit.game.service.GameDTO;
import com.example.mixit.game.service.GameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @Mock
    GameRepository gameRepository;

    @Test
    public void createGameWithNameMaxPlayers() {
        // given:
        GameDTO game = GameDTO.builder()
                .roomName("Valhalla")
                .maxPlayers(4)
                .build();

        // when:
        GameService gameService = new GameService(gameRepository);
        gameService.createGame(game);

        // then:
        verify(gameRepository, times(1)).save(any());
    }

    @Test
    public void getGameById_found() {
        // given:
        Game example = Game.builder()
                .maxPlayers(4)
                .roomName("Dragon Lair").build();
        when(gameRepository.findById(1L)).thenReturn(Optional.of(example));

        // when:
        GameService gameService = new GameService(gameRepository);
        GameDTO result = gameService.getGame(1L).get();

        // then:
        verify(gameRepository, times(1)).findById(1L);
        assertThat(result.getMaxPlayers()).isEqualTo(4);
        assertThat(result.getRoomName()).isEqualTo("Dragon Lair");
    }

    @Test
    public void getGameById_notFound() {
        // given:
        // when:
        GameService gameService = new GameService(gameRepository);
        Optional<GameDTO> result = gameService.getGame(262161L);

        // then:
        assertThat(result).isEqualTo(Optional.empty());
    }
}