package com.example.mixit.player;

import com.example.mixit.player.service.PlayerCreatedEvent;
import com.example.mixit.player.service.PlayerDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlayerIT {
    @Autowired
    private MockMvc mockMvc;

    private CountDownLatch latch = new CountDownLatch(1);

    private PlayerCreatedEvent playerCreatedEvent;

    @JmsListener(destination = "player.q")
    public void receivePlayerCreated(PlayerCreatedEvent playerCreatedEvent) {
        this.playerCreatedEvent = playerCreatedEvent;
        latch.countDown();
    }

    @Test
    public void playerCreate() throws Exception {
        // given:
        PlayerDTO player = PlayerDTO.builder()
                .alias("Bilbo Baggins")
                .email("bilbo@hairyfeet.example.com")
                .build();
        String playerJson = new ObjectMapper().writeValueAsString(player);

        // when:
        String newPlayerUrl = mockMvc.perform(post("/api/v1/player")
                .content(playerJson)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andReturn().getResponse().getHeader("Location");

        // then:
        mockMvc.perform(get(newPlayerUrl))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.email", is("bilbo@hairyfeet.example.com")))
                .andExpect(jsonPath("$.alias", is("Bilbo Baggins")))
        ;
        // and:
        latch.await(10, TimeUnit.SECONDS);
        assertThat(latch.getCount()).isEqualTo(0);
        assertThat(playerCreatedEvent.getEmail()).isEqualTo("bilbo@hairyfeet.example.com");
    }

    @Test
    public void response404ForMissingPlayer() throws Exception {
        mockMvc.perform(get("/api/v1/player/11231"))
                .andExpect(status().is(404));
    }
}
