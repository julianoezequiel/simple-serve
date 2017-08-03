package com.topdata.toppontoweb.entity.autenticacao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Departamento;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Grupo")
@XmlRootElement
public class Grupo implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdGrupo")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idGrupo;

    @Size(max = 30)
    @Column(name = "Descricao")
    private String descricao = "Novo Grupo";

    @JoinTable(name = "GrupoPermissoes", joinColumns = {
        @JoinColumn(name = "IdGrupo", referencedColumnName = "IdGrupo")}, inverseJoinColumns = {
        @JoinColumn(name = "IdPermissoes", referencedColumnName = "IdPermissoes")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Permissoes> permissoesList = new ArrayList<>();

    @JoinTable(name = "DepartamentoGrupo", joinColumns = {
        @JoinColumn(name = "IdGrupo", referencedColumnName = "IdGrupo")}, inverseJoinColumns = {
        @JoinColumn(name = "IdDepartamento", referencedColumnName = "IdDepartamento")})
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Departamento> departamentoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<Operador> operadorList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "grupo")
    private List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList = new ArrayList<>();

    //Utilizsado para enviar alguma mensagem para o front-end
    //Está sendo utilisado para guardar mensagens de erro de uma exclusão em massa
    @Transient
    private String mensagem = "";

    public Grupo() {
    }

    public Grupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Grupo(int i, String GRUPO_MASTER) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Grupo(String descricao) {
        this.descricao = descricao;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Permissoes> getPermissoesList() {
        return permissoesList;
    }

    public void setPermissoesList(List<Permissoes> permissoesList) {
        this.permissoesList = permissoesList;
    }

    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

    public void setDepartamentoList(List<Departamento> departamentoList) {
        this.departamentoList = departamentoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Operador> getOperadorList() {
        return operadorList;
    }

    public void setOperadorList(List<Operador> operadorList) {
        this.operadorList = operadorList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionalidadesGrupoOperacao> getFuncionalidadesGrupoOperacaoList() {
        return funcionalidadesGrupoOperacaoList;
    }

    public void setFuncionalidadesGrupoOperacaoList(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList) {
        this.funcionalidadesGrupoOperacaoList = funcionalidadesGrupoOperacaoList;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupo != null ? idGrupo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Grupo)) {
            return false;
        }
        Grupo other = (Grupo) object;
        if ((this.idGrupo == null && other.idGrupo != null) || (this.idGrupo != null && !this.idGrupo.equals(other.idGrupo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Grupo";
    }

}
