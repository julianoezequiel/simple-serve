package com.topdata.toppontoweb.services;

/**
 * Realiza uma valida��o genérica e retorna o objeto validado, caso o objeto n�o
 * passe no teste de valida��o, dever� ser retornado uma exceção.
 *
 *
 * @version 1.0.2 Data 02/05/2016
 * @since 1.0.2 Data 02/05/2016
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
public interface Validacao<T, I> {

    /**
     * Executa o método de forma genérica. Na maioria dos casos o segundo
     * parâmetro é opcional
     *
     * @param o classe genérica
     * @param i
     * @return T classe genérica.
     * @throws ServiceException
     */
    public T validar(T o, I i) throws ServiceException;

}
