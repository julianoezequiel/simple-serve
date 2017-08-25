package com.api.service;

import com.api.dto.UsuarioDTO;
import com.api.entity.Usuario;
import com.api.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;

@Service
public class ApiService {

    public final static Logger LOGGER = LoggerFactory.getLogger(ApiService.class.getName());

    @Autowired
    public ModelMapper modelMapper;
    @Autowired
    public ObjectMapper mapper;
    @Autowired
    private JwtUtil jwtUtil;

    public UsuarioDTO userAutenticado(HttpServletRequest request) throws ServiceException {
        UsuarioDTO usuario = this.jwtUtil.extractToken(request);
        if (usuario == null) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
        return usuario;
    }

    public Usuario usuarioAutenticado(HttpServletRequest request) throws ServiceException {
        Usuario usuario = this.jwtUtil.extractTokenUsuario(request);
        if (usuario == null) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Token inválido");
        }
        return usuario;
    }

}
