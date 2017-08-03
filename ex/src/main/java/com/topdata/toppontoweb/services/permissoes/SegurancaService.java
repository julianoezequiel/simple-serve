package com.topdata.toppontoweb.services.permissoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca;
import com.topdata.toppontoweb.entity.autenticacao.Seguranca_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_AUDITORIA;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_FUNCIONALIDADE;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * Classe realiza as regras de negócio para a Segurança
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class SegurancaService extends TopPontoService<Seguranca, Object>
        implements ValidacoesCadastro<Seguranca, Object> {

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private Seguranca seguranca;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Seguranca buscar(Class<Seguranca> entidade, Object id) throws ServiceException {
        try {
            Seguranca s = (Seguranca) this.getDao().find(Seguranca.class, id);
            if (s == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Seguranca().toString()));
            }
            return s;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response excluir(Class<Seguranca> c, Object id) throws ServiceException {
        try {
            this.seguranca = validarExcluir(new Seguranca((Integer) id));
            this.getDao().delete(this.seguranca);
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_SEGURANCA, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EXCLUIR, this.seguranca);
            return this.getTopPontoResponse().sucessoExcluir(this.seguranca.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Seguranca().toString()), ex);
        }
    }

    @Override
    public Response atualizar(Seguranca seguranca) throws ServiceException {
        try {
            seguranca = (Seguranca) this.getDao().save(validarSalvar(seguranca));
            this.getAuditoriaServices().auditar(Enum_FUNCIONALIDADE.CAD_PERMISSOES_SEGURANCA, Enum_AUDITORIA.DEFAULT, Enum_OPERACAO.EDITAR, seguranca);
            return this.getTopPontoResponse().sucessoAtualizar(seguranca.toString(), seguranca);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Seguranca.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Seguranca validarSalvar(Seguranca seguranca) throws ServiceException {
        if (seguranca.getIdSeguranca() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Seguranca_.idSeguranca.getName()));
        }
        if (seguranca.getComplexidadeLetrasNumeros() == Boolean.TRUE && seguranca.getTamanhoMinimoSenha() < 2) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.SEGURANCA.TAMANHO_MINIMO_COMPLEXIDADE.getResource()));
        }
        return atualizarValoresEntreEntidades(seguranca);
    }

    @Override
    public Seguranca validarExcluir(Seguranca t) throws ServiceException {
        try {
            if ((seguranca = buscar(Seguranca.class, t.getIdSeguranca())) != null) {

                /* verifica se existe mais de um registro de Seguran�a */
                if (this.getDao().count(Seguranca.class) < 2) {
                    throw new ServiceException(this.getTopPontoResponse().erroExcluir(Seguranca.class));
                }
            } else {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Seguranca.class));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(Seguranca.class), ex);
        }
        return seguranca;
    }

    @Override
    public Seguranca validarAtualizar(Seguranca t) throws ServiceException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    public Seguranca getSeguranca() throws ServiceException {
        try {
            List<Seguranca> segurancaList = this.getDao().findAll(Seguranca.class);
            if (segurancaList.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(Seguranca.class));
            } else {
                return segurancaList.get(0);
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Seguranca.class), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Atualiza os valores entre as entidade Segurança , para manter somente uma
     * referencia atualizada no Spring
     *
     * @param s
     * @param s1
     */
    private Seguranca atualizarValoresEntreEntidades(Seguranca s) throws ServiceException {

        Seguranca s1 = buscar(Seguranca.class, s.getIdSeguranca());

        s.setComplexidadeLetrasNumeros(s.getComplexidadeLetrasNumeros() == null
                ? s1.getComplexidadeLetrasNumeros() : s.getComplexidadeLetrasNumeros());
        s.setQtdeBloqueioTentativas(s.getQtdeBloqueioTentativas() == null
                ? s1.getQtdeBloqueioTentativas() : s.getQtdeBloqueioTentativas());
        s.setQtdeDiasTrocaSenha(s.getQtdeDiasTrocaSenha() == null
                ? s1.getQtdeDiasTrocaSenha() : s.getQtdeDiasTrocaSenha());
//        s.setQtdeHorasDesbloqueioUsuario(s.getQtdeHorasDesbloqueioUsuario() == null
//                ? s1.getQtdeHorasDesbloqueioUsuario() : s.getQtdeHorasDesbloqueioUsuario());
        s.setQtdeNaoRepetirSenhas(s.getQtdeNaoRepetirSenhas() == null
                ? s1.getQtdeNaoRepetirSenhas() : s.getQtdeNaoRepetirSenhas());
        s.setTamanhoMinimoSenha(s.getTamanhoMinimoSenha() == null
                ? s1.getTamanhoMinimoSenha() : s.getTamanhoMinimoSenha());
        return s;
    }
//</editor-fold>
}
