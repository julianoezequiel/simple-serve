package com.topdata.toppontoweb.rest.empresa;

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
import com.topdata.toppontoweb.entity.tipo.TipoDocumento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 * Classe de acesso aos métodos REST para a tolerancias das ocorrências
 *
 * @version 1.0.4 data 08/07/2016
 * @since 1.0.4 data 08/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/tipodocumento")
@Singleton
@Autowire
public class TipoDocumentoRESTController {

    @Autowired
    private TopPontoService topPontoService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public TipoDocumento find(@PathParam("id") Integer id) {
        try {
            return (TipoDocumento) topPontoService.buscar(TipoDocumento.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<TipoDocumento> findAll() {
        try {
            return topPontoService.buscarTodos(TipoDocumento.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
