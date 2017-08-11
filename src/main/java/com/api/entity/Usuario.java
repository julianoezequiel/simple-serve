package com.api.entity;

import java.io.Serializable;
import java.util.Date;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof Usuario)) {
			return false;
		}
		Usuario other = (Usuario) object;
		if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override	
	public String toString() {
		return "com.api.entity.Usuario[ id=" + id + " ]";
	}

//	public UsuarioBioCmd toUsuarioBioCmd() {
//		UsuarioBioCmd cmd = new UsuarioBioCmd();
//		cmd.setfPis(pis);
//		return cmd;
//	}

}
