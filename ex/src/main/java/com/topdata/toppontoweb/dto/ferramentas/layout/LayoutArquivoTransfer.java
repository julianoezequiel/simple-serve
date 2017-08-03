package com.topdata.toppontoweb.dto.ferramentas.layout;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.topdata.toppontoweb.dto.AbstractTransfer;
import com.topdata.toppontoweb.dto.ConversorEntidade;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivo;
import com.topdata.toppontoweb.entity.ferramentas.TipoLayout;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * @version 1.0.0 data 20/07/2017
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(value = {"toEntidade"}, ignoreUnknown = true)
public class LayoutArquivoTransfer extends AbstractTransfer implements ConversorEntidade<LayoutArquivo> {

    private Integer id;
    private String descricao;
    private String cabecalho;
    private Boolean somaContadorCabecalho;
    private String formato;
    private String formatoDataPeriodo;
    private String formatoDataExportacao;
    private Boolean sabadoDiaUtil;
    private Boolean domingoDiaUtil;
    private String delimitador;
    private Boolean horaSexagesimal;
    private TipoLayout tipoLayout;
    private Boolean porLinha;
    private List<LayoutArquivoGrupoTransfer> layoutArquivoGrupoTransferList = new ArrayList<>();

    public LayoutArquivoTransfer() {
    }

    public LayoutArquivoTransfer(LayoutArquivo layoutArquivo) {
        this.id = layoutArquivo.getIdLayoutArquivo();
        this.descricao = layoutArquivo.getDescricao();
        this.cabecalho = layoutArquivo.getCabecalho();
        this.delimitador = layoutArquivo.getDelimitador();
        this.domingoDiaUtil = layoutArquivo.getDomingoDiaUtil();
        this.formato = layoutArquivo.getFormato();
        this.formatoDataExportacao = layoutArquivo.getFormatoDataExportacao();
        this.formatoDataPeriodo = layoutArquivo.getFormatoDataPeriodo();
        this.horaSexagesimal = layoutArquivo.getHoraSexagesimal();
        this.sabadoDiaUtil = layoutArquivo.getSabadoDiaUtil();
        this.somaContadorCabecalho = layoutArquivo.getSomaContadorCabecalho();
        this.tipoLayout = layoutArquivo.getTipoLayout();
        this.porLinha = layoutArquivo.getPorLinha();
    }

    public LayoutArquivoTransfer(Integer id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCabecalho() {
        return cabecalho;
    }

    public void setCabecalho(String cabecalho) {
        this.cabecalho = cabecalho;
    }

    public Boolean getSomaContadorCabecalho() {
        return somaContadorCabecalho;
    }

    public void setSomaContadorCabecalho(Boolean somaContadorCabecalho) {
        this.somaContadorCabecalho = somaContadorCabecalho;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getFormatoDataPeriodo() {
        return formatoDataPeriodo;
    }

    public void setFormatoDataPeriodo(String formatoDataPeriodo) {
        this.formatoDataPeriodo = formatoDataPeriodo;
    }

    public String getFormatoDataExportacao() {
        return formatoDataExportacao;
    }

    public void setFormatoDataExportacao(String formatoDataExportacao) {
        this.formatoDataExportacao = formatoDataExportacao;
    }

    public Boolean getSabadoDiaUtil() {
        return sabadoDiaUtil;
    }

    public void setSabadoDiaUtil(Boolean sabadoDiaUtil) {
        this.sabadoDiaUtil = sabadoDiaUtil;
    }

    public Boolean getDomingoDiaUtil() {
        return domingoDiaUtil;
    }

    public void setDomingoDiaUtil(Boolean domingoDiaUtil) {
        this.domingoDiaUtil = domingoDiaUtil;
    }

    public String getDelimitador() {
        return delimitador;
    }

    public void setDelimitador(String delimitador) {
        this.delimitador = delimitador;
    }

    public Boolean getHoraSexagesimal() {
        return horaSexagesimal;
    }

    public void setHoraSexagesimal(Boolean horaSexagesimal) {
        this.horaSexagesimal = horaSexagesimal;
    }

    public TipoLayout getTipoLayout() {
        return tipoLayout;
    }

    public void setTipoLayout(TipoLayout tipoLayout) {
        this.tipoLayout = tipoLayout;
    }

    public void setPorLinha(boolean porLinha) {
        this.porLinha = porLinha;
    }

    public Boolean getPorLinha() {
        return porLinha;
    }

    public List<LayoutArquivoGrupoTransfer> getLayoutArquivoGrupoTransferList() {
        return layoutArquivoGrupoTransferList;
    }

    public void setLayoutArquivoGrupoTransferList(List<LayoutArquivoGrupoTransfer> layoutArquivoGrupoTransferList) {
        this.layoutArquivoGrupoTransferList = layoutArquivoGrupoTransferList;
    }

    @Override
    public LayoutArquivo toEntidade() {
        LayoutArquivo layoutArquivo = new LayoutArquivo();
        layoutArquivo.setIdLayoutArquivo(id);
        layoutArquivo.setCabecalho(cabecalho);
        layoutArquivo.setDelimitador(delimitador);
        layoutArquivo.setDescricao(descricao);
        layoutArquivo.setDomingoDiaUtil(domingoDiaUtil);
        layoutArquivo.setFormato(formato);
        layoutArquivo.setFormatoDataExportacao(formatoDataExportacao);
        layoutArquivo.setFormatoDataPeriodo(formatoDataPeriodo);
        layoutArquivo.setHoraSexagesimal(horaSexagesimal);
        layoutArquivo.setSabadoDiaUtil(sabadoDiaUtil);
        layoutArquivo.setSomaContadorCabecalho(somaContadorCabecalho);
        layoutArquivo.setTipoLayout(tipoLayout);
        layoutArquivo.setPorLinha(porLinha);
        return layoutArquivo;
    }

}
