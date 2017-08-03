package com.topdata.toppontoweb.services.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.jornada.HorarioMarcacaoDao;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao_;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.services.auditoria.AuditoriaServices;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.Utils;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Objects;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class HorarioMarcacaoService extends TopPontoService<HorarioMarcacao, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private HorarioService horarioService;

    @Autowired
    private JornadaHorarioService jornadaHorarioService;

    @Autowired
    private JornadaService jornadaService;
    
    @Autowired
    private HorarioMarcacaoDao horarioMarcacaoDao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;
    private Boolean possuiViradaMesmoHorario = false;
    private Boolean possuiViradaEntreMarcacoes = false;
    private Boolean erro = false;
    private Horario h;
    private Response hasError = null;
    //</editor-fold>

    @Override
    public HorarioMarcacao buscar(Class<HorarioMarcacao> entidade, Object id) throws ServiceException {
        try {
            HorarioMarcacao hm = (HorarioMarcacao) this.getDao().find(HorarioMarcacao.class, id);
            if (hm == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new HorarioMarcacao().toString()));
            }
            return hm;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    public Response salvar(List<HorarioMarcacao> horarioMarcacaoList) throws ServiceException {

        try {

            //valida se as sequencias da nova lista estão corretas
            validarListaSequencia(horarioMarcacaoList);
            //valida se esta em uso em outras jornadas
//            validarHorarioEmUso(horarioService.buscar(Horario.class, horarioMarcacaoList.iterator().next().getHorario().getIdHorario()), horarioMarcacaoList);

            //busca a referencia correta do horário na base
            h = horarioService.buscar(Horario.class, horarioMarcacaoList.iterator().next().getHorario().getIdHorario());
            //salva o horário validado com a lista de horario marcações vazia
            h.getHorarioMarcacaoList().clear();

            h.getHorarioMarcacaoList().addAll(horarioMarcacaoList);
            h = (Horario) this.getDao().save(h);
//            //salva as novas marcações
//            horarioMarcacaoList.forEach(hm -> {
//                try {
//                    this.getDao().save(hm);
//                } catch (DaoException ex) {
//                    Logger.getLogger(HorarioMarcacaoService.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            });
        } catch (NoSuchElementException | DaoException e) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new Horario().toString()));
        }

        return this.getTopPontoResponse().sucessoSalvar(new HorarioMarcacao().toString());
    }

    private void validarHorarioEmUso(HorarioMarcacao hm) throws ServiceException {
        if (Objects.nonNull(hm) && Objects.nonNull(hm.getHorario())) {
            //busca o horario na base
            Horario h = horarioService.buscar(Horario.class, hm.getHorario().getIdHorario());
            //busca todas jornadas que estão em uso com este horário
            List<JornadaHorario> jornadaHorariosList = jornadaHorarioService.buscarJornadasEmUso(hm.getHorario());
            List<Jornada> jornadas = jornadaHorariosList.stream().map(jh -> jh.getJornada()).collect(Collectors.toList());
//            if(jornadas.size()>1){
//                throw 
//            }

        }
    }

    @Override
    public Response excluir(Class<HorarioMarcacao> c, Object id) throws ServiceException {
        try {
            HorarioMarcacao hm = buscar(HorarioMarcacao.class, id);
            this.getDao().delete(hm);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, hm);
            return this.getTopPontoResponse().sucessoExcluir(hm.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new HorarioMarcacao().toString()), ex);
        }
    }

    public List<HorarioMarcacao> validarListaSequencia(List<HorarioMarcacao> horarioMarcacoeslist) throws ServiceException {

        for (HorarioMarcacao horarioMarcacao1 : horarioMarcacoeslist) {
            validarCamposObrigatorios(horarioMarcacao1);
        }

        //ordena pelo id de sequencia
        horarioMarcacoeslist.stream().sorted((e, e1) -> Integer.compare(e.getIdSequencia(), e1.getIdSequencia()));

        convertHorasInteiros(horarioMarcacoeslist);
        possuiViradaMesmoHorario(horarioMarcacoeslist);
        horariosRepetidos(horarioMarcacoeslist);
        possuiViradaEntreHorarios(horarioMarcacoeslist);
        validarMaximo24Horas(horarioMarcacoeslist);
        return horarioMarcacoeslist;

    }

    public void validarHorarioEmUso(Horario horario, List<HorarioMarcacao> horarioMarcacoeslist) throws ServiceException {
        erro = false;
        hasError = null;

        //Busca as jornadas que o horário esta vinculado 
        List<Jornada> jornadaList = jornadaHorarioService.buscarJornadaPorHorario(horario);
        //adiciona a lista de horarios marcacoes para realizar a validação com as demais sequencias já existentes 
        jornadaList.forEach(jornada -> {
            try {
                jornada.getJornadaHorarioList().forEach(jh -> {
                    if (jh.getHorario().equals(horario)) {
                        jh.setHorario(horario);
                    }
                });

                jornadaHorarioService.validarSequenciasHorariosSemConsultaBase(jornada.getJornadaHorarioList());

            } catch (ServiceException ex) {
                hasError = ex.getResponse();
            }
        });

        if (hasError != null) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.JORNADA_HORARIO.ALERTA_ALTERACAO_NAO_PERMITIDA.getResource()));
        }
        this.getDao().getEntityManager().clear();
    }

    private void atualizarTotalDePeriodos(Horario horario, Integer totalPeriodos) throws ServiceException {
        List<Jornada> jornadaList = new ArrayList<>();
        //lista somente uma de cada jornada existente no horário
        horario.getJornadaHorarioList().stream().distinct().forEach(jh -> {
            if (!jornadaList.contains(jh.getJornada())) {
                jornadaList.add(jh.getJornada());
            }
        });

        //se o total de periodos da jornada é menor que o total de periodos do horario atual
        jornadaList.forEach(ja -> {
            if (ja.getTotalPeriodos() < totalPeriodos) {
                try {
                    Jornada j = jornadaService.buscar(Jornada.class, ja.getIdJornada());
                    List<JornadaHorario> list = j.getJornadaHorarioList();
                    j.setTotalPeriodos(totalPeriodos);
                    this.getDao().save(j);
                    jornadaHorarioService.salvar(list);
                } catch (ServiceException | DaoException ex) {
                    Logger.getLogger(HorarioMarcacaoService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    public List<HorarioMarcacao> buscarPorHorario(Integer id) throws ServiceException {
        return buscarPorHorario(new Horario(id));
    }

    private List<HorarioMarcacao> buscarPorHorario(Horario horario) throws ServiceException {
        try {
            return horarioMarcacaoDao.buscarPorHorario(horario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    /**
     * verificar campos obrigatório
     */
    private void validarCamposObrigatorios(HorarioMarcacao o) throws ServiceException {

        if (o.getHorario() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(HorarioMarcacao_.horario.getName()));
        }
        if (o.getHorarioEntrada() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("horário de entrada"));
        }
        if (o.getHorarioSaida() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("horário de saída"));
        }
        if (new Integer(o.getIdSequencia()) == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(HorarioMarcacao_.horarioSaida.getName()));
        }
    }

    /**
     * verificar a primeira entrada e a última saida é maior que 24 horas
     */
    private void validarMaximo24Horas(List<HorarioMarcacao> list) throws ServiceException {

        try {
            HorarioMarcacao primeiraMarcacao = list.stream().reduce((a, b) -> a).get();

            HorarioMarcacao ultimaMarcacao = list.stream()
                    .filter(hm -> hm.getHorarioEntrada().getTime() != 0 || hm.getHorarioSaida().getTime() != 0)
                    .collect(Collectors.toList())
                    .stream()
                    .reduce((a, b) -> b).get();

            if ((possuiViradaMesmoHorario == true || possuiViradaEntreMarcacoes == true)
                    && ultimaMarcacao.getHorarioSaida().getTime() >= primeiraMarcacao.getHorarioEntrada().getTime()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_TOTAL_HORAS_MAIOR_24.getResource()));
            }

        } catch (NoSuchElementException ex) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_SEM_MARCACAO.getResource()), ex);
        }
    }

    public List<HorarioMarcacao> convertHorasInteiros(List<HorarioMarcacao> list) {
        list.stream().forEach(hm -> {
            hm.setHorarioEntradaInteiro(Utils.horasInt(hm.getHorarioEntrada()));
            hm.setHorarioSaidaInteiro(Utils.horasInt(hm.getHorarioSaida()));
        });
        return list;
    }

    //verifica se existe virada de dia no mesmo horario e se existir mais de uma virada retorna erro
    private Boolean possuiViradaMesmoHorario(List<HorarioMarcacao> list) throws ServiceException {
        possuiViradaMesmoHorario = false;
        erro = false;
        list.forEach(hm -> {
            if (hm.getHorarioEntrada().getTime() >= hm.getHorarioSaida().getTime()) {
                if (possuiViradaMesmoHorario == true) {
                    erro = true;
                } else {
                    possuiViradaMesmoHorario = true;
                }
            }
        });
        if (erro == true) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_TOTAL_HORAS_MAIOR_24.getResource()));
        }
        return possuiViradaMesmoHorario;
    }

    //verifica se existe virada de dia entre os horários
    private Boolean possuiViradaEntreHorarios(List<HorarioMarcacao> list) throws ServiceException {

        possuiViradaEntreMarcacoes = false;

        IntStream.range(0, list.size() - 1).forEach(s -> {
            HorarioMarcacao atual = list.get(s);
            HorarioMarcacao proximo = list.get(s < list.size() - 1 ? s + 1 : list.size() - 1);
            if (atual.getHorarioSaida().getTime() >= proximo.getHorarioEntrada().getTime()) {
                possuiViradaEntreMarcacoes = true;
            }
        });

        return possuiViradaEntreMarcacoes;
    }

    public Boolean possuiVirada(List<HorarioMarcacao> list) throws ServiceException {
        Boolean virada = possuiViradaMesmoHorario(list);
        virada = virada == false ? virada = possuiViradaEntreHorarios(list) : virada;
        return virada;
    }

    private void horariosRepetidos(List<HorarioMarcacao> list) throws ServiceException {
        erro = false;
        list.stream().forEach(hm -> {
            if (!list.stream()
                    .filter(
                            hm2 -> hm2.getIdSequencia() != hm.getIdSequencia())
                    .collect(Collectors.toList())
                    .stream().filter(
                            hm3 -> (hm3.getHorarioEntrada().compareTo(hm.getHorarioEntrada()) == 0
                            || hm3.getHorarioSaida().compareTo(hm.getHorarioSaida()) == 0)
                            || hm3.getHorarioEntrada().compareTo(hm.getHorarioSaida()) == 0)
                    .collect(Collectors.toList()).isEmpty() || !list.stream()
                    .filter(
                            hm2 -> hm2.getIdSequencia() == hm.getIdSequencia()
                            && hm2.getHorarioEntrada().compareTo(hm2.getHorarioSaida()) == 0)
                    .collect(Collectors.toList()).isEmpty()) {
                erro = true;
            }

        });

        if (erro == true) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO_MARCACAO.ALERTA_PERIODO_JA_CADASTRADO.getResource()));
        }
    }

    public void excluirTodosPorHorario(Horario horario) throws ServiceException {
        try {
            this.horarioMarcacaoDao.excluirTodosPorHorario(horario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(HorarioMarcacao.class));
        }
    }
}
