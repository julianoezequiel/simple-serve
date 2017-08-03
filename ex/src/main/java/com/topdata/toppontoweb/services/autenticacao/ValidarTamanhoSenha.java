package com.topdata.toppontoweb.services.autenticacao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Valida o tamanho mínimo da senha, conforme os dados de configuração
 *
 * @version 1.0.0.0 data 31/08/2016
 * @since 1.0.0.0 data 16/06/2016
 * @author juliano.ezequiel
 */
@Service
public class ValidarTamanhoSenha implements Validacao<Operador, Object> {

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        Seguranca seguranca = segurancaService.getSeguranca();
        if (operador.getSenha() != null) {
            if (operador.getSenha().length() < seguranca.getTamanhoMinimoSenha()) {
                throw new ServiceException(topPontoResponse.alertaValidacao(
                        MSG.SEGURANCA.TAM_MIN.getResource(), seguranca.getTamanhoMinimoSenha().toString()));
            }
        }
        return operador;
    }

}
