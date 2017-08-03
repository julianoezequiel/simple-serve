package com.topdata.toppontoweb.services.fechamento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.gerafrequencia.GeraFrequenciaStatusTransfer;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoPeriodo;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Classe de controle e serviço do Fechamento da empresa.
 *
 * @since 1.0.0 data 20/05/2017
 * @version 1.0.0 data 20/05/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoServices {

    public final static Logger LOGGER = LoggerFactory.getLogger(FechamentoServices.class.getName());

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    protected EmpresaFechamentoDataServices empresaFechamentoDataServices;
    @Autowired
    protected EmpresaFechamentoPeriodoService empresaFechamentoPeriodoService;
    @Autowired
    protected FechamentoBHTabelaAcrescimosServices fechamentoBHTabelaAcrescimosServices;
    @Autowired
    protected FechamentoBHTabelaSomenteNoturnasServices fechamentoBHTabelaSomenteNoturnasServices;
    @Autowired
    protected FechamentoTabelaExtrasDSRServices fechamentoTabelaExtrasDSRServices;
    @Autowired
    protected FechamentoTabelaExtrasServices fechamentoTabelaExtrasServices;
    @Autowired
    private TopPontoResponse topPontoResponse;
    @Autowired
    protected FuncionarioBancoHorasService funcionarioBancoHorasService;
    //</editor-fold>

    /**
     * Busca os registro e carrega as tabelas que serão utilizados pelo Gera
     * Frequencia. É utilizada para realizar o carregamento dos registros para a
     * EntradaPadrao. Os dados são carregados individualmente para não ocorrer
     * problemas de Lazy-Load no Hibernate
     *
     * @param funcionario Funcionario
     * @param dataInicio Date
     * @param dataFim Date
     * @return Lista de EmpresaFechamentoData
     */
    public synchronized List<EmpresaFechamentoData> buscarPorPeriodoFuncionarioEmpresa(Funcionario funcionario, Date dataInicio, Date dataFim) {
        List<EmpresaFechamentoData> empresaFechamentoDataList = new ArrayList<>();
        try {
            empresaFechamentoDataList = this.empresaFechamentoDataServices.buscarPorPeriodoFuncionario(funcionario, dataInicio, dataFim);
            LOGGER.debug("Consulta buscarPorPeriodoFuncionarioEmpresa qtd itens para consultar : {} - funcionário : {}", empresaFechamentoDataList.size(), funcionario.getNome());
            empresaFechamentoDataList
                    .parallelStream()
                    .forEach(new CarregarSubListas(this));
        } catch (ServiceException ex) {
            LOGGER.debug(this.getClass().getSimpleName(), ex);
        }
        return empresaFechamentoDataList;
    }

    /**
     * Após os cálculos do gera frequencia, os dados serão persistidos. Como são
     * várias lista, o processo inicia-se neste método
     *
     * @param saidaDiaList List<SaidaDia>
     * @param empresaFechamentoPeriodo EmpresaFechamentoPeriodo
     * @param geraFrequenciaStatusTransfer GeraFrequenciaStatusTransfer
     */
    public synchronized void salvarResultadoCalculo(List<SaidaDia> saidaDiaList,
            EmpresaFechamentoPeriodo empresaFechamentoPeriodo, GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer) {
        saidaDiaList
                .parallelStream()
                .forEach(
                        new salvarEmpresaFechamentoData(
                                this, empresaFechamentoPeriodo, geraFrequenciaStatusTransfer));
    }

    /**
     * Verifica se existe fechamento para a empresa no periodo informado
     *
     * @param empresa Empresa
     * @param data Date
     * @return verdadeiro se já existe fechamento
     * @throws ServiceException
     */
    public Boolean isExisteFechamento(Empresa empresa, Date data) throws ServiceException {
        return isExisteFechamento(empresa, data, null);
    }

    /**
     * Verifica se existe fechamento para a empresa no periodo informado
     *
     * @param empresa Empresa
     * @param dataInicio Date
     * @param dataFim Date
     * @return verdadeiro se já existe fechamento
     * @throws ServiceException
     */
    public Boolean isExisteFechamento(Empresa empresa, Date dataInicio, Date dataFim) throws ServiceException {
        //se não possui data fim , assume a data de inicio mais 1 dia como fim
        if (dataFim == null) {
            dataFim = dataInicio;
        }

        //Zera a data inicio
        dataInicio = Utils.configuraHorarioData(dataInicio, 0, 0, 0);

        //23:59 para a data fim
        dataFim = Utils.configuraHorarioData(dataFim, 23, 59, 59);

        //valida na base de dados se existe fechamento para o periodo
        List<EmpresaFechamentoPeriodo> empresaFechamentoPeriodosList
                = this.empresaFechamentoPeriodoService.buscarPorPeriodoEmpresa(empresa, dataInicio, dataFim);

        //verifica se existe algum fechamento em andamento
        boolean isProcessando = EmpresaFechamentoPeriodoService.CONTROLE_FECHAMENTOS.values()
                .stream()
                .anyMatch(new PredicateImpl(dataInicio, dataFim));

        return empresaFechamentoPeriodosList != null && !empresaFechamentoPeriodosList.isEmpty() || isProcessando;
    }

    /**
     * Valida se existe um fechamento no periodo informado, lança uma exeção
     * caso possui o fechamento
     *
     * @param empresa
     * @param dataInicio
     * @param dataFim
     * @throws com.topdata.toppontoweb.services.fechamento.FechamentoException
     */
    public void isPossuiFechamentoPeriodo(Empresa empresa, Date dataInicio, Date dataFim) throws FechamentoException {
        try {
            if (isExisteFechamento(empresa, dataInicio, dataFim)) {
                throw new FechamentoException(this.topPontoResponse.alertaValidacao(MSG.FECHAMENTO.ALERTA_EXISTE_FECHAMENTO_PERIODO.getResource()));
            }
        } catch (ServiceException ex) {
            throw (FechamentoException) ex;
        }
    }

    //<editor-fold defaultstate="" desc="CLASSES INTERNA DE PERSISTENCIA">
    /**
     * Classe interna para persistencia dos dados do fechamento. O processo de
     * persistencia inicia-se nesta classe
     */
    private class salvarEmpresaFechamentoData implements Consumer<SaidaDia> {

        private final FechamentoServices fechamentoServices;
        private EmpresaFechamentoPeriodo empresaFechamentoPeriodo;
        private final GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer;
        private EmpresaFechamentoData empresaFechamentoData;

        public salvarEmpresaFechamentoData(FechamentoServices fechamentoServices,
                EmpresaFechamentoPeriodo empresaFechamentoPeriodo, GeraFrequenciaStatusTransfer geraFrequenciaStatusTransfer) {
            this.fechamentoServices = fechamentoServices;
            this.empresaFechamentoPeriodo = empresaFechamentoPeriodo;
            this.geraFrequenciaStatusTransfer = geraFrequenciaStatusTransfer;
        }

        @Override
        public synchronized void accept(SaidaDia saidaDia) {
            try {

                //verifica se existe inconsistencia e atualiza a Tabela EmpresaFechamentoPeriodo
//                verificaInconsistencia(saidaDia);
                //salva dados da Tabela EmpresaFechamentoData
                this.empresaFechamentoData = saidaDia.toEmpresaFechamentoData(this.empresaFechamentoPeriodo);

                if (this.empresaFechamentoData.getIdFuncionarioBancoHoras() != null && this.empresaFechamentoData.getIdFuncionarioBancoHoras().getIdFuncionarioBancoHoras() != null) {
                    empresaFechamentoData.setIdFuncionarioBancoHoras((FuncionarioBancoHoras) this.fechamentoServices.funcionarioBancoHorasService.
                            getDao().find(FuncionarioBancoHoras.class, this.empresaFechamentoData.getIdFuncionarioBancoHoras().getIdFuncionarioBancoHoras()));
                }

                this.empresaFechamentoData
                        = this.fechamentoServices.empresaFechamentoDataServices
                        .getEmpresaFechamentoDataDao()
                        .save(saidaDia.toEmpresaFechamentoData(this.empresaFechamentoPeriodo));

                //salva dados da Tabela FechamentoBHTabelaAcrescimos
                saidaDia.toFechamentoBHTabelaAcrescimos(this.empresaFechamentoData)
                        .parallelStream()
                        .forEach(
                                new salvarFechamentoBHTabelaAcrescimos(this.fechamentoServices));

                //salva dados da Tabela FechamentoBHTabelaSomenteNoturnas
                saidaDia.toFechamentoBHTabelaSomenteNoturnas(this.empresaFechamentoData)
                        .parallelStream()
                        .forEach(
                                new salvarFechamentoBHTabelaSomenteNoturnas(this.fechamentoServices));

                //salva dados da Tabela FechamentoTabelaExtras
                saidaDia.toFechamentoTabelaExtras(this.empresaFechamentoData)
                        .parallelStream()
                        .forEach(
                                new salvarFechamentoTabelaExtras(this.fechamentoServices));

                //salva dados da Tabela FechamentoTabelaExtrasDSR
                saidaDia.toFechamentoTabelaExtrasDSR(this.empresaFechamentoData)
                        .parallelStream()
                        .forEach(
                                new salvarFechamentoTabelaExtrasDSR(this.fechamentoServices));
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }

    }

    /**
     * Salva na base de dados o FechamentoBHTabelaSomenteNoturnas
     */
    private class salvarFechamentoBHTabelaSomenteNoturnas implements Consumer<FechamentoBHTabelaSomenteNoturnas> {

        private final FechamentoServices fechamentoServices;

        public salvarFechamentoBHTabelaSomenteNoturnas(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(FechamentoBHTabelaSomenteNoturnas t) {
            LOGGER.debug("Salvando : " + t.getClass().getSimpleName());
            try {
                this.fechamentoServices.fechamentoBHTabelaSomenteNoturnasServices.getFechamentoBHTabelaSomenteNoturnasDao().save(t);
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }

    }

    /**
     * Salva na base de dados o FechamentoBHTabelaAcrescimos
     */
    private class salvarFechamentoBHTabelaAcrescimos implements Consumer<FechamentoBHTabelaAcrescimos> {

        private final FechamentoServices fechamentoServices;

        public salvarFechamentoBHTabelaAcrescimos(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(FechamentoBHTabelaAcrescimos t) {
            LOGGER.debug("Salvando : " + t.getClass().getSimpleName());
            try {
                this.fechamentoServices.fechamentoBHTabelaAcrescimosServices.getFechamentoBHTabelaAcrescimosDao().save(t);
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }

    }

    /**
     * Salva na base de dados o FechamentoTabelaExtras
     */
    private class salvarFechamentoTabelaExtras implements Consumer<FechamentoTabelaExtras> {

        private final FechamentoServices fechamentoServices;

        public salvarFechamentoTabelaExtras(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(FechamentoTabelaExtras t) {
            try {
                LOGGER.debug("Salvando : " + t.getClass().getSimpleName());
                this.fechamentoServices.fechamentoTabelaExtrasServices.getFechamentoTabelaExtrasDao().save(t);
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }

    }

    /**
     * Salva na base de dados o FechamentoTabelaExtrasDSR
     */
    private class salvarFechamentoTabelaExtrasDSR implements Consumer<FechamentoTabelaExtrasDSR> {

        private final FechamentoServices fechamentoServices;

        public salvarFechamentoTabelaExtrasDSR(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(FechamentoTabelaExtrasDSR t) {
            LOGGER.debug("Salvando : " + t.getClass().getSimpleName());
            try {
                this.fechamentoServices.fechamentoTabelaExtrasDSRServices.getFechamentoTabelaExtrasDSRDao().save(t);
            } catch (DaoException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }

    }

    /**
     * Exclui os dados de fechamento
     *
     * @param efp
     * @throws ServiceException
     */
    public void excluirEmpresaFechamentoDataPorEmpresaFechamentoPeriodo(EmpresaFechamentoPeriodo efp) throws ServiceException {
        LOGGER.debug("Excluindo registros existentes de EmpresaFechamentoData id : {}", efp.getIdEmpresaFechamentoPeriodo());
        efp = carregarSubListas(efp);
        efp.getEmpresaFechamentoDataList()
                .stream()
                .forEach(new ExcluirPorEmpresaFechamentoData(this));
        //depois das sub-listas excluidas exclui o registro
        this.empresaFechamentoDataServices.excluirPorEmpresaFechamentoPeriodo(efp);
    }

    /**
     * Exclui os dados de fechamento
     */
    private class ExcluirPorEmpresaFechamentoData implements Consumer<EmpresaFechamentoData> {

        private FechamentoServices fechamentoServices;

        public ExcluirPorEmpresaFechamentoData(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(EmpresaFechamentoData f) {
            try {
                this.fechamentoServices.fechamentoBHTabelaAcrescimosServices.excluirPorEmpresaFechamentoData(f);
                this.fechamentoServices.fechamentoBHTabelaSomenteNoturnasServices.excluirPorEmpresaFechamentoData(f);
                this.fechamentoServices.fechamentoTabelaExtrasDSRServices.excluirPorEmpresaFechamentoData(f);
                this.fechamentoServices.fechamentoTabelaExtrasServices.excluirPorEmpresaFechamentoData(f);
            } catch (ServiceException ex) {
                LOGGER.error(this.getClass().getSimpleName(), ex);
            }
        }
    }
//</editor-fold>

    /**
     *
     * @param efp
     * @return dados carregados
     * @throws ServiceException
     */
    public EmpresaFechamentoPeriodo carregarSubListas(EmpresaFechamentoPeriodo efp) throws ServiceException {
        if (efp != null) {
            efp.setEmpresaFechamentoDataList(new ArrayList<>(this.empresaFechamentoDataServices.buscarPorEmpresaFechamentoPeriodo(efp)));
            efp.getEmpresaFechamentoDataList().stream().forEach(new CarregarSubListas(this));
        }
        return efp;
    }

    /**
     * Carrega as listas que a EmpresaFechamentoData possui. Esta classe é
     * necessária para o funcionámento do Lazy-Load em Thread
     */
    private class CarregarSubListas implements Consumer<EmpresaFechamentoData> {

        private final FechamentoServices fechamentoServices;

        public CarregarSubListas(FechamentoServices fechamentoServices) {
            this.fechamentoServices = fechamentoServices;
        }

        @Override
        public void accept(EmpresaFechamentoData epd) {
            try {
                epd.setFechamentoBHTabelaAcrescimosList(this.fechamentoServices.fechamentoBHTabelaAcrescimosServices.buscarPorEmpresaFechamentodata(epd));
                epd.setFechamentoBHTabelaSomenteNoturnasList(this.fechamentoServices.fechamentoBHTabelaSomenteNoturnasServices.buscarPorEmpresaFechamentodata(epd));
                epd.setFechamentoTabelaExtrasDSRList(this.fechamentoServices.fechamentoTabelaExtrasDSRServices.buscarPorEmpresaFechamentodata(epd));
                epd.setFechamentoTabelaExtrasList(this.fechamentoServices.fechamentoTabelaExtrasServices.buscarPorEmpresaFechamentodata(epd));
            } catch (ServiceException ex) {
                LOGGER.error(FechamentoServices.this.getClass().getSimpleName(), ex);
            }
        }
    }

    private static class PredicateImpl implements Predicate<EmpresaFechamentoPeriodo> {

        private final Date dataInicio;
        private final Date dataFim;

        public PredicateImpl(Date dataInicio, Date dataFim) {
            this.dataInicio = dataInicio;
            this.dataFim = dataFim;
        }

        @Override
        public boolean test(EmpresaFechamentoPeriodo e) {
            return DateHelper.ValidarDatasPeriodo(dataInicio, dataFim, e.getInicio(), e.getTermino());
        }
    }

}
