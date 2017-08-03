package com.topdata.toppontoweb.services.gerafrequencia.services.calculo;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasSaldoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.services.fiscal.ListaAFDT;
import com.topdata.toppontoweb.services.gerafrequencia.services.fiscal.ListasACJEF;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 * Processamento do Período gerando os dados de saída, Período de Compensação,
 * Saldo banco de horas, acúmulo de extras...
 *
 * @author enio.junior
 */
public class CalculoPeriodoService {

    public final static Logger LOGGER = LoggerFactory.getLogger(CalculoPeriodoService.class.getName());

    private final List<SaidaDia> saidaDiaList;
    private final CalculoDiaService calculoDiaService;
    private final Calculo calculo;
    private final ListaAFDT listaAFDT;
    private final ListasACJEF listasACJEF;
    private final BancodeHorasSaldoDia saldoDiaBancodeHoras;

    /**
     * Inicialização das listas na API
     *
     * @param entradaAPI
     */
    public CalculoPeriodoService(EntradaApi entradaAPI) {
        this.saldoDiaBancodeHoras = new BancodeHorasSaldoDia();
        this.calculo = new Calculo(entradaAPI, this.saldoDiaBancodeHoras);
        this.saidaDiaList = new ArrayList<>();
        this.listaAFDT = new ListaAFDT(this.calculo);
        this.listasACJEF = new ListasACJEF(this.calculo);
        this.calculoDiaService = new CalculoDiaService(this.calculo, this.listaAFDT, this.listasACJEF);
    }

    /**
     * Executa as etapas necessárias para atender as regras do período
     *
     * @return
     */
    public synchronized List<SaidaDia> getSaidaAPI() {
        try {
            if (isValido() == true) {

                LOGGER.debug("Ajustando virada de dia dos horários/jornadas... funcionário: {} ", this.calculo.getNomeFuncionario());
                this.calculo.getFuncionarioService().getFuncionarioJornadaService().setAtualizarFuncionario();

                if (this.calculo.getEntradaAPI().isProcessaCalculo() && this.calculo.getEntradaAPI().isProcessaDSR()) {
                    LOGGER.debug("Calculando contadores de DSR... funcionário: {} ", this.calculo.getNomeFuncionario());
                    this.calculo.getDsrService().setContadoresPeriodoDSR();
                }

                LOGGER.debug("Criando a lista de marcações válidas de todo o período... funcionário: {} ", this.calculo.getNomeFuncionario());
                this.calculo.getFuncionarioMarcacoesService().setMarcacoesValidasList();

                //Processo interno (não grava saidaDiaList)
                if (this.calculo.getEntradaAPI().isProcessaCalculo()) {
                    preparacaoCalculo();
                }

                //Processamento do período solicitado
                calculoDiasPeriodoInformadoGeraSaida();

                //Setar período processado em DSR para cálculos de extras e faltas
                if (this.calculo.getEntradaAPI().isProcessaCalculo() && this.calculo.getEntradaAPI().isProcessaDSR()) {
                    LOGGER.debug("Processando cálculo DSR... funcionário: {} ", this.calculo.getNomeFuncionario());
                    this.calculo.getDsrService().setCalculosDsr(this.saidaDiaList);
                }
            }
        } catch (Exception e) {
            LOGGER.error(this.getClass().getSimpleName(), e);
            e.printStackTrace();
        }
        return this.saidaDiaList;
    }

    /**
     * Levantamento das informações dos períodos cadastrados para esse
     * funcionário
     */
    private void preparacaoCalculo() {
        criaListaDiasPeriodoCompensacao();
        calculoAcumuloExtrasMensalSemanal();
        calculoSaldoDiaBancodeHoras();
        calculoDiasPeriodoSaldoBancoDeHorasAteDataInicioProcessamento();
    }

    /**
     * Processa somente internamente (não grava saidaDiaList) 1º cria a lista
     * atualizada de ausências Objetivo é calcular as ausências de todo o
     * período de compensação (Sem banco de horas) Já subtraindo os abonos se
     * tiver, pegando somente a diferença 2º atualiza essa lista com as extras
     * Assim deixa pronto para iniciar os cálculos do período solicitado
     */
    private void criaListaDiasPeriodoCompensacao() {
        LOGGER.debug("Criando lista período compensação... funcionário: {} ", this.calculo.getNomeFuncionario());
        if (this.calculo.getFuncionarioService().getFuncionarioCompensacoesService()
                .isPossuiListaCompensacaoCadastrada()) {
            this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().criaListaCompensacaoAusenciasCadaDia(this.calculo.getEntradaAPI());
            criaListaAusenciasCompensacao();
            criaListaExtrasCompensacao();
        }
    }

