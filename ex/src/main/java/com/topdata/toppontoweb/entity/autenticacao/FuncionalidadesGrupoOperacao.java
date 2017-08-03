package com.topdata.toppontoweb.entity.autenticacao;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.Operacao;

/**
 * @version 1.0.4 data 09/06/2016
 * @since 1.0.4 data 09/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionalidadesGrupoOperacao")
@XmlRootElement
public class FuncionalidadesGrupoOperacao implements Entidade {

    @EmbeddedId
    protected FuncionalidadesGrupoOperacaoPK funcionalidadesGrupoOperacaoPK = new FuncionalidadesGrupoOperacaoPK();

    @JoinColumn(name = "idFuncionalidade", referencedColumnName = "idFuncionalidade", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionalidades funcionalidades;

    @JoinColumn(name = "IdGrupo", referencedColumnName = "IdGrupo", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Grupo grupo;

    @JoinColumn(name = "IdOperacao", referencedColumnName = "IdOperacao", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Operacao operacao;

    public FuncionalidadesGrupoOperacao() {
    }

    public FuncionalidadesGrupoOperacao(FuncionalidadesGrupoOperacaoPK funcionalidadesGrupoOperacaoPK) {
        this.funcionalidadesGrupoOperacaoPK = funcionalidadesGrupoOperacaoPK;
    }

    public FuncionalidadesGrupoOperacao(int idFuncionalidade, int idGrupo, int idOperacao) {
        this.funcionalidadesGrupoOperacaoPK = new FuncionalidadesGrupoOperacaoPK(idFuncionalidade, idGrupo, idOperacao);
    }

    public FuncionalidadesGrupoOperacaoPK getFuncionalidadesGrupoOperacaoPK() {
        return funcionalidadesGrupoOperacaoPK;
    }

    public void setFuncionalidadesGrupoOperacaoPK(FuncionalidadesGrupoOperacaoPK funcionalidadesGrupoOperacaoPK) {
        this.funcionalidadesGrupoOperacaoPK = funcionalidadesGrupoOperacaoPK;
    }

    public Funcionalidades getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(Funcionalidades funcionalidades) {
        this.funcionalidades = funcionalidades;
        this.funcionalidadesGrupoOperacaoPK.setIdFuncionalidade(funcionalidades.getId());
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
        this.funcionalidadesGrupoOperacaoPK.setIdGrupo(grupo.getIdGrupo());
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
        this.funcionalidadesGrupoOperacaoPK.setIdOperacao(operacao.getIdOperacao());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcionalidadesGrupoOperacaoPK != null ? funcionalidadesGrupoOperacaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionalidadesGrupoOperacao)) {
            return false;
        }
        FuncionalidadesGrupoOperacao other = (FuncionalidadesGrupoOperacao) object;
        if ((this.funcionalidadesGrupoOperacaoPK == null && other.funcionalidadesGrupoOperacaoPK != null) || (this.funcionalidadesGrupoOperacaoPK != null && !this.funcionalidadesGrupoOperacaoPK.equals(other.funcionalidadesGrupoOperacaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FuncionalidadesGrupoOperacao[ funcionalidadesGrupoOperacaoPK=" + funcionalidadesGrupoOperacaoPK + " ]";
    }

}
