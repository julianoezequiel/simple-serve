package com.topdata.toppontoweb.services.autenticacao;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.utils.PassComplexValidator;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * @version 1.0.0.0 data 09/05/2016
 * @since 1.0.0.0 data 05/05/2016
 * @author juliano.ezequiel
 */
@Service
public class ValidarComplexidadeSenha implements Validacao<Operador, Object> {

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private PassComplexValidator senhaComplexValidator;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        Seguranca seguranca = segurancaService.getSeguranca();
        if (Objects.equals(seguranca.getComplexidadeLetrasNumeros(), Boolean.TRUE)) {
            if (operador.getSenha() != null) {
                if (senhaComplexValidator.validate(operador.getSenha()) == Boolean.FALSE) {
                    throw new ServiceException(topPontoResponse.alertaValidacao(MSG.SEGURANCA.COMPLEX_SENHA.getResource()));
                }
            }
        }

        return operador;

    }

}
