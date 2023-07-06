package com.grv.moodsensingapp.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class Locations {

    private String type;

    private List<Double> coordinates;
}
