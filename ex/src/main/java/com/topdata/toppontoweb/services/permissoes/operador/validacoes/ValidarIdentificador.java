package com.topdata.toppontoweb.services.permissoes.operador.validacoes;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.4 data 30/08/2016
 * @since 1.0.1 data 02/05/2016
 * 
 * @author juliano.ezequiel
 */
@Service
public class ValidarIdentificador implements Validacao<Operador, Object> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {

        if (operador.getIdOperador() == null && operador.getUsuario() == null && operador.getEmail() == null) {
            throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_NOME_ID_NULL.getResource(), null));
        }
        return operador;
    }

}
