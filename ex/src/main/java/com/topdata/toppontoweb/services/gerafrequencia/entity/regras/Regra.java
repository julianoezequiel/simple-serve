package com.topdata.toppontoweb.services.gerafrequencia.entity.regras;

import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;

/**
 * Informações das regras do dia
 *
 * @author enio.junior
 */
public class Regra {

    private boolean diaProcessado;
    private boolean diaUtil;
    private boolean fechamento;
    private boolean jornada;
    private boolean trabalhado;
    private boolean cumprirHorario;
    private boolean horariosPreAssinalados;
    private boolean feriado;
    private boolean adicionalNoturno;

    private boolean compensaAtrasos;
    private boolean falta;
    private boolean faltandoMarcacoes;
    private boolean marcacoesImpares;
    private boolean intervaloDeslocado;
    private boolean excedeuIntervalo;
    private boolean viradaDiaJornada;
    private boolean viradaDiaTrabalhado;
    private boolean inconsistencia;

    private boolean afastadoComAbono;
    private boolean afastadoSemAbono;
    private boolean trabalhouAfastado;

    private boolean abonoSemSomenteJustificativa;
    private boolean abonoSomenteJustificativa;
    private String justificativaAbono;

    private boolean realizaCompensacao;
    private boolean diaCompensadoSemSomenteJustificativa;
    private boolean compensandoDia;
    private boolean compensacaoInvalida;
    private boolean diaCompensadoSomenteJustificativa;
    private String justificativaDiaCompensado;

    private boolean bancoDeHoras;
    private boolean naoPagaAdicionalBancoDeHoras;

    private String legendaStatusEspelho;
    private String legendaStatusPresenca;

    private String observacoes;

    public Regra() {
        this.diaUtil = Boolean.FALSE;
        this.fechamento = Boolean.FALSE;
        this.jornada = Boolean.FALSE;
        this.trabalhado = Boolean.FALSE;
        this.cumprirHorario = Boolean.FALSE;
        this.horariosPreAssinalados = Boolean.FALSE;
        this.feriado = Boolean.FALSE;
        this.adicionalNoturno = Boolean.FALSE;
        this.compensaAtrasos = Boolean.FALSE;
        this.falta = Boolean.FALSE;
        this.faltandoMarcacoes = Boolean.FALSE;
        this.marcacoesImpares = Boolean.FALSE;
        this.intervaloDeslocado = Boolean.FALSE;
        this.excedeuIntervalo = Boolean.FALSE;
        this.viradaDiaJornada = Boolean.FALSE;
        this.viradaDiaTrabalhado = Boolean.FALSE;
        this.inconsistencia = Boolean.FALSE;
        this.afastadoComAbono = Boolean.FALSE;
        this.afastadoSemAbono = Boolean.FALSE;
        this.trabalhouAfastado = Boolean.FALSE;
        this.abonoSemSomenteJustificativa = Boolean.FALSE;
        this.abonoSomenteJustificativa = Boolean.FALSE;
        this.justificativaAbono = "";
        this.realizaCompensacao = Boolean.FALSE;
        this.diaCompensadoSemSomenteJustificativa = Boolean.FALSE;
        this.compensandoDia = Boolean.FALSE;
        this.compensacaoInvalida = Boolean.FALSE;
        this.diaCompensadoSomenteJustificativa = Boolean.FALSE;
        this.justificativaDiaCompensado = "";
        this.bancoDeHoras = Boolean.FALSE;
        this.naoPagaAdicionalBancoDeHoras = Boolean.FALSE;
        this.legendaStatusEspelho = "";
        this.legendaStatusPresenca = CONSTANTES.LEGENDA_VAZIO;
        this.observacoes = "";
    }

    public boolean isDiaProcessado() {
        return diaProcessado;
    }

    public void setDiaProcessado(boolean diaProcessado) {
        this.diaProcessado = diaProcessado;
    }

    public boolean isDiaUtil() {
        return diaUtil;
    }

    public void setDiaUtil(boolean diaUtil) {
        this.diaUtil = diaUtil;
    }

    public boolean isFechamento() {
        return fechamento;
    }

    public void setFechamento(boolean fechamento) {
        this.fechamento = fechamento;
    }

    public boolean isJornada() {
        return jornada;
    }

    public void setJornada(boolean jornada) {
        this.jornada = jornada;
    }

    public boolean isTrabalhado() {
        return trabalhado;
    }

    public void setTrabalhado(boolean trabalhado) {
        this.trabalhado = trabalhado;
    }

    public boolean isCumprirHorario() {
        return cumprirHorario;
    }

    public void setCumprirHorario(boolean cumprirHorario) {
        this.cumprirHorario = cumprirHorario;
    }

    public boolean isFeriado() {
        return feriado;
    }

    public void setFeriado(boolean feriado) {
        this.feriado = feriado;
    }

    public boolean isAdicionalNoturno() {
        return adicionalNoturno;
    }

    public void setAdicionalNoturno(boolean adicionalNoturno) {
        this.adicionalNoturno = adicionalNoturno;
    }

    public boolean isCompensaAtrasos() {
        return compensaAtrasos;
    }

    public void setCompensaAtrasos(boolean compensaAtrasos) {
        this.compensaAtrasos = compensaAtrasos;
    }

    public boolean isFalta() {
        return falta;
    }

    public void setFalta(boolean falta) {
        this.falta = falta;
    }

    public boolean isFaltandoMarcacoes() {
        return faltandoMarcacoes;
    }

    public void setFaltandoMarcacoes(boolean faltandoMarcacoes) {
        this.faltandoMarcacoes = faltandoMarcacoes;
    }

