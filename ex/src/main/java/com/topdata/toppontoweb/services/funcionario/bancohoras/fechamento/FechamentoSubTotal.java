package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioBancoDeHorasFechamentoDao;
import com.topdata.toppontoweb.dto.funcionario.bancohoras.SaldoBH;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.gerafrequencia.integracao.VerificarSaldoBH;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;

/**
 * Classe de regras para o tipo de fechamento subtotal
 *
 * @version 1.0.0 data 28/06/2017
 * @since 1.0.0 data 28/06/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoSubTotal {

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

    /**
     * Validação utilizada para verificar se o fechamento do banco poderá ser
     * editado. Pois caso o fechamento seja um SUBTOTAL inicial do banco, ele
     * não poderá ser editado ou excluido.
     *
     * @param funcionarioBancoHorasFechamento
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento verificarSeSaldoInicial(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoService.buscar(FuncionarioBancoHorasFechamento.class, funcionarioBancoHorasFechamento.getIdFuncionarioBancoHorasFechamento());
        if ((funcionarioBancoHorasFechamento.getFuncionarioBancoHoras().getDataInicio().compareTo(funcionarioBancoHorasFechamento.getDataFechamento()) == 0)
                && funcionarioBancoHorasFechamento.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())) {
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_EDITAR_SUBTOTAL.getResource()));
        }
        return funcionarioBancoHorasFechamento;
    }

    /**
     * Realiza a exclusão com as validações especificas do fechamento do tipo
     * subtotal
     *
     * @param id
     * @param validarSaldo
     * @return
     * @throws ServiceException
     */
    public FuncionarioBancoHorasFechamento excluir(Object id, boolean validarSaldo) throws ServiceException {
        try {
            FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoService.buscar(FuncionarioBancoHorasFechamento.class, (Integer) id);
            if (validarSaldo) {
                validarSaldoInicial(funcionarioBancoHorasFechamento);
            }
            validarIdentificador(funcionarioBancoHorasFechamento);
            Coletivo coletivo = funcionarioBancoHorasFechamento.getColetivo();
            if (coletivo != null) {
                //remove vinculo entre coletivo e o fechamento
                funcionarioBancoHorasFechamento.setColetivo(null);
                this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
                //chama a função para verificar não exsite mais vinculos entre o coletivo e outros fechamentos
                this.coletivoService.excluirColetivoSemVinculo(coletivo);
            }
            this.funcionarioBancoDeHorasFechamentoDao.delete(funcionarioBancoHorasFechamento);
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHorasFechamento);
            return funcionarioBancoHorasFechamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroExcluir(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    private void validarIdentificador(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        if (fechamento == null || fechamento.getIdFuncionarioBancoHorasFechamento() == null) {
            throw new ServiceException(this.topPontoResponse.alertaNaoCad(new FuncionarioBancoHorasFechamento().toString()));
        }
    }

    private void validarSaldoInicial(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        verificarSeSaldoInicial(fechamento);
    }

    public void validarExlusao(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        validarIdentificador(fechamento);
        validarSaldoInicial(fechamento);
        this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fechamento);
    }

//    public SaldoBH consultaSaldoUltimoBanco(Funcionario funcionario, Date dataInicio, FuncionarioBancoHoras fbh) throws ServiceException {
//        //List<FuncionarioBancoHoras> fbhs = this.funcionarioBancoHorasService.buscarPorFuncionario(funcionario.getIdFuncionario());
//        //Date d = dataInicio;
////        List<FuncionarioBancoHoras> fbhList = fbhs
////                .stream()
////                .filter(f -> f.getDataInicio().compareTo(d) <= 0)
////                .sorted(Comparator.comparing(FuncionarioBancoHoras::getIdFuncionarioBancoHoras).reversed())
////                .collect(Collectors.toList());
////        if (fbh.getIdFuncionarioBancoHoras() != null) {
////            fbhList = fbhList.stream().filter(f -> !f.getIdFuncionarioBancoHoras().equals(fbh.getIdFuncionarioBancoHoras())).collect(Collectors.toList());
////        }
////        if (!fbhList.isEmpty()) {
////            dataInicio = fbhList.get(0).getDataFim() != null ? fbhList.get(0).getDataFim() : dataInicio;
////        }
//
//        //consulta o saldo
//        SaldoBH saldoBH = this.verificarSaldoBH.calcular(funcionario, dataInicio);
//        return saldoBH;
//    }

    /**
     * Cria na base um subtotal com base no saldo do fechamento anterior
     *
     * @param dataInicio
     * @param funcionarioBancoHoras
     * @param funcionario
     * @throws ServiceException
     */
    public void inserirFechamentoBanco(Date dataInicio, FuncionarioBancoHoras funcionarioBancoHoras, Funcionario funcionario, SaldoBH saldoBH, Coletivo coletivo) throws ServiceException {
        try {
            LOGGER.debug("criando novo do fechamento inicial subtotal com saldo anterior");

            FuncionarioBancoHorasFechamento fbhf = new FuncionarioBancoHorasFechamento();
            fbhf.setFuncionario(funcionario);
            fbhf.setFuncionarioBancoHoras(funcionarioBancoHoras);
            fbhf.setDataFechamento(dataInicio);
            fbhf.setColetivo(coletivo);

            validarJaPossuiEdicaoSaldo(fbhf);

            if (this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().contains(funcionarioBancoHoras)) {
                this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().refresh(funcionarioBancoHoras);
            } else {
                funcionarioBancoHoras = this.funcionarioBancoHorasService.buscar(FuncionarioBancoHoras.class, funcionarioBancoHoras.getIdFuncionarioBancoHoras());
            }

            FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = new FuncionarioBancoHorasFechamento();
            funcionarioBancoHorasFechamento.setTipoFechamento(this.tipoFechamentoService.buscar(TipoFechamento.class, CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal()));
            funcionarioBancoHorasFechamento.setDataFechamento(dataInicio);
            funcionarioBancoHorasFechamento.setFuncionarioBancoHoras(funcionarioBancoHoras);
            funcionarioBancoHorasFechamento.setManual(fbhf != null);
            funcionarioBancoHorasFechamento.setColetivo(fbhf.getColetivo());
            ajustarSaldos(saldoBH, funcionarioBancoHorasFechamento);

            Coletivo coletivo2 = this.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(funcionarioBancoHorasFechamento, true);

            LOGGER.debug("Valor de crédito subtotal : {} ", Utils.horasInt(funcionarioBancoHorasFechamento.getCredito()));
            LOGGER.debug("Valor de débito sutotal : {}", Utils.horasInt(funcionarioBancoHorasFechamento.getDebito()));

            //salva o novo fechamento    
            funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, funcionarioBancoHorasFechamento);

            this.coletivoService.excluirColetivoSemVinculo(coletivo2);

        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    public FuncionarioBancoHorasFechamento atualizarFechamentoSubTotal(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        try {
            LOGGER.debug("atualizando fechament subtotal");

            validarJaPossuiEdicaoSaldo(fechamento);

            if (fechamento.getIdFuncionarioBancoHorasFechamento() == null || fechamento.getFuncionarioBancoHoras() == null) {
                throw new ServiceException(this.topPontoResponse.alertaNaoCad(fechamento.toString()));
            }
            this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fechamento);

            SaldoBH saldoBH = this.verificarSaldoBH.calcular(fechamento.getFuncionarioBancoHoras().getFuncionario(), fechamento.getDataFechamento());

            ajustarSaldos(saldoBH, fechamento);

            FuncionarioBancoHorasFechamento fbhf1 = this.funcionarioBancoDeHorasFechamentoDao.find(FuncionarioBancoHorasFechamento.class, fechamento.getIdFuncionarioBancoHorasFechamento());

            Coletivo coletivo = this.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(fechamento, true);

            if (this.funcionarioBancoHorasService.contains(fbhf1.getFuncionarioBancoHoras())) {
                this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().refresh(fbhf1.getFuncionarioBancoHoras());
            } else {
                fbhf1.setFuncionarioBancoHoras(this.funcionarioBancoHorasService.buscar(FuncionarioBancoHoras.class, fbhf1.getFuncionarioBancoHoras().getIdFuncionarioBancoHoras()));
            }

            ajustarSaldos(saldoBH, fechamento);

            fbhf1.setCredito(fechamento.getCredito());
            fbhf1.setDebito(fechamento.getDebito());
            fbhf1.setDataFechamento(fechamento.getDataFechamento());
            fbhf1.setTipoFechamento(fechamento.getTipoFechamento());
            fbhf1.setManual(fechamento.isManual());
            fbhf1.setColetivo(fechamento.getColetivo());

            LOGGER.debug("Valor de crétido subtotal : {} ", Utils.horasInt(fbhf1.getCredito()));
            LOGGER.debug("Valor de débito sutotal : {}", Utils.horasInt(fbhf1.getDebito()));

            //salva o novo fechamento    
            fechamento = this.funcionarioBancoDeHorasFechamentoDao.save(fbhf1);
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, fechamento);

            this.coletivoService.excluirColetivoSemVinculo(coletivo);

            return fechamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    public void salvarFechamento(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        FuncionarioBancoHoras fbh = this.funcionarioBancoDeHorasFechamentoService.getBancoValido(funcionarioBancoHorasFechamento.getFuncionario(), funcionarioBancoHorasFechamento.getDataFechamento());
        SaldoBH saldoBH = verificarSaldoBH.calcular(funcionarioBancoHorasFechamento.getFuncionario(), funcionarioBancoHorasFechamento.getDataFechamento());
        criarFechamentoSubtotalComSaldosAnterior(funcionarioBancoHorasFechamento.getDataFechamento(), fbh, fbh.getFuncionario(), saldoBH, funcionarioBancoHorasFechamento.getColetivo());
    }

    /**
     * Adiciona os débitos e créditos que serão salvos na base de dados. Os
     * valores já estão calculados pelo gera frequência
     *
     * @param saldoBH
     * @param funcionarioBancoHorasFechamento
     */
    private void ajustarSaldos(SaldoBH saldoBH, FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) {
        funcionarioBancoHorasFechamento.setDebito(new Date(saldoBH.getDebito().getTime()));
        funcionarioBancoHorasFechamento.setCredito(new Date(saldoBH.getCredito().getTime()));
    }

    /**
     * Sobrecarga do método
     *
     * @param funcionarioBancoHoras
     */
    private void criarFechamentoSubtotalComSaldosAnterior(FuncionarioBancoHoras funcionarioBancoHoras, SaldoBH saldoBH, Coletivo coletivo) throws ServiceException {
        criarFechamentoSubtotalComSaldosAnterior(funcionarioBancoHoras.getDataInicio(), funcionarioBancoHoras, funcionarioBancoHoras.getFuncionario(), saldoBH, coletivo);
    }

    /**
     * Cria na base um subtotal com base no saldo do fechamento anterior
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    private void criarFechamentoSubtotalComSaldosAnterior(Date dataInicio, FuncionarioBancoHoras funcionarioBancoHoras, Funcionario funcionario, SaldoBH saldoBH, Coletivo coletivo) throws ServiceException {
        this.inserirFechamentoBanco(dataInicio, funcionarioBancoHoras, funcionario, saldoBH, coletivo);
    }

    /**
     * Função utilziada quando é relizado o cadastro do banco de horas para o
     * funcionario. è realizado o cadastro de um fechamento inicial zerado
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    public void criarFechamentoSubtotal(FuncionarioBancoHoras funcionarioBancoHoras, SaldoBH saldoBH) throws ServiceException {
        LOGGER.debug("criação do fechamento inicial de subtotal");
        criarFechamentoSubtotalComSaldosAnterior(funcionarioBancoHoras, saldoBH, null);
    }

    /**
     * Realiza as validações e a atualização quando necessário do subTotal
     * criado no inicio do cadastro do banco de horas para o funcionário
     *
     * @param funcionarioBancoHorasAntigo
     * @param funcionarioBancoHorasNovo
     * @throws ServiceException
     */
    public void atualizarFechamentoSubTotalInicial(FuncionarioBancoHoras funcionarioBancoHorasAntigo, FuncionarioBancoHoras funcionarioBancoHorasNovo) throws ServiceException {

        LOGGER.debug("Atualização do fechamento inicial de subtotal");

        List<FuncionarioBancoHoras> funcionarioBancoHorasList
                = this.funcionarioBancoHorasService.buscarPorFuncionario(funcionarioBancoHorasNovo.getFuncionario().getIdFuncionario());

        //se for uma atualização de um subtotal existente
        Optional<FuncionarioBancoHoras> fbh = funcionarioBancoHorasList.stream().filter(f
                -> f.getIdFuncionarioBancoHoras().equals(funcionarioBancoHorasNovo.getIdFuncionarioBancoHoras())).findAny();
        if (fbh.isPresent()) {
            //se houve alteração nas datas ou no banco, gera um novo subtotal
            if (fbh.get().getDataInicio().compareTo(funcionarioBancoHorasAntigo.getDataInicio()) != 0
                    || fbh.get().getDataFim() != null && funcionarioBancoHorasAntigo.getDataFim() != null
                    && fbh.get().getDataFim().compareTo(funcionarioBancoHorasAntigo.getDataFim()) != 0) {
                atualizarFechamentoSubtotalZerado(funcionarioBancoHorasNovo);
            } else if (funcionarioBancoHorasNovo.getDataFim() != null) {
                //remover os fechamento zerados que a data seja maior que a data de termino do bando de horas
                List<FuncionarioBancoHorasFechamento> fechamentosParaExcluir
                        = this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioBancoHoras(fbh.get())
                        .stream().filter(bh
                                -> bh.getDataFechamento().compareTo(funcionarioBancoHorasNovo.getDataFim()) > 0)
                        .collect(Collectors.toList());

                fechamentosParaExcluir.stream().forEach(fe -> this.excluirSemValidar(fe));
            }
        }
    }

    /**
     * Realiza a exclusão direta da entidade FuncionarioBancoHorasFechamento.
     * Esdta função é utilizada para remover os subtotasi que estão com a data
     * superior a data de fechamento do ultimo banco de horas
     *
     * @param fbhf
     */
    public void excluirSemValidar(FuncionarioBancoHorasFechamento fbhf) {
        try {
            excluir(fbhf.getIdFuncionarioBancoHorasFechamento(), false);
        } catch (ServiceException ex) {
            Logger.getLogger(FuncionarioBancoDeHorasFechamentoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Atualiza um subtotal
     *
     * @param funcionarioBancoHoras
     * @throws ServiceException
     */
    private void atualizarFechamentoSubtotalZerado(FuncionarioBancoHoras funcionarioBancoHoras) throws ServiceException {
        try {
            LOGGER.debug("atualizando fechamento inicial subtotal de saldo");
            List<FuncionarioBancoHorasFechamento> funcionarioBancoHorasFechamentoList = this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioBancoHoras(funcionarioBancoHoras);

            funcionarioBancoHorasFechamentoList
                    = funcionarioBancoHorasFechamentoList.stream().filter(f
                            -> f.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal()))
                    .sorted(Comparator.comparing(FuncionarioBancoHorasFechamento::getDataFechamento))
                    .collect(Collectors.toList());

            if (!funcionarioBancoHorasFechamentoList.isEmpty()) {
                FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = funcionarioBancoHorasFechamentoList.iterator().next();
                funcionarioBancoHorasFechamento.setDataFechamento(funcionarioBancoHoras.getDataInicio());
                funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
                this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, funcionarioBancoHorasFechamento);
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    //retirado a validação de tipo especifico
    private void validarJaPossuiEdicaoSaldo(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {
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
//        if (!fbdhf.getRecalcularSaldos()) {
//            fbhfs = fbhfs.stream().filter(f -> !f.getIdFuncionarioBancoHorasFechamento().equals(fbdhf.getIdFuncionarioBancoHorasFechamento())).collect(Collectors.toList());
//        }
        if (fbdhf.getIdFuncionarioBancoHorasFechamento() == null) {
            if (fbhfs
                    .stream()
                    //                    .filter(t
                    //                            -> t.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal()))
                    .anyMatch(f
                            -> f.getDataFechamento().compareTo(fbdhf.getDataFechamento()) == 0)) {
//                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_SUBTOTAL_JA_CADASTRADO_NA_DATA.getResource()));
                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA.getResource()));
            }
        } else if (fbhfs
                .stream()
                //                .filter(t
                //                        -> t.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal()))
                .filter(ff
                        -> !ff.getIdFuncionarioBancoHorasFechamento().equals(fbdhf.getIdFuncionarioBancoHorasFechamento()))
                .anyMatch(f -> f.getDataFechamento().compareTo(fbdhf.getDataFechamento()) == 0)) {
//            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_SUBTOTAL_JA_CADASTRADO_NA_DATA.getResource()));
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA.getResource()));
        }
    }

    public VerificarSaldoBH getVerificarSaldoBH() {
        return verificarSaldoBH;
    }
       

}
