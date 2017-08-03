/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.entity.marcacoes;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
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

/**
 *
 * @author tharle.camargo
 */
@Entity
@Table(name = "Importacao")
@XmlRootElement
public class Importacao implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdImportacao")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idImportacao;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "QuantidadeValidas")
    private int quantidadeValidas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "QuantidadeInvalidas")
    private int quantidadeInvalidas;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "QuantidadeIgnoradas")
    private int quantidadeIgnoradas;
    
    @JoinColumn(name = "IdOperador", referencedColumnName = "IdOperador")
    @ManyToOne(optional = false)
    private Operador operador;

    public Importacao() {
    }

    public Importacao(Operador operador) {
        this.dataHora = new Date();
        this.operador = operador;
    }

    public Importacao(Integer idImportacao) {
        this.idImportacao = idImportacao;
    }

    public Importacao(Integer idImportacao, Date dataHora, int quantidadeValidas, int quantidadeInvalidas, int quantidadeIgnoradas) {
        this.idImportacao = idImportacao;
        this.dataHora = dataHora;
        this.quantidadeValidas = quantidadeValidas;
        this.quantidadeInvalidas = quantidadeInvalidas;
        this.quantidadeIgnoradas = quantidadeIgnoradas;
    }

    public Integer getIdImportacao() {
        return idImportacao;
    }

    public void setIdImportacao(Integer idImportacao) {
        this.idImportacao = idImportacao;
    }

    public Date getDataHora() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public int getQuantidadeValidas() {
        return quantidadeValidas;
    }

    public void setQuantidadeValidas(int quantidadeValidas) {
        this.quantidadeValidas = quantidadeValidas;
    }
    
     public void addQuantidadeValidas(int quantidadeValidas) {
        this.quantidadeValidas += quantidadeValidas;
    }

    public int getQuantidadeInvalidas() {
        return quantidadeInvalidas;
    }

    public void setQuantidadeInvalidas(int quantidadeInvalidas) {
        this.quantidadeInvalidas = quantidadeInvalidas;
    }
    
    public void addQuantidadeInvalidas(int quantidadeInvalidas) {
        this.quantidadeInvalidas += quantidadeInvalidas;
    }

    public int getQuantidadeIgnoradas() {
        return quantidadeIgnoradas;
    }

    public void setQuantidadeIgnoradas(int quantidadeIgnoradas) {
        this.quantidadeIgnoradas = quantidadeIgnoradas;
    }
    public void addQuantidadeIgnoradas(int quantidadeIgnoradas) {
        this.quantidadeIgnoradas += quantidadeIgnoradas;
    }
    

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idImportacao != null ? idImportacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Importacao)) {
            return false;
        }
        Importacao other = (Importacao) object;
        if ((this.idImportacao == null && other.idImportacao != null) || (this.idImportacao != null && !this.idImportacao.equals(other.idImportacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.marcacoes.Importacao[ idImportacao=" + idImportacao + " ]";
    }
    
}
