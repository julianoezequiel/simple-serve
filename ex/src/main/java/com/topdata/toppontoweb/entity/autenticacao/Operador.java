package com.topdata.toppontoweb.entity.autenticacao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonView;

import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.rest.autenticacao.JsonViews;

/**
 * Classe entidade do Operador. Os dados referente a Date devem ser trocados por
 * Calendar
 *
 * @version 1.0.5 data 11/07/2016
 * @since 1.0.1 data 20/03/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Operador")
@XmlRootElement
public class Operador implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operador")
    private List<Importacao> importacaoList;

    @OneToMany(mappedBy = "idOperador", fetch = FetchType.EAGER)
    private List<Funcionario> funcionarioList;

    @Column(name = "UltimoAcesso")
    @Temporal(TemporalType.TIMESTAMP)
    private Date ultimoAcesso;

    @Column(name = "DataHoraBloqueioSenha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraBloqueioSenha;

    @Transient
    private Funcionario funcionario;

    @Transient
    private String novaSenha;

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdOperador")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idOperador;

    @Size(max = 40)
    @Column(name = "Usuario")
    @NotNull
    @JsonView(JsonViews.Audit.class)
    private String usuario;

    @Size(min = 1, max = 100)
    @Column(name = "Senha")
    private String senha;

    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 60)
    @Column(name = "Email")
    private String email;

    @Column(name = "Ativo")
    private Boolean ativo;

    @Column(name = "TentativasLogin")
    private Integer tentativasLogin = 0;

    @Column(name = "SenhaBloqueada")
    private Boolean senhaBloqueada;

    @Column(name = "TrocaSenhaProximoAcesso")
    private Boolean trocaSenhaProximoAcesso;

    @Size(max = 255)
    @Column(name = "Foto")
    private String foto;

    @Column(name = "VisualizarMensagens")
    private Boolean visualizarMensagens;

    @Column(name = "VisualizarAlertas")
    private Boolean visualizarAlertas;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "operador")
    private List<Auditoria> auditoriaList = new ArrayList<>();

    @Size(max = 45)
    @Column(name = "CodigoConfirmacaoNovaSenha")
    private String codigoConfirmacaoNovaSenha;

    @JoinColumn(name = "IdGrupo", referencedColumnName = "IdGrupo")
    @ManyToOne(optional = false)
    @NotNull
    private Grupo grupo;

//    @OneToOne(cascade = CascadeType.ALL, mappedBy = "operador")
//    private OperadorMensagem operadorMensagem;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOperador")
    private List<HistoricoSenhas> historicoSenhasList = new ArrayList<>();

//    @OneToMany(mappedBy = "operador", fetch = FetchType.EAGER);
//    private List<Funcionario> funcionarioList = new ArrayList<>();
    @Size(max = 100)
    @Column(name = "UltimoToken")
    private String ultimoToken;

    @Size(max = 20)
    @Column(name = "UltimoIp")
    private String ultimoIp;

    //<editor-fold defaultstate="collapsed" desc="Contrutores">
    public Operador() {
    }

    public Operador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public Operador(Integer idOperador, String senha) {
        this.idOperador = idOperador;
        this.senha = senha;
    }

    public Operador(String usuario, String senha) {
        this.usuario = usuario;
        this.senha = senha;
    }

    public Operador(String usuario) {
        this.usuario = usuario;
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Getters e Setters">
    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto != null? foto.toLowerCase() : foto;
    }

    public Boolean getVisualizarMensagens() {
        return visualizarMensagens;
    }

    public void setVisualizarMensagens(Boolean visualizarMensagens) {
        this.visualizarMensagens = visualizarMensagens;
    }

    public Boolean getVisualizarAlertas() {
        return visualizarAlertas;
    }

    public void setVisualizarAlertas(Boolean visualizarAlertas) {
        this.visualizarAlertas = visualizarAlertas;
    }

    @XmlTransient
    @JsonIgnore
    public List<Auditoria> getAuditoriaList() {
        return auditoriaList;
    }

    public void setAuditoriaList(List<Auditoria> auditoriaList) {
        this.auditoriaList = auditoriaList;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

//    public OperadorMensagem getOperadorMensagem() {
//        return operadorMensagem;
//    }
//
//    public void setOperadorMensagem(OperadorMensagem operadorMensagem) {
//        this.operadorMensagem = operadorMensagem;
//    }
    @JsonIgnore
    @XmlTransient
    public List<HistoricoSenhas> getHistoricoSenhasList() {
        return historicoSenhasList;
    }

    public void setHistoricoSenhasList(List<HistoricoSenhas> historicoSenhasList) {
        this.historicoSenhasList = historicoSenhasList;
    }

    public String getCodigoConfirmacaoNovaSenha() {
        return codigoConfirmacaoNovaSenha;
    }

    public void setCodigoConfirmacaoNovaSenha(String codigoConfirmacaoNovaSenha) {
        this.codigoConfirmacaoNovaSenha = codigoConfirmacaoNovaSenha;
    }

    public String getUltimoToken() {
        return ultimoToken;
    }

    public void setUltimoToken(String ultimoToken) {
        this.ultimoToken = ultimoToken;
    }

    public String getUltimoIp() {
        return ultimoIp;
    }

    public void setUltimoIp(String ultimoIp) {
        this.ultimoIp = ultimoIp;
    }

    //<editor-fold defaultstate="collapsed" desc="Funções">
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.idOperador);
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
        final Operador other = (Operador) obj;
        if (!Objects.equals(this.idOperador, other.idOperador)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = Objects.isNull(this.getUsuario()) == true ? "" : this.getUsuario();
        return "Operador " + s;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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

    @XmlTransient
    @JsonIgnore
    public List<Funcionario> getFuncionarioList() {
        return funcionarioList;
    }

    public void setFuncionarioList(List<Funcionario> funcionarioList) {
        this.funcionarioList = funcionarioList;
    }

    @XmlTransient
    @JsonIgnore
    public List<Importacao> getImportacaoList() {
        return importacaoList;
    }

    public void setImportacaoList(List<Importacao> importacaoList) {
        this.importacaoList = importacaoList;
    }

}
