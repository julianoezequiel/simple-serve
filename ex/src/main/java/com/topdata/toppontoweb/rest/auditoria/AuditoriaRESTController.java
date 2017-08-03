package com.topdata.toppontoweb.rest.auditoria;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.GET;
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
import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import static com.topdata.toppontoweb.utils.Utils.ConvertErrorToJson;

/**
 * @version 1.0.3 data 05/05/2016
 * @since 1.0.3 05/05/2016
 * @author juliano.ezequiel
 */
@Path("/auditoria")
@Singleton
@Autowire
public class AuditoriaRESTController {

    @PersistenceContext
    protected EntityManager entityManager;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    TopPontoService topPontoService;

    @Autowired
    private ObjectMapper mapper;

    private ObjectWriter viewWriter;

    public AuditoriaRESTController() {
    }

    /**
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String find(@PathParam("id") Integer id) {
        try {
            Auditoria auditoria = entityManager.find(Auditoria.class, id);
            configureMapper();
            return viewWriter.writeValueAsString(auditoria);
        } catch (IOException ex) {
            throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_FAILED).
                    entity(ConvertErrorToJson(ex)).type(MediaType.APPLICATION_JSON_TYPE).build());
        }
    }

    @GET
    @Path("operadorId/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String getPorOperadorId(@PathParam("id") Integer id) throws IOException {
        try {

            Operador operador = (Operador) topPontoService.buscar(Operador.class, id);
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -90);
            Query query = entityManager.createQuery("SELECT OBJECT(o) FROM Auditoria AS o WHERE o.operador = :operador AND o.dataHora > :data").
                    setParameter("operador", operador).setParameter("data", c);
            List<Auditoria> list = query.getResultList();
            configureMapper();
            return viewWriter.writeValueAsString(list);

        } catch (ServiceException ex) {
            throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_FAILED).
                    entity(ConvertErrorToJson(ex)).type(MediaType.APPLICATION_JSON_TYPE).build());
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @param nome
     * @return
     */
    @GET
    @Path("operadorNome/{nome}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String getPorOperadorNOme(@PathParam("nome") String nome) {
        try {

            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -90);

            Operador operador = operadorService.buscaOperadorPorNome(new Operador(nome));
            Query query = entityManager.createQuery("SELECT OBJECT(o) FROM Auditoria AS o WHERE o.operador = :operador AND o.dataHora > :data").
                    setParameter("operador", operador).setParameter("data", c);

            List<Auditoria> list = query.getResultList();
            configureMapper();
            return viewWriter.writeValueAsString(list);
        } catch (ServiceException | IOException ex) {
            throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(ConvertErrorToJson(ex)).type(MediaType.APPLICATION_JSON_TYPE).build());
        } finally {
            entityManager.close();
        }
    }

    /**
     *
     * @return @throws IOException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String findAll() throws IOException {
        List<Auditoria> list = find(true, -1, -1);
        configureMapper();
        return viewWriter.writeValueAsString(list);

    }

    @GET
    @Path("{max}/{first}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public String findRange(@PathParam("max") Integer max, @PathParam("first") Integer first) {
        try {
            List<Auditoria> list = find(false, max, first);
            configureMapper();
            return viewWriter.writeValueAsString(list);
        } catch (IOException ex) {
            throw new WebApplicationException(Response.status(Response.Status.PRECONDITION_FAILED)
                    .entity(ConvertErrorToJson(ex)).type(MediaType.APPLICATION_JSON_TYPE).build());
        }
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public String count() {
        try {
            Query query = entityManager.createQuery("SELECT count(o) FROM Auditoria AS o");
            return query.getSingleResult().toString();
        } finally {
            entityManager.close();
        }
    }

    private List<Auditoria> find(boolean all, int maxResults, int firstResult) {
        try {
            Calendar c = Calendar.getInstance();
            c.add(Calendar.MONTH, -90);
            Query query = entityManager.createQuery("SELECT object(o) FROM Auditoria AS o WHERE o.dataHora > :data")
                    .setParameter("data", c);
            if (!all) {
                query.setMaxResults(maxResults);
                query.setFirstResult(firstResult);
            }
            return query.getResultList();
        } finally {
            entityManager.close();
        }
    }

    private void configureMapper() {
        mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
        mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
        viewWriter = mapper.writerWithView(JsonViews.Audit.class);
    }

}
