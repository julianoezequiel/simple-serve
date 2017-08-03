package com.topdata.toppontoweb.entity.funcionario;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Cei;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
import com.topdata.toppontoweb.utils.constantes.Enum_MotivoNaoEditavel;
import javax.persistence.Transient;

/**
 * Classe entidade para a entidade Funcionário. Para as propriedades do tipo
 *
 * @version 1.0.3 04/05/2016
 * @since 1.0.3 04/05/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Funcionario")
@XmlRootElement
public class Funcionario implements Entidade, Cloneable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncionario")
    private List<EmpresaFechamentoData> empresaFechamentoDataList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncionario")
    private List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @JoinColumn(name = "idCei", referencedColumnName = "idCei")
    @ManyToOne
    private Cei cei;

    @JoinColumn(name = "IdCargo", referencedColumnName = "IdCargo")
    @ManyToOne
    private Cargo cargo;

    @JoinColumn(name = "IdDepartamento", referencedColumnName = "IdDepartamento")
    @ManyToOne(optional = false)
    private Departamento departamento;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<FuncionarioBancoHoras> funcionarioBancoHorasList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<Abono> abonoList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<Compensacao> compensacaoList;

    @OneToMany(mappedBy = "idFuncionario", cascade = CascadeType.ALL)
    private List<Marcacoes> marcacoesList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<HistoricoFuncionarioCargo> funcionarioCargoList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdFuncionario")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionario;

    @Basic(optional = false)
    @NotNull
    @Column(name = "Ativo")
    private boolean ativo;

    @JoinColumn(name = "IdOperador", referencedColumnName = "IdOperador")
    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private Operador idOperador;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<FuncionarioEmpresa> funcionarioEmpresaList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<FuncionarioDepartamento> funcionarioDepartamentoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<FuncionarioCalendario> funcionarioCalendarioList;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, mappedBy = "funcionario")
    private List<FuncionarioJornada> funcionarioJornadaList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFuncionario")
    private List<Cartao> cartaoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "funcionario")
    private List<Afastamento> afastamentoList = new ArrayList<>();

    @NotNull
    @Size(max = 40)
    @Column(name = "Nome")
    private String nome;

    @Size(min = 1, max = 20)
    @Column(name = "Ctps")
    private String ctps;

    @Size(min = 1, max = 15)
    @Column(name = "Matricula")
    private String matricula;

    @Basic(optional = false)
    @Size(min = 1, max = 14)
    @Column(name = "Pis")
    @NotNull
    private String pis;

    @Size(max = 15)
    @Column(name = "IdentificacaoExportacao")
    private String identificacaoExportacao;

    @Size(max = 255)
    @Column(name = "Foto")
    private String foto;

    @Column(name = "DataAdmissao")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataAdmissao;

    @Column(name = "DataDemissao")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataDemissao;

    @Transient
    private Boolean permitido;

    @Transient
    private Boolean editavel = true;

    @Transient
    private Enum_MotivoNaoEditavel motivoNaoEditavel;

    //Utilizsado para enviar alguma mensagem para o front-end
    //Está sendo utilisado no coletivo, como mensagem de "não editavel"
    @Transient
    private String mensagem = "";

    public Funcionario() {
    }

    public Funcionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Funcionario(Integer idFuncionario, String nome, String pis, String foto) {
        this.idFuncionario = idFuncionario;
        this.pis = pis;
        this.nome = nome;
        this.foto = foto;
    }

    public Funcionario(Operador operador) {
        this.idOperador = operador;
    }

    public Boolean getEditavel() {
        return editavel;
    }

    public void setEditavel(Boolean editavel) {
        this.editavel = editavel;
    }

    public Enum_MotivoNaoEditavel getMotivoNaoEditavel() {
        return motivoNaoEditavel;
    }

    public void setMotivoNaoEditavel(Enum_MotivoNaoEditavel motivoNaoEditavel) {
        this.motivoNaoEditavel = motivoNaoEditavel;
    }

    public Boolean getPermitido() {
        return permitido;
    }

    public void setPermitido(Boolean permitido) {
        this.permitido = permitido;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getIdentificacaoExportacao() {
        return identificacaoExportacao;
    }

    public void setIdentificacaoExportacao(String identificacaoExportacao) {
        this.identificacaoExportacao = identificacaoExportacao;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto != null ? foto.toLowerCase() : foto;
    }

    @XmlTransient
    @JsonIgnore
    public List<Afastamento> getAfastamentoList() {
        return afastamentoList;
    }

    public void setAfastamentoList(List<Afastamento> afastamentoList) {
        this.afastamentoList = afastamentoList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionario != null ? idFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idFuncionario == null && other.idFuncionario != null) || (this.idFuncionario != null && !this.idFuncionario.equals(other.idFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getNome() != null ? this.getNome() : "";
        return "Funcionário(a) " + s;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Date getDataDemissao() {
        return dataDemissao;
    }

    public void setDataDemissao(Date dataDemissao) {
        this.dataDemissao = dataDemissao;
    }

    public Operador getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Operador idOperador) {
        this.idOperador = idOperador;
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
    public List<FuncionarioDepartamento> getFuncionarioDepartamentoList() {
        return funcionarioDepartamentoList;
    }

    public void setFuncionarioDepartamentoList(List<FuncionarioDepartamento> funcionarioDepartamentoList) {
        this.funcionarioDepartamentoList = funcionarioDepartamentoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Cartao> getCartaoList() {
        return cartaoList;
    }

    public void setCartaoList(List<Cartao> cartaoList) {
        this.cartaoList = cartaoList;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioJornada> getFuncionarioJornadaList() {
        return funcionarioJornadaList;
    }

    public void setFuncionarioJornadaList(List<FuncionarioJornada> funcionarioJornadaList) {
        this.funcionarioJornadaList = funcionarioJornadaList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioCalendario> getFuncionarioCalendarioList() {
        return funcionarioCalendarioList;
    }

    public void setFuncionarioCalendarioList(List<FuncionarioCalendario> funcionarioCalendarioList) {
        this.funcionarioCalendarioList = funcionarioCalendarioList;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioBancoHoras> getFuncionarioBancoHorasList() {
        return funcionarioBancoHorasList;
    }

    public void setFuncionarioBancoHorasList(List<FuncionarioBancoHoras> funcionarioBancoHorasList) {
        this.funcionarioBancoHorasList = funcionarioBancoHorasList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Abono> getAbonoList() {
        return abonoList;
    }

    public void setAbonoList(List<Abono> abonoList) {
        this.abonoList = abonoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Compensacao> getCompensacaoList() {
        return compensacaoList;
    }

    public void setCompensacaoList(List<Compensacao> compensacaoList) {
        this.compensacaoList = compensacaoList;
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
    public List<HistoricoFuncionarioCargo> getFuncionarioCargoList() {
        return funcionarioCargoList;
    }

    public void setFuncionarioCargoList(List<HistoricoFuncionarioCargo> funcionarioCargoList) {
        this.funcionarioCargoList = funcionarioCargoList;
    }

    public Cei getCei() {
        return cei;
    }

    public void setCei(Cei cei) {
        this.cei = cei;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    @XmlTransient
    @JsonIgnore
    public List<EmpresaFechamentoData> getEmpresaFechamentoDataList() {
        return empresaFechamentoDataList;
    }

    public void setEmpresaFechamentoDataList(List<EmpresaFechamentoData> empresaFechamentoDataList) {
        this.empresaFechamentoDataList = empresaFechamentoDataList;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioDiaNaoDescontaDSR> getFuncionarioDiaNaoDescontaDSRList() {
        return funcionarioDiaNaoDescontaDSRList;
    }

    public void setFuncionarioDiaNaoDescontaDSRList(List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList) {
        this.funcionarioDiaNaoDescontaDSRList = funcionarioDiaNaoDescontaDSRList;
    }

    @Transient
    private Double pesoPercentual = 100d;
    @Transient
    private Double percentual = 0d;

    public Double getPesoPercentual() {
        return pesoPercentual;
    }

    public void setPesoPercentual(Double pesoPercentual) {
        this.pesoPercentual = pesoPercentual;
    }

    public Double getPercentual() {
        return percentual;
    }

    public void setPercentual(Double percentual) {
        this.percentual = percentual;
    }

}
