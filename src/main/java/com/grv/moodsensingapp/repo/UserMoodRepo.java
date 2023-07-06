package com.grv.moodsensingapp.repo;

import com.grv.moodsensingapp.entity.UserMoodEntity;
import com.grv.moodsensingapp.enums.Mood;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserMoodRepo extends MongoRepository<UserMoodEntity, String>, CustomRepo {

    @Aggregation(pipeline = {
            "{'$match': {'name':?0}}",
            "{'$group':{'_id':'$mood','count':{$sum:1}}}",

    })
    List<UserMoodEntity> getFrequnct(String name);


    @Aggregation(pipeline = {
            "{'$match': {'name':?0}}",
            "{'$limit': 1}",
    })
    UserMoodEntity findOneByName(String name);

}