package com.topdata.toppontoweb.services.gerafrequencia.main;

import com.topdata.toppontoweb.services.gerafrequencia.main.Main;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.DadosEntrada;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import java.util.Date;

/**
 *
 * @author enio.junior
 */
public class DadosCenario01 {

    public static void setEntrada() {
        
        String dia = "2017/09/21";
        String viradaDia = "2017/09/22";
        String dataAdmissao = "2017/01/01";
        
        Main.inicializaEntradaMain();
        Main.getEntradaMain().setDataInicioPeriodo(dia);
        Main.getEntradaMain().setDataFimPeriodo(dia);
        
        DadosEntrada dadosEntrada = new DadosEntrada();
        
        dadosEntrada.setAdmissaoData(new Date(dataAdmissao));
        //dadosEntrada.setDemissaoData(new Date("2017/12/01"));        
        //dadosEntrada.setDataFechamento(new Date("2017/12/01"));
        
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
        dadosEntrada.setJornadaDataInicio(new Date("2017/09/21"));
        dadosEntrada.setJornadaCompensaAtrasos(true);
        dadosEntrada.setJornadaTipoJornada(1); //1-Semanal  2-Variável  3-Livre
        dadosEntrada.setJornadaHorasDescontoSemanalDSR(new Date(dia + " 08:00"));
        dadosEntrada.setJornadaToleranciaExtras(new Date(dia + " 00:10"));
        dadosEntrada.setJornadaToleranciaAusencias(new Date(dia + " 00:10"));
        dadosEntrada.setJornadaToleranciaOcorrencias(new Date(dia + " 00:10"));
        //private String TerminoDiaAnterior;
        dadosEntrada.setJornadaInicioPeriodoNoturno(new Date(dia + " 22:00"));
        dadosEntrada.setJornadaTerminoPeriodoNoturno(new Date(viradaDia + " 05:00"));
        dadosEntrada.setJornadaPercentualNoturno("14.28571");
        
        //HORÁRIO
        dadosEntrada.setHorarioTipoDia(1);  //1-Normal   6-Compensado   7-Horas Noturnas
        dadosEntrada.setHorarioPreAssinalada(true);
        dadosEntrada.setHorarioTrataComoDiaNormal(false);
        dadosEntrada.setHorarioEntrada1(new Date(dia + " 08:00"));
        dadosEntrada.setHorarioSaida1(new Date(dia + " 12:00"));
        dadosEntrada.setHorarioEntrada2(new Date(dia + " 13:00"));
        dadosEntrada.setHorarioSaida2(new Date(viradaDia + " 22:00"));
        
        dadosEntrada.setLimiteCorteEntrada(new Date(dia + " 06:00"));
        dadosEntrada.setLimiteCorteSaida(new Date(dia + " 06:00"));
        dadosEntrada.setMarcacoesDiaSeguinteViradaDia(Boolean.TRUE);
        
        //CALENDÁRIO
        //dadosEntrada.setFeriadoData(new Date("2016/11/22"));
                
        //AFASTAMENTO
        //dadosEntrada.setAfastadoDataInicio(new Date("2016/11/27"));
        //dadosEntrada.setAfastadoDataFim(new Date("2016/11/29"));
        //dadosEntrada.setAfastadoComAbono(true);
        
        //COMPENSAÇÃO
        //dadosEntrada.setCompensandoData(new Date("2016/11/24"));
        //dadosEntrada.setCompensandoPeriodoInicio(new Date("2016/11/27"));
        //dadosEntrada.setCompensandoPeriodoTermino(new Date("2016/11/30"));
        //dadosEntrada.setCompensandoLimiteDiario(new Date("2016/11/17 04:00"));
        //dadosEntrada.setCompensandoConsideraDiasSemJornada(false);
            
        //ABONO
        //dadosEntrada.setAbonoData(new Date("2016/11/25"));
        //dadosEntrada.setAbonoDescontarBH(true);
        //dadosEntrada.setAbonoDiurnasAbonadas(new Date("2016/11/17 00:20"));
        //dadosEntrada.setAbonoNoturnasAbonadas(new Date("2016/11/17 00:20"));
        //dadosEntrada.setAbonoIncluirSomenteJustificativa(false);
        
        //BANCO DE HORAS
        dadosEntrada.setBhData(new Date(dia));        
        dadosEntrada.setBhTipoLimiteDiario(true);
        dadosEntrada.setBhLimiteDiarioTipoDia(new Date(dia + " 01:00")); 
        dadosEntrada.setBhTrataAbonoComoDebito(true);
        dadosEntrada.setBhTrataAusenciaComoDebito(true);
        dadosEntrada.setBhTrataFaltaComoDebito(true);
        dadosEntrada.setBhNaoPagaAdicionalNoturnoBH(true);
        dadosEntrada.setBhGatilhoPositivo(new Date(dia + " 00:00"));
        dadosEntrada.setBhGatilhoNegativo(new Date(dia + " 00:00"));
        
        //FECHAMENTO DE BANCO DE HORAS
        dadosEntrada.setAcertoData(new Date(dia));
        //dadosEntrada.setEdicaoSaldoData(new Date("2016/11/23));
        //dadosEntrada.setSubtotalData(new Date("2016/11/23"));
        dadosEntrada.setCreditar(new Date(dia + " 01:00"));
        dadosEntrada.setDebitar(new Date(dia + " 02:00"));
                
        //DSR
        dadosEntrada.setDsrLimiteHorasFaltasSemanal(new Date(dia + " 07:00"));
        dadosEntrada.setDsrIncluirHorasExtrasCalculo(true);
        dadosEntrada.setDsrIncluirFeriadosComoHoraNormal(false);
        dadosEntrada.setDsrDescontarAusencias(true);
        dadosEntrada.setDsrIntegral(false); 
        
        //dadosEntrada.setDataNaoDescontarDSR(new Date(dia));
        
        Main.getEntradaMain().setInicializa(dadosEntrada);

    }

    //Resultados esperados
    public static String getSaldoNormaisDiurnas() {
        return "13:00:00";
    }
    public static String getSaldoNormaisNoturnas() {
        return CONSTANTES.ZERO_HORA;
    }    
    public static String getSaldoExtrasDiurnas() {
        return CONSTANTES.ZERO_HORA;
    }
    public static String getSaldoExtrasNoturnas() {
        return "04:09:00";
    }
    public static String getSaldoAusenciasDiurnas() {
        return CONSTANTES.ZERO_HORA;
    }
    public static String getSaldoAusenciasNoturnas() {
        return CONSTANTES.ZERO_HORA;
    }
    public static String getSaldoDiaBH() {
        return "01:59:00";
    }

}
