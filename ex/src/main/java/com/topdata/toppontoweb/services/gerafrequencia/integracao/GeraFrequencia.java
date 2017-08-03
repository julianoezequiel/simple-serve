package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.dto.gerafrequencia.IGeraFrequenciaTransfer;
import com.topdata.toppontoweb.dto.relatorios.RelatoriosGeraFrequenciaTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.jornada.TipoJornada;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe de processo do Gera Frequancia
 *
 * @author juliano.ezequiel
 */
@Service
public class GeraFrequencia extends GeraFrequenciaServices implements IGeraFrequencia {

    private static final long DELAY = 5000;
    private static final long PERIOD = 5000;

    @Override
    public GeraFrequenciaStatusTransfer iniciar(HttpServletRequest request,
            IGeraFrequenciaTransfer geraFrequenciaTransfer, CONSTANTES.Enum_TIPO_PROCESSO tIPO_PROCESSO) throws ServiceException {
        LOGGER.debug("Início do processo gera frequência");
        carregarDadosBasico(geraFrequenciaTransfer);
        GeraFrequenciaStatusTransfer statusTransfer = this.iniciarProcesso(tIPO_PROCESSO);
        
        
        //Audita os dados
        CONSTANTES.Enum_FUNCIONALIDADE funcionalidadeAuditoria = ((RelatoriosGeraFrequenciaTransfer) this.geraFrequenciaTransfer).getTipo_relatorio().getFuncionalidadeAuditoria();
        if(funcionalidadeAuditoria != null){
            Operador operador = geraFrequenciaTransfer.getOperador();
            
            if(operador == null){
                operador = getOperadorService().getOperadorDaSessao(request);
            }
            
            getAuditoriaServices().auditar(funcionalidadeAuditoria, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.GERAR, operador, (RelatoriosGeraFrequenciaTransfer) this.geraFrequenciaTransfer);
        }
        
        //configura o período do gera frequencia
        statusTransfer.setInicio(((RelatoriosGeraFrequenciaTransfer) this.geraFrequenciaTransfer).getPeriodoInicio());
        statusTransfer.setTermino(((RelatoriosGeraFrequenciaTransfer) this.geraFrequenciaTransfer).getPeriodoFim());
        //monta a thread principal do gera frequencia
        criarThreadPrincipal(new ProcessarGeraFrequencia(listarFuncionarios(
                (RelatoriosGeraFrequenciaTransfer) this.geraFrequenciaTransfer), statusTransfer, this, geraFrequenciaTransfer), statusTransfer);
        //inicia o monitoramento da tread
        criarTimerMonitor(statusTransfer);
        //adiciona ao Hash de controle
        GeraFrequenciaServices.STATUS_MAP.put(statusTransfer.getId(), statusTransfer);
        return statusTransfer;
    }

    /**
     * Busca os funcionário conforme o filtro existente
     * RelatoriosGeraFrequenciaTransfer
     *
     * @param geraFrequenciaTransfer
     * @return
     * @throws ServiceException
     */
    private synchronized List<Funcionario> listarFuncionarios(RelatoriosGeraFrequenciaTransfer geraFrequenciaTransfer) throws ServiceException {
        List<Funcionario> funcionarioList = new ArrayList<>();
        try {
            if (geraFrequenciaTransfer.getFuncionario() == null) {
                funcionarioList = this.getRelatorioDao().buscarFuncinarioPorFiltroRelatorio(geraFrequenciaTransfer);
            } else {
                Funcionario funcionario = this.getFuncionarioService().buscar(
                        Funcionario.class, geraFrequenciaTransfer.getFuncionario().getIdFuncionario());
                funcionarioList.add(funcionario);
            }
            
            if(geraFrequenciaTransfer.isRetroativo()){
                //Buscar cargo retroativo
                funcionarioList.stream().forEach(f->{
                    try {
                        this.getFuncionarioService().alterarParaDadosRetroativos(f, geraFrequenciaTransfer.getPeriodoFim());
                    } catch (ServiceException ex) {
                        LOGGER.debug("Erro ao calcular dados retroativos.");
                        LOGGER.debug("Mensagem: "+ex.getMessage());
                    }
                });
            }
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
        return funcionarioList;
    }

    /**
     * Cria e inicia a thread principal de cálculo do gera frequancia. Esta
     * Thread determina quando o processo principal de calculo inicia e termina
     *
     * @param geraFrequencia
     * @param statusTransfer
     */
    private void criarThreadPrincipal(ProcessarGeraFrequencia geraFrequencia, GeraFrequenciaStatusTransfer statusTransfer) {
        TCalculoPrincipal tCalculoPrincipal = new TCalculoPrincipal(geraFrequencia, statusTransfer.getId());
        statusTransfer.setCalculoPrincipal(tCalculoPrincipal);
        tCalculoPrincipal.setPriority(Thread.MIN_PRIORITY);
        tCalculoPrincipal.start();
    }

    /**
     * Cria o Timer que servirá como monitor da Thread principal. Este timer tem
     * como objetivo finalizar a Thread principal caso ele não seja resetado. O
     * tempo de Reset é determinado pelas contantes DELAY e PERIOD
     *
     * @param statusTransfer
     */
    private void criarTimerMonitor(GeraFrequenciaStatusTransfer statusTransfer) {
        Timer timer = new Timer("monitor processo - " + statusTransfer.getCalculoPrincipal().getName());
        TimerTask task = new TimerMonitor(this, statusTransfer);
        timer.schedule(task, DELAY, PERIOD);
        statusTransfer.setTimerMonitor(timer);
    }

    /**
     * Carrega os dados essenciais que são em comuns para todos os calculos
     * executados
     *
     * @param geraFrequenciaTransfer
     * @throws ServiceException
     */
    private void carregarDadosBasico(IGeraFrequenciaTransfer geraFrequenciaTransfer) throws ServiceException {
        this.dsrList = this.getTopPontoService().buscarTodos(Dsr.class);
        this.tipoJornadaList = this.getTopPontoService().buscarTodos(TipoJornada.class);
        this.tipoDiaList = this.getTopPontoService().buscarTodos(TipoDia.class);
        this.geraFrequenciaTransfer = geraFrequenciaTransfer;
    }

}
