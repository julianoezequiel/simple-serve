package com.api.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.dao.UsuarioRepository;
import com.api.entity.Usuario;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	public Usuario buscarPorEmail(String email){
		return this.usuarioRepository.buscarPorEmail(email);
	}

}
