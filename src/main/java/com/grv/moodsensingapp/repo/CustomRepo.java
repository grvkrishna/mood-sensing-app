package com.grv.moodsensingapp.repo;

import com.grv.moodsensingapp.entity.UserMoodEntity;
import org.springframework.data.geo.Point;

public interface CustomRepo {

    public UserMoodEntity getNearestLocation(String name, Point point);
}
