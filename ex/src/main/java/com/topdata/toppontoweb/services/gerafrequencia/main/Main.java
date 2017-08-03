package com.topdata.toppontoweb.services.gerafrequencia.main;

import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.DadosEntrada;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasSaldoDia;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.CalculoPeriodoService;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.SaidaMainService;
import java.util.Date;

/**
 * Testes internos da API
 *
 * @author enio.junior
 */
public class Main {

    private static EntradaMain entradaMain;

    public static void main(String[] args) throws Exception {
        System.out.println("com.topdata.toppontoweb.services.api.Main.main()");
        setEntrada();
        Processar();
    }

    public static void inicializaEntradaMain() {
        entradaMain = new EntradaMain();
        ConfiguracoesGerais cg = new ConfiguracoesGerais(0, 3, 3, true);
        entradaMain.getEntrada().setConfiguracoesGerais(cg);
    }

    public static EntradaMain getEntradaMain() {
        return entradaMain;
    }

    public static void setEntrada() {
        inicializaEntradaMain();
        entradaMain.setDataInicioPeriodo("2016/11/15");
        entradaMain.setDataFimPeriodo("2016/11/30");

        DadosEntrada dadosEntrada = new DadosEntrada();

        dadosEntrada.setAdmissaoData(new Date("2016/11/17"));
        //dadosEntrada.setDemissaoData(new Date("2016/11/23"));

        dadosEntrada.setDataFechamento(new Date("2016/11/22"));

        //TABELA DE PERCENTUAIS (EXTRAS E BANCO DE HORAS)
        dadosEntrada.setDiaNormalSequencia1horas("01:00");
        dadosEntrada.setDiaNormalSequencia1percentual("10.0");
        dadosEntrada.setDiaNormalSequencia2horas("02:00");
        dadosEntrada.setDiaNormalSequencia2percentual("11.0");
        dadosEntrada.setDiaNormalSequencia3horas("01:00");
        dadosEntrada.setDiaNormalSequencia3percentual("12.0");
        dadosEntrada.setDiaNormalSequencia4horas("998:00");
        dadosEntrada.setDiaNormalSequencia4percentual("13.0");

        dadosEntrada.setDiaCompensacaoSequencia1horas("03:00");
        dadosEntrada.setDiaCompensacaoSequencia1percentual("70.0");
        dadosEntrada.setDiaCompensacaoSequencia2horas("05:00");
        dadosEntrada.setDiaCompensacaoSequencia2percentual("71.0");
        dadosEntrada.setDiaCompensacaoSequencia3horas("998:00");
        dadosEntrada.setDiaCompensacaoSequencia3percentual("72.0");

        dadosEntrada.setHorasNoturnasSequencia1horas("00:25");
        dadosEntrada.setHorasNoturnasSequencia1percentual("80.0");
        dadosEntrada.setHorasNoturnasSequencia2horas("00:35");
        dadosEntrada.setHorasNoturnasSequencia2percentual("81.0");
        dadosEntrada.setHorasNoturnasSequencia3horas("998:00");
        dadosEntrada.setHorasNoturnasSequencia3percentual("82.0");

        //JORNADA
        dadosEntrada.setJornadaDataInicio(new Date("2016/11/15"));
        dadosEntrada.setJornadaCompensaAtrasos(true);
        dadosEntrada.setJornadaTipoJornada(1); //1-Semanal  2-Variável  3-Livre
        dadosEntrada.setJornadaHorasDescontoSemanalDSR(new Date("2017/01/01 08:00"));
        dadosEntrada.setJornadaToleranciaExtras(new Date("2016/11/17 00:10"));
        dadosEntrada.setJornadaToleranciaAusencias(new Date("2016/11/17 00:10"));
        dadosEntrada.setJornadaToleranciaOcorrencias(new Date("2016/11/17 00:10"));
        //private String TerminoDiaAnterior;
        dadosEntrada.setJornadaInicioPeriodoNoturno(new Date("2016/11/17 22:00"));
        dadosEntrada.setJornadaTerminoPeriodoNoturno(new Date("2016/11/17 05:00"));
        dadosEntrada.setJornadaPercentualNoturno("14.28571");

        //HORÁRIO
        dadosEntrada.setHorarioTipoDia(1);  //1-Normal   6-Compensado   7-Horas Noturnas
        dadosEntrada.setHorarioPreAssinalada(true);
        dadosEntrada.setHorarioTrataComoDiaNormal(false);
        dadosEntrada.setHorarioEntrada1(new Date("2016/11/17 08:00"));
        dadosEntrada.setHorarioSaida1(new Date("2016/11/17 12:00"));
        dadosEntrada.setHorarioEntrada2(new Date("2016/11/17 13:00"));
        dadosEntrada.setHorarioSaida2(new Date("2016/11/17 01:00"));

        //CONFIGURAÇÕES GERAIS
        dadosEntrada.setLimiteCorteEntrada(new Date("2016/11/17 06:00"));
        dadosEntrada.setLimiteCorteSaida(new Date("2016/11/17 06:00"));
        dadosEntrada.setMarcacoesDiaSeguinteViradaDia(Boolean.TRUE);

        //CALENDÁRIO
        dadosEntrada.setFeriadoData(new Date("2016/11/22"));

        //AFASTAMENTO
        dadosEntrada.setAfastadoDataInicio(new Date("2016/11/27"));
        dadosEntrada.setAfastadoDataFim(new Date("2016/11/29"));
        dadosEntrada.setAfastadoComAbono(true);

        //COMPENSAÇÃO
        dadosEntrada.setCompensandoData(new Date("2016/11/24"));
        dadosEntrada.setCompensandoPeriodoInicio(new Date("2016/11/27"));
        dadosEntrada.setCompensandoPeriodoTermino(new Date("2016/11/30"));
        dadosEntrada.setCompensandoLimiteDiario(new Date("2016/11/17 04:00"));
        dadosEntrada.setCompensandoConsideraDiasSemJornada(false);

        //ABONO
        dadosEntrada.setAbonoData(new Date("2016/11/25"));
        dadosEntrada.setAbonoDescontarBH(true);
        dadosEntrada.setAbonoDiurnasAbonadas(new Date("2016/11/17 00:20"));
        dadosEntrada.setAbonoNoturnasAbonadas(new Date("2016/11/17 00:20"));
        dadosEntrada.setAbonoIncluirSomenteJustificativa(false);

        //BANCO DE HORAS
        dadosEntrada.setBhData(new Date("2015/03/27"));
        dadosEntrada.setBhTipoLimiteDiario(true);
        dadosEntrada.setBhLimiteDiarioTipoDia(new Date("2016/11/17 01:30"));
        dadosEntrada.setBhTrataAbonoComoDebito(true);
        dadosEntrada.setBhTrataAusenciaComoDebito(true);
        dadosEntrada.setBhTrataFaltaComoDebito(true);
        dadosEntrada.setBhNaoPagaAdicionalNoturnoBH(true);
        dadosEntrada.setBhGatilhoPositivo(new Date("2016/11/17 00:00"));
        dadosEntrada.setBhGatilhoNegativo(new Date("2016/11/17 02:00"));

        //FECHAMENTO DE BANCO DE HORAS
        dadosEntrada.setAcertoData(new Date("2016/11/23"));
        //dadosEntrada.setEdicaoSaldoData(new Date("2016/11/23));
        //dadosEntrada.setSubtotalData(new Date("2016/11/23"));
        dadosEntrada.setCreditar(new Date("2016/11/17 04:00"));
        dadosEntrada.setDebitar(new Date("2016/11/17 00:00"));

        //DSR
        dadosEntrada.setDsrLimiteHorasFaltasSemanal(new Date("2016/11/17 07:00"));
        dadosEntrada.setDsrIncluirHorasExtrasCalculo(true);
        dadosEntrada.setDsrIncluirFeriadosComoHoraNormal(true);
        dadosEntrada.setDsrDescontarAusencias(true);
        dadosEntrada.setDsrIntegral(true);

        dadosEntrada.setDataNaoDescontarDSR(new Date("2016/11/26"));

        entradaMain.setInicializa(dadosEntrada);
    }

    public static void Processar() {
        EntradaApi entradaApi = new EntradaApi(entradaMain.getEntrada().getDataInicioPeriodo(), entradaMain.getEntrada().getDataFimPeriodo(), entradaMain.getEntrada().getFuncionario(), entradaMain.getEntrada().getDsrList(), entradaMain.getEntrada().getConfiguracoesGerais());
        Calculo calculo = new Calculo(entradaApi, new BancodeHorasSaldoDia());
        entradaMain.getEntrada().setSaidaMain(new SaidaMainService(calculo));
        CalculoPeriodoService calculoPeriodoService = new CalculoPeriodoService(entradaApi);
        entradaMain.getEntrada().getSaidaMain().getSaidaResultado(entradaMain.getEntrada().getDataInicioPeriodo(), entradaMain.getEntrada().getDataFimPeriodo(), calculoPeriodoService.getSaidaAPI());
        entradaMain.getEntrada().getSaidaMain().getSaidaGeralResultado();
    }

    public static EntradaMain getResultado() {
        return entradaMain;
    }

}
