package com.grv.moodsensingapp.repo;

import com.grv.moodsensingapp.entity.UserMoodEntity;
import com.grv.moodsensingapp.enums.Mood;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;

import java.util.NoSuchElementException;

@Slf4j
public class CustomRepoImpl implements CustomRepo{

    @Autowired
    MongoTemplate mongoTemplate;


    public UserMoodEntity getNearestLocation(String name, Point point){
        Query query = new Query();
        query.addCriteria(Criteria.where("name").is(name));
        query.addCriteria(Criteria.where("mood").is(Mood.happy));

        NearQuery nQuery = NearQuery.near(point);
        nQuery.spherical(true);
        nQuery.limit(1);
        nQuery.query(query);

        try {
            GeoResults<UserMoodEntity> results = mongoTemplate.geoNear(nQuery, UserMoodEntity.class, "mood_details", UserMoodEntity.class);

            return results.getContent().get(0).getContent();
        }catch (IndexOutOfBoundsException e){
            throw new IndexOutOfBoundsException("No_Near_Found");
        }
    }
}
