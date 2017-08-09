package com.api.rest.usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.dto.TokenDTO;
import com.api.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.api.dto.datatable.PaginacaoDataTableTransfer;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.auth.UsuarioService;
import javax.servlet.http.HttpServletRequest;

@RestController
public class UsuarioRestController extends ApiRestController {

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Lista os usuários do sistema
     *
     * @param transfer
     * @param request
     * @return {@link TokenDTO}
     * @throws ServiceException
     *
     */
    @RequestMapping(value = "restrict/usuarios/paginacao", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ResponseEntity<PaginacaoDataTableRetornoTransfer> listar(@RequestBody PaginacaoDataTableTransfer transfer, HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.usuarioService.listar(transfer, request), HttpStatus.CREATED);
    }

}
