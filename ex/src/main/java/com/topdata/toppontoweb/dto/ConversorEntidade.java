package com.topdata.toppontoweb.dto;

/**
 * @version 1.0.0 data 20/07/2017
 * @param <T>
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
public interface ConversorEntidade<T> {

    public T toEntidade();
}
