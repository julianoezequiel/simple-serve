package com.topdata.toppontoweb.services.bancoHoras;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.bancohoras.BancoHorasDao;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras_;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.configuracoes.PercentuaisAcrescimoServices;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.bancohoras.FuncionarioBancoHorasService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 18/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class BancoHorasServices extends TopPontoService<BancoHoras, Object>
        implements ValidacoesCadastro<BancoHoras, Object> {

    @Autowired
    BancoHorasDao bancoHorasDao;
    @Autowired
    BancoHorasLimiteServices bancoHorasLimiteServices;
    @Autowired
    PercentuaisAcrescimoServices acrescimoServices;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private FuncionarioBancoHorasService funcionarioBancoHorasService;

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public BancoHoras buscar(Class<BancoHoras> entidade, Object id) throws ServiceException {
        try {
            BancoHoras bh = (BancoHoras) this.getDao().find(BancoHoras.class, id);
            if (bh == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new BancoHoras().toString()));
            }
            return bh;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * Busca na base um banco de horas pelo seu id
     *
     * @param idBancoHoras
     * @return
     * @throws ServiceException
     */
    public BancoHoras buscar(Integer idBancoHoras) throws ServiceException {
        try {
            BancoHoras bh = bancoHorasDao.find(idBancoHoras);
            bh.setBancoHorasLimiteList(bancoHorasLimiteServices.buscarPorIdBancoHoras(BancoHorasLimite.class, idBancoHoras));
            bh.setPercentuaisAcrescimo(acrescimoServices.buscarPorBancoHoras(idBancoHoras));
            return bh;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }

    }

    @Override
    public Response salvar(BancoHoras bancoHoras) throws ServiceException {
        try {
            bancoHoras = (BancoHoras) this.getDao().save(validarSalvar(bancoHoras));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_BANCO_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, bancoHoras);
            return this.getTopPontoResponse().sucessoSalvar(bancoHoras);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(bancoHoras), ex);
        }
    }

    @Override
    public Response atualizar(BancoHoras bancoHoras) throws ServiceException {
        try {
            bancoHoras = (BancoHoras) this.getDao().save(validarAtualizar(bancoHoras));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_BANCO_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, bancoHoras);
            return this.getTopPontoResponse().sucessoAtualizar(bancoHoras);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(BancoHoras.class), ex);
        }
    }

    @Override
    public Response excluir(Class<BancoHoras> c, Object id) throws ServiceException {
        try {
            validarExcluir(new BancoHoras((Integer) id));
            BancoHoras bancoHoras = buscar(BancoHoras.class, (Integer) id);
            //remove o vínculo com o percentual
            bancoHoras.setPercentuaisAcrescimo(null);
            //atualza a entidade sem o vínculo
            this.getDao().save(bancoHoras);
            //solicita a exclusão
            this.getDao().delete(bancoHoras);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_BANCO_HORAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, bancoHoras);
            return this.getTopPontoResponse().sucessoExcluir(bancoHoras.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new BancoHoras().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public BancoHoras validarSalvar(BancoHoras bancoHoras) throws ServiceException {

        validarJaCadastradoDescricao(bancoHoras);
        validarCamposObrigatorios(bancoHoras);

        return bancoHoras;
    }

    @Override
    public BancoHoras validarExcluir(BancoHoras bancoHoras) throws ServiceException {
        BancoHoras bancoHoras1 = buscar(BancoHoras.class, bancoHoras.getIdBancoHoras());
        if (!bancoHoras1.getFuncionarioBancoHorasList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.BANCOHORAS.ALERTA_EXCLUSAO_NAO_PERMITIDA.getResource()));
        }
        return bancoHoras1;
    }

    @Override
    public BancoHoras validarAtualizar(BancoHoras bancoHoras) throws ServiceException {

        validarIdentificador(bancoHoras);
        validarPossuiFechamento(bancoHoras);
        validarJaCadastradoDescricao(bancoHoras);
        validarCamposObrigatorios(bancoHoras);
        //verifica se o banco de horas ainda existe na base de dados
        buscar(BancoHoras.class, bancoHoras.getIdBancoHoras());

        return bancoHoras;
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="REGRAS DE VALIDAÇÕES">
    private void validarIdentificador(BancoHoras bancoHoras) throws ServiceException {
        if (bancoHoras.getIdBancoHoras() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad());
        }
    }

    private ServiceException error;

    public void validarPossuiFechamento(BancoHoras bancoHoras) throws ServiceException {
        this.error = null;
        LOGGER.debug("validando banco de horas possui funcionário com empresa e fechamento");
        List<FuncionarioBancoHoras> funcionarioBancoHorasList = this.funcionarioBancoHorasService.buscarPorBancoHoras(bancoHoras);

        if (funcionarioBancoHorasList != null) {
            LOGGER.debug("Total de {} funcionários banco de horas para validar", funcionarioBancoHorasList.size());
//            funcionarioBancoHorasList
//                    .parallelStream()
//                    .forEach(fbh -> {
//                        try {
//                            if (this.error == null) {
//                                Empresa empresa = this.empresaService.buscarPorFuncionario(fbh.getFuncionario());
//                                LOGGER.debug("validando banco de horas possui funcionario com fechamento. EMPRESA : {} - FUNCIONÁRIO : {} - DATA INÍCIO {} - DATA FIM {}", empresa.getRazaoSocial(), fbh.getFuncionario().getNome(), fbh.getDataInicio(), fbh.getDataFim());
//                                this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, fbh.getDataInicio(), fbh.getDataFim());
//                            }
//                        } catch (ServiceException ex) {
//                            this.error = ex;
//                        }
//                    });

            funcionarioBancoHorasList
                    .stream()
                    .collect(Collectors.groupingBy(p -> p.getFuncionario().getDepartamento().getEmpresa()))
                    .forEach((empresa, funcBancoHorasList) -> {
                        Date max = funcBancoHorasList.stream()
                                .map(fbh -> fbh.getDataInicio())
                                .reduce((a, b) -> a.compareTo(b) >= 0 ? a : b).get();
                        Date min = funcBancoHorasList.stream()
                                .map(fbh -> fbh.getDataInicio())
                                .reduce((a, b) -> a.compareTo(b) <= 0 ? a : b).get();
                        try {
                            this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, min, max);
                        } catch (ServiceException ex) {
                            this.error = ex;
                        }

                    });

        }
        if (this.error != null) {
            throw this.error;
        }
    }

    /**
     * Valida se já existe um BancoHoras com a descrição e em caso de
     * atualização verifica se está tentando alterar para uma descrição já
     * existente.
     *
     * @param bancoHoras
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public void validarJaCadastradoDescricao(BancoHoras bancoHoras) throws ServiceException {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(BancoHoras_.descricao.getName(), bancoHoras.getDescricao());
            List<BancoHoras> bancoHorasList = this.getDao().findbyAttributes(map, BancoHoras.class);
            if (bancoHorasList.size() > 0 && (!Objects.equals(bancoHoras.getIdBancoHoras(), bancoHorasList.get(0).getIdBancoHoras()))) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.BANCOHORAS.ALERTA_DESCRICAO_JA_CAD.getResource()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(BancoHoras.class), ex);
        }
    }

    /**
     * Valida os campos obrigatórios
     *
     * @param bancoHoras
     * @throws ServiceException
     */
    public void validarCamposObrigatorios(BancoHoras bancoHoras) throws ServiceException {
        if (bancoHoras.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(BancoHoras_.descricao.getName()));
        }
        if (Objects.equals(bancoHoras.getTipoLimiteDiario(), Boolean.FALSE) && bancoHoras.getPercentuaisAcrescimo() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.BANCOHORAS.ALERTA_MENSAL_PERCENTUAL.getResource()));
        }
    }

//</editor-fold>
}
