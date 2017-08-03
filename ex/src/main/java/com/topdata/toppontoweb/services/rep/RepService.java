package com.topdata.toppontoweb.services.rep;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.rep.RepDao;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.rep.ModeloRep;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.entity.rep.Rep_;
import com.topdata.toppontoweb.entity.tipo.TipoEquipamento;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.marcacoes.MarcacoesService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * Esta classe realiza as regras de negócio para o CRUD do REP.
 *
 * @see Rep
 * @see TopPontoService
 *
 * @version 1.0.0.0 data 19/01/2017
 * @since 1.0.0.0 data 22/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class RepService extends TopPontoService<Rep, Object>
        implements ValidacoesCadastro<Rep, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private EmpresaService empresaService;

    @Autowired
    private RepDao repDao;
    
    @Autowired
    private MarcacoesService marcacoesService;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
    private List<Rep> repList;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Busca os Rep pelo id da Empresa
     *
     * @param empresa
     * @return
     * @throws ServiceException
     */
    public List<Rep> buscarPorEmpresa(Empresa empresa) throws ServiceException {
        try {
            this.map = new HashMap<>();
            this.map.put(Rep_.empresa.getName(), empresa);
            return this.getDao().findbyAttributes(this.map, Rep.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Rep.class), ex);
        }
    }

    /**
     * Busca os modelos disponiveis para o Rep
     *
     * @return
     * @throws ServiceException
     */
    public List<ModeloRep> buscarModelosRep() throws ServiceException {
        try {
            return this.getDao().findAll(ModeloRep.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, ModeloRep.class), ex);
        }
    }

    /**
     * Busca os tipos de equipamentos disponÃ­veis
     *
     * @return
     * @throws ServiceException
     */
    public List<TipoEquipamento> buscarTiposEquimento() throws ServiceException {
        try {
            return this.getDao().findAll(TipoEquipamento.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, TipoEquipamento.class), ex);
        }
    }

    /**
     * Busca os REP pelo grupo de acesso. O grupo de acesso controla os
     * departamentos. Caso a lista de departamento do grupo de acesso esteja
     * vazio, o grupo possui acesso a todas os REP
     *
     * @param id
     * @return
     * @throws ServiceException
     */
    public List<Rep> buscaPorGrupoAcesso(Integer id) throws ServiceException {
        repList = new ArrayList<>();

        empresaService.buscaPorGrupoPermissaoAtivas(id).forEach(empresa -> {
            repList.addAll(empresa.getRepList());
        });

        //retorna a lista de Rep em formato JSON
        return repList;

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Rep buscar(Class<Rep> entidade, Object id) throws ServiceException {
        try {
            Rep rep = (Rep) this.getDao().find(Rep.class, id);
            if (rep == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Rep().toString()));
            }
            
            //Verifica se existe marcacoes
            Long quantidadeMarcacoes = marcacoesService.buscarQuantidadePorRep(rep);
            rep.setExisteMarcacoes(quantidadeMarcacoes > 0);
            
            return rep;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }

    }

    @Override
    public Response atualizar(Rep rep) throws ServiceException {
        try {
            this.getDao().save(validarAtualizar(rep));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_REP, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, rep);

            return this.getTopPontoResponse().sucessoAtualizar(Rep.class
            );
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Rep.class
            ));
        }
    }

    public void atualizarNSR(Integer idRep, String nsr) throws ServiceException {
        try {
            repDao.atualizarNSR(idRep, nsr);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(Rep.class
            ));
        }
    }

    @Override
    public Response salvar(Rep rep) throws ServiceException {
        try {
            this.getDao().save(validarSalvar(rep));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_REP, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, rep);

            return this.getTopPontoResponse().sucessoSalvar(Rep.class
            );
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(Rep.class
            ));
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Rep validarSalvar(Rep rep) throws ServiceException {
        validarNumeroRep(rep);
        validarNumeroSerie(rep);
        return rep;
    }

    @Override
    public Response excluir(Class<Rep> c, Object id) throws ServiceException {
        try {
            Rep rep = validarExcluir(new Rep((Integer) id));
            this.getDao().delete(rep);

            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_REP, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, rep);
            return this.getTopPontoResponse().sucessoExcluir(rep.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Rep().toString()), ex);
        }
    }

    @Override
    public Rep validarExcluir(Rep rep) throws ServiceException {
        rep = buscar(Rep.class, rep.getIdRep());
        
        Long qntMarcacoes = marcacoesService.buscarQuantidadePorRep(rep);

        if (qntMarcacoes <= 0) {
            return rep;
        }

        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_EXCLUIR_POSSUI_MARCACAO.getResource()));
    }

    @Override
    public Rep validarAtualizar(Rep rep) throws ServiceException {

        //Realiza as demais validacoes
        validarNaoCadastrado(rep);
        validarNumeroRep(rep);
        validarNumeroSerie(rep);

        validarNumeroFabricante(rep);

        return rep;

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    private void validarNaoCadastrado(Rep rep) throws ServiceException {
        try {
            Rep r = (Rep) getDao().find(Rep.class, rep.getIdRep());
            if (Objects.isNull(r)) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Rep().toString()));
            }
        } catch (DaoException ex) {
            Logger.getLogger(RepService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Valida o numero de serie do Rep e verifica o tipo de equipamento.
     *
     * @param rep
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     * @since 1.0.4 data 19/06/2016
     */
    private Rep validarNumeroSerie(Rep rep) throws ServiceException {
        try {
            //Altera a precisao do numero REP
            int precisao = 7;
            if(rep.getTipoEquipamento() != null 
                    && rep.getTipoEquipamento().getIdTipoEquipamento() == CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.ordinal()){
               precisao = 6; 
            }
            
            rep.setNumeroSerie(Utils.corrigePrecisaoNumero(rep.getNumeroSerie(), precisao));
            
            //caso o numero de serie seja igual a 0 e o equipamento nao for de outro fabricante sera inva¡lido o numero de serie
            if (Integer.parseInt(rep.getNumeroSerie()) == 0
                    && (!rep.getTipoEquipamento().getDescricao().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.getDescricao())
                    && !rep.getTipoEquipamento().getDescricao().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.getDescricao()))) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_NUM_SERIE_INVALIDO.getResource()));
            } else if (!rep.getTipoEquipamento().getDescricao().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.getDescricao())) {
                //realiza a consulta na base de dados pelo numero de serie
                Rep repEncontrado = repDao.buscarPorSerieCompletaEmpresa(rep.getEmpresa(), rep.getNumeroFabricante(), rep.getNumeroModelo(), rep.getNumeroSerie());
                
                //caso exista um Rep com este numero sera validado se e uma atualizacao e se esta sendo alterado o id
                if (repEncontrado != null && !Objects.equals(rep.getIdRep(), repEncontrado.getIdRep()) ) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_NUM_SERIE_JA_CAD.getResource()));
                }
            }
            return rep;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Rep.class), ex);
        }
    }

    private void validarNumeroFabricante(Rep rep) throws ServiceException {

        if (Integer.parseInt(rep.getNumeroFabricante()) == 0
                && !(rep.getTipoEquipamento().getIdTipoEquipamento().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.ordinal())
                || rep.getTipoEquipamento().getIdTipoEquipamento().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.ordinal()))) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_NUM_FABRICANTE_INVALIDO.getResource()));
        }
        if (rep.getModeloRep() != null && rep.getModeloRep().getIdModeloRep().equals(12)
                && !rep.getTipoEquipamento().getIdTipoEquipamento().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.ordinal())) {

            if (Integer.parseInt(rep.getModeloRep().getNumeroModelo()) == 0) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_CODIGO_MODELO_INVALIDO.getResource()));
            }
        }
        rep.setNumeroModelo(rep.getModeloRep().getNumeroModelo());
    }

    /**
     * Valida o numero do Rep.
     *
     * @param rep
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     * @since 1.0.4 data 29/06/2016
     */
    private Rep validarNumeroRep(Rep rep) throws ServiceException {
        try {
            //somente se o rep for de outro fabricante sera validado o numero do Rep
            if (rep.getTipoEquipamento().getDescricao().equals(CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.getDescricao())) {
                //realiza a consulta na base de dados pelo numero do Rep
                List<Rep> repEncontradoList = repDao.buscarDiferentesPorNumeroRep(rep);
                //caso exista um Rep com este numero sera validado se e uma atualizacao e se esta sendo alterado o id
                if (!repEncontradoList.isEmpty()) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.REP.ALERTA_NUM_REP_JA_CAD.getResource()));
                }
            } else {
                //Remove o numero rep
                rep.setNumeroRep(null);
            }

            return rep;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Rep.class), ex);
        }
    }
//</editor-fold>

    public Rep buscarPorNumeroRep(Integer numeroRep, Empresa empresa) throws ServiceException {
        try {
            Rep rep = repDao.buscarPorNumeroRep(numeroRep, empresa);
            if (rep != null) {
                return rep;
            } else {
                throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Rep.class));
            }

        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Rep.class), ex);
        }
    }

    public Rep buscarPorSerieCompletaEmpresa(Empresa empresa, String numeroFabricante, String numeroModelo, String numeroSerie) throws ServiceException {
        try {
            return repDao.buscarPorSerieCompletaEmpresa(empresa, numeroFabricante, numeroModelo, numeroSerie);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Rep.class), ex);
        }
    }

}
