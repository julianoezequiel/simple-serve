package com.topdata.toppontoweb.services.funcionario.bancohoras.fechamento;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioBancoDeHorasFechamentoDao;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.tipo.TipoFechamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.0.0 data 28/06/2017
 * @since 1.0.0 data 28/06/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class FechamentoEdicaoSaldo {

    public final static Logger LOGGER = LoggerFactory.getLogger(FechamentoAcerto.class.getName());

    //<editor-fold defaultstate="" desc="CDI">
    @Autowired
    private TopPontoResponse topPontoResponse;
    @Autowired
    private AuditoriaServices auditoriaServices;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoService funcionarioBancoDeHorasFechamentoService;
    @Autowired
    private FuncionarioBancoDeHorasFechamentoDao funcionarioBancoDeHorasFechamentoDao;
    @Autowired
    private TipoFechamentoService tipoFechamentoService;
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;
    @Autowired
    private ColetivoService coletivoService;
    //</editor-fold>

    public FuncionarioBancoHorasFechamento salvar(FuncionarioBancoHorasFechamento fbdhf) throws ServiceException {
        try {
            LOGGER.debug("Inicio processo salvar fechamento tipo edição de saldo");

            validarJaPossuiEdicaoSaldo(fbdhf);

            validarSubtotaisPosterior(fbdhf);

            FuncionarioBancoHoras fbh = this.funcionarioBancoDeHorasFechamentoService.getBancoValido(fbdhf.getFuncionario(), fbdhf.getDataFechamento());

            //TODO: Regra para validar se existe um subtotal inicial posterior a data ;
            //apresentar a mensagem :
            //Não é possível realizar a edição de saldo para essa data, pois existem subtotais posteriores que marcam o saldo inicial de banco de horas.
            //Quando edição de saldo e existir subtotal posterior que não é inicio de bh, 
            //deverá apresentar a mensagem: 
            //Existem subtotais posteriores a esta data. Se for criada uma edição de saldo nesta data, todos os subtotais posteriores serão excluídos. Confirma a exclusão?
            if (this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().contains(fbh)) {
                this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().refresh(fbh);
            } else {
                fbh = this.funcionarioBancoHorasService.getFuncionarioBancoHorasDao().find(FuncionarioBancoHoras.class, fbh.getIdFuncionarioBancoHoras());
            }

            FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = new FuncionarioBancoHorasFechamento();
            //se for uma edição adiciona o id a entidade
            if (fbdhf.getIdFuncionarioBancoHorasFechamento() != null) {
                funcionarioBancoHorasFechamento.setIdFuncionarioBancoHorasFechamento(fbdhf.getIdFuncionarioBancoHorasFechamento());
                this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fbdhf);
            }
            if (fbdhf.getColetivo() != null) {
                funcionarioBancoHorasFechamento.setColetivo(fbdhf.getColetivo());
            }

            funcionarioBancoHorasFechamento.setTipoFechamento(this.tipoFechamentoService.buscar(TipoFechamento.class, CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal()));
            funcionarioBancoHorasFechamento.setDataFechamento(fbdhf.getDataFechamento());
            funcionarioBancoHorasFechamento.setFuncionarioBancoHoras(fbh);

            funcionarioBancoHorasFechamento.setCredito(fbdhf.getCredito());
            funcionarioBancoHorasFechamento.setDebito(fbdhf.getDebito());
            funcionarioBancoHorasFechamento.setManual(fbdhf.isManual());

            Coletivo coletivo = this.funcionarioBancoDeHorasFechamentoService.validarAlteracaoColetivo(funcionarioBancoHorasFechamento);

            funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, funcionarioBancoHorasFechamento);

            this.coletivoService.excluirColetivoSemVinculo(coletivo);

            return funcionarioBancoHorasFechamento;

        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroSalvar(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    public FuncionarioBancoHorasFechamento excluir(Object id) throws ServiceException {
        try {
            FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = this.funcionarioBancoDeHorasFechamentoService.buscar(FuncionarioBancoHorasFechamento.class, (Integer) id);
            validarIdentificador(funcionarioBancoHorasFechamento);

            //TODO:
            //Exclusão edição de saldo: 
            //Ao excluir, apresentar a mensagem: 
            //Existem subtotais posteriores a esta edição de saldo. 
            //Se a edição de saldo for excluída, estes subtotais serão excluídos. Confirma a exclusão?
            Coletivo coletivo = funcionarioBancoHorasFechamento.getColetivo();
            if (coletivo != null) {
                //remove vinculo entre coletivo e o fechamento
                funcionarioBancoHorasFechamento.setColetivo(null);
                this.funcionarioBancoDeHorasFechamentoDao.save(funcionarioBancoHorasFechamento);
                //chama a função para verificar não exsite mais vinculos entre o coletivo e outros fechamentos
                this.coletivoService.excluirColetivoSemVinculo(coletivo);
            }
            this.funcionarioBancoDeHorasFechamentoDao.excluirEntidade(funcionarioBancoHorasFechamento);
            validarSubtotaisPosterior(funcionarioBancoHorasFechamento);
            this.auditoriaServices.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_BANCO_DE_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioBancoHorasFechamento);
            return funcionarioBancoHorasFechamento;
        } catch (DaoException ex) {
            throw new ServiceException(this.topPontoResponse.erroExcluir(new FuncionarioBancoHorasFechamento().toString()), ex);
        }
    }

    public void validarSubtotaisPosterior(FuncionarioBancoHorasFechamento fbhf) throws ServiceException {

        Funcionario funcionario;

        if (fbhf.getFuncionario() != null) {
            funcionario = fbhf.getFuncionario();
        } else if (fbhf.getFuncionarioBancoHoras() != null && fbhf.getFuncionarioBancoHoras().getFuncionario() != null) {
            funcionario = fbhf.getFuncionarioBancoHoras().getFuncionario();
        } else {
            return;
        }

        List<FuncionarioBancoHorasFechamento> fechamentos = this.funcionarioBancoDeHorasFechamentoService.buscarPorFuncionarioPosterior(funcionario, fbhf.getDataFechamento());
        List<FuncionarioBancoHoras> bancoHorasList = this.funcionarioBancoHorasService.buscarPorFuncionario(funcionario.getIdFuncionario());
        List<Date> ListaDataFechamento = fechamentos.stream().filter(f -> f.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())).map(m -> m.getDataFechamento()).collect(Collectors.toList());
        List<Date> ListaDataInicioBanco = bancoHorasList.stream().map(m -> m.getDataInicio()).collect(Collectors.toList());
        //valida se existe subtotal que marca o saldo inicial do banco de horas
        for (Date dataFechamento : ListaDataFechamento) {
            for (Date dataInicioBanco : ListaDataInicioBanco) {
                if (dataFechamento.compareTo(dataInicioBanco) == 0) {
                    throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_EDICAO_SALDO_SUBTOTAL_POSTERIOR.getResource()));
                }
            }
        }

        //exclui todos os subtotais posterior e que não sejam inicio de banco de horas
        List<FuncionarioBancoHorasFechamento> listaParaExcluir = fechamentos.stream().filter(f -> f.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.SUBTOTAL.ordinal())).collect(Collectors.toList());
        for (FuncionarioBancoHorasFechamento fechamento : listaParaExcluir) {
            this.excluir(fechamento.getIdFuncionarioBancoHorasFechamento());
        }

    }

    //retirado a validação de tipo especifico de fechamento
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
        if (!fbdhf.getRecalcularSaldos()) {
            fbhfs = fbhfs.stream().filter(f -> !f.getIdFuncionarioBancoHorasFechamento().equals(fbdhf.getIdFuncionarioBancoHorasFechamento())).collect(Collectors.toList());
        }
        if (fbdhf.getIdFuncionarioBancoHorasFechamento() == null) {
            if (fbhfs
                    .stream()
                    //                    .filter(t
                    //                            -> t.getTipoFechamento().getIdTipoFechamento().equals(CONSTANTES.Enum_TIPO_FECHAMENTO.EDICAO_DE_SALDO.ordinal()))
                    .anyMatch(f
                            -> f.getDataFechamento().compareTo(fbdhf.getDataFechamento()) == 0)) {
//                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_EDICAOSALDO_JA_CADASTRADO_NA_DATA.getResource()));
                throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA.getResource()));
            }
        } else if (fbhfs
                .stream()
                //                .filter(ff
                //                        -> !ff.getIdFuncionarioBancoHorasFechamento().equals(fbdhf.getIdFuncionarioBancoHorasFechamento()))
                .anyMatch(f
                        -> f.getDataFechamento().compareTo(fbdhf.getDataFechamento()) == 0)) {
//            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_EDICAOSALDO_JA_CADASTRADO_NA_DATA.getResource()));
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_BANCO_DE_HORAS.ALERTA_FECHAMENTO_JA_CADASTRADO_NA_DATA.getResource()));
        }
    }

    public FuncionarioBancoHorasFechamento atualizar(FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento) throws ServiceException {
        funcionarioBancoHorasFechamento.setRecalcularSaldos(Boolean.FALSE);
        return salvar(funcionarioBancoHorasFechamento);
    }

    private void validarIdentificador(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        if (fechamento == null) {
            throw new ServiceException(this.topPontoResponse.alertaNaoCad(new FuncionarioBancoHorasFechamento().toString()));
        }
    }

    public void validarExlusao(FuncionarioBancoHorasFechamento fechamento) throws ServiceException {
        validarIdentificador(fechamento);
        this.funcionarioBancoDeHorasFechamentoService.validarPossuiFechamento(fechamento);
    }

}
