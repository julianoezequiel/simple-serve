package com.topdata.toppontoweb.entity.tipo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.jornada.Jornada;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Semana")
@XmlRootElement
public class Semana implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSemana")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idSemana;

    @Size(max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(mappedBy = "semana")
    private List<Jornada> jornadaList;

    public Semana() {
    }

    public Semana(Integer idSemana) {
        this.idSemana = idSemana;
    }

    public Semana(Integer idSemana, String descricao) {
        this.idSemana = idSemana;
        this.descricao = descricao;
    }

    public Integer getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(Integer idSemana) {
        this.idSemana = idSemana;
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
        hash += (idSemana != null ? idSemana.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Semana)) {
            return false;
        }
        Semana other = (Semana) object;
        if ((this.idSemana == null && other.idSemana != null) || (this.idSemana != null && !this.idSemana.equals(other.idSemana))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Semana";
    }

}
