package com.topdata.toppontoweb.entity.marcacoes;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.utils.CustomHourSerializer;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Marcacoes")
@XmlRootElement
public class Marcacoes implements Entidade {

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idMarcacao")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idMarcacao;
    

    @Size(min = 1, max = 11)
    @Column(name = "Nsr")
    private String nsr;
    
    @Size(min = 1, max = 14)
    @Column(name = "Pis")
    private String pis;
    
    //Ignorar essa coluna
    @Column(name = "NumeroSerie")
    private Integer numeroSerie;
    
    @Basic(optional = false)
    @NotNull
    @Column(name = "DataHora")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHora;
    
    @Column(name = "Invalido")
    private Boolean invalido;
    
    @Size(max = 16)
    @Column(name = "Cartao")
    private String cartao;
    
    @Size(max = 16)
    @Column(name = "CartaoOriginal")
    private String cartaoOriginal;
    
    @JoinColumn(name = "IdEmpresa", referencedColumnName = "IdEmpresa")
    @ManyToOne
    private Empresa empresa;
    
    @JoinColumn(name = "idEncaixeMarcacao", referencedColumnName = "idEncaixeMarcacao")
    @ManyToOne
    private EncaixeMarcacao idEncaixeMarcacao;
    
    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne
    private Funcionario idFuncionario;
    
    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne
    private Motivo motivo;
    
    @JoinColumn(name = "IdRep", referencedColumnName = "IdRep")
    @ManyToOne
    private Rep rep;
    
    @JoinColumn(name = "idStatusMarcacao", referencedColumnName = "idStatusMarcacao")
    @ManyToOne
    private StatusMarcacao idStatusMarcacao;
    
    @JoinColumn(name = "IdImportacao", referencedColumnName = "IdImportacao")
    @ManyToOne
    private Importacao importacao;

    public Marcacoes() {
    }
    
    //Equipamento "outros" > Marcacao Invalida
    public Marcacoes(Empresa empresa, Rep rep, Date dataHora,  StatusMarcacao statusMarcacao, EncaixeMarcacao encaixeMarcacao, Importacao importacao){
        this.empresa = empresa;
        this.rep = rep;
        this.dataHora = dataHora;
        this.idStatusMarcacao = statusMarcacao;
        this.invalido = true;
        this.idEncaixeMarcacao = encaixeMarcacao;
        this.importacao = importacao;
    }
    
    
    /**
     * Construtor utilizado na importacao de marcacoes do rep antigo
     * @param funcionario
     * @param empresa
     * @param cartaoOriginal
     * @param rep
     * @param encaixeMarcacao
     * @param statusMarcacao
     * @param dataHora
     * @param importacao
     * @param cartao
     * @param motivo
     * @param invalido
     */
    public Marcacoes(Funcionario funcionario, Empresa empresa, Rep rep, Date dataHora, StatusMarcacao statusMarcacao, String cartaoOriginal, String cartao, Boolean invalido, EncaixeMarcacao  encaixeMarcacao, Motivo motivo, Importacao importacao){
        this.idFuncionario = funcionario;
        this.empresa = empresa;
        this.rep = rep;
        this.dataHora = dataHora;
        this.idStatusMarcacao = statusMarcacao;
        this.cartaoOriginal = cartaoOriginal;
        this.cartao = cartao;
        this.invalido = invalido;
        this.idEncaixeMarcacao = encaixeMarcacao;
        this.importacao = importacao;
        
        this.pis = null;
        this.nsr = null;
        this.numeroSerie = null;
        this.motivo = motivo;
    }

    public Marcacoes(Integer idMarcacao) {
        this.idMarcacao = idMarcacao;
        this.invalido = false;
    }

    public Marcacoes(Date dataHora, Boolean invalido, EncaixeMarcacao idEncaixeMarcacao, StatusMarcacao idStatusMarcacao) {
        this.dataHora = dataHora;
        this.invalido = invalido;
        this.idEncaixeMarcacao = idEncaixeMarcacao;
        this.idStatusMarcacao = idStatusMarcacao;
    }

    public Marcacoes(Funcionario funcionario, String pis, Date dataHora, Empresa empresa, Rep rep, StatusMarcacao statusMarcacao, String nsr, EncaixeMarcacao encaixeMarcacao, Motivo motivo, boolean invalido) {
        this.idFuncionario = funcionario;
        this.pis = pis;
        this.dataHora = dataHora;
        this.empresa = empresa;
        this.rep = rep;
        this.idStatusMarcacao = statusMarcacao;
        this.nsr = nsr;
        this.idEncaixeMarcacao = encaixeMarcacao;
        this.invalido = invalido;
        this.motivo = motivo;
    }

    public Integer getIdMarcacao() {
        return idMarcacao;
    }

    public void setIdMarcacao(Integer idMarcacao) {
        this.idMarcacao = idMarcacao;
    }

    public String getNsr() {
        return nsr;
    }

    public void setNsr(String nsr) {
        this.nsr = nsr;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Integer getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(Integer numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public Date getDataHora() {
        return dataHora;
    }
    
    @JsonSerialize(using = CustomHourSerializer.class)
    public Date getDataHoraFormatado() {
        return dataHora;
    }

    public void setDataHora(Date dataHora) {
        this.dataHora = dataHora;
    }

    public Boolean getInvalido() {
        return invalido;
    }

    public void setInvalido(Boolean invalido) {
        this.invalido = invalido;
    }

    public String getCartao() {
        return cartao;
    }

    public void setCartao(String cartao) {
        this.cartao = cartao;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public EncaixeMarcacao getIdEncaixeMarcacao() {
        return idEncaixeMarcacao;
    }

    public void setIdEncaixeMarcacao(EncaixeMarcacao idEncaixeMarcacao) {
        this.idEncaixeMarcacao = idEncaixeMarcacao;
    }

    public Funcionario getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Funcionario idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public Rep getRep() {
        return rep;
    }

    public void setRep(Rep rep) {
        this.rep = rep;
    }

    public StatusMarcacao getIdStatusMarcacao() {
        return idStatusMarcacao;
    }

    public void setIdStatusMarcacao(StatusMarcacao idStatusMarcacao) {
        this.idStatusMarcacao = idStatusMarcacao;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMarcacao != null ? idMarcacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Marcacoes)) {
            return false;
        }
        Marcacoes other = (Marcacoes) object;
        if ((this.idMarcacao == null && other.idMarcacao != null) || (this.idMarcacao != null && !this.idMarcacao.equals(other.idMarcacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.topdata.toppontoweb.entity.Marcacoes[ idMarcacao=" + idMarcacao + " ]";
    }

    @Transient
    private Date dataEncaixeDia;

    public Date getDataEncaixeDia() {
        return dataEncaixeDia;
    }

    public void setDataEncaixeDia(Date dataEncaixeDia) {
        this.dataEncaixeDia = dataEncaixeDia;
    }

    /**
     * @return the importacao
     */
    public Importacao getImportacao() {
        return importacao;
    }

    /**
     * @param importacao the importacao to set
     */
    public void setImportacao(Importacao importacao) {
        this.importacao = importacao;
    }

    /**
     * @return the cartaoOriginal
     */
    public String getCartaoOriginal() {
        return cartaoOriginal;
    }

    /**
     * @param cartaoOriginal the cartaoOriginal to set
     */
    public void setCartaoOriginal(String cartaoOriginal) {
        this.cartaoOriginal = cartaoOriginal;
    }

}
