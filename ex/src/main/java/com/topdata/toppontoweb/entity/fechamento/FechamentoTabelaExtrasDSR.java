package com.topdata.toppontoweb.entity.fechamento;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FechamentoTabelaExtrasDSR")
@XmlRootElement
public class FechamentoTabelaExtrasDSR implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFechamentoTabelaExtrasDSR")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idFechamentoTabelaExtrasDSR;

    @Column(name = "Limite")
    private Integer limite;

    @Column(name = "Diurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diurnas;

    @Column(name = "Noturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date noturnas;

    @JoinColumn(name = "idEmpresaFechamentoData", referencedColumnName = "idEmpresaFechamentoData")
    @ManyToOne(optional = false)
    private EmpresaFechamentoData idEmpresaFechamentoData;

    public FechamentoTabelaExtrasDSR() {
    }

    public FechamentoTabelaExtrasDSR(Integer idFechamentoTabelaExtrasDSR) {
        this.idFechamentoTabelaExtrasDSR = idFechamentoTabelaExtrasDSR;
    }

    public Integer getIdFechamentoTabelaExtrasDSR() {
        return idFechamentoTabelaExtrasDSR;
    }

    public void setIdFechamentoTabelaExtrasDSR(Integer idFechamentoTabelaExtrasDSR) {
        this.idFechamentoTabelaExtrasDSR = idFechamentoTabelaExtrasDSR;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public Date getDiurnas() {
        return diurnas;
    }

    public void setDiurnas(Date diurnas) {
        this.diurnas = diurnas;
    }

    public Date getNoturnas() {
        return noturnas;
    }

    public void setNoturnas(Date noturnas) {
        this.noturnas = noturnas;
    }

    public EmpresaFechamentoData getIdEmpresaFechamentoData() {
        return idEmpresaFechamentoData;
    }

    public void setIdEmpresaFechamentoData(EmpresaFechamentoData idEmpresaFechamentoData) {
        this.idEmpresaFechamentoData = idEmpresaFechamentoData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFechamentoTabelaExtrasDSR != null ? idFechamentoTabelaExtrasDSR.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechamentoTabelaExtrasDSR)) {
            return false;
        }
        FechamentoTabelaExtrasDSR other = (FechamentoTabelaExtrasDSR) object;
        if ((this.idFechamentoTabelaExtrasDSR == null && other.idFechamentoTabelaExtrasDSR != null) || (this.idFechamentoTabelaExtrasDSR != null && !this.idFechamentoTabelaExtrasDSR.equals(other.idFechamentoTabelaExtrasDSR))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fechamento tabela extras DSR ";
    }

}
