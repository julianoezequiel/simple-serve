package com.topdata.toppontoweb.rest.ferramentas.layout;

import com.sun.jersey.api.spring.Autowire;
import com.sun.jersey.spi.resource.Singleton;
import com.topdata.toppontoweb.dto.ferramentas.layout.EventoMotivo;
import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutArquivoTransfer;
import com.topdata.toppontoweb.entity.ferramentas.TipoFormatoEvento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.ferramentas.layout.EventosServices;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Path("/eventos")
@Singleton
@Autowire
public class EventosRESTController {

    @Autowired
    private EventosServices eventosServices;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EventoMotivo> findAll() {
        try {
            return this.eventosServices.buscarTodos();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("tipoformatoevento")
    public List<TipoFormatoEvento> findAllTipos() {
        try {
            return this.eventosServices.buscarTodosTipo();
        } catch (ServiceException ex) {
            throw new WebApplicationException(ex.getResponse());
        }
    }

}
