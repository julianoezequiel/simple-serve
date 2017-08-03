/**
* @Author: juliano.ezequiel <Juliano>
* @Date:   02-09-2016
* @Email:  juliano.ezequiel@topdata.com.br
* @Project: TopPontoWeb
* @Last modified by:   Juliano
* @Last modified time: 28-10-2016
*/
package com.topdata.toppontoweb.dao;

/**
 *
 * @author juliano.ezequiel
 */
public class DaoException extends Exception {

    public DaoException() {
    }

    /**
     *
     * @param message
     */
    public DaoException(String message) {
        super(message);
    }

    /**
     *
     * @param cause
     */
    public DaoException(Throwable cause) {
        super(cause);
    }

    /**
     *
     * @param message
     * @param cause
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

}
