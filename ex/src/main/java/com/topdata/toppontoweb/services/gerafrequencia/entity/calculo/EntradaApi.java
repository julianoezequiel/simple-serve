package com.topdata.toppontoweb.services.gerafrequencia.entity.calculo;

import com.topdata.toppontoweb.entity.configuracoes.ConfiguracoesGerais;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.entity.jornada.TipoJornada;
import com.topdata.toppontoweb.entity.marcacoes.EncaixeMarcacao;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensando;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.SaidaMainService;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Dados necessários para que a API possa realizar os cálculos
 *
 * @author enio.junior
 */
@Component
public class EntradaApi {

    private Date dataInicioPeriodo;
    private Date dataFimPeriodo;
    private Funcionario funcionario;
    private List<Dsr> dsrList;
    private ConfiguracoesGerais configuracoesGerais;
    private List<TipoDia> tipoDiaList;
    private List<TipoJornada> tipoJornadaList;
    private HashMap<Integer, StatusMarcacao> statusMarcacao;
    private HashMap<Integer, EncaixeMarcacao> encaixeMarcacao;
    private HashMap<Integer, Motivo> motivo;
    private SaidaMainService saidaMain;
    private String idrelatorio;
    private int qtdDias;
    private boolean processaCalculo = true;
    private boolean processaAFDT = true;
    private boolean processaACJEF = true;
    private boolean processaDSR = true;
    private boolean processaAbsenteismo = true;
    private boolean processaPrimeiraInconsistencia = false;
    private boolean processaManutencaoMarcacoes = false;
    private Long quantidadeJornadasPorPeriodo;

    //1º Criação da lista de compensações de Ausências do dia
    private List<SaldoDiaCompensado> saldoDiaCompensadoList;

    //2º Criação da lista de compensações de Extras que compensaram um dia
    private List<SaldoDiaCompensando> saldoDiaCompensandoList;

    public EntradaApi() {
        this.saldoDiaCompensadoList = new ArrayList<>();
        this.saldoDiaCompensandoList = new ArrayList<>();
        
        this.quantidadeJornadasPorPeriodo = 0l;
    }

    public EntradaApi(Date dataInicioPeriodo, Date dataFimPeriodo, Funcionario funcionario, List<Dsr> dsrList, ConfiguracoesGerais configuracoesGerais) {
        this();
        this.dataInicioPeriodo = dataInicioPeriodo;
        this.dataFimPeriodo = dataFimPeriodo;
        this.funcionario = funcionario;
        this.dsrList = dsrList;
        this.configuracoesGerais = configuracoesGerais;
        this.quantidadeJornadasPorPeriodo = 0l;
    }

    public Date getDataInicioPeriodo() {
        return dataInicioPeriodo;
    }

    public void setDataInicioPeriodo(Date dataInicioPeriodo) {
        this.dataInicioPeriodo = dataInicioPeriodo;
    }

    public Date getDataFimPeriodo() {
        return dataFimPeriodo;
    }

