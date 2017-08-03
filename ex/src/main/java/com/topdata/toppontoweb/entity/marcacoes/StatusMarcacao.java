package com.topdata.toppontoweb.entity.marcacoes;

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
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "StatusMarcacao")
@XmlRootElement
public class StatusMarcacao implements Entidade {
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idStatusMarcacao")
    private Integer idStatusMarcacao;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "Descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idStatusMarcacao")
    private List<Marcacoes> marcacoesList;

    public StatusMarcacao() {
    }

    public StatusMarcacao(Integer idStatusMarcacao) {
        this.idStatusMarcacao = idStatusMarcacao;
    }

    public StatusMarcacao(Integer idStatusMarcacao, String descricao) {
        this.idStatusMarcacao = idStatusMarcacao;
        this.descricao = descricao;
    }

    public Integer getIdStatusMarcacao() {
        return idStatusMarcacao;
    }

    public void setIdStatusMarcacao(Integer idStatusMarcacao) {
        this.idStatusMarcacao = idStatusMarcacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Marcacoes> getMarcacoesList() {
        return marcacoesList;
    }

    public void setMarcacoesList(List<Marcacoes> marcacoesList) {
        this.marcacoesList = marcacoesList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStatusMarcacao != null ? idStatusMarcacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof StatusMarcacao)) {
            return false;
        }
        StatusMarcacao other = (StatusMarcacao) object;
        if ((this.idStatusMarcacao == null && other.idStatusMarcacao != null) || (this.idStatusMarcacao != null && !this.idStatusMarcacao.equals(other.idStatusMarcacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.StatusMarcacao[ idStatusMarcacao=" + idStatusMarcacao + " ]";
    }
    
}
