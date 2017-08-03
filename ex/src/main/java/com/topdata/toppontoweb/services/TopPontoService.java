package com.topdata.toppontoweb.services;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRException;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.fechamento.FechamentoServices;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe genérica para as Entidades onde aplica-se regras de negócio genéricas
 *
 * @version 1.0.3 data 05/05/2016
 * @since 1.0.1 data 20/04/2016
 * @author juliano.ezequiel
 * @param <T>
 * @param <I>
 */
@Service
public class TopPontoService<T, I> implements InterfaceServices<T, I> {

    public final static Logger LOGGER = LoggerFactory.getLogger(TopPontoService.class.getName());

    //<editor-fold defaultstate="collapsed" desc="CDI">
    /* Injeta o Repositório de persistência */
    @Autowired
    private Dao dao;
    /* Injeta a auditoria de dados */
    @Autowired
    private AuditoriaServices auditarDados;
    /* Injeta o Repositório de retorno Response */
    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private FechamentoServices fechamentoServices;
    //</editor-fold>

    private Entidade entidade;
    private final List<Validacao> validacoes = new ArrayList<>();

    @Override
    public Response excluir(Class<T> c, Object id) throws ServiceException {
        try {

            validacoes.clear();
            validacoes.add(new VNaoCadastrado());

            for (Validacao validacao : validacoes) {
                entidade = (Entidade) validacao.validar(dao.find(c, id), null);
            }

            dao.delete(entidade);
            auditarDados.auditar(Enum_FUNCIONALIDADE.getByClass(entidade.getClass().getSimpleName()), Enum_AUDITORIA.getPorDescricao(entidade.getClass().getSimpleName()), Enum_OPERACAO.EXCLUIR, entidade);
            return topPontoResponse.sucessoExcluir(entidade.getClass());

        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroExcluir(entidade.getClass()), ex);
        }
    }

    @Override
    public String quantidade(Class<T> entidade) throws ServiceException {
        try {
            return dao.count(entidade).toString();
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erro(MSG.CADASTRO.ERRO_QTD, entidade), ex);
        }
    }

    @Override
    public T buscar(Class<T> entidade, Object id) throws ServiceException {
        try {
            Entidade e = (Entidade) (T) dao.find(entidade, id);
            if (e == null) {
                throw new ServiceException(topPontoResponse.alertaNaoCad());
            }
            return (T) e;
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erro(MSG.CADASTRO.ERRO_BUSCAR, entidade), ex);
        }
    }

    @Override
    public List<Entidade> buscarMaxMin(int maxResults, int firstResult, Class<T> entidade) throws ServiceException {
        try {
            return dao.find(maxResults, firstResult, entidade);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erro(MSG.CADASTRO.ERRO_LISTAR, entidade), ex);
        }
    }

    @Override
    public List<Entidade> buscarTodos(Class<T> entidade) throws ServiceException {
        try {
            return dao.findAll(entidade);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erro(MSG.CADASTRO.ERRO_LISTAR, entidade), ex);
        }
    }

    @Override
    public Response atualizar(T entidade) throws ServiceException {
        try {
            Entidade entidade1 = dao.save((Entidade) entidade);
            auditarDados.auditar(Enum_FUNCIONALIDADE.getByClass(entidade.getClass().getSimpleName()), Enum_AUDITORIA.getPorDescricao(entidade.getClass().getSimpleName()), Enum_OPERACAO.EDITAR, (Entidade) entidade);
            return topPontoResponse.sucessoAtualizar(entidade.toString(), entidade1);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroAtualizar(entidade.getClass()), ex);
        }
    }

    @Override
    public Response gerarRelatorio(String tipo) throws JRException, DaoException, IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Entidade> buscarPorAtributos(HashMap<String, Object> map, Class<T> entidade) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response salvar(T entidade) throws ServiceException {
        try {
            Entidade entidade1 = dao.save((Entidade) entidade);
            auditarDados.auditar(Enum_FUNCIONALIDADE.getByClass(entidade.getClass().getSimpleName()), Enum_AUDITORIA.getPorDescricao(entidade.getClass().getSimpleName()), Enum_OPERACAO.INCLUIR, (Entidade) entidade);
            return topPontoResponse.sucessoSalvar(entidade.toString(), entidade1);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(entidade.getClass()), ex);
        }
    }

    @Override
    public Entidade buscarUnicoPorAtributos(HashMap<String, Object> map, Class<T> entidade) throws ServiceException {
        try {
            return dao.findOnebyAttributes(map, entidade);
        } catch (DaoException ex) {
            return null;
        }
    }

    private class VNaoCadastrado implements Validacao<Entidade, Object> {

        @Override
        public Entidade validar(Entidade entidade, Object object) throws ServiceException {
            if (entidade == null) {
                throw new ServiceException(topPontoResponse.alertaNaoCad(entidade.getClass()));
            }
            return entidade;
        }
    }

    public TopPontoResponse getTopPontoResponse() {
        return topPontoResponse;
    }

    public AuditoriaServices getAuditoriaServices() {
        return auditarDados;
    }

    public Dao getDao() {
        return dao;
    }

    public FechamentoServices getFechamentoServices() {
        return fechamentoServices;
    }

}
