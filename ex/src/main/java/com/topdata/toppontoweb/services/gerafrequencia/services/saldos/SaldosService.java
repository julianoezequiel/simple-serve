package com.topdata.toppontoweb.services.gerafrequencia.services.saldos;

import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.OcorrenciasService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author enio.junior
 */
public class SaldosService {

    private SaldoTrabalhadas saldoTrabalhadas;
    private Duration horasNoturnas;
    private Duration horasNoturnasComAdicional;
    private final FuncionarioJornadaService funcionarioJornadaService;
    private List<MarcacoesDia> funcionarioMarcacoesDiaList;
    private final Calculo calculo;

    public SaldosService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioJornadaService = this.calculo.getFuncionarioService().getFuncionarioJornadaService();
    }

    public Saldo getTotalHorasPrevistas() {
        Saldo saldo = new Saldo();
        if (this.funcionarioJornadaService.getHorariosJornadaDia() != null) {
            this.funcionarioJornadaService.getHorariosJornadaDia()
                    .forEach((HorarioMarcacao horarioMarcacao) -> {
                        getCalculaIntervaloHoras(saldo,
                                horarioMarcacao.getHorarioEntrada(), 
                                horarioMarcacao.getHorarioSaida());
                    });
        }
        return saldo;
    }

//    public Saldo getTotalHorasPrevistasCompensacao(Date dia) {
//        //Se o dia a ser compensado possui realmente jornada a ser cumprida
//        //Já coloca com ausência a jornada prevista
//        if ((!this.calculo.getRegrasService().isFechamento())
//                && (this.calculo.getRegrasService().isFuncionarioAtivo(dia))
//                && (this.calculo.getRegrasService().isCumprirHorario())) {
//            return getTotalHorasPrevistas();
//        }
//        return null;       
//    }
    public SaldoTrabalhadas getTotalHorasTrabalhadas() {
        this.funcionarioMarcacoesDiaList = this.calculo.getFuncionarioMarcacoesService().getMarcacoesJornadaDia(this.calculo.getDiaProcessado());
        saldoTrabalhadas = new SaldoTrabalhadas();
        if (this.calculo.getRegrasService().isJornada()) {
            if (this.calculo.getRegrasService().isCompensaAtrasos()) {
                if (funcionarioMarcacoesDiaList != null) {
                    funcionarioMarcacoesDiaList
                            .forEach((MarcacoesDia marcacoesDia) -> {
                                getCalculaIntervaloHoras(saldoTrabalhadas, marcacoesDia.getHorarioEntrada(), marcacoesDia.getHorarioSaida());
                            });
                }
            } else {
                saldoTrabalhadas = setNaoCompensaAtrasos(saldoTrabalhadas);
            }
        } else //Não possui Jornada, mas se tiver marcações
        {
            if (funcionarioMarcacoesDiaList != null) {
                funcionarioMarcacoesDiaList.forEach((MarcacoesDia marcacoesDia) -> {
                    getCalculaIntervaloHoras(saldoTrabalhadas, marcacoesDia.getHorarioEntrada(), marcacoesDia.getHorarioSaida());
                });
            }
        }

        return saldoTrabalhadas;
    }

    private SaldoTrabalhadas setNaoCompensaAtrasos(SaldoTrabalhadas saldoTrabalhadas) {
        Saldo saldoExtras = new Saldo();
        Saldo saldoAusencias = new Saldo();
        AtomicInteger sequencia = new AtomicInteger(0);
        if (funcionarioMarcacoesDiaList != null) {
            funcionarioMarcacoesDiaList.forEach((MarcacoesDia marcacoesDia) -> {
                //Entrada marcação x entrada jornada
                getCalculaIntervaloHoras(CONSTANTES.ENTRADA, saldoTrabalhadas, saldoExtras, saldoAusencias,
                        this.funcionarioJornadaService.getHorariosJornadaDia().get(sequencia.get()).getHorarioEntrada(), marcacoesDia.getHorarioEntrada());

                //Saída marcação x saída jornada
                getCalculaIntervaloHoras(CONSTANTES.SAIDA, saldoTrabalhadas, saldoExtras, saldoAusencias,
                        this.funcionarioJornadaService.getHorariosJornadaDia().get(sequencia.get()).getHorarioSaida(), marcacoesDia.getHorarioSaida());

                sequencia.getAndIncrement();
            });
        }
        return saldoTrabalhadas;
    }

    private void getCalculaIntervaloHoras(Saldo saldo, Date horarioEntrada, Date horarioSaida) {
        if (saldo != null && horarioEntrada != null && horarioSaida != null
                && this.funcionarioJornadaService.getJornada().getIdJornada() != null) {
            saldo.setDiurnas(saldo.getDiurnas().plus(Utils.getIntervaloDuration(horarioEntrada, horarioSaida)));
            //horasNoturnasDiaAnterior = getCalculoNoturnoDiaAnterior(horarioEntrada, horarioSaida, horasNoturnas, horasNoturnasComAdicional);
            setAtualizaSaldos(saldo, getCalculoNoturno(horarioEntrada, horarioSaida));
        } else if (this.funcionarioJornadaService.getJornada().getIdJornada() == null) {
            saldo.setPossui(true);
            saldo.setDiurnas(saldo.getDiurnas().plus(Utils.getIntervaloDuration(horarioEntrada, horarioSaida)));
        }
    }

    private void getCalculaIntervaloHoras(String marcacao, SaldoTrabalhadas saldoTrabalhadas,
            Saldo saldoExtras, Saldo saldoAusencias, Date marcacao1, Date marcacao2) {
        if (saldoTrabalhadas != null && marcacao1 != null && marcacao2 != null
                && this.funcionarioJornadaService.getJornada().getIdJornada() != null) {

            saldoTrabalhadas.setPossui(true);
            saldoTrabalhadas.setDiurnas(Utils.getIntervaloDuration(marcacao1, marcacao2));
            //horasNoturnas = getCalculoNoturnoDiaAnterior(marcacao1, marcacao2, horasNoturnas, horasNoturnasComAdicional);
            horasNoturnas = getCalculoNoturno(marcacao1, marcacao2);

            if (marcacao.equals(CONSTANTES.ENTRADA)) {
                setHorasMarcacoesEntrada(marcacao1, marcacao2, saldoTrabalhadas, saldoExtras, saldoAusencias);
            }
            if (marcacao.equals(CONSTANTES.SAIDA)) {
                setHorasMarcacoesSaida(marcacao1, marcacao2, saldoTrabalhadas, saldoExtras, saldoAusencias);
            }

        } else if (this.funcionarioJornadaService.getJornada().getIdJornada() == null) {
            saldoTrabalhadas.setPossui(true);
            saldoTrabalhadas.setDiurnas(saldoTrabalhadas.getDiurnas().plus(Utils.getIntervaloDuration(marcacao1, marcacao2)));
        }
    }

    private Duration getCalculoNoturno(Date marcacao1, Date marcacao2) {
        //Possui período noturno (adicional) - exemplo das 22h as 5h              
        //Cálculo noturno dia processamento
        horasNoturnas = Duration.ZERO;
        horasNoturnasComAdicional = Duration.ZERO;
        if (this.calculo.getRegrasService().isAdicionalNoturno()) {
            
            Date inicioAdicionalNoturno = Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getInicioAdicionalNoturno());
            Date terminoAdicionalNoturno = Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getTerminoAdicionalNoturno());
            
            if (terminoAdicionalNoturno != null) {
                if (terminoAdicionalNoturno.getTime() < inicioAdicionalNoturno.getTime()) {
                    terminoAdicionalNoturno = Utils.getViradaDia(1, terminoAdicionalNoturno);
                }
            }
            
            //Existe a possibilidade de ter somente o início do período noturno
            if (terminoAdicionalNoturno == null) {
                if (marcacao2.getTime() > inicioAdicionalNoturno.getTime()) {
                    if (marcacao1.getTime() < inicioAdicionalNoturno.getTime()) {
                        horasNoturnas = Utils.getIntervaloDuration(inicioAdicionalNoturno, marcacao2);
                    } else {
                        horasNoturnas = Utils.getIntervaloDuration(marcacao1, marcacao2);
                    }
                }
            } else //Se entrada dentro do período noturno
            {
                if (marcacao1.getTime() > inicioAdicionalNoturno.getTime()
                        && marcacao1.getTime() < terminoAdicionalNoturno.getTime()) {
                    if (marcacao2.getTime() > terminoAdicionalNoturno.getTime()) {
                        horasNoturnas = Utils.getIntervaloDuration(marcacao1, terminoAdicionalNoturno);
                    } else {
                        horasNoturnas = Utils.getIntervaloDuration(marcacao1, marcacao2);
                    }
                } else //Se saída dentro do período noturno
                {
                    if (marcacao2.getTime() > inicioAdicionalNoturno.getTime()
                            && marcacao2.getTime() < terminoAdicionalNoturno.getTime()) {

                        if (marcacao1.getTime() < inicioAdicionalNoturno.getTime()) {
                            horasNoturnas = Utils.getIntervaloDuration(inicioAdicionalNoturno, marcacao2);
                        } else {
                            horasNoturnas = Utils.getIntervaloDuration(marcacao1, marcacao2);
                        }
                    } else //Se entrada e saída fora do período noturno
                    {
                        if (marcacao1.getTime() < inicioAdicionalNoturno.getTime()
                                && marcacao2.getTime() > terminoAdicionalNoturno.getTime()) {
                            horasNoturnas = Utils.getIntervaloDuration(inicioAdicionalNoturno, terminoAdicionalNoturno);
                        }
                    }
                }
            }
        }
        return horasNoturnas;
    }

    private void setHorasMarcacoesEntrada(Date marcacao1, Date marcacao2, SaldoTrabalhadas saldoTrabalhadas, Saldo saldoExtras, Saldo saldoAusencias) {
        OcorrenciasService ocorrenciasService = new OcorrenciasService();
        if ((marcacao1.getTime() - marcacao2.getTime()) > 0) {
            if (ocorrenciasService.Antecipada(marcacao1, marcacao2, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaExtra()))) {
                //extras = true;  //ocorrencia.setEntradaAntecipada(true);
                setAtualizaSaldosExtras(saldoTrabalhadas, saldoExtras, horasNoturnas);
                saldoTrabalhadas.setExtras(saldoExtras);
            }
        } else if (ocorrenciasService.AposHorario(marcacao1, marcacao2, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaAusencia()))) {
            //ausencias = true;  //ocorrencia.setEntradaAtrasada(true);
            setAtualizaSaldosAusencias(saldoTrabalhadas, saldoAusencias, horasNoturnas);
            saldoTrabalhadas.setAusencias(saldoAusencias);
        }
    }

    private void setHorasMarcacoesSaida(Date marcacao1, Date marcacao2, SaldoTrabalhadas saldoTrabalhadas, Saldo saldoExtras, Saldo saldoAusencias) {
        OcorrenciasService ocorrenciasService = new OcorrenciasService();
        if ((marcacao1.getTime() - marcacao2.getTime()) > 0) {
            if (ocorrenciasService.Antecipada(marcacao1, marcacao2, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaAusencia()))) {
                //ausencias = true;  //ocorrencia.setSaidaAntecipada(true);
                setAtualizaSaldosAusencias(saldoTrabalhadas, saldoAusencias, horasNoturnas);
                saldoTrabalhadas.setAusencias(saldoAusencias);
            }
        } else if (ocorrenciasService.AposHorario(marcacao1, marcacao2, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaExtra()))) {
            //extras = true;   //ocorrencia.setSaidaAposHorario(true);
            setAtualizaSaldosExtras(saldoTrabalhadas, saldoExtras, horasNoturnas);
            saldoTrabalhadas.setExtras(saldoExtras);
        }
    }

    private void setAtualizaSaldos(Saldo saldo, Duration horasNoturnas) {
        saldo.setPossui(true);
        if (horasNoturnas != null) {
            //Subtrair diurnas
            saldo.setDiurnas(saldo.getDiurnas().minus(horasNoturnas));

            //Pegar as horas referente a esse período e aplicar o percentual
            horasNoturnasComAdicional = Utils.getAcrescimoPercentualAdicionalNoturno(horasNoturnas, this.funcionarioJornadaService.getJornada().getPercentualAdicionalNoturno());
            saldo.setDiferencaadicionalnoturno(saldo.getDiferencaadicionalnoturno().plus(horasNoturnasComAdicional.minus(horasNoturnas)));

            //Somar noturnas
            saldo.setNoturnas(saldo.getNoturnas().plus(horasNoturnasComAdicional));
        }
    }

    private void setAtualizaSaldosExtras(Saldo saldo, Saldo saldoExtras, Duration horasNoturnas) {
        saldoExtras.setPossui(true);
        if (horasNoturnas != null) {
            saldoExtras.setDiurnas(saldoExtras.getDiurnas().plus(saldo.getDiurnas().minus(horasNoturnas)));

            //Pegar as horas referente a esse período e aplicar o percentual
            horasNoturnasComAdicional = Utils.getAcrescimoPercentualAdicionalNoturno(horasNoturnas, this.funcionarioJornadaService.getJornada().getPercentualAdicionalNoturno());
            saldoExtras.setDiferencaadicionalnoturno(saldoExtras.getDiferencaadicionalnoturno().plus(horasNoturnasComAdicional.minus(horasNoturnas)));

            //Somar noturnas
            saldoExtras.setNoturnas(saldoExtras.getNoturnas().plus(horasNoturnasComAdicional));
        } else {
            saldoExtras.setDiurnas(saldoExtras.getDiurnas().plus(saldo.getDiurnas()));
        }

    }

    private void setAtualizaSaldosAusencias(Saldo saldo, Saldo saldoAusencias, Duration horasNoturnas) {
        saldoAusencias.setPossui(true);
        //Subtrair diurnas
        if (horasNoturnas != null) {
            saldoAusencias.setDiurnas(saldoAusencias.getDiurnas().plus(saldo.getDiurnas().minus(horasNoturnas)));

            //Pegar as horas referente a esse período e aplicar o percentual
            horasNoturnasComAdicional = Utils.getAcrescimoPercentualAdicionalNoturno(horasNoturnas,
                    this.funcionarioJornadaService.getJornada().getPercentualAdicionalNoturno());
            saldoAusencias.setDiferencaadicionalnoturno(saldoAusencias.getDiferencaadicionalnoturno()
                    .plus(horasNoturnasComAdicional.minus(horasNoturnas)));

            //Somar noturnas
            saldoAusencias.setNoturnas(saldoAusencias.getNoturnas().plus(horasNoturnasComAdicional));
        } else {
            saldoAusencias.setDiurnas(saldoAusencias.getDiurnas().plus(saldo.getDiurnas()));
        }
    }

    public void setSaldos(SaidaDia saidaDia) {
        if (!this.calculo.getRegrasService().isFalta()) {
            saidaDia.setSaldoExtras(getCalculaSaldoExtras(saidaDia.getHorasTrabalhadas()));
            saidaDia.setTabelaExtrasList(this.funcionarioJornadaService.getTabelaExtrasList(saidaDia.getSaldoExtras()));
        } else {
            saidaDia.setSaldoExtras(new SaldoExtras());
            saidaDia.setTabelaExtrasList(new ArrayList<>());
        }
        saidaDia.setSaldoAusencias(getCalculaSaldoAusencias(saidaDia.getHorasTrabalhadas()));
        saidaDia.setSaldoNormais(getCalculaSaldoNormais(saidaDia.getHorasTrabalhadas(), saidaDia.getSaldoExtras(), saidaDia.getSaldoAusencias()));
    }

    public SaldoExtras getCalculaSaldoExtras(SaldoTrabalhadas horasTrabalhadas) {
        return new ExtrasService(getTotalHorasPrevistas(),
                horasTrabalhadas != null ? horasTrabalhadas : new SaldoTrabalhadas(), this.calculo).getSaldoExtras();
    }

    public Saldo getCalculaSaldoNormais(SaldoTrabalhadas horasTrabalhadas, SaldoExtras saldoExtras, SaldoAusencias saldoAusencias) {
        return new NormaisService(getTotalHorasPrevistas(),
                horasTrabalhadas != null ? horasTrabalhadas : new SaldoTrabalhadas(), saldoExtras, saldoAusencias, this.calculo).getSaldoNormais();
    }

    public SaldoAusencias getCalculaSaldoAusencias(SaldoTrabalhadas horasTrabalhadas) {
        return new AusenciasService(getTotalHorasPrevistas(),
                horasTrabalhadas != null ? horasTrabalhadas : new SaldoTrabalhadas(), this.calculo).getSaldoAusencias();
    }

    public void setArredondamentosHorasNoturnas(SaidaDia saidaDia) {
        if (saidaDia.getHorasTrabalhadas() != null) {
            saidaDia.getHorasTrabalhadas().setDiferencaadicionalnoturno(
                    Utils.getArredondamento(saidaDia.getHorasTrabalhadas().getDiferencaadicionalnoturno()));
        }
        if (saidaDia.getSaldoExtras() != null) {
            //saidaDia.getSaldoExtras().setDiurnas(Utils.getArredondamento(saidaDia.getSaldoExtras().getDiurnas()));
            saidaDia.getSaldoExtras().setNoturnas(Utils.getArredondamento(saidaDia.getSaldoExtras().getNoturnas()));
        }
        if (saidaDia.getSaldoAusencias() != null) {
            //saidaDia.getSaldoAusencias().setDiurnas(Utils.getArredondamento(saidaDia.getSaldoAusencias().getDiurnas()));
            saidaDia.getSaldoAusencias().setNoturnas(Utils.getArredondamento(saidaDia.getSaldoAusencias().getNoturnas()));
        }
        if (saidaDia.getSaldoNormais() != null) {
            //saidaDia.getSaldoNormais().setDiurnas(Utils.getArredondamento(saidaDia.getSaldoNormais().getDiurnas()));
            saidaDia.getSaldoNormais().setNoturnas(Utils.getArredondamento(saidaDia.getSaldoNormais().getNoturnas()));
        }
        if (saidaDia.getTabelaExtrasList() != null) {
            saidaDia.getTabelaExtrasList()
                    .stream()
                    .sorted(new Utils.ordenaSequencia())
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        tabela.setDivisaoNoturnas(Utils.getArredondamento(tabela.getDivisaoNoturnas()));
                    });
        }
    }

    //private Duration getCalculoNoturnoDiaAnterior(Date marcacao1, Date marcacao2, Duration horasNoturnas, Duration horasNoturnasComAdicional) {
    //Possui período noturno (adicional) - exemplo das 22h as 5h  
    //Cálculo noturno dia anterior
