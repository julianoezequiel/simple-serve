package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.compensacao.CompensacaoService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Define a menor data para que os dados da api sejam carregados<br>
 * A regra para a menor data é:<br>
 * A menor data do banco de horas ou a menor data da compensação ou a menor data
 * a ser compensada.
 *
 * @author juliano.ezequiel
 */
@Service
public class MenorData {

    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private CompensacaoService compensacaoService;

    public final static Logger LOGGER = LoggerFactory.getLogger(MenorData.class.getName());

    public MenorData() {
    }

    public synchronized Date getData(Funcionario funcionario, Date dataInicio) throws ServiceException {

        //lista de dadas para verificar qual é data menor entre os parametro necessários
        List<Date> datas = new ArrayList<>();
        datas.add(dataInicio);
        //busca os banco de horas do funcionario
        List<FuncionarioBancoHoras> funcBancoHorasList
                = this.funcionarioBancoHorasService.buscarPorFuncionario(funcionario.getIdFuncionario());

        //busca as compensações do funcionario
        List<Compensacao> compensacaoList
                = this.compensacaoService.buscarPorFuncionario(funcionario.getIdFuncionario());

        //filtra a menor data do banco de horas
        datas.add(funcBancoHorasList
                .stream().sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio))
                .map(f -> f.getDataInicio())
                .findFirst().orElse(dataInicio));
        //filtra a menor data de compensação
        datas.add(compensacaoList
                .stream().sorted(Comparator.comparing(Compensacao::getDataInicio))
                .map(f -> f.getDataInicio())
                .findFirst().orElse(dataInicio));
        //filtra a menor data a ser compensada
        datas.add(compensacaoList
                .stream().sorted(Comparator.comparing(Compensacao::getDataCompensada))
                .map(f -> f.getDataCompensada())
                .findFirst().orElse(dataInicio));
        //ordena as datas
        Collections.sort(datas);
        //retorna a data menor
        Date data = new Date(datas.iterator().next().getTime());
        LOGGER.debug("Menor data {} - funcionário : {}", data, funcionario.getIdFuncionario());
        return data;
    }

}