    /**
     * 1º cria a lista atualizada de ausências Informa que é para rodar somente
     * o período de compensação de Ausências
     */
    private void criaListaAusenciasCompensacao() {
        this.calculo.setPeriodoCompensacaoAusencias(true);
        LOGGER.debug("Processando período compensação de ausências... funcionário: {} ", this.calculo.getNomeFuncionario());
        processaPeriodoCompensacao();
        this.calculo.setPeriodoCompensacaoAusencias(false);
    }

    /**
     * 2º atualiza essa lista com as extras Informa que é para rodar somente o
     * período de compensação de Extras
     */
    private void criaListaExtrasCompensacao() {
        this.calculo.setPeriodoCompensacaoExtras(true);
        LOGGER.debug("Processando período compensação de extras... - funcionário: {} ", this.calculo.getNomeFuncionario());
        processaPeriodoCompensacao();
        this.calculo.setPeriodoCompensacaoExtras(false);
    }

    /**
     * Percorre toda a lista calculando sem banco de horas
     */
    private void processaPeriodoCompensacao() {
        //Calcula a partir do primeiro dia da lista de compensação
        Date dia = this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getPrimeiroDiaListaCompensacaoCadastrada();

        //Enquanto data menor que a data do último dia de compensação
        while ((dia.before(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getUltimoDiaListaCompensacaoCadastrada()))
                || (dia.equals(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getUltimoDiaListaCompensacaoCadastrada()))) {
            //Realiza o cálculo do dia e grava o resultado na lista de compensações
            this.calculo.setDiaProcessado(dia);
            calculoDiaService.processaDia(dia);
            dia = Utils.AdicionaDiasData(dia, 1);
        }
    }

