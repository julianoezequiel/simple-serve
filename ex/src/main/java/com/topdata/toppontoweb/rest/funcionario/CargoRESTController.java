package com.topdata.toppontoweb.rest.funcionario;

import java.io.IOException;

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

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.funcionario.Cargo;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.services.funcionario.cargo.CargoService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 * Classe de acesso ao métodos REST para a Entidade Cargo.
 *
 * @see TopPontoService
 * @see CargoService
 * @see Cargo
 *
 * @version 1.0.3 data 05/05/2016 inclusão dos mapper Json
 * @version 1.0.1 data 03/05/2016
 * @since 1.0.1
 * @author juliano.ezequiel
 */
@Path("/cargo")
@Singleton
@Autowire
public class CargoRESTController {

    @Autowired
    private CargoService cargoService;

    /**
     * @since 1.0.3 data 05/05/2016
     */
    @Autowired
    private ObjectMapper mapper;

    private ObjectWriter viewWriter;

    public CargoRESTController() {
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response create(Cargo entity) {
        try {
            return cargoService.salvar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response edit(Cargo entity) {
        try {
            return cargoService.atualizar(entity);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response remove(@PathParam("id") Integer id) {
        try {
            return cargoService.excluir(Cargo.class, id);
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String find(@PathParam("id") Integer id) throws IOException {
        try {
            configureMapper();
            String saida = viewWriter.writeValueAsString(cargoService.buscar(Cargo.class, id));
            return saida;
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String findAll() throws IOException {
        try {
            configureMapper();
            return viewWriter.writeValueAsString(cargoService.buscarTodos(Cargo.class));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Path("/descricao/{desc}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Cargo find(@PathParam("desc") String descricao) {
        try {
            return cargoService.buscaPorDescricao(new Cargo(descricao));
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }
    

    private void configureMapper() {
        mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        viewWriter = mapper.writerWithView(JsonViews.Audit.class);
    }

}
