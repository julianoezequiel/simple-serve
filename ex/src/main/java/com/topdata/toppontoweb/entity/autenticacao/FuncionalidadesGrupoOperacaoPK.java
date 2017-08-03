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
public class FuncionalidadesGrupoOperacaoPK implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionalidade")
    private int idFuncionalidade;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdGrupo")
    private int idGrupo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IdOperacao")
    private int idOperacao;

    public FuncionalidadesGrupoOperacaoPK() {
    }

    public FuncionalidadesGrupoOperacaoPK(int idFuncionalidade, int idGrupo, int idOperacao) {
        this.idFuncionalidade = idFuncionalidade;
        this.idGrupo = idGrupo;
        this.idOperacao = idOperacao;
    }

    public int getIdFuncionalidade() {
        return idFuncionalidade;
    }

    public void setIdFuncionalidade(int idFuncionalidade) {
        this.idFuncionalidade = idFuncionalidade;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
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
        hash += (int) idGrupo;
        hash += (int) idOperacao;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionalidadesGrupoOperacaoPK)) {
            return false;
        }
        FuncionalidadesGrupoOperacaoPK other = (FuncionalidadesGrupoOperacaoPK) object;
        if (this.idFuncionalidade != other.idFuncionalidade) {
            return false;
        }
        if (this.idGrupo != other.idGrupo) {
            return false;
        }
        if (this.idOperacao != other.idOperacao) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FuncionalidadesGrupoOperacaoPK[ idFuncionalidade=" + idFuncionalidade + ", idGrupo=" + idGrupo + ", idOperacao=" + idOperacao + " ]";
    }

}
