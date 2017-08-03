package com.topdata.toppontoweb.rest.coletivo;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
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
import com.topdata.toppontoweb.services.coletivo.LCBHFechamentoServices;
import com.topdata.toppontoweb.dto.ColetivoTransfer;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.services.coletivo.LCBHManutencaoServices;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.Context;
import org.springframework.transaction.annotation.Transactional;
//</editor-fold>

/**
 * Classe de acesso aos métodos REST para o lançamento coletivo do fechamento do
 * banco de horas
 *
 * @version 1.0.0 data 03/07/2017
 * @since 1.0.0 data 03/07/2017
 *
 * @author juliano.ezequiel
 */
@Path("/coletivo/bancohoras/fechamento")
@Singleton
@Autowire
public class LCBHFechamentoRESTController implements ColetivoRestInterface {

    @Autowired
    private LCBHFechamentoServices lCBHFechamentoServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response salvar(@Context HttpServletRequest request, ColetivoTransfer list) {
        ProgressoTransfer progresso = lCBHFechamentoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doSalvar(request, list, progresso);
        });

        thread.start();

        return lCBHFechamentoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }

    public void doSalvar(@Context HttpServletRequest request, ColetivoTransfer list, ProgressoTransfer progresso) {
        try {
            lCBHFechamentoServices.salvarColetivo(request, list, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response atualizar(@Context HttpServletRequest request, ColetivoTransfer list) {
        ProgressoTransfer progresso = lCBHFechamentoServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doAtualizar(request, list, progresso);
        });

        thread.start();

        return lCBHFechamentoServices.getTopPontoResponse().sucessoAtualizar(progresso);
    }

    public void doAtualizar(@Context HttpServletRequest request, ColetivoTransfer list, ProgressoTransfer progresso) {
        try {
            lCBHFechamentoServices.atualizarColetivo(request, list, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/progresso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") long id) {
        final ProgressoTransfer progresso = lCBHFechamentoServices.buscarProgresso(id);

        //Existe resultado? Então é a ultima vez que está sendo chamado
        if (progresso.getColetivoResultados() != null && progresso.getColetivoResultados().size() > 0) {
            lCBHFechamentoServices.removerProgresso(id);
        }

        return lCBHFechamentoServices.getTopPontoResponse().sucessoSalvar(progresso);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<ColetivoTransfer> listar(@Context HttpServletRequest request) {
        try {
            return lCBHFechamentoServices.listarColetivos(request);
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
            return lCBHFechamentoServices.buscarColetivo(request, id);
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
            return lCBHFechamentoServices.excluirColetivo(request, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

}
