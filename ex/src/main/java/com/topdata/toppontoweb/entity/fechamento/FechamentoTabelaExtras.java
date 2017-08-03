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
@Table(name = "FechamentoTabelaExtras")
@XmlRootElement
public class FechamentoTabelaExtras implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFechamentoTabelaExtras")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idFechamentoTabelaExtras;

    @Column(name = "Limite")
    private Integer limite;

    @Column(name = "Horas")
    @Temporal(TemporalType.TIME)
    private Date horas;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Percentual")
    private Double percentual;

    @Column(name = "Diurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date diurnas;

    @Column(name = "Noturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date noturnas;

    @JoinColumn(name = "idEmpresaFechamentoData", referencedColumnName = "idEmpresaFechamentoData")
    @ManyToOne(optional = false)
    private EmpresaFechamentoData idEmpresaFechamentoData;

    public FechamentoTabelaExtras() {
    }

    public FechamentoTabelaExtras(Integer idFechamentoTabelaExtras) {
        this.idFechamentoTabelaExtras = idFechamentoTabelaExtras;
    }

    public Integer getIdFechamentoTabelaExtras() {
        return idFechamentoTabelaExtras;
    }

    public void setIdFechamentoTabelaExtras(Integer idFechamentoTabelaExtras) {
        this.idFechamentoTabelaExtras = idFechamentoTabelaExtras;
    }

    public Integer getLimite() {
        return limite;
    }

    public void setLimite(Integer limite) {
        this.limite = limite;
    }

    public Date getHoras() {
        return horas;
    }

    public void setHoras(Date horas) {
        this.horas = horas;
    }

    public Double getPercentual() {
        return percentual;
    }

    public void setPercentual(Double percentual) {
        this.percentual = percentual;
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
        hash += (idFechamentoTabelaExtras != null ? idFechamentoTabelaExtras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechamentoTabelaExtras)) {
            return false;
        }
        FechamentoTabelaExtras other = (FechamentoTabelaExtras) object;
        if ((this.idFechamentoTabelaExtras == null && other.idFechamentoTabelaExtras != null) || (this.idFechamentoTabelaExtras != null && !this.idFechamentoTabelaExtras.equals(other.idFechamentoTabelaExtras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fechamento tabela extras ";
    }

}
