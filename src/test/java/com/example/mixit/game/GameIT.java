package com.example.mixit.game;

import com.example.mixit.game.service.GameDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GameIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void playerCanCreateMixitGame() throws Exception {
        // given:
        GameDTO game = GameDTO.builder()
                .roomName("Amber Room")
                .maxPlayers(12)
                .build();
        String gameJson = new ObjectMapper().writeValueAsString(game);

        // when:
        String newGameUrl = mockMvc.perform(post("/api/v1/game")
                .content(gameJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn().getResponse().getHeader("Location");

        // then:
        mockMvc.perform(get(newGameUrl))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.roomName", is("Amber Room")))
                .andExpect(jsonPath("$.maxPlayers", is(12)))
        ;
    }

    @Test
    public void response404ForMissingGame() throws Exception {
        mockMvc.perform(get("/api/v1/game/11231"))
                .andExpect(status().is(404));
    }
}
