package com.topdata.toppontoweb.services.gerafrequencia.entity.calculo;

import java.time.Duration;
import java.util.Date;
import java.util.List;

import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.services.bancodehoras.BancodeHorasService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasSaldoDia;
import com.topdata.toppontoweb.services.gerafrequencia.services.bancodehoras.SaldosBHMensal;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioMarcacoesService;
import com.topdata.toppontoweb.services.gerafrequencia.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.DsrService;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.IntervalosService;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.RegrasService;
import com.topdata.toppontoweb.services.gerafrequencia.services.saldos.SaldosService;

/**
 * Classe principal de controle com os dados para o cálculo
 *
 * @author enio.junior
 */
public final class Calculo {

    private Date diaProcessado;
    private final EntradaApi entradaAPI;
    private final FuncionarioService funcionarioService;
    private final FuncionarioMarcacoesService funcionarioMarcacoesService;
    private final DsrService dsrService;
    private final RegrasService regrasService;
    private final SaldosService saldosService;
    private final BancodeHorasService bancodeHorasService;
    private final IntervalosService intervalosService;
    private ConfiguracoesGerais configuracoesGerais;
    private List<TipoDia> tipoDiaList;
    private boolean periodoCompensacaoAusencias;
    private boolean periodoCompensacaoExtras;
    private boolean periodoSaldoPrimeiroDiaBancodeHoras;
    private boolean periodoSaldoAnteriorDiaPeriodoBancodeHoras;
    private final String nomeFuncionario;
    
    //BH
    private Duration saldoAcumuladoDiaAnteriorBancoDeHoras;
    private Duration creditoAcumuladoDiaAnteriorBancoDeHoras; 
    private Duration debitoAcumuladoDiaAnteriorBancoDeHoras; 
    private Date diaComSubtotal;
    private final BancodeHorasSaldoDia saldoDiaBancodeHoras;
    private final SaldosBHMensal saldosBHMensal;
    
    public Calculo(EntradaApi entradaAPI, BancodeHorasSaldoDia saldoDiaBancodeHoras) {
        this.entradaAPI = entradaAPI;
        this.saldoDiaBancodeHoras = saldoDiaBancodeHoras;
        this.saldosBHMensal = new SaldosBHMensal(this);
        this.funcionarioService = new FuncionarioService(this);
        this.regrasService = new RegrasService(this);
        this.bancodeHorasService = new BancodeHorasService(this);
        this.saldosService = new SaldosService(this);
        this.funcionarioMarcacoesService = new FuncionarioMarcacoesService(this);
        this.dsrService = new DsrService(this);
        this.intervalosService = new IntervalosService();
        this.configuracoesGerais = new ConfiguracoesGerais();
        this.tipoDiaList = entradaAPI.getTipoDiaList();
        this.setSaldoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.setCreditoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.setDebitoAcumuladoDiaAnteriorBancoDeHoras(Duration.ZERO);
        this.setDiaComSubtotal(null);
        this.nomeFuncionario = entradaAPI.getFuncionario().getNome();
    }

    public synchronized Date getDiaProcessado() {
        return diaProcessado;
    }

    public synchronized void setDiaProcessado(Date diaProcessado) {
        this.diaProcessado = diaProcessado;
    }

    public EntradaApi getEntradaAPI() {
        return entradaAPI;
    }

    public FuncionarioService getFuncionarioService() {
        return funcionarioService;
    }

    public FuncionarioMarcacoesService getFuncionarioMarcacoesService() {
        return funcionarioMarcacoesService;
    }

    public DsrService getDsrService() {
        return dsrService;
    }

    public RegrasService getRegrasService() {
        return regrasService;
    }

    public SaldosService getSaldosService() {
        return saldosService;
    }

    public synchronized BancodeHorasService getBancodeHorasService() {
        return bancodeHorasService;
    }