//            if (funcionarioService.getJornadaDiaAnterior().getPercentualAdicionalNoturno() != null) {
//                if (funcionarioService.getJornadaDiaAnterior().getTerminoAdicionalNoturno().getDate() < funcionarioService.getJornadaDiaAnterior().getInicioAdicionalNoturno().getDate()) {
//
//                    //Se a entrada inicia antes da data de término do adicional noturno dia anterior
//                    if (horarioEntrada.getTime() < funcionarioService.getJornadaDiaAnterior().getTerminoAdicionalNoturno().getTime()) {
//
//                        //Separar as horas noturnas
//                        if (horarioSaida.getTime() > funcionarioService.getJornadaDiaAnterior().getTerminoAdicionalNoturno().getTime()) {
//                            horasNoturnas = Utils.getIntervaloDuration(horarioEntrada, funcionarioService.getJornadaDiaAnterior().getTerminoAdicionalNoturno());
//                        } else {
//                            horasNoturnas = Utils.getIntervaloDuration(horarioEntrada), horarioSaida));
//                        }
//
//                        //Subtrair diurnas
//                        saldo.setDiurnas(saldo.getDiurnas().minus(horasNoturnas));
//
//                        //Pegar as horas referente a esse período e aplicar o percentual
//                        horasNoturnasComAdicional = getCalculaAdicionalNoturno(horasNoturnas, funcionarioService.getJornadaDiaAnterior().getPercentualAdicionalNoturno());
//                        saldo.setDiferencaadicionalnoturno(horasNoturnasComAdicional.minus(horasNoturnas));
//
//                        //Somar noturnas
//                        saldo.setNoturnas(saldo.getNoturnas().plus(horasNoturnasComAdicional));
//                    }
//                }
//            }
    //}
}
