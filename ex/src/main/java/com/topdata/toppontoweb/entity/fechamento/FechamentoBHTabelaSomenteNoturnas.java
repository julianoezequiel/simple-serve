package com.topdata.toppontoweb.entity.fechamento;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FechamentoBHTabelaSomenteNoturnas")
@XmlRootElement
public class FechamentoBHTabelaSomenteNoturnas implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFechamentoBHTabelaSomenteNoturnas")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idFechamentoBHTabelaSomenteNoturnas;

    @Column(name = "Limite")
    private Integer limite;

    @Column(name = "Horas")
    @Temporal(TemporalType.TIME)
    private Date horas;

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "Percentual")
    private Double percentual;

    @Column(name = "Divisao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date divisao;

    @Column(name = "Resultado")
    @Temporal(TemporalType.TIMESTAMP)
    private Date resultado;

    @JoinColumn(name = "idEmpresaFechamentoData", referencedColumnName = "idEmpresaFechamentoData")
    @ManyToOne(optional = false)
    private EmpresaFechamentoData idEmpresaFechamentoData;

    public FechamentoBHTabelaSomenteNoturnas() {
    }

    public FechamentoBHTabelaSomenteNoturnas(Integer idFechamentoBHTabelaSomenteNoturnas) {
        this.idFechamentoBHTabelaSomenteNoturnas = idFechamentoBHTabelaSomenteNoturnas;
    }

    public Integer getIdFechamentoBHTabelaSomenteNoturnas() {
        return idFechamentoBHTabelaSomenteNoturnas;
    }

    public void setIdFechamentoBHTabelaSomenteNoturnas(Integer idFechamentoBHTabelaSomenteNoturnas) {
        this.idFechamentoBHTabelaSomenteNoturnas = idFechamentoBHTabelaSomenteNoturnas;
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

    public Date getDivisao() {
        return divisao;
    }

    public void setDivisao(Date divisao) {
        this.divisao = divisao;
    }

    public Date getResultado() {
        return resultado;
    }

    public void setResultado(Date resultado) {
        this.resultado = resultado;
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
        hash += (idFechamentoBHTabelaSomenteNoturnas != null ? idFechamentoBHTabelaSomenteNoturnas.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FechamentoBHTabelaSomenteNoturnas)) {
            return false;
        }
        FechamentoBHTabelaSomenteNoturnas other = (FechamentoBHTabelaSomenteNoturnas) object;
        if ((this.idFechamentoBHTabelaSomenteNoturnas == null && other.idFechamentoBHTabelaSomenteNoturnas != null) || (this.idFechamentoBHTabelaSomenteNoturnas != null && !this.idFechamentoBHTabelaSomenteNoturnas.equals(other.idFechamentoBHTabelaSomenteNoturnas))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fechamento BH tabela somente noturnas ";
    }

}
