package com.topdata.toppontoweb.services.gerafrequencia.entity.calculo;

import java.util.Date;

/**
 * Uso somente da função main
 *
 * @author enio.junior
 */
public class DadosEntrada {

    private Date admissaoData;
    private Date demissaoData;

    private Date jornadaDataInicio;
    private int jornadaTipoJornada;
    private boolean jornadaCompensaAtrasos;
    private Date jornadaHorasDescontoSemanalDSR;
    private Date jornadaToleranciaExtras;
    private Date jornadaToleranciaAusencias;
    private Date jornadaToleranciaOcorrencias;
    //private Date TerminoDiaAnterior;
    private Date jornadaInicioPeriodoNoturno;
    private Date jornadaTerminoPeriodoNoturno;
    private String jornadaPercentualNoturno;

    private int horarioTipoDia;
    private boolean horarioPreAssinalada;
    private boolean horarioTrataComoDiaNormal;
    private Date horarioEntrada1;
    private Date horarioSaida1;
    private Date horarioEntrada2;
    private Date horarioSaida2;

    //Configurações gerais
    private Date limiteCorteEntrada;
    private Date limiteCorteSaida;
    private boolean marcacoesDiaSeguinteViradaDia;

    private Date feriadoData;

    private Date afastadoDataInicio;
    private Date afastadoDataFim;
    private boolean afastadoComAbono;

    private Date compensandoData;
    private Date compensandoPeriodoInicio;
    private Date compensandoPeriodoTermino;
    private Date compensandoLimiteDiario;
    private boolean compensandoConsideraDiasSemJornada;

    private Date bhData;
    private boolean bhTipoLimiteDiario;
    private Date bhLimiteDiarioTipoDia;
    private boolean bhTrataFaltaComoDebito;
    private boolean bhTrataAbonoComoDebito;
    private boolean bhTrataAusenciaComoDebito;
    private boolean bhNaoPagaAdicionalNoturnoBH;
    private Date bhGatilhoPositivo;
    private Date bhGatilhoNegativo;

    private Date acertoData;
    private Date edicaoSaldoData;
    private Date subtotalData;
    private Date debitar;
    private Date creditar;

    private Date abonoData;
    private Date abonoDiurnasAbonadas;
    private Date abonoNoturnasAbonadas;
    private boolean abonoDescontarBH;
    private boolean abonoIncluirSomenteJustificativa;

    private Date dsrLimiteHorasFaltasSemanal;
    private boolean dsrIncluirHorasExtrasCalculo;
    private boolean dsrIncluirFeriadosComoHoraNormal;
    private boolean dsrDescontarAusencias;
    private boolean dsrIntegral;

    private Date dataFechamento;
    private Date dataNaoDescontarDSR;

    private String diaNormalSequencia1horas;
    private String diaNormalSequencia1percentual;
    private String diaNormalSequencia2horas;
    private String diaNormalSequencia2percentual;
    private String diaNormalSequencia3horas;
    private String diaNormalSequencia3percentual;
    private String diaNormalSequencia4horas;
    private String diaNormalSequencia4percentual;

    private String diaCompensacaoSequencia1horas;
    private String diaCompensacaoSequencia1percentual;
    private String diaCompensacaoSequencia2horas;
    private String diaCompensacaoSequencia2percentual;
    private String diaCompensacaoSequencia3horas;
    private String diaCompensacaoSequencia3percentual;

    private String horasNoturnasSequencia1horas;
    private String horasNoturnasSequencia1percentual;
    private String horasNoturnasSequencia2horas;
    private String horasNoturnasSequencia2percentual;
    private String horasNoturnasSequencia3horas;
    private String horasNoturnasSequencia3percentual;

    public DadosEntrada() {
    }

    public Date getAdmissaoData() {
        return admissaoData;
    }

    public void setAdmissaoData(Date admissaoData) {
        this.admissaoData = admissaoData;
    }

    public Date getDemissaoData() {
        return demissaoData;
    }

