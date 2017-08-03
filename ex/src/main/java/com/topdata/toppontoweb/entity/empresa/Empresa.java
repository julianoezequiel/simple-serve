package com.topdata.toppontoweb.entity.empresa;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.autenticacao.Planos;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioEmpresa;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.entity.tipo.TipoDocumento;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;
import com.topdata.toppontoweb.utils.constantes.Enum_MotivoNaoEditavel;
import javax.persistence.FetchType;

/**
 * Classe entidade empresa
 *
 * @version 1.0.0.0 data 04/05/2016
 * @version 1.0.0.1 data 20/05/2016
 * @since 1.0.0.0 data 04/05/2016
 *
 * @see EmpresaDepartamento
 * @see GrupoEmpresas
 * @see TipoDocumento
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Empresa")
@XmlRootElement
public class Empresa implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idEmpresa")
    private List<EmpresaFechamentoPeriodo> empresaFechamentoPeriodoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Cei> ceiList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<Rep> repList;

    @OneToMany(mappedBy = "empresa")
    private List<Marcacoes> marcacoesList;

    @OneToMany(mappedBy = "empresa")
    private List<Dsr> dsrList;

    /**
     * @since 1.0.4 data 04/05/2016
     */
    @OneToMany(mappedBy = "empresa", cascade = CascadeType.ALL)
    private List<Departamento> departamentoList = new ArrayList<>();

    /**
     * @since 1.0.3
     */
    @OneToMany(mappedBy = "idEmpresa")
    private List<Planos> planosList = new ArrayList<>();

    /**
     * @since 1.0.3
     */
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 18)
    @Column(name = "Documento")
    @JsonView(JsonViews.Audit.class)
    private String documento;

    @Transient
    @Deprecated
    private List<Grupo> grupoList;

    /**
     * @since 1.0.3
     */
    @JoinColumn(name = "idTipoDocumento", referencedColumnName = "idTipoDocumento")
    @ManyToOne(optional = false)
    @JsonView(JsonViews.Audit.class)
    private TipoDocumento tipoDocumento;

    /**
     * @since 1.0.3
     */
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "empresa")
    private List<FuncionarioEmpresa> funcionarioEmpresaList = new ArrayList<>();

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdEmpresa")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    @JsonView(JsonViews.Audit.class)
    private Integer idEmpresa;

    @Basic(optional = false)
    @Size(max = 50)
    @NotNull
    @Column(name = "RazaoSocial")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+", message = "error")
    private String razaoSocial;

    @Size(min = 1, max = 50)
    @Column(name = "NomeFantasia")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String nomeFantasia;

    @Size(min = 1, max = 50)
    @Column(name = "Endereco")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String endereco;

    @Size(min = 1, max = 20)
    @Column(name = "Bairro")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String bairro;

    @Size(min = 1, max = 10)
    @Column(name = "Cep")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String cep;

    @Size(min = 1, max = 30)
    @Column(name = "Cidade")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String cidade;

    @Size(min = 1, max = 2)
    @Column(name = "Uf")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String uf;

    @Size(min = 1, max = 20)
    @Column(name = "Fone")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String fone;
    // @Pattern(regexp="^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{4})$", message="Formato de telefone/fax inválido, deve ser xxx-xxx-xxxx")//if the field contains phone or fax number consider using this annotation to enforce field validation

    @Size(min = 1, max = 20)
    @Column(name = "Fax")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String fax;

    @Size(min = 1, max = 50)
    @Column(name = "Observacao")
    @JsonView(JsonViews.Audit.class)
    @Pattern(regexp = "[\\d \\w \\u00C0-\\u00FF \\.\\/\\\"\\!\\@\\#\\$\\%\\¨\\&\\*\\(\\)\\-\\+\\=\\}\\,\\<\\>\\;\\?\\|]+")
    private String observacao;

    @Column(name = "Ativo")
    @JsonView(JsonViews.Audit.class)
    private Boolean ativo;

    @Transient
    private Boolean possuiHistorico;

    @Transient
    private Boolean possuiVinculo;

    @Transient
    private Boolean permitido = true;

    @Transient
    private Boolean editavel = true;

    @Transient
    private Enum_MotivoNaoEditavel motivoNaoEditavel;

    //Utilizsado para enviar alguma mensagem para o front-end
    //Está sendo utilisado no coletivo, como mensagem de "não editavel"
    @Transient
    private String mensagem = "";

    public Empresa() {
    }

    public Empresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public Empresa(Integer idEmpresa, String nomeFantasia, String endereco, String bairro, String cep, String cidade, String uf, String fone, String fax, String observacao) {
        this.idEmpresa = idEmpresa;
        this.nomeFantasia = nomeFantasia;
        this.endereco = endereco;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
        this.uf = uf;
        this.fone = fone;
        this.fax = fax;
        this.observacao = observacao;
    }

    public Empresa(int BYTES, Boolean TRUE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Enum_MotivoNaoEditavel getMotivoNaoEditavel() {
        return motivoNaoEditavel;
    }

    public void setMotivoNaoEditavel(Enum_MotivoNaoEditavel motivoNaoEditavel) {
        this.motivoNaoEditavel = motivoNaoEditavel;
    }

    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    @JsonIgnore
    public void carregarHistorico() {
        this.possuiHistorico = ((this.getFuncionarioEmpresaList() != null && !this.getFuncionarioEmpresaList().isEmpty()) || (this.getMarcacoesList() != null && !this.getMarcacoesList().isEmpty()));
    }

    public Boolean getPossuiHistorico() {
        return possuiHistorico;
    }

    public Boolean getPossuiVinculo() {
        return possuiVinculo;
    }

    public void setPossuiVinculo(Boolean possuiVinculo) {
        this.possuiVinculo = possuiVinculo;
    }

    public void setPossuiHistorico(Boolean possuiHistorico) {
        this.possuiHistorico = possuiHistorico;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    /**
     *
     * @return
     */
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.idEmpresa);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Empresa other = (Empresa) obj;
        if (!Objects.equals(this.idEmpresa, other.idEmpresa)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getRazaoSocial() != null ? this.getRazaoSocial() : "";
        return "Empresa " + s;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    @XmlTransient
    @JsonIgnore
    public List<Grupo> getGrupoList() {
        return grupoList;
    }

    public void setGrupoList(List<Grupo> grupoList) {
        this.grupoList = grupoList;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioEmpresa> getFuncionarioEmpresaList() {
        return funcionarioEmpresaList;
    }

    public void setFuncionarioEmpresaList(List<FuncionarioEmpresa> funcionarioEmpresaList) {
        this.funcionarioEmpresaList = funcionarioEmpresaList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Planos> getPlanosList() {
        return planosList;
    }

    public void setPlanosList(List<Planos> planosList) {
        this.planosList = planosList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

    public void setDepartamentoList(List<Departamento> departamentoList) {
        this.departamentoList = departamentoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Dsr> getDsrList() {
        return dsrList;
    }

    public void setDsrList(List<Dsr> dsrList) {
        this.dsrList = dsrList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Marcacoes> getMarcacoesList() {
        return marcacoesList;
    }

    public void setMarcacoesList(List<Marcacoes> marcacoesList) {
        this.marcacoesList = marcacoesList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Cei> getCeiList() {
        return ceiList;
    }

    public void setCeiList(List<Cei> ceiList) {
        this.ceiList = ceiList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Rep> getRepList() {
        return repList;
    }

    public void setRepList(List<Rep> repList) {
        this.repList = repList;
    }

//    @XmlTransient
//    @JsonIgnore
//    public List<EmpresaFechamentoHoras> getEmpresaFechamentoHorasList() {
//        return empresaFechamentoHorasList;
//    }
//
//    public void setEmpresaFechamentoHorasList(List<EmpresaFechamentoHoras> empresaFechamentoHorasList) {
//        this.empresaFechamentoHorasList = empresaFechamentoHorasList;
//    }
    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @XmlTransient
    @JsonIgnore
    public List<EmpresaFechamentoPeriodo> getEmpresaFechamentoPeriodoList() {
        return empresaFechamentoPeriodoList;
    }

    public void setEmpresaFechamentoPeriodoList(List<EmpresaFechamentoPeriodo> empresaFechamentoPeriodoList) {
        this.empresaFechamentoPeriodoList = empresaFechamentoPeriodoList;
    }

}
