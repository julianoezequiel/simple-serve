package com.topdata.toppontoweb.rest.bancoHoras;

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
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimitePK;
import com.topdata.toppontoweb.services.bancoHoras.BancoHorasLimiteServices;
import com.topdata.toppontoweb.services.ServiceException;

/**
 * @version 1.0.4 data 18/08/2016
 * @since 1.0.4 data 18/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/bancohoraslimite")
@Singleton
@Autowire
public class BancoHorasLimiteRESTController {

    @Autowired
    private BancoHorasLimiteServices bancoHorasLimiteServices;

    private BancoHorasLimitePK getPrimaryKey(PathSegment pathSegment) {

        BancoHorasLimitePK key = new BancoHorasLimitePK();
        MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        List<String> idBancoHoras = map.get("idBancoHoras");
        if (idBancoHoras != null && !idBancoHoras.isEmpty()) {
            key.setIdBancoHoras(new Integer(idBancoHoras.get(0)));
        }
        List<String> idTipodia = map.get("idTipodia");
        if (idTipodia != null && !idTipodia.isEmpty()) {
            key.setIdTipodia(new Integer(idTipodia.get(0)));
        }
        return key;
    }

    public BancoHorasLimiteRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(List<BancoHorasLimite> entity) {
        try {
            return bancoHorasLimiteServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(List<BancoHorasLimite> entity) {
        try {
            return bancoHorasLimiteServices.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") PathSegment id) {
        try {
            com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimitePK key = getPrimaryKey(id);
            return bancoHorasLimiteServices.excluir(BancoHorasLimite.class, key);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public BancoHorasLimite find(@PathParam("id") PathSegment id) {
        try {
            com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimitePK key = getPrimaryKey(id);
            return bancoHorasLimiteServices.buscar(BancoHorasLimite.class, key);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Entidade> findAll() {
        try {
            return bancoHorasLimiteServices.buscarTodos(BancoHorasLimite.class);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("bancohoras/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<BancoHorasLimite> buscarPorIdBancoHoras(@PathParam("id") Integer id) {
        try {
            return bancoHorasLimiteServices.buscarPorIdBancoHoras(BancoHorasLimite.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
