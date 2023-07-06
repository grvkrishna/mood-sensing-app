package com.grv.moodsensingapp;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class MoodSensingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoodSensingAppApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() { return  new ModelMapper(); }


}
