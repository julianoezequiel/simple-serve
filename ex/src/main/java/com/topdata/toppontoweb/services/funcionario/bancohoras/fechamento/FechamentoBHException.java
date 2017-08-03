package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;


import javax.ws.rs.core.Response;

import com.topdata.toppontoweb.services.ServiceException;

/**
 *
 * @author juliano.ezequiel
 */
public class FechamentoBHException extends ServiceException {

    public Response response = null;

    public FechamentoBHException() {
    }

    public FechamentoBHException(Throwable cause) {
        super(cause);
    }

    public FechamentoBHException(String message) {
        super(message);
    }

    public FechamentoBHException(Response response) {
        this.response = response;
    }

    public FechamentoBHException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public FechamentoBHException(Response response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    /**
     *
     * @param response
     * @param cause
     */
    public FechamentoBHException(Response response, Throwable cause) {
        super(cause);
        this.response = response;
    }

    public FechamentoBHException(Response response, String message) {
        super(message);
        this.response = response;
    }

    /**
     *
     * @return
     */
    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
