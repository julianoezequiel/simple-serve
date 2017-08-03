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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FechamentoBHTabelaAcrescimos")
@XmlRootElement
public class FechamentoBHTabelaAcrescimos implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFechamentoBHTabelaAcrescimos")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idFechamentoBHTabelaAcrescimos;

    @Size(max = 45)
    @Column(name = "TipodeDia")
    private String tipodeDia;

    @Column(name = "Limite")
    private Integer limite;

    @Column(name = "Horas")
    @Temporal(TemporalType.TIME)
    private Date horas;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Percentual")
    private Double percentual;

    @Column(name = "DivisaoDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date divisaoDiurnas;

    @Column(name = "ResultadoDiurnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resultadoDiurnas;

    @Column(name = "DivisaoNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date divisaoNoturnas;

    @Column(name = "ResultadoNoturnas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resultadoNoturnas;

    @JoinColumn(name = "idEmpresaFechamentoData", referencedColumnName = "idEmpresaFechamentoData")
    @ManyToOne(optional = false)
    private EmpresaFechamentoData idEmpresaFechamentoData;

    public FechamentoBHTabelaAcrescimos() {
    }

    public FechamentoBHTabelaAcrescimos(Integer idFechamentoBHTabelaAcrescimos) {
        this.idFechamentoBHTabelaAcrescimos = idFechamentoBHTabelaAcrescimos;
    }

    public Integer getIdFechamentoBHTabelaAcrescimos() {
        return idFechamentoBHTabelaAcrescimos;
    }

    public void setIdFechamentoBHTabelaAcrescimos(Integer idFechamentoBHTabelaAcrescimos) {
        this.idFechamentoBHTabelaAcrescimos = idFechamentoBHTabelaAcrescimos;
    }

    public String getTipodeDia() {
        return tipodeDia;
    }

    public void setTipodeDia(String tipodeDia) {
        this.tipodeDia = tipodeDia;
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

    public Date getDivisaoDiurnas() {
        return divisaoDiurnas;
    }

    public void setDivisaoDiurnas(Date divisaoDiurnas) {
        this.divisaoDiurnas = divisaoDiurnas;
    }

    public Date getResultadoDiurnas() {
        return resultadoDiurnas;
    }

    public void setResultadoDiurnas(Date resultadoDiurnas) {
        this.resultadoDiurnas = resultadoDiurnas;
    }

    public Date getDivisaoNoturnas() {
        return divisaoNoturnas;
    }

    public void setDivisaoNoturnas(Date divisaoNoturnas) {
        this.divisaoNoturnas = divisaoNoturnas;
    }

    public Date getResultadoNoturnas() {
        return resultadoNoturnas;
    }

    public void setResultadoNoturnas(Date resultadoNoturnas) {
        this.resultadoNoturnas = resultadoNoturnas;
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
        hash += (idFechamentoBHTabelaAcrescimos != null ? idFechamentoBHTabelaAcrescimos.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechamentoBHTabelaAcrescimos)) {
            return false;
        }
        FechamentoBHTabelaAcrescimos other = (FechamentoBHTabelaAcrescimos) object;
        if ((this.idFechamentoBHTabelaAcrescimos == null && other.idFechamentoBHTabelaAcrescimos != null) || (this.idFechamentoBHTabelaAcrescimos != null && !this.idFechamentoBHTabelaAcrescimos.equals(other.idFechamentoBHTabelaAcrescimos))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fechamento BH tabela acrescimos ";
    }

}
