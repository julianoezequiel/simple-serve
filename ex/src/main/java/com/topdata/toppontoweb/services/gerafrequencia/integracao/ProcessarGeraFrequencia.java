package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.relatorios.gerafrequencia.GeraFrequenciaRelatorio;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.Observable;
import java.util.function.Consumer;

/**
 * processo mult-thread para realizar os cálculos do gera frequencia
 *
 * @author juliano.ezequiel
 */
public class ProcessarGeraFrequencia extends Observable implements Runnable  {

    public final static Logger LOGGER = LoggerFactory.getLogger(ProcessarGeraFrequencia.class.getName());

    private enum ESTADO_THREAD {
        PARADA,
        PROCESSANDO,
        FINALIZADO
    }

    private final int THREAD_LIMIT = 1;

    private final List<Funcionario> funcionarioList;
    private final GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;
    private final GeraFrequenciaServices geraFrequenciaServices;
    private final IGeraFrequenciaTransfer geraFrequenciaTransfer;
    private Double pesoPercentFunc;
    private LinkedList<Processo> filaProcessosPendentes = new LinkedList<>();
    private LinkedList<Processo> filaProcessando = new LinkedList<>();

    /**
     *
     * @param funcList List<Funcionario> funcList
     * @param gfst GeraFrequenciaStatusTransfer
     * @param gfs GeraFrequenciaServices
     * @param igft IGeraFrequenciaTransfer
     */
    public ProcessarGeraFrequencia(List<Funcionario> funcList, GeraFrequenciaStatusTransfer gfst,
            GeraFrequenciaServices gfs, IGeraFrequenciaTransfer igft) {
        this.funcionarioList = funcList;
        this.geraFrequenciaStatusTransfer = gfst;
        this.geraFrequenciaServices = gfs;
        this.geraFrequenciaTransfer = igft;

    }

    @Override
    public synchronized void run() {

        try {
            LOGGER.debug("processar gera frequencia tipo " + this.geraFrequenciaStatusTransfer.gettIPO_PROCESSO());

            calcularPesoPercentual();

            funcionarioList.stream().forEach(new AgendarProcesso());

            //Loop principal de calculo
            while (this.filaProcessosPendentes.size() > 0 && !this.geraFrequenciaStatusTransfer.getCalculoPrincipal().getParar()) {
                long processando = this.filaProcessando.stream()
                        .filter(p -> p.getEstado_thread().equals(ESTADO_THREAD.PROCESSANDO)).count();
                if (processando < THREAD_LIMIT) {
                    Processo processo = this.filaProcessosPendentes.pop();
                    processo.setEstado_thread(ESTADO_THREAD.PROCESSANDO);
                    processo.getT().setName(this.getClass().getSimpleName());
                    processo.getT().start();
                    this.filaProcessando.push(processo);
                }
                this.filaProcessando.stream().forEach(new VerificarAtivas());

            }

            //Loop para aguarda todas as threads terminarem o proceso
            while (this.filaProcessando.stream().anyMatch(t -> t.getEstado_thread().equals(ESTADO_THREAD.PROCESSANDO))
                    && !this.geraFrequenciaStatusTransfer.getCalculoPrincipal().getParar()) {
                try {
                    Thread.sleep(10);
                    this.filaProcessando.stream().forEach(new VerificarAtivas());
                } catch (InterruptedException ex) {
                    LOGGER.error(this.getClass().getSimpleName(), ex);
                }
            }
            //termino do processo
            if (!this.geraFrequenciaStatusTransfer.getCalculoPrincipal().getParar()) {
                finalizarGeraFerquencia();
            }

        } catch (ServiceException ex) {
            this.geraFrequenciaStatusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            LOGGER.error(this.getClass().getSimpleName(), ex);
        }
    }

    /**
     * Agendas os processo para serem realizados
     */
    private class AgendarProcesso implements Consumer<Funcionario> {

        public AgendarProcesso() {
        }

