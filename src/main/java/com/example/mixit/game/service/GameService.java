package com.example.mixit.game.service;

import com.example.mixit.game.model.Game;
import com.example.mixit.game.model.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {
    public final GameRepository gameRepository;

    public Long createGame(GameDTO game) {
        Game entity = Game.builder()
                .maxPlayers(game.getMaxPlayers())
                .roomName(game.getRoomName())
                .build();
        gameRepository.save(entity);
        return entity.getId();
    }

    public Optional<GameDTO> getGame(Long id) {
        Optional<Game> entity = gameRepository.findById(id);
        if (entity.isPresent()) {
            GameDTO game = GameDTO.builder()
                    .maxPlayers(entity.get().getMaxPlayers())
                    .roomName(entity.get().getRoomName())
                    .build();
            return Optional.of(game);
        }
        return Optional.empty();
    }
}
