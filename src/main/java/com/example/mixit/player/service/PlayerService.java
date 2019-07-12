package com.example.mixit.player.service;

import com.example.mixit.player.model.Player;
import com.example.mixit.player.model.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerService {
    private final PlayerRepository playerRepository;

    public Long register(PlayerDTO player) {
        Player entity = Player.builder()
                .email(player.getEmail())
                .alias(player.getAlias())
                .build();
        playerRepository.save(entity);

        return entity.getId();
    }

    public Optional<PlayerDTO> getPlayer(Long id) {
        Optional<Player> entity = playerRepository.findById(id);
        if (entity.isPresent()) {
            PlayerDTO game = PlayerDTO.builder()
                    .alias(entity.get().getAlias())
                    .email(entity.get().getEmail())
                    .build();
            return Optional.of(game);
        }
        return Optional.empty();
    }
}
