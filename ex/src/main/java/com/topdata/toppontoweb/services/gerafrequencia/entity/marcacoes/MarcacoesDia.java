package com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes;

import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.utils.CustomHourSerializer;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * Marcação coletada (trabalhada)
 *
 * @author enio.junior
 */
public class MarcacoesDia {

    private int idSequencia;
    private Date horarioEntrada;
    private String motivoStatusEntrada;
    private String statusEntrada;
    private StatusMarcacao statusMarcacaoEntrada;
    private String numeroSerieEntrada;
    private Integer idMarcacaoEntrada;
    
    private Date horarioSaida;
    private String motivoStatusSaida;
    private String statusSaida;
    private StatusMarcacao statusMarcacaoSaida;
    private String numeroSerieSaida;
    private Integer idMarcacaoSaida;

    public MarcacoesDia() {
        this.idSequencia = 0;
        this.motivoStatusEntrada = "";
        this.statusEntrada = "";
        this.motivoStatusEntrada = "";
        this.statusSaida = "";
        this.statusMarcacaoEntrada = new StatusMarcacao();
        this.statusMarcacaoSaida = new StatusMarcacao();
        this.numeroSerieEntrada = "00000000000000000";
        this.numeroSerieSaida = "00000000000000000";
        this.idMarcacaoEntrada=0;
        this.idMarcacaoSaida=0;
    }

    public int getIdSequencia() {
        return idSequencia;
    }

    public void setIdSequencia(int idSequencia) {
        this.idSequencia = idSequencia;
    }

    @JsonSerialize(using = CustomHourSerializer.class)
    public Date getHorarioEntrada() {
        return horarioEntrada;
    }

    public void setHorarioEntrada(Date horarioEntrada) {
        this.horarioEntrada = horarioEntrada;
    }

    public String getMotivoStatusEntrada() {
        return motivoStatusEntrada;
    }

    public void setMotivoStatusEntrada(String motivoStatusEntrada) {
        this.motivoStatusEntrada = motivoStatusEntrada;
    }
    
    @JsonSerialize(using = CustomHourSerializer.class)
    public Date getHorarioSaida() {
        return horarioSaida;
    }

    public void setHorarioSaida(Date horarioSaida) {
        this.horarioSaida = horarioSaida;
    }

    public String getMotivoStatusSaida() {
        return motivoStatusSaida;
    }

    public void setMotivoStatusSaida(String motivoStatusSaida) {
        this.motivoStatusSaida = motivoStatusSaida;
    }

    public String getStatusEntrada() {
        return statusEntrada;
    }

    public void setStatusEntrada(String statusEntrada) {
        this.statusEntrada = statusEntrada;
    }

    public String getStatusSaida() {
        return statusSaida;
    }

    public void setStatusSaida(String statusSaida) {
        this.statusSaida = statusSaida;
    }

    public StatusMarcacao getStatusMarcacaoEntrada() {
        return statusMarcacaoEntrada;
    }

    public void setStatusMarcacaoEntrada(StatusMarcacao statusMarcacaoEntrada) {
        this.statusMarcacaoEntrada = statusMarcacaoEntrada;
    }

    public StatusMarcacao getStatusMarcacaoSaida() {
        return statusMarcacaoSaida;
    }

    public void setStatusMarcacaoSaida(StatusMarcacao statusMarcacaoSaida) {
        this.statusMarcacaoSaida = statusMarcacaoSaida;
    }

    public String getNumeroSerieEntrada() {
        return numeroSerieEntrada;
    }

    public void setNumeroSerieEntrada(String numeroSerieEntrada) {
        this.numeroSerieEntrada = numeroSerieEntrada;
    }

    public String getNumeroSerieSaida() {
        return numeroSerieSaida;
    }

    public void setNumeroSerieSaida(String numeroSerieSaida) {
        this.numeroSerieSaida = numeroSerieSaida;
    }

    public Integer getIdMarcacaoEntrada() {
        return idMarcacaoEntrada;
    }

    public void setIdMarcacaoEntrada(Integer idMarcacaoEntrada) {
        this.idMarcacaoEntrada = idMarcacaoEntrada;
    }

    public Integer getIdMarcacaoSaida() {
        return idMarcacaoSaida;
    }

    public void setIdMarcacaoSaida(Integer idMarcacaoSaida) {
        this.idMarcacaoSaida = idMarcacaoSaida;
    }
    
}
