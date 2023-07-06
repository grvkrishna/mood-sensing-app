package com.grv.moodsensingapp.dtos;

import com.grv.moodsensingapp.enums.Mood;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Builder
public class UserMood {

    @NotBlank
    private String name;

    @NotNull
    private Mood mood;

    @NotNull
    @DecimalMax(value = "180",message = "longitude should in rage of -180 to 180")
    @DecimalMin(value = "-180",message = "longitude should in rage of -180 to 180")
    private Double longitude;

    @NotNull
    @DecimalMax(value = "90",message = "latitude should in rage of -90 to 90")
    @DecimalMin(value = "-90",message = "latitude should in rage of -90 to 90")
    private Double latitude;
}
