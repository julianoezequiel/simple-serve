package com.topdata.toppontoweb.rest.configuracoes;

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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuaisPK;
import com.topdata.toppontoweb.services.configuracoes.SequenciaPercentuaisServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.5 data 26/07/2016
 * @since 1.0.5 data 26/07/2016
 *
 * @author juliano.ezequiel
 */
@Path("/sequenciapercentuais")
@Singleton
@Autowire
public class SequenciaPercentuaisREST {

    @Autowired
    private SequenciaPercentuaisServices sequenciaPercentuaisServices;

    private SequenciaPercentuaisPK getPrimaryKey(PathSegment pathSegment) {

        SequenciaPercentuaisPK key = new SequenciaPercentuaisPK();
        MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        List<String> idSequencia = map.get("idSequencia");
        if (idSequencia != null && !idSequencia.isEmpty()) {
            key.setIdSequencia(new Integer(idSequencia.get(0)));
        }
        List<String> idPercentuaisAcrescimo = map.get("idPercentuaisAcrescimo");
        if (idPercentuaisAcrescimo != null && !idPercentuaisAcrescimo.isEmpty()) {
            key.setIdPercentuaisAcrescimo(new Integer(idPercentuaisAcrescimo.get(0)));
        }
        List<String> idTipodia = map.get("idTipodia");
        if (idTipodia != null && !idTipodia.isEmpty()) {
            key.setIdTipodia(new Integer(idTipodia.get(0)));
        }
        return key;
    }

    public SequenciaPercentuaisREST() {
    }

//    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response create(SequenciaPercentuais entity) {
//        try {
//            return sequenciaPercentuaisServices.salvar(entity);
//        } catch (ServiceException ex) {
//            throw new WebApplicationException(ex.getResponse());
//        }
//    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(List<SequenciaPercentuais> entity) {
        try {
            return sequenciaPercentuaisServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response edit(List<SequenciaPercentuais> entity) {
        try {
            return sequenciaPercentuaisServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    
//    @PUT
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Transactional
//    public Response edit(SequenciaPercentuais entity) {
//        try {
//            return sequenciaPercentuaisServices.atualizar(entity);
//        } catch (ServiceException ex) {
//            throw new WebApplicationException(ex.getResponse());
//        }
//    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") PathSegment id) {
        try {
            com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuaisPK key = getPrimaryKey(id);
            return sequenciaPercentuaisServices.excluir(SequenciaPercentuais.class, key);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SequenciaPercentuais find(@PathParam("id") PathSegment id) {
        try {
            com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuaisPK key = getPrimaryKey(id);
            return sequenciaPercentuaisServices.buscar(SequenciaPercentuais.class, key);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return sequenciaPercentuaisServices.buscarTodos(SequenciaPercentuais.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("percentual/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<SequenciaPercentuais> buscaPorPercetualAcrescimoId(@PathParam("id") Integer id) {
        try {
            return sequenciaPercentuaisServices.buscaPorPercetualAcrescimoId(id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
}
