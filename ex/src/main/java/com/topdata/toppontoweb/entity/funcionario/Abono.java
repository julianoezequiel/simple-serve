package com.topdata.toppontoweb.entity.funcionario;

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

import com.topdata.toppontoweb.entity.Entidade;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Abono")
@XmlRootElement
public class Abono implements Entidade {

    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAbono")
    private Integer idAbono;
    @Basic(optional = false)
    @NotNull
    @Column(name = "Data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Column(name = "HorasDiurnas")
    @Temporal(TemporalType.TIME)
    private Date horasDiurnas;
    @Column(name = "HorasNoturnas")
    @Temporal(TemporalType.TIME)
    private Date horasNoturnas;
    @Column(name = "DescontaBH")
    private Boolean descontaBH;
    @Column(name = "SomenteJustificativa")
    private Boolean somenteJustificativa;
    @JoinColumn(name = "IdFuncionario", referencedColumnName = "idFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;
    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne(optional = false)
    private Motivo motivo;

    public Abono() {
    }

    public Abono(Integer idAbono) {
        this.idAbono = idAbono;
    }

    public Abono(Integer idAbono, Date data) {
        this.idAbono = idAbono;
        this.data = data;
    }

    public Integer getIdAbono() {
        return idAbono;
    }

    public void setIdAbono(Integer idAbono) {
        this.idAbono = idAbono;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHorasDiurnas() {
        return horasDiurnas;
    }

    public void setHorasDiurnas(Date horasDiurnas) {
        this.horasDiurnas = horasDiurnas;
    }

    public Date getHorasNoturnas() {
        return horasNoturnas;
    }

    public void setHorasNoturnas(Date horasNoturnas) {
        this.horasNoturnas = horasNoturnas;
    }

    public Boolean getDescontaBH() {
        return descontaBH;
    }

    public void setDescontaBH(Boolean descontaBH) {
        this.descontaBH = descontaBH;
    }

    public Boolean getSomenteJustificativa() {
        return somenteJustificativa;
    }

    public void setSomenteJustificativa(Boolean somenteJustificativa) {
        this.somenteJustificativa = somenteJustificativa;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAbono != null ? idAbono.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Abono)) {
            return false;
        }
        Abono other = (Abono) object;
        if ((this.idAbono == null && other.idAbono != null) || (this.idAbono != null && !this.idAbono.equals(other.idAbono))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.Abono[ idAbono=" + idAbono + " ]";
    }
    
}
