package com.topdata.toppontoweb.services.autenticacao;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.rest.autenticacao.UserAuthentication;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.permissoes.SegurancaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Realiza as validações das quantidades de tentativas de autenticação no
 * sistema, conforme as configurações de segurança
 *
 * @see Validacao
 * @see UserAuthentication
 * @version 1.0.0.0 data 01/05/2016
 * @since 1.0.0.0 data 01/05/2016
 * @author juliano.ezequiel
 */
@Service
public class ValidarTentativasLogin implements Validacao<Operador, Object> {

    @Autowired
    private Dao dao;

    @Autowired
    private SegurancaService segurancaService;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        try {
            if (!operador.getUsuario().equals(CONSTANTES.OPERADOR_MASTER)) {
                operador.setTentativasLogin(operador.getTentativasLogin() != null ? operador.getTentativasLogin() : 0);
                Seguranca seguranca = segurancaService.getSeguranca();

                if (!seguranca.getQtdeBloqueioTentativas().equals(0) && operador.getTentativasLogin() >= seguranca.getQtdeBloqueioTentativas()) {
                    operador.setSenhaBloqueada(Boolean.TRUE);
                    operador.setTentativasLogin(0);
                    operador.setDataHoraBloqueioSenha(Calendar.getInstance().getTime());

                    dao.save(operador);

                    throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_MAX_TENTATIVA_LOGIN.getResource(),
                            operador.toString()));
                }
                operador.setTentativasLogin(operador.getTentativasLogin() + 1);
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar(Operador.class), ex);
        }
        return operador;
    }

}
