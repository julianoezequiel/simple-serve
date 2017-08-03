package com.topdata.toppontoweb.entity.funcionario;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
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
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Compensacao")
@XmlRootElement
public class Compensacao implements Entidade, Cloneable {

    @Transient
    private Boolean manual;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdCompensacao")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idCompensacao;

    @Column(name = "DataCompensada")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataCompensada;

    @Column(name = "DataInicio")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataInicio;

    @Basic(optional = false)
    @NotNull
    @Column(name = "DataFim")
    @Temporal(TemporalType.DATE)
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataFim;

    @Column(name = "LimiteDiario")
    @Temporal(TemporalType.TIME)
    private Date limiteDiario;

    @Column(name = "ConsideraDiasSemJornada")
    private Boolean consideraDiasSemJornada;

    @JoinColumn(name = "idColetivo", referencedColumnName = "IdColetivo")
    @ManyToOne
    private Coletivo coletivo;

    @JoinColumn(name = "IdFuncionario", referencedColumnName = "IdFuncionario")
    @ManyToOne(optional = false)
    private Funcionario funcionario;

    @JoinColumn(name = "IdMotivo", referencedColumnName = "IdMotivo")
    @ManyToOne(optional = false)
    private Motivo motivo;

    /**
     *
     */
    public Compensacao() {
    }

    public Compensacao(Integer idCompensacao) {
        this.idCompensacao = idCompensacao;
    }

    public Compensacao(Integer idCompensacao, Date dataFim) {
        this.idCompensacao = idCompensacao;
        this.dataFim = dataFim;
    }

    public Integer getIdCompensacao() {
        return idCompensacao;
    }

    public void setIdCompensacao(Integer idCompensacao) {
        this.idCompensacao = idCompensacao;
    }

    public Date getDataCompensada() {
        return dataCompensada;
    }

    public void setDataCompensada(Date dataCompensada) {
        this.dataCompensada = dataCompensada;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getLimiteDiario() {
        return limiteDiario;
    }

    public void setLimiteDiario(Date limiteDiario) {
        this.limiteDiario = limiteDiario;
    }

    public Boolean getConsideraDiasSemJornada() {
        return consideraDiasSemJornada;
    }

    public void setConsideraDiasSemJornada(Boolean consideraDiasSemJornada) {
        this.consideraDiasSemJornada = consideraDiasSemJornada;
    }

    public Coletivo getColetivo() {
        return coletivo;
    }

    public void setColetivo(Coletivo coletivo) {
        this.coletivo = coletivo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCompensacao != null ? idCompensacao.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Compensacao)) {
            return false;
        }
        Compensacao other = (Compensacao) object;
        if ((this.idCompensacao == null && other.idCompensacao != null) || (this.idCompensacao != null && !this.idCompensacao.equals(other.idCompensacao))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Compensação " + (this.motivo != null ? this.motivo.getDescricao() : "");
    }

    public Boolean getManual() {
        return manual;
    }

    public void setManual(Boolean manual) {
        this.manual = manual;
    }

}
