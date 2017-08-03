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
import com.topdata.toppontoweb.services.coletivo.LCJornadaServices;
import org.springframework.transaction.annotation.Transactional;
//</editor-fold>

/**
 * Classe de acesso aos métodos REST para o lançamento coletivo da jornada do
 * funcionário
 *
 * @version 1.0.0 data 21/12/2016
 * @since 1.0.0 data 21/12/2016
 *
 * @author juliano.ezequiel
 */
@Path("/coletivo/jornada")
@Singleton
@Autowire
public class LCJornadaRESTController implements ColetivoRestInterface {

    @Autowired
    private LCJornadaServices lCJornadaServices;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response salvar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer) {
        ProgressoTransfer progresso = lCJornadaServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doSalvar(request, coletivoTransfer, progresso);
        });

        thread.start();

        return lCJornadaServices.getTopPontoResponse().sucessoSalvar(progresso);
    }
    
    public void doSalvar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer, ProgressoTransfer progresso) {
        try {
            lCJornadaServices.salvarColetivo(request, coletivoTransfer, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @GET
    @Path("/progresso/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response buscarProgresso(@Context HttpServletRequest request, @PathParam("id") long id) {
        final ProgressoTransfer progresso = lCJornadaServices.buscarProgresso(id);
        
        //Existe resultado? Então é a ultima vez que está sendo chamado
        if(progresso.getColetivoResultados() != null && progresso.getColetivoResultados() .size() > 0 ){
            lCJornadaServices.removerProgresso(id);
        }
        
        
        
        return lCJornadaServices.getTopPontoResponse().sucessoSalvar(progresso);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response atualizar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer) {
        ProgressoTransfer progresso = lCJornadaServices.criarProgresso();

        Thread thread = new Thread(() -> {
            doAtualizar(request, coletivoTransfer, progresso);
        });

        thread.start();

        return lCJornadaServices.getTopPontoResponse().sucessoAtualizar(progresso);
    }

    public void doAtualizar(@Context HttpServletRequest request, ColetivoTransfer coletivoTransfer, ProgressoTransfer progresso) {
        try {
            lCJornadaServices.atualizarColetivo(request, coletivoTransfer, progresso);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public List<ColetivoTransfer> listar(@Context HttpServletRequest request) {
        try {
            return lCJornadaServices.listarColetivos(request);
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
            return lCJornadaServices.buscarColetivo(request, id);
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
            return lCJornadaServices.excluirColetivo(request, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
