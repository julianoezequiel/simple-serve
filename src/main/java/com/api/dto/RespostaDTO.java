package com.api.dto;

import com.api.constantes.CONSTANTES;
import com.api.converter.GenericDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.http.HttpStatus;

/**
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
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
        super();
        this.content = message;
        this.httpStatus = httpStatus;
    }

    public RespostaDTO(GenericDTO genericDTO, HttpStatus httpStatus) {
        super();
        this.genericDTO = genericDTO;
        this.httpStatus = httpStatus;
    }

    public RespostaDTO(GenericDTO genericDTO, String message) {
        super();
        this.content = message;
        this.genericDTO = genericDTO;
    }

    public RespostaDTO(GenericDTO genericDTO) {
        this.genericDTO = genericDTO;
    }

}
