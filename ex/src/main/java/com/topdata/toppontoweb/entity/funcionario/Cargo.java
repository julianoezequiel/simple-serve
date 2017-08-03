package com.topdata.toppontoweb.entity.funcionario;

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
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import java.util.ArrayList;

/**
 * @version 1.0.6 data 06/10/2016
 * @since 1.0.3 data 04/05/2016
 * @version 1.0.1 data 02/05/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Cargo")
@XmlRootElement
public class Cargo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdCargo")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idCargo;

    @Size(max = 30)
    @Column(name = "Descricao")
    @JsonView(JsonViews.Audit.class)
    private String descricao;


    @OneToMany(mappedBy = "cargo")
    private List<Funcionario> funcionarioList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "cargo")
    private List<HistoricoFuncionarioCargo> funcionarioCargoList = new ArrayList<>();

    public Cargo() {
    }

    public Cargo(String descricao) {
        this.descricao = descricao;
    }

    public Cargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public Cargo(Boolean ativo, Integer idCargo, String descricao) {
        this.idCargo = idCargo;
        this.descricao = descricao;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
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
        hash += (idCargo != null ? idCargo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cargo)) {
            return false;
        }
        Cargo other = (Cargo) object;
        if ((this.idCargo == null && other.idCargo != null) || (this.idCargo != null && !this.idCargo.equals(other.idCargo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Cargo";
    }


    @XmlTransient
    @JsonIgnore
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @XmlTransient
    @JsonIgnore
    public List<HistoricoFuncionarioCargo> getFuncionarioCargoList() {
        return funcionarioCargoList;
    }

    public void setFuncionarioCargoList(List<HistoricoFuncionarioCargo> funcionarioCargoList) {
        this.funcionarioCargoList = funcionarioCargoList;
    }

}
