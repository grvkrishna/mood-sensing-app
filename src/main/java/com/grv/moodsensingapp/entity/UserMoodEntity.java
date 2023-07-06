package com.grv.moodsensingapp.entity;

import com.grv.moodsensingapp.enums.Mood;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Document("mood_details")
public class UserMoodEntity {

    private String name;

    private Mood mood;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private Locations location;

    private String _id;

    private Integer count;

}
