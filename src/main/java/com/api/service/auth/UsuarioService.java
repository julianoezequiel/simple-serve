package com.api.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.api.dao.UsuarioRepository;
import com.api.dto.datatable.PaginacaoDataTableRetornoTransfer;
import com.api.dto.datatable.PaginacaoDataTableTransfer;
import com.api.entity.Usuario;
import javax.servlet.http.HttpServletRequest;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private AuthService authService;

    public Usuario buscarPorEmail(String email) {
        return this.usuarioRepository.buscarPorEmail(email);
    }

    public PaginacaoDataTableRetornoTransfer listar(PaginacaoDataTableTransfer transfer, HttpServletRequest request) {
        
        return new PaginacaoDataTableRetornoTransfer(transfer.getColumns(), Integer.SIZE, Long.MIN_VALUE, Long.MIN_VALUE);
    }

}
