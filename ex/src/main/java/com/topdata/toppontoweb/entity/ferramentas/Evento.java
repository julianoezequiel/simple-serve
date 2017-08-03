package com.topdata.toppontoweb.entity.ferramentas;

import java.util.List;

import javax.persistence.Basic;
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
@Table(name = "Evento")
@XmlRootElement
public class Evento implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdEvento")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idEvento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(mappedBy = "evento")
    private List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList;

    public Evento() {
    }

    public Evento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public Evento(Integer idEvento, String descricao) {
        this.idEvento = idEvento;
        this.descricao = descricao;
    }

    public Integer getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Integer idEvento) {
        this.idEvento = idEvento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<LayoutGrupoEventosMotivos> getLayoutGrupoEventosMotivosList() {
        return layoutGrupoEventosMotivosList;
    }

    public void setLayoutGrupoEventosMotivosList(List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivosList) {
        this.layoutGrupoEventosMotivosList = layoutGrupoEventosMotivosList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEvento != null ? idEvento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Evento)) {
            return false;
        }
        Evento other = (Evento) object;
        if ((this.idEvento == null && other.idEvento != null) || (this.idEvento != null && !this.idEvento.equals(other.idEvento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Evento " + this.descricao != null ? this.descricao : "";
    }

}
