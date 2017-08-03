package com.topdata.toppontoweb.services.gerafrequencia.entity.calculo;

import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.fiscal.Arquivo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesTratadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.DsrApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Ocorrencia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Regra;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import com.topdata.toppontoweb.utils.DateHelper;

/**
 * Possui todas as informações de retorno dos cálculos e regras
 *
 * @author enio.junior
 */
public class SaidaDia implements Serializable {

    private Date data;
    private Funcionario funcionario;
    private FuncionarioBancoHoras funcionarioBancoHoras;
    private List<HorarioMarcacao> horariosPrevistosList;
    private List<MarcacoesDia> horariosTrabalhadosList;
    private List<MarcacoesDia> horariosTrabalhadosOriginaisEquipamentoList;
    private List<MarcacoesDia> horariosTrabalhadosPresencaList;
    private List<MarcacoesTratadas> horariosTrabalhadosTratadosList;
    private Ocorrencia ocorrencias;
    private Regra regras;
    private Saldo horasPrevistas;
    private SaldoTrabalhadas horasTrabalhadas;
    private Saldo saldoNormais;
    private SaldoExtras saldoExtras;
    private SaldoAusencias saldoAusencias;
    private Double percentualAdicionalNoturno;
    private BancodeHorasApi bancodeHoras;
    private List<TabelaSequenciaPercentuais> tabelaExtrasList;
    private DsrApi dsr;
    private List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoAPIList;
    private Intervalos intervalos;
    private int idHorario;
    private Date dataPrimeiroDiaBancodeHoras;
    private Duration saldoPrimeiroDiaBancodeHoras;
    private Duration saldoAnteriorDiaPeriodoBancodeHoras;
    private int idTipoDia;
    private List<Arquivo> listaAFDT;
    private List<Arquivo> listaACJEF;
    private List<Arquivo> listaHorariosContratuaisACJEF;

    private Calculo calculo;

    public SaidaDia() {
    }

