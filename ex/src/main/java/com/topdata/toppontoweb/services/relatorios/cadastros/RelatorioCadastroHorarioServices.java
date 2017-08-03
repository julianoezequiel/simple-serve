package com.topdata.toppontoweb.services.relatorios.cadastros;

import com.topdata.toppontoweb.services.gerafrequencia.integracao.GeraFrequenciaServices;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroHorarioTransfer;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.jornada.HorarioService;
import com.topdata.toppontoweb.services.relatorios.GerarRelatorio;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.IGeraFrequencia;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 *
 * @author tharle.camargo
 */
@Service
public class RelatorioCadastroHorarioServices implements IGeraFrequencia {

    @Autowired
    private GeraFrequenciaServices geraFrequenciaServices;

    @Autowired
    private HorarioService horarioService;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request, IGeraFrequenciaTransfer geraFrequenciaTransfer,  CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {

        GeraFrequenciaStatusTransfer statusTransfer = this.geraFrequenciaServices.iniciarProcesso(tIPO_PROCESSO);
        Operador operador = geraFrequenciaServices.getOperadorService().getOperadorDaSessao(request);
        statusTransfer.setOperador(operador);
        Thread thread = new Thread(new GerarRelatorioHorario(this.horarioService, geraFrequenciaTransfer, this.geraFrequenciaServices, statusTransfer));
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        thread.start();
        return statusTransfer;
    }

    protected class GerarRelatorioHorario extends GerarRelatorio {

        private final HorarioService horarioService;

        public GerarRelatorioHorario(HorarioService horarioService, IGeraFrequenciaTransfer relatorioTransfer, GeraFrequenciaServices relatorioServices, GeraFrequenciaStatusTransfer statusTransfer) {
            super(relatorioTransfer, relatorioServices, statusTransfer);
            this.horarioService = horarioService;
        }

        @Override
        public void run() {
            try {
                //Lista de objeto
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(5d);
                final List<Horario> lista = new ArrayList<>();
                RelatorioCadastroHorarioTransfer relatorioCadastroHorarioTransfer
                        = (RelatorioCadastroHorarioTransfer) relatorioTransfer;
                //Audita os dados
                this.geraFrequenciaServices.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.REL_CAD_HORARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, statusTransfer.getOperador(), relatorioCadastroHorarioTransfer);
                
                if (relatorioCadastroHorarioTransfer.getHorario() == null
                        || relatorioCadastroHorarioTransfer.getHorario().getIdHorario() == null) {//Todas as empresas;
                    this.horarioService.buscarTodos().stream().forEach((horario) -> {
                        horario = corrigeMarcacoes(horario);
                        lista.add(horario);
                    });
                } else {//Uma em específica
                    Horario horario = this.horarioService.buscar(Horario.class,
                            relatorioCadastroHorarioTransfer.getHorario().getIdHorario());
                    horario = corrigeMarcacoes(horario);
                    lista.add(horario);
                }
                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(90d);

                this.carregarDados(lista);
                this.geraFrequenciaServices.processarRelatorio(this.statusTransfer);

                GeraFrequenciaServices.STATUS_MAP.get(this.statusTransfer.getId()).setPercentual(100d);

            } catch (Exception e) {
                this.statusTransfer.setEstado_processo(RelatorioHandler.ESTADO_PROCESSO.ERRO);
            }

        }
        
        public Horario corrigeMarcacoes(Horario horario){
            horario.getHorarioMarcacaoList().stream().forEach(horarioMarcacao->{
                horarioMarcacao.setHorarioEntrada(Utils.getDataAjustaDiferenca3horas(horarioMarcacao.getHorarioEntrada()));
                horarioMarcacao.setHorarioSaida(Utils.getDataAjustaDiferenca3horas(horarioMarcacao.getHorarioSaida()));
            });
            
            return horario;
        }

    }
}
