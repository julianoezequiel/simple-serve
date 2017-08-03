package com.topdata.toppontoweb.rest.ferramentas.layout;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutArquivoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.ferramentas.layout.LayoutArquivoServices;

/**
 * Classe de acesso aos métodos REST para a Layout eventos
 *
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Path("/layoutArquivo")
@Singleton
@Autowire
public class LayoutArquivoRESTController {

    @Autowired
    private LayoutArquivoServices layoutArquivoServices;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(LayoutArquivoTransfer layoutArquivoTransfer) throws IOException {
        try {
            return this.layoutArquivoServices.salvar(layoutArquivoTransfer);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(LayoutArquivo layoutArquivo) throws IOException {
        try {
            return this.layoutArquivoServices.atualizar(layoutArquivo);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        try {
            return this.layoutArquivoServices.excluir(LayoutArquivo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public LayoutArquivoTransfer find(@PathParam("id") Integer id) {
        try {
            return this.layoutArquivoServices.buscar(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<LayoutArquivoTransfer> findAll() {
        try {
            return this.layoutArquivoServices.buscarTodos();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
