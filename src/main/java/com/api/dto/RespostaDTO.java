package com.api.dto;

import com.api.constantes.CONSTANTES;
import com.api.converter.GenericDTO;
import org.springframework.http.HttpStatus;

/**
 *
 * @author juliano.ezequiel
 */
public class RespostaDTO {

    public String content;
    public HttpStatus httpStatus = HttpStatus.OK;
    public String hideDelay = "3000";
    public String position = "bottom right";
    public String theme = CONSTANTES.SUCCESS;
    public GenericDTO genericDTO;
    public Object object;

    public RespostaDTO() {
    }

    public RespostaDTO(String message, HttpStatus httpStatus) {
        this.content = message;
        this.httpStatus = httpStatus;
    }

    public RespostaDTO(GenericDTO genericDTO, HttpStatus httpStatus) {
        this.genericDTO = genericDTO;
        this.httpStatus = httpStatus;
    }

    public RespostaDTO(GenericDTO genericDTO, String message) {
        this.content = message;
        this.genericDTO = genericDTO;
    }

    public RespostaDTO(GenericDTO genericDTO) {
        this.genericDTO = genericDTO;
    }

}
