package com.grv.moodsensingapp.services;


import com.grv.moodsensingapp.dtos.MoodFrequency;
import com.grv.moodsensingapp.dtos.UserMood;
import com.grv.moodsensingapp.entity.Locations;
import com.grv.moodsensingapp.entity.UserMoodEntity;
import com.grv.moodsensingapp.repo.UserMoodRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class MoodServiceImpl implements MoodService {

    @Autowired
    UserMoodRepo userMoodRepo;


    public UserMood userMoodStore(UserMood userMood){

        Locations locations = Locations.builder()
                .type("Point")
                .coordinates(Arrays.asList(userMood.getLongitude(), userMood.getLatitude()))
                .build();
        UserMoodEntity userMoodEntity = UserMoodEntity.builder()
                .mood(userMood.getMood())
                .location(locations)
                .name(userMood.getName().trim())
                .build();

        log.info("User Entity after map {}",userMoodEntity.toString());
        UserMoodEntity userMoodEntity1 = userMoodRepo.save(userMoodEntity);
        return UserMood.builder().mood(userMoodEntity1.getMood())
                .longitude(userMoodEntity1.getLocation().getCoordinates().get(0))
                .latitude(userMoodEntity1.getLocation().getCoordinates().get(1))
                .name(userMoodEntity1.getName())
                .build();
    }

    public MoodFrequency getMoodFrequncy(String name){
        UserMoodEntity u = userMoodRepo.findOneByName(name);
        log.info("This  name comes fromdb {}",u);
        if (u == null)
            throw new IllegalArgumentException("name_not_added");
       List<UserMoodEntity> freq =  userMoodRepo.getFrequnct(name);
       int sad=0;
       int happy = 0;
       int neutral = 0;
        for ( UserMoodEntity f : freq ) {
            if (f.get_id().equals("sad"))
                sad = f.getCount();
            if (f.get_id().equals("happy"))
                happy = f.getCount();
            if (f.get_id().equals("neutral"))
                neutral = f.getCount();

        }

       return  MoodFrequency.builder()
                       .happy(happy)
                               .sad(sad)
                                       .neutral(neutral)
                                               .build();

    }


    public UserMood getNearesHappyLocation(String name, Double longitude,Double latitude ){
        UserMoodEntity u = userMoodRepo.findOneByName(name);
        log.info("This  name comes fromdb {}",u);
        if (u == null)
            throw new IllegalArgumentException("name_not_added");
        Point p = new Point(longitude, latitude);

        UserMoodEntity userMood = userMoodRepo.getNearestLocation(name,p);
        return UserMood.builder()
                .name(userMood.getName())
                .mood(userMood.getMood())
                .longitude(userMood.getLocation().getCoordinates().get(0))
                .latitude(userMood.getLocation().getCoordinates().get(1))
                .build();
    }
}
