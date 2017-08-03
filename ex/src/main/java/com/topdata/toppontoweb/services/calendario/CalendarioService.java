package com.topdata.toppontoweb.services.calendario;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.ws.rs.core.Response;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.calendario.CalendarioDao;
import com.topdata.toppontoweb.dao.calendario.CalendarioFeriadoDao;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;
import com.topdata.toppontoweb.entity.calendario.Calendario_;
import com.topdata.toppontoweb.entity.calendario.Feriado;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.services.empresa.EmpresaService;
import com.topdata.toppontoweb.services.funcionario.FuncionarioService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//</editor-fold>

/**
 * Realiza as regras de negócio para o CRUD do calendário.
 *
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 24/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class CalendarioService extends TopPontoService<Calendario, Object>
        implements ValidacoesCadastro<Calendario, Object> {

    @Autowired
    CalendarioDao calendarioDao;
    @Autowired
    CalendarioFeriadoDao calendarioFeriadoDao;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;
    @Autowired
    private CalendarioFeriadoServices calendarioFeriadoServices;

    //<editor-fold defaultstate="collapsed" desc="FUNÇÕES AUXILIARES">
    public Response copiarCalendarios(List<Calendario> calendarios) throws ServiceException {
        try {
            //caso seja solicitado a copia de mais de 5 calendários será retornada uma exceção
            if (calendarios.size() > 5) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CALENDARIO.ALERTA_QTD_MAX_COPIAR.getResource()));
            }
            //realiza a copia do calendario
            for (Calendario c : calendarios) {
                //cria um novo calendario com a mesma descrição + a palávra cópia
                Calendario calendario = new Calendario();
                Integer count = 1;
                String descricao = c.getDescricao();
                calendario.setDescricao(descricao + " " + "(Cópia) " + count);

                //valida se já existe uma cópia deste calendário, e caso tenha adiciona mais uma palavra cópia no fim da descrição
                while (isCopiaValida(calendario)) {
                    count++;
                    calendario.setDescricao(descricao + " " + "(Cópia) " + count);
                }
                //tamanho máximo da descrição 30 caracter
                if (calendario.getDescricao().length() > 30) {
                    calendario.setDescricao(calendario.getDescricao().substring(0, 29));
                }

                //busca todos os feriados existente no calendario a ser copiado
                List<CalendarioFeriado> calendarioFeriado = (buscar(Calendario.class, c.getIdCalendario())).getCalendarioFeriadoList();
                List<CalendarioFeriado> novos = new ArrayList<>();
                //cria os calendario feriado novos
                calendarioFeriado.stream().map((CalendarioFeriado cf) -> {
                    CalendarioFeriado calendarioFeriado1 = new CalendarioFeriado();
                    calendarioFeriado1.setCalendario(calendario);
                    calendarioFeriado1.setDataInicio(cf.getDataInicio());
                    calendarioFeriado1.setFeriado(cf.getFeriado());
                    return calendarioFeriado1;
                }).forEach((calendarioFeriado1) -> {
                    novos.add(calendarioFeriado1);
                });

                calendario.setCalendarioFeriadoList(novos);
                this.getDao().save(calendario);
            }
            return this.getTopPontoResponse().sucessoSalvar(new Calendario().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Calendario().toString()), ex);
        }
    }

    private Boolean isCopiaValida(Calendario c) {
        try {
            ValidarJaCadastradoDescricao(c);
            return false;
        } catch (ServiceException e) {
            return true;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="" desc="CRUD">
    @Override
    public Response salvar(Calendario calendario) throws ServiceException {
        try {
            //realiza as validações antes de salvar
            calendario = validarSalvar(calendario);
            //persiste a entidade e recebe o retorno com o id gerado pelo banco de dados
            calendario = (Calendario) this.getDao().save(calendario);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, calendario);
            return this.getTopPontoResponse().sucessoSalvar(calendario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(calendario), ex);
        }
    }

    @Override
    public Response atualizar(Calendario calendario) throws ServiceException {
        try {
            //realiza as validações antes de atualizar o registro
            calendario = validarAtualizar(calendario);
            //persiste a entidade e recebe a entidade atualizada
            calendario = (Calendario) this.getDao().save(calendario);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, calendario);
            return this.getTopPontoResponse().sucessoAtualizar(calendario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Calendario().toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Calendario> classCalendario, Object id) throws ServiceException {
        try {
            Calendario c = validarExcluir(new Calendario((Integer) id));
            this.getDao().delete(c);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, c);
            return this.getTopPontoResponse().sucessoExcluir(c.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Calendario().toString()), ex);
        }
    }

    @Override
    public Calendario buscar(Class<Calendario> entidade, Object id) throws ServiceException {
        try {
            Calendario c = (Calendario) this.getDao().find(Calendario.class, (Integer) id);
            //caso não exista o registro retorna uma exceção
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Calendario().toString()));
            }
            return c;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(new Calendario().toString()), ex);
        }
    }

    public List<Calendario> buscarTodos() throws ServiceException {
        try {
            List<Calendario> calendarioList = calendarioDao.findAll();
            calendarioList.stream().forEach((calendario) -> {
                try {
                    calendario.setCalendarioFeriadoList(calendarioFeriadoDao.findAllByCalendario(calendario));
                } catch (DaoException ex) {
                    Logger.getLogger(CalendarioService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return calendarioList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public Calendario buscarCalendario(Integer idCalendario) throws ServiceException {
        Calendario calendario;
        try {
            calendario = calendarioDao.find(idCalendario);
            calendario.setCalendarioFeriadoList(calendarioFeriadoDao.findAllByCalendario(calendario));
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }

        return calendario;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Calendario validarSalvar(Calendario calendario) throws ServiceException {
        calendario = ValidarJaCadastradoDescricao(calendario);
        return calendario;
    }

    @Override
    public Calendario validarExcluir(Calendario calendario) throws ServiceException {

        calendario = ValidarId(calendario);
//        validarPossuiFechamento(calendario);
        Calendario c = buscar(Calendario.class, calendario.getIdCalendario());
        if (!c.getFuncionarioCalendarioList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CALENDARIO.ALERTA_EXCLUSAO_NAO_PERMITIDA.getResource()));
        }

        return c;
    }

    @Override
    public Calendario validarAtualizar(Calendario calendario) throws ServiceException {

        calendario = ValidarId(calendario);
//        validarPossuiFechamento(calendario);
        calendario = ValidarJaCadastradoDescricao(calendario);
        Calendario c = buscar(Calendario.class, calendario.getIdCalendario());
        calendario.setCalendarioFeriadoList(c.getCalendarioFeriadoList());
        calendario.setFuncionarioCalendarioList(c.getFuncionarioCalendarioList());
        return calendario;

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    private void validarPossuiFechamento(Calendario calendario) throws ServiceException {

        List<Funcionario> funcionarioList = this.funcionarioService.buscarPorCalendario(calendario);
        
        for (Funcionario funcionario : funcionarioList) {
            Empresa empresa = this.empresaService.buscarPorFuncionario(funcionario);
            for (FuncionarioCalendario fc : funcionario.getFuncionarioCalendarioList()) {
                List<CalendarioFeriado> calendarioFeriadoList = this.calendarioFeriadoServices.buscarPorCalendario(fc.getCalendario().getIdCalendario());
                for (CalendarioFeriado cf : calendarioFeriadoList) {
                    this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, cf.getDataInicio(), cf.getDataInicio());
                }
            }
        }

    }

    private Calendario ValidarId(Calendario c) throws ServiceException {
        if (c == null || c.getIdCalendario() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Calendario().toString()));
        }
        return c;
    }

    /**
     * Valida se já existe um calendário com a descrição e em caso de
     * atualização verifica se está tentando alterar para uma descrição já
     * existente.
     *
     * @param calendario
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private Calendario ValidarJaCadastradoDescricao(Calendario calendario) throws ServiceException {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(Calendario_.descricao.getName(), calendario.getDescricao());
            List<Calendario> calendarios = this.getDao().findbyAttributes(map, Calendario.class);
            if (calendarios.size() > 0 && (!Objects.equals(calendario.getIdCalendario(), calendarios.get(0).getIdCalendario()))) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.CALENDARIO.ALERTA_DESCRICAO_JA_CAD.getResource()));
            }
            return calendario;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroValidar(Feriado.class), ex);
        }
    }
//</editor-fold>
}