    public void setDemissaoData(Date demissaoData) {
        this.demissaoData = demissaoData;
    }

    public Date getJornadaDataInicio() {
        return jornadaDataInicio;
    }

    public void setJornadaDataInicio(Date jornadaDataInicio) {
        this.jornadaDataInicio = jornadaDataInicio;
    }

    public int getJornadaTipoJornada() {
        return jornadaTipoJornada;
    }

    public void setJornadaTipoJornada(int jornadaTipoJornada) {
        this.jornadaTipoJornada = jornadaTipoJornada;
    }

    public boolean isJornadaCompensaAtrasos() {
        return jornadaCompensaAtrasos;
    }

    public void setJornadaCompensaAtrasos(boolean jornadaCompensaAtrasos) {
        this.jornadaCompensaAtrasos = jornadaCompensaAtrasos;
    }

    public Date getJornadaHorasDescontoSemanalDSR() {
        return jornadaHorasDescontoSemanalDSR;
    }

    public void setJornadaHorasDescontoSemanalDSR(Date jornadaHorasDescontoSemanalDSR) {
        this.jornadaHorasDescontoSemanalDSR = jornadaHorasDescontoSemanalDSR;
    }

    public Date getJornadaToleranciaExtras() {
        return jornadaToleranciaExtras;
    }

    public void setJornadaToleranciaExtras(Date jornadaToleranciaExtras) {
        this.jornadaToleranciaExtras = jornadaToleranciaExtras;
    }

    public Date getJornadaToleranciaAusencias() {
        return jornadaToleranciaAusencias;
    }

    public void setJornadaToleranciaAusencias(Date jornadaToleranciaAusencias) {
        this.jornadaToleranciaAusencias = jornadaToleranciaAusencias;
    }

    public Date getJornadaToleranciaOcorrencias() {
        return jornadaToleranciaOcorrencias;
    }

    public void setJornadaToleranciaOcorrencias(Date jornadaToleranciaOcorrencias) {
        this.jornadaToleranciaOcorrencias = jornadaToleranciaOcorrencias;
    }

    public Date getJornadaInicioPeriodoNoturno() {
        return jornadaInicioPeriodoNoturno;
    }

    public void setJornadaInicioPeriodoNoturno(Date jornadaInicioPeriodoNoturno) {
        this.jornadaInicioPeriodoNoturno = jornadaInicioPeriodoNoturno;
    }

    public Date getJornadaTerminoPeriodoNoturno() {
        return jornadaTerminoPeriodoNoturno;
    }

    public void setJornadaTerminoPeriodoNoturno(Date jornadaTerminoPeriodoNoturno) {
        this.jornadaTerminoPeriodoNoturno = jornadaTerminoPeriodoNoturno;
    }

    public String getJornadaPercentualNoturno() {
        return jornadaPercentualNoturno;
    }

    public void setJornadaPercentualNoturno(String jornadaPercentualNoturno) {
        this.jornadaPercentualNoturno = jornadaPercentualNoturno;
    }

    public int getHorarioTipoDia() {
        return horarioTipoDia;
    }

    public void setHorarioTipoDia(int horarioTipoDia) {
        this.horarioTipoDia = horarioTipoDia;
    }

    public boolean isHorarioPreAssinalada() {
        return horarioPreAssinalada;
    }

    public void setHorarioPreAssinalada(boolean horarioPreAssinalada) {
        this.horarioPreAssinalada = horarioPreAssinalada;
    }

    public boolean isHorarioTrataComoDiaNormal() {
        return horarioTrataComoDiaNormal;
    }

    public void setHorarioTrataComoDiaNormal(boolean horarioTrataComoDiaNormal) {
        this.horarioTrataComoDiaNormal = horarioTrataComoDiaNormal;
    }

    public Date getHorarioEntrada1() {
        return horarioEntrada1;
    }

    public void setHorarioEntrada1(Date horarioEntrada1) {
        this.horarioEntrada1 = horarioEntrada1;
    }

    public Date getHorarioSaida1() {
        return horarioSaida1;
    }