    /**
     * Retorna o saldo atualizado de BH para iniciar os cálculos Consulta se
     * subtotal ou fechamento Processa somente internamente (não grava
     * saidaDiaList)
     *
     * Saldo dia anterior periodo banco de horas calcular até dois dias antes da
     * data de início do relatório (pois o processamento normal começa com 1 dia
     * antes)
     *
     */
    private synchronized void calculoDiasPeriodoSaldoBancoDeHorasAteDataInicioProcessamento() {
        this.calculo.setPeriodoSaldoAnteriorDiaPeriodoBancodeHoras(true);
        this.saldoDiaBancodeHoras.setSaldoPrimeiroDiaBancodeHoras(Duration.ZERO);
        this.calculo.setCreditoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.calculo.setDebitoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.calculo.setSaldoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.calculo.setDiaProcessado(Utils.DiminuiDiasData(this.calculo.getEntradaAPI().getDataInicioPeriodo(),1));
        Date diaSubtotal = this.calculo.getBancodeHorasService()
                .getBancodeHorasFechamentoService().getConsultaPrimeiroSubtotalBH();
        if (diaSubtotal != null) {
            //Enquanto data menor que a data de início do cálculo
            //while (diaSubtotal.before(Utils.DiminuiDiasData(this.calculo.getEntradaAPI().getDataInicioPeriodo(), 1))) {
            while (diaSubtotal.before(this.calculo.getEntradaAPI().getDataInicioPeriodo())) {
                this.calculo.setDiaProcessado(diaSubtotal);
                LOGGER.debug("Processando saldo acumulado período banco de horas... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                this.calculoDiaService.processaDia(diaSubtotal);
                diaSubtotal = Utils.AdicionaDiasData(diaSubtotal, 1);
            }
        }
        this.calculo.setPeriodoSaldoAnteriorDiaPeriodoBancodeHoras(false);
    }

    /**
     * Calcula saldo do primeiro dia do banco de horas e do saldo acumulado
     * atualizado para iniciar o cálculo do período Processa somente
     * internamente, não mostra na saída
     */
    private synchronized void calculoSaldoDiaBancodeHoras() {
        this.calculo.setDiaProcessado(this.calculo.getEntradaAPI().getDataInicioPeriodo());
        if (this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras() != null) {
            LOGGER.debug("Processando saldo 1º dia e anterior banco de horas... funcionário: {} ", this.calculo.getNomeFuncionario());
            saldoPrimeiroDiaBancodeHoras();
        }
    }

    /**
     * Calcula saldo do primeiro dia do banco de horas
     */
    private void saldoPrimeiroDiaBancodeHoras() {
        LOGGER.debug("Processando saldo 1º dia do banco de horas... funcionário: {} ", this.calculo.getNomeFuncionario());
        this.calculo.setPeriodoSaldoPrimeiroDiaBancodeHoras(true);
        Date dia = new Date(this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras().getDataInicio().getTime());
        this.saldoDiaBancodeHoras.setDataPrimeiroDiaBancodeHoras(dia);
        this.calculo.setDiaProcessado(dia);
        this.calculoDiaService.processaDia(dia);
        this.calculo.setPeriodoSaldoPrimeiroDiaBancodeHoras(false);
    }

    /**
     * Somente interno, processamento desde o dia 1º Acúmulo horas extras
     * mensal/semanal Percorre o período desde o dia 1º até um dia antes da data
     * de início do período informado Desse modo, teremos a lista de controle
     * mensal/semanal de extras atualizada
     */
    private synchronized void calculoAcumuloExtrasMensalSemanal() {
        Date dia = Utils.getDia1Mes(this.calculo.getEntradaAPI().getDataInicioPeriodo());
        boolean primeiroDia = true;
        while ((dia.before(this.calculo.getEntradaAPI().getDataInicioPeriodo()))) {
            if (!primeiroDia) {
                this.calculo.setDiaProcessado(dia);
                LOGGER.debug("Processando acúmulo extras mensal/semanal... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                calculoDiaService.processaDia(dia);
            }
            primeiroDia = false;
            dia = Utils.AdicionaDiasData(dia, 1);
        }
    }

    /**
     * Processamento dos dias que gera o resultado final do período
     */
    private synchronized void calculoDiasPeriodoInformadoGeraSaida() {
        Date dia = this.calculo.getEntradaAPI().getDataInicioPeriodo();
        while (dia.before(this.calculo.getEntradaAPI().getDataFimPeriodo())
                || dia.equals(this.calculo.getEntradaAPI().getDataFimPeriodo())
                && (!this.calculo.getEntradaAPI().isProcessaPrimeiraInconsistencia()
                || (this.calculo.getEntradaAPI().isProcessaPrimeiraInconsistencia()
                && !this.calculo.getRegrasService().getRegras().isInconsistencia()))) {
            calcularPercentual();
            verificarInconsistencias();
            LOGGER.debug("Processando o cálculo do dia: {} e gravando na saídaDiaList... funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            this.calculo.setDiaProcessado(dia);
            this.saidaDiaList.add(this.calculoDiaService.processaDia(new Date(dia.getTime())));
            dia = Utils.AdicionaDiasData(dia, 1);
        }
    }

    /**
     * Realiza o cálculo do percentual do progresso do andamento dos calculos de
     * cada funcionário
     */
    private synchronized void calcularPercentual() {
        if (this.calculo.getEntradaAPI().getFuncionario().getPercentual() < this.calculo.getEntradaAPI().getFuncionario().getPesoPercentual()) {
            int dias = (this.calculo.getEntradaAPI().getQtdDias() - 1) <= 0 ? 1 : this.calculo.getEntradaAPI().getQtdDias() - 1;
            this.calculo.getEntradaAPI().setQtdDias(dias);
            Double percentual = this.calculo.getEntradaAPI().getFuncionario().getPesoPercentual() / this.calculo.getEntradaAPI().getQtdDias();
            LOGGER.debug("Cálculo percentual funcionário: " + this.calculo.getNomeFuncionario() + " - percentual: " + percentual + " - dia: " + dias);
            if (GeraFrequenciaServices.STATUS_MAP.containsKey(this.calculo.getEntradaAPI().getIdrelatorio())) {
                GeraFrequenciaServices.STATUS_MAP.get(this.calculo.getEntradaAPI().getIdrelatorio()).addPercentual(percentual - this.calculo.getEntradaAPI().getFuncionario().getPercentual());
            }
            this.calculo.getEntradaAPI().getFuncionario().setPercentual(percentual);
        }
    }

    private void verificarInconsistencias() {
        if (this.calculo.getEntradaAPI() != null && GeraFrequenciaServices.STATUS_MAP.get(this.calculo.getEntradaAPI().getIdrelatorio()) != null) {
            if (!(GeraFrequenciaServices.STATUS_MAP.get(this.calculo.getEntradaAPI().getIdrelatorio()).isPossuiInconsistencias())
                    && this.saidaDiaList.stream().anyMatch(r -> r.getRegras().isInconsistencia())) {
                GeraFrequenciaServices.STATUS_MAP.get(this.calculo.getEntradaAPI().getIdrelatorio()).setPossuiInconsistencias(true);
            }
        }
    }

    public boolean isValido() {
        return this.calculo.getEntradaAPI().getDataInicioPeriodo() != null
                && this.calculo.getEntradaAPI().getDataFimPeriodo() != null
                && this.calculo.getEntradaAPI().getFuncionario() != null;
    }

}
