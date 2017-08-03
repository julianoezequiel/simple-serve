package com.topdata.toppontoweb.services.marcacoes;

import com.topdata.toppontoweb.dao.DaoException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.marcacoes.MarcacoesDao;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Cartao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoServices;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.services.funcionario.cartao.CartaoService;
import com.topdata.toppontoweb.utils.DateHelper;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class MarcacoesService extends TopPontoService<Marcacoes, Integer> {

    @Autowired
    private MarcacoesDao marcacoesDao;

    @Autowired
    private StatusMarcacaoService statusMarcacaoService;

    @Autowired
    private FechamentoServices fechamentoServices;

    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private CartaoService cartaoService;

    /**
     * Atualiza as marcacoes invalidas 1 para invalidas 0,
     *
     * @param empresa
     * @param dataInicioIn
     * @param dataFimIn
     */
    public void atualizarMarcacoesInvalidas(Empresa empresa, Date dataInicioIn, Date dataFimIn) {
        try {
            Date dataInicio = Utils.configuraHorarioData(dataInicioIn, 0, 0, 0);
            Date dataFim = Utils.configuraHorarioData(dataFimIn, 23, 59, 59);
            List<Funcionario> funcionarios = this.funcionarioService.buscarPorEmpresaPeriodoMarcacoesInvalidas(empresa, dataInicio, dataFim);
            if (funcionarios != null && !funcionarios.isEmpty()) {
                LOGGER.debug("Atualizando marcações invalidas");
                funcionarios.stream().forEach(funcionario -> {
                    try {
                        this.marcacoesDao.atualizarMarcacoesInvalidosPorPisPeriodo(funcionario, dataInicio, dataFim,empresa);
                    } catch (DaoException ex) {
                        LOGGER.error(this.getClass().getName(), ex);
                    }
                });
            }

            List<Funcionario> funcionariosCartoes = this.funcionarioService.buscarPorEmpresaPossuiCartao(empresa);

            if (funcionariosCartoes != null && !funcionariosCartoes.isEmpty()) {
                LOGGER.debug("Atualizando marcações invalidas");
                funcionariosCartoes.stream().forEach(funcionario -> {
                    try {
                        List<Cartao> cartaoList = this.cartaoService.buscarPorFuncionario(funcionario.getIdFuncionario());

                        cartaoList.stream().forEach(cartao -> {
                            try {
                                //valida o incio e fim do periodo do fechamento
                                Date dataInicioCartao = cartao.getDataInicio().getTime() > dataInicio.getTime() ? cartao.getDataInicio() : dataInicio;
                                Date dataFimCartao = this.cartaoService.buscarDataFimDoCartao(cartao);
                                dataFimCartao = dataFimCartao.getTime() < dataFim.getTime() ? dataFimCartao : dataFim;

                                this.atualizarMarcacoesInvalidasPorCartao(funcionario, cartao.getNumero(), dataInicioCartao, dataFimCartao, Boolean.FALSE);

                            } catch (ServiceException ex) {
                                Logger.getLogger(MarcacoesService.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });

                    } catch (ServiceException ex) {
                        LOGGER.error(this.getClass().getName(), ex);
                    }
                });
            }
        } catch (ServiceException ex) {
            Logger.getLogger(MarcacoesService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Marcacoes> buscarPorPeriodo(Date dataInicio, Date dataFim, Funcionario funcionario, boolean invalidas) throws ServiceException {
        try {
            return this.marcacoesDao.buscarPorPeriodo(dataInicio, dataFim, funcionario, invalidas);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * Verifica se a marcação é:
     * <b>Orignal: </b> não existe nenhuma marcação na base.
     * <b>Descosiderada: </b> já existe nenhuma marcação na base e foi
     * adicionada do mesmo arquivo (== importacao)
     * <b>Ignorada: </b> já existe nenhuma marcação na base e foi adicionada em
     * outro arquivo (!= importacao), nesse caso será sinalizada uma 'Exceção'
     *
     * @param numeroCartaoOriginal numero do cartao do arquivo
     * @param rep equipamento
     * @param empresa empresa selecionada no front
     * @param funcionario
     * @param dataHora data/hora da marcação no rep
     * @param importacao
     * @return o StatusMarcacao encontrado
     */
    public StatusMarcacao calcularStatusMarcacao(Date dataHora, String numeroCartaoOriginal, Rep rep, Funcionario funcionario, Empresa empresa, Importacao importacao) throws ServiceException {
        try {
            List<Marcacoes> marcacoesList;

            if (funcionario != null) {//Caso exista funcionario, busca por ele
                marcacoesList = this.marcacoesDao.buscarPorDataHoraECartaoOriginalFuncionario(numeroCartaoOriginal, rep, funcionario, dataHora);
            } else {//Caso contrário busca pela empresa
                marcacoesList = this.marcacoesDao.buscarPorDataHoraECartaoOriginalEmpresa(numeroCartaoOriginal, rep, empresa, dataHora);
            }

            if (marcacoesList.isEmpty()) {
                //é original
                return statusMarcacaoService.buscar(CONSTANTES.Enum_STATUS_MARCACAO.ORIGINAL);
            } else {
                Marcacoes marcacao = marcacoesList.get(0);
                if (marcacao.getImportacao() != null
                        && Objects.equals(marcacao.getImportacao().getIdImportacao(), importacao.getIdImportacao())) {
                    //é desconciderada
                    return statusMarcacaoService.buscar(CONSTANTES.Enum_STATUS_MARCACAO.DESCONSIDERADA);
                } else {
                    throw new ServiceException(this.getTopPontoResponse().erro(MSG.FERRAMENTAS.MARCACAO_JA_CADASTRADA.getResource()));
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(ex.getMessage()));
        }
    }

    /**
     * Verifica se a marcação é:
     * <b>Orignal: </b> não existe nenhuma marcação na base.
     * <b>Descosiderada: </b> já existe nenhuma marcação na base e foi
     * adicionada do mesmo arquivo (== importacao)
     * <b>Ignorada: </b> já existe nenhuma marcação na base e foi adicionada em
     * outro arquivo (!= importacao), nesse caso será sinalizada uma 'Exceção'
     *
     * @param nsr
     * @param pis numero do funcionario
     * @param rep equipamento
     * @param empresa empresa selecionada no front
     * @param dataHora data/hora da marcação no rep
     * @return o StatusMarcacao encontrado
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    public StatusMarcacao calcularStatusMarcacaoPorNSR(String nsr, Date dataHora, String pis, Rep rep, Empresa empresa) throws ServiceException {
        try {
            List<Marcacoes> marcacoesList = this.marcacoesDao.buscarPorDataHoraNSRPIS(nsr, pis, rep, empresa, dataHora);

            if (marcacoesList.isEmpty()) {
                //é original
                return statusMarcacaoService.buscar(CONSTANTES.Enum_STATUS_MARCACAO.ORIGINAL);
            } else {

                marcacoesList = marcacoesList.stream()
                        .filter(marcacao -> Objects.equals(marcacao.getRep().getIdRep(), rep.getIdRep()) && nsr.equals(marcacao.getNsr()))
                        .collect(Collectors.toList());
                if (marcacoesList.isEmpty()) {
                    //é desconciderada
                    return statusMarcacaoService.buscar(CONSTANTES.Enum_STATUS_MARCACAO.DESCONSIDERADA);
                } else {
                    throw new ServiceException(this.getTopPontoResponse().erro(MSG.FERRAMENTAS.MARCACAO_JA_CADASTRADA.getResource()));
                }
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(ex.getMessage()));
        }
    }

    @Override
    public Response salvar(Marcacoes marcacoes) throws ServiceException {
        Marcacoes marcacaoSalva;
        try {
            marcacaoSalva = (Marcacoes) getDao().save(marcacoes);
            return getTopPontoResponse().sucessoSalvar("Marcação", marcacaoSalva);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(ex.getMessage()));
        }

    }

    public int removerTodosPelaImportacao(Importacao importacao) throws ServiceException {
        try {
            return marcacoesDao.deletarAllPelaImportacao(importacao);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(ex.getMessage()));
        }
    }

    public void atualizarMarcacoesInvalidasPorPis(Funcionario f) throws ServiceException {
        try {
            Empresa empresa = empresaService.buscarPorFuncionario(f);
            
            Date dataInicio = f.getDataAdmissao();
            Date dataFim = f.getDataDemissao() != null ? f.getDataDemissao() : DateHelper.Max();

            dataInicio = Utils.configuraHorarioData(dataInicio, 0, 0, 0);
            dataFim = Utils.configuraHorarioData(dataFim, 23, 59, 59);

            //Busca todas as marcacoes validas
            List<Marcacoes> marcacoesList = this.marcacoesDao.buscarMarcacoesPorPisEmpresa(f.getPis(), empresa, true, dataInicio, dataFim);

            atualizarMarcacoesInvalidas(marcacoesList, f, empresa, Boolean.TRUE);

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Funcionario().toString()), ex);
        }
    }

    public void atualizarMarcacoesInvalidas(List<Marcacoes> marcacoesList, Funcionario funcionario, Empresa empresa, Boolean validarFechamento) throws ServiceException {
        marcacoesList.stream().forEach(marcacao -> {
            try {
                //Caso não exista fechamento na data
                if (!validarFechamento || !this.fechamentoServices.isExisteFechamento(empresa, marcacao.getDataHora())) {
                    //Vincula funcionario
                    marcacao.setIdFuncionario(funcionario);
                    marcacao.setInvalido(Boolean.FALSE);
                    //salva a marcação
                    marcacoesDao.save(marcacao);
                    LOGGER.debug("Incluida marcacao " + marcacao.getDataHora() + " [id:" + marcacao.getIdMarcacao() + "] para o funcionário " + funcionario.getNome() + " [" + funcionario.getPis() + "].");
                } else {
                    LOGGER.debug("Não incluida marcacao " + marcacao.getDataHora() + " [id:" + marcacao.getIdMarcacao() + "], pois existe um fechamento na empresa " + empresa.getRazaoSocial());
                }
            } catch (DaoException | ServiceException ex) {
                Logger.getLogger(MarcacoesService.class.getName()).log(Level.SEVERE, null, ex);
                LOGGER.debug("Problema ao salvar a marcacao " + marcacao.getIdMarcacao() + " - [" + marcacao.getDataHora().toString() + "]");
                ex.printStackTrace();
            }
        });
    }

    public void atualizarMarcacoesInvalidasPorCartao(Funcionario funcionario, String numeroCartao, Date dataInicio, Date dataFim) throws ServiceException {
        atualizarMarcacoesInvalidasPorCartao(funcionario, numeroCartao, dataInicio, dataFim, Boolean.TRUE);
    }

    public void atualizarMarcacoesInvalidasPorCartao(Funcionario funcionario, String numeroCartao, Date dataInicio, Date dataFim, Boolean validarFechamento) throws ServiceException {
        try {
            Empresa empresa = empresaService.buscarPorFuncionario(funcionario);
            
            dataInicio = Utils.configuraHorarioData(dataInicio, 0, 0, 0);
            dataFim = Utils.configuraHorarioData(dataFim, 23, 59, 59);

            //Busca todas as marcacoes validas
            List<Marcacoes> marcacoesList = this.marcacoesDao.buscarMarcacoesPorCartaoEmpresa(numeroCartao, empresa, true, dataInicio, dataFim);

            atualizarMarcacoesInvalidas(marcacoesList, funcionario, empresa, validarFechamento);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Funcionario().toString()), ex);
        }
    }

    public Long buscarQuantidadePorRep(Rep rep) throws ServiceException {
        try {

            return this.marcacoesDao.buscarQuantidadePorRep(rep);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Funcionario().toString()), ex);
        }
    }

}
