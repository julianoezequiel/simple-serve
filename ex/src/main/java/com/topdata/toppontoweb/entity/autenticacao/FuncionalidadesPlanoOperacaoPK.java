package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

/**
 *
 * @author juliano.ezequiel
 */
@Embeddable
public class FuncionalidadesPlanoOperacaoPK implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionalidade")
    private int idFuncionalidade;

    @Basic(optional = false)
    @NotNull
    @Column(name = "idPlano")
    private int idPlano;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdOperacao")
    private int idOperacao;

    public FuncionalidadesPlanoOperacaoPK() {
    }

    public FuncionalidadesPlanoOperacaoPK(int idFuncionalidade, int idPlano, int idOperacao) {
        this.idFuncionalidade = idFuncionalidade;
        this.idPlano = idPlano;
        this.idOperacao = idOperacao;
    }

    public int getIdFuncionalidade() {
        return idFuncionalidade;
    }

    public void setIdFuncionalidade(int idFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
    }

    public int getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(int idPlano) {
        this.idPlano = idPlano;
    }

    public int getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(int idOperacao) {
        this.idOperacao = idOperacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) idFuncionalidade;
        hash += (int) idPlano;
        hash += (int) idOperacao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionalidadesPlanoOperacaoPK)) {
            return false;
        }
        FuncionalidadesPlanoOperacaoPK other = (FuncionalidadesPlanoOperacaoPK) object;
        if (this.idFuncionalidade != other.idFuncionalidade) {
            return false;
        }
        if (this.idPlano != other.idPlano) {
            return false;
        }
        if (this.idOperacao != other.idOperacao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.FuncionalidadesPlanoOperacaoPK[ idFuncionalidade=" + idFuncionalidade + ", idPlano=" + idPlano + ", idOperacao=" + idOperacao + " ]";
    }

}
