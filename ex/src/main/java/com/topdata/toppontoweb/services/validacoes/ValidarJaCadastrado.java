package com.topdata.toppontoweb.services.validacoes;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe verifica se j� existe um registro com as propriedades informada no
 * HashMap
 *
 * @version 1.0.3 data 09/05/2016
 * @version 1.0.3 data 09/05/2016
 *
 * @author juliano.ezequiel
 * @param <T>
 */
@Service
public class ValidarJaCadastrado<T> implements Validacao<T, HashMap<String, Object>> {

    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public T validar(T object, HashMap<String, Object> map) throws ServiceException {
        try {
            List<Entidade> entidades = dao.findbyAttributes(map, object.getClass());
            if (!entidades.isEmpty()) {
                throw new ServiceException(topPontoResponse.alertaJaCad(entidades.get(0).toString()));
            }
            return (T) object;
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar("Erro"), ex);
        }
    }

}
