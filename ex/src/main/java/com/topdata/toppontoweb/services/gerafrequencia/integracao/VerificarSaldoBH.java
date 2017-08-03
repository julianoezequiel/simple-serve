package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.CalculoPeriodoService;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;

/**
 * @version 1.0.0 data 26/06/2017
 * @since 1.0.0 data 26/06/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class VerificarSaldoBH {

    public final static Logger LOGGER = LoggerFactory.getLogger(VerificarSaldoBH.class.getName());

    @Autowired
    private MenorData menorData;
    @Autowired
    private EntradaPadrao entradaPadrao;
    @Autowired
    private FuncionarioService funcionarioService;

    public SaldoBH calcular(Funcionario funcionario, Date dataConsulta) throws ServiceException {

        funcionario = this.funcionarioService.buscar(Funcionario.class, funcionario.getIdFuncionario());
        //menor data para corregar os dados da base
        Date dataInicio = Utils.configuraHorarioData(this.menorData.getData(funcionario, dataConsulta), 0, 0, 0);
        Date dataTermino = Utils.configuraHorarioData(dataConsulta, 23, 59, 59);
        //monta os dados da base na entrada da api
        EntradaApi entradaApi = this.entradaPadrao.getEntrada(funcionario, dataInicio, dataTermino);

        //configurações específicas da API
        entradaApi.setDataInicioPeriodo(Utils.configuraHorarioData(dataConsulta, 0, 0, 0));
        entradaApi.setDataFimPeriodo(Utils.configuraHorarioData(dataConsulta, 23, 59, 59));

        entradaApi.setProcessaCalculo(true);
        entradaApi.setProcessaACJEF(false);
        entradaApi.setProcessaAFDT(false);
        entradaApi.setProcessaAbsenteismo(false);
        entradaApi.setProcessaDSR(false);
        entradaApi.setProcessaManutencaoMarcacoes(false);

        //instancia a classe principal da API
        CalculoPeriodoService calculoPeriodoService = new CalculoPeriodoService(entradaApi);

        //solicita o calculo e 
        List<SaidaDia> saidaDiaList = calculoPeriodoService.getSaidaAPI();//.stream().sorted(Comparator.comparing((SaidaDia s)

        SaidaDia saidaDia = saidaDiaList.stream().sorted(Comparator.comparing((SaidaDia s)
                -> s.getData()).reversed()).findFirst().get();
        
        Date credito = DateHelper.DurationToDate(saidaDia.getBancodeHoras().getSaldoAcumuladoCredito());
        Date debito = DateHelper.DurationToDate(saidaDia.getBancodeHoras().getSaldoAcumuladoDebito());
        Date saldo = DateHelper.DurationToDate(saidaDia.getBancodeHoras().getSaldoAcumuladoDia());

        //cria o objeto de retorno
        SaldoBH saldoBH = new SaldoBH(dataConsulta, credito, debito, saldo);
        saldoBH.setCreditoConv(Utils.MASK.HORA(saidaDia.getBancodeHoras().getSaldoAcumuladoCredito(), false, 3));
        saldoBH.setDebitoConv(Utils.MASK.HORA(saidaDia.getBancodeHoras().getSaldoAcumuladoDebito(), false, 3));
        saldoBH.setSaldoConv(Utils.MASK.HORA(saidaDia.getBancodeHoras().getSaldoAcumuladoDia(), false, 3));

        LOGGER.debug("Funcionário : {} -  Crédito : {} -  Débito : {} - Saldo : {}",
                funcionario.getIdFuncionario(), saldoBH.getCreditoConv(), saldoBH.getDebitoConv(), saldoBH.getSaldoConv());

        return saldoBH;

    }

}
