package com.api.rest;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.api.dto.RespostaDTO;
import com.api.service.ServiceException;

@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<?> handleException(ServiceException se) {
        return new ResponseEntity<>(new RespostaDTO(se.getMessage(), se.getHttpStatus()),
                se.getHttpStatus());
    }

}
