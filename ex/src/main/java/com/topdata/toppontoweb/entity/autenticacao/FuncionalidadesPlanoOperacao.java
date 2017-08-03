package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionalidadesPlanoOperacao")
@XmlRootElement
public class FuncionalidadesPlanoOperacao implements Entidade {

    @EmbeddedId
    protected FuncionalidadesPlanoOperacaoPK funcionalidadesPlanoOperacaoPK = new FuncionalidadesPlanoOperacaoPK();

    @JoinColumn(name = "idFuncionalidade", referencedColumnName = "idFuncionalidade", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Funcionalidades funcionalidades;

    @JoinColumn(name = "IdOperacao", referencedColumnName = "IdOperacao", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Operacao operacao;

    @JoinColumn(name = "idPlano", referencedColumnName = "idPlano", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Planos planos;

    public FuncionalidadesPlanoOperacao() {
    }

    public FuncionalidadesPlanoOperacao(FuncionalidadesPlanoOperacaoPK funcionalidadesPlanoOperacaoPK) {
        this.funcionalidadesPlanoOperacaoPK = funcionalidadesPlanoOperacaoPK;
    }

    public FuncionalidadesPlanoOperacao(int idFuncionalidade, int idPlano, int idOperacao) {
        this.funcionalidadesPlanoOperacaoPK = new FuncionalidadesPlanoOperacaoPK(idFuncionalidade, idPlano, idOperacao);
    }

    public FuncionalidadesPlanoOperacaoPK getFuncionalidadesPlanoOperacaoPK() {
        return funcionalidadesPlanoOperacaoPK;
    }

    public void setFuncionalidadesPlanoOperacaoPK(FuncionalidadesPlanoOperacaoPK funcionalidadesPlanoOperacaoPK) {
        this.funcionalidadesPlanoOperacaoPK = funcionalidadesPlanoOperacaoPK;
    }

    public Funcionalidades getFuncionalidades() {
        return funcionalidades;
    }

    public void setFuncionalidades(Funcionalidades funcionalidades) {
        this.funcionalidades = funcionalidades;
        this.funcionalidadesPlanoOperacaoPK.setIdFuncionalidade(funcionalidades.getId());
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
        this.funcionalidadesPlanoOperacaoPK.setIdOperacao(operacao.getIdOperacao());
    }

    public Planos getPlanos() {
        return planos;
    }

    public void setPlanos(Planos planos) {
        this.planos = planos;
        this.funcionalidadesPlanoOperacaoPK.setIdPlano(planos.getIdPlano());
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (funcionalidadesPlanoOperacaoPK != null ? funcionalidadesPlanoOperacaoPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionalidadesPlanoOperacao)) {
            return false;
        }
        FuncionalidadesPlanoOperacao other = (FuncionalidadesPlanoOperacao) object;
        if ((this.funcionalidadesPlanoOperacaoPK == null && other.funcionalidadesPlanoOperacaoPK != null) || (this.funcionalidadesPlanoOperacaoPK != null && !this.funcionalidadesPlanoOperacaoPK.equals(other.funcionalidadesPlanoOperacaoPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.FuncionalidadesPlanoOperacao[ funcionalidadesPlanoOperacaoPK=" + funcionalidadesPlanoOperacaoPK + " ]";
    }

}
