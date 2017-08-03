package com.topdata.toppontoweb.rest.autenticacao;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

/**
 * Habilita o uso do CORS
 *
 * @version 1.0.4 data 04/07/2016
 * @since 1.0.4 data 04/07/2016
 *
 * @author juliano.ezequiel
 */
public class CORSFilter implements ContainerResponseFilter {

    @Override
    public ContainerResponse filter(final ContainerRequest request, final ContainerResponse response) {

        final ResponseBuilder resp = Response.fromResponse(response.getResponse());
//        if (!request.getMethod().equals("OPTIONS")) {
        resp.header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        final String reqHead = request.getHeaderValue("Access-Control-Request-Headers");
        if (null != reqHead && !reqHead.equals(null)) {
            resp.header("Access-Control-Allow-Headers", reqHead);
        }
        response.setResponse(resp.build());
//        }
        return response;

    }
}
