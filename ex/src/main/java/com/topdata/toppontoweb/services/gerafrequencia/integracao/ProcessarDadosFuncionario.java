package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.time.Duration;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.Utils;

/**
 * realiza o processamento dos dados do funcionario
 *
 * @author juliano.ezequiel
 */
public class ProcessarDadosFuncionario implements Runnable {

    public final static Logger LOGGER = LoggerFactory.getLogger(ProcessarDadosFuncionario.class.getName());

    private final Double pesoPercentFunc;
    private final GeraFrequenciaStatusTransfer statusTransfer;
    private final GeraFrequenciaServices geraFrequenciaServices;
    private final IGeraFrequenciaTransfer relatorioTransfer;
    private Thread tCalculoIndividual;
    private Date dataInicio;
    private Date dataFim;
    private EntradaApi entradaApi;
    private RelatoriosGeraFrequenciaTransfer apiTransfer;
    private final Funcionario funcionario;

    /**
     *
     * @param pesoPercentFunc
     * @param gfst GeraFrequenciaStatusTransfer
     * @param gfs GeraFrequenciaServices
     * @param igft IGeraFrequenciaTransfer
     * @param funcionario
     */
    public ProcessarDadosFuncionario(Double pesoPercentFunc, GeraFrequenciaStatusTransfer gfst,
            GeraFrequenciaServices gfs, IGeraFrequenciaTransfer igft, Funcionario funcionario) {
        this.pesoPercentFunc = pesoPercentFunc;
        this.statusTransfer = gfst;
        this.geraFrequenciaServices = gfs;
        this.relatorioTransfer = igft;
        this.dataFim = new Date();
        this.dataInicio = new Date();
        this.funcionario = funcionario;
    }

    /**
     * Ajustas as datas de Início e fim do processo
     *
     * @param funcionario
     */
    private synchronized void ajustarDatasInicioFim(Funcionario funcionario) {
        //data de inicio dos calculos
        if (this.apiTransfer.getTipo_relatorio() == null || (this.apiTransfer.getTipo_relatorio() != null
                && !this.apiTransfer.getTipo_relatorio().equals(RelatorioHandler.TIPO_RELATORIO.PRESENCA))) {
            try {
                this.dataInicio = this.geraFrequenciaServices.getMenorData()
                        .getData(funcionario, this.apiTransfer.getPeriodoInicio());
                //TODO: função para a data fim
                this.dataFim = this.apiTransfer.getPeriodoFim();
                if (this.dataFim.before(new Date())) {
                    this.dataFim = new Date();
                }
            } catch (ServiceException ex) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
                this.tCalculoIndividual.interrupt();
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }
    }

    /**
     * Ajsuta a quantidade de dias que serão processados, este dados seve para o
     * cáculo de percentual do progresso de cálculo
     */
    private synchronized void ajustarQtdDiasProcesso() {
        Duration duration = Duration.between(this.apiTransfer.getPeriodoInicio().toInstant(),
                this.apiTransfer.getPeriodoFim().toInstant());
        long qtdDias = duration.toDays();
        this.entradaApi.setQtdDias((int) qtdDias);
        LOGGER.debug("QTD dias para processar " + qtdDias);
    }

    /**
     * Inicia o processo multi-thread
     *
     * @param funcionario
     */
    private synchronized void inicarCalculoIndividual(Funcionario funcionario) {
        try {
            //thread de calculo da api
            this.tCalculoIndividual = new Thread(new CalculoIndividual(this.entradaApi, this.statusTransfer));
            this.tCalculoIndividual.setName("t_IdFuncionario_" + funcionario.getIdFuncionario());
            this.tCalculoIndividual.setPriority(Thread.MIN_PRIORITY);

            //inicia o processo existente na thread
            this.tCalculoIndividual.start();

            this.tCalculoIndividual.join();
        } catch (InterruptedException ex) {
            this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            this.tCalculoIndividual.interrupt();
            LOGGER.error(this.getClass().getSimpleName(), ex);
        }
    }

    @Override
    public synchronized void run() {
        if (!isParar()) {

            LOGGER.debug("Processando dados " + funcionario.toString());

            this.apiTransfer = (RelatoriosGeraFrequenciaTransfer) this.relatorioTransfer;
            this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ACESSANDO_BASE);

            this.ajustarDatasInicioFim(funcionario);

            //seta o valor que o funcionário tem sobre o percentual
            funcionario.setPesoPercentual(this.pesoPercentFunc);
            //cria e seta os valores para a entrada da api de cálculo
            this.entradaApi = this.geraFrequenciaServices.getEntradaApiPadrao()
                    .getEntrada(funcionario, Utils.configuraHorarioData(this.dataInicio, 0, 0, 0), Utils.configuraHorarioData(this.dataFim, 23, 59, 59), this.geraFrequenciaServices, this.statusTransfer);

            if (!isParar()) {
                this.apiTransfer.getTipo_relatorio().configurarEntradaApi(this.entradaApi);
                this.ajustarQtdDiasProcesso();

                this.inicarCalculoIndividual(funcionario);
            }

        } else if (GeraFrequenciaServices.STATUS_MAP.containsKey(this.statusTransfer.getId())) {
            GeraFrequenciaServices.STATUS_MAP.remove(this.statusTransfer.getId());
            LOGGER.debug("Removendo TCalculo e cancelando processamento dados " + funcionario.toString());
        } else {
            LOGGER.debug("Cancelando processamento dados " + funcionario.toString());
        }
    }

    private boolean isParar() {
        if (GeraFrequenciaServices.STATUS_MAP.containsKey(this.statusTransfer.getId())) {
            return !GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId())
                    .getCalculoPrincipal().getParar().equals(Boolean.FALSE);
        } else {
            return false;
        }
    }

}
