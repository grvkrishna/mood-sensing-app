package com.grv.moodsensingapp;

import com.grv.moodsensingapp.dtos.MoodFrequency;
import com.grv.moodsensingapp.dtos.UserMood;
import com.grv.moodsensingapp.entity.Locations;
import com.grv.moodsensingapp.entity.UserMoodEntity;
import com.grv.moodsensingapp.enums.Mood;
import com.grv.moodsensingapp.repo.UserMoodRepo;
import com.grv.moodsensingapp.services.MoodServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.geo.Point;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserMoodServiceTest {

    @InjectMocks
    MoodServiceImpl moodService;

    @Mock
    UserMoodRepo userMoodRepo;


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

    @Test
    void storeMood(){


        when(userMoodRepo.save(userMoodEntity)).thenReturn(userMoodEntity);
        UserMood userMood1 = moodService.userMoodStore(userMood);

        assertThat(userMood1).usingRecursiveComparison().isEqualTo(userMood);

    }


    @Test
    void getFrequency(){
        MoodFrequency moodFrequency1  = MoodFrequency.builder()
                .sad(1)
                .neutral(1)
                .happy(1)
                .build();
        Locations locations = Locations.builder()
                .type("Point")
                .coordinates(Arrays.asList(78.0,78.0))
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
        when(userMoodRepo.getFrequnct("test")).thenReturn(userMoodEntityList);

        MoodFrequency moodFrequency = moodService.getMoodFrequncy("test");

        assertThat(moodFrequency).usingRecursiveComparison().isEqualTo(moodFrequency1);

    }


    @Test
    void getNearestLocation(){
        Point p = new Point(78.1,78.1);

        when(userMoodRepo.getNearestLocation("test",p)).thenReturn(userMoodEntity);

        UserMood userMood1 = moodService.getNearesHappyLocation("test",78.1,78.1);

        assertThat(userMood1).usingRecursiveComparison().isEqualTo(userMood);
    }
}
