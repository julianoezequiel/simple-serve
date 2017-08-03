package com.topdata.toppontoweb.services.relatorios.cadastros;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroAfastamentoTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioCadastroAfastamentoServices implements IGeraFrequencia {

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Autowired
    private AfastamentoService afastamentoService;
    

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer , CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);
        Operador operador = geraFrequenciaServices.getOperadorService().getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioAfastamento(this.afastamentoService, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;
    }

    protected class GerarRelatorioAfastamento extends GerarRelatorio {

        private final AfastamentoService afastamentoService;

        public GerarRelatorioAfastamento(AfastamentoService afastamentoService, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.afastamentoService = afastamentoService;
        }

        @Override
        public void run() {
            try {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                //Lista de objeto
                RelatorioCadastroAfastamentoTransfer cadastroTransfer = (RelatorioCadastroAfastamentoTransfer) this.relatorioTransfer;
                
                //Audita os dados
                this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_AFASTAMENTO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), cadastroTransfer);
                
                Double max = 60d;
                
                //Busca os funcionarios
                List<Departamento> departamentoList = geraFrequenciaServices.buscarDepartamentosPorRelatorioCadastro(cadastroTransfer, statusTransfer, max);
                
                Double percIndividual = 30d / (departamentoList.isEmpty() ? 1 : departamentoList.size());
                
                departamentoList.stream().forEach(dep->{
                    GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).addPercentual(percIndividual);
                    dep.getFuncionarioList().stream().forEach(func->{
                        try {
                            List<Afastamento> afastamentoList  = this.afastamentoService.buscarPorFuncionarioEPeriodo(func.getIdFuncionario(), 
                                    cadastroTransfer.getPeriodoInicio(),cadastroTransfer.getPeriodoFim());
                            func.setAfastamentoList(afastamentoList);
                        } catch (ServiceException ex) {
                            Logger.getLogger(RelatorioCadastroAfastamentoServices.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                });

                this.carregarDados(departamentoList);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);

            } catch (ServiceException ex) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
        }
    }

}
