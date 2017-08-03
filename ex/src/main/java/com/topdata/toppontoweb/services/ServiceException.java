package com.topdata.toppontoweb.services;

import javax.ws.rs.core.Response;

/**
 *
 * @author juliano.ezequiel
 */
public class ServiceException extends Exception {

    public Response response = null;

    public ServiceException() {
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Response response) {
        this.response = response;
    }

    public ServiceException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public ServiceException(Response response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    /**
     *
     * @param response
     * @param cause
     */
    public ServiceException(Response response, Throwable cause) {
        super(cause);
        this.response = response;
    }

    public ServiceException(Response response, String message) {
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
