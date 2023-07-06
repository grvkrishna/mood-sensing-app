package com.grv.moodsensingapp;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grv.moodsensingapp.config.jwt.JwtTokenProvider;
import com.grv.moodsensingapp.controller.MoodController;
import com.grv.moodsensingapp.dtos.MoodFrequency;
import com.grv.moodsensingapp.dtos.UserMood;
import com.grv.moodsensingapp.entity.Locations;
import com.grv.moodsensingapp.entity.UserMoodEntity;
import com.grv.moodsensingapp.enums.Mood;
import com.grv.moodsensingapp.services.MoodService;
import com.grv.moodsensingapp.utils.TestUtils;
import io.jsonwebtoken.Jwts;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = MoodSensingAppApplication.class)
public class UserMoodControllerTest {



    private MockMvc mockMvc;
    @MockBean
    MoodService moodService;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).addFilter(springSecurityFilterChain).build();
    }

    UserMood userMood = UserMood.builder()
            .mood(Mood.happy)
            .latitude(78.0)
            .longitude(78.0)
            .name("test")
            .build();

    Locations locations = Locations.builder()
            .type("Point")
            .coordinates(Arrays.asList(78.0,78.0))
            .build();
    UserMoodEntity userMoodEntity = UserMoodEntity.builder()
            .mood(Mood.happy)
            .name("test")
            .location(locations)
            .build();

    UserMoodEntity userMoodEntity1 = UserMoodEntity.builder()
            ._id("sad")
            .count(1)
            .build();

    UserMoodEntity userMoodEntity2 = UserMoodEntity.builder()
            ._id("happy")
            .count(1)
            .build();

    UserMoodEntity userMoodEntity3 = UserMoodEntity.builder()
            ._id("neutral")
            .count(1)
            .build();
    List<UserMoodEntity> userMoodEntityList = Arrays.asList(userMoodEntity3,userMoodEntity1,userMoodEntity2);

    MoodFrequency moodFrequency  = MoodFrequency.builder()
            .sad(1)
            .neutral(1)
            .happy(1)
            .build();

    @Test
    public void storeMood() throws Exception {
        when(moodService.userMoodStore(userMood)).thenReturn(userMood);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/mood/")
                .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+TestUtils.getJwt())
                .content(new ObjectMapper().writeValueAsString(userMood))
        ).andExpect(status().is2xxSuccessful()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("test"));

    }

    @Test
    public void getFrequency() throws Exception {
        when(moodService.getMoodFrequncy("test")).thenReturn(moodFrequency);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mood/test")

                        .header(HttpHeaders.AUTHORIZATION,"Bearer "+TestUtils.getJwt())
                        .contentType(MediaType.APPLICATION_JSON)

                ).andExpect(status().is2xxSuccessful()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sad").value(1));

    }

    @Test
    public void getNearestLocation() throws Exception {
        when(moodService.getNearesHappyLocation("test",78.1,78.1)).thenReturn(userMood);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mood/test/78.1/78.1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+TestUtils.getJwt())
                ).andExpect(status().is2xxSuccessful()).andDo(print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.latitude").value(78.0));

    }

    @Test
    public void getNearestLocation_notAuth() throws Exception {
        when(moodService.getNearesHappyLocation("test",78.1,78.1)).thenReturn(userMood);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/mood/test/78.1/78.1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().is4xxClientError()).andDo(print());

    }
}