    public Duration getCreditoAcumuladoDiaAnteriorBancoDeHoras() {
        return creditoAcumuladoDiaAnteriorBancoDeHoras;
    }

    public void setCreditoAcumuladoDiaAnteriorBancoDeHoras(Duration creditoAcumuladoDiaAnteriorBancoDeHoras) {
        this.creditoAcumuladoDiaAnteriorBancoDeHoras = creditoAcumuladoDiaAnteriorBancoDeHoras;
    }

    public Duration getDebitoAcumuladoDiaAnteriorBancoDeHoras() {
        return debitoAcumuladoDiaAnteriorBancoDeHoras;
    }

    public void setDebitoAcumuladoDiaAnteriorBancoDeHoras(Duration debitoAcumuladoDiaAnteriorBancoDeHoras) {
        this.debitoAcumuladoDiaAnteriorBancoDeHoras = debitoAcumuladoDiaAnteriorBancoDeHoras;
    }
    
    public synchronized Duration getSaldoAcumuladoDiaAnteriorBancoDeHoras() {
        return saldoAcumuladoDiaAnteriorBancoDeHoras;
    }

    public void setSaldoAcumuladoDiaAnteriorBancoDeHoras(Duration saldoAcumuladoDiaAnteriorBancoDeHoras) {
        this.saldoAcumuladoDiaAnteriorBancoDeHoras = saldoAcumuladoDiaAnteriorBancoDeHoras;
    }

    public boolean isPeriodoCompensacaoAusencias() {
        return periodoCompensacaoAusencias;
    }

    public synchronized void setPeriodoCompensacaoAusencias(boolean periodoCompensacaoAusencias) {
        this.periodoCompensacaoAusencias = periodoCompensacaoAusencias;
    }

    public boolean isPeriodoCompensacaoExtras() {
        return periodoCompensacaoExtras;
    }

    public synchronized void setPeriodoCompensacaoExtras(boolean periodoCompensacaoExtras) {
        this.periodoCompensacaoExtras = periodoCompensacaoExtras;
    }

    public IntervalosService getIntervalosService() {
        return intervalosService;
    }

    public boolean isPeriodoSaldoPrimeiroDiaBancodeHoras() {
        return periodoSaldoPrimeiroDiaBancodeHoras;
    }

    public void setPeriodoSaldoPrimeiroDiaBancodeHoras(boolean periodoSaldoPrimeiroDiaBancodeHoras) {
        this.periodoSaldoPrimeiroDiaBancodeHoras = periodoSaldoPrimeiroDiaBancodeHoras;
    }

    public boolean isPeriodoSaldoAnteriorDiaPeriodoBancodeHoras() {
        return periodoSaldoAnteriorDiaPeriodoBancodeHoras;
    }

    public void setPeriodoSaldoAnteriorDiaPeriodoBancodeHoras(boolean periodoSaldoAnteriorDiaPeriodoBancodeHoras) {
        this.periodoSaldoAnteriorDiaPeriodoBancodeHoras = periodoSaldoAnteriorDiaPeriodoBancodeHoras;
    }

    public List<TipoDia> getTipoDiaList() {
        return tipoDiaList;
    }

    public void setTipoDiaList(List<TipoDia> tipoDiaList) {
        this.tipoDiaList = tipoDiaList;
    }

    public ConfiguracoesGerais getConfiguracoesGerais() {
        return configuracoesGerais;
    }

    public void setConfiguracoesGerais(ConfiguracoesGerais configuracoesGerais) {
        this.configuracoesGerais = configuracoesGerais;
    }

    public SaldosBHMensal getSaldosBHMensal() {
        return saldosBHMensal;
    }

    public BancodeHorasSaldoDia getSaldoDiaBancodeHoras() {
        return saldoDiaBancodeHoras;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public Date getDiaComSubtotal() {
        return diaComSubtotal;
    }

    public void setDiaComSubtotal(Date diaComSubtotal) {
        this.diaComSubtotal = diaComSubtotal;
    }
    
}
