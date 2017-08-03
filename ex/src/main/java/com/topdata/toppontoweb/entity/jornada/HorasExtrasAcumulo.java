/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.entity.jornada;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "HorasExtrasAcumulo")
@XmlRootElement
public class HorasExtrasAcumulo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdHorasExtrasAcumulo")
    private Integer idHorasExtrasAcumulo;
    @Size(max = 20)
    @Column(name = "Descricao")
    private String descricao;
    @OneToMany(mappedBy = "horasExtrasAcumulo")
    private List<Jornada> jornadaList;

    public HorasExtrasAcumulo() {
    }

    public HorasExtrasAcumulo(Integer idHorasExtrasAcumulo) {
        this.idHorasExtrasAcumulo = idHorasExtrasAcumulo;
    }

    public Integer getIdHorasExtrasAcumulo() {
        return idHorasExtrasAcumulo;
    }

    public void setIdHorasExtrasAcumulo(Integer idHorasExtrasAcumulo) {
        this.idHorasExtrasAcumulo = idHorasExtrasAcumulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Jornada> getJornadaList() {
        return jornadaList;
    }

    public void setJornadaList(List<Jornada> jornadaList) {
        this.jornadaList = jornadaList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorasExtrasAcumulo != null ? idHorasExtrasAcumulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof HorasExtrasAcumulo)) {
            return false;
        }
        HorasExtrasAcumulo other = (HorasExtrasAcumulo) object;
        if ((this.idHorasExtrasAcumulo == null && other.idHorasExtrasAcumulo != null) || (this.idHorasExtrasAcumulo != null && !this.idHorasExtrasAcumulo.equals(other.idHorasExtrasAcumulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.HorasExtrasAcumulo[ idHorasExtrasAcumulo=" + idHorasExtrasAcumulo + " ]";
    }
    
}
