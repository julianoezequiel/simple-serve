package com.topdata.toppontoweb.entity.tipo;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Motivo;

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.1
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoMotivo")
@XmlRootElement
public class TipoMotivo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdTipoMotivo")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idTipoMotivo;

    @Size(max = 30)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTipoMotivo")
    private List<Motivo> motivoList;

    public TipoMotivo() {
    }

    public TipoMotivo(Integer idTipoMotivo) {
        this.idTipoMotivo = idTipoMotivo;
    }

    public TipoMotivo(Integer idTipoMotivo, String descricao) {
        this.idTipoMotivo = idTipoMotivo;
        this.descricao = descricao;
    }

    public Integer getIdTipoMotivo() {
        return idTipoMotivo;
    }

    public void setIdTipoMotivo(Integer idTipoMotivo) {
        this.idTipoMotivo = idTipoMotivo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Motivo> getMotivoList() {
        return motivoList;
    }

    public void setMotivoList(List<Motivo> motivoList) {
        this.motivoList = motivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoMotivo != null ? idTipoMotivo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoMotivo)) {
            return false;
        }
        TipoMotivo other = (TipoMotivo) object;
        if ((this.idTipoMotivo == null && other.idTipoMotivo != null) || (this.idTipoMotivo != null && !this.idTipoMotivo.equals(other.idTipoMotivo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tipo motivo " + this.descricao;
    }

}
