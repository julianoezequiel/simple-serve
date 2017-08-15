package com.api.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "usuario")
@XmlRootElement
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    @Size(max = 255)
    @Column(name = "email")
    private String email;

    @Column(name = "senha")
    private String senha;

    @JoinColumn(name = "empresa_id", referencedColumnName = "id")
    @ManyToOne
    private Empresa empresaId;

    @ManyToMany(mappedBy = "usuarioList")
    private List<Permissao> permissaoList;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "tentativasLogin")
    private Integer tentativasLogin = 0;

    @Column(name = "senhaBloqueada")
    private Boolean senhaBloqueada;

    @Column(name = "trocaSenhaProximoAcesso")
    private Boolean trocaSenhaProximoAcesso;

    @Size(max = 45)
    @Column(name = "codigoConfirmacaoNovaSenha")
    private String codigoConfirmacaoNovaSenha;

    @Column(name = "ultimoAcesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoAcesso;

    @Column(name = "dataHoraBloqueioSenha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraBloqueioSenha;

    public Usuario() {
    }

    public Usuario(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String emai) {
        this.email = emai;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Empresa getEmpresaId() {
        return empresaId;
    }

    public void setEmpresaId(Empresa empresaId) {
        this.empresaId = empresaId;
    }

    public List<Permissao> getPermissaoList() {
        return permissaoList;
    }

    public void setPermissaoList(List<Permissao> permissaoList) {
        this.permissaoList = permissaoList;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Integer getTentativasLogin() {
        return tentativasLogin;
    }

    public void setTentativasLogin(Integer tentativasLogin) {
        this.tentativasLogin = tentativasLogin;
    }

    public Boolean getSenhaBloqueada() {
        return senhaBloqueada;
    }

    public void setSenhaBloqueada(Boolean senhaBloqueada) {
        this.senhaBloqueada = senhaBloqueada;
    }

    public Boolean getTrocaSenhaProximoAcesso() {
        return trocaSenhaProximoAcesso;
    }

    public void setTrocaSenhaProximoAcesso(Boolean trocaSenhaProximoAcesso) {
        this.trocaSenhaProximoAcesso = trocaSenhaProximoAcesso;
    }

    public String getCodigoConfirmacaoNovaSenha() {
        return codigoConfirmacaoNovaSenha;
    }

    public void setCodigoConfirmacaoNovaSenha(String codigoConfirmacaoNovaSenha) {
        this.codigoConfirmacaoNovaSenha = codigoConfirmacaoNovaSenha;
    }

    public Date getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Date ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public Date getDataHoraBloqueioSenha() {
        return dataHoraBloqueioSenha;
    }

    public void setDataHoraBloqueioSenha(Date dataHoraBloqueioSenha) {
        this.dataHoraBloqueioSenha = dataHoraBloqueioSenha;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
