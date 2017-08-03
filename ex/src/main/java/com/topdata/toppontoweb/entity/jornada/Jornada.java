package com.topdata.toppontoweb.entity.jornada;

import java.util.Date;
import java.util.List;

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

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.tipo.Semana;

/**
 * @version 1.0.6 data 06/10/2016
 * @version 1.0.4 data 16/08/2016
 * @version 1.0.1 data 10/04/2016
 * @since 1.0.1 data 10/04/2016
 *
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Jornada")
@XmlRootElement
public class Jornada implements Entidade, Cloneable {

    /**
     * @since 1.0.6 data 08/12/2016
     *
     */
    @JoinColumn(name = "idTipoJornada", referencedColumnName = "idTipoJornada")
    @ManyToOne(optional = false)
    private TipoJornada tipoJornada;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdJornada")
    @SequenceGenerator(name = "seq", initialValue = 1)
    @GeneratedValue(generator = "seq", strategy = GenerationType.IDENTITY)
    private Integer idJornada;

    @Transient
    private String percentualNoturno;

    public String getPercentualNoturno() {
        return percentualNoturno == null ? percentualNoturno : percentualNoturno.replaceAll("[\\.\\,]0+$", "");
    }

    public void setPercentualNoturno(String percentualNoturno) {
        this.percentualNoturno = percentualNoturno;
    }

    @Transient
    private Boolean possuiVinculo;

    @JsonIgnore
    public void carregarPossuiVinculo() {
        Boolean b = this.getFuncionarioJornadaList() != null ? (this.getFuncionarioJornadaList().size() > 1 || !this.getFuncionarioJornadaList().isEmpty()) : false;
        this.possuiVinculo = b;
    }

    public Boolean getPossuiVinculo() {
        return possuiVinculo;
    }

    public void setPossuiVinculo(Boolean possuiVinculo) {
        this.possuiVinculo = possuiVinculo;
    }

    @JoinColumn(name = "idPercentuaisAcrescimo", referencedColumnName = "idPercentuaisAcrescimo")
    @ManyToOne(cascade = CascadeType.REFRESH)
    private PercentuaisAcrescimo percentuaisAcrescimo;

    @JoinColumn(name = "IdHorasExtrasAcumulo", referencedColumnName = "IdHorasExtrasAcumulo")
    @ManyToOne
    private HorasExtrasAcumulo horasExtrasAcumulo = new HorasExtrasAcumulo(1);

    @Column(name = "ToleranciaOcorrencia")
    @Temporal(TemporalType.TIME)
    private Date toleranciaOcorrencia;

    @Column(name = "DiaFechamentoExtra")
    private Integer diaFechamentoExtra;

    @Size(max = 50)
    @Column(name = "Descricao")
    private String descricao;

    @Column(name = "CompensaAtrasos")
    private Boolean compensaAtrasos;

    @Column(name = "ToleranciaAusencia")
    @Temporal(TemporalType.TIME)
    private Date toleranciaAusencia;

    @Column(name = "ToleranciaExtra")
    @Temporal(TemporalType.TIME)
    private Date toleranciaExtra;

    @Column(name = "TotalHoras")
    @Temporal(TemporalType.TIMESTAMP)
    private Date totalHoras;

    @Column(name = "TotalPeriodos")
    private Integer totalPeriodos;

    @Column(name = "PagaHorasFeriado")
    private Boolean pagaHorasFeriado;

    @Column(name = "InicioAdicionalNoturno")
    @Temporal(TemporalType.TIME)
    private Date inicioAdicionalNoturno;

    @Column(name = "TerminoAdicionalNoturno")
    @Temporal(TemporalType.TIME)
    private Date terminoAdicionalNoturno;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    @Column(name = "PercentualAdicionalNoturno")
    private Double percentualAdicionalNoturno;

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REFRESH}, orphanRemoval = true  ,mappedBy = "jornada", fetch = FetchType.EAGER)
    private List<JornadaHorario> jornadaHorarioList;

    @Column(name = "DescontaHorasDSR")
    @Temporal(TemporalType.TIMESTAMP)
    private Date descontaHorasDSR;

