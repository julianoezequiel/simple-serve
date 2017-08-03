package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * @version 1.0.3 04/05/2016
 * @since 1.0.3 04/05/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Planos")
@XmlRootElement
public class Planos implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planos")
    private List<FuncionalidadesPlanoOperacao> funcionalidadesPlanoOperacaoList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idPlano")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlano;
    
    @Size(max = 60)
    @Column(name = "Descricao")
    private String descricao;
    
    @Column(name = "LimiteEmpresas")
    private Integer limiteEmpresas;
    
    @Column(name = "LimiteFuncionarios")
    private Integer limiteFuncionarios;
    
    @Column(name = "LimiteOperadores")
    private Integer limiteOperadores;
    
    @Column(name = "PrazoExpira")
    @Temporal(TemporalType.DATE)
    private Date prazoExpira;

    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne(optional = false)
    private Empresa idEmpresa;

    public Planos() {
    }

    public Planos(Integer idPlano) {
        this.idPlano = idPlano;
    }

    public Integer getIdPlano() {
        return idPlano;
    }

    public void setIdPlano(Integer idPlano) {
        this.idPlano = idPlano;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getLimiteEmpresas() {
        return limiteEmpresas;
    }

    public void setLimiteEmpresas(Integer limiteEmpresas) {
        this.limiteEmpresas = limiteEmpresas;
    }

    public Integer getLimiteFuncionarios() {
        return limiteFuncionarios;
    }

    public void setLimiteFuncionarios(Integer limiteFuncionarios) {
        this.limiteFuncionarios = limiteFuncionarios;
    }

    public Integer getLimiteOperadores() {
        return limiteOperadores;
    }

    public void setLimiteOperadores(Integer limiteOperadores) {
        this.limiteOperadores = limiteOperadores;
    }

    public Date getPrazoExpira() {
        return prazoExpira;
    }

    public void setPrazoExpira(Date prazoExpira) {
        this.prazoExpira = prazoExpira;
    }

    public Empresa getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Empresa idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPlano != null ? idPlano.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Planos)) {
            return false;
        }
        Planos other = (Planos) object;
        if ((this.idPlano == null && other.idPlano != null) || (this.idPlano != null && !this.idPlano.equals(other.idPlano))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Planos[ idPlano=" + idPlano + " ]";
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
