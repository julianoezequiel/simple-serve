package com.topdata.toppontoweb.services.gerafrequencia.services.regras;

import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.DsrApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author enio.junior
 */
public class DsrService {

    private final DsrApi dsrApi;
    private Dsr dsr;
    private List<Dsr> dsrList;
    private List<SaidaDia> saidaDiaList;
    private final FuncionarioService funcionarioService;
    private final Calculo calculo;

    public DsrService(Calculo calculo) {
        this.calculo = calculo;
        this.dsrApi = new DsrApi();
        this.funcionarioService = this.calculo.getFuncionarioService();
    }

    private List<Dsr> getDsrList() {
        return dsrList;
    }

    public void setDsrList(List<Dsr> dsrList) {
        this.dsrList = dsrList;
    }

    private List<SaidaDia> getSaidaDiaList() {
        return saidaDiaList;
    }

    private void setSaidaDiaList(List<SaidaDia> saidaDiaList) {
        this.saidaDiaList = saidaDiaList;
    }

    public void setCalculosDsr(List<SaidaDia> saidaDiaList) {
        dsr = getConsultaDSR(funcionarioService.getFuncionarioDSRService().getEmpresa());
        if (!Utils.getDataAjustaDiferenca3horas(dsr.getLimiteHorasFaltas()).equals(Utils.getDataDefault())) {
            setSaidaDiaList(saidaDiaList);
            setCalculoExtrasDSR();
            setCalculoFaltasDSR();
        }
    }

    public void setContadoresPeriodoDSR() {
        dsrApi.setDataInicio(this.calculo.getEntradaAPI().getDataInicioPeriodo());
        dsrApi.setDataFim(this.calculo.getEntradaAPI().getDataFimPeriodo());

        Calendar menorData = Utils.getMenorData(dsrApi.getDataInicio());
        Calendar maiorData = Utils.getMaiorData(dsrApi.getDataFim(), menorData);
        Date dia = menorData.getTime();
        int contadorDiasUteis = 0;
        int contadorDomingos = 0;
        while ((dia.before(maiorData.getTime())) || (dia.equals(maiorData.getTime()))) {
            Calendar c3 = Calendar.getInstance();
            c3.setTime(dia);
            if (c3.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && c3.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
                contadorDiasUteis++;
            }
            if (c3.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                contadorDomingos++;
                dsrApi.setUltimoDomingoMes(dia);
            }
            dia = Utils.AdicionaDiasData(dia, 1);
        }
        dsrApi.setDiasUteis(contadorDiasUteis);
        dsrApi.setDomingos(contadorDomingos);
        dsrApi.setFeriados((int) funcionarioService.getFuncionarioCalendarioService().getQuantidadeFeriadosPeriodo(dsrApi.getDataInicio(), dsrApi.getDataFim()));
    }

