package com.topdata.toppontoweb.services.validacoes;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe verifica se existe um cadastro com as propriedades passadas no HashMap
 *
 * @version 1.0.3 data 09/05/2016
 * @since 1.0.3 data 06/05/2016
 * @author juliano.ezequiel
 * @param <T>
 */
@Service
public class ValidarNaoCadastrado<T> implements Validacao<T, HashMap<String, Object>> {

    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public T validar(T o, HashMap<String, Object> map) throws ServiceException {
        try {
            List<Entidade> entidades = dao.findbyAttributes(map, o.getClass());
            if (entidades.isEmpty()) {
                throw new ServiceException(topPontoResponse.alertaNaoCad(o.getClass()));
            }
            return (T) entidades.get(0);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar(o.getClass()), ex);
        }
    }
}
