package com.topdata.toppontoweb.services.gerafrequencia.services.calculo;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import java.util.Date;
import java.util.List;

import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.DsrApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Ocorrencia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Regra;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.services.fiscal.ListaAFDT;
import com.topdata.toppontoweb.services.gerafrequencia.services.fiscal.ListasACJEF;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.OcorrenciasService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsável em efetuar os cálculos do dia, contêm a rotina/regras
 *
 * @author enio.junior
 */
public class CalculoDiaService {

    public final static Logger LOGGER = LoggerFactory.getLogger(CalculoDiaService.class.getName());
    private SaidaDia saidaDia;
    private final Calculo calculo;
    private final ListaAFDT listaAFDT;
    private final ListasACJEF listasACJEF;

    public CalculoDiaService(Calculo calculo, ListaAFDT listaAFDT, ListasACJEF listasACJEF) {
        this.calculo = calculo;
        this.listaAFDT = listaAFDT;
        this.listasACJEF = listasACJEF;
    }

    /**
     * Executa passo a passo as regras de cálculo do dia
     *
     * @param dia
     * @return
     */
    public SaidaDia processaDia(Date dia) {
        Date diaProcessa = new Date(dia.getTime());
        this.saidaDia = new SaidaDia(this.calculo);
        setInicializaSaidaDia(diaProcessa);

        //Durante o processamento do período de compensação não calcula banco de horas
        //Só processa dia se funcionário ativo
        if (this.calculo.getRegrasService().isFuncionarioAtivo(diaProcessa)) {
            setJornada();
            setTrabalhado();

            //Não precisa carregar as regras no saidaDia pois é somente interno
            if (!isProcessandoPeriodoCompensacao()) {
                setRegras();
                this.saidaDia.setIdTipoDia(this.calculo.getRegrasService().getIdTipoDia());
                this.saidaDia.setIntervalos(this.calculo.getIntervalosService().getIntervalos());
                this.saidaDia.setSaldoAnteriorDiaPeriodoBancodeHoras(this.calculo.getSaldoDiaBancodeHoras().getSaldoAnteriorDiaPeriodoBancodeHoras());
                insereAFDT();
                insereACJEF();
                gravaSaidaDiaSomenteUltimoDiaPeriodo(diaProcessa);
            }
        }
        return this.saidaDia;
    }

