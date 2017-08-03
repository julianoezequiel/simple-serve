package com.topdata.toppontoweb.entity.jornada;

import java.time.LocalTime;
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
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.tipo.TipoDia;

/**
 * @version 1.0.6 data 06/10/2016
 * @since 1.0.6 data 06/10/2016
 * @author juliano.ezequiel
 */
@Entity
@Table(name = "Horario")
@XmlRootElement
public class Horario implements Entidade, Cloneable {

    @Column(name = "PreAssinalada")
    private Boolean preAssinalada;
    @Column(name = "TrataComoDiaNormal")
    private Boolean trataComoDiaNormal;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IdHorario")
    @SequenceGenerator(name = "seq_usu", initialValue = 1)
    @GeneratedValue(generator = "seq_usu", strategy = GenerationType.IDENTITY)
    private Integer idHorario;

    @Size(max = 50)
    @Column(name = "Descricao")
    private String descricao;

    //fetch = FetchType.EAGER, orphanRemoval = true
    @OneToMany(cascade = {CascadeType.REFRESH}, mappedBy = "horario")
    private List<HorarioMarcacao> horarioMarcacaoList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "horario")
    private List<JornadaHorario> jornadaHorarioList;

    @JoinColumn(name = "IdTipodia", referencedColumnName = "IdTipodia")
    @ManyToOne(optional = false)
    private TipoDia tipodia;

    @Transient
    private Date totalHoras;

    public Date getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Date totalHoras) {
        this.totalHoras = totalHoras;
    }

    @Transient
    private LocalTime entrada;

    @Transient
    private LocalTime saida;

    public LocalTime getEntrada() {
        return entrada;
    }

    public void setEntrada(LocalTime entrada) {
        this.entrada = entrada;
    }

    public LocalTime getSaida() {
        return saida;
    }

    public void setSaida(LocalTime saida) {
        this.saida = saida;
    }

    public Horario() {
    }

    public Horario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Horario(Integer idHorario, String descricao, boolean preAssinalada, TipoDia idTipodia) {
        this.idHorario = idHorario;
        this.descricao = descricao;
        this.preAssinalada = preAssinalada;
        this.tipodia = idTipodia;
    }

    public Horario(Integer idHorario, boolean preAssinalada) {
        this.idHorario = idHorario;
        this.preAssinalada = preAssinalada;
    }

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    @JsonIgnore
    public List<HorarioMarcacao> getHorarioMarcacaoList() {
        return horarioMarcacaoList;
    }

    public void setHorarioMarcacaoList(List<HorarioMarcacao> horarioMarcacaoList) {
        this.horarioMarcacaoList = horarioMarcacaoList;
    }

    @XmlTransient
    @JsonIgnore
    public List<JornadaHorario> getJornadaHorarioList() {
        return jornadaHorarioList;
    }

    public void setJornadaHorarioList(List<JornadaHorario> jornadaHorarioList) {
        this.jornadaHorarioList = jornadaHorarioList;
    }

    public TipoDia getTipodia() {
        return tipodia;
    }

    public void setTipodia(TipoDia tipodia) {
        this.tipodia = tipodia;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHorario != null ? idHorario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Horario)) {
            return false;
        }
        Horario other = (Horario) object;
        if ((this.idHorario == null && other.idHorario != null) || (this.idHorario != null && !this.idHorario.equals(other.idHorario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Horário ";
    }

    public Boolean getPreAssinalada() {
        return preAssinalada;
    }

    public void setPreAssinalada(Boolean preAssinalada) {
        this.preAssinalada = preAssinalada;
    }

    public Boolean getTrataComoDiaNormal() {
        return trataComoDiaNormal;
    }

    public void setTrataComoDiaNormal(Boolean trataComoDiaNormal) {
        this.trataComoDiaNormal = trataComoDiaNormal;
    }

}
