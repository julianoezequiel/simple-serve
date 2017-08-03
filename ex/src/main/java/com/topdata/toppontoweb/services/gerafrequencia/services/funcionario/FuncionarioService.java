package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import java.util.Date;

/**
 * Inicialização das classes que vão realizar a leitura das listas de Funcionários
 * @author enio.junior
 */
public class FuncionarioService {

    private final Funcionario funcionarioAPI;
    private final FuncionarioEmpresaFechamentoService funcionarioEmpresaFechamentoService;
    private final FuncionarioJornadaService funcionarioJornadaService;
    private final FuncionarioBancodeHorasService funcionarioBancodeHorasService;
    private final FuncionarioCompensacoesService funcionarioCompensacoesService;
    private final FuncionarioCalendarioService funcionarioCalendarioService;
    private final FuncionarioAbonoService funcionarioAbonoService;
    private final FuncionarioDSRService funcionarioDSRService;

    public FuncionarioService(Calculo calculo) {
        this.funcionarioAPI = calculo.getEntradaAPI().getFuncionario();
        this.funcionarioEmpresaFechamentoService = new FuncionarioEmpresaFechamentoService(calculo);
        this.funcionarioJornadaService = new FuncionarioJornadaService(calculo);
        this.funcionarioBancodeHorasService = new FuncionarioBancodeHorasService(calculo);
        this.funcionarioCompensacoesService = new FuncionarioCompensacoesService(calculo);
        this.funcionarioCalendarioService = new FuncionarioCalendarioService(calculo);
        this.funcionarioAbonoService = new FuncionarioAbonoService(calculo);
        this.funcionarioDSRService = new FuncionarioDSRService(calculo);
    }

    public Date getDataAdmissao() {
        return funcionarioAPI.getDataAdmissao();
    }

    public Date getDataDemissao() {
        return funcionarioAPI.getDataDemissao();
    }

    public FuncionarioEmpresaFechamentoService getFuncionarioEmpresaFechamentoService() {
        return funcionarioEmpresaFechamentoService;
    }

    public FuncionarioJornadaService getFuncionarioJornadaService() {
        return funcionarioJornadaService;
    }

    public FuncionarioBancodeHorasService getFuncionarioBancodeHorasService() {
        return funcionarioBancodeHorasService;
    }

    public FuncionarioCompensacoesService getFuncionarioCompensacoesService() {
        return funcionarioCompensacoesService;
    }

    public FuncionarioCalendarioService getFuncionarioCalendarioService() {
        return funcionarioCalendarioService;
    }

    public FuncionarioAbonoService getFuncionarioAbonoService() {
        return funcionarioAbonoService;
    }

    public FuncionarioDSRService getFuncionarioDSRService() {
        return funcionarioDSRService;
    }

}
