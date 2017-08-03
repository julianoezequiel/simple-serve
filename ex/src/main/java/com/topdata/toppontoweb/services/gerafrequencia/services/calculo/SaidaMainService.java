package com.topdata.toppontoweb.services.gerafrequencia.services.calculo;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesTratadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensando;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 * Somente a função Main utiliza (interno)
 *
 * @author enio.junior
 */
public class SaidaMainService implements Serializable {

    private final Calculo calculo;
    private Duration totalHorasDeveriaTrabalharPeriodo;
    private Duration totalHorasNaoTrabalhadasPeriodo;

    public SaidaMainService(Calculo calculo) {
        this.calculo = calculo;
    }

    private List<SaidaDia> saidaDiaList = new ArrayList<>();

    public List<SaidaDia> getSaidaDiaList() {
        return saidaDiaList;
    }

    public void getSaidaResultado(Date inicioPeriodo, Date fimPeriodo, List<SaidaDia> saidaDiaList) {
        this.saidaDiaList = saidaDiaList;
        totalHorasDeveriaTrabalharPeriodo = Duration.ZERO;
        totalHorasNaoTrabalhadasPeriodo = Duration.ZERO;
        System.out.println("");
        System.out.println("Período de " + Utils.FormatoData(inicioPeriodo) + " a " + Utils.FormatoData(fimPeriodo));
        if (saidaDiaList != null) {
            saidaDiaList.forEach(new visualizarSaidaDia());
        }
    }

    private class visualizarSaidaDia implements Consumer<SaidaDia> {

        public visualizarSaidaDia() {
        }

        @Override
        public void accept(SaidaDia saidaDia) {
            System.out.println("");
            System.out.println("*******************************************************************************************************************************************");
            System.out.println("Data: " + Utils.FormatoData(saidaDia.getData()) + "      Dia Processado: " + saidaDia.getRegras().isDiaProcessado() + ((!saidaDia.getRegras().isDiaProcessado()) ? " (Não admitido)" : ""));
            if (saidaDia.getRegras().isDiaProcessado()) {
                setSaidaJornada(saidaDia);
                setSaidaMarcacoesOriginais(saidaDia);
                setSaidaMarcacoesRelatorioPresenca(saidaDia);
                setSaidaMarcacoesComTratamento(saidaDia);
                setSaidaTrabalhado(saidaDia);
                setSaidaRegras(saidaDia);
                setSaidaHorasPrevistas(saidaDia);
                setSaidaHorasTrabalhadas(saidaDia);
                setSaidaSaldoNormais(saidaDia);
                setSaidaSaldoExtras(saidaDia);
                if (saidaDia.getRegras().isTrabalhado() || saidaDia.getRegras().isFalta()) {
                    setSaidaSaldoAusencias(saidaDia);
                    setSaidaPossui(saidaDia);
                }
                setSaidaTabelaExtras(saidaDia);
                setSaidaTabelaExtrasDSR(saidaDia);
                setSaidaBancodeHoras(saidaDia);
                setSaidaCalculosComplementares(saidaDia);
                totalHorasDeveriaTrabalharPeriodo = totalHorasDeveriaTrabalharPeriodo.plus(saidaDia.getIntervalos().getSaldoHorasDeveriaTrabalharAbsenteismo());
                totalHorasNaoTrabalhadasPeriodo = totalHorasNaoTrabalhadasPeriodo.plus(saidaDia.getIntervalos().getSaldoHorasNaoTrabalhadasAbsenteismo());
            }
        }
    }

    private void setSaidaJornada(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("Fechamento: " + saidaDia.getRegras().isFechamento() + "  Inconsistência: " + saidaDia.getRegras().isInconsistencia() + "  Feriado: " + saidaDia.getRegras().isFeriado());
        System.out.println("Jornada: " + saidaDia.getRegras().isJornada() + " (CH: " + saidaDia.getIdHorario() + " / Cumprir Horário: " + saidaDia.getRegras().isCumprirHorario() + " / Virada de dia: " + saidaDia.getRegras().isViradaDiaJornada() + " / Período Noturno: " + saidaDia.getRegras().isAdicionalNoturno() + ")");
        System.out.println("Legenda: " + saidaDia.getRegras().getLegendaStatusEspelho());
        if (saidaDia.getRegras().isCumprirHorario()) {
            System.out.println("  Sequência Entrada    Saída");
            saidaDia.getHorariosPrevistosList()
                    .forEach((HorarioMarcacao horarioMarcacao) -> {
                        System.out.println("  " + horarioMarcacao.getIdSequencia()
                                + "         "
                                + Utils.FormatoHoraMain(horarioMarcacao.getHorarioEntrada())
                                + "   " + Utils.FormatoHoraMain(horarioMarcacao.getHorarioSaida()));
                    });
        }
    }

