/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.entity.marcacoes;

import com.topdata.toppontoweb.entity.Entidade;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "EncaixeMarcacao")
@XmlRootElement
public class EncaixeMarcacao implements Entidade{
    
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idEncaixeMarcacao")
    private Integer idEncaixeMarcacao;
    @Size(max = 45)
    @Column(name = "Descricao")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEncaixeMarcacao")
    private List<Marcacoes> marcacoesList;

    public EncaixeMarcacao() {
    }

    public EncaixeMarcacao(Integer idEncaixeMarcacao) {
        this.idEncaixeMarcacao = idEncaixeMarcacao;
    }

    public Integer getIdEncaixeMarcacao() {
        return idEncaixeMarcacao;
    }

    public void setIdEncaixeMarcacao(Integer idEncaixeMarcacao) {
        this.idEncaixeMarcacao = idEncaixeMarcacao;
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
        hash += (idEncaixeMarcacao != null ? idEncaixeMarcacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EncaixeMarcacao)) {
            return false;
        }
        EncaixeMarcacao other = (EncaixeMarcacao) object;
        if ((this.idEncaixeMarcacao == null && other.idEncaixeMarcacao != null) || (this.idEncaixeMarcacao != null && !this.idEncaixeMarcacao.equals(other.idEncaixeMarcacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.EncaixeMarcacao[ idEncaixeMarcacao=" + idEncaixeMarcacao + " ]";
    }
    
}
