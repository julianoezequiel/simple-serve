package com.topdata.toppontoweb.services.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.jornada.JornadaHorarioDao;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario_;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Comparator;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class JornadaHorarioService extends TopPontoService<JornadaHorario, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private HorarioMarcacaoService horarioMarcacaoService;

    @Autowired
    private JornadaHorarioDao jornadaHorarioDao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIAVEIS">
    private Response hasError = null;
    private List<JornadaHorario> jornadaHorarioTemp;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    /**
     * Lista os horário da jornada passando o horário
     *
     * @param h
     * @return
     * @throws ServiceException
     */
    public List<Jornada> buscarJornadaPorHorario(Horario h) throws ServiceException {

        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(JornadaHorario_.horario.getName(), h);
            List<JornadaHorario> list = this.getDao().findbyAttributes(map, JornadaHorario.class);

            return list.stream().map(jh -> jh.getJornada()).distinct().collect(Collectors.toList());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    public List<JornadaHorario> buscarPorHorario(Horario h) throws ServiceException {

        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(JornadaHorario_.horario.getName(), h);
            List<JornadaHorario> list = this.getDao().findbyAttributes(map, JornadaHorario.class);

            return list;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    /**
     * Lista os horarios da jornada, passando a jornada
     *
     * @param j
     * @return
     * @throws ServiceException
     */
    private List<JornadaHorario> buscarPorJornada(Jornada j) throws ServiceException {
        try {
            HashMap<String, Object> map = new HashMap<>();
            map.put(JornadaHorario_.jornada.getName(), j);
            List<JornadaHorario> list = this.getDao().findbyAttributes(map, JornadaHorario.class);
            return list;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    public List<JornadaHorario> buscarJornadasEmUso(Horario h) throws ServiceException {
        try {
            return jornadaHorarioDao.buscarPorHorario(h);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public List<JornadaHorario> buscarPorJornada(Integer id) throws ServiceException {
        try {
//            HashMap<String, Object> map = new HashMap<>();
//            map.put(JornadaHorario_.jornada.getName(), new Jornada(id));
            return this.jornadaHorarioDao.buscarPorJornada(new Jornada(id));
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CRUD">
    @Override
    public JornadaHorario buscar(Class<JornadaHorario> entidade, Object id) throws ServiceException {
        try {
            JornadaHorario jh = (JornadaHorario) this.getDao().find(JornadaHorario.class, id);
            if (jh == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new JornadaHorario().toString()));
            }
            return jh;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public Response salvar(List<JornadaHorario> jornadahorarioList) throws ServiceException {

        try {
            this.jornadaHorarioTemp = buscarPorJornada(jornadahorarioList.iterator().next().getJornada());
            jornadahorarioList = validarSalvar(jornadahorarioList);
            final Jornada jornada = jornadaService.buscar(Jornada.class, jornadahorarioList.iterator().next().getJornada().getIdJornada());
//            jornada.getJornadaHorarioList().clear();
//            jornada.getJornadaHorarioList().addAll(jornadahorarioList);

            //Remove todo mundo
            jornadaHorarioDao.removerTodasPorJornada(jornada);
            
            jornada.setTotalPeriodos(0);
            //Busca o maior periodo
            jornadahorarioList.stream().forEach(jh ->{
                if(jh.getHorario() != null && jh.getHorario().getHorarioMarcacaoList() != null
                        && jornada.getTotalPeriodos() < jh.getHorario().getHorarioMarcacaoList().size()){
                    jornada.setTotalPeriodos(jh.getHorario().getHorarioMarcacaoList().size());
                }
            });
            
            this.getDao().save(jornada);
            
            //Considera todas como novas
            for (JornadaHorario jornadaHorario : jornadahorarioList) {
                jornadaHorario.setJornada(jornada);
                jornadaHorario.setIdJornadaHorario(null);
                jornadaHorarioDao.save(jornadaHorario);
            }
            
            return this.getTopPontoResponse().sucessoSalvar(new JornadaHorario().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new JornadaHorario().toString()), ex);
        }
    }
    
    @Override
    public Response excluir(Class<JornadaHorario> c, Object id) throws ServiceException {
        try {
            JornadaHorario jh = buscar(JornadaHorario.class, (Integer) id);
            this.getDao().delete(jh);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, jh);
            return this.getTopPontoResponse().sucessoExcluir(jh.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new JornadaHorario().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIA">
    public List<JornadaHorario> validarSalvar(List<JornadaHorario> jornadaHorarioList) throws ServiceException {

        for (JornadaHorario jh : jornadaHorarioList) {
            validarCamposObrigatorios(jh);
        }

        validarQuantidadeDeDias(jornadaHorarioList);
        validarInicioFimDia(jornadaHorarioList);
        validarUltimoEPrimeiroDia(jornadaHorarioList);
        return jornadaHorarioList;
    }
//</editor-fold>

    //<editor-fold  desc="REGRAS DE VALIDAÇÕES">
    /**
     * Valida se a quantidade de dias estão corretos, 7 dias para jornada
     * semanal e pelo menos 1 para jornada variavel
     *
     * @param list
     * @throws ServiceException
     */
    private void validarQuantidadeDeDias(List<JornadaHorario> list) throws ServiceException {
        JornadaHorario jornadaHorario;
        if (!list.isEmpty()) {
            jornadaHorario = list.iterator().next();
        } else {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.JORNADA.ALERTA_JORNADA_SEM_HORARIO.getResource()));
        }
        jornadaHorario.setJornada(jornadaService.buscar(Jornada.class, jornadaHorario.getJornada().getIdJornada()));
        if (Objects.equals(jornadaHorario.getJornada().getTipoJornada().getIdTipoJornada(), CONSTANTES.Enum_TIPO_JORNADA.SEMANAL.ordinal()) && list.size() != 7) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.JORNADA_HORARIO.ALERTA_QDT_DIAS_JORNADA_SEMANAL.getResource()));
        }

        if (list.size() > 250) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.JORNADA_HORARIO.ALERTA_QDT_DIAS_JORNADA.getResource()));
        }
    }

    /**
     * Valida os campos obrigatório
     *
     * @param jornadaHorario
     * @throws ServiceException
     */
    private void validarCamposObrigatorios(JornadaHorario jornadaHorario) throws ServiceException {

        if (jornadaHorario.getHorario() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("horário"));
        }
        if (jornadaHorario.getJornada() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("jornada"));
        }
        if (new Integer(jornadaHorario.getIdSequencia()) == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("sequência"));
        }

    }

    /**
     * Valida o final do ultimo dia e o inicio do primeiro
     *
     * @param jornadaHorarioList
     * @throws ServiceException
     */
    private void validarUltimoEPrimeiroDia(List<JornadaHorario> jornadaHorarioList) throws ServiceException {

        //valida o inicio e o fim do ultimo e do primeiro
        try {
            if (!jornadaHorarioList.isEmpty()) {

                //busca as entidades de horário na base, pois na lista os horários não vem com os tipos de dias
                for (JornadaHorario jh : jornadaHorarioList) {
                    Horario h = horarioService.buscar(Horario.class, jh.getHorario().getIdHorario());
                    jh.setHorario(h);
                }

                Horario primeiro = jornadaHorarioList.stream()
                        .filter(m -> m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA.ordinal()
                                && m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA_DIFERENCIADA.ordinal())
                        .map(e -> e.getHorario())
                        .reduce((a, b) -> a)
                        .get();

                Horario ultimo = jornadaHorarioList.stream()
                        .filter(m -> m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA.ordinal()
                                && m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA_DIFERENCIADA.ordinal())
                        .map(e -> e.getHorario())
                        .reduce((a, b) -> b)
                        .get();

                validarInicioFimHorario(ultimo, primeiro, true);
            }
        } catch (ServiceException ex) {
            hasError = ex.getResponse();
        } catch (java.util.NoSuchElementException e) {
            return;
        }

        if (hasError != null) {
            Jornada jornada = jornadaService.buscar(Jornada.class, jornadaHorarioList.iterator().next().getJornada().getIdJornada());
            try {
                jornada.getJornadaHorarioList().clear();
                jornada.getJornadaHorarioList().addAll(jornadaHorarioTemp);
                this.getDao().save(jornada);
            } catch (DaoException ex) {
                Logger.getLogger(JornadaHorarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ServiceException(hasError);
        }

    }

    /**
     * Valida o final de cada dias e o inicio do proximo
     *
     * @param jornadaHorarioList
     * @throws ServiceException
     */
    public void validarInicioFimDia(List<JornadaHorario> jornadaHorarioList) throws ServiceException {
        hasError = null;
        //valida o inicio e o fim de cada horário
        IntStream.range(0, jornadaHorarioList.size() - 1).forEach(i -> {

            try {
                JornadaHorario atual = jornadaHorarioList.get(i);
                Horario horarioAtual = horarioService.buscar(atual.getHorario());

                JornadaHorario proximo = jornadaHorarioList.get(i + 1 < jornadaHorarioList.size() ? i + 1 : jornadaHorarioList.size() - 1);

                if (!Objects.equals(atual.getHorario(), proximo.getHorario())) {
                    //busca o proximo horario ou atribui a entidade do atual para o proximo
                    Horario horarioProximo = horarioService.buscar(Horario.class, proximo.getHorario().getIdHorario());

                    validarInicioFimHorario(horarioAtual, horarioProximo, false);

                }
            } catch (ServiceException ex) {
                hasError = ex.getResponse();
            } catch (NoSuchElementException e) {
            }

        });
        if (hasError != null) {
            Jornada jornada = jornadaService.buscarPorId(jornadaHorarioList.iterator().next().getJornada().getIdJornada());
//            Jornada jornada = jornadaService.buscar(Jornada.class, jornadaHorarioList.iterator().next().getJornada().getIdJornada());
            try {
                jornada.getJornadaHorarioList().clear();
                jornada.getJornadaHorarioList().addAll(jornadaHorarioTemp);
                this.getDao().save(jornada);
            } catch (DaoException ex) {
                Logger.getLogger(JornadaHorarioService.class.getName()).log(Level.SEVERE, null, ex);
            }
            throw new ServiceException(hasError);
        }
    }

    /**
     * Valida se as sequencias de horarios e suas marcações estão corretas.
     * Nesta valdiação não é utilizado as referencias do contexto do hibernate
     * para a verificcação ----- ex1: saida deve ser antes da próxima entrada .
     * ex2: a ultima saida deve ser antes da primeira entrada
     *
     * @param jornadaHorarioList
     * @throws ServiceException
     */
    public void validarSequenciasHorariosSemConsultaBase(List<JornadaHorario> jornadaHorarioList) throws ServiceException {

        hasError = null;
        //valida o inicio e o fim de cada horário
        IntStream.range(0, jornadaHorarioList.size() - 1).forEach(i -> {

            try {
                JornadaHorario atual = jornadaHorarioList.get(i);
                Horario horarioAtual = atual.getHorario();

                JornadaHorario proximo = jornadaHorarioList.get(i + 1 < jornadaHorarioList.size() ? i + 1 : jornadaHorarioList.size() - 1);

                //busca o proximo horario ou atribui a entidade do atual para o proximo
                Horario horarioProximo = proximo.getHorario();

                validarInicioFimHorario(horarioAtual, horarioProximo, false);

            } catch (ServiceException ex) {
                hasError = ex.getResponse();
            } catch (java.util.NoSuchElementException e) {
            }

        });

        //valida o inicio e o fim do ultimo e do primeiro
        try {
            if (!jornadaHorarioList.isEmpty()) {
                //busca as entidades de horário na base, pois na lista os horários não vem com os tipos de dias
//                jornadaHorarioList.stream().forEach(jh -> {
//                    try {
//                        jh.setHorario(horarioService.buscar(Horario.class, jh.getHorario().getIdHorario()));
//                    } catch (ServiceException ex) {
//                        Logger.getLogger(JornadaHorarioService.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                });
                Horario primeiro = jornadaHorarioList.stream()
                        .filter(m -> m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA.ordinal()
                                && m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA_DIFERENCIADA.ordinal())
                        .map(e -> e.getHorario())
                        .reduce((a, b) -> a)
                        .get();

                Horario ultimo = jornadaHorarioList.stream()
                        .filter(m -> m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA.ordinal()
                                && m.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA_DIFERENCIADA.ordinal())
                        .map(e -> e.getHorario())
                        .reduce((a, b) -> b)
                        .get();

                validarInicioFimHorario(ultimo, primeiro, true);
            }
        } catch (ServiceException ex) {
            hasError = ex.getResponse();
        } catch (java.util.NoSuchElementException e) {
            return;
        }

        if (hasError != null) {
            throw new ServiceException(hasError);
        }

    }

    /**
     * Valida se o horario A é anterior ao Horario B. Considera-se um possível
     * virada de dia , tendo como base '00:00' como inicio e '23:59' como fim
     *
     * @param horarioA
     * @param horarioB
     * @param diaInvertido
     * @throws ServiceException
     */
    public void validarInicioFimHorario(Horario horarioA, Horario horarioB, Boolean diaInvertido) throws ServiceException {
        try {
            List<HorarioMarcacao> horarioALista = horarioMarcacaoService.buscarPorHorario(horarioA.getIdHorario());
            List<HorarioMarcacao> horarioBLista = horarioMarcacaoService.buscarPorHorario(horarioB.getIdHorario());
            List<HorarioMarcacao> dia1 = horarioMarcacaoService.convertHorasInteiros(horarioALista);
            List<HorarioMarcacao> dia2 = horarioMarcacaoService.convertHorasInteiros(horarioBLista);
            //filtra a primeira entrada
            HorarioMarcacao entrada = dia2
                    .stream()
                    .sorted((e, e1) -> Integer.compare(e.getIdSequencia(), e1.getIdSequencia()))
                    .reduce((a, b) -> a).get();
            //filtra a ultima saída
            HorarioMarcacao saida = dia1
                    .stream()
                    .sorted((e, e1) -> Integer.compare(e.getIdSequencia(), e1.getIdSequencia()))
                    .reduce((a, b) -> b).get();
            // caso de existir virada de dia e a entrada for mair que a saida
            if (horarioMarcacaoService.possuiVirada(dia1) == true && entrada.getHorarioEntrada().getTime() <= saida.getHorarioSaida().getTime()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO_MARCACAO.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
            } else if (horarioMarcacaoService.possuiVirada(dia2) == true && diaInvertido == true && entrada.getHorarioEntrada().getTime() <= saida.getHorarioSaida().getTime()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO_MARCACAO.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
            }
        } catch (NoSuchElementException e) {
        }

    }
//</editor-fold>
}
