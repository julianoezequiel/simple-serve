package com.topdata.toppontoweb.entity.tipo;

import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesPlanoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * @version 1.0.4 data 06/06/2016
 * @since 1.0.4 data 06/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Operacao")
@XmlRootElement
public class Operacao implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operacao")
    private List<FuncionalidadesPlanoOperacao> funcionalidadesPlanoOperacaoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operacao")
    private List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList;

    @ManyToMany(mappedBy = "operacaoList")
    private List<Funcionalidades> funcionalidadesList;

    @Transient
    private CONSTANTES.Enum_OPERACAO operacao;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdOperacao")
    @SequenceGenerator(name = "seq_usu", initialValue = 0)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idOperacao;

    @Size(max = 45)
    @Column(name = "Descricao")
    private String descricao;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operacao")
    private List<Auditoria> auditoriaList;

    public Operacao() {
    }

    public Operacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }

    public Operacao(Integer idOperacao, String descricao) {
        this.idOperacao = idOperacao;
        this.descricao = descricao;
    }

    public Operacao(CONSTANTES.Enum_OPERACAO operacao) {
        this.descricao = operacao.getDescricao();
        this.idOperacao = operacao.ordinal();
        this.operacao = operacao;
    }

    public Integer getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(Integer idOperacao) {
        this.idOperacao = idOperacao;
    }

    @JsonView(JsonViews.Audit.class)
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<Auditoria> getAuditoriaList() {
        return auditoriaList;
    }

    public void setAuditoriaList(List<Auditoria> auditoriaList) {
        this.auditoriaList = auditoriaList;
    }

    public CONSTANTES.Enum_OPERACAO getOperacao() {
        for (CONSTANTES.Enum_OPERACAO operacao1 : CONSTANTES.Enum_OPERACAO.values()) {
            if (operacao1.ordinal() == this.idOperacao) {
                this.operacao = operacao1;
            }
        }
        return this.operacao;
    }

    public void setOperacao(CONSTANTES.Enum_OPERACAO operacao) {
        this.operacao = operacao;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.idOperacao);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Operacao other = (Operacao) obj;
        if (!Objects.equals(this.idOperacao, other.idOperacao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Operacao[ idOperacao=" + idOperacao + " ]";
    }

    @XmlTransient
    @JsonIgnore
    public List<Funcionalidades> getFuncionalidadesList() {
        return funcionalidadesList;
    }

    public void setFuncionalidadesList(List<Funcionalidades> funcionalidadesList) {
        this.funcionalidadesList = funcionalidadesList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionalidadesGrupoOperacao> getFuncionalidadesGrupoOperacaoList() {
        return funcionalidadesGrupoOperacaoList;
    }

    public void setFuncionalidadesGrupoOperacaoList(List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList) {
        this.funcionalidadesGrupoOperacaoList = funcionalidadesGrupoOperacaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionalidadesPlanoOperacao> getFuncionalidadesPlanoOperacaoList() {
        return funcionalidadesPlanoOperacaoList;
    }

    public void setFuncionalidadesPlanoOperacaoList(List<FuncionalidadesPlanoOperacao> funcionalidadesPlanoOperacaoList) {
        this.funcionalidadesPlanoOperacaoList = funcionalidadesPlanoOperacaoList;
    }

}