    public DsrApi getCalculosTabelaExtrasDSR(List<TabelaSequenciaPercentuais> tabelaExtras) {
        List<TabelaSequenciaPercentuais> tabelaList = new ArrayList<>();
        if (tabelaExtras != null && tabelaExtras.size() > 0) {
            tabelaExtras
                    .stream()
                    .sorted(new Utils.ordenaSequencia())
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        TabelaSequenciaPercentuais t = new TabelaSequenciaPercentuais();
                        t.setIdSequencia(tabela.getIdSequencia());
                        t.setDivisaoDiurnas(Utils.getCalculoDsr(tabela.getDivisaoDiurnas(), dsrApi.getDiasUteis(), dsrApi.getDomingos(), dsrApi.getFeriados()));
                        t.setDivisaoNoturnas(Utils.getCalculoDsr(tabela.getDivisaoNoturnas(), dsrApi.getDiasUteis(), dsrApi.getDomingos(), dsrApi.getFeriados()));
                        tabelaList.add(t);
                    });
            dsrApi.setTabelaExtrasDSRList(tabelaList);
        }
        return dsrApi;
    }

    private Dsr getConsultaDSR(int idEmpresa) {
        if (getDsrList() != null && getDsrList().size() > 0) {
            return getDsrList().stream()
                    .filter(f
                            -> (f.getEmpresa() != null
                            && f.getEmpresa().getIdEmpresa() == idEmpresa))
                    .findFirst().orElse(new Dsr());
        }
        return new Dsr();

        //Não teremos mais a configuração geral de DSR
        //Agora é obrigatório cadastrar por Empresa
        //if (cadastroEmpresaDSR == null) {
        //return getDsrList().stream()
        //        .filter(f -> (f.getEmpresa() == null))
        //        .findFirst().orElse(null);
    }

    private Duration getConsultaSomaExtras() {
        dsrApi.setSomaExtras(Duration.ZERO);
        if (getSaidaDiaList() != null && getSaidaDiaList().size() > 0) {
            getSaidaDiaList()
                    .stream()
                    .forEach((SaidaDia saidaDia) -> {
                        if (saidaDia.getSaldoExtras() != null) {
                            dsrApi.setSomaExtras(dsrApi.getSomaExtras().plus(saidaDia.getSaldoExtras().getDiurnas().plus(saidaDia.getSaldoExtras().getNoturnas())));
                        }
                    });
        }
        return dsrApi.getSomaExtras();
    }

    /**
     * Geral do período Só vai ter hora extra nesse cálculo dos dias que não
     * possuem fechamento de horas Verificar se flag Incluir horas extras no
     * cálculo está como sim SE (Feriado="Não" OU (Feriado="Sim" && Incluir
     * feriados como hora normal="Sim"))
     */
    private void setCalculoExtrasDSR() {
        if (dsr.getIncluiHoraExtra()
                && (!this.calculo.getRegrasService().isFeriado()
                || (this.calculo.getRegrasService().isFeriado()
                && dsr.getIncluiFeriadoComoHoraNormal()))) {

            //SOMAR AS HORAS EXTRAS DO PERÍODO
            //SEMPRE CONSIDERAR considerarMesCheio (FLAG DESATIVADO)	
            //CÁLCULO = (SALDO FINAL EXTRAS/ DIAS UTEIS) * (DOMINGOS + FERIADOS)
            dsrApi.setExtrasDSR(Duration.ofMillis(new BigDecimal(getConsultaSomaExtras().toMillis())
                    .divide(new BigDecimal(dsrApi.getDiasUteis() == 0 ? 1 : dsrApi.getDiasUteis()), 3, RoundingMode.HALF_EVEN)
                    .multiply(new BigDecimal(dsrApi.getDomingos() + dsrApi.getFeriados()))
                    .longValue()));

            //extrasDSR = Duration.ofMillis(new BigDecimal(extrasDSR.toMillis()).multiply(new BigDecimal(dsr.getDomingos()+dsr.getFeriados())).longValue());
        }

    }

    public Duration getCalculoExtrasDSR() {
        return dsrApi.getExtrasDSR();
    }

    private Duration getConsultaSomaFaltas() {
        dsrApi.setSomaFaltas(Duration.ZERO);
        dsrApi.setFaltasDSR(Duration.ZERO);
        dsrApi.setSaldoMesSeguinte(Duration.ZERO);
        if (getSaidaDiaList() != null && getSaidaDiaList().size() > 0) {
            getSaidaDiaList()
                    .stream()
                    .forEach((SaidaDia saidaDia) -> {
                        if (saidaDia.getSaldoAusencias() != null) {
                            if (saidaDia.getRegras().isFalta()) {

                                //Horas de falta semanal        
                                setHorasSemanal(saidaDia);

                                //Se é após o último domingo do mês fica pro mês seguinte
                                setMesSeguinte(saidaDia);

                                //Se é primeiro dia
                                setPrimeiroDia(saidaDia);

                                dsrApi.setSomaFaltas(dsrApi.getSomaFaltas().plus(saidaDia.getSaldoAusencias().getDiurnas().plus(saidaDia.getSaldoAusencias().getNoturnas())));

                                setProcessaRegraFaltas(saidaDia);
                            }
                        }
                    });
        }
        return dsrApi.getSomaFaltas();
    }

    private Duration getConsultaSomaAusencias() {
        dsrApi.setSomaAusencias(Duration.ZERO);
        if (getSaidaDiaList() != null && getSaidaDiaList().size() > 0) {
            getSaidaDiaList()
                    .stream()
                    .forEach((SaidaDia saidaDia) -> {
                        if (saidaDia.getSaldoAusencias() != null) {
                            if (!saidaDia.getRegras().isFalta()) {

                                //Horas de ausências semanal        
                                setHorasSemanal(saidaDia);

                                //Se é após o último domingo do mês fica pro mês seguinte
                                setMesSeguinte(saidaDia);

                                //Se é primeiro dia
                                setPrimeiroDia(saidaDia);

                                dsrApi.setSomaAusencias(dsrApi.getSomaAusencias().plus(saidaDia.getSaldoAusencias().getDiurnas().plus(saidaDia.getSaldoAusencias().getNoturnas())));

                                setProcessaRegraAusencias(saidaDia);
                            }
                        }
                    });
        }

        return dsrApi.getSomaAusencias();
    }

    private void setHorasSemanal(SaidaDia saidaDia) {
        Calendar c4 = Calendar.getInstance();
        c4.setTime(saidaDia.getData());
        if (c4.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            //Por semana essa soma de faltas ou ausências
            //Quando chegar na segunda zera as horas de ausência da semana e começa novamente
            if (saidaDia.getRegras().isFalta()) {
                dsrApi.setSomaFaltas(Duration.ZERO);
            } else {
                dsrApi.setSomaAusencias(Duration.ZERO);
            }
        }
    }

    /**
     * Se é após o último domingo do mês fica pro mês seguinte
     *
     * @param saidaDia
     */
    private void setMesSeguinte(SaidaDia saidaDia) {
        if (saidaDia.getData() != null && dsrApi != null && dsrApi.getUltimoDomingoMes() != null && saidaDia.getData().after(dsrApi.getUltimoDomingoMes())) {
            dsrApi.setDescontoProximoMes(true);
            dsrApi.setSaldoMesSeguinte(dsrApi.getSaldoMesSeguinte().plus(saidaDia.getSaldoAusencias().getDiurnas().plus(saidaDia.getSaldoAusencias().getNoturnas())));
        } else {
            dsrApi.setDescontoProximoMes(false);
        }
    }

    /**
     * Se é dia 1º então soma saldo horas de ausência zera saldo mes seguinte e
     * deixa como false desconto próximo mês
     *
     * @param saidaDia
     */
    private void setPrimeiroDia(SaidaDia saidaDia) {
        if (saidaDia.getData().getDate() == 1) {
            dsrApi.setSomaFaltas(dsrApi.getSomaFaltas().plus(dsrApi.getSaldoMesSeguinte()));
            dsrApi.setSaldoMesSeguinte(Duration.ZERO);
            dsrApi.setDescontoProximoMes(false);
        }
    }

    private void setProcessaRegraFaltas(SaidaDia saidaDia) {
        Jornada jornadaFuncionario = funcionarioService.getFuncionarioJornadaService().getJornada();
        if (!funcionarioService.getFuncionarioDSRService().getNaoDescontaDSRdia(saidaDia.getData())
                && !dsrApi.isDescontoProximoMes()
                && dsrApi.getSomaFaltas().toMillis() > Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(dsr.getLimiteHorasFaltas()).toInstant()).toMillis()) {

            if (jornadaFuncionario != null
                    && Utils.getDataAjustaDiferenca3horas(jornadaFuncionario.getDescontaHorasDSR()) != null) {
                if (dsrApi.getFaltasDSR() == null) {
                    dsrApi.setFaltasDSR(Duration.ZERO);
                }
                dsrApi.setFaltasDSR(dsrApi.getFaltasDSR().plus(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(jornadaFuncionario.getDescontaHorasDSR()).toInstant())));
            }
        }
    }

    private void setProcessaRegraAusencias(SaidaDia saidaDia) {
        Jornada jornadaFuncionario = funcionarioService.getFuncionarioJornadaService().getJornada();

        //Horas parciais de ausência
        if (!dsr.getIntegral()
                && !funcionarioService.getFuncionarioDSRService().getNaoDescontaDSRdia(saidaDia.getData())
                && !dsrApi.isDescontoProximoMes()
                && dsrApi.getSomaAusencias().toMillis() > 0) {
            dsrApi.setFaltasDSR(dsrApi.getFaltasDSR().plus(dsrApi.getSomaAusencias()));
        }

        //Horas integrais de ausência
        if (dsr.getIntegral()
                && !funcionarioService.getFuncionarioDSRService().getNaoDescontaDSRdia(saidaDia.getData())
                && !dsrApi.isDescontoProximoMes()
                && dsrApi.getSomaAusencias().toMillis() > 0
                && jornadaFuncionario != null
                && jornadaFuncionario.getDescontaHorasDSR() != null) {
            dsrApi.setFaltasDSR(dsrApi.getFaltasDSR().plus(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(jornadaFuncionario.getDescontaHorasDSR()).toInstant())));
        }
    }

    /**
     * Geral do período
     */
    private void setCalculoFaltasDSR() {
        if (getConsultaSomaFaltas() == Duration.ZERO) {
            getConsultaSomaAusencias();
        }
    }

    public Duration getCalculoFaltasDSR() {
        return dsrApi.getFaltasDSR();
    }

}
