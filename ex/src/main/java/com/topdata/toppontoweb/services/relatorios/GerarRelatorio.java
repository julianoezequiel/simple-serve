package com.topdata.toppontoweb.services.relatorios;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroTransfer;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.SaidaPadrao;
import com.topdata.toppontoweb.utils.Utils;

/**
 *
 * @author juliano.ezequiel
 */
public abstract class GerarRelatorio implements Runnable {

    public final IGeraFrequenciaTransfer relatorioTransfer;
    public final GeraFrequenciaServices geraFrequenciaServices;
    public final GeraFrequenciaStatusTransfer statusTransfer;

    public GerarRelatorio(IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices geraFrequenciaServices, GeraFrequenciaStatusTransfer statusTransfer) {
        this.relatorioTransfer = relatorioTransfer;
        this.geraFrequenciaServices = geraFrequenciaServices;
        this.statusTransfer = statusTransfer;
    }

    public void carregarDados(Collection<?> collection) {

        if (this.relatorioTransfer instanceof RelatorioCadastroTransfer) {
            carregaDadosTipoRelatorioCadastroTransfer(collection);
        } else if (this.relatorioTransfer instanceof RelatoriosGeraFrequenciaTransfer) {
            carregadadosTipoRelatoriosApiTransfer(collection);
        }

    }

    private void carregaDadosTipoRelatorioCadastroTransfer(Collection<?> collection) {

        RelatorioCadastroTransfer cadastroTransfer = (RelatorioCadastroTransfer) this.relatorioTransfer;

        this.statusTransfer.setTemplate(cadastroTransfer.tipo_relatorio.getTemplate(relatorioTransfer));
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_PERIODO_INICIO, cadastroTransfer.getPeriodoInicio());
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_PERIODO_TERMINO, cadastroTransfer.getPeriodoFim());

        this.statusTransfer.setDataSource(new JRBeanCollectionDataSource(collection));
    }

    private void carregadadosTipoRelatoriosApiTransfer(Collection<?> collection) {

        RelatoriosGeraFrequenciaTransfer apiTransfer = (RelatoriosGeraFrequenciaTransfer) this.relatorioTransfer;
        this.statusTransfer.setTemplate(apiTransfer.tipo_relatorio.getTemplate(relatorioTransfer));
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_PERIODO_INICIO, apiTransfer.getPeriodoInicio());
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_PERIODO_TERMINO, apiTransfer.getPeriodoFim());
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_IS_CAMPOS_ZERADO, apiTransfer.getExibirCamposZerados());
        this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_IS_TOTAL_HORAS_JORNADA, apiTransfer.getExibirTotalHorasJornada());
        String caminhoRelatorio = getClass().getClassLoader().getResource("relatorio").getPath();
        caminhoRelatorio = Utils.corrigirUrl(caminhoRelatorio);
        this.statusTransfer.getParametros().put(RelatorioHandler.REPORT_PATH, caminhoRelatorio);
        
        List<TipoDia> tipoDiaList = new ArrayList<>();
        try {
            tipoDiaList = this.geraFrequenciaServices.getTopPontoService().buscarTodos(TipoDia.class);
        } catch (ServiceException ex) {
            Logger.getLogger(GerarRelatorio.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        this.statusTransfer.getParametros().put(RelatorioHandler.TIPO_DIA_LIST, tipoDiaList);
        
        
        //Carrega os parametros especificos
        apiTransfer.getTipo_relatorio().configurarParametros(this.statusTransfer, this.relatorioTransfer );
        
        //carrega as collection de acordo com tipo de agrupamento
        collection = apiTransfer.getTipo_relatorio().getAgrupamento(this.relatorioTransfer, (Collection<SaidaPadrao>) collection);
        this.statusTransfer.setDataSource(new JRBeanCollectionDataSource(collection));

    }
    
}
