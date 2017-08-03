package com.topdata.toppontoweb.entity.empresa;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDepartamento;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.utils.constantes.Enum_MotivoNaoEditavel;
import java.util.ArrayList;
import javax.persistence.FetchType;
import javax.persistence.Transient;

/**
 * @version 1.0.0.3 data 06/10/2016
 * @version 1.0.0.2 data 04/05/2016
 * @version 1.0.0.1 data 20/05/2016
 *
 * @since 1.0.0.0
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Departamento")
@XmlRootElement
public class Departamento implements Entidade {
    
    @ManyToMany(mappedBy = "departamentoList", fetch = FetchType.LAZY)
    private List<Grupo> grupoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamento")
    private List<Funcionario> funcionarioList;

    /**
     * @since 1.0.4 data 20/05/2016
     */
    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    @JsonView(JsonViews.Admin.class)
    private Empresa empresa;

    /**
     * @since 1.0.3 data 04/05/2016
     */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "departamento")
    private List<FuncionarioDepartamento> funcionarioDepartamentoList;

    @Id
    @Basic(optional = false)
    @Column(name = "IdDepartamento")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idDepartamento;

    @Size(max = 50)
    @Column(name = "Descricao")
    @JsonView(JsonViews.Audit.class)
    private String descricao;

    @Column(name = "Ativo")
    @JsonView(JsonViews.Audit.class)
    private Boolean ativo = true;

    @Transient
    private Boolean permitido;

    @Transient
    private Boolean editavel = true;

    @Transient
    private Enum_MotivoNaoEditavel motivoNaoEditavel;

    //Utilizsado para enviar alguma mensagem para o front-end
    //Está sendo utilisado no coletivo, como mensagem de "não editavel"
    @Transient
    private String mensagem = "";

    public Departamento() {
    }

    public Departamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public Departamento(Integer idDepartamento, String descricao, Empresa empresa) {
        this.idDepartamento = idDepartamento;
        this.descricao = descricao;
        this.empresa = empresa;
    }

    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    public Enum_MotivoNaoEditavel getMotivoNaoEditavel() {
        return motivoNaoEditavel;
    }

    public void setMotivoNaoEditavel(Enum_MotivoNaoEditavel motivoNaoEditavel) {
        this.motivoNaoEditavel = motivoNaoEditavel;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDepartamento != null ? idDepartamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departamento)) {
            return false;
        }
        Departamento other = (Departamento) object;
        if ((this.idDepartamento == null && other.idDepartamento != null) || (this.idDepartamento != null && !this.idDepartamento.equals(other.idDepartamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Departamento";
    }

    @XmlTransient
    @JsonIgnore
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioDepartamento> getFuncionarioDepartamentoList() {
        return funcionarioDepartamentoList;
    }

    public void setFuncionarioDepartamentoList(List<FuncionarioDepartamento> funcionarioDepartamentoList) {
        this.funcionarioDepartamentoList = funcionarioDepartamentoList;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @XmlTransient
    @JsonIgnore
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

}
