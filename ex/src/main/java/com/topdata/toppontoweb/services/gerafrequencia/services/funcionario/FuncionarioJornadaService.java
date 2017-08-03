package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaExtras;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Jornada do funcionário e divisões das extras
 *
 * @author enio.junior
 */
public class FuncionarioJornadaService {

    public final static Logger LOGGER = LoggerFactory.getLogger(FuncionarioJornadaService.class.getName());
    private final Funcionario funcionario;
    private final TabelaExtras tabelaExtras;
    private final SaldosJornadaMensalSemanal jornadaMensalSemanal;
    private final Calculo calculo;
    private final List<Integer> horariosAtualizadosList;
    private FuncionarioJornada funcionarioJornada;
    private Horario horarioDia;
    private List<SequenciaPercentuais> sequenciaPercentuaisList;
    private List<SequenciaPercentuais> sequenciaPercentuaisSaldoMensalSemanalList;

    public FuncionarioJornadaService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionario = this.calculo.getEntradaAPI().getFuncionario();
        this.tabelaExtras = new TabelaExtras();
        this.jornadaMensalSemanal = new SaldosJornadaMensalSemanal(calculo);
        this.horariosAtualizadosList = new ArrayList<>();
    }

    public void setHorarioJornada() {
        this.funcionarioJornada = getJornadaDiaFuncionario();
        this.horarioDia = getHorarioDia();
    }

    /**
     * Jornada do dia ou a primeira anterior, retorna primeiro a exceção se
     * tiver
     *
     * @return
     */
    public FuncionarioJornada getJornadaDiaFuncionario() {
        if (funcionario.getFuncionarioJornadaList() != null) {
            return funcionario.getFuncionarioJornadaList().stream()
                    .filter(f -> (Utils.formatoDataSemHorario(f.getDataInicio()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0
                            && (f.getDataFim() == null || Utils.formatoDataSemHorario(f.getDataFim()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) >= 0)))
                    .sorted(Comparator.comparing(FuncionarioJornada::getDataInicio).reversed())
                    .sorted(Comparator.comparing(FuncionarioJornada::getExcecaoJornada).reversed())
                    .findFirst()
                    .orElse(new FuncionarioJornada());
        }
        return new FuncionarioJornada();
    }

    public Jornada getJornada() {
        if (funcionarioJornada.getIdJornadaFuncionario() != null) {
            return funcionarioJornada.getJornada();
        }
        return new Jornada();
    }

//    private JornadaAPI getJornadaDiaAnterior() {
//        Date diaAnterior = Utils.AdicionaDiasData(calculoClasses.getDiaProcessado(), -1);
//        return funcionarioAPI.getFuncionarioJornadaList()
//                //Jornada do dia anterior ou a primeira anterior
//                //Retorna primeiro a exceção se tiver
//                .stream()
//                .filter(f -> (f.getDataInicio().compareTo(diaAnterior) <= 0))
//                .sorted(Comparator.comparing(FuncionarioJornadaAPI::getDataInicio).reversed())
//                .sorted(Comparator.comparing(FuncionarioJornadaAPI::getExcecaoJornada).reversed())
//                .map(f -> f.getJornada()).findFirst().orElse(null);
//    }
    private long getIntervaloDiasHorariosJornadaFuncionario() {
        return Utils.getQuantidadeDias(this.calculo.getDiaProcessado(), funcionarioJornada.getDataInicio());
    }

    /**
     * Lista dos Horários Jornada inicia em 0, Lista de Marcações do Horário
     * inicia em 1
     *
     * @return
     */
    private Integer getSequenciaInicialHorarioJornadaFuncionario() {
        if (funcionarioJornada.getIdJornadaFuncionario() != null) {
            return funcionarioJornada.getSequenciaInicial();
        }
        return null;
    }

    /**
     * Retorna o horário do dia (identifica pela sequência e quantidade de dias)
     *
     * @return
     */
    public synchronized Horario getHorarioDia() {
        if (getJornada().getJornadaHorarioList() != null) {
            if (getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_LIVRE)) {
                return new Horario();
            }
            if (getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_SEMANAL)) {
                return getJornada().getJornadaHorarioList().get(Utils.RetornaDiadaSemana(this.calculo.getDiaProcessado())).getHorario();
            }
            if (getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_VARIAVEL)) {

                int qtdeHorarios = 0;
                if (getJornada().getJornadaHorarioList().size() - 1 > 0) {
                    qtdeHorarios = getJornada().getJornadaHorarioList().size() - 1;
                }
                Integer sequenciaInicial = getSequenciaInicialHorarioJornadaFuncionario();
                if (sequenciaInicial != null) {
                    //Identificação da sequência do dia
                    long qtdedias = getIntervaloDiasHorariosJornadaFuncionario();
                    int i = 1;
                    if (qtdedias > 0) {
                        while (i <= qtdedias) {
                            if (sequenciaInicial >= qtdeHorarios) {
                                sequenciaInicial = 0;
                            } else {
                                sequenciaInicial++;
                            }
                            i++;
                        }
                    }
                    return getJornada().getJornadaHorarioList().get(sequenciaInicial).getHorario();
                }
            }
        }
        return new Horario();
    }

    public List<HorarioMarcacao> getHorariosJornadaDia() {
        if (this.horarioDia != null && this.horarioDia.getHorarioMarcacaoList() != null) {
            return this.horarioDia.getHorarioMarcacaoList()
                    .stream()
                    .sorted(Comparator.comparing(HorarioMarcacao::getIdSequencia))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Quando jornada livre assume o tipo de dia normal para acréscimos das
     * horas extras
     *
     * @param feriado
     * @param diaCompensado
     * @return
     */
    public List<SequenciaPercentuais> getPercentuaisTipoDiaExtras(boolean feriado, boolean diaCompensado) {
        if (getJornada().getPercentuaisAcrescimo() != null) {
            return getJornada().getPercentuaisAcrescimo()
                    .getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getTipoDia().getIdTipodia().compareTo(this.calculo.getRegrasService().getTipoDia().getIdTipodia()) == 0))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<TabelaSequenciaPercentuais> getTabelaExtrasList(SaldoExtras saldoExtras) {
        LOGGER.debug("Calculando divisões tabela de extras... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        tabelaExtras.setTabelaTipoDiaList(new ArrayList<>());
        tabelaExtras.setSaldoExtrasDiurnas(saldoExtras.getDiurnas());
        tabelaExtras.setSaldoExtrasNoturnas(saldoExtras.getNoturnas());

        Duration totalExtras = tabelaExtras.getSaldoExtrasDiurnas().plus(tabelaExtras.getSaldoExtrasNoturnas());
        if (this.calculo.getRegrasService().isCumprirHorario() && !totalExtras.isZero() ) {
            sequenciaPercentuaisList = getPercentuaisTipoDiaExtras(this.calculo.getRegrasService().isFeriado(), this.calculo.getRegrasService().isDiaCompensadoSemSomenteJustificativa());
            inicializaSaldosJornadaMensalSemanal();
            setDivisoesDiurnas();
            setDivisoesNoturnas();
        }

        //Remover da tabela os acréscimos não utilizados
        Predicate<TabelaSequenciaPercentuais> predicate;
        predicate = p -> (p.getDivisaoDiurnas().plus(p.getDivisaoNoturnas())).isZero();
        tabelaExtras.getTabelaTipoDiaList().removeIf(predicate);
        totalExtras = tabelaExtras.getSaldoExtrasDiurnas().plus(tabelaExtras.getSaldoExtrasNoturnas());
        if (!totalExtras.isZero()) {
            setCriarUltimoPercentual();
        }
        return tabelaExtras.getTabelaTipoDiaList();
    }

    /**
     * Verifica se existe o controle de saldos mensal/semanal da jornada do
     * funcionário
     */
    private void inicializaSaldosJornadaMensalSemanal() {
        if (sequenciaPercentuaisList.size() > 0
                && !this.calculo.isPeriodoSaldoPrimeiroDiaBancodeHoras()
                && getJornada().getHorasExtrasAcumulo() != null
                && !getJornada().getHorasExtrasAcumulo()
                .getDescricao().equals(CONSTANTES.JORNADA_EXTRAS_DIARIO)) {

            if (this.jornadaMensalSemanal.getFuncionarioJornada() == null
                    || (this.jornadaMensalSemanal.getFuncionarioJornada() != null
                    && !this.jornadaMensalSemanal.getFuncionarioJornada().getIdJornadaFuncionario().equals(funcionarioJornada.getIdJornadaFuncionario()))) {

                //Não possui, então cria o controle de saldos da jornanda mensal/semanal
                this.jornadaMensalSemanal.setFuncionarioJornada(funcionarioJornada);
            }
            sequenciaPercentuaisSaldoMensalSemanalList = this.jornadaMensalSemanal.getPercentuaisTipoDiaExtras(this.calculo.getRegrasService().isFeriado(), this.calculo.getRegrasService().isDiaCompensadoSemSomenteJustificativa());
        }

    }

    private synchronized void setDivisoesDiurnas() {
        if (getJornada().getHorasExtrasAcumulo() == null
                || (getJornada().getHorasExtrasAcumulo() != null
                && getJornada().getHorasExtrasAcumulo()
                .getDescricao().equals(CONSTANTES.JORNADA_EXTRAS_DIARIO))) {
            if (sequenciaPercentuaisList.size() > 0) {
                sequenciaPercentuaisList
                        .forEach(new divisoesDiurnasExtras());
            } else {
                AlocaLimite1SemAcrescimos();
            }
        } else if (sequenciaPercentuaisSaldoMensalSemanalList != null && sequenciaPercentuaisSaldoMensalSemanalList.size() > 0) {
            sequenciaPercentuaisSaldoMensalSemanalList
                    .forEach(new divisoesDiurnasExtras());
        } else {
            AlocaLimite1SemAcrescimos();
        }
    }

    /**
     * Como não tem limite cadastrado, aloca todas as horas no 1 limite sem
     * acréscimos
     */
    private void AlocaLimite1SemAcrescimos() {
        setTabelaPercentuaisTipoDia(null);
        tabelaExtras.getTabela().setDivisaoDiurnas(tabelaExtras.getSaldoExtrasDiurnas());
        tabelaExtras.setSaldoExtrasDiurnas(Duration.ZERO);
        tabelaExtras.getTabelaTipoDiaList().add(tabelaExtras.getTabela());
    }

    private class divisoesDiurnasExtras implements Consumer<SequenciaPercentuais> {

        public divisoesDiurnasExtras() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            setTabelaPercentuaisTipoDia(sequenciaPercentuais);
            //Campo acima 24h
            //Horas Diurnas por limites para o cálculo de percentuais
            if (tabelaExtras.getSaldoExtrasDiurnas().toMillis() > 0) {

                Duration horas = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras()).toInstant());

                if (tabelaExtras.getSaldoExtrasDiurnas().toMillis() > horas.toMillis()) {
                    tabelaExtras.getTabela().setDivisaoDiurnas(horas);
                    tabelaExtras.setSaldoExtrasDiurnas(tabelaExtras.getSaldoExtrasDiurnas().minus(horas));
                } else {
                    tabelaExtras.getTabela().setDivisaoDiurnas(tabelaExtras.getSaldoExtrasDiurnas());
                    tabelaExtras.setSaldoExtrasDiurnas(Duration.ZERO);
                }
                setHorasExtrasAcumulo(sequenciaPercentuais, tabelaExtras.getSaldoExtrasDiurnas(), tabelaExtras.getTabela().getDivisaoDiurnas());
            } else {
                tabelaExtras.getTabela().setDivisaoDiurnas(Duration.ZERO);
            }
            tabelaExtras.getTabelaTipoDiaList().add(tabelaExtras.getTabela());
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
        if (getJornada().getHorasExtrasAcumulo() != null
                && (getJornada().getHorasExtrasAcumulo()
                .getDescricao().equals(CONSTANTES.JORNADA_EXTRAS_SEMANAL)
                || getJornada().getHorasExtrasAcumulo()
                .getDescricao().equals(CONSTANTES.JORNADA_EXTRAS_MENSAL))) {

            //Diminuir do saldo disponível limite mensal as horas
            //que foram alocadas acima
            sequenciaPercentuais.setHoras(Utils.FormatoDataHora(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras()).toInstant()).minus(divisoes)));

            Integer diafechamento = this.jornadaMensalSemanal.getFuncionarioJornada().getJornada().getDiaFechamentoExtra();

            //Se controle mensal
            if (diafechamento != null && diafechamento > 0) {
                if (this.calculo.getDiaProcessado().getDate() > diafechamento) {
                    //saldo extras = saldo extras - limite original
                    //pois não poderá alocar pro outro período o que era pra ser nesse.
                    setDivisoesAcumulo(sequenciaPercentuais, saldoExtras);
                }
            } else {
                //Senão é semanal
                Integer diasemana = this.jornadaMensalSemanal.getFuncionarioJornada().getJornada().getSemana().getIdSemana();
                if (diasemana != null && diasemana > 0) {
                    if (Utils.getDiaDaSemana(this.calculo.getDiaProcessado()) >= diasemana) {
                        setDivisoesAcumulo(sequenciaPercentuais, saldoExtras);
                    }
                }
            }
        }
    }

    private void setDivisoesAcumulo(SequenciaPercentuais sequenciaPercentuais, Duration saldoExtras) {
        Duration limiteOriginal = getHorasLimiteOriginal(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
        if (saldoExtras.toMillis() > limiteOriginal.toMillis()) {
            saldoExtras.minus(limiteOriginal);
        }
    }

    /**
     * Consultar na jornada do funcionário o limite original, pois em
     * SaldosJornadaMensalSemanal está o saldo atualizado que ainda pode ser
     * utilizado, quando jornada livre assume o tipo de dia normal para
     * acréscimos das horas extras
     *
     * @param idSequencia
     * @return
     */
    private Duration getHorasLimiteOriginal(Integer idSequencia) {
        if (getJornada().getPercentuaisAcrescimo() != null) {
            Date horas = getJornada().getPercentuaisAcrescimo()
                    .getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getSequenciaPercentuaisPK().getIdSequencia() == idSequencia))
                    .map(f -> f.getHoras()).findFirst().orElse(null);
            return Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(horas).toInstant());
        }
        return Duration.ZERO;
    }

    private synchronized void setDivisoesNoturnas() {
        if (getJornada().getHorasExtrasAcumulo() == null
                || (getJornada().getHorasExtrasAcumulo() != null
                && getJornada().getHorasExtrasAcumulo()
                .getDescricao().equals(CONSTANTES.JORNADA_EXTRAS_DIARIO))) {
            if (tabelaExtras.getTabelaTipoDiaList() != null && tabelaExtras.getTabelaTipoDiaList().size() > 0) {
                tabelaExtras.getTabelaTipoDiaList()
                        .stream()
                        .sorted(new Utils.ordenaSequencia())
                        .forEach(new divisoesNoturnasExtras());
            }
        } else if (sequenciaPercentuaisSaldoMensalSemanalList != null && sequenciaPercentuaisSaldoMensalSemanalList.size() > 0) {
            sequenciaPercentuaisSaldoMensalSemanalList
                    .stream()
                    .sorted(new Utils.ordenaSequencia())
                    .forEach(new divisoesNoturnasExtrasMensalSemanal());
        }
    }

    private class divisoesNoturnasExtras implements Consumer<TabelaSequenciaPercentuais> {

        public divisoesNoturnasExtras() {
        }

        @Override
        public void accept(TabelaSequenciaPercentuais tabela) {
            if (sequenciaPercentuaisList.size() > 0) {
                //Horas Noturnas por limites
                //Campo acima 24h

                Date horas = Utils.getDataAjustaDiferenca3horas(tabela.getHoras());

                if (tabelaExtras.getSaldoExtrasNoturnas().toMillis() > 0
                        && tabela.getDivisaoDiurnas().toMillis()
                        < Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {

                    if (tabelaExtras.getSaldoExtrasNoturnas().toMillis()
                            > Utils.getHorasAcima24horas(Utils.getViradaDia(1, horas).toInstant()).minus(tabela.getDivisaoNoturnas()).toMillis()) {
                        tabela.setDivisaoNoturnas(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabela.getDivisaoNoturnas()));
                        tabelaExtras.setSaldoExtrasNoturnas(tabelaExtras.getSaldoExtrasNoturnas().minus(Utils.getHorasAcima24horas(horas.toInstant()).minus(tabela.getDivisaoNoturnas())));
                    } else {
                        tabela.setDivisaoNoturnas(tabelaExtras.getSaldoExtrasNoturnas());
                        tabelaExtras.setSaldoExtrasNoturnas(Duration.ZERO);
                    }
                } else {
                    tabela.setDivisaoNoturnas(Duration.ZERO);
                }
            } else {
                //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
                tabela.setDivisaoNoturnas(tabelaExtras.getSaldoExtrasNoturnas());
                tabelaExtras.setSaldoExtrasNoturnas(Duration.ZERO);
            }
        }
    }

    private class divisoesNoturnasExtrasMensalSemanal implements Consumer<SequenciaPercentuais> {

        public divisoesNoturnasExtrasMensalSemanal() {
        }

        @Override
        public void accept(SequenciaPercentuais sequenciaPercentuais) {
            TabelaSequenciaPercentuais atualizaTabela = tabelaExtras.getTabelaTipoDiaList().get(sequenciaPercentuais.getSequenciaPercentuaisPK().getIdSequencia());
            if (sequenciaPercentuaisList.size() > 0) {
                //Horas Noturnas por limites
                //Campo acima 24h

                Date horas = Utils.getDataAjustaDiferenca3horas(sequenciaPercentuais.getHoras());

                if (tabelaExtras.getSaldoExtrasNoturnas().toMillis() > 0
                        && atualizaTabela.getDivisaoDiurnas().toMillis()
                        < Utils.getHorasAcima24horas(horas.toInstant()).toMillis()) {

                    if (tabelaExtras.getSaldoExtrasNoturnas().toMillis()
                            > Utils.getHorasAcima24horas(Utils.getViradaDia(1, horas).toInstant()).minus(atualizaTabela.getDivisaoNoturnas()).toMillis()) {
                        atualizaTabela.setDivisaoNoturnas(Utils.getHorasAcima24horas(horas.toInstant()).minus(atualizaTabela.getDivisaoNoturnas()));
                        tabelaExtras.setSaldoExtrasNoturnas(tabelaExtras.getSaldoExtrasNoturnas().minus(Utils.getHorasAcima24horas(horas.toInstant()).minus(atualizaTabela.getDivisaoNoturnas())));
                    } else {
                        atualizaTabela.setDivisaoNoturnas(tabelaExtras.getSaldoExtrasNoturnas());
                        tabelaExtras.setSaldoExtrasNoturnas(Duration.ZERO);
                    }
                    setHorasExtrasAcumulo(sequenciaPercentuais, tabelaExtras.getSaldoExtrasNoturnas(), tabelaExtras.getTabela().getDivisaoNoturnas());
                } else {
                    atualizaTabela.setDivisaoNoturnas(Duration.ZERO);
                }
            } else {
                //Como não tem limite cadastrado, aloca todas as horas no 1 limite sem acréscimos
                atualizaTabela.setDivisaoNoturnas(tabelaExtras.getSaldoExtrasNoturnas());
                tabelaExtras.setSaldoExtrasNoturnas(Duration.ZERO);
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
        tabelaExtras.setTabela(tabela);
    }

    /**
     * Se efetuou todas as divisões e ainda ficou saldo pois tem mais horas que
     * os limites configurados, então criar um novo com 0% Como não tem limite
     * cadastrado, aloca todas as horas no 1 limite sem acréscimos
     */
    private void setCriarUltimoPercentual() {
        setTabelaPercentuaisTipoDia(null);
        tabelaExtras.getTabela().setDivisaoDiurnas(tabelaExtras.getSaldoExtrasDiurnas());
        tabelaExtras.getTabela().setDivisaoNoturnas(tabelaExtras.getSaldoExtrasNoturnas());
        tabelaExtras.setSaldoExtrasDiurnas(Duration.ZERO);
        tabelaExtras.setSaldoExtrasNoturnas(Duration.ZERO);
        tabelaExtras.getTabelaTipoDiaList().add(tabelaExtras.getTabela());
    }

    public void setAtualizarFuncionario() {
        if (this.funcionario != null) {
            //Jornada Horários
            if (this.funcionario.getFuncionarioJornadaList() != null) {
                this.funcionario.getFuncionarioJornadaList()
                        .forEach(new atualizarFuncionarioJornada());
            }
        }
    }

    private class atualizarFuncionarioJornada implements Consumer<FuncionarioJornada> {

        public atualizarFuncionarioJornada() {
        }

        @Override
        public void accept(FuncionarioJornada funcionarioJornada) {

            if (funcionarioJornada.getJornada().getJornadaHorarioList() != null) {
                funcionarioJornada.getJornada().getJornadaHorarioList()
                        .forEach((JornadaHorario jornadaHorario) -> {
                            if (Utils.getVerificaHorarioNaoAtualizado(jornadaHorario.getHorario().getIdHorario(), horariosAtualizadosList)) {
                                horariosAtualizadosList.add(jornadaHorario.getHorario().getIdHorario());
                                if (jornadaHorario.getHorario().getHorarioMarcacaoList() != null) {
                                    jornadaHorario.getHorario().getHorarioMarcacaoList().forEach((HorarioMarcacao horarioMarcacao) -> {
                                        if (horarioMarcacao.getHorarioEntrada() != null) {
                                            horarioMarcacao.setHorarioEntrada(Utils.getDataAjustaDiferenca3horas(horarioMarcacao.getHorarioEntrada()));
                                        }
                                        if (horarioMarcacao.getHorarioSaida() != null) {
                                            horarioMarcacao.setHorarioSaida(Utils.getDataAjustaDiferenca3horas(horarioMarcacao.getHorarioSaida()));
                                        }
                                    });
                                }
                            }
                        });

                //Controle para saber se o horário já foi atualizado
                funcionarioJornada.getJornada().getJornadaHorarioList()
                        .stream()
                        .forEach(new AtualizaHorarios());
            }
        }

        private class AtualizaHorarios implements Consumer<JornadaHorario> {

            private Date horarioanterior;

            public AtualizaHorarios() {
                this.horarioanterior = null;
            }

            @Override
            public void accept(JornadaHorario jornadaHorario) {
                if (jornadaHorario.getHorario().getHorarioMarcacaoList() != null) {
                    jornadaHorario.getHorario().getHorarioMarcacaoList().forEach((HorarioMarcacao horarioMarcacao) -> {
                        if (horarioanterior != null) {
                            if (horarioMarcacao.getHorarioEntrada().getTime() < horarioanterior.getTime()) {
                                //Virada
                                horarioMarcacao.setHorarioEntrada(Utils.getViradaDia(1, horarioMarcacao.getHorarioEntrada()));
                                horarioMarcacao.setHorarioSaida(Utils.getViradaDia(1, horarioMarcacao.getHorarioSaida()));
                                horarioanterior = horarioMarcacao.getHorarioSaida();
                            } else {
                                //mesmo dia
                                if (horarioMarcacao.getHorarioSaida().getTime() < horarioMarcacao.getHorarioEntrada().getTime()) {
                                    horarioMarcacao.setHorarioSaida(Utils.getViradaDia(1, horarioMarcacao.getHorarioSaida()));
                                } else {
                                    horarioMarcacao.setHorarioSaida(horarioMarcacao.getHorarioSaida());
                                }
                                horarioMarcacao.setHorarioEntrada(horarioMarcacao.getHorarioEntrada());
                                horarioanterior = horarioMarcacao.getHorarioSaida();
                            }
                        } else {
                            if (horarioMarcacao.getHorarioSaida().getTime() < horarioMarcacao.getHorarioEntrada().getTime()) {
                                horarioMarcacao.setHorarioSaida(Utils.getViradaDia(1, horarioMarcacao.getHorarioSaida()));
                            } else {
                                horarioMarcacao.setHorarioSaida(horarioMarcacao.getHorarioSaida());
                            }
                            horarioMarcacao.setHorarioEntrada(horarioMarcacao.getHorarioEntrada());
                            horarioanterior = horarioMarcacao.getHorarioSaida();
                        }
                    });
                    horarioanterior = null;
                }
            }
        }
    }

}
