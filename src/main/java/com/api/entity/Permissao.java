package com.api.entity;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "permissao")
@XmlRootElement
public class Permissao {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	
	@JoinColumn(name = "modulo_id", referencedColumnName = "id")
	@ManyToOne
	private Modulo moduloId;
	
    @JoinTable(name = "permissaoUsuario", joinColumns = {
            @JoinColumn(name = "id_permissao", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "id_usuario", referencedColumnName = "id")})
    @ManyToMany(fetch = FetchType.EAGER)
	private List<Usuario> usuarioList;
    
    @Size(max = 45)
    @Column(name = "descricao")
    private String descricao;
    
}
