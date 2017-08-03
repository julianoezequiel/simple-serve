package com.topdata.toppontoweb.services.gerafrequencia.services.bancodehoras;

import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioBancodeHorasFechamentoService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Após ter os saldos atualizados com as regras de extras e ausências, consulta
 * e realiza as operações de banco de horas, se possuir banco de horas
 * cadastrado, realizada os cálculos...
 *
 * @author enio.junior
 */
public class BancodeHorasService {

    public final static Logger LOGGER = LoggerFactory.getLogger(BancodeHorasService.class.getName());
    private final BancodeHorasAcrescimosService bancodeHorasAcrescimosService;
    private final FuncionarioBancodeHorasFechamentoService bancodeHorasFechamentoService;
    private final FuncionarioService funcionarioService;
    private final Calculo calculo;
    private BancodeHorasApi bancodeHoras;

    public BancodeHorasService(Calculo calculo) {
        this.calculo = calculo;
        this.bancodeHorasAcrescimosService = new BancodeHorasAcrescimosService(calculo);
        this.bancodeHorasFechamentoService = new FuncionarioBancodeHorasFechamentoService(calculo);
        this.funcionarioService = this.calculo.getFuncionarioService();
    }

    public BancodeHorasApi getBancodeHoras() {
        return bancodeHoras;
    }

    public FuncionarioBancodeHorasFechamentoService getBancodeHorasFechamentoService() {
        return bancodeHorasFechamentoService;
    }

