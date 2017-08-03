package com.topdata.toppontoweb.entity.funcionario;

import java.util.Date;

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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.topdata.toppontoweb.dto.funcionario.bancohoras.Gatilho;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.TipoAcerto;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.utils.CustomDateSerializer;

/**
 * @version 1.0.0 data 20/06/2017
 * @since 1.0.0 data 20/06/2017
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "FuncionarioBancoHorasFechamento")
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class FuncionarioBancoHorasFechamento implements Entidade, Cloneable {

    /**
     * @since 1.0.0 data 05/07/2017
     * @author juliano.ezequiel
     */
    @JoinColumn(name = "idTipoAcerto", referencedColumnName = "idTipoAcerto")
    @ManyToOne
    private TipoAcerto tipoAcerto;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "idFuncionarioBancoHorasFechamento")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idFuncionarioBancoHorasFechamento;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataFechamento")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataFechamento;

    @Column(name = "Credito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date credito;

    @Column(name = "Debito")
    @Temporal(TemporalType.TIMESTAMP)
    private Date debito;

    @JoinColumn(name = "IdColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "idFuncionarioBancoHoras", referencedColumnName = "idFuncionarioBancoHoras")
    @ManyToOne(optional = false, cascade = CascadeType.REFRESH)
    private FuncionarioBancoHoras funcionarioBancoHoras;

    @JoinColumn(name = "idTipoFechamento", referencedColumnName = "idTipoFechamento")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private TipoFechamento tipoFechamento;

    @Transient
    private Funcionario funcionario;

    @Transient
    private Gatilho gatilho;

    public Gatilho getGatilho() {
        return gatilho;
    }

    public void setGatilho(Gatilho gatilho) {
        this.gatilho = gatilho;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public FuncionarioBancoHorasFechamento() {
    }

    public FuncionarioBancoHorasFechamento(Integer idFuncionarioBancoHorasFechamento) {
        this.idFuncionarioBancoHorasFechamento = idFuncionarioBancoHorasFechamento;
    }

    public FuncionarioBancoHorasFechamento(Integer idFuncionarioBancoHorasFechamento, Date dataFechamento) {
        this.idFuncionarioBancoHorasFechamento = idFuncionarioBancoHorasFechamento;
        this.dataFechamento = dataFechamento;
    }

    public Integer getIdFuncionarioBancoHorasFechamento() {
        return idFuncionarioBancoHorasFechamento;
    }

    public void setIdFuncionarioBancoHorasFechamento(Integer idFuncionarioBancoHorasFechamento) {
        this.idFuncionarioBancoHorasFechamento = idFuncionarioBancoHorasFechamento;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Date getCredito() {
        return credito;
    }

    public void setCredito(Date credito) {
        this.credito = credito;
    }

    public Date getDebito() {
        return debito;
    }

    public void setDebito(Date debito) {
        this.debito = debito;
    }

    public Coletivo getColetivo() {
        return coletivo;
    }

    public void setColetivo(Coletivo coletivo) {
        this.coletivo = coletivo;
    }

    public FuncionarioBancoHoras getFuncionarioBancoHoras() {
        return funcionarioBancoHoras;
    }

    public void setFuncionarioBancoHoras(FuncionarioBancoHoras funcionarioBancoHoras) {
        this.funcionarioBancoHoras = funcionarioBancoHoras;
    }

    public TipoFechamento getTipoFechamento() {
        return tipoFechamento;
    }

    public void setTipoFechamento(TipoFechamento tipoFechamento) {
        this.tipoFechamento = tipoFechamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFuncionarioBancoHorasFechamento != null ? idFuncionarioBancoHorasFechamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof FuncionarioBancoHorasFechamento)) {
            return false;
        }
        FuncionarioBancoHorasFechamento other = (FuncionarioBancoHorasFechamento) object;
        if ((this.idFuncionarioBancoHorasFechamento == null && other.idFuncionarioBancoHorasFechamento != null) || (this.idFuncionarioBancoHorasFechamento != null && !this.idFuncionarioBancoHorasFechamento.equals(other.idFuncionarioBancoHorasFechamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Fechamento ";
    }

    @Transient
    private boolean manual = false;

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public boolean isManual() {
        return manual;
    }

    public TipoAcerto getTipoAcerto() {
        return tipoAcerto;
    }

    public void setTipoAcerto(TipoAcerto tipoAcerto) {
        this.tipoAcerto = tipoAcerto;
    }

    @Transient
    private Boolean recalcularSaldos = true;

    public Boolean getRecalcularSaldos() {
        return recalcularSaldos;
    }

    public void setRecalcularSaldos(Boolean recalcularSaldos) {
        this.recalcularSaldos = recalcularSaldos;
    }

}
