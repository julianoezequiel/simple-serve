package com.api.rest.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.PermissaoDTO;
import com.api.entity.Permissao;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.auth.PermissoesService;
import java.util.List;

@RestController
public class PermissoesRestController extends ApiRestController {

    @Autowired
    private PermissoesService permissoesService;

    @RequestMapping(value = "teste/permissoes", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<Permissao>> buscarTodosModulos() throws ServiceException {
        return new ResponseEntity<>(this.permissoesService.buscarTodasPermissoes(), HttpStatus.OK);
    }

    @RequestMapping(value = "teste/permissoesDTO", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<List<PermissaoDTO>> buscarModulosDTO(HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.permissoesService.buscarTodasPermissoesDTO(), HttpStatus.OK);
    }

}
