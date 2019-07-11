package com.example.mixit.game;

import com.example.mixit.game.service.GameDTO;
import com.example.mixit.game.service.GameService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/game")
@RequiredArgsConstructor
@Api(value = "Game", description = "Game management api", tags = { "Game" })
public class GameController {
    private final GameService gameService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody GameDTO gameDTO) {
        Long id = gameService.createGame(gameDTO);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GameDTO> get(@PathVariable Long id) {
        return gameService.getGame(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
