package com.topdata.toppontoweb.services;

import com.topdata.toppontoweb.services.ServiceException;

/**
 * Interface para validações dos cadastros na camada de negócio.
 *
 * @version 1.0.3 data 02/05/2016
 * @since 1.0.2 data 02/05/2016
 *
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
public interface ValidacoesCadastro<T, I> {

    /**
     * Verifica as validações necessárias antes de da operação de
     * persistências na base
     *
     * @param t
     * @return
     * @throws ServiceException
     */
    T validarSalvar(T t) throws ServiceException;

    /**
     * Realiza as validações necessárias antes de das atualizações de um
     * registro na base
     *
     * @param t
     * @return
     * @throws ServiceException
     */
    T validarAtualizar(T t) throws ServiceException;

    /**
     * Realiza as validações necessárias antes de da exclusão d um
     * registro da base
     *
     * @param t
     * @return
     * @throws ServiceException
     */
    T validarExcluir(T t) throws ServiceException;
}
