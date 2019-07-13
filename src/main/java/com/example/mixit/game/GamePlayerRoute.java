package com.example.mixit.game;

import com.example.mixit.game.service.GamePlayerDTO;
import com.example.mixit.game.service.PlayerJoinedGameEvent;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
public class GamePlayerRoute extends RouteBuilder {
    @Override
    public void configure() {
        rest().post("/gameplayer/").route().routeId("rest:gameplayer")
                .unmarshal().json(JsonLibrary.Jackson, GamePlayerDTO.class)
                .wireTap("direct:gameplayer");

        from("direct:gameplayer")
                .routeId("gamePlayer")
                .transform().exchange(ex -> {
                    GamePlayerDTO gamePlayer = ex.getMessage().getBody(GamePlayerDTO.class);
                    PlayerJoinedGameEvent event = PlayerJoinedGameEvent.builder()
                            .gameId(gamePlayer.getGameId())
                            .playerId(gamePlayer.getPlayerId())
                            .build();
                    return event;
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader("_type", constant(PlayerJoinedGameEvent.class.getName()))
                .to("amqp:queue:gamePlayer.q")
                .log("Player joined: ${body}")
                ;
    }
}