    public void setHorarioSaida1(Date horarioSaida1) {
        this.horarioSaida1 = horarioSaida1;
    }

    public Date getHorarioEntrada2() {
        return horarioEntrada2;
    }

    public void setHorarioEntrada2(Date horarioEntrada2) {
        this.horarioEntrada2 = horarioEntrada2;
    }

    public Date getHorarioSaida2() {
        return horarioSaida2;
    }

    public void setHorarioSaida2(Date horarioSaida2) {
        this.horarioSaida2 = horarioSaida2;
    }

    public Date getLimiteCorteEntrada() {
        return limiteCorteEntrada;
    }

    public void setLimiteCorteEntrada(Date limiteCorteEntrada) {
        this.limiteCorteEntrada = limiteCorteEntrada;
    }

    public Date getLimiteCorteSaida() {
        return limiteCorteSaida;
    }

    public void setLimiteCorteSaida(Date limiteCorteSaida) {
        this.limiteCorteSaida = limiteCorteSaida;
    }

    public boolean isMarcacoesDiaSeguinteViradaDia() {
        return marcacoesDiaSeguinteViradaDia;
    }

    public void setMarcacoesDiaSeguinteViradaDia(boolean marcacoesDiaSeguinteViradaDia) {
        this.marcacoesDiaSeguinteViradaDia = marcacoesDiaSeguinteViradaDia;
    }

    public Date getFeriadoData() {
        return feriadoData;
    }

    public void setFeriadoData(Date feriadoData) {
        this.feriadoData = feriadoData;
    }

    public Date getAfastadoDataInicio() {
        return afastadoDataInicio;
    }

    public void setAfastadoDataInicio(Date afastadoDataInicio) {
        this.afastadoDataInicio = afastadoDataInicio;
    }

    public Date getAfastadoDataFim() {
        return afastadoDataFim;
    }

    public void setAfastadoDataFim(Date afastadoDataFim) {
        this.afastadoDataFim = afastadoDataFim;
    }

    public boolean isAfastadoComAbono() {
        return afastadoComAbono;
    }

    public void setAfastadoComAbono(boolean afastadoComAbono) {
        this.afastadoComAbono = afastadoComAbono;
    }

    public Date getCompensandoData() {
        return compensandoData;
    }

    public void setCompensandoData(Date compensandoData) {
        this.compensandoData = compensandoData;
    }

    public Date getCompensandoPeriodoInicio() {
        return compensandoPeriodoInicio;
    }

    public void setCompensandoPeriodoInicio(Date compensandoPeriodoInicio) {
        this.compensandoPeriodoInicio = compensandoPeriodoInicio;
    }

    public Date getCompensandoPeriodoTermino() {
        return compensandoPeriodoTermino;
    }

    public void setCompensandoPeriodoTermino(Date compensandoPeriodoTermino) {
        this.compensandoPeriodoTermino = compensandoPeriodoTermino;
    }

    public Date getCompensandoLimiteDiario() {
        return compensandoLimiteDiario;
    }

    public void setCompensandoLimiteDiario(Date compensandoLimiteDiario) {
        this.compensandoLimiteDiario = compensandoLimiteDiario;
    }

    public boolean isCompensandoConsideraDiasSemJornada() {
        return compensandoConsideraDiasSemJornada;
    }

    public void setCompensandoConsideraDiasSemJornada(boolean compensandoConsideraDiasSemJornada) {
        this.compensandoConsideraDiasSemJornada = compensandoConsideraDiasSemJornada;
    }

    public Date getBhData() {
        return bhData;
    }

    public void setBhData(Date bhData) {
        this.bhData = bhData;
    }

    public boolean isBhTipoLimiteDiario() {
        return bhTipoLimiteDiario;
    }

    public void setBhTipoLimiteDiario(boolean bhTipoLimiteDiario) {
        this.bhTipoLimiteDiario = bhTipoLimiteDiario;
    }

    public Date getBhLimiteDiarioTipoDia() {
        return bhLimiteDiarioTipoDia;
    }

