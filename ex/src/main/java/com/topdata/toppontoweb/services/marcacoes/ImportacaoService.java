/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.services.marcacoes;

import com.sun.jersey.api.spring.Autowire;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.importar.ImportarHandler;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author tharle.camargo
 */
@Service
public class ImportacaoService extends TopPontoService<Importacao, Object>{
    @Autowired
    OperadorService operadorService;
    
    public Importacao criarImportacao(HttpServletRequest request) throws ServiceException{
        try {
            Operador operador = operadorService.getOperadorDaSessao(request);
            
            Importacao importacao = new Importacao(operador);
            
            return (Importacao) getDao().save(importacao);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public void auditar(Importacao importacao, ImportarHandler handler, Operador operador) throws ServiceException {
        this.getAuditoriaServices().auditar(handler.getFuncionalidade(), CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.IMPORTAR, operador, importacao);
    }
}
