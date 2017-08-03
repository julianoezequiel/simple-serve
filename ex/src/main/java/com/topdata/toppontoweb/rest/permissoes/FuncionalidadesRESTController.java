package com.topdata.toppontoweb.rest.permissoes;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
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

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.services.permissoes.FuncionalidadesServices;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 * @version 1.0.4 data 07/06/2016
 * @since 1.0.4 data 07/06/2016
 * @author juliano.ezequiel
 */
@Path("/funcionalidades")
@Singleton
@Autowire
public class FuncionalidadesRESTController {

    @Autowired
    private TopPontoService topPontoService;

    @Autowired
    FuncionalidadesServices funcionalidadesServices;

    @POST
    @Path("/funcionalidade/grupo")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response adicionar(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacao) {
        try {
            return funcionalidadesServices.adicionarFuncionalidadeGrupo(funcionalidadesGrupoOperacao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Path("/funcionalidadegrupooperacao")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response atualizarList(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacao) {
        try {
            return funcionalidadesServices.atualizarFuncionalidadeGrupo(funcionalidadesGrupoOperacao);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Funcionalidades find(@PathParam("id") Integer id) {
        try {
            return (Funcionalidades) topPontoService.buscar(Funcionalidades.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String findAll() {
        try {
            return funcionalidadesServices.getFuncionalidades(null);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/grupo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String buscarFuncionalidades(@PathParam("id") Integer id) throws IOException {
        try {
            return funcionalidadesServices.getFuncionalidades(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/modulos")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Modulos> buscarTodasFuncionalidades() throws IOException {
        try {
            return funcionalidadesServices.buscarModulosFuncionalidades();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/modulos/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Modulos buscarModulosPorId(@PathParam("id") Integer id) throws IOException {
        try {
            return funcionalidadesServices.buscarModulosPorId(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
     @GET
    @Path("/tiposauditoria")
    @Produces(MediaType.APPLICATION_JSON)
    public  List<TipoAuditoria> buscarTiposAuditoria() throws IOException {
        try {
            return funcionalidadesServices.buscarTiposAuditoria();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
