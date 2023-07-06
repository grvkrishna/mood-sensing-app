package com.grv.moodsensingapp.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MoodFrequency {

    Integer happy;
    Integer sad;
    Integer neutral;

}
