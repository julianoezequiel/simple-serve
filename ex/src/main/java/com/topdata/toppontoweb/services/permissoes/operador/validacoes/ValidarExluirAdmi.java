package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

/**
 * Verifica se está ocorrendo a tentativa de exclusão do Operador
 * admin
 *
 * @version 1.0.1 data 02/05/2016
 * @since 1.0.1 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarExluirAdmi implements Validacao<Operador, Object> {

    @Autowired
    private MessageSource msg;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (operador.getUsuario().equals(CONSTANTES.OPERADOR_ADMIN)) {
            throw new ServiceException(topPontoResponse.alertaValidacao(MSG.OPERADOR.ALERTA_ALTERAR_ADMIN.getResource()));
        }
        return operador;
    }

}