    private void setSaidaMarcacoesOriginais(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("Marcações registradas no ponto eletrônico:");
        System.out.println("  Sequência Entrada    Saída    MotivoEntrada   MotivoSaída");
        saidaDia.getHorariosTrabalhadosOriginaisEquipamentoList()
                .forEach((MarcacoesDia marcacoesDia) -> {
                    System.out.println("  " + marcacoesDia.getIdSequencia()
                            + "         "
                            + Utils.FormatoHoraMain(marcacoesDia.getHorarioEntrada())
                            + "   "
                            + Utils.FormatoHoraMain(marcacoesDia.getHorarioSaida())
                            + "   "
                            + (marcacoesDia.getMotivoStatusEntrada() != null ? marcacoesDia.getMotivoStatusEntrada() : "")
                            + "   "
                            + (marcacoesDia.getMotivoStatusSaida() != null ? marcacoesDia.getMotivoStatusSaida() : ""));
                });
    }

    private void setSaidaMarcacoesRelatorioPresenca(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("Marcações relatório de Presença:");
        System.out.println("Status:" + saidaDia.getRegras().getLegendaStatusPresenca());
        System.out.println("  Sequência Entrada    Saída    StatusEntrada   StatusSaída");
        saidaDia.getHorariosTrabalhadosPresencaList()
                .forEach((MarcacoesDia marcacoesDia) -> {
                    System.out.println("  " + marcacoesDia.getIdSequencia()
                            + "         "
                            + Utils.FormatoHoraMain(marcacoesDia.getHorarioEntrada())
                            + "   "
                            + Utils.FormatoHoraMain(marcacoesDia.getHorarioSaida())
                            + "   "
                            + (marcacoesDia.getStatusEntrada() != null ? marcacoesDia.getStatusEntrada() : "")
                            + "   "
                            + (marcacoesDia.getStatusSaida() != null ? marcacoesDia.getStatusSaida() : ""));
                });
    }

    private void setSaidaMarcacoesComTratamento(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("Marcações com tratamento");
        System.out.println("  Horário    Ocorrência     Motivo");
        saidaDia.getHorariosTrabalhadosTratadosList()
                .forEach((MarcacoesTratadas marcacoesTratadas) -> {
                    System.out.println("  " + Utils.FormatoHoraMain(marcacoesTratadas.getHorarioMarcacao())
                            + "   "
                            + marcacoesTratadas.getIdLegenda()
                            + "   "
                            + (marcacoesTratadas.getDescricaoMotivo() != null ? marcacoesTratadas.getDescricaoMotivo() : ""));
                });
    }

    private void setSaidaTrabalhado(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("Trabalhado: " + saidaDia.getRegras().isTrabalhado() + " (Virada de dia: " + saidaDia.getRegras().isViradaDiaTrabalhado() + ")");
        if (saidaDia.getRegras().isTrabalhado()) {
            System.out.println("  Sequência Entrada    Saída    MotivoEntrada   MotivoSaída");
            saidaDia.getHorariosTrabalhadosList()
                    .forEach((MarcacoesDia marcacoesDia) -> {
                        System.out.println("  " + marcacoesDia.getIdSequencia()
                                + "         "
                                + Utils.FormatoHoraMain(marcacoesDia.getHorarioEntrada())
                                + "   "
                                + Utils.FormatoHoraMain(marcacoesDia.getHorarioSaida())
                                + "   "
                                + (marcacoesDia.getMotivoStatusEntrada() != null ? marcacoesDia.getMotivoStatusEntrada() : "")
                                + "   "
                                + (marcacoesDia.getMotivoStatusSaida() != null ? marcacoesDia.getMotivoStatusSaida() : ""));
                    });

            if (saidaDia.getRegras().isCumprirHorario()) {
                System.out.println("");
                System.out.println("Ocorrências: (Entrada antecipada: " + saidaDia.getOcorrencias().isEntradaAntecipada() + "   Entrada atrasada: " + saidaDia.getOcorrencias().isEntradaAtrasada() + "   Saída antecipada: " + saidaDia.getOcorrencias().isSaidaAntecipada() + "   Saída após horário: " + saidaDia.getOcorrencias().isSaidaAposHorario() + ")");
            }
        }
    }

