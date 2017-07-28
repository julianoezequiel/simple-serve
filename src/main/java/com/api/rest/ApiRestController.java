package com.api.rest;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.api.entity.Usuario;
import com.api.service.ServiceException;
import com.api.service.auth.UsuarioService;
import com.api.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ApiRestController {

	public final static Logger LOGGER = LoggerFactory.getLogger(ApiRestController.class.getName());

	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UsuarioService usuarioService;
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ObjectMapper mapper;

	
	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}


	public Usuario buscarRepPorNumSerie(String email) throws ServiceException {
		Usuario usuario = this.usuarioService.buscarPorEmail(email);
		if (usuario == null) {
			throw new ServiceException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
		}
		return usuario;
	}
}