    public SaidaDia(Calculo calculo) {
        this.calculo = calculo;
        this.horariosPrevistosList = new ArrayList<>();
        this.funcionarioBancoHorasFechamentoAPIList = new ArrayList<>();
        this.horariosTrabalhadosList = new ArrayList<>();
        this.horariosTrabalhadosOriginaisEquipamentoList = new ArrayList<>();
        this.horariosTrabalhadosTratadosList = new ArrayList<>();
        this.horariosTrabalhadosPresencaList = new ArrayList<>();
        this.tabelaExtrasList = new ArrayList<>();
        this.listaAFDT = new ArrayList<>();
        this.listaACJEF = new ArrayList<>();
        this.listaHorariosContratuaisACJEF = new ArrayList<>();
        this.saldoPrimeiroDiaBancodeHoras = Duration.ZERO;
        this.dataPrimeiroDiaBancodeHoras = Utils.getDataDefault();
        this.saldoAnteriorDiaPeriodoBancodeHoras = Duration.ZERO;
        this.idHorario = 0;
        this.idTipoDia = 0;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public FuncionarioBancoHoras getFuncionarioBancoHoras() {
        return funcionarioBancoHoras;
    }

    public void setFuncionarioBancoHoras(FuncionarioBancoHoras funcionarioBancoHoras) {
        this.funcionarioBancoHoras = funcionarioBancoHoras;
    }

    public List<HorarioMarcacao> getHorariosPrevistosList() {
        return horariosPrevistosList;
    }

    public void setHorariosPrevistosList(List<HorarioMarcacao> horariosPrevistosList) {
        this.horariosPrevistosList = horariosPrevistosList;
    }

    public List<MarcacoesDia> getHorariosTrabalhadosList() {
        return horariosTrabalhadosList;
    }

    public void setHorariosTrabalhadosList(List<MarcacoesDia> horariosTrabalhadosList) {
        this.horariosTrabalhadosList = horariosTrabalhadosList;
    }

    public List<MarcacoesDia> getHorariosTrabalhadosOriginaisEquipamentoList() {
        return horariosTrabalhadosOriginaisEquipamentoList;
    }

    public void setHorariosTrabalhadosOriginaisEquipamentoList(List<MarcacoesDia> horariosTrabalhadosOriginaisEquipamentoList) {
        this.horariosTrabalhadosOriginaisEquipamentoList = horariosTrabalhadosOriginaisEquipamentoList;
    }

    public Ocorrencia getOcorrencias() {
        return ocorrencias;
    }

    public void setOcorrencias(Ocorrencia ocorrencias) {
        this.ocorrencias = ocorrencias;
    }

    public Regra getRegras() {
        return regras;
    }

    public void setRegras(Regra regras) {
        this.regras = regras;
    }

    public Saldo getHorasPrevistas() {
        return horasPrevistas;
    }

    public void setHorasPrevistas(Saldo horasPrevistas) {
        this.horasPrevistas = horasPrevistas;
    }

    public SaldoTrabalhadas getHorasTrabalhadas() {
        return horasTrabalhadas;
    }

    public void setHorasTrabalhadas(SaldoTrabalhadas horasTrabalhadas) {
        this.horasTrabalhadas = horasTrabalhadas;
    }

    public Saldo getSaldoNormais() {
        return saldoNormais;
    }

    public void setSaldoNormais(Saldo saldoNormais) {
        this.saldoNormais = saldoNormais;
    }

    public SaldoExtras getSaldoExtras() {
        return saldoExtras;
    }

    public void setSaldoExtras(SaldoExtras saldoExtras) {
        this.saldoExtras = saldoExtras;
    }

    public SaldoAusencias getSaldoAusencias() {
        return saldoAusencias;
    }

    public void setSaldoAusencias(SaldoAusencias saldoAusencias) {
        this.saldoAusencias = saldoAusencias;
    }

    public Double getPercentualAdicionalNoturno() {
        return percentualAdicionalNoturno;
    }

    public void setPercentualAdicionalNoturno(Double percentualAdicionalNoturno) {
        this.percentualAdicionalNoturno = percentualAdicionalNoturno;
    }

    public BancodeHorasApi getBancodeHoras() {
        return bancodeHoras;
    }

    public void setBancodeHoras(BancodeHorasApi bancodeHoras) {
        this.bancodeHoras = bancodeHoras;
    }

    public List<TabelaSequenciaPercentuais> getTabelaExtrasList() {
        return tabelaExtrasList;
    }

    public void setTabelaExtrasList(List<TabelaSequenciaPercentuais> tabelaExtrasList) {
        this.tabelaExtrasList = tabelaExtrasList;
    }

    public DsrApi getDsr() {
        return dsr;
    }

    public void setDsr(DsrApi dsr) {
        this.dsr = dsr;
    }

    public List<FuncionarioBancoHorasFechamento> getFuncionarioBancoHorasFechamentoAPIList() {
        return funcionarioBancoHorasFechamentoAPIList;
    }

    public void setFuncionarioBancoHorasFechamentoAPIList(List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoAPIList) {
        this.funcionarioBancoHorasFechamentoAPIList = funcionarioBancoHorasFechamentoAPIList;
    }

    public Intervalos getIntervalos() {
        return intervalos;
    }

    public void setIntervalos(Intervalos intervalos) {
        this.intervalos = intervalos;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }

    public List<MarcacoesTratadas> getHorariosTrabalhadosTratadosList() {
        return horariosTrabalhadosTratadosList;
    }

    public void setHorariosTrabalhadosTratadosList(List<MarcacoesTratadas> horariosTrabalhadosTratadosList) {
        this.horariosTrabalhadosTratadosList = horariosTrabalhadosTratadosList;
    }

    public List<MarcacoesDia> getHorariosTrabalhadosPresencaList() {
        return horariosTrabalhadosPresencaList;
    }

    public void setHorariosTrabalhadosPresencaList(List<MarcacoesDia> horariosTrabalhadosPresencaList) {
        this.horariosTrabalhadosPresencaList = horariosTrabalhadosPresencaList;
    }

    /**
     * Converte a SaidaDia para o tipo EmpresaFechamentoData
     *
     * @param empresaFechamentoPeriodo
     * @return EmpresaFechamentoData
     */
    public EmpresaFechamentoData toEmpresaFechamentoData(EmpresaFechamentoPeriodo empresaFechamentoPeriodo) {

        EmpresaFechamentoData empresaFechamentoData = new EmpresaFechamentoData();
        empresaFechamentoData.setIdEmpresaFechamentoPeriodo(empresaFechamentoPeriodo);
        empresaFechamentoData.setData(this.getData());
        empresaFechamentoData.setHorasPrevistasDiferencaAdicionalNoturno(
                this.getHorasPrevistas() != null
                        ? new Date(this.getHorasPrevistas().getDiferencaadicionalnoturno().toMillis())
                        : null);
        empresaFechamentoData.setHorasPrevistasDiurnas(
                this.getHorasPrevistas() != null
                        ? new Date(this.getHorasPrevistas().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setHorasPrevistasNoturnas(
                this.getHorasPrevistas() != null
                        ? new Date(this.getHorasPrevistas().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setHorasTrabalhadasDiferencaAdicionalNoturno(
                this.getHorasTrabalhadas() != null
                        ? new Date(this.getHorasTrabalhadas().getDiferencaadicionalnoturno().toMillis())
                        : null);
        empresaFechamentoData.setHorasTrabalhadasDiurnas(
                this.getHorasTrabalhadas() != null && this.getHorasTrabalhadas().getDiurnas() != null
                        ? new Date(this.getHorasTrabalhadas().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setHorasTrabalhadasNoturnas(
                this.getHorasTrabalhadas() != null && this.getHorasTrabalhadas().getNoturnas() != null
                        ? new Date(this.getHorasTrabalhadas().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setPossuiAfastadoComAbono(this.getRegras() != null
                ? this.getRegras().isAfastadoComAbono()
                : null);
        empresaFechamentoData.setPossuiAusencias(
                this.getSaldoAusencias() != null
                        ? this.getSaldoAusencias().isPossui()
                        : null);
        empresaFechamentoData.setPossuiAusenciasAbonadas(
                this.getSaldoAusencias() != null && this.getSaldoAusencias().getAbonoDia() != null
                        ? this.getSaldoAusencias().getAbonoDia().isPossui()
                        : null);
        empresaFechamentoData.setPossuiAusenciasCompensadas(
                this.getData() != null && Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()) != null
                ? Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).isPossui()
                : null);
        empresaFechamentoData.setPossuiExtras(
                this.getSaldoExtras() != null
                        ? this.getSaldoExtras().isPossui()
                        : null);
        empresaFechamentoData.setPossuiExtrasCompensacao(
                this.getData() != null && Utils.getSaldoDiaCompensando(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()) != null
                ? Utils.getSaldoDiaCompensando(this.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoDiurnas().plus(Utils.getSaldoDiaCompensando(this.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoNoturnas()).isZero() == false
                : null);
        empresaFechamentoData.setPossuiHorasPrevistas(
                this.getHorasPrevistas() != null
                        ? this.getHorasPrevistas().isPossui()
                        : null);
        empresaFechamentoData.setPossuiHorasTrabalhadas(
                this.getHorasTrabalhadas() != null
                        ? this.getHorasTrabalhadas().isPossui()
                        : null);
        empresaFechamentoData.setPossuiNormais(
                this.getSaldoNormais() != null
                        ? this.getSaldoNormais().isPossui()
                        : null);
        empresaFechamentoData.setPossuiTodasComoExtras(
                this.getSaldoExtras() != null
                        ? this.getSaldoExtras().isTodas()
                        : null);
        empresaFechamentoData.setSaldoAusenciasAbonadasDiurnas(
                this.getSaldoAusencias() != null && this.getSaldoAusencias().getAbonoDia() != null
                        ? new Date(this.getSaldoAusencias().getAbonoDia().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoAusenciasAbonadasNoturnas(
                this.getSaldoAusencias() != null && this.getSaldoAusencias().getAbonoDia() != null
                        ? new Date(this.getSaldoAusencias().getAbonoDia().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoAusenciasCompensadasDiurnas(
                this.getData() != null && Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()) != null
                ? new Date(Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas().toMillis())
                : null);
        empresaFechamentoData.setSaldoAusenciasCompensadasNoturnas(
                this.getData() != null && Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()) != null
                ? new Date(Utils.getSaldoDiaCompensado(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas().toMillis())
                : null);
        empresaFechamentoData.setSaldoAusenciasDiurnas(
                this.getSaldoAusencias() != null
                        ? new Date(this.getSaldoAusencias().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoAusenciasNoturnas(
                this.getSaldoAusencias() != null
                        ? new Date(this.getSaldoAusencias().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoExtrasCompensacaoDiurnas(
                this.getData() != null && Utils.getSaldoDiaCompensando(this.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()) != null
                ? new Date(Utils.getSaldoDiaCompensando(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoDiurnas().toMillis())
                : null);
        empresaFechamentoData.setSaldoExtrasCompensacaoNoturnas(
                this.getData() != null && Utils.getSaldoDiaCompensando(this.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()) != null
                ? new Date(Utils.getSaldoDiaCompensando(getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoNoturnas().toMillis())
                : null);
        empresaFechamentoData.setSaldoExtrasDiferencaAdicionalNoturno(
                this.getSaldoExtras() != null && this.getSaldoExtras().getDiferencaadicionalnoturno() != null
                        ? new Date(this.getSaldoExtras().getDiferencaadicionalnoturno().toMillis())
                        : null);
        empresaFechamentoData.setSaldoExtrasDiurnas(
                this.getSaldoExtras() != null
                        ? new Date(this.getSaldoExtras().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoExtrasNoturnas(
                this.getSaldoExtras() != null
                        ? new Date(this.getSaldoExtras().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoNormaisDiurnas(
                this.getSaldoNormais() != null
                        ? new Date(this.getSaldoNormais().getDiurnas().toMillis())
                        : null);
        empresaFechamentoData.setSaldoNormaisNoturnas(
                this.getSaldoNormais() != null
                        ? new Date(this.getSaldoNormais().getNoturnas().toMillis())
                        : null);
        empresaFechamentoData.setBHCredito(
                this.getBancodeHoras() != null && this.getBancodeHoras().getCredito() != null
                        ? new Date(this.getBancodeHoras().getCredito().toMillis())
                        : null);
        empresaFechamentoData.setBHDebito(
                this.getBancodeHoras() != null && this.getBancodeHoras().getDebito() != null
                        ? new Date(this.getBancodeHoras().getDebito().toMillis())
                        : null);
        empresaFechamentoData.setBHDiferencaAdicionalNoturno(
                this.getRegras() != null && this.getHorasTrabalhadas() != null
                        ? new Date(Utils.getArredondamento(Utils.getVisualizaDiferencaAdicionalNoturnoBancoDeHoras(this.getRegras().isBancoDeHoras(), this.getHorasTrabalhadas().getDiferencaadicionalnoturno())).toMillis())
                        : null);
        empresaFechamentoData.setBHGatilho(
                this.getBancodeHoras() != null && this.getBancodeHoras().getGatilho() != null
                        ? new Date(this.getBancodeHoras().getGatilho().toMillis())
                        : null);
        empresaFechamentoData.setBHSaldoAcumuladoDia(
                this.getBancodeHoras() != null && this.getBancodeHoras().getSaldoAcumuladoDia() != null
                        ? new Date(this.getBancodeHoras().getSaldoAcumuladoDia().toMillis())
                        : null);
        empresaFechamentoData.setBHSaldoAcumuladoDiaAnterior(
                this.getBancodeHoras() != null && this.getBancodeHoras().getSaldoAcumuladoDiaAnterior() != null
                        ? new Date(this.getBancodeHoras().getSaldoAcumuladoDiaAnterior().toMillis())
                        : null);
        empresaFechamentoData.setBHSaldoCredito(
                this.getBancodeHoras() != null && this.getBancodeHoras().getSaldoAcumuladoCredito() != null
                        ? new Date(this.getBancodeHoras().getSaldoAcumuladoCredito().toMillis())
                        : null);
        empresaFechamentoData.setBHSaldoDebito(
                this.getBancodeHoras() != null && this.getBancodeHoras().getSaldoAcumuladoDebito() != null
                        ? new Date(this.getBancodeHoras().getSaldoAcumuladoDebito().toMillis())
                        : null);
        empresaFechamentoData.setBHSaldoDia(
                this.getBancodeHoras() != null && this.getBancodeHoras().getSaldoDia() != null
                        ? new Date(this.getBancodeHoras().getSaldoDia().toMillis())
                        : null);
        empresaFechamentoData.setIdFuncionario(this.getFuncionario());
        if (this.getRegras().isBancoDeHoras()) {
            empresaFechamentoData.setIdFuncionarioBancoHoras(this.getFuncionarioBancoHoras());
        }
       
        return empresaFechamentoData;
    }

    /**
     * Converte a SaidaDia para o Tipo FechamentoTabelaExtras
     *
     * @param empresaFechamentoData
     * @return List<FechamentoTabelaExtras>
     */
    public List<FechamentoTabelaExtras> toFechamentoTabelaExtras(EmpresaFechamentoData empresaFechamentoData) {
        List<FechamentoTabelaExtras> fechamentoTabelaExtrasList = new ArrayList<>();

        if (this.getTabelaExtrasList() != null) {
            this.getTabelaExtrasList()
                    .stream()
                    .forEach((TabelaSequenciaPercentuais tabelaSequenciaPercentuais) -> {

                        if ((tabelaSequenciaPercentuais.getDivisaoDiurnas() != null && tabelaSequenciaPercentuais.getDivisaoNoturnas() != null)
                                && (tabelaSequenciaPercentuais.getDivisaoDiurnas().isZero() == false
                                || tabelaSequenciaPercentuais.getDivisaoNoturnas().isZero() == false)) {

                            FechamentoTabelaExtras fechamentoTabelaExtras = new FechamentoTabelaExtras();
                            fechamentoTabelaExtras.setIdEmpresaFechamentoData(empresaFechamentoData);
                            fechamentoTabelaExtras.setLimite(tabelaSequenciaPercentuais.getIdSequencia());
                            fechamentoTabelaExtras.setHoras(tabelaSequenciaPercentuais.getHoras());
                            fechamentoTabelaExtras.setPercentual(tabelaSequenciaPercentuais.getAcrescimo());
                            fechamentoTabelaExtras.setDiurnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoDiurnas())));
                            fechamentoTabelaExtras.setNoturnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoNoturnas())));
                            fechamentoTabelaExtrasList.add(fechamentoTabelaExtras);

                        }
                    });
        }

        return fechamentoTabelaExtrasList;
    }

    /**
     * Converte a SaidaDia para o tipo FechamentoTabelaExtrasDSR
     *
     * @param empresaFechamentoData
     * @return List<FechamentoTabelaExtrasDSR>
     */
    public List<FechamentoTabelaExtrasDSR> toFechamentoTabelaExtrasDSR(EmpresaFechamentoData empresaFechamentoData) {
        List<FechamentoTabelaExtrasDSR> fechamentoTabelaExtrasDSRList = new ArrayList<>();

        if (this.getDsr() != null && this.getDsr().getTabelaExtrasDSRList() != null) {
            this.getDsr().getTabelaExtrasDSRList()
                    .stream()
                    .forEach((TabelaSequenciaPercentuais tabelaSequenciaPercentuais) -> {

                        if ((tabelaSequenciaPercentuais.getDivisaoDiurnas() != null && tabelaSequenciaPercentuais.getDivisaoNoturnas() != null)
                                && (tabelaSequenciaPercentuais.getDivisaoDiurnas().isZero() == false
                                || tabelaSequenciaPercentuais.getDivisaoNoturnas().isZero() == false)) {

                            FechamentoTabelaExtrasDSR fechamentoTabelaExtrasDSR = new FechamentoTabelaExtrasDSR();
                            fechamentoTabelaExtrasDSR.setIdEmpresaFechamentoData(empresaFechamentoData);
                            fechamentoTabelaExtrasDSR.setLimite(tabelaSequenciaPercentuais.getIdSequencia());
                            fechamentoTabelaExtrasDSR.setDiurnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoDiurnas())));
                            fechamentoTabelaExtrasDSR.setNoturnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoNoturnas())));
                            fechamentoTabelaExtrasDSRList.add(fechamentoTabelaExtrasDSR);
                        }
                    });
        }

        return fechamentoTabelaExtrasDSRList;
    }

    /**
     * Converte a SaidaDia para o tipo FechamentoBHTabelaAcrescimos
     *
     * @param empresaFechamentoData
     * @return List<FechamentoBHTabelaAcrescimos>
     */
    public List<FechamentoBHTabelaAcrescimos> toFechamentoBHTabelaAcrescimos(EmpresaFechamentoData empresaFechamentoData) {
        List<FechamentoBHTabelaAcrescimos> fechamentoBHTabelaAcrescimosList = new ArrayList<>();

        if (this.getBancodeHoras() != null && this.getBancodeHoras().getTabelaTiposdeDia() != null) {
            this.getBancodeHoras().getTabelaTiposdeDia()
                    .stream()
                    .forEach((TabelaSequenciaPercentuais tabelaSequenciaPercentuais) -> {

                        if ((tabelaSequenciaPercentuais.getDivisaoDiurnas() != null && tabelaSequenciaPercentuais.getDivisaoNoturnas() != null)
                                && (tabelaSequenciaPercentuais.getDivisaoDiurnas().isZero() == false
                                || tabelaSequenciaPercentuais.getDivisaoNoturnas().isZero() == false)) {

                            FechamentoBHTabelaAcrescimos fechamentoBHTabelaAcrescimos = new FechamentoBHTabelaAcrescimos();
                            fechamentoBHTabelaAcrescimos.setIdEmpresaFechamentoData(empresaFechamentoData);
                            fechamentoBHTabelaAcrescimos.setLimite(tabelaSequenciaPercentuais.getIdSequencia());
                            fechamentoBHTabelaAcrescimos.setHoras(tabelaSequenciaPercentuais.getHoras());
                            fechamentoBHTabelaAcrescimos.setPercentual(tabelaSequenciaPercentuais.getAcrescimo());
                            fechamentoBHTabelaAcrescimos.setDivisaoDiurnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoDiurnas())));
                            fechamentoBHTabelaAcrescimos.setDivisaoNoturnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoNoturnas())));
                            fechamentoBHTabelaAcrescimos.setResultadoDiurnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getResultadoDiurnas())));
                            fechamentoBHTabelaAcrescimos.setResultadoNoturnas(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getResultadoNoturnas())));
                            fechamentoBHTabelaAcrescimosList.add(fechamentoBHTabelaAcrescimos);
                        }

                    });
        }