    public boolean isMarcacoesImpares() {
        return marcacoesImpares;
    }

    public void setMarcacoesImpares(boolean marcacoesImpares) {
        this.marcacoesImpares = marcacoesImpares;
    }

    public boolean isIntervaloDeslocado() {
        return intervaloDeslocado;
    }

    public void setIntervaloDeslocado(boolean intervaloDeslocado) {
        this.intervaloDeslocado = intervaloDeslocado;
    }

    public boolean isExcedeuIntervalo() {
        return excedeuIntervalo;
    }

    public void setExcedeuIntervalo(boolean excedeuIntervalo) {
        this.excedeuIntervalo = excedeuIntervalo;
    }

    public boolean isViradaDiaJornada() {
        return viradaDiaJornada;
    }

    public void setViradaDiaJornada(boolean viradaDiaJornada) {
        this.viradaDiaJornada = viradaDiaJornada;
    }

    public boolean isViradaDiaTrabalhado() {
        return viradaDiaTrabalhado;
    }

    public void setViradaDiaTrabalhado(boolean viradaDiaTrabalhado) {
        this.viradaDiaTrabalhado = viradaDiaTrabalhado;
    }

    public boolean isInconsistencia() {
        return inconsistencia;
    }

    public void setInconsistencia(boolean inconsistencia) {
        this.inconsistencia = inconsistencia;
    }

    public boolean isAfastadoComAbono() {
        return afastadoComAbono;
    }

    public void setAfastadoComAbono(boolean afastadoComAbono) {
        this.afastadoComAbono = afastadoComAbono;
    }

    public boolean isAfastadoSemAbono() {
        return afastadoSemAbono;
    }

    public void setAfastadoSemAbono(boolean afastadoSemAbono) {
        this.afastadoSemAbono = afastadoSemAbono;
    }

    public boolean isTrabalhouAfastado() {
        return trabalhouAfastado;
    }

    public void setTrabalhouAfastado(boolean trabalhouAfastado) {
        this.trabalhouAfastado = trabalhouAfastado;
    }

    public boolean isAbonoSemSomenteJustificativa() {
        return abonoSemSomenteJustificativa;
    }

    public void setAbonoSemSomenteJustificativa(boolean abonoSemSomenteJustificativa) {
        this.abonoSemSomenteJustificativa = abonoSemSomenteJustificativa;
    }

    public boolean isAbonoSomenteJustificativa() {
        return abonoSomenteJustificativa;
    }

    public void setAbonoSomenteJustificativa(boolean abonoSomenteJustificativa) {
        this.abonoSomenteJustificativa = abonoSomenteJustificativa;
    }

    public String getJustificativaAbono() {
        return justificativaAbono;
    }

    public void setJustificativaAbono(String justificativaAbono) {
        this.justificativaAbono = justificativaAbono;
    }

    public boolean isRealizaCompensacao() {
        return realizaCompensacao;
    }

    public void setRealizaCompensacao(boolean realizaCompensacao) {
        this.realizaCompensacao = realizaCompensacao;
    }

    public boolean isDiaCompensadoSemSomenteJustificativa() {
        return diaCompensadoSemSomenteJustificativa;
    }

    public void setDiaCompensadoSemSomenteJustificativa(boolean diaCompensadoSemSomenteJustificativa) {
        this.diaCompensadoSemSomenteJustificativa = diaCompensadoSemSomenteJustificativa;
    }

    public boolean isCompensandoDia() {
        return compensandoDia;
    }

    public void setCompensandoDia(boolean compensandoDia) {
        this.compensandoDia = compensandoDia;
    }

    public boolean isCompensacaoInvalida() {
        return compensacaoInvalida;
    }

    public void setCompensacaoInvalida(boolean compensacaoInvalida) {
        this.compensacaoInvalida = compensacaoInvalida;
    }

    public boolean isDiaCompensadoSomenteJustificativa() {
        return diaCompensadoSomenteJustificativa;
    }

    public void setDiaCompensadoSomenteJustificativa(boolean diaCompensadoSomenteJustificativa) {
        this.diaCompensadoSomenteJustificativa = diaCompensadoSomenteJustificativa;
    }

    public String getJustificativaDiaCompensado() {
        return justificativaDiaCompensado;
    }

    public void setJustificativaDiaCompensado(String justificativaDiaCompensado) {
        this.justificativaDiaCompensado = justificativaDiaCompensado;
    }

    public boolean isBancoDeHoras() {
        return bancoDeHoras;
    }

    public void setBancoDeHoras(boolean bancoDeHoras) {
        this.bancoDeHoras = bancoDeHoras;
    }

    public boolean isNaoPagaAdicionalBancoDeHoras() {
        return naoPagaAdicionalBancoDeHoras;
    }

    public void setNaoPagaAdicionalBancoDeHoras(boolean naoPagaAdicionalBancoDeHoras) {
        this.naoPagaAdicionalBancoDeHoras = naoPagaAdicionalBancoDeHoras;
    }

    public boolean isHorariosPreAssinalados() {
        return horariosPreAssinalados;
    }

    public void setHorariosPreAssinalados(boolean horariosPreAssinalados) {
        this.horariosPreAssinalados = horariosPreAssinalados;
    }

    public String getLegendaStatusEspelho() {
        return legendaStatusEspelho;
    }

    public void setLegendaStatusEspelho(String legendaStatusEspelho) {
        this.legendaStatusEspelho = legendaStatusEspelho;
    }

    public String getLegendaStatusPresenca() {
        return legendaStatusPresenca;
    }

    public void setLegendaStatusPresenca(String legendaStatusPresenca) {
        this.legendaStatusPresenca = legendaStatusPresenca;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

}
