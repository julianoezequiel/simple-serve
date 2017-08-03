package com.topdata.toppontoweb.services.permissoes.operador.validacoes;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.Validacao;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Esta classe realiza a copia dos valores entre entidades para manter uma
 * entidade atualizada. Esta classe dever� ser corrigida para trabalhar através
 * de REFLEXION
 *
 * @version 1.0.2 data 02/05/2016
 * @since 1.0.2 data 02/05/2016
 *
 * @author juliano.ezequiel
 */
@Service
@Deprecated
public class AtualizarValoresEntreEntidades implements Validacao<Operador, Object> {

    @Autowired
    private Dao dao;

    @Autowired
    private TopPontoResponse topPontoResponse;

    @Override
    public Operador validar(Operador newOperador, Object object) throws ServiceException {
        Logger.getLogger(this.getClass().getSimpleName()).log(Level.INFO, "VALIDACAO: {0}", newOperador);
        try {
            Operador oldOperador = (Operador) dao.find(Operador.class, newOperador.getIdOperador());
            newOperador.setUsuario(newOperador.getUsuario() == null ? oldOperador.getUsuario() : newOperador.getUsuario());
            newOperador.setIdOperador(newOperador.getIdOperador() == null ? oldOperador.getIdOperador() : newOperador.getIdOperador());
            newOperador.setAtivo(newOperador.getAtivo() == null ? oldOperador.getAtivo() : newOperador.getAtivo());
            newOperador.setEmail(newOperador.getEmail() == null ? oldOperador.getEmail() : newOperador.getEmail());
            newOperador.setFoto(newOperador.getFoto() == null ? oldOperador.getFoto() : newOperador.getFoto());
            newOperador.setGrupo(newOperador.getGrupo() == null ? oldOperador.getGrupo() : newOperador.getGrupo());
            newOperador.setSenha(newOperador.getSenha() == null ? oldOperador.getSenha() : newOperador.getSenha());
            newOperador.setUltimoAcesso(newOperador.getUltimoAcesso() == null ? oldOperador.getUltimoAcesso() : newOperador.getUltimoAcesso());
            newOperador.setTentativasLogin(0);
            newOperador.setAuditoriaList(oldOperador.getAuditoriaList());
            newOperador.setHistoricoSenhasList(oldOperador.getHistoricoSenhasList());
            newOperador.setSenhaBloqueada(newOperador.getSenhaBloqueada() == null ? oldOperador.getSenhaBloqueada() : newOperador.getSenhaBloqueada());
            newOperador.setTrocaSenhaProximoAcesso(newOperador.getTrocaSenhaProximoAcesso() == null ? oldOperador.getTrocaSenhaProximoAcesso() : newOperador.getTrocaSenhaProximoAcesso());
            newOperador.setVisualizarAlertas(newOperador.getVisualizarAlertas() == null ? oldOperador.getVisualizarAlertas() : newOperador.getVisualizarAlertas());
            newOperador.setVisualizarMensagens(newOperador.getVisualizarMensagens() == null ? oldOperador.getVisualizarMensagens() : newOperador.getVisualizarMensagens());
            newOperador.setUltimoIp(newOperador.getUltimoIp() == null ? oldOperador.getUltimoIp() : newOperador.getUltimoIp());
            newOperador.setCodigoConfirmacaoNovaSenha(newOperador.getCodigoConfirmacaoNovaSenha() == null ? oldOperador.getCodigoConfirmacaoNovaSenha() : newOperador.getCodigoConfirmacaoNovaSenha());
            newOperador.setUltimoToken(newOperador.getUltimoToken() == null ? oldOperador.getUltimoToken() : newOperador.getUltimoToken());
            newOperador.setDataHoraBloqueioSenha(newOperador.getDataHoraBloqueioSenha() == null ? oldOperador.getDataHoraBloqueioSenha() : newOperador.getDataHoraBloqueioSenha());
            newOperador.setFuncionarioList(newOperador.getFuncionarioList() == null ? oldOperador.getFuncionarioList(): newOperador.getFuncionarioList());
            return newOperador;
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroValidar(Operador.class));
        }
    }

}
