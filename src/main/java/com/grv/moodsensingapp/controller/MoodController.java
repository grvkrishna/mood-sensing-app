package com.grv.moodsensingapp.controller;


import com.grv.moodsensingapp.dtos.MoodFrequency;
import com.grv.moodsensingapp.dtos.UserMood;
import com.grv.moodsensingapp.services.MoodService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/mood")
@Validated
public class MoodController {


    @Autowired
    MoodService moodService;

    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    public UserMood  storeUserMood(@Valid @RequestBody UserMood uMood){
        moodService.userMoodStore(uMood);
        return uMood;
    }

    @GetMapping("/{name}")
    public MoodFrequency getMoodFrequency(@PathVariable("name") @NotBlank(message = "username can't be black.") String uName){
      return   moodService.getMoodFrequncy(uName);
    }


    @GetMapping("/{name}/{longitude}/{latitude}")
    public UserMood getNearestHappyLoc(@PathVariable("name") @NotBlank(message = "username can't be black.") String uName,
                                       @PathVariable("longitude") @NotNull
                                       @DecimalMax(value = "180",message = "longitude should in rage of -180 to 180")
                                       @DecimalMin(value = "-180",message = "longitude should in rage of -180 to 180") Double longitude,
                                       @PathVariable("latitude") @NotNull
                                           @DecimalMax(value = "90",message = "latitude should in rage of -90 to 90")
                                           @DecimalMin(value = "-90",message = "latitude should in rage of -90 to 90") Double latitude){

       return moodService.getNearesHappyLocation(uName,longitude,latitude);
    }
}