    public BancodeHorasApi getCalculosBH(SaidaDia saidaDia) {
        LOGGER.debug("Processando cálculos de BH... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        bancodeHoras = new BancodeHorasApi();
        bancodeHoras.setPossuiBH(true);
        bancodeHoras.setSaldoDia(Duration.ZERO);
        setSaldoExtras(saidaDia);
        setSaldoAusencias(saidaDia);
        if (bancodeHoras.getCredito() != null) {
            bancodeHoras.setSaldoDia(bancodeHoras.getSaldoDia().plus(bancodeHoras.getCredito()));
        }
        if (bancodeHoras.getDebito() != null) {
            bancodeHoras.setSaldoDia(bancodeHoras.getSaldoDia().minus(bancodeHoras.getDebito()));
        }
        bancodeHorasFechamentoService.setFechamentoHorasNoDia(bancodeHoras);
        return bancodeHoras;
    }

    public void setSaldosAcumuladosBH() {
        //Subtotal
        if (this.calculo.getDiaComSubtotal() != null
                && this.calculo.getDiaComSubtotal().equals(this.calculo.getDiaProcessado())) {
            bancodeHoras.setSaldoAcumuladoCredito(this.calculo.getCreditoAcumuladoDiaAnteriorBancoDeHoras());
            bancodeHoras.setSaldoAcumuladoDebito(this.calculo.getDebitoAcumuladoDiaAnteriorBancoDeHoras());
            bancodeHoras.setSaldoAcumuladoDia(this.calculo.getSaldoAcumuladoDiaAnteriorBancoDeHoras());
        } //Edição de Saldo 
        else if (this.calculo.getRegrasService().isFechamentoBHEdicaodeSaldo()) {
            bancodeHoras.setSaldoAcumuladoCredito(bancodeHoras.getCreditoFechamento());
            bancodeHoras.setSaldoAcumuladoDebito(bancodeHoras.getDebitoFechamento());
            bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getCreditoFechamento().minus(bancodeHoras.getDebitoFechamento()));
        } //Acerto 
        else if (this.calculo.getRegrasService().isFechamentoBHAcerto()) {
            //Zerar
            if (this.calculo.getBancodeHorasService()
                    .getBancodeHorasFechamentoService().getFechamentoNoDia()
                    .getTipoAcerto().getDescricao().equals(CONSTANTES.FECHAMENTOBH_ACERTO_ZERAR)) {
         
                bancodeHoras.setSaldoAcumuladoDebito(Duration.ZERO);
                bancodeHoras.setSaldoAcumuladoCredito(Duration.ZERO);
                Duration saldoAcumuladoCredito = this.calculo.getCreditoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getCredito().plus(bancodeHoras.getCreditoFechamento()));
                Duration saldoAcumuladoDebito = this.calculo.getDebitoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getDebito().plus(bancodeHoras.getDebitoFechamento()));
                bancodeHoras.setSaldoAcumuladoDia(saldoAcumuladoCredito.minus(saldoAcumuladoDebito));
            
                if (bancodeHoras.getSaldoAcumuladoDia().isNegative()) {
                    bancodeHoras.setSaldoAcumuladoDebito(bancodeHoras.getSaldoAcumuladoDia().multipliedBy(-1));
                } else {
                    bancodeHoras.setSaldoAcumuladoCredito(bancodeHoras.getSaldoAcumuladoDia());
                }
           
            } //Manual
            else if (this.calculo.getBancodeHorasService()
                    .getBancodeHorasFechamentoService().getFechamentoNoDia()
                    .getTipoAcerto().getDescricao().equals(CONSTANTES.FECHAMENTOBH_ACERTO_MANUAL)) {
                bancodeHoras.setSaldoAcumuladoCredito(this.calculo.getCreditoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getCredito().plus(bancodeHoras.getCreditoFechamento())));
                bancodeHoras.setSaldoAcumuladoDebito(this.calculo.getDebitoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getDebito().plus(bancodeHoras.getDebitoFechamento())));
                bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getSaldoAcumuladoCredito().minus(bancodeHoras.getSaldoAcumuladoDebito()));
            } //Gatilhos
            else {
                bancodeHoras.setSaldoAcumuladoCredito(this.calculo.getCreditoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getCredito().minus(bancodeHoras.getDebitoFechamento())));
                bancodeHoras.setSaldoAcumuladoDebito(this.calculo.getDebitoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getDebito().minus(bancodeHoras.getCreditoFechamento())));
                bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getSaldoAcumuladoCredito().minus(bancodeHoras.getSaldoAcumuladoDebito()));
            }
        } //Segundo dia em diante 
        else if (this.calculo.getSaldoAcumuladoDiaAnteriorBancoDeHoras() != null) {
            //Saldo acumulado arredondado
            //bancodeHoras.setSaldoAcumuladoDia(Utils.getArredondamento(this.calculo.getSaldoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getSaldoDia())));
            bancodeHoras.setSaldoAcumuladoDia(this.calculo.getSaldoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getSaldoDia()));
            bancodeHoras.setSaldoAcumuladoCredito(this.calculo.getCreditoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getCredito()));
            bancodeHoras.setSaldoAcumuladoDebito(this.calculo.getDebitoAcumuladoDiaAnteriorBancoDeHoras().plus(bancodeHoras.getDebito()));
        } //Primeiro dia 
        else {
            //Saldo acumulado arredondado
            //bancodeHoras.setSaldoAcumuladoDia(Utils.getArredondamento(bancodeHoras.getSaldoDia()));
            bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getSaldoDia());
            bancodeHoras.setSaldoAcumuladoCredito(bancodeHoras.getCredito());
            bancodeHoras.setSaldoAcumuladoDebito(bancodeHoras.getDebito());
        }

        if (bancodeHoras.getSaldoAcumuladoDia().isNegative()) {
            bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getSaldoAcumuladoDia().plus(bancodeHoras.getGatilho()));
        } else {
            bancodeHoras.setSaldoAcumuladoDia(bancodeHoras.getSaldoAcumuladoDia().minus(bancodeHoras.getGatilho()));
        }

        setArmazenaSaldosBH();
    }

    private void setArmazenaSaldosBH() {
        Duration saldoDia = bancodeHoras.getSaldoDia();
        Duration saldoAcumuladoDiaAnterior = this.calculo.getSaldoAcumuladoDiaAnteriorBancoDeHoras();

        //Armazena o saldo do primeiro dia
        if (this.calculo.isPeriodoSaldoPrimeiroDiaBancodeHoras() && saldoDia != null) {
            this.calculo.getSaldoDiaBancodeHoras().setSaldoPrimeiroDiaBancodeHoras(saldoDia);
        }

        //Armazena o saldo anterior ao primeiro dia do período que está sendo gerado o relatório
        if (this.calculo.isPeriodoSaldoAnteriorDiaPeriodoBancodeHoras() && saldoDia != null) {
            this.calculo.getSaldoDiaBancodeHoras().setSaldoAnteriorDiaPeriodoBancodeHoras(saldoDia);
        }

        //Armazena o saldo que estava acumulado para esse dia
        bancodeHoras.setSaldoAcumuladoDiaAnterior(
                saldoAcumuladoDiaAnterior != null
                        ? saldoAcumuladoDiaAnterior
                        : Duration.ZERO);

        //Atualizar a variável de saldo acumulado para o cálculo do próximo dia
        if (!this.calculo.isPeriodoSaldoPrimeiroDiaBancodeHoras()) {
            this.calculo.setSaldoAcumuladoDiaAnteriorBancoDeHoras(bancodeHoras.getSaldoAcumuladoDia());
            this.calculo.setCreditoAcumuladoDiaAnteriorBancoDeHoras(bancodeHoras.getSaldoAcumuladoCredito());
            this.calculo.setDebitoAcumuladoDiaAnteriorBancoDeHoras(bancodeHoras.getSaldoAcumuladoDebito());
        }
    }

    /**
     * Se tiver saldo de extras pode virar crédito COM ACRÉSCIMOS dependendo do
     * limite cadastro no B.H.
     *
     * @param saidaDia
     */
    private void setSaldoExtras(SaidaDia saidaDia) {
        if (saidaDia.getSaldoExtras() != null
                && saidaDia.getSaldoExtras().isPossui()) {
            LOGGER.debug("Possui crédito para o BH iniciando cálculo acréscimos... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            bancodeHorasAcrescimosService.getCalculaAcrescimos(saidaDia, bancodeHoras);
        } else {
            bancodeHoras.setCredito(Duration.ZERO);
        }
        //Se teve horas compensadas nesse dia (PC) e possui BH Diario
        if (!this.calculo.isPeriodoSaldoPrimeiroDiaBancodeHoras()
                && this.calculo.getFuncionarioService()
                .getFuncionarioCompensacoesService().getCompensandoDia().getIdCompensacao() != null
                && this.calculo.getRegrasService().isBancoDeHoras()
                && this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService() != null
                && this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras().getBancoHoras().getTipoLimiteDiario()) {
            LOGGER.debug("Possui horas compensadas nesse dia para o BH iniciando cálculo acréscimos... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            bancodeHorasAcrescimosService.getCalculaAcrescimosCompensadas(saidaDia, bancodeHoras);
        }
        bancodeHorasAcrescimosService.setTabelaCreditoSaldoUnico();
    }

    private void setSaldoAusencias(SaidaDia saidaDia) {
        if (saidaDia.getSaldoAusencias() != null
                && saidaDia.getSaldoAusencias().isPossui()) {
            LOGGER.debug("Possui débito para o BH, processando regras.... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            getCalculaAusencias(saidaDia);
        } else {
            bancodeHoras.setDebito(Duration.ZERO);
        }
    }

    /**
     * Se tiver saldo de ausências pode virar débito
     *
     * @param saidaDia
     */
    private void getCalculaAusencias(SaidaDia saidaDia) {
        if ((this.calculo.getRegrasService().isFalta()
                && funcionarioService.getFuncionarioBancodeHorasService().getBancoDeHoras().getTrataFaltaDebito())
                || ((saidaDia.getSaldoAusencias().isPossui()
                && funcionarioService.getFuncionarioBancodeHorasService().getBancoDeHoras().getTrataAusenciaDebito()))) {

            //Possui falta e trata falta como débito 
            bancodeHoras.setSaldoAusenciasDiurnas(saidaDia.getSaldoAusencias().getDiurnas());

            //Zera as ausências pois foi tudo pra banco
            saidaDia.getSaldoAusencias().setDiurnas(Duration.ZERO);

            //Ajuste do percentual noturno de acordo com a configuração do banco de horas
            ajustePercentualNoturno(saidaDia);

            //Acréscimos para as horas de BH que foram compensadas para esse dia
            //De acordo com o cadastro de percentuais de acréscimo
            //Essas horas eram para ser ausências...
            //bancodeHorasAcrescimosService.getCalculaAcrescimosCompensadas(saidaDia, bancodeHoras);
            setAbono(saidaDia);

        }
    }

    /**
     * Quando é afastamento com abono não precisa retirar a diferença do
     * adicional noturno, é abonado tudo
     *
     * @param saidaDia
     */
    private void ajustePercentualNoturno(SaidaDia saidaDia) {
        if (this.calculo.getRegrasService().isAdicionalNoturno()
                && funcionarioService.getFuncionarioBancodeHorasService().getBancoDeHoras().getNaoPagaAdicionalNoturnoBH()
                && !this.calculo.getRegrasService().isAfastadoComAbono()
                && saidaDia.getSaldoAusencias().getNoturnas() != null
                && !saidaDia.getSaldoAusencias().getNoturnas().isZero()) {

            if (this.calculo.getRegrasService().isFalta()) {
                bancodeHoras.setSaldoAusenciasNoturnas(saidaDia.getSaldoAusencias().getNoturnas().minus(saidaDia.getHorasPrevistas().getDiferencaadicionalnoturno()));
            } else {
                bancodeHoras.setSaldoAusenciasNoturnas(saidaDia.getSaldoAusencias().getNoturnas().minus(saidaDia.getHorasTrabalhadas().getDiferencaadicionalnoturno()));
            }

            //Deixa em ausências somente a diferença do adicional noturno
            saidaDia.getSaldoAusencias().setNoturnas(saidaDia.getSaldoAusencias().getNoturnas().minus(bancodeHoras.getSaldoAusenciasNoturnas()));

        } else {
            bancodeHoras.setSaldoAusenciasNoturnas(saidaDia.getSaldoAusencias().getNoturnas());

            //Zera as ausências pois foi tudo pra banco
            saidaDia.getSaldoAusencias().setNoturnas(Duration.ZERO);
        }
    }

    /**
     * Possui abono e trata abono como débito
     *
     * @param saidaDia
     */
    private void setAbono(SaidaDia saidaDia) {
        if (this.calculo.getRegrasService().isAbonoSemSomenteJustificativa()
                && (funcionarioService.getFuncionarioBancodeHorasService().getBancoDeHoras().getTrataAbonoDebito()
                || (funcionarioService.getFuncionarioAbonoService().getAbono().getDescontaBH()))) {

            //Possui abono e trata abono como débito
            bancodeHoras.setSaldoAusenciasDiurnas(bancodeHoras.getSaldoAusenciasDiurnas().plus(saidaDia.getSaldoAusencias().getAbonoDia().getDiurnas()));
            bancodeHoras.setSaldoAusenciasNoturnas(bancodeHoras.getSaldoAusenciasNoturnas().plus(saidaDia.getSaldoAusencias().getAbonoDia().getNoturnas()));
        }
        bancodeHoras.setDebito(bancodeHoras.getSaldoAusenciasDiurnas().plus(bancodeHoras.getSaldoAusenciasNoturnas()));
    }
}
