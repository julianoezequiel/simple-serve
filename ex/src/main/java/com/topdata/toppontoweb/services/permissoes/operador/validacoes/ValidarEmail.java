package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.EmailValidator;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.5 data 31/08/2016
 * @since 1.0.4 data 16/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarEmail implements Validacao<Operador, Object> {

    @Autowired
    private EmailValidator emailValidator;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private OperadorService operadorService;

    private Operador operador;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (emailValidator.validate(operador.getEmail()) == Boolean.FALSE) {
            throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_EMAIL_INVALIDO.getResource(), operador.getEmail()));
        }
        if ((this.operador = operadorService.buscaOperadorPorEmail(operador)) != null) {
            if (this.operador.getEmail().equals(operador.getEmail()) && (this.operador.getIdOperador() != null && !Objects.equals(this.operador.getIdOperador(), operador.getIdOperador()))) {
                throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_EMAIL_JA_CAD.getResource(), operador.getEmail()));
            }
        }
        return operador;
    }

}
