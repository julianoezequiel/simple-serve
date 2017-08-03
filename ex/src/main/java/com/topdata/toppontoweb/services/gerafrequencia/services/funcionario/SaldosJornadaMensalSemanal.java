package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controle das horas quando as extras da jornada for do tipo mensal ou semanal
 *
 * @author enio.junior
 */
public class SaldosJornadaMensalSemanal {

    private FuncionarioJornada funcionarioJornada;
    private final Calculo calculo;

    public SaldosJornadaMensalSemanal(Calculo calculo) {
        this.funcionarioJornada = null;
        this.calculo = calculo;
    }

    public FuncionarioJornada getFuncionarioJornada() {
        return funcionarioJornada;
    }

    public void setFuncionarioJornada(FuncionarioJornada funcionarioJornada) {
        this.funcionarioJornada = funcionarioJornada;
    }

    public List<SequenciaPercentuais> getPercentuaisTipoDiaExtras(boolean feriado, boolean diaCompensado) {
        if (this.funcionarioJornada
                .getJornada() != null
                && this.funcionarioJornada
                .getJornada().getPercentuaisAcrescimo() != null) {

            List<SequenciaPercentuais> sequenciaPercentuaisList = this.funcionarioJornada
                    .getJornada()
                    .getPercentuaisAcrescimo()
                    .getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getTipoDia().getIdTipodia().compareTo(this.calculo.getRegrasService().getTipoDia().getIdTipodia()) == 0))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());

            return sequenciaPercentuaisList;

        }
        return new ArrayList<>();
    }

}
