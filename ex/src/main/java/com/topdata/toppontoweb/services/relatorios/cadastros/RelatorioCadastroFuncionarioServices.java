package com.topdata.toppontoweb.services.relatorios.cadastros;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroFuncionarioTransfer;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.empresa.DepartamentoService;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.services.jornada.JornadaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioCadastroFuncionarioServices implements IGeraFrequencia {

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;
    
    @Autowired
    private JornadaService jornadaService;
   
    @Autowired
    FuncionarioJornadaService funcionarioJornadaService;
    
    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer,  CONSTANTES.Enum_TIPO_PROCESSO TIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(TIPO_PROCESSO);
        Operador operador = geraFrequenciaServices.getOperadorService().getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioFuncionario(this.geraFrequenciaServices.getDepartamentoService(), geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;

    }
    //</editor-fold>

    protected class GerarRelatorioFuncionario extends GerarRelatorio {

        private final DepartamentoService departamentoService;

        public GerarRelatorioFuncionario(DepartamentoService departamentoService, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.departamentoService = departamentoService;
        }

        @Override
        public void run() {
            try {

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                //Lista de objeto
                RelatorioCadastroFuncionarioTransfer cadastroFuncionarioTransfer = (RelatorioCadastroFuncionarioTransfer) this.relatorioTransfer;
                
                //Audita os dados
                this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_FUNCIONARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), cadastroFuncionarioTransfer);
                
                Double max = 60d;
                
                //Busca os funcionarios
                List<Departamento> departamentoList = geraFrequenciaServices.buscarDepartamentosPorRelatorioCadastro(cadastroFuncionarioTransfer, statusTransfer, max);
                
                Double percDepartamento = 30d / (departamentoList.isEmpty() ? 1 : departamentoList.size());
                
                departamentoList.stream().forEach(dep->{
                    Double percFuncionario = percDepartamento / (dep.getFuncionarioList().isEmpty() ? 1 : dep.getFuncionarioList().size());
                    dep.getFuncionarioList().stream().forEach(func->{
                        GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).addPercentual(percFuncionario);
                        try {
                            List<FuncionarioJornada> funcJornadaList = funcionarioJornadaService.buscarPorFuncionario(func.getIdFuncionario(), false);
                            
                            func.setFuncionarioJornadaList(funcJornadaList);
                            
                        } catch (ServiceException ex) {
                            Logger.getLogger(RelatorioCadastroAfastamentoServices.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    });
                });
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(90d);
                
                this.carregarDados(departamentoList);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);
            } catch (ServiceException ex) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
        }
    }

}
