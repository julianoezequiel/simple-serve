package com.api.service.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.api.dto.TokenDTO;
import com.api.dto.UsuarioDTO;
import com.api.dto.UsuarioLoginDTO;
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
     * @param usuarioLoginDTO
     * @return TokenDTO
     * @throws ServiceException
     */
    public TokenDTO autenticar(UsuarioLoginDTO usuarioLoginDTO) throws ServiceException {

        LOGGER.info("Login usuário : " + usuarioLoginDTO.getEmail());
        // Campo obrigatório
        if (usuarioLoginDTO.getEmail() == null || usuarioLoginDTO.getSenha() == null) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        // busca o usuario na base
        Usuario usuario = this.usuarioService.buscarPorEmail(usuarioLoginDTO.getEmail());

        if (usuario != null && usuario.getSenha().equals(usuarioLoginDTO.getSenha())) {
            token = jwtUtil.generateToken(usuario.getEmail());
        } else {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, "Usuário ou senha inválidos");
        }

        ApiService.LOGGER.info("Token criado : " + token);

        TokenDTO tokenDTO = new TokenDTO(token);

        return tokenDTO;
    }

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
