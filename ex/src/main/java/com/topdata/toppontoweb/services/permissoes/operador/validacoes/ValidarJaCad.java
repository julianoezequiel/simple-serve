package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Verifica se já existe um operador cadastrado com nome de usuário
 *
 * @version 1.0,1 data 02/05/2016
 * @since 1.0.1 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarJaCad implements Validacao<Operador, Object> {

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {

        if (operadorService.buscaOperadorPorNome(operador) != null) {
                throw new ServiceException(topPontoResponse.alertaJaCad(operador.toString()));
        }
        return operador;
    }

}
