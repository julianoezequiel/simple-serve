/**
* @Author: juliano.ezequiel <Juliano>
* @Date:   02-09-2016
* @Email:  juliano.ezequiel@topdata.com.br
* @Project: TopPontoWeb
* @Last modified by:   Juliano
* @Last modified time: 28-10-2016
*/
package com.topdata.toppontoweb.dao;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Interface de repositório para os métodos genéricos de persistência
 *
 * @version 1.0.1 data 03/05/2016
 * @since 1.0.1 data 03/05/2016
 *
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
@Repository
public interface InterfaceDao<T extends Entidade, I> {

    /**
     * Busca todas a entidades
     *
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public List<T> findAll(Class<T> entidade) throws DaoException;

    /**
     * Busca a entidade pelo seu ID
     *
     * @param entidade
     * @param id
     * @return
     * @throws DaoException
     */
    @Transactional
    public T find(Class<T> entidade, Object id) throws DaoException;

    /**
     * Persiste a entidade na base de dados
     *
     * @param Entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public T save(T Entidade) throws DaoException;

    /**
     * Exclui a entidade da base de dados
     *
     * @param id
     * @throws DaoException
     */
    @Transactional
    public void delete(I id) throws DaoException;

    /**
     * Busca as entidades pelo máximo e primeiro registro
     *
     * @param maxResults
     * @param firstResult
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public List<Entidade> find(int maxResults, int firstResult, Class<T> entidade) throws DaoException;

    /**
     * Realiza o count na base de dados da entidade solicitada
     *
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public Long count(Class<T> entidade) throws DaoException;

    /**
     * Realiza um busca parametrizada na base de dados
     *
     * @param criterios
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public List<T> findbyAttributes(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException;

    /**
     * Realiza um busca parametrizada na base de dados
     *
     * @param criterios
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public T findOnebyAttributes(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException;

    /**
     * Realiza um count parametrizados na base de dados
     *
     * @param criterios
     * @param entidade
     * @return
     * @throws DaoException
     */
    @Transactional
    public Long countByCriteria(HashMap<String, Object> criterios, Class<T> entidade) throws DaoException;

    public T deleteAll(Class<T> entidade) throws DaoException;

    public T findReference(Class<T> entidade, Object id) throws DaoException;

}