    private void setSaidaRegras(SaidaDia saidaDia) {
        System.out.println(" ");
        System.out.println("CompensaAtrasos: " + saidaDia.getRegras().isCompensaAtrasos() + "   Falta: " + saidaDia.getRegras().isFalta() + "   Faltando marcações: " + saidaDia.getRegras().isFaltandoMarcacoes() + "   Intervalo deslocado: " + saidaDia.getRegras().isIntervaloDeslocado() + "   Excedeu intervalo: " + saidaDia.getRegras().isExcedeuIntervalo() + "   Marcações ímpares: " + saidaDia.getRegras().isMarcacoesImpares()); // + "   Tipo de dia: " + resultadoDia.getRegras().getIdTipoDia());

        if (saidaDia.getRegras().isAbonoSomenteJustificativa()) {
            System.out.println("Abono somente justificativa: " + saidaDia.getRegras().isAbonoSomenteJustificativa() + " - " + saidaDia.getRegras().getJustificativaAbono());
        }
        System.out.println("Trabalhou afastado: " + saidaDia.getRegras().isTrabalhouAfastado() + "   Afastado sem abono: " + saidaDia.getRegras().isAfastadoSemAbono() + "   Compensando dia: " + saidaDia.getRegras().isCompensandoDia() + "   Compensação inválida: " + saidaDia.getRegras().isCompensacaoInvalida());

        if (saidaDia.getRegras().isDiaCompensadoSomenteJustificativa()) {
            System.out.println("Dia compensado somente justificativa: " + saidaDia.getRegras().isDiaCompensadoSomenteJustificativa() + " - " + saidaDia.getRegras().getJustificativaDiaCompensado());
        }

        if (saidaDia.getRegras().isDiaCompensadoSemSomenteJustificativa()) {
            System.out.println("Dia compensado: " + saidaDia.getRegras().isDiaCompensadoSemSomenteJustificativa());
        }
    }

    private void setSaidaHorasPrevistas(SaidaDia saidaDia) {
        System.out.println("");
        System.out.println("                   Diurnas    Noturnas    Total       Diferença Adic. noturno");
        if (saidaDia.getRegras().isCumprirHorario()) {
            System.out.println("Horas previstas    " + Utils.FormatoHora(saidaDia.getHorasPrevistas().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getHorasPrevistas().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getHorasPrevistas().getDiurnas().plus(saidaDia.getHorasPrevistas().getNoturnas()))
                    + "    " + Utils.FormatoHora(saidaDia.getHorasPrevistas().getDiferencaadicionalnoturno()));
        }
    }

    private void setSaidaHorasTrabalhadas(SaidaDia saidaDia) {
        if (saidaDia.getRegras().isTrabalhado()) {
            System.out.println("Horas trabalhadas  " + Utils.FormatoHora(saidaDia.getHorasTrabalhadas().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getHorasTrabalhadas().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getHorasTrabalhadas().getDiurnas().plus(saidaDia.getHorasTrabalhadas().getNoturnas()))
                    + "    " + Utils.FormatoHora(saidaDia.getHorasTrabalhadas().getDiferencaadicionalnoturno()));
        }
    }

    private void setSaidaSaldoNormais(SaidaDia saidaDia) {
        if (saidaDia.getSaldoNormais().isPossui()) {
            System.out.println("Saldo normais      " + Utils.FormatoHora(saidaDia.getSaldoNormais().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getSaldoNormais().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getSaldoNormais().getDiurnas().plus(saidaDia.getSaldoNormais().getNoturnas())));
        }
    }

