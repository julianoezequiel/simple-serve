package com.topdata.toppontoweb.dto.gerafrequencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.UUID;
import java.util.stream.Collectors;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.TCalculoPrincipal;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler.ESTADO_PROCESSO;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * Objeto de status do processo do gera frequencia
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(value = {
    "template",
    "parametros",
    "dataSource",
    "threadList",
    "saidaPadraoList",
    "timerMonitor",
    "calculoPrincipal",
    "operacao",
    "inicio",
    "termino",
    "operacao",
    "operador",
    "mapAux",
    "marcacoesInvalidas"
}, ignoreUnknown = true)
public class GeraFrequenciaStatusTransfer {

    private String id;
    private Double percentual = 0d;
    private Date data;

    private String template;
    private Map<String, Object> parametros = new HashMap<>();
    private JRBeanCollectionDataSource dataSource;
    private List<Thread> threadList;
    private List<SaidaPadrao> saidaPadraoList;
    private ESTADO_PROCESSO estado_processo = ESTADO_PROCESSO.AGENDANDO_PROCESSO;
    private Timer timerMonitor;
    private TCalculoPrincipal calculoPrincipal;
    private Date inicio;
    private Date termino;
    private CONSTANTES.Enum_OPERACAO operacao;
    private Operador operador;
    private boolean carregarMarcacoesInvalidas = false;
    private boolean possuiInconsistencias = false;
    private boolean retrato = true;
    private boolean processarMarcacoesInvalidas = false;

    private CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO;

    public HashMap<String, Object> mapAux = new HashMap<>();

    public GeraFrequenciaStatusTransfer(CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) {
        super();
        this.saidaPadraoList = new ArrayList<>();
        this.id = UUID.randomUUID().toString();
        this.threadList = new ArrayList<>();
        this.tIPO_PROCESSO = tIPO_PROCESSO;
        this.data = new Date();
        this.mapAux = new HashMap<>();
    }

    public synchronized void addPercentual(Integer percIndividual) {
        this.percentual += percIndividual;
    }

    public synchronized void addPercentual(Double percIndividual) {
        this.percentual += percIndividual;
    }

    public synchronized String getId() {
        return id;
    }

    public synchronized void setId(String id) {
        this.id = id;
    }

    public synchronized Double getPercentual() {
        return percentual;
    }

    public synchronized void setPercentual(Double percentual) {
        this.percentual = percentual;
    }

    public synchronized Date getData() {
        return data;
    }

    public synchronized void setData(Date data) {
        this.data = data;
    }

    public synchronized String getTemplate() {
        return template;
    }

    public synchronized void setTemplate(String template) {
        this.template = template;
    }

    public synchronized Map<String, Object> getParametros() {
        return parametros;
    }

    public synchronized void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public synchronized JRBeanCollectionDataSource getDataSource() {
        return dataSource;
    }

    public synchronized void setDataSource(JRBeanCollectionDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public synchronized List<SaidaPadrao> getSaidaPadraoList() {
        return saidaPadraoList;
    }

    public synchronized void setSaidaPadraoList(List<SaidaPadrao> saidaPadraoList) {
        this.saidaPadraoList = saidaPadraoList;
    }

    public synchronized List<Thread> getThreadList() {
        return threadList;
    }

    public synchronized void setThreadList(List<Thread> threadList) {
        this.threadList = threadList;
    }

    public synchronized void addThead(Thread t) {
        List<Thread> list = this.threadList.stream().filter(tr -> tr.isAlive()).collect(Collectors.toList());
        this.threadList = list;
        this.threadList.add(t);
    }

    public synchronized void setEstado_processo(ESTADO_PROCESSO estado_processo) {
        this.estado_processo = estado_processo;
    }

    public synchronized ESTADO_PROCESSO getEstado_processo() {
        return estado_processo;
    }

    public synchronized Timer getTimerMonitor() {
        return timerMonitor;
    }

    public synchronized void setTimerMonitor(Timer timerMonitor) {
        this.timerMonitor = timerMonitor;
    }

    public synchronized CONSTANTES.Enum_TIPO_PROCESSO gettIPO_PROCESSO() {
        return tIPO_PROCESSO;
    }

    public synchronized void settIPO_PROCESSO(CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) {
        this.tIPO_PROCESSO = tIPO_PROCESSO;
    }

    public synchronized Date getInicio() {
        return inicio;
    }

    public synchronized void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public synchronized Date getTermino() {
        return termino;
    }

    public synchronized void setTermino(Date termino) {
        this.termino = termino;
    }

    public synchronized CONSTANTES.Enum_OPERACAO getOperacao() {
        return operacao;
    }

    public synchronized void setOperacao(CONSTANTES.Enum_OPERACAO operacao) {
        this.operacao = operacao;
    }

    public synchronized Operador getOperador() {
        return operador;
    }

    public synchronized void setOperador(Operador operador) {
        this.operador = operador;
    }

    public synchronized TCalculoPrincipal getCalculoPrincipal() {
        return calculoPrincipal;
    }

    public synchronized void setCalculoPrincipal(TCalculoPrincipal calculoPrincipal) {
        this.calculoPrincipal = calculoPrincipal;
    }

    public synchronized boolean isCarregarMarcacoesInvalidas() {
        return carregarMarcacoesInvalidas;
    }

    public synchronized void setCarregarMarcacoesInvalidas(boolean carregarMarcacoesInvalidas) {
        this.carregarMarcacoesInvalidas = carregarMarcacoesInvalidas;
    }

    public synchronized HashMap<String, Object> getMapAux() {
        return mapAux;
    }

    public synchronized void setMapAux(HashMap<String, Object> mapAux) {
        this.mapAux = mapAux;
    }

    public synchronized boolean isPossuiInconsistencias() {
        return possuiInconsistencias;
    }

    public synchronized void setPossuiInconsistencias(boolean possuiInconsistencias) {
        this.possuiInconsistencias = possuiInconsistencias;
    }

    /**
     * @return the retrato
     */
    public synchronized boolean isRetrato() {
        return retrato;
    }

    /**
     * @param retrato the retrato to set
     */
    public synchronized void setRetrato(boolean retrato) {
        this.retrato = retrato;
    }

    public void setRetrato(int pageWidth, int pageHeight) {
        this.retrato = pageHeight > pageWidth;
    }

    public boolean isProcessarMarcacoesInvalidas() {
        return processarMarcacoesInvalidas;
    }

    public void setProcessarMarcacoesInvalidas(boolean processarMarcacoesInvalidas) {
        this.processarMarcacoesInvalidas = processarMarcacoesInvalidas;
    }

}
