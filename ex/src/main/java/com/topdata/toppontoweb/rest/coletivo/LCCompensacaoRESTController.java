package com.topdata.toppontoweb.rest.coletivo;

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
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.coletivo.LCCompensacaoServices;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import org.springframework.transaction.annotation.Transactional;

/**
 * Classe de acesso aos métodos REST para o lançamento coletivo da compencação
 * do funcionário
 *
 * @version 1.0.0 data 21/12/2016
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Path("/coletivo/compensacao")
@Singleton
@Autowire
public class LCCompensacaoRESTController implements ColetivoRestInterface {

    @Autowired
    private LCCompensacaoServices lCCompensacaoServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response salvar(@Context HttpServletRequest request, ColetivoTransfer list) {
        ProgressoTransfer progresso = lCCompensacaoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doSalvar(request, list, progresso);
        });

        thread.start();

        return lCCompensacaoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }
    
    
    public void doSalvar(@Context HttpServletRequest request, ColetivoTransfer list, ProgressoTransfer progresso) {
        try {
            lCCompensacaoServices.salvarColetivo(request, list, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response atualizar(@Context HttpServletRequest request, ColetivoTransfer list) {
        ProgressoTransfer progresso = lCCompensacaoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doAtualizar(request, list, progresso);
        });

        thread.start();

        return lCCompensacaoServices.getTopPontoResponse().sucessoAtualizar(progresso);
    }
    @GET
    @Path("/progresso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") long id) {
        final ProgressoTransfer progresso = lCCompensacaoServices.buscarProgresso(id);
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(progresso.getColetivoResultados() != null && progresso.getColetivoResultados() .size() > 0 ){
            lCCompensacaoServices.removerProgresso(id);
        }
        
        
        
        return lCCompensacaoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }
    
    
    
    public void doAtualizar(@Context HttpServletRequest request, ColetivoTransfer list, ProgressoTransfer progresso) {
        try {
            lCCompensacaoServices.atualizarColetivo(request, list, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<ColetivoTransfer> listar(@Context HttpServletRequest request) {
        try {
            return lCCompensacaoServices.listarColetivos(request);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public ColetivoTransfer listar(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            return lCCompensacaoServices.buscarColetivo(request, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response excluir(@Context HttpServletRequest request, @PathParam("id") Integer id) {
        try {
            return lCCompensacaoServices.excluirColetivo(request, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
