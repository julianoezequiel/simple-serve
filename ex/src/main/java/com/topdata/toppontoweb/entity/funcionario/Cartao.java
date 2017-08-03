package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;

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
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.utils.CustomDateSerializer;

/**
 * @version 1.0.5 data 05/09/2016
 * @since 1.0.5 data 05/09/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Cartao")
@XmlRootElement
public class Cartao implements Entidade, Cloneable {

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idCartao")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idCartao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataInicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @Column(name = "DataValidade")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataValidade;

    @Column(name = "Numero")
    @NotNull
    private String numero;

    @Column(name = "Provisorio")
    private Boolean provisorio;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario idFuncionario;

    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne
    private Motivo motivo;

    public Cartao() {
    }

    public Cartao(Integer idCartao) {
        this.idCartao = idCartao;
    }

    public Cartao(Integer idCartao, Date dataInicio) {
        this.idCartao = idCartao;
        this.dataInicio = dataInicio;
    }

    public Integer getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(Integer idCartao) {
        this.idCartao = idCartao;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Boolean getProvisorio() {
        return provisorio;
    }

    public void setProvisorio(Boolean provisorio) {
        this.provisorio = provisorio;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCartao != null ? idCartao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Cartao)) {
            return false;
        }
        Cartao other = (Cartao) object;
        if ((this.idCartao == null && other.idCartao != null) || (this.idCartao != null && !this.idCartao.equals(other.idCartao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = (this.numero != null ? "número " + this.numero : "");
        return "Cartao " + s;
    }

}
