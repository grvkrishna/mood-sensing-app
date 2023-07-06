package com.grv.moodsensingapp.services;

import com.grv.moodsensingapp.dtos.MoodFrequency;
import com.grv.moodsensingapp.dtos.UserMood;

public interface MoodService {

    public UserMood userMoodStore(UserMood userMood);


    public MoodFrequency getMoodFrequncy(String name);

    public UserMood getNearesHappyLocation(String name, Double longitude,Double latitude );
}
