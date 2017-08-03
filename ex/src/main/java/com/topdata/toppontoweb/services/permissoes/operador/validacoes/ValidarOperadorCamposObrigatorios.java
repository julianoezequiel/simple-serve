package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.validacoes.ValidarCamposObrigatorio;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarOperadorCamposObrigatorios implements Validacao<Operador, Object> {

    @Autowired
    private ValidarCamposObrigatorio validarCamposObrigatorio;

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {

        return (Operador) validarCamposObrigatorio.validar(operador, null);
//        if (operador.getUsuario() == null) {
//            throw new ServiceException(msg.getMessage(MSG.CAMPO_OBRIGATORIO, new Object[]{Operador_.usuario.getName()}, null));
//        }
//        if (operador.getSenha() == null) {
//            throw new ServiceException(msg.getMessage(MSG.CAMPO_OBRIGATORIO, new Object[]{Operador_.senha.getName()}, null));
//        }
//        if (operador.getGrupo() == null) {
//            throw new ServiceException(msg.getMessage(MSG.CAMPO_OBRIGATORIO, new Object[]{Operador_.grupo.getName()}, null));
//        }
////        if (operador.getPermissoesList().isEmpty()) {
////            throw new ServiceException(msg.getMessage(MSG.CAMPO_OBRIGATORIO, new Object[]{Operador_.permissoesList.getName()}, null));
////        }
//        if (operador.getEmail() == null) {
//            throw new ServiceException(msg.getMessage(MSG.CAMPO_OBRIGATORIO, new Object[]{Operador_.email.getName()}, null));
//        }
    }

}
