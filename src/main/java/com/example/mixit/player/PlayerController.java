package com.example.mixit.player;

import com.example.mixit.player.service.PlayerDTO;
import com.example.mixit.player.service.PlayerService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/api/v1/player")
@RequiredArgsConstructor
@Api(value = "Player", description = "Player api", tags = { "Player" })
public class PlayerController {

    private final PlayerService playerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody PlayerDTO player) {
        Long id = playerService.register(player);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<PlayerDTO> get(@PathVariable Long id) {
        return playerService.getPlayer(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
