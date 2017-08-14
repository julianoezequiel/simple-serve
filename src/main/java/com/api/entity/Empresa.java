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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "empresa")
@XmlRootElement
public class Empresa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Integer id;
	
	@OneToMany(mappedBy = "empresaId", fetch = FetchType.EAGER)
	private List<Usuario> usuarioList;
	
	@Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "documento")
    private String documento;
	
	
	@JoinColumn(name = "tipo_documento_Id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TipoDocumento tipoDocumento;
		
	@Basic(optional = false)
    @Size(max = 50)
    @NotNull
    @Column(name = "razaoSocial")
    private String razaoSocial;

    @Size(min = 1, max = 50)
    @Column(name = "nomeFantasia")
    private String nomeFantasia;

    @Size(min = 1, max = 50)
    @Column(name = "endereco")
    private String endereco;

    @Size(min = 1, max = 20)
    @Column(name = "bairro")
    private String bairro;

    @Size(min = 1, max = 10)
    @Column(name = "cep")
    private String cep;

    @Size(min = 1, max = 30)
    @Column(name = "cidade")
    private String cidade;

    @Size(min = 1, max = 2)
    @Column(name = "Uf")
    private String uf;

    @Size(min = 1, max = 20)
    @Column(name = "fone")
    private String fone;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Formato de telefone/fax inválido, deve ser xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation

    @Size(min = 1, max = 20)
    @Column(name = "fax")
    private String fax;

    @Size(min = 1, max = 50)
    @Column(name = "observacao")
    private String observacao;

    @Column(name = "ativo")
    private Boolean ativo;
}