    public void setBhLimiteDiarioTipoDia(Date bhLimiteDiarioTipoDia) {
        this.bhLimiteDiarioTipoDia = bhLimiteDiarioTipoDia;
    }

    public boolean isBhTrataFaltaComoDebito() {
        return bhTrataFaltaComoDebito;
    }

    public void setBhTrataFaltaComoDebito(boolean bhTrataFaltaComoDebito) {
        this.bhTrataFaltaComoDebito = bhTrataFaltaComoDebito;
    }

    public boolean isBhTrataAbonoComoDebito() {
        return bhTrataAbonoComoDebito;
    }

    public void setBhTrataAbonoComoDebito(boolean bhTrataAbonoComoDebito) {
        this.bhTrataAbonoComoDebito = bhTrataAbonoComoDebito;
    }

    public boolean isBhTrataAusenciaComoDebito() {
        return bhTrataAusenciaComoDebito;
    }

    public void setBhTrataAusenciaComoDebito(boolean bhTrataAusenciaComoDebito) {
        this.bhTrataAusenciaComoDebito = bhTrataAusenciaComoDebito;
    }

    public boolean isBhNaoPagaAdicionalNoturnoBH() {
        return bhNaoPagaAdicionalNoturnoBH;
    }

    public void setBhNaoPagaAdicionalNoturnoBH(boolean bhNaoPagaAdicionalNoturnoBH) {
        this.bhNaoPagaAdicionalNoturnoBH = bhNaoPagaAdicionalNoturnoBH;
    }

    public Date getBhGatilhoPositivo() {
        return bhGatilhoPositivo;
    }

    public void setBhGatilhoPositivo(Date bhGatilhoPositivo) {
        this.bhGatilhoPositivo = bhGatilhoPositivo;
    }

    public Date getBhGatilhoNegativo() {
        return bhGatilhoNegativo;
    }

    public void setBhGatilhoNegativo(Date bhGatilhoNegativo) {
        this.bhGatilhoNegativo = bhGatilhoNegativo;
    }

    public Date getAcertoData() {
        return acertoData;
    }

    public void setAcertoData(Date acertoData) {
        this.acertoData = acertoData;
    }

    public Date getEdicaoSaldoData() {
        return edicaoSaldoData;
    }

    public void setEdicaoSaldoData(Date edicaoSaldoData) {
        this.edicaoSaldoData = edicaoSaldoData;
    }

    public Date getSubtotalData() {
        return subtotalData;
    }

    public void setSubtotalData(Date subtotalData) {
        this.subtotalData = subtotalData;
    }

    public Date getDebitar() {
        return debitar;
    }

    public void setDebitar(Date debitar) {
        this.debitar = debitar;
    }

    public Date getCreditar() {
        return creditar;
    }

    public void setCreditar(Date creditar) {
        this.creditar = creditar;
    }

    public Date getAbonoData() {
        return abonoData;
    }

    public void setAbonoData(Date abonoData) {
        this.abonoData = abonoData;
    }

    public Date getAbonoDiurnasAbonadas() {
        return abonoDiurnasAbonadas;
    }

    public void setAbonoDiurnasAbonadas(Date abonoDiurnasAbonadas) {
        this.abonoDiurnasAbonadas = abonoDiurnasAbonadas;
    }

    public Date getAbonoNoturnasAbonadas() {
        return abonoNoturnasAbonadas;
    }

    public void setAbonoNoturnasAbonadas(Date abonoNoturnasAbonadas) {
        this.abonoNoturnasAbonadas = abonoNoturnasAbonadas;
    }

    public boolean isAbonoDescontarBH() {
        return abonoDescontarBH;
    }

    public void setAbonoDescontarBH(boolean abonoDescontarBH) {
        this.abonoDescontarBH = abonoDescontarBH;
    }

    public boolean isAbonoIncluirSomenteJustificativa() {
        return abonoIncluirSomenteJustificativa;
    }

