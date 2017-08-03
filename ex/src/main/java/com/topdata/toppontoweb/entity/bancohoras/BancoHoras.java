package com.topdata.toppontoweb.entity.bancohoras;

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

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;

/**
 * @version 1.0.4 data 18/08/2016
 * @version 1.0.1 data 10/04/2016
 * @since 1.0.1 data 10/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "BancoHoras")
@XmlRootElement
public class BancoHoras implements Entidade {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bancoHoras")
    private List<BancoHorasLimite> bancoHorasLimiteList;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdBancoHoras")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idBancoHoras;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "Descricao")
    private String descricao;

    @Basic(optional = false)
    @NotNull
    @Column(name = "GatilhoPositivo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gatilhoPositivo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "GatilhoNegativo")
    @Temporal(TemporalType.TIMESTAMP)
    private Date gatilhoNegativo;

    @JoinColumn(name = "idPercentuaisAcrescimo", referencedColumnName = "idPercentuaisAcrescimo")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private PercentuaisAcrescimo percentuaisAcrescimo;

    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoLimiteDiario")
    private boolean tipoLimiteDiario;

    @Column(name = "TrataFaltaDebito")
    private Boolean trataFaltaDebito;

    @Basic(optional = false)
    @NotNull
    @Column(name = "TrataAbonoDebito")
    private boolean trataAbonoDebito;

    @Basic(optional = false)
    @NotNull
    @Column(name = "TrataAusenciaDebito")
    private boolean trataAusenciaDebito;

    @Basic(optional = false)
    @NotNull
    @Column(name = "NaoPagaAdicionalNoturnoBH")
    private boolean naoPagaAdicionalNoturnoBH;

    @Basic(optional = false)
    @NotNull
    @Column(name = "HabilitaDiaFechamento")
    private boolean habilitaDiaFechamento;

    @Column(name = "DiaFechamentoExtra")
    private Integer diaFechamentoExtra;

    @OneToMany(cascade = {CascadeType.REMOVE, CascadeType.REFRESH}, mappedBy = "bancoHoras")
    private List<FuncionarioBancoHoras> funcionarioBancoHorasList;

    public BancoHoras() {
    }

    public BancoHoras(Integer idBancoHoras) {
        this.idBancoHoras = idBancoHoras;
    }

    public BancoHoras(Integer idBancoHoras, String descricao, boolean tipoLimiteDiario, Date gatilhoPositivo, Date gatilhoNegativo, boolean trataAbonoDebito, boolean trataAusenciaDebito, boolean naoPagaAdicionalNoturnoBH, boolean habilitaDiaFechamento, Integer diaFechamentoExtra) {
        this.idBancoHoras = idBancoHoras;
        this.descricao = descricao;
        this.tipoLimiteDiario = tipoLimiteDiario;
        this.gatilhoPositivo = gatilhoPositivo;
        this.gatilhoNegativo = gatilhoNegativo;
        this.trataAbonoDebito = trataAbonoDebito;
        this.trataAusenciaDebito = trataAusenciaDebito;
        this.naoPagaAdicionalNoturnoBH = naoPagaAdicionalNoturnoBH;
        this.habilitaDiaFechamento = habilitaDiaFechamento;
        this.diaFechamentoExtra = diaFechamentoExtra;
    }

    public Integer getIdBancoHoras() {
        return idBancoHoras;
    }

    public void setIdBancoHoras(Integer idBancoHoras) {
        this.idBancoHoras = idBancoHoras;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean getTipoLimiteDiario() {
        return tipoLimiteDiario;
    }

    public void setTipoLimiteDiario(boolean tipoLimiteDiario) {
        this.tipoLimiteDiario = tipoLimiteDiario;
    }

    public Boolean getTrataFaltaDebito() {
        return trataFaltaDebito;
    }

    public void setTrataFaltaDebito(Boolean trataFaltaDebito) {
        this.trataFaltaDebito = trataFaltaDebito;
    }

    public boolean getTrataAbonoDebito() {
        return trataAbonoDebito;
    }

    public void setTrataAbonoDebito(boolean trataAbonoDebito) {
        this.trataAbonoDebito = trataAbonoDebito;
    }

    public boolean getTrataAusenciaDebito() {
        return trataAusenciaDebito;
    }

    public void setTrataAusenciaDebito(boolean trataAusenciaDebito) {
        this.trataAusenciaDebito = trataAusenciaDebito;
    }

    public boolean getNaoPagaAdicionalNoturnoBH() {
        return naoPagaAdicionalNoturnoBH;
    }

    public void setNaoPagaAdicionalNoturnoBH(boolean naoPagaAdicionalNoturnoBH) {
        this.naoPagaAdicionalNoturnoBH = naoPagaAdicionalNoturnoBH;
    }

    public boolean getHabilitaDiaFechamento() {
        return habilitaDiaFechamento;
    }

    public void setHabilitaDiaFechamento(boolean habilitaDiaFechamento) {
        this.habilitaDiaFechamento = habilitaDiaFechamento;
    }

    public Integer getDiaFechamentoExtra() {
        return diaFechamentoExtra;
    }

    public void setDiaFechamentoExtra(Integer diaFechamentoExtra) {
        this.diaFechamentoExtra = diaFechamentoExtra;
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
    public List<BancoHorasLimite> getBancoHorasLimiteList() {
        return bancoHorasLimiteList;
    }

    public void setBancoHorasLimiteList(List<BancoHorasLimite> bancoHorasLimiteList) {
        this.bancoHorasLimiteList = bancoHorasLimiteList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBancoHoras != null ? idBancoHoras.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BancoHoras)) {
            return false;
        }
        BancoHoras other = (BancoHoras) object;
        if ((this.idBancoHoras == null && other.idBancoHoras != null) || (this.idBancoHoras != null && !this.idBancoHoras.equals(other.idBancoHoras))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.descricao != null ? this.descricao : "";
        return "Banco horas " + s;
    }

    public PercentuaisAcrescimo getPercentuaisAcrescimo() {
        return percentuaisAcrescimo;
    }

    public void setPercentuaisAcrescimo(PercentuaisAcrescimo percentuaisAcrescimo) {
        this.percentuaisAcrescimo = percentuaisAcrescimo;
    }

    public Date getGatilhoPositivo() {
        return gatilhoPositivo;
    }

    public void setGatilhoPositivo(Date gatilhoPositivo) {
        this.gatilhoPositivo = gatilhoPositivo;
    }

    public Date getGatilhoNegativo() {
        return gatilhoNegativo;
    }

    public void setGatilhoNegativo(Date gatilhoNegativo) {
        this.gatilhoNegativo = gatilhoNegativo;
    }

}
