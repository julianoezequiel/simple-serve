package com.topdata.toppontoweb.rest.tipos;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 * @version 1.0.5 data 26/07/2016
 * @since 1.0.5 data 26/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/tipodia")
@Singleton
@Autowire
public class TipoDiaRESTController {

    @Autowired
    private TopPontoService topPontoService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public TipoDia find(@PathParam("id") Integer id) {
        try {
            return (TipoDia) topPontoService.buscar(TipoDia.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<TipoDia> findAll() {
        try {
            return topPontoService.buscarTodos(TipoDia.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