    public void setAbonoIncluirSomenteJustificativa(boolean abonoIncluirSomenteJustificativa) {
        this.abonoIncluirSomenteJustificativa = abonoIncluirSomenteJustificativa;
    }

    public Date getDsrLimiteHorasFaltasSemanal() {
        return dsrLimiteHorasFaltasSemanal;
    }

    public void setDsrLimiteHorasFaltasSemanal(Date dsrLimiteHorasFaltasSemanal) {
        this.dsrLimiteHorasFaltasSemanal = dsrLimiteHorasFaltasSemanal;
    }

    public boolean isDsrIncluirHorasExtrasCalculo() {
        return dsrIncluirHorasExtrasCalculo;
    }

    public void setDsrIncluirHorasExtrasCalculo(boolean dsrIncluirHorasExtrasCalculo) {
        this.dsrIncluirHorasExtrasCalculo = dsrIncluirHorasExtrasCalculo;
    }

    public boolean isDsrIncluirFeriadosComoHoraNormal() {
        return dsrIncluirFeriadosComoHoraNormal;
    }

    public void setDsrIncluirFeriadosComoHoraNormal(boolean dsrIncluirFeriadosComoHoraNormal) {
        this.dsrIncluirFeriadosComoHoraNormal = dsrIncluirFeriadosComoHoraNormal;
    }

    public boolean isDsrDescontarAusencias() {
        return dsrDescontarAusencias;
    }

    public void setDsrDescontarAusencias(boolean dsrDescontarAusencias) {
        this.dsrDescontarAusencias = dsrDescontarAusencias;
    }

    public boolean isDsrIntegral() {
        return dsrIntegral;
    }

    public void setDsrIntegral(boolean dsrIntegral) {
        this.dsrIntegral = dsrIntegral;
    }

    public Date getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Date getDataNaoDescontarDSR() {
        return dataNaoDescontarDSR;
    }

    public void setDataNaoDescontarDSR(Date dataNaoDescontarDSR) {
        this.dataNaoDescontarDSR = dataNaoDescontarDSR;
    }

    public String getDiaNormalSequencia1horas() {
        return diaNormalSequencia1horas;
    }

    public void setDiaNormalSequencia1horas(String diaNormalSequencia1horas) {
        this.diaNormalSequencia1horas = diaNormalSequencia1horas;
    }

    public String getDiaNormalSequencia1percentual() {
        return diaNormalSequencia1percentual;
    }

    public void setDiaNormalSequencia1percentual(String diaNormalSequencia1percentual) {
        this.diaNormalSequencia1percentual = diaNormalSequencia1percentual;
    }

    public String getDiaNormalSequencia2horas() {
        return diaNormalSequencia2horas;
    }

    public void setDiaNormalSequencia2horas(String diaNormalSequencia2horas) {
        this.diaNormalSequencia2horas = diaNormalSequencia2horas;
    }

    public String getDiaNormalSequencia2percentual() {
        return diaNormalSequencia2percentual;
    }

    public void setDiaNormalSequencia2percentual(String diaNormalSequencia2percentual) {
        this.diaNormalSequencia2percentual = diaNormalSequencia2percentual;
    }

    public String getDiaNormalSequencia3horas() {
        return diaNormalSequencia3horas;
    }

    public void setDiaNormalSequencia3horas(String diaNormalSequencia3horas) {
        this.diaNormalSequencia3horas = diaNormalSequencia3horas;
    }

    public String getDiaNormalSequencia3percentual() {
        return diaNormalSequencia3percentual;
    }

    public void setDiaNormalSequencia3percentual(String diaNormalSequencia3percentual) {
        this.diaNormalSequencia3percentual = diaNormalSequencia3percentual;
    }

    public String getDiaNormalSequencia4horas() {
        return diaNormalSequencia4horas;
    }

    public void setDiaNormalSequencia4horas(String diaNormalSequencia4horas) {
        this.diaNormalSequencia4horas = diaNormalSequencia4horas;
    }

