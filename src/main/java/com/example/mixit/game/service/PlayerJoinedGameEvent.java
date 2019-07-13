package com.example.mixit.game.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class PlayerJoinedGameEvent {
    private Long gameId;
    private Long playerId;
}
