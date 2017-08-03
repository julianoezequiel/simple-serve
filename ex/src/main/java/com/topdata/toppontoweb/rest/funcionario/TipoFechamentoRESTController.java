package com.topdata.toppontoweb.rest.funcionario;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento.TipoFechamentoService;
import java.util.List;

/**
 * @version 1.0.0 data 20/06/2017
 * @since 1.0.0 data 20/06/2017
 *
 * @author juliano.ezequiel
 */
@Path("/tipofechamento")
@Singleton
@Autowire
public class TipoFechamentoRESTController {

    @Autowired
    private TipoFechamentoService service;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TipoFechamento find(@PathParam("id") Integer id) {
        try {
            return this.service.buscar(TipoFechamento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> find() {
        try {
            return this.service.buscarTodos(TipoFechamento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