    public String getDiaNormalSequencia4percentual() {
        return diaNormalSequencia4percentual;
    }

    public void setDiaNormalSequencia4percentual(String diaNormalSequencia4percentual) {
        this.diaNormalSequencia4percentual = diaNormalSequencia4percentual;
    }

    public String getDiaCompensacaoSequencia1horas() {
        return diaCompensacaoSequencia1horas;
    }

    public void setDiaCompensacaoSequencia1horas(String diaCompensacaoSequencia1horas) {
        this.diaCompensacaoSequencia1horas = diaCompensacaoSequencia1horas;
    }

    public String getDiaCompensacaoSequencia1percentual() {
        return diaCompensacaoSequencia1percentual;
    }

    public void setDiaCompensacaoSequencia1percentual(String diaCompensacaoSequencia1percentual) {
        this.diaCompensacaoSequencia1percentual = diaCompensacaoSequencia1percentual;
    }

    public String getDiaCompensacaoSequencia2horas() {
        return diaCompensacaoSequencia2horas;
    }

    public void setDiaCompensacaoSequencia2horas(String diaCompensacaoSequencia2horas) {
        this.diaCompensacaoSequencia2horas = diaCompensacaoSequencia2horas;
    }

    public String getDiaCompensacaoSequencia2percentual() {
        return diaCompensacaoSequencia2percentual;
    }

    public void setDiaCompensacaoSequencia2percentual(String diaCompensacaoSequencia2percentual) {
        this.diaCompensacaoSequencia2percentual = diaCompensacaoSequencia2percentual;
    }

    public String getDiaCompensacaoSequencia3horas() {
        return diaCompensacaoSequencia3horas;
    }

    public void setDiaCompensacaoSequencia3horas(String diaCompensacaoSequencia3horas) {
        this.diaCompensacaoSequencia3horas = diaCompensacaoSequencia3horas;
    }

    public String getDiaCompensacaoSequencia3percentual() {
        return diaCompensacaoSequencia3percentual;
    }

    public void setDiaCompensacaoSequencia3percentual(String diaCompensacaoSequencia3percentual) {
        this.diaCompensacaoSequencia3percentual = diaCompensacaoSequencia3percentual;
    }

    public String getHorasNoturnasSequencia1horas() {
        return horasNoturnasSequencia1horas;
    }

    public void setHorasNoturnasSequencia1horas(String horasNoturnasSequencia1horas) {
        this.horasNoturnasSequencia1horas = horasNoturnasSequencia1horas;
    }

    public String getHorasNoturnasSequencia1percentual() {
        return horasNoturnasSequencia1percentual;
    }

    public void setHorasNoturnasSequencia1percentual(String horasNoturnasSequencia1percentual) {
        this.horasNoturnasSequencia1percentual = horasNoturnasSequencia1percentual;
    }

    public String getHorasNoturnasSequencia2horas() {
        return horasNoturnasSequencia2horas;
    }

    public void setHorasNoturnasSequencia2horas(String horasNoturnasSequencia2horas) {
        this.horasNoturnasSequencia2horas = horasNoturnasSequencia2horas;
    }

    public String getHorasNoturnasSequencia2percentual() {
        return horasNoturnasSequencia2percentual;
    }

    public void setHorasNoturnasSequencia2percentual(String horasNoturnasSequencia2percentual) {
        this.horasNoturnasSequencia2percentual = horasNoturnasSequencia2percentual;
    }

    public String getHorasNoturnasSequencia3horas() {
        return horasNoturnasSequencia3horas;
    }

    public void setHorasNoturnasSequencia3horas(String horasNoturnasSequencia3horas) {
        this.horasNoturnasSequencia3horas = horasNoturnasSequencia3horas;
    }

    public String getHorasNoturnasSequencia3percentual() {
        return horasNoturnasSequencia3percentual;
    }

    public void setHorasNoturnasSequencia3percentual(String horasNoturnasSequencia3percentual) {
        this.horasNoturnasSequencia3percentual = horasNoturnasSequencia3percentual;
    }

}
