package com.topdata.toppontoweb.rest.sistema;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Sistema;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.SistemaService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tharle.camargo
 */
@Path("/sistema")
@Singleton
@Autowire
public class SistemaRESTController {
    @Autowired
    private SistemaService service;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Sistema buscarDados() throws ServiceException {
        return service.buscar();
    }
}
