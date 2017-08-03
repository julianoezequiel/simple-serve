package com.topdata.toppontoweb.rest.autenticacao;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class UnauthorizedEntryPoint implements AuthenticationEntryPoint {
    
    @Autowired
    private ObjectMapper mapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, WebApplicationException {
//        RedirectStrategy redirectStrategy =  new DefaultRedirectStrategy();
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        
        //Adicionando cabeçalho de Cross-origen        
        //TODO: Verificar se precisa remover esse trecho de código quando não tiver cross-origin
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, PUT, DELETE, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept");
        response.addHeader("Access-Control-Max-Age", "1728000");
        
        Map<String, Object> map= new HashMap<>();
        response.getWriter().write(mapper.writeValueAsString(map));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        redirectStrategy.sendRedirect(request, response, "#/login.html");
    }

}
