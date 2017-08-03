/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.entity.tipo;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
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
@Table(name = "TipoFechamento")
@XmlRootElement
public class TipoFechamento implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idTipoFechamento")
    private Integer idTipoFechamento;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoFechamento")
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList;

    public TipoFechamento() {
    }

    public TipoFechamento(Integer idTipoFechamento) {
        this.idTipoFechamento = idTipoFechamento;
    }

    public TipoFechamento(Integer idTipoFechamento, String descricao) {
        this.idTipoFechamento = idTipoFechamento;
        this.descricao = descricao;
    }

    public Integer getIdTipoFechamento() {
        return idTipoFechamento;
    }

    public void setIdTipoFechamento(Integer idTipoFechamento) {
        this.idTipoFechamento = idTipoFechamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoList() {
        return funcionarioBancoHorasFechamentoList;
    }

    public void setFuncionarioBancoHorasFechamentoList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList) {
        this.funcionarioBancoHorasFechamentoList = funcionarioBancoHorasFechamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoFechamento != null ? idTipoFechamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoFechamento)) {
            return false;
        }
        TipoFechamento other = (TipoFechamento) object;
        if ((this.idTipoFechamento == null && other.idTipoFechamento != null) || (this.idTipoFechamento != null && !this.idTipoFechamento.equals(other.idTipoFechamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Tipo de fechamento " + descricao;
    }

}
