package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDiaNaoDescontaDSR;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.jornada.TipoJornada;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.configuracoes.ConfiguracoesGeraisServices;
import com.topdata.toppontoweb.services.fechamento.FechamentoServices;
import com.topdata.toppontoweb.services.funcionario.abono.AbonoService;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.funcionario.calendario.FuncionarioCalendarioService;
import com.topdata.toppontoweb.services.funcionario.compensacao.CompensacaoService;
import com.topdata.toppontoweb.services.funcionario.dsr.FuncionarioDiaNaoDescontaDSRService;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class EntradaPadrao {

    public final static Logger LOGGER = LoggerFactory.getLogger(EntradaPadrao.class.getName());

    @Autowired
    private MarcacoesService marcacoesService;
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private FuncionarioJornadaService funcionarioJornadaService;
    @Autowired
    private AfastamentoService afastamentoService;
    @Autowired
    private CompensacaoService compensacaoService;
    @Autowired
    private FuncionarioCalendarioService funcionarioCalendarioService;
    @Autowired
    private AbonoService abonoService;
    @Autowired
    private FechamentoServices fechamentoServices;
    @Autowired
    private FuncionarioDiaNaoDescontaDSRService funcionarioDiaNaoDescontaDSRService;
    @Autowired
    private ConfiguracoesGeraisServices configuracoesGeraisServices;
    @Autowired
    private TopPontoService topPontoService;

    public EntradaPadrao() {
    }

    public synchronized EntradaApi getEntrada(Funcionario funcionario, Date dataInicio, Date dataFim,
            GeraFrequenciaServices geraFrequenciaServices, GeraFrequenciaStatusTransfer statusTransfer) {

        try {

            LOGGER.debug("Entrada API Padrão Iniciada funcionário : {}", funcionario.getNome());

            EntradaApi entradaApi = new EntradaApi();

            //Consulta as lista que serão utilizadas pela API
            List<Marcacoes> marcacoes
                    = this.marcacoesService.buscarPorPeriodo(dataInicio, dataFim, funcionario, statusTransfer.isCarregarMarcacoesInvalidas());

            List<FuncionarioBancoHoras> funcBancoHorasList
                    = this.funcionarioBancoHorasService.buscarPorFuncionarioEPeriodo(funcionario, dataInicio, dataFim);

            List<FuncionarioJornada> funcionarioJornadaList
                    = this.funcionarioJornadaService.buscarEntreDatasFuncionario(funcionario, dataInicio, dataFim);
            List<Afastamento> afastamentoList
                    = this.afastamentoService.buscarPorFuncionarioEPeriodo(funcionario.getIdFuncionario(), dataInicio, dataFim);
            List<Compensacao> compensacaoList
                    = this.compensacaoService.buscarPorFuncionario(funcionario.getIdFuncionario());
            List<FuncionarioCalendario> funcionarioCalendarioList
                    = this.funcionarioCalendarioService.buscarPorFuncionario(funcionario);
            List<Abono> abonoList
                    = this.abonoService.buscarPorFuncionarioPeriodo(funcionario, dataInicio, dataFim);
            List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList
                    = this.funcionarioDiaNaoDescontaDSRService.buscarPorFuncionario(funcionario);

            if (statusTransfer.getOperacao() != null && !statusTransfer.getOperacao().equals(CONSTANTES.Enum_OPERACAO.EDITAR)) {
                List<EmpresaFechamentoData> empresaFechamentoDataList
                        = this.fechamentoServices.buscarPorPeriodoFuncionarioEmpresa(funcionario, dataInicio, dataFim);
                funcionario.setEmpresaFechamentoDataList(empresaFechamentoDataList);
            } else {
                funcionario.setEmpresaFechamentoDataList(new ArrayList<>());
            }

            //adiciona as lista ao funcionario que será utilizado pela API
            funcionario.setFuncionarioBancoHorasList(funcBancoHorasList);
            funcionario.setMarcacoesList(marcacoes);
            funcionario.setFuncionarioJornadaList(funcionarioJornadaList);
            funcionario.setAfastamentoList(afastamentoList);
            funcionario.setCompensacaoList(compensacaoList);
            funcionario.setFuncionarioCalendarioList(funcionarioCalendarioList);
            funcionario.setAbonoList(abonoList);
            funcionario.setFuncionarioDiaNaoDescontaDSRList(funcionarioDiaNaoDescontaDSRList);

            //datas de inicio e fim do calculo
            entradaApi.setDataFimPeriodo(((RelatoriosGeraFrequenciaTransfer) geraFrequenciaServices.geraFrequenciaTransfer).getPeriodoFim());
            entradaApi.setDataInicioPeriodo(((RelatoriosGeraFrequenciaTransfer) geraFrequenciaServices.geraFrequenciaTransfer).getPeriodoInicio());

            entradaApi.setFuncionario(funcionario);

            //adiciona as listas padrões para a API
            entradaApi.setDsrList((List<Dsr>) geraFrequenciaServices.dsrList);
            entradaApi.setTipoDiaList((List<TipoDia>) geraFrequenciaServices.tipoDiaList);
            entradaApi.setTipoJornadaList((List<TipoJornada>) geraFrequenciaServices.tipoJornadaList);

            //Configuracoes gerais
            ConfiguracoesGerais cg = configuracoesGeraisServices.buscarConfiguracoesGerais();
            entradaApi.setConfiguracoesGerais(cg);

            entradaApi.setIdrelatorio(statusTransfer.getId());
            LOGGER.debug("Entrada API Padrão Finalizada funcionário : {}", funcionario.getNome());
            
            //Busca quantidade de jornadas
            Long qunatidadeJornadas = funcionarioJornadaService.buscarEntreDatasFuncionarioQuantidade(funcionario, dataInicio, dataFim);
            entradaApi.setQuantidadeJornadasPorPeriodo(qunatidadeJornadas);

            return entradaApi;
        } catch (ServiceException ex) {
            LOGGER.error(this.getClass().getSimpleName(), ex);
            return null;
        }
    }

    public synchronized EntradaApi getEntrada(Funcionario funcionario, Date dataInicio, Date dataFim) {
        try {

            LOGGER.debug("Entrada API Padrão Iniciada funcionário : {}", funcionario.getIdFuncionario());

            EntradaApi entradaApi = new EntradaApi();

            //Consulta as lista que serão utilizadas pela API
            List<Marcacoes> marcacoes
                    = this.marcacoesService.buscarPorPeriodo(dataInicio, dataFim, funcionario, false);

            List<FuncionarioBancoHoras> funcBancoHorasList = this.funcionarioBancoHorasService.buscarPorFuncionarioEPeriodo(funcionario, dataInicio, dataFim);

            List<FuncionarioJornada> funcionarioJornadaList = this.funcionarioJornadaService.buscarEntreDatasFuncionario(funcionario, dataInicio, dataFim);

            List<Afastamento> afastamentoList
                    = this.afastamentoService.buscarPorFuncionarioEPeriodo(funcionario.getIdFuncionario(), dataInicio, dataFim);
            List<Compensacao> compensacaoList
                    = this.compensacaoService.buscarPorFuncionario(funcionario.getIdFuncionario());
            List<FuncionarioCalendario> funcionarioCalendarioList
                    = this.funcionarioCalendarioService.buscarPorFuncionario(funcionario);
            List<Abono> abonoList
                    = this.abonoService.buscarPorFuncionarioPeriodo(funcionario, dataInicio, dataFim);
            List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList
                    = this.funcionarioDiaNaoDescontaDSRService.buscarPorFuncionario(funcionario);

            funcionario.setEmpresaFechamentoDataList(new ArrayList<>());

            //adiciona as lista ao funcionario que será utilizado pela API
            funcionario.setFuncionarioBancoHorasList(funcBancoHorasList);
            funcionario.setMarcacoesList(marcacoes);
            funcionario.setFuncionarioJornadaList(funcionarioJornadaList);
            funcionario.setAfastamentoList(afastamentoList);
            funcionario.setCompensacaoList(compensacaoList);
            funcionario.setFuncionarioCalendarioList(funcionarioCalendarioList);
            funcionario.setAbonoList(abonoList);
            funcionario.setFuncionarioDiaNaoDescontaDSRList(funcionarioDiaNaoDescontaDSRList);

            entradaApi.setFuncionario(funcionario);

            //adiciona as listas padrões para a API
            entradaApi.setDsrList((List<Dsr>) this.topPontoService.buscarTodos(Dsr.class));
            entradaApi.setTipoDiaList((List<TipoDia>) this.topPontoService.buscarTodos(TipoDia.class));
            entradaApi.setTipoJornadaList((List<TipoJornada>) this.topPontoService.buscarTodos(TipoJornada.class));

            //Configuracoes gerais
            ConfiguracoesGerais cg = configuracoesGeraisServices.buscarConfiguracoesGerais();
            entradaApi.setConfiguracoesGerais(cg);

            Duration duration = Duration.between(new Date(dataInicio.getTime()).toInstant(),
                    new Date(dataFim.getTime()).toInstant());
            long qtdDias = duration.toDays();
            entradaApi.setQtdDias((int) qtdDias);

            LOGGER.debug("Entrada API Padrão Finalizada funcionário : {}", funcionario.getIdFuncionario());
            return entradaApi;
        } catch (ServiceException ex) {
            LOGGER.error(this.getClass().getSimpleName(), ex);
            return null;
        }
    }
}
