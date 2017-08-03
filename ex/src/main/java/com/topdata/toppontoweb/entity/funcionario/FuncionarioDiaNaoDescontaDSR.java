package com.topdata.toppontoweb.entity.funcionario;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioDiaNaoDescontaDSR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FuncionarioDiaNaoDescontaDSR.findAll", query = "SELECT f FROM FuncionarioDiaNaoDescontaDSR f"),
    @NamedQuery(name = "FuncionarioDiaNaoDescontaDSR.findByIdFuncionarioDiaNaoDescontaDSR", query = "SELECT f FROM FuncionarioDiaNaoDescontaDSR f WHERE f.idFuncionarioDiaNaoDescontaDSR = :idFuncionarioDiaNaoDescontaDSR"),
    @NamedQuery(name = "FuncionarioDiaNaoDescontaDSR.findByData", query = "SELECT f FROM FuncionarioDiaNaoDescontaDSR f WHERE f.data = :data")})
public class FuncionarioDiaNaoDescontaDSR implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionarioDiaNaoDescontaDSR")
    private Integer idFuncionarioDiaNaoDescontaDSR;
    @Column(name = "Data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario idFuncionario;

    public FuncionarioDiaNaoDescontaDSR() {
    }

    public FuncionarioDiaNaoDescontaDSR(Integer idFuncionarioDiaNaoDescontaDSR) {
        this.idFuncionarioDiaNaoDescontaDSR = idFuncionarioDiaNaoDescontaDSR;
    }

    public Integer getIdFuncionarioDiaNaoDescontaDSR() {
        return idFuncionarioDiaNaoDescontaDSR;
    }

    public void setIdFuncionarioDiaNaoDescontaDSR(Integer idFuncionarioDiaNaoDescontaDSR) {
        this.idFuncionarioDiaNaoDescontaDSR = idFuncionarioDiaNaoDescontaDSR;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionarioDiaNaoDescontaDSR != null ? idFuncionarioDiaNaoDescontaDSR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioDiaNaoDescontaDSR)) {
            return false;
        }
        FuncionarioDiaNaoDescontaDSR other = (FuncionarioDiaNaoDescontaDSR) object;
        if ((this.idFuncionarioDiaNaoDescontaDSR == null && other.idFuncionarioDiaNaoDescontaDSR != null) || (this.idFuncionarioDiaNaoDescontaDSR != null && !this.idFuncionarioDiaNaoDescontaDSR.equals(other.idFuncionarioDiaNaoDescontaDSR))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.FuncionarioDiaNaoDescontaDSR[ idFuncionarioDiaNaoDescontaDSR=" + idFuncionarioDiaNaoDescontaDSR + " ]";
    }

}
