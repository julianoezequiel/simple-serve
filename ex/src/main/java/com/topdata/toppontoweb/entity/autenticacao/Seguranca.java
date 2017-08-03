package com.topdata.toppontoweb.entity.autenticacao;

import com.topdata.toppontoweb.entity.Entidade;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entidade contem os dados de configurações de segurança do sistema
 *
 * @version 1.0
 * @since 1.0
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Seguranca")
@XmlRootElement
public class Seguranca implements Entidade {

    //<editor-fold defaultstate="collapsed" desc="PROPRIEDADES">
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdSeguranca")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idSeguranca;

    @NotNull
    @Column(name = "TamanhoMinimoSenha")
    private Integer tamanhoMinimoSenha;

    @NotNull
    @Column(name = "QtdeBloqueioTentativas")
    private Integer qtdeBloqueioTentativas;

    @NotNull
    @Column(name = "QtdeDiasTrocaSenha")
    private Integer qtdeDiasTrocaSenha;

    @NotNull
    @Column(name = "QtdeNaoRepetirSenhas")
    private Integer qtdeNaoRepetirSenhas;

    @NotNull
    @Column(name = "ComplexidadeLetrasNumeros")
    private Boolean complexidadeLetrasNumeros;

//    @NotNull
//    @Column(name = "QtdeHorasDesbloqueioUsuario")
//    private Integer qtdeHorasDesbloqueioUsuario;
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="CONSTRUTORES">
    public Seguranca() {
    }

    /**
     *
     * @param idSeguranca
     */
    public Seguranca(Integer idSeguranca) {
        this.idSeguranca = idSeguranca;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="GETTERS E SETTERS">
    public Integer getIdSeguranca() {
        return idSeguranca;
    }

    public void setIdSeguranca(Integer idSeguranca) {
        this.idSeguranca = idSeguranca;
    }

    public Integer getTamanhoMinimoSenha() {
        return tamanhoMinimoSenha;
    }

    public void setTamanhoMinimoSenha(Integer tamanhoMinimoSenha) {
        this.tamanhoMinimoSenha = tamanhoMinimoSenha;
    }

    public Integer getQtdeBloqueioTentativas() {
        return qtdeBloqueioTentativas;
    }

    public void setQtdeBloqueioTentativas(Integer qtdeBloqueioTentativas) {
        this.qtdeBloqueioTentativas = qtdeBloqueioTentativas;
    }

    public Integer getQtdeDiasTrocaSenha() {
        return qtdeDiasTrocaSenha;
    }

    public void setQtdeDiasTrocaSenha(Integer qtdeDiasTrocaSenha) {
        this.qtdeDiasTrocaSenha = qtdeDiasTrocaSenha;
    }

    public Integer getQtdeNaoRepetirSenhas() {
        return qtdeNaoRepetirSenhas;
    }

    public void setQtdeNaoRepetirSenhas(Integer qtdeNaoRepetirSenhas) {
        this.qtdeNaoRepetirSenhas = qtdeNaoRepetirSenhas;
    }

    public Boolean getComplexidadeLetrasNumeros() {
        return complexidadeLetrasNumeros;
    }

    public void setComplexidadeLetrasNumeros(Boolean complexidadeLetrasNumeros) {
        this.complexidadeLetrasNumeros = complexidadeLetrasNumeros;
    }

//    public Integer getQtdeHorasDesbloqueioUsuario() {
//        return qtdeHorasDesbloqueioUsuario;
//    }
//
//    public void setQtdeHorasDesbloqueioUsuario(Integer qtdeHorasDesbloqueioUsuario) {
//        this.qtdeHorasDesbloqueioUsuario = qtdeHorasDesbloqueioUsuario;
//    }
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="FUN��ES">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idSeguranca != null ? idSeguranca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Seguranca)) {
            return false;
        }
        Seguranca other = (Seguranca) object;
        if ((this.idSeguranca == null && other.idSeguranca != null) || (this.idSeguranca != null && !this.idSeguranca.equals(other.idSeguranca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Segurança ";
    }
    //</editor-fold>

}
