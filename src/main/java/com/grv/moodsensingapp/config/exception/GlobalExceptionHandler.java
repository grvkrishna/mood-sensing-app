package com.grv.moodsensingapp.config.exception;

import com.grv.moodsensingapp.dtos.DefaultResponseDto;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public DefaultResponseDto methodArgumentNotValidException(MethodArgumentNotValidException e){
        log.info(e.getLocalizedMessage());
        DefaultResponseDto dto = DefaultResponseDto.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .parameter_name(e.getFieldError().getField())
                .msg(e.getFieldError().getDefaultMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();

        return dto;
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public DefaultResponseDto indexOutOfBoundsException(IndexOutOfBoundsException e){
        DefaultResponseDto dto = null;
        if (e.getLocalizedMessage().equals("No_Near_Found")){
             dto = DefaultResponseDto.builder()
                    .error(HttpStatus.NOT_FOUND.name())
                    .msg("No near Happy request found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();

        }
        return dto;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public DefaultResponseDto constraintViolationException(ConstraintViolationException e){
        DefaultResponseDto dto = DefaultResponseDto.builder()
                .error(HttpStatus.BAD_REQUEST.name())
                .msg(e.getLocalizedMessage())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return dto;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    public DefaultResponseDto illegalArgumentException(IllegalArgumentException e){
        DefaultResponseDto dto = null;
        if (e.getLocalizedMessage().equals("name_not_added")){
            dto = DefaultResponseDto.builder()
                    .error(HttpStatus.BAD_REQUEST.name())
                    .msg("Name not in our database. Please check and try again")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .build();

        }
        return dto;
    }
}
