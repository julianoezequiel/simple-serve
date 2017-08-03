package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.entity.rep.Rep;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesList;
import com.topdata.toppontoweb.services.gerafrequencia.entity.marcacoes.MarcacoesTratadas;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 * Consulta as marcações importadas ou cadastradas para esse funcionário
 *
 * @author enio.junior
 */
public class FuncionarioMarcacoesService {

    public final static Logger LOGGER = LoggerFactory.getLogger(FuncionarioMarcacoesService.class.getName());

    private final Funcionario funcionarioAPI;
    private final List<Marcacoes> todasMarcacoesOriginaisList;
    private List<Marcacoes> todasMarcacoesValidasList;
    private final List<MarcacoesList> todasMarcacoesValidasJaTratadasPorDiaList = new ArrayList<>();
    private final Calculo calculo;
    private List<MarcacoesDia> marcacoesDiaList;
    private List<MarcacoesTratadas> marcacoesTratadasList;
    private Date diferenca = null;
    private Date horarioMarcacao;

    public FuncionarioMarcacoesService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
        this.todasMarcacoesOriginaisList = getTodasMarcacoesOriginaisList();
    }

    public List<MarcacoesDia> getMarcacoesDiaList() {
        return marcacoesDiaList;
    }

    public void setMarcacoesDiaList(List<MarcacoesDia> marcacoesDiaList) {
        this.marcacoesDiaList = marcacoesDiaList;
    }

    /**
     * Retorna todas as marcações desse funcionário Filtro 1 Status != 3 and 5
     * 1-Original (Registro importado) 2-Incluída (Manualmente) 3-Desconsiderada
     * (Manualmente – excluída da importação) 4-Pré-assinalada (Rotina
     * automática) 5-Excluída (Manualmente) Já eliminando as marcações
     * desconsideradas e excluídas
     *
     * @return
     */
    public final List<Marcacoes> getTodasMarcacoesOriginaisList() {
        if (this.funcionarioAPI.getMarcacoesList() != null) {
            return this.funcionarioAPI.getMarcacoesList().stream()
                    .filter(f -> (f.getIdStatusMarcacao() != null && !f.getIdStatusMarcacao().getDescricao().equals(CONSTANTES.MARCACAO_EXCLUIDA)))
                    .sorted(Comparator.comparing(Marcacoes::getDataHora))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * Consultar somente as marcações desconsideradas Complemento para gerar
     * AFDT
     *
     * @param dia
     * @return
     */
    public List<Marcacoes> getMarcacoesDesconsideradas(Date dia) {
        if (this.funcionarioAPI.getMarcacoesList() != null) {
            return this.funcionarioAPI.getMarcacoesList().stream()
                    .filter(f -> (f.getDataHora() != null && Utils.FormatoData(f.getDataHora()).equals(Utils.FormatoData(dia))))
                    .filter(f -> (f.getIdStatusMarcacao() != null && f.getIdStatusMarcacao().getDescricao().equals(CONSTANTES.MARCACAO_DESCONSIDERADA)))
                    .sorted(Comparator.comparing(Marcacoes::getDataHora))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Date getInicioMarcacoesList() {
        return this.todasMarcacoesOriginaisList.stream()
                .sorted(Comparator.comparing(Marcacoes::getDataHora))
                .map(f -> f.getDataHora()).findFirst().orElse(null);
    }

    private Date getTerminoMarcacoesList() {
        return this.todasMarcacoesOriginaisList.stream()
                .sorted(Comparator.comparing(Marcacoes::getDataHora).reversed())
                .map(f -> f.getDataHora()).findFirst().orElse(null);
    }

    public List<MarcacoesDia> getMarcacoesOriginaisDia(Date dia) {
        AtomicInteger contadorMarcacoes = new AtomicInteger(0);
        AtomicInteger contadorSequencia = new AtomicInteger(0);
        this.marcacoesDiaList = new ArrayList<>();

        if (this.todasMarcacoesOriginaisList.size() > 0) {
            this.todasMarcacoesOriginaisList.stream()
                    .filter(f -> (f.getDataHora() != null && Utils.FormatoData(f.getDataHora()).equals(Utils.FormatoData(dia))))
                    .sorted(Comparator.comparing(Marcacoes::getDataHora))
                    .collect(Collectors.toList()).forEach((Marcacoes marcacoes) -> {

                this.horarioMarcacao = Utils.getViradaDia(getVerificaDia(marcacoes), marcacoes.getDataHora());
                setMarcacoesJornadaLivre(marcacoes, contadorMarcacoes, contadorSequencia);
            });
        }
        return this.marcacoesDiaList;
    }

    public synchronized List<MarcacoesDia> getMarcacoesJornadaDia(Date dia) {
        Date diaMarcacao = new Date(dia.getTime());
        calculo.getFuncionarioService().getFuncionarioJornadaService().setHorarioJornada();
        Jornada jornadaFuncionario = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada();

        //Flag mostrar marcações no dia seguinte para jornada com virada de dia?
        //Somente para os horários da jornada com virada de dia
        if (jornadaFuncionario.getIdJornada() != null
                && this.calculo.getConfiguracoesGerais().getMarcacoesDiaSeguinteViradaDia() != null) {
            if (this.calculo.getFuncionarioService().getFuncionarioJornadaService() != null
                    && this.calculo.getConfiguracoesGerais().getMarcacoesDiaSeguinteViradaDia()
                    && this.calculo.getRegrasService().isViradaDiaJornada()) {
                diaMarcacao = Utils.DiminuiDiasData(diaMarcacao, 1);
            }
        }
        return this.todasMarcacoesValidasJaTratadasPorDiaList.stream()
                .filter(new consultaMarcacoesDia(diaMarcacao))
                .map(f -> f.getMarcacaoList()).findAny().orElse(new ArrayList<>());

    }

    private class consultaMarcacoesDia implements Predicate<MarcacoesList> {

        private final Date diaConsulta;

        public consultaMarcacoesDia(Date diaConsulta) {
            this.diaConsulta = diaConsulta;
        }

        @Override
        public boolean test(MarcacoesList f) {
            return Utils.FormatoData(f.getDia()).equals(Utils.FormatoData(diaConsulta));
        }
    }

    public synchronized void setMarcacoesValidasList() {
        //Loop do início até o fim da lista
        //Gravando as marcações somente do período que o funcionário está ativo 
        Date dia = getInicioMarcacoesList();
        Date dataTermino = getTerminoMarcacoesList();

        LOGGER.debug("Inicio das marcações validas : {} - Término das marcações válidas : {}",
                dia, dataTermino);

        if (dia != null) {
            while ((dia.before(dataTermino)) || (dia.equals(dataTermino))) {

                //Para cada dia
                this.calculo.setDiaProcessado(dia);
                if (this.calculo.getRegrasService().isFuncionarioAtivo(dia)) {
                    MarcacoesList marcacoes = new MarcacoesList();
                    marcacoes.setDia(dia);
                    marcacoes.setMarcacaoList(getMarcacoesValidasDia(dia));

                    //Gravar na lista de marcações válidas o dia e a lista de marcações já tratadas desse dia
                    this.todasMarcacoesValidasJaTratadasPorDiaList.add(marcacoes);
                }

                dia = Utils.AdicionaDiasData(dia, 1);
            }
        }
    }

    /**
     * Esse método deverá retornar somente as marcações válidas do dia
     * Realizando os encaixes Já organizando as sequências de entrada e saída
     * Deverá ter um controle das marcações que já foram alocadas para o dia,
     * para não correr o risco de utilizar a mesma marcação mais de uma vez.
     *
     * @param dia
     * @return
     */
    private List<MarcacoesDia> getMarcacoesValidasDia(Date dia) {
        setSomenteMarcacoesValidas();
        setRealizaEncaixes(dia);
        return getConsultaMarcacoesDia(dia);
    }

    private void setSomenteMarcacoesValidas() {
        this.todasMarcacoesValidasList = this.todasMarcacoesOriginaisList.stream()
                .filter(f -> {
                    if (!this.calculo.getEntradaAPI().isProcessaManutencaoMarcacoes()) {
                        return f.getIdStatusMarcacao() != null && !f.getIdStatusMarcacao().getDescricao().equals(CONSTANTES.MARCACAO_DESCONSIDERADA);
                    } else {
                        return true;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * Filtro 2 Encaixe 0-Não definido (Registro importado) 1-Anterior
     * (Manualmente) 2-Atual 3-Seguinte (Manualmente)
     *
     * @param dia
     */
    private void setRealizaEncaixes(Date dia) {
        //Consultar as marcações do dia, anterior e posterior            
        Date anterior = Utils.AdicionaDiasData(dia, -1);
        Date seguinte = Utils.AdicionaDiasData(dia, 1);

        if (this.todasMarcacoesValidasList.size() > 0) {
            this.todasMarcacoesValidasList.stream()
                    //Marcações do dia, anterior e posterior
                    .filter(f -> ((f.getDataHora().getDate() == dia.getDate())
                            || (f.getDataHora().getDate() == anterior.getDate())
                            || (f.getDataHora().getDate() == seguinte.getDate())))
                    .filter(f -> (f.getDataEncaixeDia() == null))
                    .collect(Collectors.toList()).forEach((Marcacoes marcacoes) -> {
                if (!setEncaixeDiaAnterior(marcacoes, anterior, dia)) {
                    if (!setEncaixeDiaAtual(marcacoes, anterior, dia, seguinte)) {
                        setEncaixeDiaSeguinte(marcacoes, dia, seguinte);
                    }
                }
            });
        }
    }

    private boolean setEncaixeDiaAnterior(Marcacoes marcacoes, Date anterior, Date dia) {
        boolean resultadoEncaixe = false;
        if (Utils.FormatoData(marcacoes.getDataHora()).equals(Utils.FormatoData(anterior)) && marcacoes.getIdEncaixeMarcacao() != null) {
            //Não definido
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_NAO_DEFINIDO)
                    || marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_ATUAL)) {
                marcacoes.setDataEncaixeDia(anterior);
                resultadoEncaixe = true;
            }
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_SEGUINTE)) {
                marcacoes.setDataEncaixeDia(dia);
                resultadoEncaixe = true;
            }
        }
        return resultadoEncaixe;
    }

    private boolean setEncaixeDiaAtual(Marcacoes marcacoes, Date anterior, Date dia, Date seguinte) {
        if (Utils.FormatoData(marcacoes.getDataHora()).equals(Utils.FormatoData(dia)) && marcacoes.getIdEncaixeMarcacao() != null) {
            //Não definido
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_NAO_DEFINIDO)
                    || marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_ATUAL)) {
                marcacoes.setDataEncaixeDia(dia);
                return true;
            }
            //Dia anterior
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_ANTERIOR)) {
                marcacoes.setDataEncaixeDia(anterior);
                return true;
            }
            //Próximo dia
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_SEGUINTE)) {
                marcacoes.setDataEncaixeDia(seguinte);
                return true;
            }
        }
        return false;
    }

    private void setEncaixeDiaSeguinte(Marcacoes marcacoes, Date dia, Date seguinte) {
        if (Utils.FormatoData(marcacoes.getDataHora()).equals(Utils.FormatoData(seguinte)) && marcacoes.getIdEncaixeMarcacao() != null) {
            //Não definido
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_NAO_DEFINIDO)
                    || marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_ATUAL)) {
                marcacoes.setDataEncaixeDia(seguinte);
            }
            //Dia atual
            if (marcacoes.getIdEncaixeMarcacao().getDescricao().equals(CONSTANTES.ENCAIXE_ANTERIOR)) {
                marcacoes.setDataEncaixeDia(dia);
            }
        }
    }

    /**
     * São as marcacoes referentes a esse dia (com dia seguinte ou não) de
     * acordo com os encaixes realizados É a data do encaixe que informa se a
     * marcação do dia anterior ou seguinte faz parte desse dia Ordenando por
     * data e hora
     *
     * @param dia
     * @return
     */
    private List<MarcacoesDia> getConsultaMarcacoesDia(Date dia) {
        AtomicInteger contadorMarcacoes = new AtomicInteger(0);
        AtomicInteger contadorSequencia = new AtomicInteger(0);
        AtomicInteger contadorEntrada = new AtomicInteger(0);
        this.marcacoesDiaList = new ArrayList<>();
        this.marcacoesTratadasList = new ArrayList<>();
        calculo.getFuncionarioService().getFuncionarioJornadaService().setHorarioJornada();
        Jornada jornadaFuncionario = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada();

        Integer qtdeMarcacoesTrabalhadas = 0;
        if (this.todasMarcacoesValidasList.size() > 0) {
            List<Marcacoes> marcacoesListFiltrada = this.todasMarcacoesValidasList.stream()
                    .filter(f -> (f.getDataEncaixeDia() != null && Utils.FormatoData(f.getDataEncaixeDia()).equals(Utils.FormatoData(dia))))
                    .sorted(Comparator.comparing(Marcacoes::getDataHora))
                    .collect(Collectors.toList());
            qtdeMarcacoesTrabalhadas = marcacoesListFiltrada.size();
            marcacoesListFiltrada
                    .stream()
                    .forEach(new DefineMarcacoesEntradaSaida(dia, jornadaFuncionario, contadorMarcacoes, contadorSequencia, contadorEntrada));
        }

        if (this.calculo.getEntradaAPI().isProcessaManutencaoMarcacoes()) {
            setMarcacoesQueFaltam(qtdeMarcacoesTrabalhadas, contadorSequencia);
        }

        return this.marcacoesDiaList;
    }

    public void setMarcacoesQueFaltam(Integer qtdeMarcacoesTrabalhadas, AtomicInteger contadorSequencia) {
        if (this.calculo.getRegrasService().isHorarioCumprir()) {

            //verificar a quantidade de marcações da jornada do dia
            Integer qtdeMarcacoesJornadaDia = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorariosJornadaDia().size() * 2;

            //verificar quantas marcações faltam
            Integer qtdeMarcacoesFaltando;
            if (qtdeMarcacoesJornadaDia > qtdeMarcacoesTrabalhadas) {
                qtdeMarcacoesFaltando = qtdeMarcacoesJornadaDia - qtdeMarcacoesTrabalhadas;
            } else {
                qtdeMarcacoesFaltando = qtdeMarcacoesTrabalhadas - qtdeMarcacoesJornadaDia;
            }

            if (qtdeMarcacoesFaltando > 0) {

                while (qtdeMarcacoesFaltando > 0) {
                    if ((qtdeMarcacoesFaltando % 2) == 0) {
                        MarcacoesDia marcacao = new MarcacoesDia();
                        marcacao.setIdSequencia(contadorSequencia.get());
                        marcacao.setHorarioEntrada(null);
                        marcacao.setMotivoStatusEntrada("");
                        marcacao.setIdMarcacaoEntrada(null);
                        marcacao.setStatusEntrada(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADA);
                        marcacao.setStatusMarcacaoEntrada(new StatusMarcacao());
                        marcacao.setNumeroSerieEntrada("");
                        this.marcacoesDiaList.add(marcacao);
                    } else {
                        this.marcacoesDiaList.get(contadorSequencia.get()).setStatusSaida(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDA);
                        this.marcacoesDiaList.get(contadorSequencia.get()).setHorarioSaida(null);
                        this.marcacoesDiaList.get(contadorSequencia.get()).setStatusMarcacaoSaida(new StatusMarcacao());
                        this.marcacoesDiaList.get(contadorSequencia.get()).setNumeroSerieSaida("");
                        this.marcacoesDiaList.get(contadorSequencia.getAndIncrement()).setIdMarcacaoSaida(null);
                    }
                    qtdeMarcacoesFaltando--;
                }
            }
        }
    }

    private class DefineMarcacoesEntradaSaida implements Consumer<Marcacoes> {

        private final Date dia;
        private Jornada jornadaFuncionario;
        private final AtomicInteger contadorMarcacoes;
        private final AtomicInteger contadorSequencia;
        private final AtomicInteger contadorEntrada;

        public DefineMarcacoesEntradaSaida(Date dia, Jornada jornadaFuncionario, AtomicInteger contadorMarcacoes, AtomicInteger contadorSequencia, AtomicInteger contadorEntrada) {
            this.dia = dia;
            this.jornadaFuncionario = jornadaFuncionario;
            this.contadorMarcacoes = contadorMarcacoes;
            this.contadorSequencia = contadorSequencia;
            this.contadorEntrada = contadorEntrada;
        }

        @Override
        public void accept(Marcacoes marcacoes) {
            horarioMarcacao = Utils.getDataPadrao(getVerificaDia(marcacoes), marcacoes.getDataHora());
            calculo.setDiaProcessado(dia);
            calculo.getFuncionarioService().getFuncionarioJornadaService().setHorarioJornada();
            jornadaFuncionario = calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada();
            if (jornadaFuncionario != null && jornadaFuncionario.getTipoJornada() == null
                    || jornadaFuncionario.getTipoJornada().getDescricao().equals(CONSTANTES.TIPOJORNADA_LIVRE)) {
                setMarcacoesJornadaLivre(marcacoes, contadorMarcacoes, contadorSequencia);
            } else if ((contadorMarcacoes.getAndIncrement() % 2) == 0) {
                setMarcacoesEntradaJornada(marcacoes, contadorEntrada, contadorSequencia);
            } else {
                setMarcacoesSaidaJornada(marcacoes, contadorEntrada, contadorSequencia);
            }
        }
    }

    /**
     * Encaixe 1-Anterior (Manualmente) 2-Atual 3-Seguinte (Manualmente)
     *
     * @param marcacoes
     * @return
     */
    private int getVerificaDia(Marcacoes marcacoes) {
        return Utils.getDiferencaData(marcacoes.getDataHora(), marcacoes.getDataEncaixeDia());
    }

    private String configurarSerieCompleta(Marcacoes marcacoes) {
        String serieCompleta = "00000000000000000";
        Rep rep = marcacoes.getRep();
        if (rep.getTipoEquipamento().getIdTipoEquipamento()
                != com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_EQUIPAMENTO.OUTRO.ordinal()) {

            if (rep.getTipoEquipamento().getIdTipoEquipamento()
                    == com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_TIPO_EQUIPAMENTO.INNER.ordinal()) {
                serieCompleta = com.topdata.toppontoweb.utils.Utils.corrigePrecisaoNumero(rep.getNumeroSerie(), 17);
            } else {
                serieCompleta = rep.getNumeroFabricante();
                serieCompleta += rep.getNumeroModelo();
                serieCompleta += com.topdata.toppontoweb.utils.Utils.corrigePrecisaoNumero(rep.getNumeroSerie(), 7);
            }
        }
        return serieCompleta;
    }

    private void setMarcacoesJornadaLivre(Marcacoes marcacoes, AtomicInteger contadorMarcacoes, AtomicInteger contadorSequencia) {
        String serieCompleta = configurarSerieCompleta(marcacoes);

        if ((contadorMarcacoes.getAndIncrement() % 2) == 0) {
            MarcacoesDia marcacao = new MarcacoesDia();
            marcacao.setIdSequencia(contadorSequencia.get());
            marcacao.setStatusEntrada(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADA);
            marcacao.setHorarioEntrada(this.horarioMarcacao);
            marcacao.setMotivoStatusEntrada(getMotivo(marcacoes));
            marcacao.setStatusMarcacaoEntrada(marcacoes.getIdStatusMarcacao());
            marcacao.setNumeroSerieEntrada(serieCompleta);
            marcacao.setIdMarcacaoEntrada(marcacoes.getIdMarcacao());            
            this.marcacoesDiaList.add(marcacao);
        } else {
            this.marcacoesDiaList.get(contadorSequencia.get()).setStatusSaida(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDA);
            this.marcacoesDiaList.get(contadorSequencia.get()).setHorarioSaida(horarioMarcacao);
            this.marcacoesDiaList.get(contadorSequencia.get()).setMotivoStatusSaida(getMotivo(marcacoes));
            this.marcacoesDiaList.get(contadorSequencia.get()).setStatusMarcacaoSaida(marcacoes.getIdStatusMarcacao());
            this.marcacoesDiaList.get(contadorSequencia.get()).setNumeroSerieSaida(serieCompleta);
            this.marcacoesDiaList.get(contadorSequencia.getAndIncrement()).setIdMarcacaoSaida(marcacoes.getIdMarcacao());
        }
    }

    private String getMotivo(Marcacoes marcacoes) {
        String descricaoStatus = marcacoes.getIdStatusMarcacao().getDescricao();
        String retorno;
        String descricaoMotivo = "";
        if (marcacoes.getMotivo() != null && marcacoes.getMotivo().getDescricao() != null) {
            descricaoMotivo = marcacoes.getMotivo().getDescricao();
        }

        if (!descricaoStatus.equals(CONSTANTES.MARCACAO_ORIGINAL)
                && !descricaoStatus.equals(CONSTANTES.MARCACAO_EXCLUIDA)) {
            MarcacoesTratadas marcacoesTratadas = new MarcacoesTratadas();
            marcacoesTratadas.setHorarioMarcacao(horarioMarcacao);
            marcacoesTratadas.setDescricaoMotivo(descricaoMotivo);

            if (CONSTANTES.MARCACAO_INCLUIDA.equals(descricaoStatus)) {
                marcacoesTratadas.setIdLegenda(CONSTANTES.LEGENDA_INCLUIDA);
                retorno = CONSTANTES.LEGENDA_INCLUIDA + " - " + descricaoMotivo;
            } else if (CONSTANTES.MARCACAO_DESCONSIDERADA.equals(descricaoStatus)) {
                marcacoesTratadas.setIdLegenda(CONSTANTES.LEGENDA_DESCONSIDERADA);
                retorno = CONSTANTES.LEGENDA_DESCONSIDERADA + " - " + descricaoMotivo;
            } else if (CONSTANTES.MARCACAO_PREASSINALADA.equals(descricaoStatus)) {
                marcacoesTratadas.setIdLegenda(CONSTANTES.LEGENDA_PREASSINALADA);
                retorno = CONSTANTES.LEGENDA_PREASSINALADA + " - " + descricaoMotivo;
            } else {
                marcacoesTratadas.setIdLegenda("");
                retorno = "";
            }

            this.marcacoesTratadasList.add(marcacoesTratadas);
        } else {
            retorno = "";
        }

        return retorno;
    }

    /**
     * Filtro 3 Verifica se a marcação está dentro do limite da jornada Para
     * cada sequência: (marcação de entrada e saída) Exemplo Sequência 1
     * marcação = sequência 1 da jornada Sequência da marcação é a mesma da
     * jornada
     *
     * @param marcacoes
     * @param contadorEntrada
     * @param contadorSequencia
     */
    private synchronized void setMarcacoesEntradaJornada(Marcacoes marcacoes, AtomicInteger contadorEntrada, AtomicInteger contadorSequencia) {
        MarcacoesDia marcacao = new MarcacoesDia();
        marcacao.setHorarioEntrada(this.horarioMarcacao);
        marcacao.setMotivoStatusEntrada(getMotivo(marcacoes));
        marcacao.setStatusMarcacaoEntrada(marcacoes.getIdStatusMarcacao());
        marcacao.setNumeroSerieEntrada(configurarSerieCompleta(marcacoes));
        marcacao.setIdMarcacaoEntrada(marcacoes.getIdMarcacao());
        List<HorarioMarcacao> horariosJornadaDiaList = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorariosJornadaDia();

        if (contadorSequencia.get() < horariosJornadaDiaList.size()) {
            if (horariosJornadaDiaList.get(contadorSequencia.get()) != null) {
                //Se entrada dentro do período
                this.diferenca = Utils.getIntervaloHoras(horariosJornadaDiaList.get(contadorSequencia.get()).getHorarioEntrada(), this.horarioMarcacao);
                if (this.diferenca.getTime() <= this.calculo.getConfiguracoesGerais().getLimiteCorteEntrada().getTime()) {
                    marcacao.setIdSequencia(horariosJornadaDiaList.get(contadorSequencia.get()).getIdSequencia());
                    marcacao.setStatusEntrada(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADA);
                }
            }
        } else {
            marcacao.setIdSequencia(contadorSequencia.get());
            marcacao.setStatusEntrada(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADAEXTRA);
        }

        this.marcacoesDiaList.add(marcacao);
        contadorEntrada.incrementAndGet();
    }

    private synchronized void setMarcacoesSaidaJornada(Marcacoes marcacoes, AtomicInteger contadorEntrada, AtomicInteger contadorSequencia) {
        //Senão é marcação de saída   
        if (this.marcacoesDiaList.size() > 0) {
            int contador = contadorEntrada.get() - 1;
            this.marcacoesDiaList.get(contador).setHorarioSaida(this.horarioMarcacao);
            this.marcacoesDiaList.get(contador).setMotivoStatusSaida(getMotivo(marcacoes));
            this.marcacoesDiaList.get(contador).setStatusMarcacaoSaida(marcacoes.getIdStatusMarcacao());
            this.marcacoesDiaList.get(contador).setNumeroSerieSaida(configurarSerieCompleta(marcacoes));
            this.marcacoesDiaList.get(contador).setIdMarcacaoSaida(marcacoes.getIdMarcacao());

            List<HorarioMarcacao> horariosJornadaDiaList = this.calculo.getFuncionarioService().getFuncionarioJornadaService().getHorariosJornadaDia();

            if (contadorSequencia.get() < horariosJornadaDiaList.size()) {
                for (int i = contadorSequencia.getAndIncrement(); i < horariosJornadaDiaList.size(); i++) {
                    this.diferenca = Utils.getIntervaloHoras(horariosJornadaDiaList.get(i).getHorarioSaida(), this.horarioMarcacao);
                    if (this.diferenca.getTime() <= this.calculo.getConfiguracoesGerais().getLimiteCorteSaida().getTime()) {
                        this.marcacoesDiaList.get(contador).setStatusSaida(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDA);
                        break;
                    } else {
                        this.marcacoesDiaList.get(contador).setStatusSaida(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDAEXTRA);
                    }
                }
            } else {
                this.marcacoesDiaList.get(contador).setStatusSaida(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDAEXTRA);
            }
        }
    }

    public boolean getMarcacoesImpares() {
        List<MarcacoesDia> marcacoesDias = getMarcacoesJornadaDia(this.calculo.getDiaProcessado());
        if (marcacoesDias != null && marcacoesDias
                .stream()
                .mapToInt(f -> f.getIdSequencia()).sum() > 0) {

            if (marcacoesDias
                    .stream()
                    .filter(f -> f.getHorarioEntrada() == null || f.getHorarioSaida() == null)
                    .mapToInt(f -> f.getIdSequencia()).sum() > 0) {
                return true;
            }
        } else {
            return getMarcacoesImparesOriginaisDia(this.calculo.getDiaProcessado());
        }
        return false;
    }

    public boolean getMarcacaoExtra() {
        List<MarcacoesDia> marcacoesDias = getMarcacoesJornadaDia(this.calculo.getDiaProcessado());
        return marcacoesDias != null && marcacoesDias
                .stream()
                .filter(f -> f.getStatusEntrada().equals(CONSTANTES.LEGENDA_MARCACAO_STATUS_ENTRADAEXTRA)
                        || f.getStatusSaida().equals(CONSTANTES.LEGENDA_MARCACAO_STATUS_SAIDAEXTRA))
                .mapToInt(f -> f.getIdSequencia()).count() > 0;
    }

    public boolean getMarcacoesImparesOriginaisDia(Date dia) {
        return getMarcacoesOriginaisDia(this.calculo.getDiaProcessado()) != null
                && getMarcacoesOriginaisDia(this.calculo.getDiaProcessado())
                .stream()
                .filter(f -> f.getHorarioEntrada() == null || f.getHorarioSaida() == null)
                .mapToInt(f -> f.getIdSequencia()).sum() > 0;
    }

    public List<MarcacoesTratadas> getMarcacoesTratadasList(Date dia) {
        return this.marcacoesTratadasList
                .stream()
                .filter(f -> (f.getHorarioMarcacao() != null && Utils.FormatoData(f.getHorarioMarcacao()).equals(Utils.FormatoData(dia))))
                .sorted(Comparator.comparing(MarcacoesTratadas::getHorarioMarcacao))
                .collect(Collectors.toList());
    }

}
