package com.topdata.toppontoweb.rest.funcionario;

import java.util.List;

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
import com.topdata.toppontoweb.entity.tipo.TipoMotivo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.motivo.TipoMotivoService;

/**
 * @version 1.0.0 data 05/09/2016
 * @since 1.0.0 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Path("/tipoMotivo")
@Singleton
@Autowire
public class TipoMotivoRESTController {

    @Autowired
    private TipoMotivoService tipoMotivoService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public TipoMotivo find(@PathParam("id") Integer id) {
        try {
            return tipoMotivoService.buscar(TipoMotivo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/descricao/{desc}")
    @Produces(MediaType.APPLICATION_JSON)
    public TipoMotivo buscaPorDescricao(@PathParam("desc") String descricao) {
        try {
            return tipoMotivoService.buscaPorDescricao(descricao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Entidade> findAll() {
        try {
            return tipoMotivoService.buscarTodos(TipoMotivo.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
