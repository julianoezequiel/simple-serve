package com.api.rest.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.api.contantes.CONSTANTES;
import com.api.dto.TokenDTO;
import com.api.dto.UsuarioDTO;
import com.api.dto.UsuarioLoginDTO;
import com.api.rest.ApiRestController;
import com.api.service.ServiceException;
import com.api.service.auth.AuthService;

@RestController
public class AuthRestController extends ApiRestController {

    @Autowired
    private AuthService authService;
    
    /**
     * Método para autenticação do usuário.
     *
     * @param usuarioLoginDTO
     * @return {@link TokenDTO}
     * @throws ServiceException
     *
     */
    @RequestMapping(value = CONSTANTES.URL_AUTH, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
    public ResponseEntity<TokenDTO> autenticar(@RequestBody UsuarioLoginDTO usuarioLoginDTO) throws ServiceException {
        return new ResponseEntity<>(this.authService.autenticar(usuarioLoginDTO), HttpStatus.CREATED);
    }

    @RequestMapping(value = CONSTANTES.URL_USER, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
    public ResponseEntity<UsuarioDTO> autenticar(HttpServletRequest request) throws ServiceException {
        return new ResponseEntity<>(this.authService.userAutenticado(request), HttpStatus.CREATED);
    }

}