    private void setSaidaSaldoExtras(SaidaDia saidaDia) {
        if (saidaDia.getRegras().isTrabalhado()) {
            if (saidaDia.getSaldoExtras().isPossui()) {
                System.out.println("Saldo extras       " + Utils.FormatoHora(saidaDia.getSaldoExtras().getDiurnas())
                        + "   " + Utils.FormatoHora(saidaDia.getSaldoExtras().getNoturnas())
                        + "     " + Utils.FormatoHora(saidaDia.getSaldoExtras().getDiurnas().plus(saidaDia.getSaldoExtras().getNoturnas()))
                        + "   (Percentual Noturno: " + saidaDia.getPercentualAdicionalNoturno() + ")"
                        + "   (Diferença Adicional Noturno Extras: " + Utils.FormatoHora(Utils.getArredondamento(Utils.getVisualizaDiferencaAdicionalNoturnoHoraExtraMain(saidaDia.getRegras().isBancoDeHoras(), saidaDia.getHorasTrabalhadas().getDiferencaadicionalnoturno()))) + ")");
            }
            if (Utils.getSaldoDiaCompensando(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensandoList()) != null) {
                System.out.println("Horas extras compensação  " + Utils.FormatoHora(Utils.getSaldoDiaCompensando(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoDiurnas())
                        + "   " + Utils.FormatoHora(Utils.getSaldoDiaCompensando(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoNoturnas())
                        + "     " + Utils.FormatoHora(Utils.getSaldoDiaCompensando(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoDiurnas().plus(Utils.getSaldoDiaCompensando(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoNoturnas())));
            }
        }
    }

    private void setSaidaSaldoAusencias(SaidaDia saidaDia) {
        if (saidaDia.getSaldoAusencias().isPossui()) {
            System.out.println("Saldo ausências    " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getDiurnas().plus(saidaDia.getSaldoAusencias().getNoturnas())));
        }
        if (Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()) != null
                && (Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas().isZero() == false
                || Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas().isZero() == false)) {
            System.out.println("Horas ausências compensadas  " + Utils.FormatoHora(Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas())
                    + "   " + Utils.FormatoHora(Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas())
                    + "     " + Utils.FormatoHora(Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas().plus(Utils.getSaldoDiaCompensado(saidaDia.getData(), calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas())));
        }
        if (saidaDia.getRegras().isAbonoSemSomenteJustificativa() && saidaDia.getSaldoAusencias().getAbonoDia() != null) {
            System.out.println("Horas ausências abonadas     " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getDiurnas().plus(saidaDia.getSaldoAusencias().getAbonoDia().getNoturnas())));
        } else if (saidaDia.getRegras().isAfastadoComAbono() && saidaDia.getSaldoAusencias().getAbonoDia() != null) {
            System.out.println("Horas ausências Afastado com abono " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getDiurnas())
                    + "   " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getNoturnas())
                    + "     " + Utils.FormatoHora(saidaDia.getSaldoAusencias().getAbonoDia().getDiurnas().plus(saidaDia.getSaldoAusencias().getAbonoDia().getNoturnas())));
        }
    }

    private void setSaidaPossui(SaidaDia saidaDia) {
        System.out.println(" ");
        if (saidaDia.getRegras().isTrabalhado()) {
            System.out.println("Possui normais: " + saidaDia.getSaldoNormais().isPossui());
            System.out.println("Possui extras (Acima Tolerância): " + saidaDia.getSaldoExtras().isPossui() + " (Todas: " + saidaDia.getSaldoExtras().isTodas() + ")");
        }
        System.out.println("Possui ausências (Acima Tolerância ou falta): " + saidaDia.getSaldoAusencias().isPossui());
    }

    private void setSaidaTabelaExtras(SaidaDia saidaDia) {
        System.out.println(" ");
        System.out.println("Tabela de Extras:");
        System.out.println("  Limite Horas Percentual Diurnas Noturnas");
        if (saidaDia.getTabelaExtrasList() != null) {
            saidaDia.getTabelaExtrasList().forEach((TabelaSequenciaPercentuais tabela) -> {
                System.out.println("  " + tabela.getIdSequencia() + "      " + Utils.FormatoHora(Utils.getHorasAcima24horas(tabela.getHoras().toInstant()))
                        + "  " + tabela.getAcrescimo() + "         " + Utils.FormatoHora(tabela.getDivisaoDiurnas())
                        + "    " + Utils.FormatoHora(tabela.getDivisaoNoturnas()));
            });
        }
    }