    /**
     * Gera cada linha do arquivo de AFDT
     */
    private void insereAFDT() {
        if (this.calculo.getEntradaAPI().isProcessaAFDT()) {
            LOGGER.debug("Calculando AFDT... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            this.listaAFDT.setInsere();
        }
    }

    /**
     * Grava quando processo de AFDT
     */
    private void gravaAFDT() {
        if (this.calculo.getEntradaAPI().isProcessaAFDT()) {
            this.saidaDia.setListaAFDT(this.listaAFDT.getAFDTList());
        }
    }

    /**
     * Gera cada linha do arquivo de ACJEF
     */
    private void insereACJEF() {
        if (this.calculo.getEntradaAPI().isProcessaCalculo()
                && this.calculo.getEntradaAPI().isProcessaACJEF()) {
            LOGGER.debug("Calculando ACJEF... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
            this.listasACJEF.setInsereHorariosContratuaisACJEFList();
            this.listasACJEF.setInsereACJEFList(this.saidaDia);
        }
    }

    /**
     * Grava quando processo de ACJEF
     */
    private void gravaACJEF() {
        if (this.calculo.getEntradaAPI().isProcessaCalculo()
                && this.calculo.getEntradaAPI().isProcessaACJEF()) {
            this.saidaDia.setListaACJEF(this.listasACJEF.getACJEFList());
            this.saidaDia.setListaHorariosContratuaisACJEF(listasACJEF.getHorariosContratuaisACJEFList());
        }
    }

    /**
     * Grava as informações somente no último dia do período informado para não
     * sobrecarregar os outros dias (performance)
     *
     * @param dia
     */
    private void gravaSaidaDiaSomenteUltimoDiaPeriodo(Date dia) {
        if (Utils.getDiferencaData(dia, this.calculo.getEntradaAPI().getDataFimPeriodo()) == 0) {
            this.saidaDia.setSaldoPrimeiroDiaBancodeHoras(this.calculo.getSaldoDiaBancodeHoras().getSaldoPrimeiroDiaBancodeHoras());
            this.saidaDia.setDataPrimeiroDiaBancodeHoras(this.calculo.getSaldoDiaBancodeHoras().getDataPrimeiroDiaBancodeHoras());
            gravaAFDT();
            gravaACJEF();
        }
    }

    /**
     * Inicializa todos os dados de saidaDia independente de serem utilizados no
     * cálculo
     *
     * @param dia
     */
    private void setInicializaSaidaDia(Date dia) {
        Regra r = new Regra();
        r.setDiaProcessado(false);
        this.saidaDia.setData(dia);

        if (this.calculo.getEntradaAPI().getFuncionario() != null) {
            this.saidaDia.setFuncionario(this.calculo.getEntradaAPI().getFuncionario());
        } else {
            this.saidaDia.setFuncionario(new Funcionario());
        }

        if (this.calculo.getFuncionarioService()
                .getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras() != null) {
            this.saidaDia.setFuncionarioBancoHoras(this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras());
        } else {
            this.saidaDia.setFuncionarioBancoHoras(new FuncionarioBancoHoras());
        }

        this.saidaDia.setRegras(r);
        this.saidaDia.setBancodeHoras(new BancodeHorasApi());
        this.saidaDia.setDsr(new DsrApi());
        this.saidaDia.setHorasPrevistas(new Saldo());
        this.saidaDia.setHorasTrabalhadas(new SaldoTrabalhadas());
        this.saidaDia.setOcorrencias(new Ocorrencia());
        this.saidaDia.setPercentualAdicionalNoturno(0d);
        this.saidaDia.setRegras(new Regra());
        this.saidaDia.setSaldoExtras(new SaldoExtras());
        this.saidaDia.setSaldoAusencias(new SaldoAusencias());
        this.saidaDia.setSaldoNormais(new Saldo());

        this.calculo.getRegrasService().inicializaRegra();
        this.saidaDia.setRegras(this.calculo.getRegrasService().getRegras());

        this.calculo.getIntervalosService().inicializaIntervalos();
        this.saidaDia.setIntervalos(this.calculo.getIntervalosService().getIntervalos());
    }

    /**
     * Grava as informações dos horários da Jornada (caso tenha horário a
     * cumprir) e da quantidade de horas previstas para o dia Se possui
     * fechamento de horas no dia, somente consulta os dados calculados
     */
    private void setJornada() {
        LOGGER.debug("Calculando horas jornada... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        this.calculo.getFuncionarioService().getFuncionarioJornadaService().setHorarioJornada();
        if (this.calculo.getRegrasService().isCumprirHorario()
                || this.calculo.getRegrasService().isCumprirHorarioComFeriado()) {
            this.saidaDia.setHorariosPrevistosList(this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorariosJornadaDia());
            if (this.calculo.getEntradaAPI().isProcessaCalculo()) {
                if (!this.calculo.getRegrasService().isFechamento()) {
                    this.saidaDia.setHorasPrevistas(this.calculo.getSaldosService().getTotalHorasPrevistas());
                } else {
                    this.calculo.getFuncionarioService().getFuncionarioEmpresaFechamentoService().setConsultaHorasPrevistasFechamento(this.saidaDia);
                }
            }
        }
        if (this.calculo.getRegrasService().isJornada()) {
            this.saidaDia.setPercentualAdicionalNoturno(this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada().getPercentualAdicionalNoturno());
            if (this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorarioDia().getIdHorario() != null) {
                this.saidaDia.setIdHorario(this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorarioDia().getIdHorario());
            }
        }
    }

    /**
     * Grava as informações dos horários trabalhados, responsável em inicializar
     * os cálculos, se possui fechamento de horas no dia, somente consulta os
     * dados calculados
     */
    private void setTrabalhado() {
        LOGGER.debug("Calculando horas trabalhadas... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        List<MarcacoesDia> marcacoesOriginaisDias = this.calculo.getFuncionarioMarcacoesService().getMarcacoesOriginaisDia(this.calculo.getDiaProcessado());
        this.saidaDia.setHorariosTrabalhadosOriginaisEquipamentoList(marcacoesOriginaisDias);
        this.saidaDia.setHorariosTrabalhadosPresencaList(marcacoesOriginaisDias);

        if (this.calculo.getRegrasService().isTrabalhado()) {
            setHorarioseHorasDiaTrabalhado();
        } else if (!isProcessandoPeriodoCompensacao()) {
            //É falta
            this.calculo.getIntervalosService().setUltimaMarcacaoDiaAnterior(null);

            if (this.calculo.getEntradaAPI().isProcessaManutencaoMarcacoes()) {
                this.calculo.getFuncionarioMarcacoesService().setMarcacoesQueFaltam(0, new AtomicInteger(0));
                this.saidaDia.setHorariosTrabalhadosList(this.calculo.getFuncionarioMarcacoesService().getMarcacoesDiaList());
            }
        }

        if (this.calculo.getEntradaAPI().isProcessaCalculo()) {
            if (!this.calculo.getRegrasService().isFechamento()) {
                setProcessaCalculo();
            } else {
                this.calculo.getFuncionarioService().getFuncionarioEmpresaFechamentoService().setConsultaDadosFechamento(this.saidaDia);
            }
            if (this.calculo.getRegrasService().isBancoDeHoras()) {
                this.calculo.getBancodeHorasService().setSaldosAcumuladosBH();
            }
            setPreencherPrimeiraLinhaTabelasComZero();
            setAtualizaStatusAposTodasAsOperacoes();
        }
    }

    private void setHorarioseHorasDiaTrabalhado() {
        if (!isProcessandoPeriodoCompensacao()) {
            this.saidaDia.setHorariosTrabalhadosTratadosList(this.calculo.getFuncionarioMarcacoesService().getMarcacoesTratadasList(this.saidaDia.getData()));
        }

        List<MarcacoesDia> marcacoesDias = this.calculo.getFuncionarioMarcacoesService().getMarcacoesJornadaDia(this.calculo.getDiaProcessado());
        this.saidaDia.setHorariosTrabalhadosList(marcacoesDias);

        //Se possui jornada, seta novamente então com as marcações de acordo com a Jornada
        this.saidaDia.setHorariosTrabalhadosPresencaList(marcacoesDias);

        if (!isProcessandoPeriodoCompensacao()
                && this.calculo.getEntradaAPI().isProcessaCalculo()
                && this.calculo.getEntradaAPI().isProcessaAbsenteismo()) {
            this.calculo.getIntervalosService().setCalculaIntervalosJornadas(marcacoesDias);
        }

        if (this.calculo.getEntradaAPI().isProcessaCalculo()) {
            if (!this.calculo.getRegrasService().isFechamento()) {
                this.saidaDia.setHorasTrabalhadas(this.calculo.getSaldosService().getTotalHorasTrabalhadas());
            } else {
                this.calculo.getFuncionarioService().getFuncionarioEmpresaFechamentoService().setConsultaHorasTrabalhadasFechamento(this.saidaDia);
            }
        }

        if (this.calculo.getRegrasService().isCumprirHorario()) {
            this.saidaDia.setOcorrencias(new OcorrenciasService().getOcorrencias(this.saidaDia, Utils.getDataPadrao(0, this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada().getToleranciaOcorrencia())));
        }
    }

    private void setProcessaCalculo() {
        //Se não possui fechamento de horas no dia
        LOGGER.debug("Calculando os saldos... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        this.calculo.getSaldosService().setSaldos(this.saidaDia);

        //Se for período de compensação não calcula banco, não arredonda e não calcula DSR
        if (!isProcessandoPeriodoCompensacao()) {

            //Banco de Horas e armazena o saldo de BH do dia anterior
            if (this.calculo.getRegrasService().isBancoDeHoras()) {
                this.saidaDia.setBancodeHoras(this.calculo.getBancodeHorasService().getCalculosBH(this.saidaDia));
            }

            this.calculo.getSaldosService().setArredondamentosHorasNoturnas(this.saidaDia);

            //DSR
            if (!this.calculo.getRegrasService().isFalta()
                    && this.calculo.getEntradaAPI().isProcessaCalculo()
                    && this.calculo.getEntradaAPI().isProcessaDSR()) {
                LOGGER.debug("Calculando tabela de extras DSR... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                this.saidaDia.setDsr(this.calculo.getDsrService().getCalculosTabelaExtrasDSR(this.saidaDia.getTabelaExtrasList()));
            }
        }
    }

    private void setPreencherPrimeiraLinhaTabelasComZero() {

        //Tabela de extras
        Integer totalExtras = this.saidaDia.getTabelaExtrasList().size();
        this.calculo.getTipoDiaList()
                .stream()
                .filter(f -> (totalExtras > 0
                        ? !Objects.equals(f.getIdTipodia(), this.calculo.getRegrasService().getTipoDia().getIdTipodia())
                        : true))
                .forEach(tipoDia -> {
                    this.saidaDia
                            .getTabelaExtrasList().add(novoTipoDia(tipoDia));
                });

        //Tabela de banco de horas
        Integer totalBH = this.saidaDia.getBancodeHoras().getTabelaTiposdeDia().size();
        this.calculo.getTipoDiaList()
                .stream()
                .filter(f -> (totalBH > 0
                        ? !Objects.equals(f.getIdTipodia(), this.calculo.getRegrasService().getTipoDia().getIdTipodia())
                        : true))
                .forEach(tipoDia -> {
                    this.saidaDia.getBancodeHoras()
                            .getTabelaTiposdeDia().add(novoTipoDia(tipoDia));
                });
    }

    private TabelaSequenciaPercentuais novoTipoDia(TipoDia tipoDia) {
        TabelaSequenciaPercentuais tabela = new TabelaSequenciaPercentuais();
        tabela.setTipoDia(tipoDia);
        tabela.setIdSequencia(1);
        tabela.setAcrescimo(0d);
        return tabela;
    }

    private void setRegras() {
        LOGGER.debug("Aplicando as regras... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        this.calculo.getRegrasService().setRegras(this.saidaDia.getOcorrencias(), this.saidaDia.getHorariosTrabalhadosPresencaList());
        this.saidaDia.setRegras(this.calculo.getRegrasService().getRegras());
    }

    private void setAtualizaStatusAposTodasAsOperacoes() {
        if (this.saidaDia.getSaldoExtras() != null && this.saidaDia.getSaldoExtras().getDiurnas().plus(this.saidaDia.getSaldoExtras().getNoturnas()).isZero()) {
            this.saidaDia.getSaldoExtras().setPossui(false);
        }
        if (this.saidaDia.getSaldoAusencias() != null && this.saidaDia.getSaldoAusencias().getDiurnas().plus(this.saidaDia.getSaldoAusencias().getNoturnas()).isZero()) {
            this.saidaDia.getSaldoAusencias().setPossui(false);
        }
    }

    private boolean isProcessandoPeriodoCompensacao() {
        return this.calculo.isPeriodoCompensacaoAusencias()
                || this.calculo.isPeriodoCompensacaoExtras();
    }

}