        @Override
        public synchronized void accept(Funcionario f) {

            ProcessarDadosFuncionario processarDadosFuncionario
                    = new ProcessarDadosFuncionario(ProcessarGeraFrequencia.this.pesoPercentFunc,
                            ProcessarGeraFrequencia.this.geraFrequenciaStatusTransfer,
                            ProcessarGeraFrequencia.this.geraFrequenciaServices,
                            ProcessarGeraFrequencia.this.geraFrequenciaTransfer, f);

            Thread t = new Thread(processarDadosFuncionario);
            t.setPriority(Thread.MIN_PRIORITY);
            ProcessarGeraFrequencia.this.filaProcessosPendentes.push(new Processo(t, processarDadosFuncionario, ESTADO_THREAD.PARADA));
            ProcessarGeraFrequencia.this.geraFrequenciaStatusTransfer.getThreadList().add(t);
        }
    }

    /**
     * Verifica as threads que estão ativas e ajusta o estado das threads
     */
    private static class VerificarAtivas implements Consumer<Processo> {

        public VerificarAtivas() {
        }

        @Override
        public void accept(Processo f) {
            if (!f.getT().isAlive()) {
                f.setEstado_thread(ESTADO_THREAD.FINALIZADO);
            }
        }
    }

    /**
     * Define qual será o peso do percentual que cada funcionário terá sobre o
     * cálculo total
     */
    private void calcularPesoPercentual() {
        //calcula o peso que cada funcionario a ser processado terá sobre o percentual total
        int divisor = this.funcionarioList.isEmpty() ? 1 : this.funcionarioList.size();
        double max = 90d;

        if (this.geraFrequenciaStatusTransfer.gettIPO_PROCESSO().equals(CONSTANTES.Enum_TIPO_PROCESSO.FECHAMENTO_EMPRESA)) {
            max = 45d;
        } else if (this.geraFrequenciaStatusTransfer.gettIPO_PROCESSO().equals(CONSTANTES.Enum_TIPO_PROCESSO.EXPORTAR_ARQUIVO)) {
            max = 60d;
        }

        this.pesoPercentFunc = max / divisor;
        //adiciona um percentual inicial ao processo
        GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setPercentual(
                this.geraFrequenciaStatusTransfer.gettIPO_PROCESSO().equals(CONSTANTES.Enum_TIPO_PROCESSO.FECHAMENTO_EMPRESA)
                ? 10d : 0d);

    }

    /**
     * Finaliza o cálcculo do gera frequencia , e verifica qual será o tipo de
     * saida
     *
     * @throws ServiceException
     */
    private void finalizarGeraFerquencia() throws ServiceException {
        //limpa a lista de threads
        this.geraFrequenciaStatusTransfer.getThreadList().clear();

        switch (this.geraFrequenciaStatusTransfer.gettIPO_PROCESSO()) {
            case FECHAMENTO_EMPRESA:
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setPercentual(50d);
                this.geraFrequenciaServices.getFechamentoPeriodoService().iniciarProcessoSalvarDadosFechamento(this.geraFrequenciaStatusTransfer);
                break;

            case FECHAMENTO_BANCO_HORAS:
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO);
                break;
            case VERIFICACAO_SALDO_BH:
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO);
                notifyObservers(this.geraFrequenciaStatusTransfer);
                
                break;
            case RELATORIO:
                //gera o relatorio
                GerarRelatorio gerarRelatorio = new GeraFrequenciaRelatorio(this.geraFrequenciaTransfer,
                        this.geraFrequenciaServices, this.geraFrequenciaStatusTransfer);
                Thread thread = new Thread(gerarRelatorio);
                thread.start();
                break;
            case EXPORTAR_ARQUIVO:
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO);
                break;
            case MANUTENCAO_MARCACOES:
                GeraFrequenciaServices.STATUS_MAP.get(this.geraFrequenciaStatusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.CALCULO_CONCLUIDO);
                break;
            default:
                break;
        }
    }

    /**
     * Classe de encapsulamento do processo a ser realizado
     */
    private class Processo {

        Thread t;
        ProcessarDadosFuncionario dadosFuncionario;
        ESTADO_THREAD estado_thread;

        public Processo(Thread t, ProcessarDadosFuncionario dadosFuncionario, ESTADO_THREAD estado_thread) {
            this.t = t;
            this.dadosFuncionario = dadosFuncionario;
            this.estado_thread = estado_thread;
        }

        public Thread getT() {
            return t;
        }

        public void setEstado_thread(ESTADO_THREAD estado_thread) {
            this.estado_thread = estado_thread;
        }

        public ESTADO_THREAD getEstado_thread() {
            return estado_thread;
        }

    }

}
