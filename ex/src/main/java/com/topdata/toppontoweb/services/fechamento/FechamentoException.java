package com.topdata.toppontoweb.services.fechamento;

import com.topdata.toppontoweb.services.ServiceException;
import javax.ws.rs.core.Response;

/**
 *
 * @author juliano.ezequiel
 */
public class FechamentoException extends ServiceException {

    public FechamentoException() {
    }

    public FechamentoException(Throwable cause) {
        super(cause);
    }

    public FechamentoException(String message) {
        super(message);
    }

    public FechamentoException(Response response) {
        this.response = response;
    }

    public FechamentoException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public FechamentoException(Response response, String message, Throwable cause) {
        super(message, cause);
        this.response = response;
    }

    /**
     *
     * @param response
     * @param cause
     */
    public FechamentoException(Response response, Throwable cause) {
        super(cause);
        this.response = response;
    }

    public FechamentoException(Response response, String message) {
        super(message);
        this.response = response;
    }
}
