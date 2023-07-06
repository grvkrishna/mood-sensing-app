package com.grv.moodsensingapp.config.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Indexes;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.GeospatialIndex;
import org.springframework.stereotype.Component;

@Component
public class MongoAutoIndex {

    @Autowired
    MongoTemplate template;

    @PostConstruct
    public void setupIndex()
    {
        MongoCollection<Document> collection = template.getCollection("mood_details");
        collection.createIndex(Indexes.geo2dsphere("location"));
//        template.indexOps("mood_details").ensureIndex( Indexes.geo2dsphere("location") );
    }
}
