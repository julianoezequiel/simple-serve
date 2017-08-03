package com.topdata.toppontoweb.services.gerafrequencia.services.bancodehoras;

import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasAcrescimosApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.TabelaCreditoSaldoUnico;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Realiza todos os acréscimos no saldo de crédito do banco de horas de acordo
 * com a tabela de percentuais cadastrada
 *
 * @author enio.junior
 */
public class BancodeHorasAcrescimosService {

    public final static Logger LOGGER = LoggerFactory.getLogger(BancodeHorasAcrescimosService.class.getName());
    private final BancodeHorasAcrescimosApi bancodeHorasAcrescimos;
    private final Calculo calculo;
    private final SaldosBHMensal saldosBHMensal;
    private BancodeHorasApi bancodeHoras;
    private List<SequenciaPercentuais> sequenciaPercentuaisList;
    private List<SequenciaPercentuais> sequenciaPercentuaisSaldoMensalList;
    private List<SequenciaPercentuais> sequenciaPercentuaisNoturnosList;
    private boolean tipoLimiteDiario;
    private BancoHoras bancoHoras;
    private Duration compensadasDiurnas;
    private Duration compensadasNoturnas;
    private Duration diferencaAcrescimoDiurnas;
    private Duration diferencaAcrescimoDiurnasACompensar;
    private Duration diferencaAcrescimoNoturnas;
    private Duration diferencaAcrescimoNoturnasACompensar;
    private Duration somaDiurnasTabelaAcrescimosBH;
    private Duration somaNoturnasTabelaAcrescimosBH;
    private Duration somaCompensadasTabelaAcrescimosBH;

    public BancodeHorasAcrescimosService(Calculo calculo) {
        this.bancodeHorasAcrescimos = new BancodeHorasAcrescimosApi();
        this.calculo = calculo;
        this.saldosBHMensal = this.calculo.getSaldosBHMensal();
    }

