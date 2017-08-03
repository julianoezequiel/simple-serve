package com.topdata.toppontoweb.entity.fechamento;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "EmpresaFechamentoPeriodo")
@XmlRootElement
public class EmpresaFechamentoPeriodo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEmpresaFechamentoPeriodo")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idEmpresaFechamentoPeriodo;

    @Column(name = "Inicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date inicio;

    @Column(name = "Termino")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date termino;

    @Column(name = "DSRExtras")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dSRExtras;

    @Column(name = "DSRFaltas")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dSRFaltas;

    @Column(name = "PossuiInconsistencia")
    private Boolean possuiInconsistencia;

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresaFechamentoPeriodo", orphanRemoval = true)
    private List<EmpresaFechamentoData> empresaFechamentoDataList;

    public EmpresaFechamentoPeriodo() {
    }

    public EmpresaFechamentoPeriodo(Date inicio, Date termino, Boolean possuiInconsistencia, Empresa idEmpresa) {
        this.inicio = inicio;
        this.termino = termino;
        this.possuiInconsistencia = possuiInconsistencia;
        this.idEmpresa = idEmpresa;
    }

    public EmpresaFechamentoPeriodo(Integer idEmpresaFechamentoPeriodo) {
        this.idEmpresaFechamentoPeriodo = idEmpresaFechamentoPeriodo;
    }

    public Integer getIdEmpresaFechamentoPeriodo() {
        return idEmpresaFechamentoPeriodo;
    }

    public void setIdEmpresaFechamentoPeriodo(Integer idEmpresaFechamentoPeriodo) {
        this.idEmpresaFechamentoPeriodo = idEmpresaFechamentoPeriodo;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getTermino() {
        return termino;
    }

    public void setTermino(Date termino) {
        this.termino = termino;
    }

    public Date getDSRExtras() {
        return dSRExtras;
    }

    public void setDSRExtras(Date dSRExtras) {
        this.dSRExtras = dSRExtras;
    }

    public Date getDSRFaltas() {
        return dSRFaltas;
    }

    public void setDSRFaltas(Date dSRFaltas) {
        this.dSRFaltas = dSRFaltas;
    }

    public Boolean getPossuiInconsistencia() {
        return possuiInconsistencia;
    }

    public void setPossuiInconsistencia(Boolean possuiInconsistencia) {
        this.possuiInconsistencia = possuiInconsistencia;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @XmlTransient
    @JsonIgnore
    public List<EmpresaFechamentoData> getEmpresaFechamentoDataList() {
        return empresaFechamentoDataList;
    }

    public void setEmpresaFechamentoDataList(List<EmpresaFechamentoData> empresaFechamentoDataList) {
        this.empresaFechamentoDataList = empresaFechamentoDataList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpresaFechamentoPeriodo != null ? idEmpresaFechamentoPeriodo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresaFechamentoPeriodo)) {
            return false;
        }
        EmpresaFechamentoPeriodo other = (EmpresaFechamentoPeriodo) object;
        return !((this.idEmpresaFechamentoPeriodo == null && other.idEmpresaFechamentoPeriodo != null) || (this.idEmpresaFechamentoPeriodo != null && !this.idEmpresaFechamentoPeriodo.equals(other.idEmpresaFechamentoPeriodo)));
    }

    @Override
    public String toString() {
        return "Empresa fechamento período";
    }

}