        return fechamentoBHTabelaAcrescimosList;
    }

    /**
     * Converte a SaidaDia para o tipo FechamentoBHTabelaSomenteNoturnas
     *
     * @param empresaFechamentoData
     * @return List<FechamentoBHTabelaSomenteNoturnas>
     */
    public List<FechamentoBHTabelaSomenteNoturnas> toFechamentoBHTabelaSomenteNoturnas(EmpresaFechamentoData empresaFechamentoData) {
        List<FechamentoBHTabelaSomenteNoturnas> fechamentoBHTabelaSomenteNoturnasList = new ArrayList<>();

        if (this.getBancodeHoras() != null && this.getBancodeHoras().getAcrescimosSomenteNoturnasList() != null) {
            this.getBancodeHoras().getAcrescimosSomenteNoturnasList()
                    .stream()
                    .forEach((TabelaSequenciaPercentuais tabelaSequenciaPercentuais) -> {

                        if ((tabelaSequenciaPercentuais.getDivisaoDiurnas() != null && tabelaSequenciaPercentuais.getDivisaoNoturnas() != null)
                                && (tabelaSequenciaPercentuais.getDivisaoDiurnas().isZero() == false
                                || tabelaSequenciaPercentuais.getDivisaoNoturnas().isZero() == false)) {

                            FechamentoBHTabelaSomenteNoturnas fechamentoBHTabelaSomenteNoturnas = new FechamentoBHTabelaSomenteNoturnas();
                            fechamentoBHTabelaSomenteNoturnas.setIdEmpresaFechamentoData(empresaFechamentoData);
                            fechamentoBHTabelaSomenteNoturnas.setLimite(tabelaSequenciaPercentuais.getIdSequencia());
                            fechamentoBHTabelaSomenteNoturnas.setHoras(tabelaSequenciaPercentuais.getHoras());
                            fechamentoBHTabelaSomenteNoturnas.setPercentual(tabelaSequenciaPercentuais.getAcrescimo());
                            fechamentoBHTabelaSomenteNoturnas.setDivisao(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getDivisaoNoturnas())));
                            fechamentoBHTabelaSomenteNoturnas.setResultado(DateHelper.ajustarData(Utils.FormatoDataHora(tabelaSequenciaPercentuais.getResultadoNoturnas())));
                            fechamentoBHTabelaSomenteNoturnasList.add(fechamentoBHTabelaSomenteNoturnas);

                        }
                    });
        }

        return fechamentoBHTabelaSomenteNoturnasList;
    }

    public Date getDataPrimeiroDiaBancodeHoras() {
        return dataPrimeiroDiaBancodeHoras;
    }

    public void setDataPrimeiroDiaBancodeHoras(Date dataPrimeiroDiaBancodeHoras) {
        this.dataPrimeiroDiaBancodeHoras = dataPrimeiroDiaBancodeHoras;
    }

    public Duration getSaldoPrimeiroDiaBancodeHoras() {
        return saldoPrimeiroDiaBancodeHoras;
    }

    public void setSaldoPrimeiroDiaBancodeHoras(Duration saldoPrimeiroDiaBancodeHoras) {
        this.saldoPrimeiroDiaBancodeHoras = saldoPrimeiroDiaBancodeHoras;
    }

    public int getIdTipoDia() {
        return idTipoDia;
    }

    public void setIdTipoDia(int idTipoDia) {
        this.idTipoDia = idTipoDia;
    }

    public Duration getDiferencaAdicionalNoturnoHoraExtra() {
        return Utils.getArredondamento(Utils.getVisualizaDiferencaAdicionalNoturnoHoraExtraMain(this.getRegras().isBancoDeHoras(), this.getHorasTrabalhadas().getDiferencaadicionalnoturno()));
    }

    public Duration getSaldoAnteriorDiaPeriodoBancodeHoras() {
        return saldoAnteriorDiaPeriodoBancodeHoras;
    }

    public void setSaldoAnteriorDiaPeriodoBancodeHoras(Duration saldoAnteriorDiaPeriodoBancodeHoras) {
        this.saldoAnteriorDiaPeriodoBancodeHoras = saldoAnteriorDiaPeriodoBancodeHoras;
    }

    public Duration getDuracaoTurnoTotal() {
        Duration diurnas = this.getHorasPrevistas() != null ? this.getHorasPrevistas().getDiurnas() : Duration.ZERO;
        Duration noturnas = this.getHorasPrevistas() != null ? this.getHorasPrevistas().getNoturnas() : Duration.ZERO;
        return diurnas.plus(noturnas);
    }

    public List<Arquivo> getListaAFDT() {
        return listaAFDT;
    }

    public void setListaAFDT(List<Arquivo> listaAFDT) {
        this.listaAFDT = listaAFDT;
    }

    public List<Arquivo> getListaACJEF() {
        return listaACJEF;
    }

    public void setListaACJEF(List<Arquivo> listaACJEF) {
        this.listaACJEF = listaACJEF;
    }

    public List<Arquivo> getListaHorariosContratuaisACJEF() {
        return listaHorariosContratuaisACJEF;
    }

    public void setListaHorariosContratuaisACJEF(List<Arquivo> listaHorariosContratuaisACJEF) {
        this.listaHorariosContratuaisACJEF = listaHorariosContratuaisACJEF;
    }

}
