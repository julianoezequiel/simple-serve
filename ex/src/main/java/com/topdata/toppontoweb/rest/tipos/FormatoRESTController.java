package com.topdata.toppontoweb.rest.tipos;


import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.services.relatorios.FormatoServices;

/**
 *
 * @author tharle.camargo
 */
@Path("/formato")
@Singleton
@Autowire
public class FormatoRESTController {
    
    @Autowired
    FormatoServices formatoServices;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<FormatoTransfer> findAll() {
        return formatoServices.buscarTodos();
    }
    
}
