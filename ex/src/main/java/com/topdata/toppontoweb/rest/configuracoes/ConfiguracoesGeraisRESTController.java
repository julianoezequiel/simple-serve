package com.topdata.toppontoweb.rest.configuracoes;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.configuracoes.ConfiguracoesGeraisServices;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tharle.camargo
 */
@Path("/configuracoes/gerais")
@Singleton
@Autowire
public class ConfiguracoesGeraisRESTController {
    @Autowired
    private ConfiguracoesGeraisServices service;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public ConfiguracoesGerais find() {
        try {
            return service.buscarConfiguracoesGerais();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(ConfiguracoesGerais configuracoesGerais) {
        try {
            return service.atualizar(configuracoesGerais);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
