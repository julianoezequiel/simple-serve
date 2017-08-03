package com.topdata.toppontoweb.rest.jornada;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.jornada.TipoJornada;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import javax.ws.rs.WebApplicationException;

/**
 *
 * @author juliano.ezequiel
 */
@Path("tipojornada")
@Singleton
@Autowire
public class TipoJornadaRESTController {

    @Autowired
    private TopPontoService topPontoService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<TipoJornada> findAll() {
        try {
            return topPontoService.buscarTodos(TipoJornada.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
