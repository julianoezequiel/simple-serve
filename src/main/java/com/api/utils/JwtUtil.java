package com.api.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.api.constantes.CONSTANTES;
import com.api.dto.UsuarioDTO;
import com.api.entity.Usuario;
import com.api.service.ServiceException;
import com.api.service.auth.UsuarioService;

@Component
public class JwtUtil {

    @Autowired
    private UsuarioService usuarioService;

    public UsuarioDTO parseToken(String token) throws ServiceException {
        try {
            if (token == null || token.equals("") || token.length() < 10) {
                // throw new ServiceException(HttpStatus.UNAUTHORIZED);
                return null;
            }
            Claims body = Jwts.parser().setSigningKey(CONSTANTES.AUTH_KEY).parseClaimsJws(token).getBody();

            Usuario usuario = new Usuario();
            usuario.setEmail(body.getSubject());

            usuario = usuarioService.buscarPorEmail(usuario.getEmail());

            UsuarioDTO dto = new UsuarioDTO();
            dto.setEmail(usuario.getEmail());

            return dto;

        } catch (JwtException | ClassCastException e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED);
        }
    }

    public Usuario parseTokenUsuario(String token) throws ServiceException {
        try {
            if (token == null || token.equals("") || token.length() < 10) {
                // throw new ServiceException(HttpStatus.UNAUTHORIZED);
                return null;
            }
            Claims body = Jwts.parser().setSigningKey(CONSTANTES.AUTH_KEY).parseClaimsJws(token).getBody();

            Usuario usuario = new Usuario();
            usuario.setEmail(body.getSubject());

            usuario = usuarioService.buscarPorEmail(usuario.getEmail());

            return usuario;

        } catch (JwtException | ClassCastException e) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED);
        }
    }

    public String generateToken(String email) {
        // TOKEN com 10 minuto de validade
        return Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS256, CONSTANTES.AUTH_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 24)).compact();
    }

    public UsuarioDTO extractToken(HttpServletRequest request) throws ServiceException {
        return this.parseToken(request.getHeader("Authorization"));
    }

    public Usuario extractTokenUsuario(HttpServletRequest request) throws ServiceException {
        return this.parseTokenUsuario(request.getHeader("Authorization"));
    }

}
