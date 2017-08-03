package com.topdata.toppontoweb.services.funcionario.jornada;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.funcionario.FuncionarioJornadaDao;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoResponse;
import com.topdata.toppontoweb.services.jornada.HorarioMarcacaoService;
import com.topdata.toppontoweb.services.jornada.JornadaHorarioService;
import com.topdata.toppontoweb.services.jornada.JornadaService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
//</editor-fold>

/**
 * @version 1.0.0 data 13/12/2016
 * @since 1.0.0 data 13/12/2016
 *
 * @author juliano.ezequiel
 */
@Component
public class ValidacaoEntreJornadas {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private TopPontoResponse topPontoResponse;

    @Autowired
    private JornadaService jornadaService;

    @Autowired
    private JornadaHorarioService jornadaHorarioService;

    @Autowired
    private FuncionarioJornadaDao funcionarioJornadaDao;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="VARIAVEIS">
    private final SimpleDateFormat sdf;
    private Long totalDiasAnterior = 0l;
    private Long totalDiasPosterior = 0l;
    private int count;
    private int countAnterior;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="CONSTRUTOR">
    public ValidacaoEntreJornadas() {
        this.sdf = new SimpleDateFormat("dd/MM/yyyy");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="MÉTODOS">
    public void validar(FuncionarioJornada funcionarioJornada) throws ServiceException {
        try {
            FuncionarioJornada fj = (FuncionarioJornada) funcionarioJornada.clone();
            if (fj.getJornada() != null) {

                debugInit();//CONSOLE LOG

                JornadaHorario jornadaHorarioAnterior = buscarAnterior(fj);
                JornadaHorario jornadaHorarioPosterior = buscarPosterior(fj);

                System.out.println("-------" + CONSTANTES.ANSI_CYAN + " JORNADA ATUAL " + CONSTANTES.ANSI_RESET + "----------");

                Jornada jornadaAtual = jornadaService.buscar(Jornada.class, fj.getJornada().getIdJornada());
                fj.setSequenciaInicial(fj.getSequenciaInicial() < 0 ? 0 : fj.getSequenciaInicial());
                //busca a jornada horário atual, que deverá ser validado .
                JornadaHorario jornadaHorarioAtualUltima = getJornadaHorario(jornadaAtual.getJornadaHorarioList(), fj.getSequenciaInicial(), totalDiasPosterior, true);
                JornadaHorario jornadaHorarioAtualPrimeira = jornadaAtual.getJornadaHorarioList().size() > 0 ? jornadaAtual.getJornadaHorarioList().get(fj.getSequenciaInicial()) : null;

                //CONSOLE LOG
                debug(jornadaAtual.getDescricao(), fj.getDataInicio(), jornadaAtual.getJornadaHorarioList().size(), fj.getSequenciaInicial(), false);

                System.out.println("Diferença de dias ANTERIOR: " + totalDiasAnterior);
                System.out.println("Diferença de dias POSTERIOR: " + totalDiasPosterior);

                if (jornadaHorarioAnterior != null && jornadaHorarioAnterior.getIdSequencia() != null) {
                    System.out.println("Horário para validar A : h" + jornadaHorarioAnterior.getIdSequencia() + " e h" + fj.getSequenciaInicial());
                }

                if (jornadaHorarioPosterior != null && jornadaHorarioPosterior.getIdSequencia() != null && jornadaHorarioAtualUltima != null) {
                    System.out.println("Horário para validar B : h" + jornadaHorarioAtualUltima.getIdSequencia() + " e h" + jornadaHorarioPosterior.getIdSequencia());
                }

                System.out.println("--------------------------------\n");
                debugEnd();//CONSOLE LOG

                validarHorarios(jornadaHorarioPosterior, jornadaHorarioAnterior, jornadaHorarioAtualUltima, jornadaHorarioAtualPrimeira);

            }
        } catch (NoSuchElementException | CloneNotSupportedException ex) {
            Logger.getLogger(FuncionarioJornadaService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Buscas as JornadaHorario anterior a funcionarioJornada passado como
     * referencia
     *
     * @param funcionarioJornada
     * @return JornadaHorario anterior
     * @throws ServiceException
     */
    private JornadaHorario buscarAnterior(FuncionarioJornada funcionarioJornada) throws ServiceException {
        JornadaHorario jornadaHorarioAnterior = null;
        FuncionarioJornada funcionarioJornadaAnterior;
        try {
            totalDiasAnterior = 0l;

            List<FuncionarioJornada> listaAnterior = funcionarioJornadaDao.buscarAnterior(funcionarioJornada);
            //filtra o registro do mesmo id
            if (funcionarioJornada.getIdJornadaFuncionario() != null) {
                listaAnterior = listaAnterior.stream().filter(fj -> !Objects.equals(fj.getIdJornadaFuncionario(), funcionarioJornada.getIdJornadaFuncionario())).collect(Collectors.toList());
            }

            // ################################## ANTERIOR ############################################################
            System.out.println("-------" + CONSTANTES.ANSI_CYAN + " JORNADA ANTERIOR " + CONSTANTES.ANSI_RESET + "-------");

            //se existir jornada anterior, deverá verificar a data de fim da anterior e a data de inicio da atual
            if (!listaAnterior.isEmpty()) {

                funcionarioJornadaAnterior = listaAnterior.iterator().next();

                //busca a jornada anterior
                Jornada jornadaAnterior = jornadaService.buscar(Jornada.class, funcionarioJornadaAnterior.getJornada().getIdJornada());

                totalDiasAnterior = getDiferencaDias(funcionarioJornadaAnterior.getDataInicio(), funcionarioJornada.getDataInicio());

                debug(jornadaAnterior.getDescricao(), funcionarioJornadaAnterior.getDataInicio(), jornadaAnterior.getJornadaHorarioList().size(), funcionarioJornadaAnterior.getSequenciaInicial(), true);

                //busca a jornada horário , que deverá ser validado
                jornadaHorarioAnterior = getJornadaHorario(jornadaAnterior.getJornadaHorarioList(), funcionarioJornadaAnterior.getSequenciaInicial(), totalDiasAnterior, true);

            } else {
                System.out.println("Não possui jornada anterior");
                System.out.println("--------------------------------\n");
            }
        } catch (DaoException ex) {
            Logger.getLogger(ValidacaoEntreJornadas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return jornadaHorarioAnterior;
    }

    /**
     * Buscas as JornadaHorario posterior a funcionarioJornada passado como
     * referencia
     *
     * @param funcionarioJornada
     * @return JornadaHorario posterior ao fuincionarioJornada
     * @throws ServiceException
     */
    private JornadaHorario buscarPosterior(FuncionarioJornada funcionarioJornada) throws ServiceException {
        JornadaHorario jornadaHorarioPosterior = null;
        FuncionarioJornada funcionarioJornadaPosterior;
        try {

            totalDiasPosterior = 0l;
            List<FuncionarioJornada> listaPosterior = funcionarioJornadaDao.buscarPosterior(funcionarioJornada);

            //filtra o registro do mesmo id
            if (funcionarioJornada.getIdJornadaFuncionario() != null) {
                listaPosterior = listaPosterior.stream().filter(fj -> !Objects.equals(fj.getIdJornadaFuncionario(), funcionarioJornada.getIdJornadaFuncionario())).collect(Collectors.toList());
            }

            //############################## POSTERIOR ################################################################
            System.out.println("-------" + CONSTANTES.ANSI_CYAN + " JORNADA POSTERIOR " + CONSTANTES.ANSI_RESET + "------");

            //se existir jornadas posterior, deverá verificar a data de inicio da proxima jornada e inicio da atual
            if (!listaPosterior.isEmpty()) {
                funcionarioJornadaPosterior = listaPosterior.iterator().next();
                //busca a Jornada posterior
                Jornada jornadaPosterior = jornadaService.buscar(Jornada.class, funcionarioJornadaPosterior.getJornada().getIdJornada());

                //busca a primeira jornada horário da jornada posterior
                // posterior sempre verifica a sequência inicial
                if (jornadaPosterior != null && jornadaPosterior.getJornadaHorarioList() != null && jornadaPosterior.getJornadaHorarioList().size() > 0) {
                    //busca pela sequência inicial
                    jornadaHorarioPosterior = jornadaHorarioService.buscar(JornadaHorario.class, jornadaPosterior.getJornadaHorarioList().get(funcionarioJornadaPosterior.getSequenciaInicial()).getIdJornadaHorario());

                    totalDiasPosterior = getDiferencaDias(funcionarioJornada.getDataInicio(), funcionarioJornadaPosterior.getDataInicio());

                    debug(jornadaPosterior.getDescricao(), funcionarioJornadaPosterior.getDataInicio(), jornadaPosterior.getJornadaHorarioList().size(), funcionarioJornadaPosterior.getSequenciaInicial(), true);

                }
            } else {
                System.out.println("Não possui jornada posterior");
                System.out.println("--------------------------------\n");

            }
        } catch (DaoException ex) {
            Logger.getLogger(ValidacaoEntreJornadas.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return jornadaHorarioPosterior;
    }

    /**
     * Calcular qual é a jornada horario válido para o dia
     *
     * @param listJornadasHorarios lista contendo as jornadasHorarios
     * @param inicio sequencia de início
     * @param totalDias totals de dias qeu será validado
     * @param anterior caso necessite descontar um dia
     * @return JornadaHorario válido
     */
    public JornadaHorario getJornadaHorario(List<JornadaHorario> listJornadasHorarios, Integer inicio, Long totalDias, Boolean anterior) {

        int totalHorarios = listJornadasHorarios.size() - 1;
        int sequenciaInicial = inicio;

        try {
            //Identificação da sequência do dia
            count = sequenciaInicial;
            countAnterior = 0;
            if (totalDias > 0) {
                for (int i = 1; i <= totalDias; i++) {

                    if (i == totalDias) {
                        countAnterior = count;
                    }
                    if (count == totalHorarios) {
                        count = 0;
                    } else {
                        count++;
                    }
                }
            }

            if (anterior) {
                count = countAnterior;
            }

            return listJornadasHorarios.stream().filter(f1 -> f1.getIdSequencia() == count).findAny().get();

        } catch (NoSuchElementException ex) {
            return null;
        }
    }

    /**
     * Valida se as marcações entre último do jornadaHorarioAtual e o primeiro
     * do jornadaHorarioPosterior e se o último do jornadaHorarioAnterior e do
     * primeiro do jornadaHorarioAtual
     *
     * @param jornadaHorarioPosterior
     * @param jornadaHorarioAnterior
     * @param jornadaHorarioAtualUltima
     * @throws ServiceException
     */
    private void validarHorarios(JornadaHorario jornadaHorarioPosterior, JornadaHorario jornadaHorarioAnterior, JornadaHorario jornadaHorarioAtualUltima, JornadaHorario jornadaHorarioPrimeira) throws ServiceException {
        //########################## CALCULAR HORARIOS ###########################################
        try {
            //validação entre atual e posterior
            if ((jornadaHorarioPosterior != null && jornadaHorarioPosterior.getHorario() != null) && (jornadaHorarioAtualUltima != null && jornadaHorarioAtualUltima.getHorario() != null)) {
                jornadaHorarioService.validarInicioFimHorario(jornadaHorarioAtualUltima.getHorario(), jornadaHorarioPosterior.getHorario(), Boolean.FALSE);
            }
            //validação entre  anterior e atual
            if ((jornadaHorarioAnterior != null && jornadaHorarioAnterior.getHorario() != null) && (jornadaHorarioPrimeira != null && jornadaHorarioPrimeira.getHorario() != null)) {
                jornadaHorarioService.validarInicioFimHorario(jornadaHorarioAnterior.getHorario(), jornadaHorarioPrimeira.getHorario(), Boolean.FALSE);
            }
        } catch (ServiceException e) {
            throw new ServiceException(this.topPontoResponse.alertaValidacao(MSG.FUNCIONARIO_JORNADA.ALERTA_ENTRE_JORNADA.getResource()));
        }
    }

    /**
     * Calcula a diferença em dias entre duas datas
     *
     * @param d1
     * @param d2
     * @return
     */
    private Long getDiferencaDias(Date d1, Date d2) {
        LocalDateTime inicioJornadaAtual = LocalDateTime.ofInstant(new Date(d1.getTime()).toInstant(), ZoneId.systemDefault());
        LocalDateTime inicioProximaJornada = LocalDateTime.ofInstant(new Date(d2.getTime()).toInstant(), ZoneId.systemDefault());
        Duration duration = Duration.between(inicioJornadaAtual, inicioProximaJornada);
        return duration.toDays();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="DEBUG">
    private void debug(String descricao, Date dataInicio, int qtdDias, int seqInicial, boolean fim) {
        System.out.println("Jornada : " + descricao);
        System.out.println("Data início : " + sdf.format(dataInicio));
        System.out.println("Quantidade de dias : " + qtdDias);
        System.out.println("Sequência de início : " + seqInicial);
        if (fim) {
            System.out.println("--------------------------------\n");
        }
    }

    private void debugInit() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(CONSTANTES.ANSI_GREEN + " Início cálculo horários entre jornadas " + new Date(System.currentTimeMillis()) + CONSTANTES.ANSI_RESET);
        System.out.println("-------------------------------------------------------------------------------");
    }

    private void debugEnd() {
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println(CONSTANTES.ANSI_GREEN + " Fim cálculo horários entre jornadas " + CONSTANTES.ANSI_RESET);
        System.out.println("-------------------------------------------------------------------------------\n");

    }
    //</editor-fold>
}
