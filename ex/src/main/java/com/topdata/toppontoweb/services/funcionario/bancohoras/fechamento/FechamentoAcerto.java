package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioBancoDeHorasFechamentoDao;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.tipo.TipoAcerto;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.VerificarSaldoBH;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.stream.Collectors;

/**
 * Classe de regras para o fechamento de banco de horas do funcionário do tipo
 * acerto
 *
 * @version 1.0.0 data 28/06/2017
 * @since 1.0.0 data 28/06/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoAcerto {

    public final static Logger LOGGER = LoggerFactory.getLogger(FechamentoAcerto.class.getName());

    //<editor-fold defaultstate="" desc="CDI">
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private TopPontoResponse topPontoResponse;
    @Autowired
    private AuditoriaServices auditoriaServices;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoService funcionarioBancoDeHorasFechamentoService;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoDao funcionarioBancoDeHorasFechamentoDao;
    @Autowired
    private VerificarSaldoBH verificarSaldoBH;
    @Autowired
    private TipoFechamentoService tipoFechamentoService;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private FuncionarioService funcionarioService;

    //</editor-fold>
    private IOpcoesAcerto opcoesAcerto;

    /**
     * Inicio do processo de persistencia de um fechamento do tipo acerto
     *
     * @param fbdhf
     * @throws ServiceException
     */
    public void salvar(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {
        LOGGER.debug("Inicio processo salvar fechamento tipo acerto");
        validarCamposObrigatorio(fbdhf);
        validarJaPossuiAcertoData(fbdhf);
        realizarFechamento(fbdhf);
    }

    /**
     * Inicia o processo de fechamento
     *
     * @param fbdhf
     * @return
     * @throws ServiceException
     */
    private FuncionarioBancoHorasFechamento realizarFechamento(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {

        if (fbdhf.getTipoAcerto() == null || fbdhf.getTipoAcerto().getIdTipoAcerto().equals(CONSTANTES.Enum_TIPOS_ACERTO.MANUAL.ordinal())) {
            this.opcoesAcerto = new Manual(this);
        } else if (fbdhf.getTipoAcerto().getIdTipoAcerto().equals(CONSTANTES.Enum_TIPOS_ACERTO.ZERAR.ordinal())) {
            this.opcoesAcerto = new Zerar(this);
        } else if (fbdhf.getTipoAcerto().getIdTipoAcerto().equals(CONSTANTES.Enum_TIPOS_ACERTO.USAR_GATILHO.ordinal())) {
            this.opcoesAcerto = new Gatilho(this);
        } else {
            throw new ServiceException(this.topPontoResponse.campoObrigatorio("Opção de inválido"));
        }

        Funcionario funcionario;
        if (fbdhf.getFuncionario() != null) {
            funcionario = fbdhf.getFuncionario();
        } else if (fbdhf.getFuncionarioBancoHoras() != null && fbdhf.getFuncionarioBancoHoras().getFuncionario() != null) {
            funcionario = fbdhf.getFuncionarioBancoHoras().getFuncionario();
        } else {
            throw new ServiceException(this.topPontoResponse.alertaNaoCad());
        }

        return this.opcoesAcerto.IniciarProcesso(fbdhf, this.funcionarioBancoDeHorasFechamentoService.getBancoValido(funcionario, fbdhf.getDataFechamento()));
    }

    /**
     * Inicia o processo de exclusão de um fechamento do tipo Acerto
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento excluir(Object id) throws ServiceException {
        try {
            FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoService.buscar(FuncionarioBancoHorasFechamento.class, (Integer) id);
            validarIdentificador(funcionarioBancoHorasFechamento);
            Coletivo coletivo = funcionarioBancoHorasFechamento.getColetivo();
            if (coletivo != null) {
                //remove vinculo entre coletivo e o fechamento
                funcionarioBancoHorasFechamento.setColetivo(null);
                this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
                this.funcionarioBancoDeHorasFechamentoDao.refresh(funcionarioBancoHorasFechamento);
                //chama a função para verificar não exsite mais vinculos entre o coletivo e outros fechamentos
                this.coletivoService.excluirColetivoSemVinculo(coletivo);
            }
            this.funcionarioBancoDeHorasFechamentoDao.delete(funcionarioBancoHorasFechamento);
            this.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionarioBancoHorasFechamento.getFuncionarioBancoHoras().getFuncionario(), funcionarioBancoHorasFechamento.getDataFechamento());
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHorasFechamento);
            return funcionarioBancoHorasFechamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroExcluir(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    /**
     * Realiza a validação dos campos obrigatórios para um fechamento do Tipo
     * Acerto
     *
     * @param fbhf
     * @throws ServiceException
     */
    private void validarCamposObrigatorio(FuncionarioBancoHorasFechamento fbhf) throws ServiceException {
        if (fbhf.getIdFuncionarioBancoHorasFechamento() == null && fbhf.getTipoAcerto() == null) {
            throw new ServiceException(this.topPontoResponse.campoObrigatorio("Opção de acerto"));
        }
        if (fbhf.getDataFechamento() == null) {
            throw new ServiceException(this.topPontoResponse.campoObrigatorio("Data de fechamento"));
        }
    }

    /**
     * Valida se ja existe um fechamento do tipo acerto para a data do novo
     * fechamento OBS: retirado a validação de tipo especifico
     *
     * @param fbdhf
     * @throws ServiceException
     */
    private void validarJaPossuiAcertoData(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {

        Integer idFuncionario;
        if (fbdhf.getFuncionario() != null && fbdhf.getFuncionario().getIdFuncionario() != null) {
            idFuncionario = fbdhf.getFuncionario().getIdFuncionario();
        } else if (fbdhf.getFuncionarioBancoHoras() != null && fbdhf.getFuncionarioBancoHoras().getFuncionario() != null && fbdhf.getFuncionarioBancoHoras().getFuncionario().getIdFuncionario() != null) {
            idFuncionario = fbdhf.getFuncionarioBancoHoras().getFuncionario().getIdFuncionario();
        } else {
            return;
        }
        List<FuncionarioBancoHorasFechamento> fbhfs
                = this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionario(idFuncionario);
        if (!fbdhf.getRecalcularSaldos()) {
            fbhfs = fbhfs.stream().filter(f -> !f.getIdFuncionarioBancoHorasFechamento().equals(fbdhf.getIdFuncionarioBancoHorasFechamento())).collect(Collectors.toList());
        }
        if (fbhfs
                .stream()
                //                .filter(t
                //                        -> t.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal()))
                .anyMatch(f
                        -> f.getDataFechamento().compareTo(fbdhf.getDataFechamento()) == 0)) {
//            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_ACERTO_JA_CADASTRADO_NA_DATA.getResource()));
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA.getResource()));
        }
    }

    /**
     * Atualiza o fechamento com os novos valores
     *
     * @param fbdhf
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento atualizar(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {
        validarCamposObrigatorio(fbdhf);
        this.opcoesAcerto = new Manual(this);

        Funcionario funcionario;
        if (fbdhf.getFuncionario() != null) {
            funcionario = fbdhf.getFuncionario();
        } else if (fbdhf.getFuncionarioBancoHoras() != null && fbdhf.getFuncionarioBancoHoras().getFuncionario() != null) {
            funcionario = fbdhf.getFuncionarioBancoHoras().getFuncionario();
        } else {
            throw new ServiceException(this.topPontoResponse.alertaNaoCad());
        }

        return this.opcoesAcerto.IniciarProcesso(fbdhf, this.funcionarioBancoDeHorasFechamentoService.getBancoValido(funcionario, fbdhf.getDataFechamento()));
    }

    /**
     * Cria um objeto de fechamento com os dados basicos para o fechamento do
     * tipo acerto
     *
     * @param fechamento
     * @param bancoHoras
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento getFechamentoPadraoAcerto(FuncionarioBancoHorasFechamento fechamento, FuncionarioBancoHoras bancoHoras) throws ServiceException {

        Date dataFechamento = fechamento.getDataFechamento();
        TipoAcerto tipoAcerto = fechamento.getTipoAcerto();
        Boolean manual = fechamento.isManual();

        if (fechamento.getIdFuncionarioBancoHorasFechamento() != null && !this.funcionarioBancoDeHorasFechamentoDao.contains(fechamento)) {
            fechamento = this.funcionarioBancoDeHorasFechamentoService.buscar(FuncionarioBancoHorasFechamento.class, fechamento.getIdFuncionarioBancoHorasFechamento());
            this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fechamento);
        }
        if (!this.funcionarioBancoHorasService.contains(bancoHoras)) {
            bancoHoras = this.funcionarioBancoHorasService.buscar(FuncionarioBancoHoras.class, bancoHoras.getIdFuncionarioBancoHoras());
        } else {
            this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().refresh(bancoHoras);
        }

        FuncionarioBancoHorasFechamento bancoHorasFechamento = new FuncionarioBancoHorasFechamento();

        //se for uma edição adiocna o id a entidade
        if (fechamento.getIdFuncionarioBancoHorasFechamento() != null) {
            bancoHorasFechamento.setIdFuncionarioBancoHorasFechamento(fechamento.getIdFuncionarioBancoHorasFechamento());
        }

        if (fechamento.getColetivo() != null) {
            bancoHorasFechamento.setColetivo(fechamento.getColetivo());
        }

        bancoHorasFechamento.setTipoFechamento(this.tipoFechamentoService.buscar(TipoFechamento.class, CONSTANTES.Enum_TIPO_FECHAMENTO.ACERTO.ordinal()));
        bancoHorasFechamento.setFuncionarioBancoHoras(bancoHoras);
        bancoHorasFechamento.setDataFechamento(dataFechamento);
        bancoHorasFechamento.setTipoAcerto(tipoAcerto);
        bancoHorasFechamento.setManual(manual);

        return bancoHorasFechamento;
    }

    public void validarExlusao(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        validarIdentificador(fechamento);
        this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fechamento);
    }

    private void validarIdentificador(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        if (fechamento == null || fechamento.getIdFuncionarioBancoHorasFechamento() == null) {
            throw new ServiceException(this.topPontoResponse.alertaNaoCad(new FuncionarioBancoHorasFechamento().toString()));
        }
    }

    /**
     * Interface das classes privadas
     */
    private interface IOpcoesAcerto {

        /**
         * Somente para inicio do processo, caso exista uma pre-validação
         *
         * @param fbhf
         * @param fbh
         * @throws ServiceException
         */
        FuncionarioBancoHorasFechamento IniciarProcesso(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh) throws ServiceException;

        /**
         * Realiza o processo de persistencia do fechamento
         *
         * @param fbhf
         * @param fbh
         * @param funcionario
         * @throws ServiceException
         */
        FuncionarioBancoHorasFechamento salvarFechamento(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh, Funcionario funcionario) throws ServiceException;

        /**
         * Ajusta os valores de credito e debito conforme o saldo do BH
         *
         * @param saldoBH
         * @param funcionarioBancoHorasFechamento
         */
        void ajustarSaldos(SaldoBH saldoBH, FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException;
    }

    /**
     * Classe de serviço para o tipo fechamento acerto opção zerar
     *
     * @version 1.0.0 data 29/06/2017
     * @version 1.0.0 data 29/06/2017
     *
     * @author juliano.ezequiel
     */
    private class Zerar implements IOpcoesAcerto {

        private FechamentoAcerto fechamentoAcerto;

        public Zerar(FechamentoAcerto fechamentoAcerto) {
            this.fechamentoAcerto = fechamentoAcerto;
        }

        @Override
        public FuncionarioBancoHorasFechamento IniciarProcesso(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh) throws ServiceException {
            return salvarFechamento(fbhf, fbh, fbh.getFuncionario());
        }

        /**
         * Salva o fechamento do tipo acerto zero
         *
         * @param fbhf
         * @param fbh
         * @param funcionario
         * @throws ServiceException
         */
        @Override
        public FuncionarioBancoHorasFechamento salvarFechamento(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh, Funcionario funcionario) throws ServiceException {
            try {
                LOGGER.debug("criando novo do fechamento tipo acerto zerar ");
                //consulta o saldo
                SaldoBH saldoBH = this.fechamentoAcerto.verificarSaldoBH.calcular(funcionario, fbhf.getDataFechamento());

                if (saldoBH.getSaldo().getTime() == 0 && fbhf.getIdFuncionarioBancoHorasFechamento() == null) {
                    throw new ServiceException(this.fechamentoAcerto.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_SALDO_ZERADO.getResource()));
                }

                if (funcionarioService.contains(funcionario)) {
                    funcionarioService.refresh(funcionario);
                }

                FuncionarioBancoHorasFechamento fechamento = this.fechamentoAcerto.getFechamentoPadraoAcerto(fbhf, fbh);
                if (fbhf.getIdFuncionarioBancoHorasFechamento() == null) {
                    ajustarSaldos(saldoBH, fechamento);
                } else {
                    fechamento.setCredito(fbhf.getCredito());
                    fechamento.setDebito(fbhf.getDebito());
                }

                LOGGER.debug("Valor de crédito zerar : {} ", Utils.horasInt(fechamento.getCredito()));
                LOGGER.debug("Valor de débito zerar : {}", Utils.horasInt(fechamento.getDebito()));

                Coletivo coletivo = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(fechamento);

                //salva o novo fechamento
                fechamento = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoDao.save(fechamento);
                this.fechamentoAcerto.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, fechamento);

                this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionario, fechamento.getDataFechamento());

                this.fechamentoAcerto.coletivoService.excluirColetivoSemVinculo(coletivo);

                return fechamento;
            } catch (DaoException ex) {
                throw new ServiceException(this.fechamentoAcerto.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
            }
        }

        /**
         * Ajusta os saldos de acordo com o resultado da consulta
         *
         * @param saldoBH
         * @param funcionarioBancoHorasFechamento
         */
        @Override
        public void ajustarSaldos(SaldoBH saldoBH, FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
            //adiciona os debitos e cretidos necessários
            if (saldoBH.getSaldo().getTime() < DateHelper.DataPadrao().getTime()) { //saldo menor que 0, possui debito
                funcionarioBancoHorasFechamento.setCredito(new Date(saldoBH.getSaldo().getTime() * -1));
                funcionarioBancoHorasFechamento.setDebito(DateHelper.DataPadrao());
            } else if (saldoBH.getSaldo().getTime() > DateHelper.DataPadrao().getTime()) { // se maior que 0 possui crédito
                funcionarioBancoHorasFechamento.setDebito(saldoBH.getSaldo());
                funcionarioBancoHorasFechamento.setCredito(DateHelper.DataPadrao());
            } else { // se não salva com datas zeradas
                funcionarioBancoHorasFechamento.setDebito(DateHelper.DataPadrao());
                funcionarioBancoHorasFechamento.setCredito(DateHelper.DataPadrao());
            }
        }

    }

    /**
     * Classe de serviço para o tipo fechamento acerto opção Gatilhos
     *
     * @version 1.0.0 data 29/06/2017
     * @version 1.0.0 data 29/06/2017
     *
     * @author juliano.ezequiel
     */
    private class Gatilho implements IOpcoesAcerto {

        private FechamentoAcerto fechamentoAcerto;

        public Gatilho(FechamentoAcerto fechamentoAcerto) {
            this.fechamentoAcerto = fechamentoAcerto;
        }

        @Override
        public FuncionarioBancoHorasFechamento IniciarProcesso(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh) throws ServiceException {
            if (fbhf.getGatilho() == null) {
                throw new ServiceException(this.fechamentoAcerto.topPontoResponse.campoObrigatorio("Gatilhos"));
            }
            return salvarFechamento(fbhf, fbh, fbh.getFuncionario());
        }

        @Override
        public FuncionarioBancoHorasFechamento salvarFechamento(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh, Funcionario funcionario) throws ServiceException {
            try {
                LOGGER.debug("criando novo do fechamento tipo acerto gatilho");

                //validas se os gatilhos estão zerados
                if (fbhf.getGatilho().isManual() == true && fbhf.getGatilho().getPositivo().getTime() == DateHelper.DataPadrao().getTime()
                        && fbhf.getGatilho().getNegativo().getTime() == DateHelper.DataPadrao().getTime()) {
                    throw new ServiceException(this.fechamentoAcerto.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_GATILHOS_ZERADOS.getResource()));
                } else if (fbhf.getGatilho().isManual() == false) {
                    FuncionarioBancoHoras funcionarioBancoHoras
                            = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.getBancoValido(funcionario, fbhf.getDataFechamento());
                    fbhf.getGatilho().setNegativo(funcionarioBancoHoras.getBancoHoras().getGatilhoNegativo());
                    fbhf.getGatilho().setPositivo(funcionarioBancoHoras.getBancoHoras().getGatilhoPositivo());
                }

                //consulta o saldo
                SaldoBH saldoBH = this.fechamentoAcerto.verificarSaldoBH.calcular(funcionario, fbhf.getDataFechamento());

                FuncionarioBancoHorasFechamento fechamento = this.fechamentoAcerto.getFechamentoPadraoAcerto(fbhf, fbh);

                //se for uma edição adiocna o id a entidade
                if (fbhf.getIdFuncionarioBancoHorasFechamento() != null) {
                    fechamento.setIdFuncionarioBancoHorasFechamento(fbhf.getIdFuncionarioBancoHorasFechamento());
                }

                //atribio os gatilhos vindo do front-end para enviar para os ajustes
                fechamento.setGatilho(fbhf.getGatilho());

                ajustarSaldos(saldoBH, fechamento);

                LOGGER.debug("Valor de crédito gatilho : {} ", Utils.horasInt(fechamento.getCredito()));
                LOGGER.debug("Valor de débito gatilho : {}", Utils.horasInt(fechamento.getDebito()));

                Coletivo coletivo = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(fechamento);

                //salva o novo fechamento
                fechamento = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoDao.save(fechamento);
                this.fechamentoAcerto.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, fechamento);

                this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionario, fechamento.getDataFechamento());

                this.fechamentoAcerto.coletivoService.excluirColetivoSemVinculo(coletivo);

                return fechamento;
            } catch (DaoException ex) {
                throw new ServiceException(this.fechamentoAcerto.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
            }
        }

        @Override
        public void ajustarSaldos(SaldoBH saldoBH, FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {

            //adiciona os debitos e cretidos necessários
            if (saldoBH.getSaldo().getTime() < DateHelper.DataPadrao().getTime()) { //saldo menor que 0, possui debito
                //valida se atingiu o limite mínimo
                if (funcionarioBancoHorasFechamento.getGatilho().getNegativo().getTime() > 0
                        && (saldoBH.getSaldo().getTime() * -1) < funcionarioBancoHorasFechamento.getGatilho().getNegativo().getTime()) {
                    throw new ServiceException(this.fechamentoAcerto.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_GATILHO_NAO_ATINGIDO.getResource()));
                }
                funcionarioBancoHorasFechamento.setCredito(funcionarioBancoHorasFechamento.getGatilho().getNegativo());
                funcionarioBancoHorasFechamento.setDebito(DateHelper.DataPadrao());

            } else if (saldoBH.getSaldo().getTime() > DateHelper.DataPadrao().getTime()) { // se maior que 0 possui crédito

                if (funcionarioBancoHorasFechamento.getGatilho().getPositivo().getTime() > 0
                        && saldoBH.getSaldo().getTime() < funcionarioBancoHorasFechamento.getGatilho().getPositivo().getTime()) {
                    throw new ServiceException(this.fechamentoAcerto.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_GATILHO_NAO_ATINGIDO.getResource()));
                }
                funcionarioBancoHorasFechamento.setDebito(funcionarioBancoHorasFechamento.getGatilho().getPositivo());
                funcionarioBancoHorasFechamento.setCredito(DateHelper.DataPadrao());

            } else { // se não salva com datas zeradas
                funcionarioBancoHorasFechamento.setDebito(DateHelper.DataPadrao());
                funcionarioBancoHorasFechamento.setCredito(DateHelper.DataPadrao());
            }
        }

    }

    /**
     * Classe de serviço para o tipo fechamento acerto opção Manual
     *
     * @version 1.0.0 data 29/06/2017
     * @version 1.0.0 data 29/06/2017
     *
     * @author juliano.ezequiel
     */
    private class Manual implements IOpcoesAcerto {

        private FechamentoAcerto fechamentoAcerto;

        public Manual(FechamentoAcerto fechamentoAcerto) {
            this.fechamentoAcerto = fechamentoAcerto;
        }

        @Override
        public FuncionarioBancoHorasFechamento IniciarProcesso(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh) throws ServiceException {
            return salvarFechamento(fbhf, fbh, fbh.getFuncionario());
        }

        @Override
        public FuncionarioBancoHorasFechamento salvarFechamento(FuncionarioBancoHorasFechamento fbhf, FuncionarioBancoHoras fbh, Funcionario funcionario) throws ServiceException {
            try {
                LOGGER.debug("criando novo do fechamento tipo acerto manual");

                FuncionarioBancoHorasFechamento fechamento = this.fechamentoAcerto.getFechamentoPadraoAcerto(fbhf, fbh);

                if (fbhf.getCredito() != null && fbhf.getCredito().getTime() == DateHelper.DataPadrao().getTime()
                        && fbhf.getDebito() != null && fbhf.getDebito().getTime() == DateHelper.DataPadrao().getTime()) {
                    throw new ServiceException(this.fechamentoAcerto.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_ACERTO_MANUAL_ZERADO.getResource()));
                }

                fechamento.setCredito(fbhf.getCredito());
                fechamento.setDebito(fbhf.getDebito());

                LOGGER.debug("Valor de crédito manual : {} ", Utils.horasInt(fechamento.getCredito()));
                LOGGER.debug("Valor de débito manual : {}", Utils.horasInt(fechamento.getDebito()));

                Coletivo coletivo = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(fechamento);

                //salva o novo fechamento
                fechamento = this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoDao.save(fechamento);
                this.fechamentoAcerto.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, fechamento);
                if (fbhf.getRecalcularSaldos()) {
                    this.fechamentoAcerto.funcionarioBancoDeHorasFechamentoService.recalcularSubTotaisPosteriores(funcionario, fechamento.getDataFechamento());
                }
                this.fechamentoAcerto.coletivoService.excluirColetivoSemVinculo(coletivo);
                return fechamento;
            } catch (DaoException ex) {
                throw new ServiceException(this.fechamentoAcerto.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
            }
        }

        @Override
        public void ajustarSaldos(SaldoBH saldoBH, FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

}
