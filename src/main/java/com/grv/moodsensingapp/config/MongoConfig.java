package com.grv.moodsensingapp.config;


import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Collections;

@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    private String db;

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Value("${spring.data.mongodb.username}")
    private String username;

    @Value("${spring.data.mongodb.password}")
    private String password;



    @Bean
    MongoTemplate mongoTemplate() { return new MongoTemplate(mongoDatabaseFactory(mongoClient()));}

    @Bean
    @Primary
    MongoDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient){
        return new SimpleMongoClientDatabaseFactory(mongoClient,db);
    }

    @Bean
    MongoClient mongoClient(){
        ConnectionString connectionString = null;
        if (username.isEmpty())
            connectionString = new ConnectionString("mongodb://"+host+":"+port+"/"+db);
        else
            connectionString = new ConnectionString("mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + db);

        var settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return new MongoClientFactory(Collections.emptyList()).createMongoClient(settings);

    }

}
