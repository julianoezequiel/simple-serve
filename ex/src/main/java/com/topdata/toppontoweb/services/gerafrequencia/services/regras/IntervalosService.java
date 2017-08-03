package com.topdata.toppontoweb.services.gerafrequencia.services.regras;

import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Realiza os cálculos para os relatórios de absenteísmo, intrajornada e
 * interjornada
 *
 * @author enio.junior
 */
public class IntervalosService {

    private Intervalos intervalos;
    private Date ultimaMarcacaoDiaAnterior; //Esse é acumulado para o cálculo
    private Date ultimaSaida;

    public IntervalosService() {
    }

    public Intervalos getIntervalos() {
        return intervalos;
    }

    public void inicializaIntervalos() {
        intervalos = new Intervalos();
        intervalos.setSaldoHorasIntraJornadaList(new ArrayList<>());
        intervalos.setSaldoHorasInterJornada(Duration.ZERO);
        intervalos.setIndiceAbsenteismo(0d);
    }

    public void setCalculaIntervalosJornadas(List<MarcacoesDia> marcacoesDias) {
        intervalos.setSaldoHorasIntraJornadaList(getIntraJornada(marcacoesDias));
        intervalos.setSaldoHorasInterJornada(getInterJornada(marcacoesDias));
    }

    /**
     * qtdeHorasDeveriaTrabalhar=Duration.ofHours(160);
     * qtdeHorasNaoTrabalhadas=Duration.ofHours(2); Resultado: 1.25%");
     *
     * @param horasPrevistas
     * @param saldoAusencias
     */
    public void setCalculaIndiceAbsenteismo(Saldo horasPrevistas, SaldoAusencias saldoAusencias) {
        intervalos.setSaldoHorasDeveriaTrabalharAbsenteismo(horasPrevistas.getDiurnas().plus(horasPrevistas.getNoturnas()));
        intervalos.setSaldoHorasNaoTrabalhadasAbsenteismo(saldoAusencias.getDiurnas().plus(saldoAusencias.getNoturnas()));
        //qtdeHorasNaoTrabalhadasPeriodo = qtdeHorasNaoTrabalhadasPeriodo.minus(Duration.ofMinutes(300));

        intervalos.setIndiceAbsenteismo(Utils.getPercentualAbsenteismo(
                intervalos.getSaldoHorasDeveriaTrabalharAbsenteismo(), intervalos.getSaldoHorasNaoTrabalhadasAbsenteismo()));
    }

    private List<Duration> getIntraJornada(List<MarcacoesDia> marcacoesDias) {
        List<Duration> list = new ArrayList<>();
        ultimaSaida = null;
        list.add(Duration.ZERO);
        marcacoesDias.forEach((MarcacoesDia marcacoesDia) -> {
            //Se dia anterior virada de dia
            if (ultimaSaida != null) {
                if (ultimaSaida.getDate() > marcacoesDia.getHorarioEntrada().getDate()) {
                    ultimaSaida = Utils.DiminuiDiasData(ultimaSaida, 1);
                }
                list.add(Utils.getIntervaloDuration(ultimaSaida, marcacoesDia.getHorarioEntrada()));
            }
            if (marcacoesDia.getHorarioSaida() != null) {
                ultimaSaida = marcacoesDia.getHorarioSaida();
            } else {
                ultimaSaida = null;
            }
        });
        return list;
    }

    private Duration getInterJornada(List<MarcacoesDia> marcacoesDias) {
        Date horarioEntrada = gethorarioEntrada(marcacoesDias);
        Date horarioSaida = gethorarioSaida(marcacoesDias);
        Date ultimaMarcacaoDiaAnterior2 = ultimaMarcacaoDiaAnterior;
        setUltimaMarcacaoDiaAnterior(horarioSaida);
        if (ultimaMarcacaoDiaAnterior2 != null && horarioEntrada != null) {
            //Se dia anterior virada de dia
            if (ultimaMarcacaoDiaAnterior2.getDate() > horarioEntrada.getDate()) {
                ultimaMarcacaoDiaAnterior2 = Utils.DiminuiDiasData(ultimaMarcacaoDiaAnterior2, 1);
            } else if (ultimaMarcacaoDiaAnterior2.getHours() > horarioEntrada.getHours()) {
                ultimaMarcacaoDiaAnterior2 = Utils.DiminuiDiasData(ultimaMarcacaoDiaAnterior2, 1);
            }
            return Utils.getIntervaloDuration(ultimaMarcacaoDiaAnterior2, horarioEntrada);
        } else {
            return Duration.ZERO;
        }
    }

    private Date gethorarioEntrada(List<MarcacoesDia> marcacoesDias) {
        return marcacoesDias
                .stream()
                .filter(f -> (f.getHorarioSaida() != null))
                .sorted(Comparator.comparing(MarcacoesDia::getIdSequencia))
                .map(f -> f.getHorarioEntrada())
                .findFirst()
                .orElse(null);
    }

    private Date gethorarioSaida(List<MarcacoesDia> marcacoesDias) {
        return marcacoesDias
                .stream()
                .filter(f -> (f.getHorarioSaida() != null))
                .sorted(Comparator.comparing(MarcacoesDia::getIdSequencia).reversed())
                .map(f -> f.getHorarioSaida())
                .findFirst()
                .orElse(null);
    }

    public void setUltimaMarcacaoDiaAnterior(Date horarioSaida) {
        ultimaMarcacaoDiaAnterior = horarioSaida;
    }
}
