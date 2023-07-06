package com.grv.moodsensingapp.dtos;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultResponseDto {

    int status;

    String error;

    String msg;

    String parameter_name;
}
