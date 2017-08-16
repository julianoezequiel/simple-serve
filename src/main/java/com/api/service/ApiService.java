package com.api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;

@Service
public class ApiService {

    public final static Logger LOGGER = LoggerFactory.getLogger(ApiService.class.getName());

    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public ObjectMapper mapper;

}
