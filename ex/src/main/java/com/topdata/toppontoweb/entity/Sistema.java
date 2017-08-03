/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.topdata.toppontoweb.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author tharle.camargo
 */
@Entity
@Table(name = "Sistema")
@XmlRootElement
public class Sistema implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idSistema")
    private Integer idSistema;
    @Size(max = 20)
    @Column(name = "Versao")
    private String versao;

    public Sistema() {
    }

    public Sistema(Integer idSistema) {
        this.idSistema = idSistema;
    }

    public Integer getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Integer idSistema) {
        this.idSistema = idSistema;
    }

    public String getVersao() {
        return versao;
    }

    public void setVersao(String versao) {
        this.versao = versao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSistema != null ? idSistema.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.Sistema[ idSistema=" + idSistema + " ]";
    }
    
}
