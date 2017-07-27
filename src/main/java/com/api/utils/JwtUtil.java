package com.api.utils;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.api.contantes.CONSTANTES;
import com.api.entity.Usuario;
import com.api.service.ServiceException;
import com.api.service.auth.UsuarioService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	@Autowired
	private UsuarioService usuarioService;
	
	public Usuario parseToken(String token) throws ServiceException {
		try {
			if (token == null || token.equals("") || token.length() < 10) {
				// throw new ServiceException(HttpStatus.UNAUTHORIZED);
				return null;
			}
			Claims body = Jwts.parser().setSigningKey(CONSTANTES.AUTH_KEY).parseClaimsJws(token).getBody();
			
			Usuario usuario = new Usuario();
			usuario.setEmai(body.getSubject());
			
			return usuarioService.buscarPorEmail(usuario.getEmai());

		} catch (JwtException | ClassCastException e) {
			throw  new ServiceException(HttpStatus.UNAUTHORIZED);
		}
	}

	public String generateToken(String serie) {
		// TOKEN com 10 minuto de validade
		return Jwts.builder().setSubject(serie).signWith(SignatureAlgorithm.HS256, CONSTANTES.AUTH_KEY)
				.setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 24)).compact();
	}

	public Usuario extractToken(HttpServletRequest request) throws ServiceException {
		return this.parseToken(request.getHeader("Authorization"));

	}

}
