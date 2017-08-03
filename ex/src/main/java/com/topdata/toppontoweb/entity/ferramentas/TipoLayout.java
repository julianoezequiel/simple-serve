package com.topdata.toppontoweb.entity.ferramentas;

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

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.SequenceGenerator;

/**
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoLayout")
@XmlRootElement
public class TipoLayout implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoLayout")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idTipoLayout;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoLayout")
    private List<LayoutArquivo> layoutArquivoList;

    public TipoLayout() {
    }

    public TipoLayout(Integer idTipoLayout) {
        this.idTipoLayout = idTipoLayout;
    }

    public TipoLayout(Integer idTipoLayout, String descricao) {
        this.idTipoLayout = idTipoLayout;
        this.descricao = descricao;
    }

    public Integer getIdTipoLayout() {
        return idTipoLayout;
    }

    public void setIdTipoLayout(Integer idTipoLayout) {
        this.idTipoLayout = idTipoLayout;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutArquivo> getLayoutArquivoList() {
        return layoutArquivoList;
    }

    public void setLayoutArquivoList(List<LayoutArquivo> layoutArquivoList) {
        this.layoutArquivoList = layoutArquivoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoLayout != null ? idTipoLayout.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoLayout)) {
            return false;
        }
        TipoLayout other = (TipoLayout) object;
        if ((this.idTipoLayout == null && other.idTipoLayout != null) || (this.idTipoLayout != null && !this.idTipoLayout.equals(other.idTipoLayout))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.ferramentas.TipoLayout[ idTipoLayout=" + idTipoLayout + " ]";
    }

}
