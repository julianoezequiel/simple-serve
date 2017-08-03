package com.topdata.toppontoweb.services.autenticacao;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Verifica se o operador está com a senha bloqueada
 *
 * @version 1.0.0.0 data 01/05/2016
 * @since 1.0.0.0 data 01/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarSenhaBloqueada implements Validacao<Operador, Object> {

//    @Autowired
//    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

//    @Autowired
//    private SegurancaService segurancaService;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        if (Objects.equals(operador.getSenhaBloqueada(), Boolean.TRUE)) {
//            try {
//                Seguranca seguranca = segurancaService.getSeguranca();
//                if (seguranca.getQtdeHorasDesbloqueioUsuario() > 0) {
//                    Calendar c = Calendar.getInstance();
//                    c.setTime(operador.getDataHoraBloqueioSenha() != null ? operador.getDataHoraBloqueioSenha() : Calendar.getInstance().getTime());
//                    c.add(Calendar.HOUR, seguranca.getQtdeHorasDesbloqueioUsuario());
//
//                    if (Calendar.getInstance().getTime().before(c.getTime())) {
//                        throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_SENHA_BLOQUEADA.getResource(), null));
//                    } else {
//                        operador.setSenhaBloqueada(Boolean.FALSE);
//                        operador.setTentativasLogin(0);
//                        dao.save(operador);
//                    }
//                } else {
//                }
            throw new ServiceException(topPontoResponse.alerta(MSG.OPERADOR.ALERTA_SENHA_BLOQUEADA.getResource(), null));
//            } catch (DaoException ex) {
//                throw new ServiceException(topPontoResponse.erroValidar(Seguranca.class), ex);
//            }
        }
        return operador;
    }

}
