package com.topdata.toppontoweb.rest.relatorios;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.FormatoTransfer;
import com.topdata.toppontoweb.dto.relatorios.SubTipoRelatorio;
import com.topdata.toppontoweb.services.relatorios.FormatoServices;
import com.topdata.toppontoweb.services.relatorios.SubTipoRelatorioServices;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author tharle.camargo
 */
@Path("/relatorios/tipo")
@Singleton
@Autowire
public class TipoRelatorioRESTController {
    @Autowired
    SubTipoRelatorioServices service;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubTipoRelatorio> findOnlyFilters() {
        return service.buscarTodos(false);
    }
    
    @GET
    @Path("todos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<SubTipoRelatorio> findAll() {
        return service.buscarTodos(true);
    }
}
