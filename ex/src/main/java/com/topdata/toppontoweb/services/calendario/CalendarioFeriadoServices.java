package com.topdata.toppontoweb.services.calendario;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;
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
//</editor-fold>

/**
 * Realiza as regras de negócio para o CRUD do CalendarioFeriado.
 *
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 30/06/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class CalendarioFeriadoServices extends TopPontoService<CalendarioFeriado, Object>
        implements ValidacoesCadastro<CalendarioFeriado, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    /* Injeta o Repositório de persistência */
    @Autowired
    private FeriadoServices feriadoServices;
    @Autowired
    private FuncionarioService funcionarioService;
    @Autowired
    private EmpresaService empresaService;

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private Response hasError = null;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    public List<CalendarioFeriado> buscarPorCalendario(Integer id) throws ServiceException {
        try {
            Calendario c = ((Calendario) this.getDao().find(Calendario.class, id));
            if (c == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Calendario().toString()));
            }
            return c.getCalendarioFeriadoList();
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    private void validarPossuiFechamento(List<CalendarioFeriado> calendarioFeriadoList) throws ServiceException {
        for (CalendarioFeriado cf : calendarioFeriadoList) {
            if (cf.getIdCalendarioFeriado() != null) {
                CalendarioFeriado calendarioFeriadoOriginal = this.buscar(CalendarioFeriado.class, cf.getIdCalendarioFeriado());
                if (calendarioFeriadoOriginal.getDataInicio().compareTo(cf.getDataInicio()) != 0) {
                    validarPossuiFechamento(calendarioFeriadoOriginal);
                }
            } else {
                validarPossuiFechamento(cf);
            }
        }
    }

    private void validarPossuiFechamento(CalendarioFeriado calendarioFeriado) throws ServiceException {
        List<Funcionario> funcionarioList = this.funcionarioService.buscarPorCalendario(calendarioFeriado.getCalendario());
        if (funcionarioList != null && !funcionarioList.isEmpty()) {
            for (Funcionario funcionario : funcionarioList) {
                Empresa empresa = this.empresaService.buscarPorFuncionario(funcionario);
                this.getFechamentoServices().isPossuiFechamentoPeriodo(empresa, calendarioFeriado.getDataInicio(), calendarioFeriado.getDataInicio());
            }
        }
    }

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    public Response salvar(List<CalendarioFeriado> calendarioFeriadoList) throws ServiceException {

        Calendario c = null;

        if (!calendarioFeriadoList.isEmpty()) {

            try {
                //busca na base de dados o calendário que está sendo alterado
                c = (Calendario) this.getDao().find(Calendario.class, calendarioFeriadoList.iterator().next().getCalendario().getIdCalendario());
                //caso o calendário não exista
                if (c == null) {
                    throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Calendario().toString()));
                }

                //valida a lista original caso seja uma atualização
                validarPossuiFechamento(this.buscarPorCalendario(c.getIdCalendario()));
                //valida a nova lista
                validarPossuiFechamento(calendarioFeriadoList);

                //percorre a lista de Calendário ferido e realiza a validação
                for (CalendarioFeriado cf : calendarioFeriadoList) {

                    //realiza a validação de cada período
                    cf = ValidarExisteFeriado(cf);
                    cf = ValidarPeriodos(cf, calendarioFeriadoList);
                    //atribui a referência do calendário para a lista de calendario feriado
                    cf.setCalendario(c);
                }
                //limpa as referências antigar e atribui a nova lista para o calendário
                c.getCalendarioFeriadoList().clear();
                c.getCalendarioFeriadoList().addAll(calendarioFeriadoList);
                this.getDao().save(c);
            } catch (DaoException ex) {
                throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Calendario().toString()), ex);
            }
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.DEFAULT, CONSTANTES.Enum_AUDITORIA.CAD_CALENDARIO_FERIADO, CONSTANTES.Enum_OPERACAO.INCLUIR, c);
            return this.getTopPontoResponse().sucessoSalvar(c.toString(), new Calendario());
        } else {
            return this.getTopPontoResponse().response(Response.Status.NO_CONTENT, MSG.CADASTRO.ALERTA_NAO_CADASTRADO, CalendarioFeriado.class, MSG.TIPOMSG.ALERTA);
        }
    }

    @Override
    public Response excluir(Class<CalendarioFeriado> c, Object id) throws ServiceException {
        try {
            CalendarioFeriado cf = validarExcluir(new CalendarioFeriado((Integer) id));
            this.getDao().delete(cf);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CALENDARIO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, cf);
            return this.getTopPontoResponse().sucessoExcluir(cf.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(CalendarioFeriado.class), ex);
        }

    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="REGRAS DE VALIDAÇÕES">
    /**
     * Função para auxilio do na validação do dront-end, na adição de um feriado
     * a lista do calendário
     *
     * @param calendarioFeriado
     * @return
     * @throws ServiceException
     */
    public Response validarFeriado(List<CalendarioFeriado> calendarioFeriado) throws ServiceException {
        hasError = null;
        calendarioFeriado.forEach(cf -> {
            try {
                if (cf.getCalendario() != null && cf.getDataInicio() != null && cf.getFeriado() != null) {
                    this.ValidarPeriodos(cf, calendarioFeriado);
                }
            } catch (ServiceException ex) {
                hasError = ex.getResponse();
            }
        });
        return hasError == null ? this.getTopPontoResponse().sucessoValidar(MSG.CALENDARIO.ALERTA_CALENDARIO_VALIDADO.getResource()) : hasError;
    }

    /**
     * Valida se as datas dos feriados não estão sobrepostas
     */
    private CalendarioFeriado ValidarPeriodos(CalendarioFeriado calendarioFeriadoList1, List<CalendarioFeriado> calendarioFeriadoList2) throws ServiceException {
        if (calendarioFeriadoList2.stream().filter(e
                -> e != calendarioFeriadoList1 && e.getDataInicio().equals(calendarioFeriadoList1.getDataInicio()))
                .findAny().orElse(null) != null) {
            throw new ServiceException(getTopPontoResponse().alertaValidacao(MSG.CALENDARIO.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
        }

        return calendarioFeriadoList1;
    }

    /**
     * Valida se existe o feriado a ser vicnulado com o calendário
     *
     * @param i
     * @return
     * @throws com.topdata.toppontoweb.services.ServiceException
     */
    private CalendarioFeriado ValidarExisteFeriado(CalendarioFeriado i) throws ServiceException {

        if (i.getFeriado().getIdFeriado() != null) {
            feriadoServices.buscar(Feriado.class, i.getFeriado().getIdFeriado());
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Feriado().toString()));
        }

        return i;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public CalendarioFeriado validarSalvar(CalendarioFeriado t) throws ServiceException {
        return t;
    }

    @Override
    public CalendarioFeriado validarExcluir(CalendarioFeriado t) throws ServiceException {
        try {
            CalendarioFeriado calendarioFeriado = (CalendarioFeriado) this.getDao().find(CalendarioFeriado.class, t);
            if (calendarioFeriado == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(CalendarioFeriado.class));
            }
            List<Calendario> list = this.getDao().findAll(Calendario.class);
            //verifica se o feriado já está cadastrado em outro calendário
            if (list.stream().map(c -> c.getCalendarioFeriadoList().contains(t)).count() > 0) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.FERIADO.ALERTA_EXCLUSAO_EM_USO.getResource()));
            }
            return calendarioFeriado;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, Calendario.class), ex);
        }
    }

    @Override
    public CalendarioFeriado validarAtualizar(CalendarioFeriado t) throws ServiceException {
        return t;
    }

//</editor-fold>
    public FeriadoServices getFeriadoServices() {
        return feriadoServices;
    }

}
