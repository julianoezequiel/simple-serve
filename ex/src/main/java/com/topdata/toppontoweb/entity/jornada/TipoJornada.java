package com.topdata.toppontoweb.entity.jornada;

import com.topdata.toppontoweb.entity.Entidade;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @version 1.0.6 data 08/12/2016
 * @since 1.0.6 data 08/12/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoJornada")
@XmlRootElement
public class TipoJornada implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoJornada")
    private Integer idTipoJornada;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoJornada")
    private List<Jornada> jornadaList;

    public TipoJornada() {
    }

    public TipoJornada(Integer idTipoJornada) {
        this.idTipoJornada = idTipoJornada;
    }

    public TipoJornada(Integer idTipoJornada, String descricao) {
        this.idTipoJornada = idTipoJornada;
        this.descricao = descricao;
    }

    public Integer getIdTipoJornada() {
        return idTipoJornada;
    }

    public void setIdTipoJornada(Integer idTipoJornada) {
        this.idTipoJornada = idTipoJornada;
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
        hash += (idTipoJornada != null ? idTipoJornada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoJornada)) {
            return false;
        }
        TipoJornada other = (TipoJornada) object;
        if ((this.idTipoJornada == null && other.idTipoJornada != null) || (this.idTipoJornada != null && !this.idTipoJornada.equals(other.idTipoJornada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.jornada.TipoJornada[ idTipoJornada=" + idTipoJornada + " ]";
    }

}
