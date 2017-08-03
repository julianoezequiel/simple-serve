package com.topdata.toppontoweb.services.configuracoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.configuracoes.percentuais.PercentuaisAcrescimoDao;
import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo_;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.bancoHoras.BancoHorasServices;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 19/07/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class PercentuaisAcrescimoServices extends TopPontoService<PercentuaisAcrescimo, Object>
        implements ValidacoesCadastro<PercentuaisAcrescimo, Object> {

    @Autowired
    PercentuaisAcrescimoDao acrescimoDao;
    @Autowired
    SequenciaPercentuaisServices sequenciaPercentuaisServices;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private BancoHorasServices bancoHorasServices;
    @Autowired
    private FuncionarioJornadaService funcionarioJornadaService;

    private ServiceException error;
    private HashMap<String, Object> map;

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public PercentuaisAcrescimo buscar(Class<PercentuaisAcrescimo> entidade, Object id) throws ServiceException {
        try {
            PercentuaisAcrescimo p = (PercentuaisAcrescimo) this.getDao().find(PercentuaisAcrescimo.class, (Integer) id);
            if (p == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new PercentuaisAcrescimo().toString()));
            }
            return p;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public PercentuaisAcrescimo buscarPorBancoHoras(Integer idBancoHoras) throws ServiceException {
        try {
            PercentuaisAcrescimo percAcr = acrescimoDao.findByBancoHoras(idBancoHoras);
            if (percAcr != null) {
                percAcr.setSequenciaPercentuaisList(sequenciaPercentuaisServices.buscaPorPercentuaisAcrescimo(percAcr.getIdPercentuaisAcrescimo()));
            }

            return percAcr;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        try {
            percentuaisAcrescimo = validarSalvar(percentuaisAcrescimo);
            percentuaisAcrescimo = (PercentuaisAcrescimo) this.getDao().save(percentuaisAcrescimo);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, percentuaisAcrescimo);
            return this.getTopPontoResponse().sucessoSalvar(percentuaisAcrescimo.toString(), percentuaisAcrescimo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new PercentuaisAcrescimo().toString()), ex);
        }
    }

    public void validarPossuiFechamento(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        try {
            error = null;
            LOGGER.debug("validação possui fechamento percentuais de acréscimo");
            List<BancoHoras> bancoHorasList;
            List<Jornada> jornadaList;
            //busca o registro original
            PercentuaisAcrescimo percentuaisAcrescimoOriginal = this.acrescimoDao.find(PercentuaisAcrescimo.class, percentuaisAcrescimo.getIdPercentuaisAcrescimo());
            //lista todos bancos que utilizam este percentual
            if (!percentuaisAcrescimoOriginal.getBancoHorasList().isEmpty()) {
                LOGGER.debug("validação possui fechamento percentuais de acréscimo banco de horas");
                bancoHorasList = percentuaisAcrescimoOriginal.getBancoHorasList().stream().distinct().collect(Collectors.toList());
                bancoHorasList.parallelStream().forEach(bh -> {
                    try {
                        bancoHorasServices.validarPossuiFechamento(bh);
                    } catch (ServiceException ex) {
                        error = ex;
                    }
                });
            }

            //lista todos as joranadas que utilizam este percentual
            if (error == null && !percentuaisAcrescimoOriginal.getJornadaList().isEmpty()) {
                LOGGER.debug("validação possui fechamento percentuais de acréscimo jornada");
                jornadaList = percentuaisAcrescimoOriginal.getJornadaList().stream().distinct().collect(Collectors.toList());
                List<FuncionarioJornada> funcionarioJornadaList = new ArrayList<>();

                jornadaList.parallelStream().forEach(jf -> {
                    try {
                        funcionarioJornadaList.addAll(funcionarioJornadaService.buscarPorJornada(jf));
                    } catch (ServiceException ex) {
                        LOGGER.error(ex.getMessage());
                    }
                });
                LOGGER.debug("Total de {} funcionário jornadas para validar", funcionarioJornadaList.size());
                funcionarioJornadaList.stream().forEach(fj -> {
                    try {
                        funcionarioJornadaService.validarPossuiFechamento(fj);
                    } catch (ServiceException ex) {
                        error = ex;
                    }
                });
            }

            if (error != null) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FECHAMENTO.ALERTA_EXISTE_FECHAMENTO_PERIODO_CADASTRO.getResource()));
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new PercentuaisAcrescimo().toString()), ex);
        }
    }

    @Override
    public Response atualizar(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        try {
            percentuaisAcrescimo = validarAtualizar(percentuaisAcrescimo);
            validarPossuiFechamento(percentuaisAcrescimo);
            percentuaisAcrescimo = (PercentuaisAcrescimo) this.getDao().save(percentuaisAcrescimo);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, percentuaisAcrescimo);
            return this.getTopPontoResponse().sucessoAtualizar(percentuaisAcrescimo.toString(), percentuaisAcrescimo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(new PercentuaisAcrescimo().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<PercentuaisAcrescimo> c, Object id) throws ServiceException {
        PercentuaisAcrescimo percentuaisAcrescimo = buscar(PercentuaisAcrescimo.class, (Integer) id);
        if (percentuaisAcrescimo != null) {
            if (!percentuaisAcrescimo.getBancoHorasList().isEmpty() || !percentuaisAcrescimo.getJornadaList().isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.PERCENTUAIS_ACRESCIMO.ALERTA_EXISTE_VINCULO.getResource()));
            }
            try {
                this.getDao().delete(percentuaisAcrescimo);
                this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, percentuaisAcrescimo);
                percentuaisAcrescimo.getSequenciaPercentuaisList().stream().forEach(s -> {
                    try {
                        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, s);
                    } catch (ServiceException ex) {
                        Logger.getLogger(PercentuaisAcrescimoServices.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return this.getTopPontoResponse().sucessoExcluir(percentuaisAcrescimo.toString());
            } catch (DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erroExcluir(new PercentuaisAcrescimo().toString()), ex);
            }
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new PercentuaisAcrescimo().toString()));
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public PercentuaisAcrescimo validarSalvar(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        try {
            //valida descrição
            if (percentuaisAcrescimo.getDescricao().equals("") || percentuaisAcrescimo.getDescricao() == null) {
                throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(PercentuaisAcrescimo_.descricao.getName()));
            }
            // valida se já existe um percentual com esta descrição
            map = new HashMap<>();
            map.put(PercentuaisAcrescimo_.descricao.getName(), percentuaisAcrescimo.getDescricao());
            if (!this.getDao().findbyAttributes(map, PercentuaisAcrescimo.class).isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaJaCad(new PercentuaisAcrescimo().toString()));
            }
            return percentuaisAcrescimo;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new PercentuaisAcrescimo().toString()), ex);
        }
    }

    @Override
    public PercentuaisAcrescimo validarExcluir(PercentuaisAcrescimo t) throws ServiceException {
        return t;
    }

    @Override
    public PercentuaisAcrescimo validarAtualizar(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        validarIdentificador(percentuaisAcrescimo);
        atualizarValoresEntreEntidades(percentuaisAcrescimo);
        validarDescricaoJaCadastrada(percentuaisAcrescimo);
        return percentuaisAcrescimo;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    /**
     * Realiza a cópia dos percentuais passado na lista
     *
     * @param listaPercentuais
     * @return
     * @throws ServiceException
     */
    public Response copiar(List<PercentuaisAcrescimo> listaPercentuais) throws ServiceException {
        //limite de 5 itens para cópia
        if (listaPercentuais.size() > 5) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.PERCENTUAIS_ACRESCIMO.ALERTA_QTD_MAXIMA_COPIA.getResource()));
        }

        // realiza a cópia de um por vez
        for (PercentuaisAcrescimo p : listaPercentuais) {
            try {
                //valida campo obrigatório para buscar na base de dados
                if (p.getIdPercentuaisAcrescimo() == null) {
                    throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(p.toString()));
                }
                //busca a entidade na base de dados
                p = buscar(PercentuaisAcrescimo.class, p.getIdPercentuaisAcrescimo());

                //realiza uma cópia da entidade
                PercentuaisAcrescimo pa = (PercentuaisAcrescimo) p.clone();
                //limpa os valores que não serão copiados
                pa.setSequenciaPercentuaisList(null);
                pa.setIdPercentuaisAcrescimo(null);
                pa.setBancoHorasList(null);
                pa.setJornadaList(null);

                Integer count = 1;
                String descricao = pa.getDescricao();
                pa.setDescricao(descricao + " " + "(Cópia) " + count);

                //valida a descrição do percentual , até que possua um nome válido
                while (isCopiaValida(pa)) {
                    count++;
                    pa.setDescricao(descricao + " " + "(Cópia) " + count);
                }

                //tamanho máximo da descrição 50 caracter
                if (pa.getDescricao().length() > 50) {
                    pa.setDescricao(pa.getDescricao().substring(0, 49));
                }

                //persiste a entidade de percentual na base de dados e recebe a entidae com o id gerado pela base de dados
                pa = (PercentuaisAcrescimo) this.getDao().save(pa);

                if (p.getSequenciaPercentuaisList() != null && !p.getSequenciaPercentuaisList().isEmpty()) {

                    //copias as novas entidades sequências
                    for (SequenciaPercentuais s : p.getSequenciaPercentuaisList()) {
                        SequenciaPercentuais sp = new SequenciaPercentuais();
                        sp.setIdSequencia(s.getSequenciaPercentuaisPK().getIdSequencia());
                        sp.setAcrescimo(s.getAcrescimo());
                        sp.setHoras(s.getHoras());
                        sp.setPercentuaisAcrescimo(pa);
                        sp.setTipoDia(s.getTipoDia());
                        //persiste na base de dados a nova sequência
                        this.getDao().save(sp);
                    }
                }

            } catch (CloneNotSupportedException | DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erroSalvar(new PercentuaisAcrescimo().toString()));
            }
        }

        return this.getTopPontoResponse().sucessoSalvar(new PercentuaisAcrescimo().toString());
    }

    /**
     * Valida a descrição da cópia da jornada
     *
     * @param j
     * @return
     */
    private Boolean isCopiaValida(PercentuaisAcrescimo p) throws DaoException {
        map = new HashMap<>();
        map.put(PercentuaisAcrescimo_.descricao.getName(), p.getDescricao());
        return !this.getDao().findbyAttributes(map, PercentuaisAcrescimo.class).isEmpty();
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    /**
     * valida se o percentual posui um id
     *
     * @param percentuaisAcrescimo
     * @throws ServiceException
     */
    public void validarIdentificador(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        if (percentuaisAcrescimo == null || percentuaisAcrescimo.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new PercentuaisAcrescimo().toString()));
        }
    }

    /**
     * Atualiza a entidade recebida do front-end com os valores exsitentes na
     * base de dados
     *
     * @param percentuaisAcrescimo
     * @throws ServiceException
     */
    public void atualizarValoresEntreEntidades(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {
        PercentuaisAcrescimo d = buscar(PercentuaisAcrescimo.class, percentuaisAcrescimo.getIdPercentuaisAcrescimo());
        percentuaisAcrescimo.setDescricao(percentuaisAcrescimo.getDescricao() == null ? d.getDescricao() : percentuaisAcrescimo.getDescricao());
        percentuaisAcrescimo.setSequenciaPercentuaisList(percentuaisAcrescimo.getSequenciaPercentuaisList().size() > 0 ? percentuaisAcrescimo.getSequenciaPercentuaisList() : d.getSequenciaPercentuaisList());
    }

    /**
     * Valida se já existe um percentual com esta desrição
     *
     * @param percentuaisAcrescimo
     * @throws ServiceException
     */
    public void validarDescricaoJaCadastrada(PercentuaisAcrescimo percentuaisAcrescimo) throws ServiceException {

        try {
            // busca o PercentuaisAcrescimo pela descrição
            map = new HashMap<>();
            map.put(PercentuaisAcrescimo_.descricao.getName(), percentuaisAcrescimo.getDescricao());
            List<PercentuaisAcrescimo> dpts;
            if (!(dpts = this.getDao().findbyAttributes(map, PercentuaisAcrescimo.class)).isEmpty()) {

                PercentuaisAcrescimo e = buscar(PercentuaisAcrescimo.class, percentuaisAcrescimo.getIdPercentuaisAcrescimo());

                if (e != null && !dpts.get(0).getIdPercentuaisAcrescimo().equals(percentuaisAcrescimo.getIdPercentuaisAcrescimo())) {
                    throw new ServiceException(this.getTopPontoResponse().alertaJaCad(percentuaisAcrescimo.toString()));
                }
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new PercentuaisAcrescimo().toString()), ex);
        }
    }
//</editor-fold>
}