//    @Column(name = "JornadaVariavel")
//    private Boolean jornadaVariavel;
    @Column(name = "TrataSabadoDiaNormal")
    private Boolean trataSabadoDiaNormal;

    @Column(name = "TrataDomingoDiaNormal")
    private Boolean trataDomingoDiaNormal;

    @OneToMany(cascade = {CascadeType.ALL, CascadeType.REFRESH}, mappedBy = "jornada")
    private List<FuncionarioJornada> funcionarioJornadaList;

    @JoinColumn(name = "IdSemana", referencedColumnName = "IdSemana")
    @ManyToOne
    private Semana semana;

    public Jornada() {
    }

    public Jornada(Integer idJornada) {
        this.idJornada = idJornada;
    }

    public Jornada(Integer idJornada, Date descontaHorasDSR, int diaFechamentoExtra) {
        this.idJornada = idJornada;
        this.descontaHorasDSR = descontaHorasDSR;
        this.diaFechamentoExtra = diaFechamentoExtra;
    }

    public Integer getIdJornada() {
        return idJornada;
    }

    public void setIdJornada(Integer idJornada) {
        this.idJornada = idJornada;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDescontaHorasDSR() {
        return descontaHorasDSR;
    }

    public void setDescontaHorasDSR(Date descontaHorasDSR) {
        this.descontaHorasDSR = descontaHorasDSR;
    }

//    /**
//     *
//     * @return
//     */
//    public Boolean getJornadaVariavel() {
//        return jornadaVariavel;
//    }
//
//    public void setJornadaVariavel(Boolean jornadaVariavel) {
//        this.jornadaVariavel = jornadaVariavel;
//    }
    public Boolean getTrataSabadoDiaNormal() {
        return trataSabadoDiaNormal;
    }

    public void setTrataSabadoDiaNormal(Boolean trataSabadoDiaNormal) {
        this.trataSabadoDiaNormal = trataSabadoDiaNormal;
    }

    public Boolean getTrataDomingoDiaNormal() {
        return trataDomingoDiaNormal;
    }

    public void setTrataDomingoDiaNormal(Boolean trataDomingoDiaNormal) {
        this.trataDomingoDiaNormal = trataDomingoDiaNormal;
    }

    @XmlTransient
    @JsonIgnore
    public List<FuncionarioJornada> getFuncionarioJornadaList() {
        return funcionarioJornadaList;
    }

    /**
     *
     * @param funcionarioJornadaList
     */
    public void setFuncionarioJornadaList(List<FuncionarioJornada> funcionarioJornadaList) {
        this.funcionarioJornadaList = funcionarioJornadaList;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idJornada != null ? idJornada.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Jornada)) {
            return false;
        }
        Jornada other = (Jornada) object;
        if ((this.idJornada == null && other.idJornada != null) || (this.idJornada != null && !this.idJornada.equals(other.idJornada))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String s = this.getDescricao() != null ? this.getDescricao() : "";
        return "Jornada " + s;
    }

    public Boolean getPagaHorasFeriado() {
        return pagaHorasFeriado;
    }

    public void setPagaHorasFeriado(Boolean pagaHorasFeriado) {
        this.pagaHorasFeriado = pagaHorasFeriado;
    }

    public Date getInicioAdicionalNoturno() {
        return inicioAdicionalNoturno;
    }

    public void setInicioAdicionalNoturno(Date inicioAdicionalNoturno) {
        this.inicioAdicionalNoturno = inicioAdicionalNoturno;
    }

    public Date getTerminoAdicionalNoturno() {
        return terminoAdicionalNoturno;
    }

    public void setTerminoAdicionalNoturno(Date terminoAdicionalNoturno) {
        this.terminoAdicionalNoturno = terminoAdicionalNoturno;
    }

    public Double getPercentualAdicionalNoturno() {
        return percentualAdicionalNoturno;
    }

    public void setPercentualAdicionalNoturno(Double percentualAdicionalNoturno) {
        this.percentualAdicionalNoturno = percentualAdicionalNoturno;
    }

    @XmlTransient
    @JsonIgnore
    public List<JornadaHorario> getJornadaHorarioList() {
        return jornadaHorarioList;
    }

    public void setJornadaHorarioList(List<JornadaHorario> jornadaHorarioList) {
        this.jornadaHorarioList = jornadaHorarioList;
    }

    public Boolean getCompensaAtrasos() {
        return compensaAtrasos;
    }

    public void setCompensaAtrasos(Boolean compensaAtrasos) {
        this.compensaAtrasos = compensaAtrasos;
    }

    public Date getToleranciaAusencia() {
        return toleranciaAusencia;
    }

    public void setToleranciaAusencia(Date toleranciaAusencia) {
        this.toleranciaAusencia = toleranciaAusencia;
    }

    public Date getToleranciaExtra() {
        return toleranciaExtra;
    }

    public void setToleranciaExtra(Date toleranciaExtra) {
        this.toleranciaExtra = toleranciaExtra;
    }

    public Date getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Date totalHoras) {
        this.totalHoras = totalHoras;
    }

    public Integer getTotalPeriodos() {
        return totalPeriodos;
    }

    public void setTotalPeriodos(Integer totalPeriodos) {
        this.totalPeriodos = totalPeriodos;
    }

    public Date getToleranciaOcorrencia() {
        return toleranciaOcorrencia;
    }

    public void setToleranciaOcorrencia(Date toleranciaOcorrencia) {
        this.toleranciaOcorrencia = toleranciaOcorrencia;
    }

    public Integer getDiaFechamentoExtra() {
        return diaFechamentoExtra;
    }

    public void setDiaFechamentoExtra(Integer diaFechamentoExtra) {
        this.diaFechamentoExtra = diaFechamentoExtra;
    }

    public HorasExtrasAcumulo getHorasExtrasAcumulo() {
        return horasExtrasAcumulo;
    }

    public void setHorasExtrasAcumulo(HorasExtrasAcumulo horasExtrasAcumulo) {
        this.horasExtrasAcumulo = horasExtrasAcumulo;
    }

    public PercentuaisAcrescimo getPercentuaisAcrescimo() {
        return percentuaisAcrescimo;
    }

    public void setPercentuaisAcrescimo(PercentuaisAcrescimo percentuaisAcrescimo) {
        this.percentuaisAcrescimo = percentuaisAcrescimo;
    }

    public TipoJornada getTipoJornada() {
        return tipoJornada;
    }

    public void setTipoJornada(TipoJornada tipoJornada) {
        this.tipoJornada = tipoJornada;
    }

}
