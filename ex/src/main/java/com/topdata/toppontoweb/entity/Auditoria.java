package com.topdata.toppontoweb.entity;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import java.util.Date;
import javax.persistence.Transient;

/**
 * @version 1.0.3 data 05/05/2016
 * @since 1.0.3 data 05/05/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Auditoria")
@XmlRootElement
public class Auditoria implements Entidade {

    @Transient
    private String descricao;

    @Column(name = "DataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;

    @JoinColumn(name = "idFuncionalidade", referencedColumnName = "idFuncionalidade")
    @ManyToOne
    @JsonView(JsonViews.Audit.class)
    private Funcionalidades funcionalidade;

    @JoinColumn(name = "idTipoAuditoria", referencedColumnName = "idTipoAuditoria")
    @ManyToOne
    @JsonView(JsonViews.Audit.class)
    private TipoAuditoria tipoAuditoria;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdAuditoria")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idAuditoria;

    @Size(max = 8000)
    @Column(name = "Conteudo", length = 8000)
    @JsonView(JsonViews.Audit.class)
    private String conteudo;

    @JoinColumn(name = "IdOperacao", referencedColumnName = "IdOperacao")
    @ManyToOne
    @JsonView(JsonViews.Audit.class)
    private Operacao operacao;

    @JoinColumn(name = "IdOperador", referencedColumnName = "IdOperador")
    @ManyToOne
    @JsonView(JsonViews.Audit.class)
    private Operador operador;

    @Size(max = 15)
    @Column(name = "EnderecoIp")
    @JsonView(JsonViews.Audit.class)
    private String enderecoIp;

    public Auditoria() {

    }

    public Auditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public String getEnderecoIp() {
        return enderecoIp;
    }

    public void setEnderecoIp(String enderecoIp) {
        this.enderecoIp = enderecoIp;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAuditoria != null ? idAuditoria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Auditoria)) {
            return false;
        }
        Auditoria other = (Auditoria) object;
        if ((this.idAuditoria == null && other.idAuditoria != null) || (this.idAuditoria != null && !this.idAuditoria.equals(other.idAuditoria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Auditoria";
    }

    public Funcionalidades getFuncionalidade() {
        return funcionalidade;
    }

    public void setFuncionalidade(Funcionalidades funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public TipoAuditoria getTipoAuditoria() {
        return tipoAuditoria;
    }

    public void setTipoAuditoria(TipoAuditoria tipoAuditoria) {
        this.tipoAuditoria = tipoAuditoria;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

}
