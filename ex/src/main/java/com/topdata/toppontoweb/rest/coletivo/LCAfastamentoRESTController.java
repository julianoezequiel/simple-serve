package com.topdata.toppontoweb.rest.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.coletivo.LCAfastamentoServices;
import org.springframework.transaction.annotation.Transactional;
//</editor-fold>

/**
 * Classe de acesso aos métodos REST para o lançamento coletivo do afastamento
 * do funcionário
 *
 * @version 1.0.0 data 21/12/2016
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Path("/coletivo/afastamento")
@Singleton
@Autowire
public class LCAfastamentoRESTController implements ColetivoRestInterface {

    @Autowired
    private LCAfastamentoServices lCColetivoServices;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<ColetivoTransfer> listar(@Context HttpServletRequest request) {
        try {
            return lCColetivoServices.listarColetivos(request);
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
            return lCColetivoServices.buscarColetivo(request, id);
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
            return lCColetivoServices.excluirColetivo(request, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/progresso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") long id) {
        final ProgressoTransfer progresso = lCColetivoServices.buscarProgresso(id);
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(progresso.getColetivoResultados() != null && progresso.getColetivoResultados() .size() > 0 ){
            lCColetivoServices.removerProgresso(id);
        }
        
        
        
        return lCColetivoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }

    @POST
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Override
    public Response salvar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer) {
        ProgressoTransfer progresso = lCColetivoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doSalvar(request, coletivoTransfer, progresso);
        });

        thread.start();

        return lCColetivoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }

    private void doSalvar(HttpServletRequest request, ColetivoTransfer list, ProgressoTransfer progress) {
        try {
            lCColetivoServices.salvarColetivo(request, list, progress);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(value = MediaType.APPLICATION_JSON)
    @Produces(value = MediaType.APPLICATION_JSON)
    @Override
    public Response atualizar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer) {
        ProgressoTransfer progresso = lCColetivoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doAtualizar(request, coletivoTransfer, progresso);
        });

        thread.start();

        return lCColetivoServices.getTopPontoResponse().sucessoAtualizar(progresso);
    }

    private void doAtualizar(HttpServletRequest request, ColetivoTransfer coletivoTransfer, ProgressoTransfer progresso) {
        try {
            lCColetivoServices.atualizarColetivo(request, coletivoTransfer, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
