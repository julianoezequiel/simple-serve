package com.topdata.toppontoweb.services.autenticacao;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Valida a troca de senha esta habilitada
 *
 * @version 1.0.0.0 data 01/03/2016
 * @since 1.0.0.0 data 01/03/2016
 * @author juliano.ezequiel
 */
@Service
public class ValidarTrocarSenhaProximoAcesso implements Validacao<Operador, Object> {

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private OperadorService operadorService;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (Objects.equals(operador.getTrocaSenhaProximoAcesso(), Boolean.TRUE)) {
            throw new ServiceException(topPontoResponse.alertaTrocaSenha(MSG.OPERADOR.ALERTA_TROCAR_SENHA.getResource(), operadorService.buscaOperadorPorEmail(operador)));
        }
        return operador;
    }

}
