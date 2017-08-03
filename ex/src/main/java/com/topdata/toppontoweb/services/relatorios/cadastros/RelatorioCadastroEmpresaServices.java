package com.topdata.toppontoweb.services.relatorios.cadastros;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroEmpresaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.permissoes.OperadorService;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioCadastroEmpresaServices implements IGeraFrequencia {

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer,  CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);

        Operador operador = operadorService.getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioEmpresa(this.empresaService, operador, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        thread.start();

        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        return statusTransfer;
    }

    protected class GerarRelatorioEmpresa extends GerarRelatorio {

        private final EmpresaService empresaService;
        private final Operador operador;

        public GerarRelatorioEmpresa(EmpresaService empresaService, Operador operador, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.empresaService = empresaService;
            this.operador = operador;
        }

        @Override
        public void run() {
            try {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                List<Empresa> lista = new ArrayList<>();
                RelatorioCadastroEmpresaTransfer relatorioCadastroEmpresaTransfer = (RelatorioCadastroEmpresaTransfer) relatorioTransfer;
                
                //Audita os dados
                 this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_EMPRESA, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), relatorioCadastroEmpresaTransfer);
                
                if (relatorioCadastroEmpresaTransfer.getEmpresa() == null || relatorioCadastroEmpresaTransfer.getEmpresa().getIdEmpresa() == null) {//Todas as empresas
                    if (relatorioCadastroEmpresaTransfer.getIsEmpresasAtivas()) {
                        lista = this.empresaService.buscaPorGrupoPermissaoAtivas(this.operador.getGrupo().getIdGrupo());
                    } else {
                        lista = this.empresaService.buscaPorGrupoPermissao(this.operador.getGrupo().getIdGrupo());
                    }
                } else {//Uma em específica
                    Empresa empresa = this.empresaService.buscar(relatorioCadastroEmpresaTransfer.getEmpresa().getIdEmpresa());
                    lista.add(empresa);
                }

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(50d);

                this.carregarDados(lista);
                this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_IS_CEI, ((RelatorioCadastroEmpresaTransfer) this.relatorioTransfer).getIsCEI());
                this.statusTransfer.getParametros().put(RelatorioHandler.FILTRO_IS_DEPARTAMENTO, ((RelatorioCadastroEmpresaTransfer) this.relatorioTransfer).getIsDepartamento());

                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);

            } catch (Exception e) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
        }

    }

}
