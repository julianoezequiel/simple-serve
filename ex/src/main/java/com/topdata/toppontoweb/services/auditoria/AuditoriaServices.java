package com.topdata.toppontoweb.services.auditoria;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.auditoria.AuditoriaDao;
import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Auditoria_;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.autenticacao.Operador_;
import com.topdata.toppontoweb.entity.autenticacao.User;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import java.io.Serializable;
import org.slf4j.LoggerFactory;
//</editor-fold>

/**
 * @version 1.0.0.0 data 19/01/2017
 * @since 1.0.0.0 data 03/03/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class AuditoriaServices {

    public final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(AuditoriaServices.class.getName());

    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private AuditoriaDao auditoriaDao;

    private Auditoria auditoria;
    private ObjectMapper mapper;
    private Funcionalidades funcionalidades;
    private Operacao operacao;
    private TipoAuditoria tipoAuditoria;
    private HashMap<String, Object> map;

    public AuditoriaServices() {
        this.auditoria = new Auditoria();
        this.mapper = new ObjectMapper();
        this.map = new HashMap<>();
    }

    public void auditar(Enum_FUNCIONALIDADE eFuncoes, Enum_AUDITORIA aAuditoria, Enum_OPERACAO eOperacao, Entidade entidade) throws ServiceException {
        try {

            funcionalidades = (Funcionalidades) dao.find(Funcionalidades.class, eFuncoes != null ? eFuncoes.ordinal() : Enum_FUNCIONALIDADE.DEFAULT.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, aAuditoria != null ? aAuditoria.ordinal() : Enum_AUDITORIA.DEFAULT.ordinal());
            LOGGER.debug("Auditando dados operação : {} - funcionalidade : {} - tipo auditoria : {}", eOperacao, eFuncoes, aAuditoria);
            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                Operador operador = operadorService.buscaOperadorPorNome(new Operador(SecurityContextHolder.getContext().getAuthentication().getName()));
                auditoria.setDataHora(Calendar.getInstance().getTime());
                auditoria.setFuncionalidade(funcionalidades);
                auditoria.setOperacao(operacao);
                auditoria.setTipoAuditoria(tipoAuditoria);
                auditoria.setOperador(operador);
                auditoria.setConteudo(tamanhoMaxConteudo(mapper.writeValueAsString(entidade)));
                auditoria.setEnderecoIp(operador.getUltimoIp() != null ? operador.getUltimoIp() : "");
                dao.save(auditoria);
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.toString()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void auditar(Enum_FUNCIONALIDADE eFuncoes, Enum_AUDITORIA aAuditoria, Enum_OPERACAO eOperacao, Operador operador, Serializable objeto) throws ServiceException {
        try {

            funcionalidades = (Funcionalidades) dao.find(Funcionalidades.class, eFuncoes != null ? eFuncoes.ordinal() : Enum_FUNCIONALIDADE.DEFAULT.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, aAuditoria != null ? aAuditoria.ordinal() : Enum_AUDITORIA.DEFAULT.ordinal());
            LOGGER.debug("Auditando dados operação : {} - funcionalidade : {} - tipo auditoria : {}", eOperacao, eFuncoes, aAuditoria);
            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);

            if (operador != null) {
                auditoria.setDataHora(Calendar.getInstance().getTime());
                auditoria.setFuncionalidade(funcionalidades);
                auditoria.setOperacao(operacao);
                auditoria.setTipoAuditoria(tipoAuditoria);
                auditoria.setOperador(operador);
                auditoria.setConteudo(tamanhoMaxConteudo(mapper.writeValueAsString(objeto)));
                auditoria.setEnderecoIp(operador.getUltimoIp() != null ? operador.getUltimoIp() : "");
                dao.save(auditoria);
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.toString()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void auditar(Enum_FUNCIONALIDADE eFuncoes, Enum_AUDITORIA aAuditoria, Enum_OPERACAO eOperacao, Entidade entidade, Operador operador) throws ServiceException {
        try {
            funcionalidades = (Funcionalidades) dao.find(Funcionalidades.class, eFuncoes != null ? eFuncoes.ordinal() : Enum_FUNCIONALIDADE.DEFAULT.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, aAuditoria != null ? aAuditoria.ordinal() : Enum_AUDITORIA.DEFAULT.ordinal());
            LOGGER.debug("Auditando dados operação : {} - funcionalidade : {} - tipo auditoria : {}", eOperacao, eFuncoes, aAuditoria);
            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);

            auditoria.setDataHora(Calendar.getInstance().getTime());
            auditoria.setFuncionalidade(funcionalidades);
            auditoria.setOperacao(operacao);
            auditoria.setTipoAuditoria(tipoAuditoria);
            auditoria.setOperador(operador);
            auditoria.setConteudo(tamanhoMaxConteudo(mapper.writeValueAsString(entidade)));
            auditoria.setEnderecoIp(operador.getUltimoIp() != null ? operador.getUltimoIp() : "");
            dao.save(auditoria);

        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.toString()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private String tamanhoMaxConteudo(String s) {
        return s.length() > 8000 ? s.substring(0, 8000) : s;
    }

    public void auditar(Enum_AUDITORIA eAuditoria, Enum_OPERACAO eOperacao, Entidade entidade) throws ServiceException {
        try {
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, eAuditoria.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            mapper.configure(SerializationConfig.Feature.DEFAULT_VIEW_INCLUSION, false);
            String dados = operacao.getDescricao().equals(Enum_OPERACAO.INCLUIR.getDescricao()) ? entidade.toString() : mapper.writerWithView(JsonViews.Audit.class).writeValueAsString(entidade);
            LOGGER.debug("Auditando dados operação : {} - tipo auditoria : {}", eOperacao, eAuditoria);
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                String nomeUser = SecurityContextHolder.getContext().getAuthentication().getName();
                map.clear();
                map.put(Operador_.usuario.getName(), nomeUser);
                Operador operadorAuditado = (Operador) dao.findOnebyAttributes(map, Operador.class);
                auditoria.setDataHora(Calendar.getInstance().getTime());
                auditoria.setOperacao(operacao);
                auditoria.setTipoAuditoria(tipoAuditoria);
                auditoria.setOperador(operadorAuditado);
                auditoria.setFuncionalidade(null);
                auditoria.setConteudo(tamanhoMaxConteudo(dados));
                auditoria.setEnderecoIp(operadorAuditado.getUltimoIp() != null ? operadorAuditado.getUltimoIp() : "");
                dao.save(auditoria);
            }
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.getClass()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void auditar(Enum_AUDITORIA eAuditoria, Enum_OPERACAO eOperacao, Map<String, Object> map, User user) throws ServiceException {
        try {
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, eAuditoria.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            LOGGER.debug("Auditando dados operação : {} - tipo auditoria : {}", eOperacao, eAuditoria);
            String dados = mapper.writeValueAsString(map);

            auditoria.setDataHora(Calendar.getInstance().getTime());
            auditoria.setOperacao(operacao);
            auditoria.setTipoAuditoria(tipoAuditoria);
            auditoria.setOperador(user.getOperador());
            auditoria.setConteudo(tamanhoMaxConteudo(dados));
            auditoria.setEnderecoIp(user.getIP());
            dao.save(auditoria);

        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.getClass()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void auditar(Enum_AUDITORIA eAuditoria, Enum_OPERACAO eOperacao, Map<String, Object> mapJson, String Ip) throws ServiceException {
        try {
            tipoAuditoria = (TipoAuditoria) dao.find(TipoAuditoria.class, eAuditoria.ordinal());
            operacao = (Operacao) dao.find(Operacao.class, eOperacao.ordinal());
            LOGGER.debug("Auditando dados operação : {} - tipo auditoria : {}", eOperacao, eAuditoria);
            String dados = mapper.writeValueAsString(mapJson);
            map.clear();
            map.put(Operador_.usuario.getName(), "anonymousUser");
            List<Operador> operadorAuditado = dao.findbyAttributes(map, Operador.class);
            auditoria.setDataHora(Calendar.getInstance().getTime());
            auditoria.setTipoAuditoria(tipoAuditoria);
            auditoria.setOperacao(operacao);
            auditoria.setOperador(!operadorAuditado.isEmpty()? operadorAuditado.get(0):null);
            auditoria.setConteudo(tamanhoMaxConteudo(dados));
            auditoria.setEnderecoIp(Ip);
            dao.save(auditoria);

        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(auditoria.getClass()), ex);
        } catch (IOException ex) {
            Logger.getLogger(AuditoriaServices.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Consulta as Auditoria que possuem tipos autidoria no periodo informado
     *
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<Auditoria> buscarTiposAuditoriaPeriodo(Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.auditoriaDao.buscarTiposAuditoriaPeriodo(dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta as Auditoria que possuem o tipo autidoria e o operador no
     * periodo informado
     *
     * @param operador
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<Auditoria> buscarTiposAuditoriaPeriodoOperador(Operador operador, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.auditoriaDao.buscarTiposAuditoriaPeriodoOperador(operador.getIdOperador(), dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta Aditoria por uma lista de criterio e o periodo
     *
     * @param map
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<Auditoria> buscarPorCriterioPeriodo(HashMap<String, Object> map, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.auditoriaDao.buscarCriterioPeriodo(map, dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta as Auditoria que possuem o id do modulo e do operador no periodo
     * informado
     *
     * @param modulo
     * @param operador
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<Auditoria> buscarAuditoriaPorPeriodoIdModuloOperador(Modulos modulo, Operador operador, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.auditoriaDao.buscarAuditoriaPorPeriodoIdModuloOperador(modulo.getId(), operador.getIdOperador(), dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Consulta as Auditoria que possuem o id do modulo no periodo informado
     *
     * @param modulo
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws ServiceException
     */
    public List<Auditoria> buscarAuditoriaPorPeriodoIdModulo(Modulos modulo, Date dataInicio, Date dataFim) throws ServiceException {
        try {
            return this.auditoriaDao.buscarAuditoriaPorPeriodoIdModulo(modulo.getId(), dataInicio, dataFim);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

}
