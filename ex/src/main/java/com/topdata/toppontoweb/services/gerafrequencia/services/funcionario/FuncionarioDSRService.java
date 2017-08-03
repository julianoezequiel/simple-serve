package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import java.util.Date;

/**
 * Consulta dados DSR do funcionário
 *
 * @author enio.junior
 */
public class FuncionarioDSRService {

    private final Funcionario funcionarioAPI;
    private final Calculo calculo;

    public FuncionarioDSRService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
    }

    public boolean getNaoDescontaDSRdia(Date dia) {
        if (funcionarioAPI.getFuncionarioDiaNaoDescontaDSRList() != null) {
            return funcionarioAPI.getFuncionarioDiaNaoDescontaDSRList().stream()
                    .filter(f -> (f.getData().compareTo(dia) == 0))
                    .count()>0;
        }
        return false;
    }

    public int getEmpresa() {
        if (funcionarioAPI.getDepartamento() != null) {
            return funcionarioAPI.getDepartamento().getEmpresa().getIdEmpresa();
        }
        return 0;
    }

}
