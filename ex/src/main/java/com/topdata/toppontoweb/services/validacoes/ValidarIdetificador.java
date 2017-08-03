package com.topdata.toppontoweb.services.validacoes;

import java.util.HashMap;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.Validacao;

/**
 * Classe verifica de a entidade possui um identificador válido
 *
 * @version 1.0.3 data 09/05/2016
 * @since 1.0.3 data 09/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarIdetificador<T> implements Validacao<T, HashMap<String, Object>> {

    @Override
    public T validar(T o, HashMap<String, Object> map) throws ServiceException {
        return o;
    }
}
