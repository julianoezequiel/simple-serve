package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Grupo_;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class ValidarAlteracaoAdmin implements Validacao<Operador, Object> {

    @Autowired
    private MessageSource msg;

    @Autowired
    private Dao dao;

    private final HashMap<String, Object> map = new HashMap<>();

    @Override
    public Operador validar(Operador operador, Object object) throws ServiceException {
        try {
            map.clear();
            map.put(Operador_.usuario.getName(), CONSTANTES.OPERADOR_ADMIN);
            Operador operAdmin = (Operador) dao.findbyAttributes(map, Operador.class);

            if (operador.getIdOperador().equals(operAdmin.getIdOperador())) {
                if (!operador.getUsuario().equals(operAdmin.getUsuario())) {
                    throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ALTERAR_ADMIN.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
                }
                alteracaoAdmin(operAdmin, operador);
                alteracaoId(operador);
            }
            if (operador.getUsuario().equals(operAdmin.getUsuario())) {
                if (!operador.getIdOperador().equals(operAdmin.getIdOperador())) {
                    throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ALTERAR_ADMIN.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
                }
                alteracaoAdmin(operAdmin, operador);
                alteracaoId(operador);
            }
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
        return operador;
    }

    /**
     * Valida a altera��o do Grupo,Permiss�es REST e bloqueio do ADMIN
     *
     * @param operAdmin
     * @param entidade
     * @throws ServiceException
     */
    public void alteracaoAdmin(Operador operAdmin, Operador entidade) throws ServiceException {
        try {
            if (entidade.getGrupo() != null && (!entidade.getGrupo().equals(operAdmin.getGrupo()))) {
                throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_GRUPO_ADMIN.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
            }
            if (entidade.getAtivo() != null && (entidade.getAtivo() == false)) {
                throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_DESATIVADO.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
            }
//            if (operAdmin.getGrupo() getPermissoesList().size() != entidade.getPermissoesList().size()) {
//                throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ADMIN_PEMISSOES.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
//            }

            if (!entidade.getGrupo().getDescricao().contains(grupoAdmin().getDescricao())) {
                throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ADMIN_PEMISSOES.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
            }
//        if (!entidade.getPermissoesList().contains(permissoesDao.getPermissaoPorDescricao(CONSTANTES.PERMISSAO_ADMIN))) {
//            throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ADMIN_PEMISSOES.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
//        }
        } catch (DaoException ex) {
            Logger.getLogger(ValidacaoOperador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Grupo grupoAdmin() throws DaoException {
        map.put(Grupo_.descricao.getName(), CONSTANTES.GRUPO_ADMIN);
        List<Grupo> list = dao.findbyAttributes(map, Grupo.class);
        return list.isEmpty() ? null : list.get(0);
    }

    public void alteracaoId(Operador entidade) throws ServiceException {
        if (entidade.getIdOperador() != null) {
            try {
                Operador operador = (Operador) dao.find(Operador.class, entidade.getIdOperador());
                if (entidade.getIdOperador().equals(operador.getIdOperador())) {
                    if (!entidade.getUsuario().equals(operador.getUsuario())) {
                        throw new ServiceException(msg.getMessage(MSG.OPERADOR.ALERTA_ALTERAR_ID.getResource(), new Object[]{}, LocaleContextHolder.getLocale()));
                    }
                }
            } catch (DaoException ex) {
                throw new ServiceException(ex);
            }
        }
    }

}
