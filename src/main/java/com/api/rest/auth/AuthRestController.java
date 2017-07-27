package com.api.rest.auth;

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
	 * @param repDTO
	 * @return {@link TokenDTO}
	 * @throws ServiceException
	 *
	 */
	@RequestMapping(value = CONSTANTES.URL_AUTH_SIGN, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.POST)
	public ResponseEntity<TokenDTO> autenticar(@RequestBody UsuarioDTO usuarioDTO) throws ServiceException {
		return new ResponseEntity<TokenDTO>(this.authService.autenticar(usuarioDTO), HttpStatus.CREATED);
	}


}
