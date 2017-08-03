package com.topdata.toppontoweb.rest.rep;

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
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.rep.ModeloRep;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.entity.tipo.TipoEquipamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.rep.RepService;

/**
 * Classe de acesso aos métodos REST para o Rep
 *
 * @version 1.0.4 data 22/06/2016
 * @since 1.0.4 data 22/06/2016
 *
 * @author juliano.ezequiel
 */
@Path("/rep")
@Singleton
@Autowire
public class RepRESTController {

    @Autowired
    private RepService repService;

    /**
     * Insere um novo Rep
     *
     * @param rep
     * @return Response contendo o resultado da operação
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Rep rep) {
        try {
            return repService.salvar(rep);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Atualiza um Rep existente
     *
     * @param rep
     * @return Response contendo o resultado da operação
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Rep rep) {
        try {
            return repService.atualizar(rep);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Exclui um Rep pelo seu Id
     *
     * @param id
     * @return Response contendo o resultado da operação
     */
    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return repService.excluir(Rep.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca um Rep pelo seu Id
     *
     * @param id
     * @return Rep
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Rep find(@PathParam("id") Integer id) {
        try {
            return repService.buscar(Rep.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca todos os Reps do sistema
     *
     * @return Lista Entidade
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return repService.buscarTodos(Rep.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca a quantidade de Reps
     *
     * @return String
     */
    @GET
    @Path("qtd")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String count() {
        try {
            return repService.quantidade(Rep.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca os Reps pelo Id da empresa
     *
     * @param id
     * @return Lista Rep
     */
    @GET
    @Path("/grupoacesso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Rep> buscaPorGrupoAcesso(@PathParam("id") Integer id) {
        try {
            return repService.buscaPorGrupoAcesso(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca os modelos disponíveis para o Reps
     *
     * @return Lista ModeloRep
     */
    @GET
    @Path("/modelos")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<ModeloRep> buscarModelosRep() {
        try {
            return repService.buscarModelosRep();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    /**
     * Busca os tipos de equipamentos disponíveis
     *
     * @return Lista TiposEquipamentos
     */
    @GET
    @Path("/tiposequipamentos")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<TipoEquipamento> buscarTiposEquimento() {
        try {
            return repService.buscarTiposEquimento();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
