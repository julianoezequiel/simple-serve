package com.topdata.toppontoweb.services.auditoria;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public interface InterfaceAuditoria {

    public void auditar(CONSTANTES.Enum_FUNCIONALIDADE eFuncoes, CONSTANTES.Enum_AUDITORIA aAuditoria, CONSTANTES.Enum_OPERACAO eOperacao, Entidade entidade) throws ServiceException;

    public void auditar(CONSTANTES.Enum_FUNCIONALIDADE eFuncoes, CONSTANTES.Enum_AUDITORIA aAuditoria, CONSTANTES.Enum_OPERACAO eOperacao, Map<String, Object> map, String Ip) throws ServiceException;

    public void auditar(CONSTANTES.Enum_FUNCIONALIDADE eFuncoes, CONSTANTES.Enum_OPERACAO eOperacao, Map<String, Object> map, User user) throws ServiceException;

    public void auditar(CONSTANTES.Enum_AUDITORIA eAuditoria, CONSTANTES.Enum_OPERACAO eOperacao, Map<String, Object> map, User user) throws ServiceException;

    public void auditar(CONSTANTES.Enum_AUDITORIA eAuditoria, CONSTANTES.Enum_OPERACAO eOperacao, Map<String, Object> mapJson, String Ip) throws ServiceException;
}