    public void getCalculaAcrescimos(SaidaDia saidaDia, BancodeHorasApi bancodeHoras) {
        this.sequenciaPercentuaisList = this.calculo.getFuncionarioService()
                .getFuncionarioBancodeHorasService().getPercentuaisTipoDiaBancoDeHoras(
                        this.calculo.getRegrasService().isFeriado(), this.calculo.getRegrasService().isRealizaCompensacao()); //Calculo.getRegrasService().isDiaCompensadoSemSomenteJustificativa()
        this.sequenciaPercentuaisNoturnosList = this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getPercentuaisSomenteNoturnosBancoDeHoras();
        this.bancoHoras = this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getBancoDeHoras();
        this.tipoLimiteDiario = this.bancoHoras.getTipoLimiteDiario();

        //Cria controle quando tipo de bh mensal
        if (!this.tipoLimiteDiario) {
            LOGGER.debug("BH tipo mensal... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            inicializaSaldosBHMensal();
        } else {
            LOGGER.debug("BH tipo diário... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        }

        this.bancodeHoras = bancodeHoras;
        this.bancodeHorasAcrescimos.setBancodeHoras(bancodeHoras);
        bancodeHoras.setTotalAcrescimosDiurnas(Duration.ZERO);
        bancodeHoras.setTotalAcrescimosNoturnas(Duration.ZERO);
        bancodeHoras.setSaldoExtrasDiurnas(saidaDia.getSaldoExtras().getDiurnas());

        //Ajustar percentual Noturno
        getAjustePercentualNoturno(saidaDia.getSaldoExtras());

        //Enquadrar dentro dos limites do dia
        setEnquadrarLimites();

        //Seta de fato quanto das horas extras vão para o banco de horas (Dentro dos limites)
        //O restante continua sendo hora extra
        if (bancodeHoras.getSaldoExtrasDiurnas() != null) {
            saidaDia.getSaldoExtras().setDiurnas(saidaDia.getSaldoExtras().getDiurnas().minus(bancodeHoras.getSaldoExtrasDiurnas()));
        }
        if (bancodeHoras.getSaldoExtrasNoturnas() != null) {
            saidaDia.getSaldoExtras().setNoturnas(saidaDia.getSaldoExtras().getNoturnas().minus(bancodeHoras.getSaldoExtrasNoturnas()));
        }
        saidaDia.setTabelaExtrasList(this.calculo.getFuncionarioService().getFuncionarioJornadaService().getTabelaExtrasList(saidaDia.getSaldoExtras()));

        //Realiza as operações de crédito
        setAcrescimos(bancodeHoras);
    }

    public void getCalculaAcrescimosCompensadas(SaidaDia saidaDia, BancodeHorasApi bancodeHoras) {
        this.bancodeHoras = bancodeHoras;
        this.bancodeHorasAcrescimos.setBancodeHoras(bancodeHoras);

        this.sequenciaPercentuaisList = this.calculo.getFuncionarioService()
                .getFuncionarioBancodeHorasService()
                .getPercentuaisTipoDiaBancoDeHorasCompensadas();

        if (this.bancodeHorasAcrescimos.getTabelaTipoDiaList() == null) {
            this.bancodeHorasAcrescimos.setTabelaTipoDiaList(new ArrayList<>());
        }

        Date dataCompensada = this.calculo.getFuncionarioService()
                .getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada();

        SaldoDiaCompensado saldoDiaCompensado = Utils.getSaldoDiaCompensado(dataCompensada, this.calculo.getEntradaAPI().getSaldoDiaCompensadoList());
        setDiurnasCompensadas(saldoDiaCompensado);
        setNoturnasCompensadas(saldoDiaCompensado);

    }

    private void setDiurnasCompensadas(SaldoDiaCompensado saldoDiaCompensado) {
        this.compensadasDiurnas = saldoDiaCompensado.getCompensadasDiurnas();
        this.diferencaAcrescimoDiurnas = compensadasDiurnas;
        this.diferencaAcrescimoDiurnasACompensar = saldoDiaCompensado.getACompensarDiurnas();

        if (this.sequenciaPercentuaisList.size() > 0) {
            this.sequenciaPercentuaisList
                    .stream()
                    .sorted(new Utils.ordenaSequencia())
                    .forEach(new acrescimosDiurnasCompensadas());

        } else {
            //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
            setTabelaPercentuaisTipoDiaCompensado(null);
            this.bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(this.compensadasDiurnas);
            this.bancodeHorasAcrescimos.getTabelaTipoDiaList().add(this.bancodeHorasAcrescimos.getTabela());
        }

        //Atualizar a lista de dias que compensaram com os acréscimos realizados
        //tipo de dia compensado
        saldoDiaCompensado.setCompensadasDiurnas(this.diferencaAcrescimoDiurnas);
        saldoDiaCompensado.setACompensarDiurnas(this.diferencaAcrescimoDiurnasACompensar);
    }

    private class acrescimosDiurnasCompensadas implements Consumer<SequenciaPercentuais> {

        public acrescimosDiurnasCompensadas() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentual) {
            setTabelaPercentuaisTipoDiaCompensado(sequenciaPercentual);

            //CAMPO ACIMA 24 HORAS
            //Horas Diurnas por limites para o cálculo de percentuais
            if (compensadasDiurnas.toMillis() > 0) {

                Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentual.getHoras());

                if (compensadasDiurnas.toMillis() > Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(Utils.getHorasAcima24horas(horas.toInstant()));
                    compensadasDiurnas = compensadasDiurnas.minus(Utils.getHorasAcima24horas(horas.toInstant()));
                } else {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(compensadasDiurnas);
                    compensadasDiurnas = compensadasDiurnas.minus(bancodeHoras.getSaldoAusenciasDiurnas());
                }

                //Somente tipo diário multiplicar pelos acréscimos
                Duration horasComAcrescimo = Utils.getAcrescimoPercentualTabelasPercentuais(bancodeHorasAcrescimos.getTabela().getDivisaoDiurnas(), bancodeHorasAcrescimos.getTabela().getAcrescimo());
                Duration acrescimoRealizado = horasComAcrescimo.minus(bancodeHorasAcrescimos.getTabela().getDivisaoDiurnas());

                //Total compensado com os acréscimos
                //Pega somente a diferença e soma no que já está lá
                diferencaAcrescimoDiurnas = diferencaAcrescimoDiurnas.plus(acrescimoRealizado);
                diferencaAcrescimoDiurnasACompensar = diferencaAcrescimoDiurnasACompensar.minus(acrescimoRealizado);
                if (diferencaAcrescimoDiurnasACompensar.isNegative()) {
                    diferencaAcrescimoDiurnasACompensar = Duration.ZERO;
                }

                bancodeHorasAcrescimos.getTabela().setResultadoDiurnas(horasComAcrescimo);
                bancodeHoras.setTotalAcrescimosDiurnas(bancodeHoras.getTotalAcrescimosDiurnas().plus(bancodeHorasAcrescimos.getTabela().getResultadoDiurnas()));
                bancodeHorasAcrescimos.getTabelaTipoDiaList().add(bancodeHorasAcrescimos.getTabela());

            }
        }
    }

    /**
     *
     * @param saldoDiaCompensado
     */
    private void setNoturnasCompensadas(SaldoDiaCompensado saldoDiaCompensado) {
        this.compensadasNoturnas = saldoDiaCompensado.getCompensadasNoturnas();
        this.diferencaAcrescimoNoturnas = this.compensadasNoturnas;
        this.diferencaAcrescimoNoturnasACompensar = saldoDiaCompensado.getACompensarNoturnas();
        this.bancodeHorasAcrescimos.getTabelaTipoDiaList()
                .stream()
                .sorted(new Utils.ordenaSequencia())
                .forEach(new AcrescimosNoturnos());
        //Atualizar a lista de dias que compensaram com os acréscimos realizados
        //tipo de dia compensado
        saldoDiaCompensado.setCompensadasNoturnas(this.diferencaAcrescimoNoturnas);
        saldoDiaCompensado.setACompensarNoturnas(this.diferencaAcrescimoNoturnasACompensar);
    }

    /**
     * Tabela acréscimos noturnos de compensação
     */
    private class AcrescimosNoturnos implements Consumer<TabelaSequenciaPercentuais> {

        public AcrescimosNoturnos() {
        }

        @Override
        public void accept(TabelaSequenciaPercentuais tabelaTipoDia) {
            if (sequenciaPercentuaisList.size() > 0) {
                //CAMPO ACIMA 24 HORAS
                //Horas Noturnas por limites para o cálculo de percentuais

                Date horas = Utils.getDataAjustaDiferenca3horas(tabelaTipoDia.getHoras());

                if (compensadasNoturnas.toMillis() > 0
                        && tabelaTipoDia.getDivisaoDiurnas().toMillis()
                        < Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    if (compensadasNoturnas.toMillis()
                            > Utils.getHorasAcima24horas(horas.toInstant()).minus(tabelaTipoDia.getDivisaoNoturnas()).toMillis()) {
                        tabelaTipoDia.setDivisaoNoturnas(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabelaTipoDia.getDivisaoNoturnas()));
                        compensadasNoturnas = compensadasNoturnas.minus(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabelaTipoDia.getDivisaoNoturnas()));
                    } else {
                        tabelaTipoDia.setDivisaoNoturnas(bancodeHoras.getSaldoExtrasNoturnas());
                        compensadasNoturnas = compensadasNoturnas.minus(bancodeHoras.getSaldoExtrasNoturnas());
                    }

                    //Somente tipo diário realizar os acréscimos
                    Duration horasComAcrescimo = Utils.getAcrescimoPercentualTabelasPercentuais(tabelaTipoDia.getDivisaoNoturnas(), tabelaTipoDia.getAcrescimo());
                    Duration acrescimoRealizado = horasComAcrescimo.minus(tabelaTipoDia.getDivisaoNoturnas());

                    //Total compensado com os acréscimos
                    //Pega somente a diferença e soma no que já está lá
                    diferencaAcrescimoNoturnas = diferencaAcrescimoNoturnas.plus(acrescimoRealizado);
                    diferencaAcrescimoNoturnasACompensar = diferencaAcrescimoNoturnasACompensar.minus(acrescimoRealizado);
                    if (diferencaAcrescimoNoturnasACompensar.isNegative()) {
                        diferencaAcrescimoNoturnasACompensar = Duration.ZERO;
                    }

                    tabelaTipoDia.setResultadoNoturnas(horasComAcrescimo);
                    bancodeHoras.setTotalAcrescimosNoturnas(bancodeHoras.getTotalAcrescimosNoturnas().plus(tabelaTipoDia.getResultadoNoturnas()));

                }
            } else {
                //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
                tabelaTipoDia.setDivisaoNoturnas(compensadasNoturnas);
                bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
            }
        }
    }

    /**
     * Verifica se existe o controle de saldos bh mensal do banco de horas do
     * funcionário
     */
    private void inicializaSaldosBHMensal() {
        if (this.sequenciaPercentuaisList.size() > 0
                && !this.calculo.isPeriodoSaldoPrimeiroDiaBancodeHoras()
                && !this.tipoLimiteDiario) { //BH Mensal

            if (this.saldosBHMensal.getBancoHoras() == null
                    || (this.saldosBHMensal.getBancoHoras() != null
                    && !this.saldosBHMensal.getBancoHoras().getIdBancoHoras().equals(this.bancoHoras.getIdBancoHoras()))) {

                //Não possui, então cria o controle de saldos do bh mensal 
                this.saldosBHMensal.setBancoHoras(bancoHoras);
            }
            sequenciaPercentuaisSaldoMensalList = this.saldosBHMensal.getPercentuaisTipoDiaBancoDeHoras(this.calculo.getRegrasService().isFeriado(), this.calculo.getRegrasService().isRealizaCompensacao());
        }

    }

    private void setEnquadrarLimites() {
        this.bancodeHorasAcrescimos.setLimiteDia(Utils.getHorasAcima24horas(this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getLimiteDiaBancoDeHoras(
                this.calculo.getRegrasService().getIdTipoDia()).toInstant()));
        if (getOrdenacao().equals(CONSTANTES.PRIMEIRO_DIURNAS)) {
            this.bancodeHoras.setSaldoExtrasDiurnas(getEnquadraLimiteDia(this.bancodeHoras.getSaldoExtrasDiurnas()));
            this.bancodeHoras.setSaldoExtrasNoturnas(getEnquadraLimiteDia(this.bancodeHoras.getSaldoExtrasNoturnas()));
        } else {
            this.bancodeHoras.setSaldoExtrasNoturnas(getEnquadraLimiteDia(this.bancodeHoras.getSaldoExtrasNoturnas()));
            this.bancodeHoras.setSaldoExtrasDiurnas(getEnquadraLimiteDia(this.bancodeHoras.getSaldoExtrasDiurnas()));
        }
    }

    private Duration getEnquadraLimiteDia(Duration saldoCredito) {
        if (saldoCredito.toMillis() > 0 && this.bancodeHorasAcrescimos.getLimiteDia().toMillis() > 0) {
            if (saldoCredito.toMillis() > this.bancodeHorasAcrescimos.getLimiteDia().toMillis()) {
                saldoCredito = this.bancodeHorasAcrescimos.getLimiteDia();
                this.bancodeHorasAcrescimos.setLimiteDia(Duration.ZERO);
            } else {
                this.bancodeHorasAcrescimos.setLimiteDia(this.bancodeHorasAcrescimos.getLimiteDia().minus(saldoCredito));
            }
        } else {
            saldoCredito = Duration.ZERO;
        }
        return saldoCredito;
    }

    /**
     * Ajuste do percentual noturno de acordo com a configuração do banco de
     * horas
     *
     * @param saldoExtras
     */
    private void getAjustePercentualNoturno(SaldoExtras saldoExtras) {
        if (this.calculo.getRegrasService().isAdicionalNoturno()
                && this.bancoHoras.getNaoPagaAdicionalNoturnoBH()
                && !saldoExtras.getNoturnas().isZero()) {
            this.bancodeHoras.setSaldoExtrasNoturnas(Utils.getRetirarPercentual(saldoExtras.getNoturnas(), this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada().getPercentualAdicionalNoturno()));
        } else {
            bancodeHoras.setSaldoExtrasNoturnas(saldoExtras.getNoturnas());
        }
    }

    /**
     * Se as horas diurnas previstas da jornada for maior que as noturnas ou
     * tudo zerado Então primeiro Diurnas, Senão primeiro Noturnas
     *
     * @return
     */
    private String getOrdenacao() {
        Duration diurnas = this.calculo.getSaldosService().getTotalHorasPrevistas().getDiurnas();
        Duration noturnas = this.calculo.getSaldosService().getTotalHorasPrevistas().getNoturnas();
        if (diurnas.toMillis() > noturnas.toMillis()
                || (diurnas == Duration.ZERO && noturnas == Duration.ZERO)) {
            return CONSTANTES.PRIMEIRO_DIURNAS;
        } else {
            return CONSTANTES.PRIMEIRO_NOTURNAS;
        }
    }

    private void setAcrescimos(BancodeHorasApi bancodeHoras) {
        getTabelaDiurnas();
        getTabelaNoturnas();
        bancodeHoras.setAcrescimosTipoDiaList(this.bancodeHorasAcrescimos.getTabelaTipoDiaList());
        getTabelaSomenteNoturnas();
        bancodeHoras.setAcrescimosSomenteNoturnasList(this.bancodeHorasAcrescimos.getTabelaNoturnasList());
        bancodeHoras.setTabelaTiposdeDia(this.bancodeHorasAcrescimos.getTabelaTipoDiaList());

        //bancodeHoras.setCredito(Utils.getArredondamento(bancodeHoras.getTotalAcrescimosDiurnas().plus(bancodeHoras.getTotalAcrescimosNoturnas())));
        bancodeHoras.setCredito(Utils.getSomenteHorasMinutos(bancodeHoras.getTotalAcrescimosDiurnas()).plus(Utils.getSomenteHorasMinutos(bancodeHoras.getTotalAcrescimosNoturnas())));

        if (!bancodeHoras.getSaldoExtrasDiurnas().plus(bancodeHoras.getSaldoExtrasNoturnas()).isZero()) {
            setCriarUltimoPercentual();
        }
    }

    public void setTabelaCreditoSaldoUnico() {
        setTotaisCreditoSaldoUnico();
        List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList = new ArrayList<>();
        this.calculo.getTipoDiaList()
                .stream()
                .forEach(new criarSaldosTipoDia(tabelaCreditoSaldoUnicoList));
        if (this.bancodeHoras != null) {
            this.bancodeHoras.setTabelaCreditoSaldoUnicoList(tabelaCreditoSaldoUnicoList);
        }
    }

    private class criarSaldosTipoDia implements Consumer<TipoDia> {

        private final List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList;

        public criarSaldosTipoDia(List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList) {
            this.tabelaCreditoSaldoUnicoList = tabelaCreditoSaldoUnicoList;
        }

        @Override
        public void accept(TipoDia tipoDia) {
            TabelaCreditoSaldoUnico creditoSaldoUnico = new TabelaCreditoSaldoUnico();
            creditoSaldoUnico.setTipoDia(tipoDia);
            creditoSaldoUnico.setSaldo(Duration.ZERO);

            if (calculo.getRegrasService().getIdTipoDia() == tipoDia.getIdTipodia()) {
                creditoSaldoUnico.setSaldo(somaDiurnasTabelaAcrescimosBH);
            }
            if (tipoDia.getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO)) {
                creditoSaldoUnico.setSaldo(somaCompensadasTabelaAcrescimosBH);
            }
            if (tipoDia.getDescricao().equals(CONSTANTES.TIPODIA_HORAS_NOTURNAS)) {
                creditoSaldoUnico.setSaldo(somaNoturnasTabelaAcrescimosBH);
            }
            tabelaCreditoSaldoUnicoList.add(creditoSaldoUnico);
        }
    }

    private void setTotaisCreditoSaldoUnico() {
        if (this.bancodeHoras != null && this.bancodeHoras.getTabelaTiposdeDia() != null) {
            this.somaDiurnasTabelaAcrescimosBH = Duration.ZERO;
            this.bancodeHoras.getTabelaTiposdeDia()
                    .stream()
                    .filter(f -> !f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaDiurnasTabelaAcrescimosBH = this.somaDiurnasTabelaAcrescimosBH.plus(tabela.getResultadoDiurnas());
                    });

            this.somaNoturnasTabelaAcrescimosBH = Duration.ZERO;
            this.bancodeHoras.getTabelaTiposdeDia()
                    .stream()
                    .filter(f -> !f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaNoturnasTabelaAcrescimosBH = this.somaNoturnasTabelaAcrescimosBH.plus(tabela.getResultadoNoturnas());
                    });

            this.somaCompensadasTabelaAcrescimosBH = Duration.ZERO;
            this.bancodeHoras.getTabelaTiposdeDia()
                    .stream()
                    .filter(f -> f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaCompensadasTabelaAcrescimosBH = this.somaCompensadasTabelaAcrescimosBH.plus(tabela.getResultadoDiurnas().plus(tabela.getResultadoNoturnas()));
                    });
        }
    }

    private void getTabelaDiurnas() {
        LOGGER.debug("Processando tabela de acréscimos diurnas BH... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        this.bancodeHorasAcrescimos.setTabelaTipoDiaList(new ArrayList<>());
        //Se BH diário
        if (this.tipoLimiteDiario) {
            if (this.sequenciaPercentuaisList.size() > 0) {
                this.sequenciaPercentuaisList
                        .stream()
                        .sorted(new Utils.ordenaSequencia())
                        .forEach(new acrescimosDiurnasLimiteDiario());
            } else {
                AlocaLimite1SemAcrescimos();
            }
        } else //É BH mensal
        //Quando BH Mensal ignora o tipo de dia compensado
        {
            if (this.sequenciaPercentuaisSaldoMensalList != null && this.sequenciaPercentuaisSaldoMensalList.size() > 0) {
                this.sequenciaPercentuaisSaldoMensalList
                        .stream()
                        .sorted(new Utils.ordenaSequencia())
                        .forEach(new acrescimosDiurnasLimiteMensal());
            } else {
                AlocaLimite1SemAcrescimos();
            }
        }
    }

    /**
     * Como não tem limite cadastrado, aloca todas as horas no 1 limite sem
     * acréscimos
     */
    private void AlocaLimite1SemAcrescimos() {
        setTabelaPercentuaisTipoDia(null);
        this.bancodeHorasAcrescimos.getTabela().setResultadoDiurnas(this.bancodeHoras.getSaldoExtrasDiurnas());
        this.bancodeHorasAcrescimos.getTabela().setResultadoNoturnas(this.bancodeHoras.getSaldoExtrasNoturnas());
        this.bancodeHoras.setTotalAcrescimosDiurnas(this.bancodeHoras.getSaldoExtrasDiurnas());
        this.bancodeHoras.setTotalAcrescimosNoturnas(this.bancodeHoras.getSaldoExtrasNoturnas());
        this.bancodeHoras.setSaldoExtrasDiurnas(Duration.ZERO);
        this.bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
        this.bancodeHorasAcrescimos.getTabelaTipoDiaList().add(this.bancodeHorasAcrescimos.getTabela());
    }

    private class acrescimosDiurnasLimiteDiario implements Consumer<SequenciaPercentuais> {

        public acrescimosDiurnasLimiteDiario() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            setTabelaPercentuaisTipoDia(sequenciaPercentuais);
            //CAMPO ACIMA 24 HORAS
            //Horas Diurnas por limites para o cálculo de percentuais

            Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras());

            if (bancodeHoras.getSaldoExtrasDiurnas().toMillis() > 0) {
                if (bancodeHoras.getSaldoExtrasDiurnas().toMillis() > Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(Utils.getHorasAcima24horas(horas.toInstant()));
                    bancodeHoras.setSaldoExtrasDiurnas(bancodeHoras.getSaldoExtrasDiurnas().minus(Utils.getHorasAcima24horas(horas.toInstant())));
                } else {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(bancodeHoras.getSaldoExtrasDiurnas());
                    bancodeHoras.setSaldoExtrasDiurnas(Duration.ZERO);
                }
                //Somente tipo diário multiplicar pelos acréscimos
                bancodeHorasAcrescimos.getTabela().setResultadoDiurnas(Utils.getAcrescimoPercentualTabelasPercentuais(bancodeHorasAcrescimos.getTabela().getDivisaoDiurnas(), sequenciaPercentuais.getAcrescimo()));
                bancodeHoras.setTotalAcrescimosDiurnas(bancodeHoras.getTotalAcrescimosDiurnas().plus(bancodeHorasAcrescimos.getTabela().getResultadoDiurnas()));
                bancodeHorasAcrescimos.getTabelaTipoDiaList().add(bancodeHorasAcrescimos.getTabela());
            }
        }
    }

    private class acrescimosDiurnasLimiteMensal implements Consumer<SequenciaPercentuais> {

        public acrescimosDiurnasLimiteMensal() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            setTabelaPercentuaisTipoDia(sequenciaPercentuais);
            //CAMPO ACIMA 24 HORAS
            //Horas Diurnas por limites para o cálculo de percentuais

            Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras());

            if (bancodeHoras.getSaldoExtrasDiurnas().toMillis() > 0
                    && Utils.getHorasAcima24horas(horas.toInstant()).toMillis() > 0) {
                if (bancodeHoras.getSaldoExtrasDiurnas().toMillis() > Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(Utils.getHorasAcima24horas(horas.toInstant()));
                    bancodeHoras.setSaldoExtrasDiurnas(bancodeHoras.getSaldoExtrasDiurnas().minus(Utils.getHorasAcima24horas(horas.toInstant())));
                } else {
                    bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(bancodeHoras.getSaldoExtrasDiurnas());
                    bancodeHoras.setSaldoExtrasDiurnas(Duration.ZERO);
                }
                setHorasExtrasAcumulo(sequenciaPercentuais, bancodeHoras.getSaldoExtrasDiurnas(), bancodeHorasAcrescimos.getTabela().getDivisaoDiurnas());
                //Tipo mensal não realiza os acréscimos, somente separa as horas
                //deve somente separar igual é feito nas extras
                bancodeHorasAcrescimos.getTabela().setResultadoDiurnas(bancodeHorasAcrescimos.getTabela().getDivisaoDiurnas());
                bancodeHoras.setTotalAcrescimosDiurnas(bancodeHoras.getTotalAcrescimosDiurnas().plus(bancodeHorasAcrescimos.getTabela().getResultadoDiurnas()));
                bancodeHorasAcrescimos.getTabelaTipoDiaList().add(bancodeHorasAcrescimos.getTabela());
            } else {
                bancodeHorasAcrescimos.getTabelaTipoDiaList().add(bancodeHorasAcrescimos.getTabela());
            }
        }
    }

    /**
     * Se possui dia de fechamento, verificar se é o dia ou se passou, caso sim,
     * começa novamente do primeiro limite, verifica se tem saldo no limite
     * cadastrado, se não tem, então não faz nada, não gera pro próximo. Senão
     * continua normalmente...
     *
     * @param sequenciaPercentuais
     * @param saldoExtras
     * @param divisoes
     */
    private void setHorasExtrasAcumulo(SequenciaPercentuais sequenciaPercentuais, Duration saldoExtras, Duration divisoes) {
        //Diminuir do saldo disponível limite mensal as horas
        //que foram alocadas acima
        sequenciaPercentuais.setHoras(Utils.FormatoDataHora(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras()).toInstant()).minus(divisoes)));

        if (this.saldosBHMensal.getBancoHoras().getHabilitaDiaFechamento()) {
            Integer diaFechamento = this.saldosBHMensal.getBancoHoras().getDiaFechamentoExtra();
            if (this.calculo.getDiaProcessado().getDate() > diaFechamento) {
                //saldo extras = saldo extras - limite original
                //pois não poderá alocar pro outro período o que era pra ser nesse.
                Duration limiteOriginal = getHorasLimiteOriginal(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
                if (saldoExtras.toMillis() > limiteOriginal.toMillis()) {
                    saldoExtras.minus(limiteOriginal);
                }
            }
        }
    }

    /**
     * Consultar no banco de horas do funcionário o limite original, pois em
     * SaldosBHMensal está o saldo atualizado que ainda pode ser utilizado
     *
     * @param idSequencia
     * @return
     */
    private Duration getHorasLimiteOriginal(Integer idSequencia) {
        Date horas = this.bancoHoras.getPercentuaisAcrescimo()
                .getSequenciaPercentuaisList()
                .stream()
                .filter(f -> (f.getSequenciaPercentuaisPK().getIdSequencia() == idSequencia))
                .map(f -> f.getHoras()).findFirst().orElse(null);
        return Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(horas).toInstant());
    }

    private void getTabelaNoturnas() {
        LOGGER.debug("Processando tabela de acréscimos noturnas BH... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        //Se BH diário
        if (this.tipoLimiteDiario) {
            this.bancodeHorasAcrescimos.getTabelaTipoDiaList()
                    .stream()
                    .sorted(new Utils.ordenaSequencia())
                    .forEach(new acrescimosNoturnasLimiteDiario());
        } else //É BH mensal
        //Quando BH Mensal ignora o tipo de dia compensado
         if (this.sequenciaPercentuaisSaldoMensalList != null && this.sequenciaPercentuaisSaldoMensalList.size() > 0) {
                this.sequenciaPercentuaisSaldoMensalList
                        .stream()
                        .sorted(new Utils.ordenaSequencia())
                        .forEach(new acrescimosNoturnasLimiteMensal());
            }
    }

    private class acrescimosNoturnasLimiteMensal implements Consumer<SequenciaPercentuais> {

        public acrescimosNoturnasLimiteMensal() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            TabelaSequenciaPercentuais atualizaTabela = bancodeHorasAcrescimos.getTabelaTipoDiaList().get(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
            //CAMPO ACIMA 24 HORAS
            //Horas Diurnas por limites para o cálculo de percentuais

            Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras());

            if (bancodeHoras.getSaldoExtrasNoturnas().toMillis() > 0
                    && Utils.getHorasAcima24horas(horas.toInstant()).toMillis() > 0) {

                if (bancodeHoras.getSaldoExtrasNoturnas().toMillis() > Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    atualizaTabela.setDivisaoNoturnas(atualizaTabela.getDivisaoNoturnas().plus(Utils.getHorasAcima24horas(horas.toInstant())));
                    bancodeHoras.setSaldoExtrasNoturnas(bancodeHoras.getSaldoExtrasNoturnas().minus(Utils.getHorasAcima24horas(horas.toInstant())));
                } else {
                    atualizaTabela.setDivisaoNoturnas(atualizaTabela.getDivisaoNoturnas().plus(bancodeHoras.getSaldoExtrasNoturnas()));
                    bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
                }
                setHorasExtrasAcumulo(sequenciaPercentuais, bancodeHoras.getSaldoExtrasNoturnas(), atualizaTabela.getDivisaoNoturnas());
                atualizaTabela.setResultadoNoturnas(atualizaTabela.getDivisaoNoturnas());
                bancodeHoras.setTotalAcrescimosNoturnas(bancodeHoras.getTotalAcrescimosNoturnas().plus(atualizaTabela.getResultadoNoturnas()));
            }
        }
    }

    private class acrescimosNoturnasLimiteDiario implements Consumer<TabelaSequenciaPercentuais> {

        public acrescimosNoturnasLimiteDiario() {
        }

        @Override
        public void accept(TabelaSequenciaPercentuais tabela) {
            if (sequenciaPercentuaisList.size() > 0) {
                //CAMPO ACIMA 24 HORAS
                //Horas Noturnas por limites para o cálculo de percentuais

                Date horas = Utils.getDataAjustaDiferenca3horas(tabela.getHoras());

                if (bancodeHoras.getSaldoExtrasNoturnas().toMillis() > 0
                        && tabela.getDivisaoDiurnas().toMillis()
                        < Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    if (bancodeHoras.getSaldoExtrasNoturnas().toMillis()
                            > Utils.getHorasAcima24horas(horas.toInstant()).minus(tabela.getDivisaoNoturnas()).toMillis()) {
                        tabela.setDivisaoNoturnas(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabela.getDivisaoNoturnas()));
                        bancodeHoras.setSaldoExtrasNoturnas(bancodeHoras.getSaldoExtrasNoturnas().minus(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabela.getDivisaoNoturnas())));
                    } else {
                        tabela.setDivisaoNoturnas(bancodeHoras.getSaldoExtrasNoturnas());
                        bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
                    }
                    //Somente tipo diário realizar os acréscimos
                    tabela.setResultadoNoturnas(Utils.getAcrescimoPercentualTabelasPercentuais(tabela.getDivisaoNoturnas(), tabela.getAcrescimo()));
                    bancodeHoras.setTotalAcrescimosNoturnas(Utils.getSomenteHorasMinutos(bancodeHoras.getTotalAcrescimosNoturnas()).plus(Utils.getSomenteHorasMinutos(tabela.getResultadoNoturnas())));
                }
            } else {
                //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
                tabela.setDivisaoNoturnas(bancodeHoras.getSaldoExtrasNoturnas());
                bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
            }
        }
    }

    private void getTabelaSomenteNoturnas() {
        LOGGER.debug("Processando tabela de acréscimos somente noturnas BH... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        this.bancodeHorasAcrescimos.setTabelaNoturnasList(new ArrayList<>());

        //Horas noturnas somente para BH Diário
        if (this.tipoLimiteDiario) {
            if (this.sequenciaPercentuaisNoturnosList.size() > 0) {
                this.bancodeHoras.setSaldoExtrasNoturnas(bancodeHoras.getTotalAcrescimosNoturnas());
                this.bancodeHoras.setTotalAcrescimosNoturnas(Duration.ZERO);
                this.sequenciaPercentuaisNoturnosList
                        .stream()
                        .sorted(new Utils.ordenaSequencia())
                        .forEach(new acrescimosSomenteNoturnasLimiteDiario());
            } else {
                //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
                setTabelaPercentuaisTipoDia(null);
                this.bancodeHorasAcrescimos.getTabela().setDivisaoNoturnas(this.bancodeHoras.getSaldoExtrasNoturnas());
                this.bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
                this.bancodeHorasAcrescimos.getTabelaNoturnasList().add(this.bancodeHorasAcrescimos.getTabela());
            }
        }
    }

    private class acrescimosSomenteNoturnasLimiteDiario implements Consumer<SequenciaPercentuais> {

        public acrescimosSomenteNoturnasLimiteDiario() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            setTabelaPercentuaisTipoDia(sequenciaPercentuais);

            //Campo acima 24h
            //Horas Noturnas por limites para o cálculo de percentuais
            Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras());

            if (bancodeHoras.getSaldoExtrasNoturnas().toMillis() > 0) {
                if (bancodeHoras.getSaldoExtrasNoturnas().toMillis() > Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {
                    bancodeHorasAcrescimos.getTabela().setDivisaoNoturnas(Utils.getHorasAcima24horas(horas.toInstant()));
                    bancodeHoras.setSaldoExtrasNoturnas(bancodeHoras.getSaldoExtrasNoturnas().minus(Utils.getHorasAcima24horas(horas.toInstant())));
                } else {
                    bancodeHorasAcrescimos.getTabela().setDivisaoNoturnas(bancodeHoras.getSaldoExtrasNoturnas());
                    bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
                }

                //Somente tipo diário realizar os acréscimos
                Duration somenteNoturnasComAcrescimos = Utils.getAcrescimoPercentualTabelasPercentuais(bancodeHorasAcrescimos.getTabela().getDivisaoNoturnas(), sequenciaPercentuais.getAcrescimo());
                Duration somenteAcrescimosCalculados = somenteNoturnasComAcrescimos.minus(bancodeHorasAcrescimos.getTabela().getDivisaoNoturnas());
                bancodeHorasAcrescimos.getTabela().setResultadoNoturnas(somenteNoturnasComAcrescimos);
                bancodeHorasAcrescimos.getTabelaNoturnasList().add(bancodeHorasAcrescimos.getTabela());

                //Atualizar a tabela que unifica tudo
                Integer idSequencia = sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia();

                if (idSequencia <= bancodeHorasAcrescimos.getTabelaTipoDiaList().size() - 1) {
                    
                    Duration totalNoturnas = bancodeHorasAcrescimos.getTabelaTipoDiaList().get(idSequencia)
                                    .getResultadoNoturnas().plus(somenteAcrescimosCalculados);
                    
                    bancodeHorasAcrescimos.getTabelaTipoDiaList().get(idSequencia)
                            .setResultadoNoturnas(totalNoturnas);
                    
                    bancodeHoras.setTotalAcrescimosNoturnas(
                            bancodeHoras.getTotalAcrescimosNoturnas().plus(totalNoturnas));
                
                } else {
                    bancodeHorasAcrescimos.getTabelaTipoDiaList().add(
                            new TabelaSequenciaPercentuais(
                                    Duration.ZERO,
                                    Duration.ZERO,
                                    bancodeHorasAcrescimos.getTabela().getDivisaoNoturnas(),
                                    somenteNoturnasComAcrescimos,
                                    null,
                                    bancodeHorasAcrescimos.getTabela().getAcrescimo(),
                                    idSequencia,
                                    calculo.getRegrasService().getTipoDia()));
                    
                    bancodeHoras.setTotalAcrescimosNoturnas(
                            Utils.getSomenteHorasMinutos(bancodeHoras.getTotalAcrescimosNoturnas())
                                    .plus(Utils.getSomenteHorasMinutos(somenteNoturnasComAcrescimos)));

                }
            }
        }
    }

    private void setTabelaPercentuaisTipoDia(SequenciaPercentuais sequenciaPercentuais) {
        TabelaSequenciaPercentuais tabela = new TabelaSequenciaPercentuais();
        if (sequenciaPercentuais != null) {
            tabela.setTipoDia(sequenciaPercentuais.getTipoDia());
            tabela.setIdSequencia(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
            tabela.setHoras(Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras()));
            tabela.setAcrescimo(sequenciaPercentuais.getAcrescimo());
        } else {
            tabela.setTipoDia(this.calculo.getRegrasService().getTipoDia());
            tabela.setIdSequencia(1);
            tabela.setAcrescimo(0d);
        }
        this.bancodeHorasAcrescimos.setTabela(tabela);
    }

    private void setTabelaPercentuaisTipoDiaCompensado(SequenciaPercentuais sequenciaPercentuais) {
        TabelaSequenciaPercentuais tabela = new TabelaSequenciaPercentuais();
        tabela.setTipoDia(new TipoDia(2, CONSTANTES.TIPODIA_COMPENSADO));
        if (sequenciaPercentuais != null) {
            tabela.setIdSequencia(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
            tabela.setHoras(Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras()));
            tabela.setAcrescimo(sequenciaPercentuais.getAcrescimo());
        } else {
            tabela.setIdSequencia(1);
            tabela.setAcrescimo(0d);
        }
        this.bancodeHorasAcrescimos.setTabela(tabela);
    }

    /**
     * Se efetuou todas as divisões e ainda ficou saldo pois tem mais horas que
     * os limites configurados, então criar um novo com 0%, como não tem limite
     * cadastrado, aloca todas as horas no 1 limite sem acréscimos
     */
    private void setCriarUltimoPercentual() {
        setTabelaPercentuaisTipoDia(null);
        this.bancodeHorasAcrescimos.getTabela().setDivisaoDiurnas(this.bancodeHoras.getSaldoExtrasDiurnas());
        this.bancodeHorasAcrescimos.getTabela().setDivisaoNoturnas(this.bancodeHoras.getSaldoExtrasNoturnas());
        this.bancodeHoras.setSaldoExtrasDiurnas(Duration.ZERO);
        this.bancodeHoras.setSaldoExtrasNoturnas(Duration.ZERO);
        this.bancodeHorasAcrescimos.getTabelaTipoDiaList().add(this.bancodeHorasAcrescimos.getTabela());
    }
}
