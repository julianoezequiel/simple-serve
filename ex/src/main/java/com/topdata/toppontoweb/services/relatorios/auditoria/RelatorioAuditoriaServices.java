package com.topdata.toppontoweb.services.relatorios.auditoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.relatorios.RelatorioAuditoriaTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.Auditoria_;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioAuditoriaServices implements IGeraFrequencia {

    public final static Logger LOGGER = LoggerFactory.getLogger(RelatorioAuditoriaServices.class.getName());

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Autowired
    private AuditoriaServices auditoriaServices;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer,  CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {
        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);
        Thread thread = new Thread(new GerarRelatorioAuditoria(this.auditoriaServices, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;
    }

    protected class GerarRelatorioAuditoria extends GerarRelatorio {

        private final AuditoriaServices auditoriaServices;
        private final RelatorioAuditoriaTransfer auditoriaTransfer;
        private List<Auditoria> auditoriaList;

        private final Modulos moduloOutros;

        public GerarRelatorioAuditoria(AuditoriaServices auditoriaServices, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.moduloOutros = new Modulos("Outros");
            this.auditoriaServices = auditoriaServices;
            this.auditoriaTransfer = (RelatorioAuditoriaTransfer) relatorioTransfer;
        }

        @Override
        public void run() {
            try {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(30d);
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ACESSANDO_BASE);

                if (this.auditoriaTransfer.getModulo() == null || this.auditoriaTransfer.getTipoAuditoria() != null || this.auditoriaTransfer.getFuncionalidade() != null) {
                    this.relatorioFuncionalidades();
                } else if (this.auditoriaTransfer.getModulo() != null && this.auditoriaTransfer.getModulo().getText().equalsIgnoreCase("Outros")) {
                    this.relatorioModuloTipoAuditoria();
                } else {
                    this.relatorioModulo();
                }

                //filtra somente os funcinario ativos caso necessario
                if (this.auditoriaTransfer.getOperador() != null && Objects.equals(this.auditoriaTransfer.getOperadorAtivo(), Boolean.FALSE)) {
                    this.auditoriaList = this.auditoriaList.stream().filter(a -> Objects.equals(a.getOperador().getAtivo(), Boolean.TRUE)).collect(Collectors.toList());
                }

                List<DadosAuditados> dadosAutitadoseList = new ArrayList<>();
                //cria o objeto para o relatorio
                this.auditoriaList
                        .stream()
                        .collect(Collectors
                                .groupingBy(a -> a.getFuncionalidade() != null ? a.getFuncionalidade().getIdModulo() : this.moduloOutros))
                        .forEach((Modulos modulo, List<Auditoria> listaAuditoria) -> {
                            dadosAutitadoseList.add(new DadosAuditados(modulo, listaAuditoria));
                        });

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(70d);

                this.carregarDados(dadosAutitadoseList);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);

            } catch (Exception e) {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }

        }

        private void relatorioModuloTipoAuditoria() throws ServiceException {

            if (this.auditoriaTransfer.getOperador() != null) {

                this.auditoriaList = this.auditoriaServices.buscarTiposAuditoriaPeriodoOperador(
                        this.auditoriaTransfer.getOperador(),
                        this.auditoriaTransfer.getPeriodoInicio(),
                        this.auditoriaTransfer.getPeriodoFim());

            } else {

                this.auditoriaList = this.auditoriaServices.buscarTiposAuditoriaPeriodo(
                        this.auditoriaTransfer.getPeriodoInicio(),
                        this.auditoriaTransfer.getPeriodoFim());

            }
        }

        private void relatorioModulo() throws ServiceException {

            if (this.auditoriaTransfer.getOperador() != null) {

                this.auditoriaList = this.auditoriaServices.buscarAuditoriaPorPeriodoIdModuloOperador(
                        this.auditoriaTransfer.getModulo(),
                        this.auditoriaTransfer.getOperador(),
                        this.auditoriaTransfer.getPeriodoInicio(),
                        this.auditoriaTransfer.getPeriodoFim());
            } else {

                this.auditoriaList = this.auditoriaServices.buscarAuditoriaPorPeriodoIdModulo(
                        this.auditoriaTransfer.getModulo(),
                        this.auditoriaTransfer.getPeriodoInicio(),
                        this.auditoriaTransfer.getPeriodoFim());
            }
        }

        private void relatorioFuncionalidades() throws ServiceException {
            HashMap<String, Object> mapCriterios = new HashMap<>();
            mapCriterios.put(Auditoria_.tipoAuditoria.getName(), this.auditoriaTransfer.getTipoAuditoria());
            mapCriterios.put(Auditoria_.funcionalidade.getName(), this.auditoriaTransfer.getFuncionalidade());
            mapCriterios.put(Auditoria_.operacao.getName(), this.auditoriaTransfer.getOperacao());
            mapCriterios.put(Auditoria_.operador.getName(), this.auditoriaTransfer.getOperador());

            //busca por Tipo auditoria, por auditoria ou os dois tipos
            this.auditoriaList = this.auditoriaServices.buscarPorCriterioPeriodo(
                    mapCriterios,
                    this.auditoriaTransfer.getPeriodoInicio(),
                    this.auditoriaTransfer.getPeriodoFim());
        }

        public class DadosAuditados {

            private final List<Auditoria> auditoriaList;
            private final Modulos modulo;

            private DadosAuditados(Modulos t, List<Auditoria> u) {
                this.auditoriaList = u;
                this.modulo = t;
            }

            public List<Auditoria> getAuditoriaList() {
                return auditoriaList;
            }

            public Modulos getModulo() {
                return modulo;
            }

        }

    }

}
