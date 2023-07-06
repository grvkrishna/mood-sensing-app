package com.grv.moodsensingapp.Utility;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Utility {

    @Autowired
    ModelMapper modelMapper;


    public <D, T> D map(T entities, Class<D> dtoClass){
       return modelMapper.map(entities,dtoClass);
    }
}
