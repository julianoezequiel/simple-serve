package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.1 data 03/03/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarAuditList implements Validacao<Operador, Object> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (operador != null) {
            if (operador.getAuditoriaList().size() > 1) {
                throw new ServiceException(topPontoResponse.alerta(MSG.FUNCIONARIO.ALERTA_FUNCIONARIO_VINCULO_OPERADOR.getResource(), operador.getUsuario()));
            }
        }
        return operador;
    }

}
