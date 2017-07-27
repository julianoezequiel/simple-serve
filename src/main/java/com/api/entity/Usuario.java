/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.api.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
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
	@Column(name = "emai")
	private String emai;
	
	@Lob
	@Column(name = "senha")
	private String senha;

//	@OneToMany(mappedBy = "usuarioBioId")
//	@JsonIgnore
//	private Collection<Tarefa> tarefaCollection;

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

	

//	public Collection<Tarefa> getTarefaCollection() {
//		return tarefaCollection;
//	}
//
//	public void setTarefaCollection(Collection<Tarefa> tarefaCollection) {
//		this.tarefaCollection = tarefaCollection;
//	}

	public String getEmai() {
		return emai;
	}

	public void setEmai(String emai) {
		this.emai = emai;
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
