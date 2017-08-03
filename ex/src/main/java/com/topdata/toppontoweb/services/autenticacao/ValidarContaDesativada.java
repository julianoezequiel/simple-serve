package com.topdata.toppontoweb.services.autenticacao;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Valida se a conta do operador está desativada
 *
 * @version 1.0.0.0 data 01/03/2016
 * @since 1.0.0.0 data 01/03/2016
 * @author juliano.ezequiel
 */
@Service
public class ValidarContaDesativada implements Validacao<Operador, Object> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    public ValidarContaDesativada() {
    }

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (Objects.equals(operador.getAtivo(), Boolean.FALSE)) {
            throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_DESATIVADO.getResource(), null));
        }
        return operador;
    }

}
