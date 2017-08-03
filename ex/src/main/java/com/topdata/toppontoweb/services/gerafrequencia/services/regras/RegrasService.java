package com.topdata.toppontoweb.services.gerafrequencia.services.regras;

import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Ocorrencia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Regra;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioBancodeHorasFechamentoService;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioAbonoService;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioCompensacoesService;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Possui todas as informações de regras do cálculo do dia
 *
 * @author enio.junior
 */
public class RegrasService {

    private final FuncionarioService funcionarioService;
    private final FuncionarioJornadaService funcionarioJornadaService;
    private final FuncionarioAbonoService funcionarioAbonoService;
    private final FuncionarioCompensacoesService funcionarioCompensacoesService;
    private final Calculo calculo;
    private BancoHoras funcionarioBancoHoras;
    private List<MarcacoesDia> funcionarioMarcacoesDiaList;
    private Regra regra;

    public RegrasService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioService = this.calculo.getFuncionarioService();
        this.funcionarioJornadaService = funcionarioService.getFuncionarioJornadaService();
        this.funcionarioAbonoService = funcionarioService.getFuncionarioAbonoService();
        this.funcionarioCompensacoesService = funcionarioService.getFuncionarioCompensacoesService();
    }

    public Regra getRegras() {
        return regra;
    }

    public void inicializaRegra() {
        regra = new Regra();
        this.funcionarioBancoHoras = funcionarioService.getFuncionarioBancodeHorasService().getBancoDeHoras();
        this.funcionarioMarcacoesDiaList = this.calculo.getFuncionarioMarcacoesService().getMarcacoesJornadaDia(this.calculo.getDiaProcessado());
    }

    public Regra setRegras(Ocorrencia ocorrencia, List<MarcacoesDia> marcacoesDiaList) {
        inicializaRegra();
        regra.setDiaProcessado(true);
        regra.setDiaUtil(isDiaUtil());
        regra.setFechamento(isFechamento());
        regra.setJornada(isJornada());
        regra.setCumprirHorario(isCumprirHorario());
        regra.setHorariosPreAssinalados(isHorariosPreAssinalados());
        regra.setFeriado(isFeriado());
        regra.setAdicionalNoturno(isAdicionalNoturno());
        regra.setTrabalhado(isTrabalhado());
        regra.setCompensaAtrasos(isCompensaAtrasos());
        regra.setFalta(isFalta());
        regra.setFaltandoMarcacoes(isFaltandoMarcacoes());
        regra.setMarcacoesImpares(isMarcacoesImpares());
        regra.setInconsistencia(isFaltandoMarcacoes() || isMarcacoesImpares());
        regra.setIntervaloDeslocado(isIntervaloDeslocado());
        regra.setExcedeuIntervalo(isExcedeuIntervalo());
        regra.setViradaDiaJornada(isViradaDiaJornada());
        regra.setViradaDiaTrabalhado(isViradaDiaTrabalhado());
        regra.setAbonoSemSomenteJustificativa(isAbonoSemSomenteJustificativa());
        regra.setAbonoSomenteJustificativa(isAbonoSomenteJustificativa());
        regra.setJustificativaAbono(getJustificativaAbono());
        regra.setAfastadoComAbono(isAfastadoComAbono());
        regra.setAfastadoSemAbono(isAfastadoSemAbono());
        regra.setTrabalhouAfastado(isTrabalhouAfastado());
        regra.setCompensandoDia(isCompensandoDia());
        regra.setDiaCompensadoSemSomenteJustificativa(isDiaCompensadoSemSomenteJustificativa());
        regra.setCompensacaoInvalida(isCompensacaoInvalida());
        regra.setDiaCompensadoSomenteJustificativa(isDiaCompensadoSomenteJustificativa());
        regra.setRealizaCompensacao(isRealizaCompensacao());
        regra.setJustificativaDiaCompensado(getJustificativaDiaCompensado());
        regra.setBancoDeHoras(isBancoDeHoras());
        regra.setNaoPagaAdicionalBancoDeHoras(isNaoPagaAdicionalBancoDeHoras());
        regra.setLegendaStatusEspelho(getIdLegenda());
        regra.setObservacoes(getObservacoes(ocorrencia));
        setCarregaLegendaStatusPresenca(marcacoesDiaList);
        return regra;
    }

    public String getJustificativaDiaCompensado() {
        return isDiaCompensadoSomenteJustificativa() ? this.funcionarioCompensacoesService.getDiaCompensado().getMotivo().getDescricao() : "";
    }

    public String getJustificativaAbono() {
        if (isAbonoSomenteJustificativa()) {
            return this.funcionarioService.getFuncionarioAbonoService().getAbono().getMotivo().getDescricao();
        }
        if (isAfastadoComAbono() || isAfastadoSemAbono()) {
            return this.funcionarioService.getFuncionarioAbonoService().getAfastamento().getMotivo().getDescricao();
        }
        return "";
    }

    public boolean isAdicionalNoturno() {
        return this.funcionarioJornadaService.getJornada().getPercentualAdicionalNoturno() != null;
    }

    public boolean isCompensaAtrasos() {
        return this.funcionarioJornadaService.getJornada().getCompensaAtrasos() != null
                && this.funcionarioJornadaService.getJornada().getCompensaAtrasos();
    }

    public boolean isFechamento() {
        return this.funcionarioService.getFuncionarioEmpresaFechamentoService().getDadosFechamentoDia(this.calculo.getDiaProcessado()).getData() != null;
    }

    /**
     * Não é quando sábado, domingo ou feriado (calendário)
     *
     * @return
     */
    public boolean isDiaUtil() {
        return Utils.getDiaUtil(this.calculo.getDiaProcessado()) && !isFeriado();
    }

    public boolean isFuncionarioAtivo(Date diaProcessado) {
        if (this.calculo.getFuncionarioService() != null
                && this.funcionarioService.getDataAdmissao() != null) {
            return (diaProcessado.compareTo(this.funcionarioService.getDataAdmissao()) >= 0)
                    && ((this.funcionarioService.getDataDemissao() == null)
                    || (diaProcessado.compareTo(this.funcionarioService.getDataDemissao()) <= 0));
        }
        return false;
    }

    public boolean isFeriado() {
        return this.funcionarioService.getFuncionarioCalendarioService().isFeriadoNesseDia();
    }

    /**
     * Se livre retorna false
     *
     * @return
     */
    public boolean isHorarioCumprir() {
        if (this.funcionarioJornadaService.getJornada().getIdJornada() != null) {
            if (this.funcionarioJornadaService.getHorarioDia().getIdHorario() == null || this.funcionarioJornadaService.getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_LIVRE)) {
                return false;
            } else {
                return !((CONSTANTES.TIPODIA_FOLGA.equals(this.funcionarioJornadaService.getHorarioDia().getTipodia().getDescricao()))
                        || (CONSTANTES.TIPODIA_FOLGA_DIFERENCIADA.equals(this.funcionarioJornadaService.getHorarioDia().getTipodia().getDescricao())));
            }
        }
        return false;
    }

    public boolean isCumprirHorario() {
        return isHorarioCumprir() && !isFeriado();
    }

    public boolean isCumprirHorarioComFeriado() {
        return isHorarioCumprir() && isFeriado();
    }

    public int getIdTipoDia() {
        return getTipoDia().getIdTipodia();
    }

    public TipoDia getTipoDia() {
        if (isFeriado()) {
            return new TipoDia(5, CONSTANTES.TIPODIA_FERIADO);
        }
        if (isDiaCompensadoSemSomenteJustificativa()) {
            return new TipoDia(2, CONSTANTES.TIPODIA_COMPENSADO);
        }
        if (this.funcionarioJornadaService.getJornada().getIdJornada() != null
                && this.funcionarioJornadaService.getHorarioDia().getIdHorario() != null) {
            return this.funcionarioJornadaService.getHorarioDia().getTipodia();
        } else {
            return new TipoDia(1, CONSTANTES.TIPODIA_NORMAL);
        }
    }

    public boolean isHorariosPreAssinalados() {
        if (this.funcionarioJornadaService.getHorarioDia().getIdHorario() != null) {
            return this.funcionarioJornadaService.getHorarioDia().getPreAssinalada();
        }
        return false;
    }

    public boolean isJornada() {
        return this.funcionarioJornadaService.getJornada().getIdJornada() != null;
    }

    /**
     * Se um dos horários próximo dia, comparar se uma data é maior que a outra
     * EM DIAS
     *
     * @return
     */
    public boolean isViradaDiaJornada() {
        Date ultimaData = null;
        if (this.funcionarioJornadaService.getHorariosJornadaDia().size() > 0) {
            for (HorarioMarcacao horarioMarcacao : this.funcionarioJornadaService.getHorariosJornadaDia()) {

                Date horarioEntrada = horarioMarcacao.getHorarioEntrada();
                Date horarioSaida = horarioMarcacao.getHorarioSaida();

                if (ultimaData != null && horarioEntrada.getDate() > ultimaData.getDate()) {
                    return true;
                } else {
                    ultimaData = horarioEntrada;
                }
                if (ultimaData != null && horarioSaida.getDate() > ultimaData.getDate()) {
                    return true;
                } else {
                    ultimaData = horarioSaida;
                }
            }
        }
        return false;
    }

    public boolean isFalta() {
        return isCumprirHorario() && !isTrabalhado() && !isAfastadoComAbono() && !isAfastadoSemAbono();
    }

    public boolean isTrabalhouDiaACompensar() {
        return funcionarioMarcacoesDiaList.size() > 0;
    }

    private boolean isCompensandoDia() {
        return this.funcionarioCompensacoesService.getCompensandoDia().getIdCompensacao() != null;
    }

    private boolean isCompensacaoInvalida() {
        return isCompensandoDia() && isPossuiMarcacaoDiaCompensando();
    }

    /**
     * Se possui marcações no dia a ser compensado
     *
     * @return
     */
    private boolean isPossuiMarcacaoDiaCompensando() {
        return this.calculo.getFuncionarioMarcacoesService().getMarcacoesJornadaDia(this.funcionarioCompensacoesService.getCompensandoDia().getDataCompensada()).size() > 0;
    }

    public boolean isAfastadoComAbono() {
        return this.funcionarioAbonoService.getAfastamento().getIdAfastamento() != null
                && this.funcionarioAbonoService.getAfastamento().getAbonado();
    }

    public boolean isAfastadoSemAbono() {
        return this.funcionarioAbonoService.getAfastamento().getIdAfastamento() != null
                && !this.funcionarioAbonoService.getAfastamento().getAbonado();
    }

    private boolean isTrabalhouAfastado() {
        return this.funcionarioAbonoService.getAfastamento().getIdAfastamento() != null
                && funcionarioMarcacoesDiaList != null
                && funcionarioMarcacoesDiaList.size() > 0;
    }

    public boolean isAbonoSemSomenteJustificativa() {
        return this.funcionarioAbonoService.getAbono().getIdAbono() != null
                && this.funcionarioAbonoService.getAbono().getData() != null
                && !this.funcionarioAbonoService.getAbono().getSomenteJustificativa();
    }

    private boolean isAbonoSomenteJustificativa() {
        return this.funcionarioAbonoService.getAbono().getIdAbono() != null
                && this.funcionarioAbonoService.getAbono().getData() != null
                && this.funcionarioAbonoService.getAbono().getSomenteJustificativa() == true;
    }

    public boolean isDiaCompensadoSemSomenteJustificativa() {
        return this.funcionarioCompensacoesService.getDiaCompensado().getIdCompensacao() != null
                && this.funcionarioCompensacoesService.getDiaCompensado().getDataCompensada() != null
                && isFalta();
        //&& this.funcionarioCompensacoesService.getDiaCompensado().getMotivo() == null;
    }

    //Sempre tem motivo "justificativa" não faz sentido separar igual versão de campo
    private boolean isDiaCompensadoSomenteJustificativa() {
        //return this.funcionarioCompensacoesService.getDiaCompensado() != null
        //      && this.funcionarioCompensacoesService.getDiaCompensado().getDataCompensada() != null
        //      && isFalta()
        //      && this.funcionarioCompensacoesService.getDiaCompensado().getMotivo() != null;
        return isDiaCompensadoSemSomenteJustificativa();
    }

    /**
     * Está compensando algum dia e não possui marcação no dia que vai ser
     * compensado e (Possui Jornada ou (não possui Jornada (não cumprir horário)
     * e considera dias sem jornada) e se não é somente justificativa
     *
     * @return
     */
    public boolean isRealizaCompensacao() {
        return isCompensandoDia()
                && !isPossuiMarcacaoDiaCompensando()
                && (isCumprirHorario() || (!isCumprirHorario()
                && this.funcionarioCompensacoesService.getCompensandoDia().getConsideraDiasSemJornada()));
    }

    public boolean isTrabalhado() {
        return funcionarioMarcacoesDiaList.size() > 0;
    }

    private boolean isFaltandoMarcacoes() {
        if (this.funcionarioJornadaService.getJornada().getIdJornada() != null
                && !this.funcionarioJornadaService.getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_LIVRE)
                && this.funcionarioJornadaService.getHorariosJornadaDia() != null) {
            return (this.funcionarioJornadaService.getHorariosJornadaDia().size() != funcionarioMarcacoesDiaList.size()) || isMarcacoesImpares();
        } else {
            return isMarcacoesImpares();
        }
    }

    private boolean isMarcacoesImpares() {
        return this.calculo.getFuncionarioMarcacoesService().getMarcacoesImpares();
    }

    private boolean isMarcacaoExtra() {
        return this.calculo.getFuncionarioMarcacoesService().getMarcacaoExtra();
    }

    /**
     * Se o horário da saída ou retorno trabalhado - horário da saída ou retorno
     * trabalhado for maior que a tolerância de ocorrência
     *
     * @return
     */
    private boolean isIntervaloDeslocado() {
        boolean saida = false;
        Date diferenca;

        if (this.funcionarioJornadaService.getJornada().getIdJornada() != null
                && this.funcionarioJornadaService.getHorariosJornadaDia() != null) {
            if (funcionarioMarcacoesDiaList.size() > 0) {
                for (HorarioMarcacao horarioMarcacao : this.funcionarioJornadaService.getHorariosJornadaDia()) {
                    //Se não for a última saída então é intervalo
                    if (horarioMarcacao.getIdSequencia() < this.funcionarioJornadaService.getHorariosJornadaDia().size()) {

                        if (this.calculo.getFuncionarioMarcacoesService()
                                .getMarcacoesJornadaDia(this.calculo.getDiaProcessado()).size() > (horarioMarcacao.getIdSequencia())) {

                            if (this.calculo.getFuncionarioMarcacoesService()
                                    .getMarcacoesJornadaDia(this.calculo.getDiaProcessado())
                                    .get(horarioMarcacao.getIdSequencia()).getHorarioSaida() != null) {
                                diferenca = Utils.getIntervaloHoras(horarioMarcacao.getHorarioSaida(), funcionarioMarcacoesDiaList.get(horarioMarcacao.getIdSequencia()).getHorarioSaida());
                                if (Utils.getUltrapassouTolerancia(diferenca, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaOcorrencia()))) {
                                    saida = true;
                                }
                            }
                        }
                    }
                    //Se não for a primeira entrada então é intervalo
                    if (horarioMarcacao.getIdSequencia() > 1) {
                        if (funcionarioMarcacoesDiaList.size() > (horarioMarcacao.getIdSequencia())) {
                            if (funcionarioMarcacoesDiaList.get(horarioMarcacao.getIdSequencia()).getHorarioEntrada() != null) {
                                diferenca = Utils.getIntervaloHoras(horarioMarcacao.getHorarioEntrada(), funcionarioMarcacoesDiaList.get(horarioMarcacao.getIdSequencia()).getHorarioEntrada());
                                if ((Utils.getUltrapassouTolerancia(diferenca, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaOcorrencia()))) && saida) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Se as horas do intervalo trabalhado forem maiores que do previsto na
     * jornada
     *
     * @return
     */
    private boolean isExcedeuIntervalo() {
        List<Long> intervalosJornadaList = new ArrayList<>();
        Date inicioIntervalo = null;
        AtomicInteger contador = new AtomicInteger(0);

        if (this.funcionarioJornadaService.getHorariosJornadaDia().size() > 0
                && !this.funcionarioJornadaService.getJornada().getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_LIVRE)
                && funcionarioMarcacoesDiaList.size() > 0) {
            //Levantar as horas de cada intervalo da jornada
            for (HorarioMarcacao horarioMarcacao : this.funcionarioJornadaService.getHorariosJornadaDia()) {
                if (inicioIntervalo != null) {

                    Date horarioEntrada = horarioMarcacao.getHorarioEntrada();

                    //Se não for a primeira entrada então é intervalo
                    if (horarioMarcacao.getIdSequencia() > 1) {
                        //Armazena cada intervalo da jornada
                        if ((inicioIntervalo.getTime() - horarioEntrada.getTime()) > 0) {
                            intervalosJornadaList.add((inicioIntervalo.getTime() - horarioEntrada.getTime()));
                        } else {
                            intervalosJornadaList.add((horarioEntrada.getTime() - inicioIntervalo.getTime()));
                        }
                        inicioIntervalo = null;
                    }
                }
                //Se não for a última saída então é intervalo
                if (horarioMarcacao.getIdSequencia() < this.funcionarioJornadaService.getHorariosJornadaDia().size()) {
                    inicioIntervalo = horarioMarcacao.getHorarioSaida();
                }
            }

            inicioIntervalo = null;

            //Para cada intervalo trabalhado
            for (MarcacoesDia marcacoesDia : funcionarioMarcacoesDiaList) {
                if (inicioIntervalo != null) {
                    //Se não for a primeira entrada então é intervalo
                    if (marcacoesDia.getIdSequencia() > 1) {
                        if (contador.get() >= intervalosJornadaList.size()) {
                            break;
                        } else {
                            //comparar se pra cada intervalo da jornada tem um trabalhado maior
                            Date dataIntervaloMinutos;
                            if (((inicioIntervalo.getTime() - marcacoesDia.getHorarioEntrada().getTime()) - intervalosJornadaList.get(contador.get())) > 0) {
                                dataIntervaloMinutos = Utils.FormatoDataHora(Utils.getIntervaloDuration(marcacoesDia.getHorarioEntrada(), inicioIntervalo).minusMillis(intervalosJornadaList.get(contador.get())));
                            } else {
                                dataIntervaloMinutos = Utils.FormatoDataHora(Utils.getIntervaloDuration(inicioIntervalo, marcacoesDia.getHorarioEntrada()).minusMillis(intervalosJornadaList.get(contador.get())));
                            }
                            if (Utils.getUltrapassouTolerancia(dataIntervaloMinutos, Utils.getDataPadrao(0, this.funcionarioJornadaService.getJornada().getToleranciaOcorrencia()))) {
                                //Caso sim, excedeu o intervalo
                                return true;
                            }
                            inicioIntervalo = null;
                            contador.getAndIncrement();
                        }
                    }
                }
                //Se não for a última saída então é intervalo
                if (marcacoesDia.getIdSequencia() < funcionarioMarcacoesDiaList.size()) {
                    inicioIntervalo = marcacoesDia.getHorarioSaida();
                }
            }
        }
        return false;
    }

    /**
     * Se umas das marcações próximo dia, comparar se uma data é maior que a
     * outra EM DIAS
     *
     * @return
     */
    private boolean isViradaDiaTrabalhado() {
        Date ultimaData = null;

        if (funcionarioMarcacoesDiaList.size() > 0) {
            for (MarcacoesDia marcacoesDia : funcionarioMarcacoesDiaList) {
                if (marcacoesDia.getHorarioEntrada() != null) {
                    if (ultimaData != null && marcacoesDia.getHorarioEntrada().getDate() > ultimaData.getDate()) {
                        return true;
                    } else {
                        ultimaData = marcacoesDia.getHorarioEntrada();
                    }
                }
                if (marcacoesDia.getHorarioSaida() != null) {
                    if (ultimaData != null && marcacoesDia.getHorarioSaida().getDate() > ultimaData.getDate()) {
                        return true;
                    } else {
                        ultimaData = marcacoesDia.getHorarioSaida();
                    }
                }
            }
        }
        return false;
    }

    public boolean isBancoDeHoras() {
        return funcionarioBancoHoras.getIdBancoHoras() != null;
    }

    private boolean isNaoPagaAdicionalBancoDeHoras() {
        return isBancoDeHoras() && funcionarioBancoHoras.getNaoPagaAdicionalNoturnoBH();
    }

    public boolean isFechamentoBHEdicaodeSaldo() {
        FuncionarioBancodeHorasFechamentoService bhfs = this.calculo.getBancodeHorasService().getBancodeHorasFechamentoService();
        return isBancoDeHoras()
                && bhfs != null
                && bhfs.getFechamentoNoDia().getIdFuncionarioBancoHorasFechamento() != null
                && bhfs.getFechamentoNoDia().getTipoFechamento()
                .getDescricao().equals(CONSTANTES.FECHAMENTOBH_EDICAODESALDO)
                && (bhfs.getFechamentoNoDia().getCredito() != null && bhfs.getFechamentoNoDia().getDebito() != null);
    }

    public boolean isFechamentoBHAcerto() {
        FuncionarioBancodeHorasFechamentoService bhfs = this.calculo.getBancodeHorasService().getBancodeHorasFechamentoService();
        return isBancoDeHoras()
                && bhfs != null
                && bhfs.getFechamentoNoDia().getIdFuncionarioBancoHorasFechamento() != null
                && bhfs.getFechamentoNoDia().getTipoFechamento()
                .getDescricao().equals(CONSTANTES.FECHAMENTOBH_ACERTO)
                && (bhfs.getFechamentoNoDia().getCredito() != null && bhfs.getFechamentoNoDia().getDebito() != null);
    }

    public String getIdLegenda() {
        if (isFechamentoBHEdicaodeSaldo()) {
            return CONSTANTES.LEGENDA_EDICAODESALDOBH;
        } else if (isAfastadoComAbono() || isAfastadoSemAbono()) {
            return CONSTANTES.LEGENDA_AFASTADO;
        } else if (isFeriado()) {
            return CONSTANTES.LEGENDA_FERIADO;
        } else if (isCompensandoDia()) {
            return CONSTANTES.LEGENDA_PERIODOCOMPENSACAO;
        } else if (isDiaCompensadoSomenteJustificativa() || isDiaCompensadoSemSomenteJustificativa()) {
            return CONSTANTES.LEGENDA_DIACOMPENSADO;
        } else if (isMarcacaoExtra()) {
            return CONSTANTES.LEGENDA_MARCACAO_EXTRA;
        } else {
            return "";
        }
    }

    public String getObservacoes(Ocorrencia ocorrencia) {
        if (isAbonoSemSomenteJustificativa() || isAbonoSomenteJustificativa()) {
            return regra.getJustificativaAbono();
        } else {
            if (isCompensacaoInvalida()) {
                return CONSTANTES.OBSERVACAO_COMPENSACAOINVALIDA;
            }
            if (isMarcacoesImpares()) {
                return CONSTANTES.OBSERVACAO_MARCACOESIMPARES;
            }
            if (!isFuncionarioAtivo(this.calculo.getDiaProcessado())) {
                return CONSTANTES.OBSERVACAO_FUNCIONARIOATIVO;
            }
            if (isAfastadoComAbono() || isAfastadoSemAbono()) {
                return regra.getJustificativaAbono();
            }
            if (isTrabalhouAfastado()) {
                return CONSTANTES.OBSERVACAO_TRABALHOUAFASTADO;
            }
            if (isFalta()) {
                return CONSTANTES.OBSERVACAO_FALTA;
            }
            if (ocorrencia != null) {
                if (ocorrencia.isEntradaAtrasada()) {
                    return CONSTANTES.OBSERVACAO_ENTRADAATRASADA;
                }
                if (ocorrencia.isSaidaAntecipada()) {
                    return CONSTANTES.OBSERVACAO_SAIDAANTECIPADA;
                }
                if (ocorrencia.isEntradaAntecipada()) {
                    return CONSTANTES.OBSERVACAO_ENTRADAANTECIPADA;
                }
                if (ocorrencia.isSaidaAposHorario()) {
                    return CONSTANTES.OBSERVACAO_SAIDAAPOSHORARIO;
                }
            }
            if (isExcedeuIntervalo()) {
                return CONSTANTES.OBSERVACAO_EXCEDEUINTERVALO;
            }
            if (isIntervaloDeslocado()) {
                return CONSTANTES.OBSERVACAO_INTERVALODESLOCADO;
            }
            return "";
        }
    }

    /**
     * Relatório de Presença
     *
     * @param marcacoesDiaList
     */
    public void setCarregaLegendaStatusPresenca(List<MarcacoesDia> marcacoesDiaList) {
        if (isAfastadoComAbono()
                || isAfastadoSemAbono()) {
            regra.setLegendaStatusPresenca(CONSTANTES.LEGENDA_AFASTADO);
        } else if (marcacoesDiaList.size() > 0) {
            regra.setLegendaStatusPresenca(CONSTANTES.LEGENDA_PRESENTE);
        } else {
            regra.setLegendaStatusPresenca(CONSTANTES.LEGENDA_VAZIO);
        }
    }
}
