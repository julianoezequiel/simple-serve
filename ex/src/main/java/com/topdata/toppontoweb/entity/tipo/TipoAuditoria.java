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
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * @version 1.0
 * @since 1.0.3 data 04/05/2016
 * @see Auditoria
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "TipoAuditoria")
@XmlRootElement
public class TipoAuditoria implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAuditoria")
    private List<Auditoria> auditoriaList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoAuditoria")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idTipoAuditoria;

    @Size(max = 45)
    @Column(name = "Descricao")
    private String descricao;

    public TipoAuditoria() {
    }

    public TipoAuditoria(Integer idTipoAuditoria) {
        this.idTipoAuditoria = idTipoAuditoria;
    }

    public TipoAuditoria(Integer idTipoAuditoria, String descricao) {
        this.idTipoAuditoria = idTipoAuditoria;
        this.descricao = descricao;
    }

    public TipoAuditoria(CONSTANTES.Enum_AUDITORIA auditoria) {
        this.descricao = auditoria.getDescricao();
        this.idTipoAuditoria = auditoria.ordinal();
    }

    public Integer getIdTipoAuditoria() {
        return idTipoAuditoria;
    }

    public void setIdTipoAuditoria(Integer idTipoAuditoria) {
        this.idTipoAuditoria = idTipoAuditoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAuditoria != null ? idTipoAuditoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAuditoria)) {
            return false;
        }
        TipoAuditoria other = (TipoAuditoria) object;
        if ((this.idTipoAuditoria == null && other.idTipoAuditoria != null) || (this.idTipoAuditoria != null && !this.idTipoAuditoria.equals(other.idTipoAuditoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tipo auditoria ";
    }

    @XmlTransient
    @JsonIgnore
    public List<Auditoria> getAuditoriaList() {
        return auditoriaList;
    }

    public void setAuditoriaList(List<Auditoria> auditoriaList) {
        this.auditoriaList = auditoriaList;
    }

}
