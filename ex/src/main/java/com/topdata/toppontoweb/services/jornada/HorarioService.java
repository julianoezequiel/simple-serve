package com.topdata.toppontoweb.services.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.jornada.HorarioDao;
import com.topdata.toppontoweb.dto.PaginacaoTransfer;
import com.topdata.toppontoweb.dto.jornada.HorarioTransfer;
import com.topdata.toppontoweb.entity.Entidade;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Horario_;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.ValidacoesCadastro;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0.0 data 18/01/2017
 * @since 1.0.0.0 data 16/08/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class HorarioService extends TopPontoService<Horario, Object>
        implements ValidacoesCadastro<Horario, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private HorarioMarcacaoService horarioMarcacaoService;

    @Autowired
    private JornadaHorarioService jornadaHorarioService;
    
    @Autowired
    private HorarioDao horarioDao;
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIÁVEIS">
    private HashMap<String, Object> map;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
//</editor-fold>

    //<editor-fold defaultstate="" desc="CRUD">
    @Override
    public Horario buscar(Class<Horario> entidade, Object id) throws ServiceException {
        try {
            Horario h = (Horario) this.getDao().find(Horario.class, id);
            if (h == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new Horario().toString()));
            }
            try {
                h.setHorarioMarcacaoList(this.horarioMarcacaoService.buscarPorHorario(h.getIdHorario()));
                h.setJornadaHorarioList(this.jornadaHorarioService.buscarPorHorario(h));
            } catch (Exception e) {

            }
            return h;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar(), ex);
        }
    }

    @Override
    public Response salvar(Horario entidade) throws ServiceException {
        try {
            entidade = (Horario) this.getDao().save(validarSalvar(entidade));
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, entidade);
            return this.getTopPontoResponse().sucessoSalvar(entidade.toString(), entidade);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(entidade.toString()), ex);
        }
    }

    public Response salvar(com.topdata.toppontoweb.dto.HorarioTransfer horarioTransfer) throws ServiceException {

        try {
            //caso venha do front-end um horário nulo
            if (horarioTransfer.getHorario() == null) {
                throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("Horário"));
            }
            //verifica se o horário esta com id válido
            if (horarioTransfer.getHorario().getIdHorario() != null && horarioTransfer.getHorario().getIdHorario() == 0) {
                horarioTransfer.getHorario().setIdHorario(null);
            }

            //horario sem marcações, somente folga e folga diferenciada
            if (horarioTransfer.getHorarioMarcacaoList().isEmpty()
                    && horarioTransfer.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA.ordinal()
                    && horarioTransfer.getHorario().getTipodia().getIdTipodia() != CONSTANTES.Enum_TIPODIA.FOLGA_DIFERENCIADA.ordinal()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_SEM_MARCACAO.getResource()));
            }

            //validações para o horário
            validarDescricao(horarioTransfer.getHorario());
            validarCamposObrigatorios(horarioTransfer.getHorario());

            Horario horario = horarioTransfer.getHorario();

            //validações para as marcações
            if (!horarioTransfer.getHorarioMarcacaoList().isEmpty()) {
                horario.setHorarioMarcacaoList(horarioMarcacaoService.validarListaSequencia(horarioTransfer.getHorarioMarcacaoList()));
            }

            //caso seja uma edição de horário, veridica em quais jornadas estão snedo utilziados
            if (horarioTransfer.getHorario().getIdHorario() != null) {
                //verifica se esta ocorrendo aletrações no horário
                Horario h = buscar(Horario.class, horarioTransfer.getHorario().getIdHorario());

                //se o tamanho da lista ou o tipo de dia for diferente já existe alterações nos registros
                Boolean isAlterado = isAlterado(h, horarioTransfer.getHorario());

                List<Jornada> jornadasList = h.getJornadaHorarioList().stream().map(hor -> hor.getJornada()).distinct().collect(Collectors.toList());

                if (isAlterado && (jornadasList.size() > 1
                        || (!jornadasList.isEmpty() && !jornadasList.iterator().next().equals(horarioTransfer.getJornada())))) {
                    throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_ATUALIZAR.getResource()));
                }

                //se estive havendo alterações realiza a validação
                if (horarioTransfer.getJornada().getIdJornada() != null && isAlterado == true) {
                    //busca a referencia compleda da jornda na base de dados
                    horarioTransfer.setJornada(jornadaService.buscar(Jornada.class, horarioTransfer.getJornada().getIdJornada()));
                    //busca todas as jorndas que estão utilizando este horário
                    List<Jornada> jornadas = jornadaHorarioService.buscarJornadaPorHorario(horarioTransfer.getHorario());
                    //se o horário estiver em mais de uma jornada ou a jornada atual não possuir este horário
                    if (jornadas.size() > 1 || (jornadas.size() == 1 && !jornadas.contains(horarioTransfer.getJornada()))) {
                        throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_ATUALIZAR.getResource()));
                    }
                }
            }
            
            //Remove todos os horarios marcacoes existentes
            if(horario.getIdHorario() != null){
                horarioMarcacaoService.excluirTodosPorHorario(horario);
            }

            //salva o registro na base de djornados
            Horario novoHorario = (Horario) horarioDao.save(horario);
            
            
            //realiza a aoditoria da ação
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, horario);
            
            if(novoHorario != null && novoHorario.getIdHorario() != null){
                //adiciona a mesma referencia de horario para a lista de marcações
                horarioTransfer.getHorarioMarcacaoList().forEach(hm -> {
                    hm.setHorario(novoHorario);
                    hm.setIdHorarioMarcacao(null);
                    try {
                        horarioMarcacaoService.salvar(hm);
                    } catch (ServiceException ex) {
                        Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
                        
            //retorna a mensagem e a entidade criada
            return this.getTopPontoResponse().sucessoSalvar(horario.toString(), novoHorario);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(horarioTransfer.getHorario().toString()), ex);
        }
    }

    /**
     * Verifica se houve alguma alteracao importante no horário e em suas
     * marcações
     *
     * @param h1
     * @param h2
     * @return
     */
    private Boolean isAlterado(Horario h1, Horario h2) {
        AtomicBoolean diff = new AtomicBoolean(
                h1.getTipodia().getIdTipodia().equals(h2.getTipodia().getIdTipodia())
                && h1.getHorarioMarcacaoList().size() == h2.getHorarioMarcacaoList().size());
        if (diff.get()) {
            diff.set(false);
            h1.getHorarioMarcacaoList().stream().forEach(hh1 -> {
                h2.getHorarioMarcacaoList().stream().forEach(hh2 -> {
                    if (!diff.get()) {
                        diff.set(!((!hh1.getHorario().equals(hh2.getHorario())
                                || !new Date(hh1.getHorarioEntrada().getTime()).equals(hh2.getHorarioEntrada())
                                || !new Date(hh1.getHorarioSaida().getTime()).equals(hh2.getHorarioSaida()))
                                && hh1.getIdSequencia() == hh2.getIdSequencia()));
                    }
                });
            });
        }
        return !diff.get();
    }

    public Horario salvarHorario(Horario entidade) throws ServiceException {
        try {
            entidade = validarSalvar(entidade);
            entidade = (Horario) this.getDao().save(entidade);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, entidade);
            return entidade;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(entidade.toString()), ex);
        }
    }

    @Override
    public Response atualizar(Horario entidade) throws ServiceException {
        try {
            entidade = validarAtualizar(entidade);
            entidade = (Horario) this.getDao().save(entidade);
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EDITAR, entidade);
            return this.getTopPontoResponse().sucessoAtualizar(entidade.toString(), entidade);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroAtualizar(entidade.toString()), ex);
        }
    }

    @Override
    public Response excluir(Class<Horario> c, Object id) throws ServiceException {
        try {
            Horario horario = validarExcluir(new Horario((Integer) id));
            
            //Deleta os horariosMarcacoes
            if(horario != null && horario.getIdHorario() != null){
                //adiciona a mesma referencia de horario para a lista de marcações
                horario.getHorarioMarcacaoList().forEach(hm -> {
                    try {
                        horarioMarcacaoService.excluir(HorarioMarcacao.class, hm.getIdHorarioMarcacao());
                    } catch (ServiceException ex) {
                        Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            }
            
            //Depois, deleta o horario
            this.getDao().delete(horario);
            
            
            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_JORNADAS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, horario);
            return this.getTopPontoResponse().sucessoExcluir(new Horario().toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new Horario().toString()), ex);
        }
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VALIDAÇÕES OBRIGATÓRIAS">
    @Override
    public Horario validarSalvar(Horario t) throws ServiceException {
        validarCamposObrigatorios(t);
        validarDescricao(t);
        return t;
    }

    @Override
    public Horario validarExcluir(Horario t) throws ServiceException {
        validarIdentificador(t);
        validarEmUso(t);
        return buscar(Horario.class, t.getIdHorario());
    }

    @Override
    public Horario validarAtualizar(Horario t) throws ServiceException {

        validarIdentificador(t);

        //Horario h = buscar(Horario.class, t.getIdHorario());
        t.setHorarioMarcacaoList(buscar(Horario.class, t.getIdHorario()).getHorarioMarcacaoList());
        t = validarSalvar(t);
        return t;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSULTAS">
    public List<HorarioJornadaSequencia> buscarPorJornada(Integer id) throws ServiceException {

        List<HorarioJornadaSequencia> jornadaSequencias = new ArrayList<>();

        Jornada jornada = jornadaService.buscar(Jornada.class, id);

        jornada.getJornadaHorarioList().stream().distinct()
                .collect(Collectors.toList()).stream().forEach(jh -> {
            HorarioJornadaSequencia hjs = new HorarioJornadaSequencia();
            hjs.setIdHorario(jh.getIdSequencia());
            //Somente para "maquear" problema da sequencia começar em 0 para o novo sistema
            // e começar em 1 para o antigo
            Integer sequenciaVirtual = jh.getIdSequencia() + 1;
            hjs.getDescricao().append(Objects.equals(jornada.getTipoJornada().getIdTipoJornada(), CONSTANTES.Enum_TIPO_JORNADA.VARIAVEL.ordinal())
                    ? "(" + sequenciaVirtual + ") Sequência : "
                    : CONSTANTES.Enum_DIA_SEMANA.getById(jh.getIdSequencia()).getDescricao() + " : ");

            if (jornadaSequencias.isEmpty()) {
                validarSequencia(jh, hjs);
            } else if (!Objects.equals(jornadaSequencias.get(jornadaSequencias.size() - 1).getIdHorario(), jh.getIdSequencia())) {
                validarSequencia(jh, hjs);
            }

            jornadaSequencias.add(hjs);
        });

        jornadaSequencias.stream().distinct().forEach(s -> {
            StringBuffer sb = new StringBuffer();
            s.getMarcacoes().stream().distinct().forEach((String[] s2) -> {
                sb.append(" - [ ").append(s2[0].length() == 1 ? "0" + s2[0] : s2[0]).append(" as ").append(s2[1].length() == 1 ? "0" + s2[1] : s2[1]).append(" ] ");
            });
            sb.replace(0, 3, "");
            s.getDescricao().append(sb);
        });

        return jornadaSequencias;
    }

    private Horario buscarPorDescricao(Horario horario) throws ServiceException {
        try {
            map = new HashMap<>();
            map.put(Horario_.descricao.getName(), horario.getDescricao());
            return (Horario) this.getDao().findOnebyAttributes(map, Horario.class);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    public List<Horario> buscarTodos() throws ServiceException {
        try {
            List<?> list = this.buscarTodos(Horario.class);
            List<Horario> horarioList = (List<Horario>) list;
            horarioList.stream().forEach(h -> {
                try {
                    h.setHorarioMarcacaoList(this.horarioMarcacaoService.buscarPorHorario(h.getIdHorario()));
                    h.setJornadaHorarioList(this.jornadaHorarioService.buscarPorHorario(h));
                } catch (ServiceException ex) {
                    Logger.getLogger(HorarioService.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return horarioList;
        } catch (ServiceException ex) {
            throw new ServiceException();
        }
    }
//</editor-fold>

    //<editor-fold desc="REGRAS DE VALIDAÇÕES">
    private HorarioJornadaSequencia validarSequencia(JornadaHorario jh, HorarioJornadaSequencia hjs) {
        if (jh.getHorario().getHorarioMarcacaoList().isEmpty()) {
            hjs.getMarcacoes().add(new String[]{"--:--", "--:--"});
        } else {
            jh.getHorario().getHorarioMarcacaoList().stream().distinct().forEach(hm -> {
                Calendar c = Calendar.getInstance();
                c.setTime(hm.getHorarioEntrada());
                c.add(Calendar.HOUR_OF_DAY, 3);
                Calendar c2 = Calendar.getInstance();
                c2.setTime(hm.getHorarioSaida());
                c2.add(Calendar.HOUR_OF_DAY, 3);
                hjs.getMarcacoes().add(new String[]{dateFormat.format(c.getTime()),
                    dateFormat.format(c2.getTime())});
            });
        }
        return hjs;
    }

    /**
     * Valida os campos obrigatório para o cadastro de um horário
     *
     * @param o
     * @throws ServiceException
     */
    private void validarCamposObrigatorios(Horario o) throws ServiceException {
        if (o.getDescricao() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Horario_.descricao.getName()));
        }
        if (o.getTipodia() == null) {
            throw new ServiceException(this.getTopPontoResponse().campoObrigatorio(Horario_.tipodia.getName()));
        }

    }

    /**
     * Valida se já existe um horário com a descrição do registro a ser
     * cadastrado
     *
     * @param o
     * @throws ServiceException
     */
    private void validarDescricao(Horario o) throws ServiceException {
        Horario horario = buscarPorDescricao(o);
        if (horario != null) {
            if (o.getDescricao().equals(horario.getDescricao()) && !Objects.equals(o, horario)) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_DECRICAO_JA_CADASTRADA.getResource()));
            }
        }
    }

    /**
     * Valida se o registro possui um id válido
     *
     * @param o
     * @throws ServiceException
     */
    private void validarIdentificador(Horario o) throws ServiceException {
        if (o == null || o.getIdHorario() == null) {
            throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(o.toString()));
        }
    }

    /**
     * Valida se o horário já esta em uso em outra jornada
     *
     * @param o
     * @throws ServiceException
     */
    private void validarEmUso(Horario o) throws ServiceException {
        if (!(buscar(Horario.class, o.getIdHorario())).getJornadaHorarioList().isEmpty()) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.HORARIO.ALERTA_EXCLUIR.getResource()));
        }
    }
//</editor-fold>

    public Response buscarPorPaginacao(PaginacaoTransfer pt) throws ServiceException {
        try {
            List<Horario> horarioList = horarioDao.buscarPorPaginacao(pt);
            List<HorarioTransfer> horarioTransferList = new ArrayList<>();
            horarioList.stream().forEach(h -> {
                horarioTransferList.add(new HorarioTransfer(h));
            });

            return this.getTopPontoResponse().sucessoSalvar(horarioTransferList);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.ERRO_OPERACAO_NAO_REALIZADA));
        }
    }

    Horario buscar(Horario horario) throws ServiceException {
        horario = this.buscar(Horario.class, horario.getIdHorario());
        List<HorarioMarcacao> horarioMarcacaoList = horarioMarcacaoService.buscarPorHorario(horario.getIdHorario());
        
        horario.setHorarioMarcacaoList(horarioMarcacaoList);
        
        return horario;
    }

}
