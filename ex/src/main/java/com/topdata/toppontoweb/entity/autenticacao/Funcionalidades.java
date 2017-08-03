package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Auditoria;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 * @version 1.0.4 data 06/06/2016
 * @since 1.0.4 data 06/06/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Funcionalidades")
@XmlRootElement
public class Funcionalidades implements Entidade {

    public Funcionalidades() {
    }

    @OneToMany(mappedBy = "funcionalidade")
    private List<Auditoria> auditoriaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionalidades")
    private List<FuncionalidadesPlanoOperacao> funcionalidadesPlanoOperacaoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionalidades")
    private List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList;

    @JoinTable(name = "FuncionalidadesOperacao", joinColumns = {
        @JoinColumn(name = "idFuncionalidade", referencedColumnName = "idFuncionalidade")}, inverseJoinColumns = {
        @JoinColumn(name = "IdOperacao", referencedColumnName = "IdOperacao")})
    @ManyToMany(fetch = FetchType.EAGER)//(mappedBy = "funcionalidadesList")
    private List<Operacao> operacaoList = new ArrayList<>();

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionalidade")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "idPai")
    private Integer parent;

    @Size(max = 45)
    @Column(name = "Descricao")
    private String text;

    @JoinColumn(name = "idModulo", referencedColumnName = "idModulo")
    @ManyToOne(optional = false)
    @JsonIgnore
    private Modulos idModulo;

    @Transient
    private CONSTANTES.Enum_FUNCIONALIDADE funcionalidade;

    @Transient
    private String icon;

    public Funcionalidades(CONSTANTES.Enum_FUNCIONALIDADE funcionalidade, Modulos modulos) {
        this.text = funcionalidade.getDescricao();
        this.id = funcionalidade.ordinal();
        this.idModulo = modulos;
        this.parent = modulos.getId();
        this.funcionalidade = funcionalidade;
    }

    public Funcionalidades(Integer idFuncionalidade) {
        this.id = idFuncionalidade;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParent() {
        return parent;
    }

    public void setParent(Integer parent) {
        this.parent = parent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Modulos getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Modulos idModulo) {
        this.idModulo = idModulo;
    }

    public CONSTANTES.Enum_FUNCIONALIDADE getFuncionalidade() {
        for (CONSTANTES.Enum_FUNCIONALIDADE funcionalidade1 : CONSTANTES.Enum_FUNCIONALIDADE.values()) {
            if (funcionalidade1.ordinal() == this.id) {
                this.funcionalidade = funcionalidade1;
            }
        }
        return this.funcionalidade;
    }

    public void setFuncionalidade(CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public String getIcon() {
        for (CONSTANTES.Enum_FUNCIONALIDADE funcionalidade1 : CONSTANTES.Enum_FUNCIONALIDADE.values()) {
            if (funcionalidade1.ordinal() == this.id) {
                this.icon = funcionalidade1.getIcone();
            }
        }
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionalidades)) {
            return false;
        }
        Funcionalidades other = (Funcionalidades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Funcionalidades[ idFuncionalidade=" + id + " ]";
    }

//    @XmlTransient
//    @JsonIgnore
    public List<Operacao> getOperacaoList() {
        return operacaoList;
    }

    public void setOperacaoList(List<Operacao> operacaoList) {
        this.operacaoList = operacaoList;
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

    @XmlTransient
    @JsonIgnore
    public List<Auditoria> getAuditoriaList() {
        return auditoriaList;
    }

    public void setAuditoriaList(List<Auditoria> auditoriaList) {
        this.auditoriaList = auditoriaList;
    }

}
