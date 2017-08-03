package com.topdata.toppontoweb.services.funcionario.calendario;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import com.topdata.toppontoweb.services.funcionario.afastamento.AfastamentoService;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioCalendarioDao;
import com.topdata.toppontoweb.dto.ColetivoResultado;
import com.topdata.toppontoweb.dto.MsgRetorno;
import com.topdata.toppontoweb.dto.ProgressoTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.calendario.CalendarioFeriadoServices;
import com.topdata.toppontoweb.services.coletivo.ColetivoService;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.fechamento.FechamentoException;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.stream.Collectors;
//</editor-fold>

/**
 * Classe realiza as regras de negócio para o Funcionario Calendario
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 29/09/2016
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioCalendarioService extends TopPontoService<FuncionarioCalendario, Object>
        implements ValidacoesCadastro<FuncionarioCalendario, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private FuncionarioCalendarioDao funcionarioCalendarioDao;
    @Autowired
    private ColetivoService coletivoService;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private CalendarioFeriadoServices calendarioFeriadoServices;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    HashMap<String, Object> map = new HashMap<>();
//</editor-fold>

    //<editor-fold defaultstate="" desc="CONSULTAS">
    private FuncionarioCalendario buscarPorDataInicio(FuncionarioCalendario funcionarioCalendario) throws DaoException {
        List<FuncionarioCalendario> funcionarioCalendarioList = this.funcionarioCalendarioDao.buscarPorFuncionarioEDataInicio(funcionarioCalendario);
        return !funcionarioCalendarioList.isEmpty() ? funcionarioCalendarioList.iterator().next() : null;
    }

    public List<FuncionarioCalendario> buscarPorFuncionario(Integer id) throws ServiceException {
        Funcionario funcionario = funcionarioService.buscar(Funcionario.class, id);
        return funcionario.getFuncionarioCalendarioList();
    }

    public List<FuncionarioCalendario> buscarPorColetivo(Coletivo coletivo) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(FuncionarioCalendario_.coletivo.getName(), coletivo);
            return funcionarioCalendarioDao.findbyAttributes(map, FuncionarioCalendario.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<FuncionarioCalendario> buscarPorFuncionario(Funcionario funcionario) throws ServiceException {
        try {
            return this.funcionarioCalendarioDao.buscarPorFuncionario(funcionario);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="" desc="CRUD">
    @Override
    public FuncionarioCalendario buscar(Class<FuncionarioCalendario> entidade, Object id) throws ServiceException {
        try {
            FuncionarioCalendario fc = (FuncionarioCalendario) funcionarioCalendarioDao.find(FuncionarioCalendario.class, id);
            if (fc == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new FuncionarioCalendario().toString()));
            }
            return fc;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
    
    public FuncionarioCalendario buscarPorIdFuncionarioCalendario(Integer idFuncionarioCalendario) throws ServiceException {
        try {
            FuncionarioCalendario fc = funcionarioCalendarioDao.buscarPorFuncionarioCalendario(idFuncionarioCalendario);
            if (fc == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new FuncionarioCalendario().toString()));
            }
            return fc;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(FuncionarioCalendario entidade) throws ServiceException {
        try {
            entidade = (FuncionarioCalendario) funcionarioCalendarioDao.save(validarSalvar(entidade));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, entidade);
            return this.getTopPontoResponse().sucessoSalvar(entidade.toString(), entidade);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(entidade.toString()), ex);
        }
    }

    @Override
    public Response atualizar(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        try {
            Coletivo coletivo = funcionarioCalendario.getColetivo();
            funcionarioCalendario = (FuncionarioCalendario) this.funcionarioCalendarioDao.save(this.validarAtualizar(funcionarioCalendario));
            this.coletivoService.excluirColetivoSemVinculo(coletivo);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, funcionarioCalendario);
            return this.getTopPontoResponse().sucessoAtualizar(funcionarioCalendario.toString(), funcionarioCalendario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(funcionarioCalendario.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<FuncionarioCalendario> c, Object id) throws ServiceException {
        FuncionarioCalendario funcionarioCalendario = this.validarExcluir(new FuncionarioCalendario((Integer) id));
        Coletivo coletivo = funcionarioCalendario.getColetivo();
        this.excluirEntidade(funcionarioCalendario, coletivo != null);

//        if (coletivo != null) {
//            this.coletivoService.excluirColetivoSemVinculo(coletivo);
//        }

//        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, funcionarioCalendario);
        return this.getTopPontoResponse().sucessoExcluir(funcionarioCalendario.toString());
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public FuncionarioCalendario validarSalvar(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        validarPossuiFechamento(funcionarioCalendario);
        validarDataInicio(funcionarioCalendario);
        validarDataAnterioAdmissao(funcionarioCalendario);
        validarDataPosterioDemissao(funcionarioCalendario);
        return funcionarioCalendario;
    }

    @Override
    public FuncionarioCalendario validarExcluir(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        funcionarioCalendario = buscarPorIdFuncionarioCalendario(funcionarioCalendario.getIdFuncionarioCalendario());
        validarIdentificador(funcionarioCalendario);
        validarPossuiFechamento(funcionarioCalendario);
        
        return funcionarioCalendario;
    }

    @Override
    public FuncionarioCalendario validarAtualizar(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        validarIdentificador(funcionarioCalendario);
        validarPossuiFechamento(funcionarioCalendario);
        validarDataInicio(funcionarioCalendario);
        validarDataAnterioAdmissao(funcionarioCalendario);
        validarDataPosterioDemissao(funcionarioCalendario);
        validarAlteracaoColetivo(funcionarioCalendario);
        return funcionarioCalendario;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÃO">
    private void validarPossuiFechamento(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        Funcionario funcionario = this.funcionarioService.buscar(Funcionario.class, funcionarioCalendario.getFuncionario());
        Empresa empresa = this.empresaService.buscarPorFuncionario(funcionario);

        //se for uma atualização ou exclusão valida o registro original
        if (funcionarioCalendario.getIdFuncionarioCalendario() != null) {
            FuncionarioCalendario funcionarioCalendarioOriginal = this.buscar(FuncionarioCalendario.class, funcionarioCalendario.getIdFuncionarioCalendario());
            List<CalendarioFeriado> calendarioFeriadosOriginal = this.calendarioFeriadoServices.buscarPorCalendario(funcionarioCalendarioOriginal.getCalendario().getIdCalendario());
            for (CalendarioFeriado cf : calendarioFeriadosOriginal) {
                this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, cf.getDataInicio(), cf.getDataInicio());
            }
        }

        List<CalendarioFeriado> calendarioFeriados = this.calendarioFeriadoServices.buscarPorCalendario(funcionarioCalendario.getCalendario().getIdCalendario());
        for (CalendarioFeriado cf : calendarioFeriados) {
            this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, cf.getDataInicio(), cf.getDataInicio());
        }
    }

    private class PredicateCompararDatasInicio implements Predicate<CalendarioFeriado> {

        private FuncionarioCalendario funcionarioCalendario;

        public PredicateCompararDatasInicio(FuncionarioCalendario funcionarioCalendario) {
            this.funcionarioCalendario = funcionarioCalendario;
        }

        @Override
        public boolean test(CalendarioFeriado c) {
            return c.getDataInicio().compareTo(this.funcionarioCalendario.getDataInicio()) >= 0;
        }
    }

    public FuncionarioCalendario validarAlteracaoColetivo(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        FuncionarioCalendario fc = buscar(FuncionarioCalendario.class, funcionarioCalendario.getIdFuncionarioCalendario());
        funcionarioCalendario.setColetivo(fc.getColetivo() != null ? fc.getColetivo() : null);
        if (Objects.equals(funcionarioCalendario.getIdFuncionarioCalendario(), fc.getIdFuncionarioCalendario())
                && (!Objects.equals(funcionarioCalendario.getCalendario(), fc.getCalendario())
                || !Objects.equals(funcionarioCalendario.getDataInicio(), fc.getDataInicio()))
                && Objects.equals(funcionarioCalendario.getManual(), Boolean.TRUE)) {
            funcionarioCalendario.setColetivo(null);
        }
        return funcionarioCalendario;
    }

    public void validarIdentificador(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        if (funcionarioCalendario.getIdFuncionarioCalendario() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(funcionarioCalendario.toString()));
        }
    }

    public void validarDataInicio(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        try {
            FuncionarioCalendario fc = buscarPorDataInicio(funcionarioCalendario);
            if (Objects.nonNull(fc) && !Objects.equals(fc.getIdFuncionarioCalendario(), funcionarioCalendario.getIdFuncionarioCalendario())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FUNCIONARIO_CALENDARIO.ALERTA_DATA_INICIAL_JA_CADASTRADA.getResource()));
            }
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(funcionarioCalendario.toString()), ex);
        }
    }

    public void validarDataAnterioAdmissao(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        funcionarioCalendario.setFuncionario(funcionarioService.buscar(Funcionario.class, funcionarioCalendario.getFuncionario().getIdFuncionario()));
        if (Objects.nonNull(funcionarioCalendario.getFuncionario().getDataAdmissao())) {
            if (funcionarioCalendario.getDataInicio().before(funcionarioCalendario.getFuncionario().getDataAdmissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_ANTERIOR_ADMISSAO.getResource(), MSG.FUNCIONARIO_CALENDARIO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }

    public void validarDataPosterioDemissao(FuncionarioCalendario funcionarioCalendario) throws ServiceException {
        if (Objects.nonNull(funcionarioCalendario.getFuncionario().getDataDemissao())) {
            if (funcionarioCalendario.getDataInicio().after(funcionarioCalendario.getFuncionario().getDataDemissao())) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacaoComGenero(MSG.FUNCIONARIO.ALERTA_DATA_INICIAL_POSTERIOR_DEMISSAO.getResource(), MSG.FUNCIONARIO_CALENDARIO.ALERTA_GENERO_ENTIDADE.getResource()));
            }
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COLETIVO">
    /**
     * Método para exclusão de uma lista de calendario, este método é utilizado
     * pelo lançamento coletivo
     *
     * @since 13/01/2013
     * @param funcionarioCalendarioList
     * @param coletivo
     * @param removerColetivo
     * @return
     * @throws ServiceException
     */
    public List<ColetivoResultado> removerColetivo(List<FuncionarioCalendario> funcionarioCalendarioList, Coletivo coletivo, boolean removerColetivo) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        funcionarioCalendarioList.stream().forEach((FuncionarioCalendario funcCalendario) -> {
            try {

                if (funcCalendario.getColetivo() != null) {
                    funcCalendario.setColetivo(null);
                    funcCalendario = (FuncionarioCalendario) funcionarioCalendarioDao.save(funcCalendario);
                }
//                Response r = this.excluir(FuncionarioCalendario.class, f.getIdFuncionarioCalendario());
                Response r = this.excluirEntidade(funcCalendario, removerColetivo);

                //Preciso do valor de volta
                funcCalendario.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) funcCalendario, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.EXCLUIDO));
            } catch (ServiceException ex) {
                funcCalendario.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) funcCalendario, Utils.extractMsgReturn(ex.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            } catch (DaoException ex) {
                funcCalendario.setColetivo(coletivo);
                resultadosColetivo.add(new ColetivoResultado((Entidade) funcCalendario, new MsgRetorno(ex.getMessage(), MSG.TIPOMSG.ALERTA), ColetivoResultado.Enum_ResultadoColetivo.ERRO));
            }
        });
        return resultadosColetivo;
    }

    public Collection<? extends ColetivoResultado> salvarCalendarioColetivo(List<FuncionarioCalendario> funcionarioCalendarioList, ProgressoTransfer progresso) throws ServiceException {
        List<ColetivoResultado> resultadosColetivo = new ArrayList<>();
        List<FuncionarioCalendario> listaRemover = new ArrayList<>();
        funcionarioCalendarioList.stream().forEach((a) -> {
            try {
                Response r = a.getIdFuncionarioCalendario() == null ? this.salvar(a) : this.atualizar(a);
                resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(r), ColetivoResultado.Enum_ResultadoColetivo.LANCADO));
            } catch (ServiceException se) {
                try {
                    //Caso for um novo, talvez n tenha o valor de funcionario aqui
                    Funcionario funcionario = funcionarioService.buscar(Funcionario.class, a.getFuncionario().getIdFuncionario());
                    a.setFuncionario(funcionario);
                    boolean fechamento = se instanceof FechamentoException;
                    resultadosColetivo.add(new ColetivoResultado((Entidade) a, Utils.extractMsgReturn(se.getResponse()), ColetivoResultado.Enum_ResultadoColetivo.ERRO, fechamento));
                    if (!fechamento) {
                        listaRemover.add((FuncionarioCalendario) a.clone());
                    }
                } catch (CloneNotSupportedException ex) {
                    Logger.getLogger(FuncionarioJornadaService.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ServiceException ex) {
                    Logger.getLogger(FuncionarioCalendarioService.class.getName()).log(Level.SEVERE, null, ex);
                }
            } finally {
                progresso.addProgresso(1);
            }
        });

        //Tenta remover os que deram erros, pois esse é o procedimento do coletivo
        if (!listaRemover.isEmpty()) {
            listaRemover.stream().forEach(entidade -> {
                try {
                    if (entidade.getIdFuncionarioCalendario() != null) {
                        this.excluirEntidade(entidade, true);
                    }
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }

        return resultadosColetivo;
    }

    public Response excluirEntidade(FuncionarioCalendario entidade, boolean removerColetivo) throws ServiceException {
        Coletivo coletivo = null;
        if(entidade.getColetivo() != null){
            coletivo = entidade.getColetivo();
            entidade.setColetivo(null);
            try {
                funcionarioCalendarioDao.save(entidade);
            } catch (DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erroExcluir(entidade.toString()), ex);
            }
        }
        
        this.funcionarioCalendarioDao.excluirEntidade(entidade);
        
        if(coletivo != null){
            List<FuncionarioCalendario> funcCalendarioList = coletivo.getFuncionarioCalendarioList().stream().filter(fc ->{
                return !Objects.equals(fc.getIdFuncionarioCalendario(), entidade.getIdFuncionarioCalendario());
            }).collect(Collectors.toList());
            
            coletivo.setFuncionarioCalendarioList(funcCalendarioList);
            
            this.coletivoService.salvar(coletivo);
        }

        if (removerColetivo) {
            this.coletivoService.excluirColetivoSemVinculo(coletivo);
        }

        this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_FUNCIONARIO_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, entidade);

        return this.getTopPontoResponse().sucessoExcluir(entidade.toString());
    }

    public Response excluirPorColetivo(Coletivo c) throws ServiceException {
        try {
            this.funcionarioCalendarioDao.excluirPorColetivo(c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(c.toString()), ex);
        }
    }

    /**
     * Método para remover o coletivo de uma lista de calendario
     *
     * @since 13/01/2013
     * @param funcionariocalendarioList
     * @throws ServiceException
     */
    public void removerColetivo(List<FuncionarioCalendario> funcionariocalendarioList) throws ServiceException {
        if (funcionariocalendarioList != null) {
            funcionariocalendarioList.stream().map(a -> a.getColetivo()).forEach(ac -> {
                try {
                    this.coletivoService.excluirColetivoSemVinculo(ac);
                } catch (ServiceException ex) {
                    Logger.getLogger(AfastamentoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        }
    }
//</editor-fold>

}
