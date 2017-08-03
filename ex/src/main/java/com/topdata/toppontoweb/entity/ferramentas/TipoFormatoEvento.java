package com.topdata.toppontoweb.entity.ferramentas;

import java.io.Serializable;
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
 * @version 1.0.0 data 20/07/2017
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoFormatoEvento")
@XmlRootElement
public class TipoFormatoEvento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoFormatoEvento")
    private Integer idTipoFormatoEvento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoFormatoEvento")
    private List<LayoutArquivoGrupo> layoutArquivoGrupoList;

    public TipoFormatoEvento() {
    }

    public TipoFormatoEvento(Integer idTipoFormatoEvento) {
        this.idTipoFormatoEvento = idTipoFormatoEvento;
    }

    public TipoFormatoEvento(Integer idTipoFormatoEvento, String descricao) {
        this.idTipoFormatoEvento = idTipoFormatoEvento;
        this.descricao = descricao;
    }

    public Integer getIdTipoFormatoEvento() {
        return idTipoFormatoEvento;
    }

    public void setIdTipoFormatoEvento(Integer idTipoFormatoEvento) {
        this.idTipoFormatoEvento = idTipoFormatoEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutArquivoGrupo> getLayoutArquivoGrupoList() {
        return layoutArquivoGrupoList;
    }

    public void setLayoutArquivoGrupoList(List<LayoutArquivoGrupo> layoutArquivoGrupoList) {
        this.layoutArquivoGrupoList = layoutArquivoGrupoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoFormatoEvento != null ? idTipoFormatoEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoFormatoEvento)) {
            return false;
        }
        TipoFormatoEvento other = (TipoFormatoEvento) object;
        if ((this.idTipoFormatoEvento == null && other.idTipoFormatoEvento != null) || (this.idTipoFormatoEvento != null && !this.idTipoFormatoEvento.equals(other.idTipoFormatoEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.ferramentas.TipoFormatoEvento[ idTipoFormatoEvento=" + idTipoFormatoEvento + " ]";
    }
    
}
