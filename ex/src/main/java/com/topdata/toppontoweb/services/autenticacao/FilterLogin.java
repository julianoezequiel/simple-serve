package com.topdata.toppontoweb.services.autenticacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.Validacao;

/**
 * Realiza as verificações durante a autenticaçao das requisições Http
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class FilterLogin implements Validacao<Operador, Object> {

    @Autowired
    private ValidarSenhaBloqueada senhaBloqueada;

    @Autowired
    private ValidarContaDesativada contaDesativada;

    @Autowired
    private ValidarTrocarSenhaProximoAcesso trocarSenhaProximoAcesso;

    public FilterLogin() {
    }

    @Override
    public Operador validar(Operador operador, Object object) {

        try {
            this.senhaBloqueada.validar(operador, null);
            this.contaDesativada.validar(operador, null);
            this.trocarSenhaProximoAcesso.validar(operador, null);
            return operador;
        } catch (ServiceException serviceException) {
            return null;
        }
    }

}
