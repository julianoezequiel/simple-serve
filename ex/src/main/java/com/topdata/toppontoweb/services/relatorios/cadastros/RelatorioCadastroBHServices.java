package com.topdata.toppontoweb.services.relatorios.cadastros;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroBHTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class RelatorioCadastroBHServices implements IGeraFrequencia {

    @Autowired
    FuncionarioBancoHorasService funcBancoHorasService;

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer, CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);
        Operador operador = geraFrequenciaServices.getOperadorService().getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioBancoHoras(this.funcBancoHorasService, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;
    }

    protected class GerarRelatorioBancoHoras extends GerarRelatorio {

        private final FuncionarioBancoHorasService funcBancoHorasService;

        public GerarRelatorioBancoHoras(FuncionarioBancoHorasService funcBancoHorasService, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.funcBancoHorasService = funcBancoHorasService;
        }

        @Override
        public void run() {
            try {
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(0d);
                RelatorioCadastroBHTransfer relatorioCadastroBHTransfer = (RelatorioCadastroBHTransfer) relatorioTransfer;

                //Audita os dados
                this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_BANCO_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), relatorioCadastroBHTransfer);

                List<Funcionario> funcionarioList = geraFrequenciaServices.buscarFuncionariosPorRelatorioCadastro(relatorioCadastroBHTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(60d);

                Double percIndividual = 30d / (funcionarioList.isEmpty() ? 1 : funcionarioList.size());
                //Filtra pelas datas
                funcionarioList.stream().forEach((funcionario) -> {
                    try {
                        GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).addPercentual(percIndividual);
                        if (relatorioCadastroBHTransfer.isBancosSemDataFim() == true) {
                            funcionario.setFuncionarioBancoHorasList(this.funcBancoHorasService.buscarBancoHorasAbertoPeriodo(funcionario, relatorioCadastroBHTransfer.getPeriodoInicio(), relatorioCadastroBHTransfer.getPeriodoFim()));
                        } else {
                            funcionario.setFuncionarioBancoHorasList(this.funcBancoHorasService.buscarPorFuncionarioEPeriodo(funcionario, relatorioCadastroBHTransfer.getPeriodoInicio(), relatorioCadastroBHTransfer.getPeriodoFim()));
                        }
                    } catch (ServiceException ex) {
                        Logger.getLogger(RelatorioCadastroBHServices.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                funcionarioList.stream().forEach(func -> {
                    func.getFuncionarioBancoHorasList().stream().forEach(fb -> {
                        fb.getBancoHoras().setGatilhoNegativo(Utils.getDataAjustaDiferenca3horas(fb.getBancoHoras().getGatilhoNegativo()));
                        fb.getBancoHoras().setGatilhoPositivo(Utils.getDataAjustaDiferenca3horas(fb.getBancoHoras().getGatilhoPositivo()));
                        fb.getBancoHoras().getBancoHorasLimiteList().stream().forEach(l -> {
                            l.setLimite(Utils.getDataAjustaDiferenca3horas(l.getLimite()));
                        });
                    });
                });

                this.carregarDados(funcionarioList);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);

            } catch (ServiceException ex) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }
        }

    }

}
