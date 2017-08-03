package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.Comparator;
import java.util.Date;
import java.util.NoSuchElementException;

/**
 * Consulta feriados cadastrados e vinculados a esse funcionário
 *
 * @author enio.junior
 */
public class FuncionarioCalendarioService {

    private final Funcionario funcionarioAPI;
    private final Calculo calculo;

    public FuncionarioCalendarioService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
    }

    public boolean isFeriadoNesseDia() {
        try {
            if (funcionarioAPI.getFuncionarioCalendarioList() != null) {
                return funcionarioAPI.getFuncionarioCalendarioList()
                        //Calendário do dia ou o primeiro anterior
                        .stream().filter(f -> (Utils.formatoDataSemHorario(f.getDataInicio()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0))
                        .sorted(Comparator.comparing(FuncionarioCalendario::getDataInicio).reversed())
                        .map(f -> f.getCalendario()).findFirst().get()
                        .getCalendarioFeriadoList().stream()
                        //Consultar se existe feriado no dia
                        .filter(f -> Utils.formatoDataSemHorario(this.calculo.getDiaProcessado()).compareTo(Utils.formatoDataSemHorario(f.getDataInicio())) == 0)
                        .count()>0;
            } else {
                return false;
            }
        } catch (NoSuchElementException ex) {
            return false;
        }
    }

    public long getQuantidadeFeriadosPeriodo(Date dataInicio, Date datafim) {
        try {
            if (funcionarioAPI.getFuncionarioCalendarioList() != null) {
                return funcionarioAPI.getFuncionarioCalendarioList().stream()
                        .findFirst().get()
                        .getCalendario().getCalendarioFeriadoList().stream()
                        .filter(f -> Utils.formatoDataSemHorario(f.getDataInicio())
                                .compareTo(Utils.formatoDataSemHorario(dataInicio)) == 0)
                        .count();
            } else {
                return 0;
            }
        } catch (NoSuchElementException ex) {
            return 0;
        }
    }

}
