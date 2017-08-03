package com.topdata.toppontoweb.services;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.ws.rs.core.Response;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Service;

/**
 *
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
@Service
public interface InterfaceServices<T, I> {

    /**
     * Pesquisa na base de dados uma entidade pelo ID
     *
     * @param entidade
     * @param id
     * @return
     * @throws ServiceException
     */
    public T buscar(Class<T> entidade, Object id) throws ServiceException;

    /**
     * Realiza a persistência de uma entidade na base de dados
     *
     * @param Entidade
     * @return
     * @throws ServiceException
     */
    public Response salvar(T Entidade) throws ServiceException;

    /**
     * Realiza o update de uma entidade na base de dados
     *
     * @param Entidade
     * @return
     * @throws ServiceException
     */
    public Response atualizar(T Entidade) throws ServiceException;

    /**
     * Exclui uma entidade da base de dados
     *
     * @param entidade
     * @param id
     * @return
     * @throws ServiceException
     */
    public Response excluir(Class<T> entidade, Object id) throws ServiceException;

    /**
     * Realiza a busca de uma lista de entidade pelo máximo e mínimo
     *
     * @param maxResults
     * @param firstResult
     * @param entidade
     * @return
     * @throws ServiceException
     */
    public List<Entidade> buscarMaxMin(int maxResults, int firstResult, Class<T> entidade) throws ServiceException;

    /**
     * Realiza a busca de uma lista de entidades. Este comando pode causar
     * lentidão no sistema se for utilizado indevidamente. Pois uma consulta
     * longa poderá gerar tráfego no sistema.
     *
     * @param entidade
     * @return
     * @throws ServiceException
     */
    public List<Entidade> buscarTodos(Class<T> entidade) throws ServiceException;

    /**
     * Realiza a count de entidade na base de dados
     *
     * @param entidade
     * @return
     * @throws ServiceException
     */
    public String quantidade(Class<T> entidade) throws ServiceException;

    /**
     * Para gerar os relatório criados pelo Jasper
     *
     * @param tipo
     * @return
     * @throws JRException
     * @throws DaoException
     * @throws IOException
     */
    public Response gerarRelatorio(String tipo) throws JRException, DaoException, IOException;

    /**
     * Realiza a consulta a base de dados, através de vários critério da
     * entidade. A busca é realizada com o parâmetro "LIKE"
     *
     * @param map
     * @param entidade
     * @return
     * @throws ServiceException
     */
    public List<Entidade> buscarPorAtributos(HashMap<String, Object> map, Class<T> entidade) throws ServiceException;
    
    /**
     * Realiza a consulta a base de dados, através de vários critério da
     * entidade. A busca é realizada com o parâmetro "LIKE"
     *
     * @param map
     * @param entidade
     * @return
     * @throws ServiceException
     */
    public Entidade buscarUnicoPorAtributos(HashMap<String, Object> map, Class<T> entidade) throws ServiceException;
}
