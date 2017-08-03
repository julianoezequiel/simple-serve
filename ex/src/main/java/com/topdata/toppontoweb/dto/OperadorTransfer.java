package com.topdata.toppontoweb.dto;

import org.springframework.stereotype.Controller;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;

/**
 * @version 1.0.4 data 16/06/2016
 * @since 1.0.1 data 01/04/2016
 *
 * @author juliano.ezequiel
 */
@Controller
public class OperadorTransfer implements Entidade {

    private final Operador operador;

    public OperadorTransfer() {
        this.operador = null;
    }

    public OperadorTransfer(Operador operador) {
        this.operador = operador;
    }

    public Funcionario getFuncionario() {
        if (this.operador.getFuncionarioList() != null) {
            return !this.operador.getFuncionarioList().isEmpty()
                    ? new Funcionario(this.operador.getFuncionarioList().get(0).getIdFuncionario(),
                            this.operador.getFuncionarioList().get(0).getNome(),
                            this.operador.getFuncionarioList().get(0).getPis(),
                            this.operador.getFuncionarioList().get(0).getFoto())
                    : null;
        } else {
            return null;
        }
    }

    public String getUsuario() {
        return operador.getUsuario();
    }

    public String getEmail() {
        return operador.getEmail();
    }

    public String getFoto() {
        return operador.getFoto();
    }

    public Integer getIdOperador() {
        return operador.getIdOperador();
    }

    public Integer getTentativasLogin() {
        return operador.getTentativasLogin();
    }

    public Boolean getAtivo() {
        return operador.getAtivo();
    }

    public Boolean getSenhaBloqueada() {
        return operador.getSenhaBloqueada();
    }

    public Boolean getTrocaSenhaProximoAcesso() {
        return operador.getTrocaSenhaProximoAcesso();
    }

    public Grupo getGrupo() {
        return operador.getGrupo();
    }

    public Boolean getVisualizarAlertas() {
        return operador.getVisualizarAlertas();
    }

    public Boolean getVisualizarMensagens() {
        return operador.getVisualizarMensagens();
    }

}
