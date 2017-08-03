package com.topdata.toppontoweb.rest.testeAPI;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 * @version 1.0.4 data 16/08/2016
 * @since 1.0.4 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Path("/testeapi")
@Singleton
@Autowire
public class TesteAPI {

    @Autowired
    private TopPontoService topPontoService;

    @Autowired
    private ObjectMapper mapper;

    public TesteAPI() {
    }

    @GET
    @Path("{id}/{data1}/{data2}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String exec(@PathParam("id") Integer id, @PathParam("data1") String d1, @PathParam("data2") String d2) throws IOException {

        try {
            mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
            Funcionario f = (Funcionario) topPontoService.buscar(Funcionario.class, id);
//            EntradaAPI e = new EntradaAPI(new Date(Integer.parseInt(d1.split("-")[0]), Integer.parseInt(d1.split("-")[1]), Integer.parseInt(d1.split("-")[2])),
//                    new Date(Integer.parseInt(d2.split("-")[0]), Integer.parseInt(d2.split("-")[1]), Integer.parseInt(d2.split("-")[2])), f);
//
//            Calculo c = new Calculo();
//
//            Resultado r = c.processamento(e);

            return null;//mapper.writeValueAsString(r);

        } catch (ServiceException ex) {
            Logger.getLogger(TesteAPI.class.getName()).log(Level.SEVERE, null, ex);
            throw new WebApplicationException(ex.getResponse());
        }

    }

}
