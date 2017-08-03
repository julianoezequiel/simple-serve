package com.topdata.toppontoweb.entity.configuracoes;

import com.topdata.toppontoweb.entity.Entidade;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.Date;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "DSR")
@XmlRootElement
public class Dsr implements Entidade {

    @Basic(optional = false)
    @NotNull
    @Column(name = "LimiteHorasFaltas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date limiteHorasFaltas;

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne
    private Empresa empresa;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdDsr")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDsr;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IncluiHoraExtra")
    private boolean incluiHoraExtra;

    @Basic(optional = false)
    @NotNull
    @Column(name = "IncluiFeriadoComoHoraNormal")
    private boolean incluiFeriadoComoHoraNormal;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DescontaAusencia")
    private boolean descontaAusencia;

    @Column(name = "Integral")
    private Boolean integral;

    public Dsr() {
        this.limiteHorasFaltas = Utils.getDataDefault();
        this.integral=false;
        this.descontaAusencia=false;
        this.incluiFeriadoComoHoraNormal = false;
        this.incluiHoraExtra = false;
    }

    public Dsr(Integer idDsr) {
        this.idDsr = idDsr;
    }

    public Dsr(Integer idDsr, Date limiteHorasFaltas, boolean incluiHoraExtra, boolean incluiFeriadoComoHoraNormal, boolean descontaAusencia, Boolean integral) {
        this.idDsr = idDsr;
        this.limiteHorasFaltas = limiteHorasFaltas;
        this.incluiHoraExtra = incluiHoraExtra;
        this.incluiFeriadoComoHoraNormal = incluiFeriadoComoHoraNormal;
        this.descontaAusencia = descontaAusencia;
        this.integral = integral;
    }

    public Integer getIdDsr() {
        return idDsr;
    }

    public void setIdDsr(Integer idDsr) {
        this.idDsr = idDsr;
    }

    public boolean getIncluiHoraExtra() {
        return incluiHoraExtra;
    }

    public void setIncluiHoraExtra(boolean incluiHoraExtra) {
        this.incluiHoraExtra = incluiHoraExtra;
    }

    public boolean getIncluiFeriadoComoHoraNormal() {
        return incluiFeriadoComoHoraNormal;
    }

    public void setIncluiFeriadoComoHoraNormal(boolean incluiFeriadoComoHoraNormal) {
        this.incluiFeriadoComoHoraNormal = incluiFeriadoComoHoraNormal;
    }

    public void setDescontaAusencia(boolean descontaAusencia) {
        this.descontaAusencia = descontaAusencia;
    }

    public void setIntegral(Boolean integral) {
        this.integral = integral;
    }

    public Boolean getIntegral() {
        return integral;
    }

    public boolean getDescontaAusencia() {
        return descontaAusencia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDsr != null ? idDsr.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dsr)) {
            return false;
        }
        Dsr other = (Dsr) object;
        if ((this.idDsr == null && other.idDsr != null) || (this.idDsr != null && !this.idDsr.equals(other.idDsr))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dsr";
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Date getLimiteHorasFaltas() {
        return limiteHorasFaltas;
    }

    public void setLimiteHorasFaltas(Date limiteHorasFaltas) {
        this.limiteHorasFaltas = limiteHorasFaltas;
    }

}
