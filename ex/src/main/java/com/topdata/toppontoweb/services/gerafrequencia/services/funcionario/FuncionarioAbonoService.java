package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 * Consulta abonos do funcionário (parcial / integral / afastamento)
 *
 * @author enio.junior
 */
public class FuncionarioAbonoService {

    private final Funcionario funcionarioAPI;
    private final Calculo calculo;

    public FuncionarioAbonoService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
    }

    /**
     * Se tem afastamento no dia que está sendo processado
     *
     * @return
     */
    public Afastamento getAfastamento() {
        if (funcionarioAPI.getAfastamentoList() != null) {
            return funcionarioAPI.getAfastamentoList().stream()
                    .filter(f -> (Utils.formatoDataSemHorario(f.getDataInicio()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0
                            && (f.getDataFim() == null || Utils.formatoDataSemHorario(f.getDataFim()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) >= 0)))
                    .findFirst().orElse(new Afastamento());
        } else {
            return new Afastamento();
        }
    }

    /**
     * Se tem abono no dia que está sendo processado
     *
     * @return
     */
    public Abono getAbono() {
        if (funcionarioAPI.getAbonoList() != null) {
            return funcionarioAPI.getAbonoList().stream()
                    .filter(f -> (Utils.formatoDataSemHorario(f.getData()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) == 0))
                    .findFirst().orElse(new Abono());
        }
        return new Abono();
    }
}