    public void setDataFimPeriodo(Date dataFimPeriodo) {
        this.dataFimPeriodo = dataFimPeriodo;
    }

    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
    }

    public List<Dsr> getDsrList() {
        return dsrList;
    }

    public void setDsrList(List<Dsr> dsrList) {
        this.dsrList = dsrList;
    }

    public SaidaMainService getSaidaMain() {
        return saidaMain;
    }

    public void setSaidaMain(SaidaMainService saidaMain) {
        this.saidaMain = saidaMain;
    }

    public List<TipoDia> getTipoDiaList() {
        return tipoDiaList;
    }

    public void setTipoDiaList(List<TipoDia> tipoDiaList) {
        this.tipoDiaList = tipoDiaList;
    }

    public List<TipoJornada> getTipoJornadaList() {
        return tipoJornadaList;
    }

    public void setTipoJornadaList(List<TipoJornada> tipoJornadaList) {
        this.tipoJornadaList = tipoJornadaList;
    }

    public String getIdrelatorio() {
        return idrelatorio;
    }

    public void setIdrelatorio(String idrelatorio) {
        this.idrelatorio = idrelatorio;
    }

    public int getQtdDias() {
        return qtdDias;
    }

    public void setQtdDias(int qtdDias) {
        this.qtdDias = qtdDias;
    }

    public HashMap<Integer, StatusMarcacao> getStatusMarcacao() {
        return statusMarcacao;
    }

    public void setStatusMarcacao(HashMap<Integer, StatusMarcacao> statusMarcacao) {
        this.statusMarcacao = statusMarcacao;
    }

    public HashMap<Integer, EncaixeMarcacao> getEncaixeMarcacao() {
        return encaixeMarcacao;
    }

    public void setEncaixeMarcacao(HashMap<Integer, EncaixeMarcacao> encaixeMarcacao) {
        this.encaixeMarcacao = encaixeMarcacao;
    }

    public HashMap<Integer, Motivo> getMotivo() {
        return motivo;
    }

    public void setMotivo(HashMap<Integer, Motivo> motivo) {
        this.motivo = motivo;
    }

    public ConfiguracoesGerais getConfiguracoesGerais() {
        return configuracoesGerais;
    }

    public void setConfiguracoesGerais(ConfiguracoesGerais configuracoesGerais) {
        this.configuracoesGerais = configuracoesGerais;
    }

    public boolean isProcessaCalculo() {
        return processaCalculo;
    }

    public void setProcessaCalculo(boolean processaCalculo) {
        this.processaCalculo = processaCalculo;
    }

    public boolean isProcessaACJEF() {
        return processaACJEF;
    }

    public void setProcessaACJEF(boolean processaACJEF) {
        this.processaACJEF = processaACJEF;
    }

    public boolean isProcessaDSR() {
        return processaDSR;
    }

    public void setProcessaDSR(boolean processaDSR) {
        this.processaDSR = processaDSR;
    }

    public boolean isProcessaAbsenteismo() {
        return processaAbsenteismo;
    }

    public void setProcessaAbsenteismo(boolean processaAbsenteismo) {
        this.processaAbsenteismo = processaAbsenteismo;
    }

    public boolean isProcessaAFDT() {
        return processaAFDT;
    }

    public void setProcessaAFDT(boolean processaAFDT) {
        this.processaAFDT = processaAFDT;
    }

    public List<SaldoDiaCompensado> getSaldoDiaCompensadoList() {
        return saldoDiaCompensadoList;
    }

    public List<SaldoDiaCompensando> getSaldoDiaCompensandoList() {
        return saldoDiaCompensandoList;
    }

    public void setSaldoDiaCompensadoList(List<SaldoDiaCompensado> saldoDiaCompensadoList) {
        this.saldoDiaCompensadoList = saldoDiaCompensadoList;
    }

    public void setSaldoDiaCompensandoList(List<SaldoDiaCompensando> saldoDiaCompensandoList) {
        this.saldoDiaCompensandoList = saldoDiaCompensandoList;
    }

    /**
     * @return the quantidadeJornadasPorPeriodo
     */
    public Long getQuantidadeJornadasPorPeriodo() {
        return quantidadeJornadasPorPeriodo;
    }

    /**
     * @param quantidadeJornadasPorPeriodo the quantidadeJornadasPorPeriodo to set
     */
    public void setQuantidadeJornadasPorPeriodo(Long quantidadeJornadasPorPeriodo) {
        this.quantidadeJornadasPorPeriodo = quantidadeJornadasPorPeriodo;
    }

    public boolean isProcessaPrimeiraInconsistencia() {
        return processaPrimeiraInconsistencia;
    }

    public void setProcessaPrimeiraInconsistencia(boolean processaPrimeiraInconsistencia) {
        this.processaPrimeiraInconsistencia = processaPrimeiraInconsistencia;
    }

    public boolean isProcessaManutencaoMarcacoes() {
        return processaManutencaoMarcacoes;
    }

    public void setProcessaManutencaoMarcacoes(boolean processaManutencaoMarcacoes) {
        this.processaManutencaoMarcacoes = processaManutencaoMarcacoes;
    }
    
}
