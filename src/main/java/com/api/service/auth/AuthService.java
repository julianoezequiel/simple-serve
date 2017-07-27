package com.api.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.dto.TokenDTO;
import com.api.dto.UsuarioDTO;
import com.api.entity.Usuario;
import com.api.service.ApiService;
import com.api.service.ServiceException;
import com.api.utils.JwtUtil;

/**
 * Classe responsável pela autenticação do Rep no servidor
 * 
 * @author juliano.ezequiel
 *
 */
@Service
public class AuthService extends ApiService {

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UsuarioService usuarioService;

	private String token;

	/**
	 * Metodo para autenticar e criar o Token de acesso para o Rep.
	 * 
	 * @param repDTO
	 * @return TokenDTO
	 * @throws ServiceException
	 */
	public TokenDTO autenticar(UsuarioDTO usuarioDTO) throws ServiceException {

		LOGGER.info("Login usuário : " + usuarioDTO.getEmail());
		// Campo obrigatório
		if (usuarioDTO.getEmail() == null || usuarioDTO.getSenha() == null) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
		}

		// busca o usuario na base
		Usuario usuario = this.usuarioService.buscarPorEmail(usuarioDTO.getEmail());

		if (usuario.getSenha().equals(usuarioDTO.getSenha())) {
			token = jwtUtil.generateToken(usuario.getEmai());
		} else {
			throw new ServiceException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
		}

		ApiService.LOGGER.info("Token criado : " + token);

		TokenDTO tokenDTO = new TokenDTO(token);

		return tokenDTO;
	}

}