    private void setSaidaTabelaExtrasDSR(SaidaDia saidaDia) {
        System.out.println(" ");
        System.out.println("Tabela de DSR Extras:");
        System.out.println("  Limite Diurnas Noturnas");
        if (saidaDia.getDsr() != null && saidaDia.getDsr().getTabelaExtrasDSRList() != null) {
            saidaDia.getDsr().getTabelaExtrasDSRList().forEach((TabelaSequenciaPercentuais tabela) -> {
                System.out.println("  " + tabela.getIdSequencia()
                        + "    " + Utils.FormatoHora(tabela.getDivisaoDiurnas())
                        + "    " + Utils.FormatoHora(tabela.getDivisaoNoturnas()));
            });
        }
    }

    private void setSaidaBancodeHoras(SaidaDia saidaDia) {
        if (saidaDia.getRegras().isBancoDeHoras()) {
            System.out.println(" ");
            System.out.println("Possui Banco de Horas: " + saidaDia.getRegras().isBancoDeHoras());
            System.out.println("Não paga adicional noturno B.H.: " + saidaDia.getRegras().isNaoPagaAdicionalBancoDeHoras());
            if (saidaDia.getRegras().isTrabalhado()) {
                System.out.println("Percentual Noturno: " + saidaDia.getPercentualAdicionalNoturno() + "   (Diferença Adicional Noturno B.H.: " + Utils.FormatoHora(Utils.getArredondamento(Utils.getVisualizaDiferencaAdicionalNoturnoBancoDeHoras(saidaDia.getRegras().isBancoDeHoras(), saidaDia.getHorasTrabalhadas().getDiferencaadicionalnoturno()))) + ")");
            } else {
                System.out.println("Percentual Noturno: " + saidaDia.getPercentualAdicionalNoturno() + "   (Diferença Adicional Noturno B.H.: )");
            }
            System.out.println(" ");
            System.out.println("Tabela de Acréscimos Tipo de dia: " + saidaDia.getIdTipoDia());
            System.out.println("                                   [Diurnas]          [Noturnas]");
            System.out.println("  Limite Horas    Percentual  Divisão Resultado Divisão  Resultado");
            Duration totalDivisaoDiurnas = Duration.ZERO;
            Duration totalResultadoDiurnas = Duration.ZERO;
            Duration totalDivisaoNoturnas = Duration.ZERO;
            Duration totalResultadoNoturnas = Duration.ZERO;
            if (saidaDia.getBancodeHoras() != null && saidaDia.getBancodeHoras().getAcrescimosTipoDiaList() != null) {
                saidaDia.getBancodeHoras().getAcrescimosTipoDiaList()
                        .forEach(new TotaisTipoDia(totalDivisaoDiurnas, totalResultadoDiurnas, totalDivisaoNoturnas, totalResultadoNoturnas));

                System.out.println(" ");
                System.out.println("  Total                        "
                        + Utils.FormatoHora(totalDivisaoDiurnas)
                        + "  "
                        + Utils.FormatoHora(totalResultadoDiurnas)
                        + "   "
                        + Utils.FormatoHora(totalDivisaoNoturnas)
                        + "  "
                        + Utils.FormatoHora(totalResultadoNoturnas));
            }
            System.out.println(" ");
            System.out.println("Tabela de Somente Noturnas:");
            System.out.println("  Limite Horas    Percentual  Divisão Resultado");
            if (saidaDia.getBancodeHoras() != null && saidaDia.getBancodeHoras().getAcrescimosSomenteNoturnasList() != null) {
                totalDivisaoNoturnas = Duration.ZERO;
                totalResultadoNoturnas = Duration.ZERO;
                saidaDia.getBancodeHoras().getAcrescimosSomenteNoturnasList()
                        .forEach(new TotaisSomenteNoturnas(totalDivisaoNoturnas, totalResultadoNoturnas));

                System.out.println(" ");
                System.out.println("  Total                        "
                        + Utils.FormatoHora(totalDivisaoNoturnas)
                        + " "
                        + Utils.FormatoHora(totalResultadoNoturnas));
            }

            System.out.println(" ");
            if (saidaDia.getBancodeHoras() != null) {
                System.out.println("Fechamento");
                System.out.println("  Crédito: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getCreditoFechamento()));
                System.out.println("  Débito: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getDebitoFechamento()));
                System.out.println("Acertos");
                System.out.println("  Crédito Funcionário: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getDebitoFechamento()));
                System.out.println("  Débito Funcionário: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getCreditoFechamento()));
                System.out.println(" ");
                System.out.println("Saldo Acumulado dia anterior: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getSaldoAcumuladoDiaAnterior()));
                System.out.println("Crédito do dia: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getCredito()));
                System.out.println("Débito do dia: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getDebito()));
                System.out.println("Saldo Dia: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getSaldoDia()));
                System.out.println("Saldo Crédito: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getSaldoAcumuladoCredito()));
                System.out.println("Saldo Débito: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getSaldoAcumuladoDebito()));
                System.out.println("Gatilho: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getGatilho()));
                System.out.println("Saldo Acumulado dia: " + Utils.FormatoHora(saidaDia.getBancodeHoras().getSaldoAcumuladoDia()));
            }
        }
    }

    private static class TotaisTipoDia implements Consumer<TabelaSequenciaPercentuais> {

        private Duration totalDivisaoDiurnas;
        private Duration totalResultadoDiurnas;
        private Duration totalDivisaoNoturnas;
        private Duration totalResultadoNoturnas;

        public TotaisTipoDia(Duration totalDivisaoDiurnas, Duration totalResultadoDiurnas, Duration totalDivisaoNoturnas, Duration totalResultadoNoturnas) {
            this.totalDivisaoDiurnas = totalDivisaoDiurnas;
            this.totalResultadoDiurnas = totalResultadoDiurnas;
            this.totalDivisaoNoturnas = totalDivisaoNoturnas;
            this.totalResultadoNoturnas = totalResultadoNoturnas;
        }

        @Override
        public void accept(TabelaSequenciaPercentuais tabela) {
            System.out.println("  " + tabela.getIdSequencia()
                    + "      " + Utils.FormatoHora(Utils.getHorasAcima24horas(tabela.getHoras().toInstant()))
                    + " " + tabela.getAcrescimo()
                    + "%        "
                    + Utils.FormatoHora(tabela.getDivisaoDiurnas())
                    + "  " + Utils.FormatoHora(tabela.getResultadoDiurnas())
                    + "   "
                    + Utils.FormatoHora(tabela.getDivisaoNoturnas())
                    + "  " + Utils.FormatoHora(tabela.getResultadoNoturnas()));

            totalDivisaoDiurnas = totalDivisaoDiurnas.plus(tabela.getDivisaoDiurnas());
            totalResultadoDiurnas = totalResultadoDiurnas.plus(tabela.getResultadoDiurnas());

            totalDivisaoNoturnas = totalDivisaoNoturnas.plus(tabela.getDivisaoNoturnas());
            totalResultadoNoturnas = totalResultadoNoturnas.plus(tabela.getResultadoNoturnas());
        }
    }
    
     private static class TotaisSomenteNoturnas implements Consumer<TabelaSequenciaPercentuais> {

        private Duration totalDivisaoNoturnas;
        private Duration totalResultadoNoturnas;

        public TotaisSomenteNoturnas(Duration totalDivisaoNoturnas, Duration totalResultadoNoturnas) {
            this.totalDivisaoNoturnas = totalDivisaoNoturnas;
            this.totalResultadoNoturnas = totalResultadoNoturnas;
        }

        @Override
        public void accept(TabelaSequenciaPercentuais tabela) {
            System.out.println("  " + tabela.getIdSequencia()
                    + "      " + Utils.FormatoHora(Utils.getHorasAcima24horas(tabela.getHoras().toInstant()))
                    + " " + tabela.getAcrescimo()
                    + "%        "
                    + Utils.FormatoHora(tabela.getDivisaoNoturnas())
                    + " " + Utils.FormatoHora(tabela.getResultadoNoturnas()));
            
            totalDivisaoNoturnas = totalDivisaoNoturnas.plus(tabela.getDivisaoNoturnas());
            totalResultadoNoturnas = totalResultadoNoturnas.plus(tabela.getResultadoNoturnas());
        }
    }

    private void setSaidaCalculosComplementares(SaidaDia saidaDia) {
        System.out.println(" ");
        if (saidaDia.getIntervalos() != null) {
            System.out.println("Cálculos complementares");
            System.out.println("  IntraJornada");
            System.out.println("    Intervalos");
            saidaDia.getIntervalos().getSaldoHorasIntraJornadaList().forEach((Duration d) -> {
                System.out.println("    " + d);
            });
            System.out.println("  InterJornada: " + saidaDia.getIntervalos().getSaldoHorasInterJornada());
            System.out.println("  Índice de Absenteísmo: " + Utils.FormatoPercentualMain(saidaDia.getIntervalos().getIndiceAbsenteismo()));
        }
    }

    public void getSaidaGeralResultado() {
        setSaidaDsr();
        setSaidaListaCompensacaoAusencias();
        setSaidaListaCompensacaoExtras();
        setSaidaIndiceAbsenteismoPeriodo();
    }

    private void setSaidaDsr() {
        System.out.println("");
        System.out.println("*******************************************************************************************************************************************");
        System.out.println("DSR:");
        System.out.println("   Extras: " + Utils.FormatoHora(this.calculo.getDsrService().getCalculoExtrasDSR()));
        System.out.println("   Faltas: " + Utils.FormatoHora(this.calculo.getDsrService().getCalculoFaltasDSR()));
    }

    private void setSaidaListaCompensacaoAusencias() {
        System.out.println("*******************************************************************************************************************************************");
        System.out.println("Lista de compensação Ausências");
        if (this.calculo.getEntradaAPI().getSaldoDiaCompensadoList() != null) {
            this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()
                    .forEach((SaldoDiaCompensado saldoDiaCompensado) -> {
                        System.out.println("*****************************************");
                        System.out.println("  Dia Compensado:       " + Utils.FormatoData(saldoDiaCompensado.getDataCompensada()));
                        System.out.println("  Status:               " + saldoDiaCompensado.getStatus());
                        System.out.println("  Início:               " + Utils.FormatoData(saldoDiaCompensado.getDataInicio()));
                        System.out.println("  Fim:                  " + Utils.FormatoData(saldoDiaCompensado.getDataFim()));
                        System.out.println("  Ausências Diurnas:    " + Utils.FormatoHora(saldoDiaCompensado.getDiurnas()));
                        System.out.println("            Noturnas:   " + Utils.FormatoHora(saldoDiaCompensado.getNoturnas()));
                        System.out.println("  Compensadas Diurnas:  " + Utils.FormatoHora(saldoDiaCompensado.getCompensadasDiurnas()));
                        System.out.println("              Noturnas: " + Utils.FormatoHora(saldoDiaCompensado.getCompensadasNoturnas()));
                        System.out.println("  A compensar Diurnas:  " + Utils.FormatoHora(saldoDiaCompensado.getACompensarDiurnas()));
                        System.out.println("              Noturnas: " + Utils.FormatoHora(saldoDiaCompensado.getACompensarNoturnas()));
                    });
        }
    }

    private void setSaidaListaCompensacaoExtras() {
        System.out.println("*******************************************************************************************************************************************");
        System.out.println("Lista de compensação Extras");
        if (this.calculo.getEntradaAPI().getSaldoDiaCompensandoList() != null) {
            this.calculo.getEntradaAPI().getSaldoDiaCompensandoList().forEach((SaldoDiaCompensando saldoDiaCompensando) -> {
                System.out.println("*****************************************");
                System.out.println("  Dia Compensando:      " + Utils.FormatoData(saldoDiaCompensando.getDataCompensando()));
                System.out.println("  Compensadas Diurnas:  " + Utils.FormatoHora(saldoDiaCompensando.getCompensandoDiurnas()));
                System.out.println("              Noturnas: " + Utils.FormatoHora(saldoDiaCompensando.getCompensandoNoturnas()));
            });
        }
        System.out.println(" ");
    }

    private void setSaidaIndiceAbsenteismoPeriodo() {
        System.out.println("*******************************************************************************************************************************************");
        System.out.println(" Índice de Absenteísmo do Período: "
                + Utils.FormatoPercentualMain(Utils.getPercentualAbsenteismo(
                        totalHorasDeveriaTrabalharPeriodo, totalHorasNaoTrabalhadasPeriodo)));
        System.out.println("*******************************************************************************************************************************************");
    }

}
