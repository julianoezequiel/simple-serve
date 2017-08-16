package com.api.rest.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.ModuloDTO;
import com.api.entity.Modulo;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.auth.ModuloService;
import java.util.List;

@RestController
public class ModuloRestController extends ApiRestController {

    @Autowired
    private ModuloService moduloService;

    @RequestMapping(value = "teste/modulo", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<Modulo>> buscarTodosModulos() throws ServiceException {
        return new ResponseEntity<>(this.moduloService.buscarTodosModulos(), HttpStatus.OK);
    }

    @RequestMapping(value = "teste/moduloDTO", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<ModuloDTO>> buscarModulosDTO(HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.moduloService.buscarTodosModulosDTO(), HttpStatus.OK);
    }

}
