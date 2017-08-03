package com.topdata.toppontoweb.services.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Objects;
import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.jornada.JornadaDao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.entity.jornada.Jornada_;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.dto.HorarioTransfer;
import com.topdata.toppontoweb.dto.JornadaHorarioTransfer;
import com.topdata.toppontoweb.dto.JornadaTransfer;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.services.funcionario.jornada.FuncionarioJornadaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0.0 data 16/08/2016
 * @since 1.0.0.0 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class JornadaService extends TopPontoService<Jornada, Object>
        implements ValidacoesCadastro<Jornada, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private AuditoriaServices auditarDados;

    @Autowired
    private TopPontoResponse topPontoResponse;
    
    @Autowired
    FuncionarioJornadaService funcionarioJornadaService;

    @Autowired
    private Dao dao;
    
    @Autowired
    private JornadaDao jornadaDao;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    @Override
    public Jornada buscar(Class<Jornada> entidade, Object id) throws ServiceException {
        try {
            Jornada jornada = (Jornada) dao.find(Jornada.class, id);
            if (jornada != null) {
                if (jornada.getPercentualAdicionalNoturno() != null) {
                    jornada.setPercentualNoturno(jornada.getPercentualAdicionalNoturno().toString().replace(".", ","));
                }
            } else {
                throw new ServiceException(topPontoResponse.alertaNaoCad(new Jornada().toString()));
            }
            
            jornada.setFuncionarioJornadaList(this.funcionarioJornadaService.buscarPorJornada(jornada));
            
            jornada.carregarPossuiVinculo();
            return jornada;
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroBuscar(), ex);
        }
    }

    /**
     * Busca uma jornada pela sua descrição
     *
     * @param jornada
     * @return
     * @throws ServiceException
     */
    private Jornada buscarPorDescricao(Jornada jornada) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Jornada_.descricao.getName(), jornada.getDescricao());
            return (Jornada) dao.findOnebyAttributes(map, Jornada.class);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroBuscar(), ex);
        }
    }

    /**
     * Busca as jornadas e converte em transfer
     *
     * @param aClass
     * @param id
     * @return
     * @throws ServiceException
     */
    public JornadaTransfer buscarTransfer(Class<Jornada> aClass, Integer id) throws ServiceException {

        Jornada j = buscar(Jornada.class, id);

        JornadaTransfer jornadaTransfer = new JornadaTransfer();
        jornadaTransfer.setJornada(j);
        jornadaTransfer.setPossuiVinculos(!j.getFuncionarioJornadaList().isEmpty());

        List<HorarioTransfer> horarioTransferList = new ArrayList<>();

        List<JornadaHorarioTransfer> jorndaHorarioTransferList = new ArrayList<>();

        j.getJornadaHorarioList().stream().forEach((jornadaHorario) -> {
            HorarioTransfer horarioTransfer = new HorarioTransfer();
            horarioTransfer.setHorario(jornadaHorario.getHorario());
            horarioTransfer.setJornada(j);
            horarioTransfer.setHorarioMarcacaoList(jornadaHorario.getHorario().getHorarioMarcacaoList());

            JornadaHorarioTransfer jht = new JornadaHorarioTransfer();
            jht.setJornadaHorario(jornadaHorario);

            horarioTransferList.add(horarioTransfer);
        });

        jornadaTransfer.setJorndaHorarioTransferList(jorndaHorarioTransferList);

        return jornadaTransfer;
    }
    
    
    public List<Jornada> buscarPorFuncionario(Funcionario funcionario) throws ServiceException {
        try {
            return jornadaDao.buscarByFuncionario(funcionario);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public Response salvar(Jornada jornada) throws ServiceException {
        try {
            if (jornada.getIdJornada() != null) {
                jornada = validarAtualizar(jornada);
            } else {
                jornada = validarSalvar(jornada);
            }
            jornada = (Jornada) dao.save(jornada);
            if (jornada.getPercentualAdicionalNoturno() != null) {
                jornada.setPercentualNoturno(jornada.getPercentualAdicionalNoturno().toString().replace(".", ","));
            }
            auditarDados.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, jornada);
            return topPontoResponse.sucessoSalvar(jornada.toString(), jornada);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroSalvar(jornada.toString()), ex);
        }
    }

    @Override
    public Response atualizar(Jornada jornada) throws ServiceException {
        try {
            jornada = (Jornada) dao.save(validarAtualizar(jornada));
            if (jornada.getPercentualAdicionalNoturno() != null) {
                jornada.setPercentualNoturno(jornada.getPercentualAdicionalNoturno().toString().replace(".", ","));
            }
            auditarDados.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, jornada);
            return topPontoResponse.sucessoAtualizar(jornada.toString(), jornada);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroAtualizar(jornada.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Jornada> c, Object id) throws ServiceException {
        try {
            Jornada jornada = validarExcluir(new Jornada((Integer) id));
            dao.delete(jornada);
            auditarDados.auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, jornada);
            return topPontoResponse.sucessoExcluir(jornada.toString());
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroExcluir(new Jornada().toString()), ex);
        }
    }
    
    
    public Jornada buscarPorId(Integer idJornada) throws ServiceException {
        try {
            return jornadaDao.buscarPorId(idJornada);
        } catch (DaoException ex) {
            throw new ServiceException(topPontoResponse.erroBuscar());
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES">
    @Override
    public Jornada validarSalvar(Jornada t) throws ServiceException {
        validarCamposObrigatorio(t);
        validarDescricaoJaCadastrada(t);
        validarAdicionalNoturno(t);
        validarJornadaLivrePossuiMarcacoes(t);
        return t;
    }

    @Override
    public Jornada validarExcluir(Jornada t) throws ServiceException {
        validarIdentificador(t);
        validarEmUso(t);
        return buscar(Jornada.class, t.getIdJornada());
    }

    @Override
    public Jornada validarAtualizar(Jornada t) throws ServiceException {
        validarIdentificador(t);
        atualizaValorEntreEntidades(t);
        validarSalvar(t);
        validarVinculo(t);
        return t;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="COPIAR JORNADA">
    public Response copiar(List<Jornada> jornadaList) throws ServiceException {
        //limite de 5 itens para cópia
        if (jornadaList.size() > 5) {
            throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_MAXIMO_COPIA.getResource()));
        }
        // realiza a cópia de um por vez
        for (Jornada j : jornadaList) {
            try {
                //valida campo obrigatório para buscar na base de dados
                if (j.getIdJornada() == null) {
                    throw new ServiceException(topPontoResponse.alertaNaoCad(j.toString()));
                }
                //busca a entidade na base de dados
                Jornada jornada = buscar(Jornada.class, j.getIdJornada());
                //cria uma nova jornada tendo como base a jornda recebida da base de dados
                Jornada j1 = (Jornada) jornada.clone();
                j1.setIdJornada(null);
                j1.setFuncionarioJornadaList(null);
                Integer count = 1;
                String descricao = jornada.getDescricao();
                j1.setDescricao(descricao + " " + "(Cópia) " + count);

                //valida a descrição da jornada , até que possua um nome válido
                while (isCopiaValida(j1)) {
                    count++;
                    j1.setDescricao(descricao + " " + "(Cópia) " + count);
                }
                //tamanho máximo da descrição 50 caracter
                if (j1.getDescricao().length() > 50) {
                    j1.setDescricao(j1.getDescricao().substring(0, 49));
                }

                //lista para as novas jornadasHorarios
                List<JornadaHorario> jornadaHorarioList = new ArrayList<>();

                if (j1.getJornadaHorarioList() != null && !j1.getJornadaHorarioList().isEmpty()) {
                    //cria as novas entidades
                    j1.getJornadaHorarioList().stream().forEach((jh) -> {
                        JornadaHorario jh1 = new JornadaHorario();
                        jh1.setJornada(j1);
                        jh1.setHorario(jh.getHorario());
                        jh1.setIdSequencia(jh.getIdSequencia());
                        jornadaHorarioList.add(jh1);
                    });
                    //adiciona a nova lista à nova jornada
                    j1.setJornadaHorarioList(jornadaHorarioList);
                }
                //persiste a entidade jornada na base de dados
                dao.save(j1);
            } catch (CloneNotSupportedException | DaoException ex) {
                throw new ServiceException(topPontoResponse.erroSalvar(new Jornada().toString()), ex);
            }
        }

        return topPontoResponse.sucessoSalvar(new Jornada().toString());
    }

    /**
     * Valida a descrição da cópia da jornada
     *
     * @param j
     * @return
     */
    private Boolean isCopiaValida(Jornada j) {
        try {
            validarDescricaoJaCadastrada(j);
            return false;
        } catch (ServiceException e) {
            return true;
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES">
    private void validarJornadaLivrePossuiMarcacoes(Jornada jornada) throws ServiceException {
        if (jornada != null && jornada.getTipoJornada() != null && jornada.getTipoJornada().getIdTipoJornada() == CONSTANTES.Enum_TIPO_JORNADA.LIVRE.ordinal()) {
            if (jornada.getJornadaHorarioList() != null) {
                jornada.getJornadaHorarioList().clear();
            }
        }
    }

    /**
     * Valida os campos obrigatórios
     *
     * @param o
     * @throws ServiceException
     */
    public void validarCamposObrigatorio(Jornada o) throws ServiceException {
        if (o.getDescricao() == null) {
            throw new ServiceException(topPontoResponse.campoObrigatorio(Jornada_.descricao.getName()));
        }
        if (o.getTipoJornada() == null) {
            throw new ServiceException(topPontoResponse.campoObrigatorio("Tipo de jornada"));
        }
    }

    /**
     * Valida se a decrição já esta cadastrada
     *
     * @param o
     * @throws ServiceException
     */
    public void validarDescricaoJaCadastrada(Jornada o) throws ServiceException {
        Jornada jornada = buscarPorDescricao(o);
        if (jornada != null) {
            if (jornada.getDescricao().equals(o.getDescricao()) && !Objects.equal(o, jornada)) {
                throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_DECRICAO_JA_CADASTRADA.getResource()));
            }
        }
    }

    /**
     * Valida os campos obrigatória ,para quando está habilitado o adicinal
     * noturno
     *
     * @param o
     * @throws ServiceException
     */
    public void validarAdicionalNoturno(Jornada o) throws ServiceException {

        if (o.getInicioAdicionalNoturno() != null && o.getTerminoAdicionalNoturno() != null) {
            if (o.getInicioAdicionalNoturno().compareTo(o.getTerminoAdicionalNoturno()) == 0) {
                throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_PARAMETRO_INICIO_FIM.getResource()));
            }

//            if (o.getPercentualNoturno() == null || o.getPercentualNoturno().equals("")) {
//                throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_PARAMETRO_PERCENTUAL_OBRIGATORIO.getResource()));
//            }
            if (Double.parseDouble(o.getPercentualNoturno().replace(",", ".")) > 200) {
                throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_PARAMETRO_PERCENTUAL_MAX.getResource()));
            }
            o.setPercentualAdicionalNoturno(new Double(o.getPercentualNoturno().replace(",", ".")));
        }
    }

    /**
     * Valida se a jornada possui um id válido
     *
     * @param o
     * @throws ServiceException
     */
    public void validarIdentificador(Jornada o) throws ServiceException {
        if (o == null || o.getIdJornada() == null) {
            throw new ServiceException(topPontoResponse.alertaNaoCad(new Jornada().toString()));
        }
    }

    /**
     * Valida se a jornada possui vinculos com os funcionários
     *
     * @param o
     * @throws ServiceException
     */
    public void validarEmUso(Jornada o) throws ServiceException {

        if (!(buscar(Jornada.class, o.getIdJornada())).getFuncionarioJornadaList().isEmpty()) {
            throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_EXCLUIR_VINCULO_FUNCIONARIO.getResource()));
        }
    }

    /**
     * Atualiza os valores entre a entidade jornada enviada do frontend e a
     * jornada existente na base de dados
     *
     * @param o
     * @throws ServiceException
     */
    public void atualizaValorEntreEntidades(Jornada o) throws ServiceException {

        Jornada j = buscar(Jornada.class, o.getIdJornada());
        if (j != null) {
            o.setCompensaAtrasos(o.getCompensaAtrasos() != null ? o.getCompensaAtrasos() : j.getCompensaAtrasos());
            o.setDescontaHorasDSR(o.getDescontaHorasDSR() != null ? o.getDescontaHorasDSR() : j.getDescontaHorasDSR());
            o.setDescricao(o.getDescricao() != null ? o.getDescricao() : j.getDescricao());
//            o.setDiaFechamentoExtra(o.getDiaFechamentoExtra() != null ? o.getDiaFechamentoExtra() : j.getDiaFechamentoExtra());
//            o.setFuncionarioJornadaList(o.getFuncionarioJornadaList() != null ? o.getFuncionarioJornadaList() : j.getFuncionarioJornadaList());
//            o.setFuncionarioJornadaList(o.getFuncionarioJornadaList() != null ? o.getFuncionarioJornadaList() : j.getFuncionarioJornadaList());
            o.setHorasExtrasAcumulo(o.getHorasExtrasAcumulo() != null ? o.getHorasExtrasAcumulo() : j.getHorasExtrasAcumulo());
            o.setIdJornada(o.getIdJornada() != null ? o.getIdJornada() : j.getIdJornada());
//            o.setInicioAdicionalNoturno(o.getInicioAdicionalNoturno() != null ? o.getInicioAdicionalNoturno() : j.getInicioAdicionalNoturno());
            o.setJornadaHorarioList(o.getJornadaHorarioList() != null ? o.getJornadaHorarioList() : j.getJornadaHorarioList());
//            o.setJornadaVariavel(o.getJornadaVariavel() != null ? o.getJornadaVariavel() : j.getJornadaVariavel());
            o.setTipoJornada(o.getTipoJornada() != null ? o.getTipoJornada() : j.getTipoJornada());
            o.setPagaHorasFeriado(o.getPagaHorasFeriado() != null ? o.getPagaHorasFeriado() : j.getPagaHorasFeriado());
//            o.setPercentuaisAcrescimo(o.getPercentuaisAcrescimo() != null ? o.getPercentuaisAcrescimo() : j.getPercentuaisAcrescimo());
            o.setPercentualAdicionalNoturno(o.getPercentualAdicionalNoturno() != null ? o.getPercentualAdicionalNoturno() : j.getPercentualAdicionalNoturno());
            o.setPercentualNoturno(o.getPercentualNoturno() != null ? o.getPercentualNoturno() : j.getPercentualNoturno());
//            o.setSemana(o.getSemana() != null ? o.getSemana() : j.getSemana());
//            o.setTerminoAdicionalNoturno(o.getTerminoAdicionalNoturno() != null ? o.getTerminoAdicionalNoturno() : j.getTerminoAdicionalNoturno());
//            o.setToleranciaAusencia(o.getToleranciaAusencia() != null ? o.getToleranciaAusencia() : j.getToleranciaAusencia());
//            o.setToleranciaExtra(o.getToleranciaExtra() != null ? o.getToleranciaExtra() : j.getToleranciaExtra());
//            o.setToleranciaOcorrencia(o.getToleranciaOcorrencia() != null ? o.getToleranciaOcorrencia() : j.getToleranciaOcorrencia());
            o.setTotalHoras(o.getTotalHoras() != null ? o.getTotalHoras() : j.getTotalHoras());
            o.setTotalPeriodos(o.getTotalPeriodos() != null ? o.getTotalPeriodos() : j.getTotalPeriodos());
            o.setTrataDomingoDiaNormal(o.getTrataDomingoDiaNormal() != null ? o.getTrataDomingoDiaNormal() : j.getTrataDomingoDiaNormal());
            o.setTrataSabadoDiaNormal(o.getTrataSabadoDiaNormal() != null ? o.getTrataSabadoDiaNormal() : j.getTrataSabadoDiaNormal());
        }
    }

    /**
     * valida o flag da jornada, se ela possui vinculos
     *
     * @param jornada
     * @throws ServiceException
     */
    private void validarVinculo(Jornada jornada) throws ServiceException {
        Jornada j = buscar(Jornada.class, jornada.getIdJornada());
        if (j.getPossuiVinculo()) {
            throw new ServiceException(topPontoResponse.alertaValidacao(MSG.JORNADA.ALERTA_EDITAR_VINCULO_FUNCIONARIO.getResource()));
        }
    }
//</editor-fold>

    
}
