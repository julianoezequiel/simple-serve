package com.topdata.toppontoweb.services.gerafrequencia.main;

import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.bancohoras.BancoHorasLimite;
import com.topdata.toppontoweb.entity.calendario.Calendario;
import com.topdata.toppontoweb.entity.calendario.CalendarioFeriado;
import com.topdata.toppontoweb.entity.configuracoes.Dsr;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR;
import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.entity.funcionario.Afastamento;
import com.topdata.toppontoweb.entity.funcionario.Coletivo;
import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioCalendario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDiaNaoDescontaDSR;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioJornada;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.entity.jornada.TipoJornada;
import com.topdata.toppontoweb.entity.marcacoes.EncaixeMarcacao;
import com.topdata.toppontoweb.entity.marcacoes.Marcacoes;
import com.topdata.toppontoweb.entity.marcacoes.StatusMarcacao;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.DadosEntrada;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Uso somente da função main
 * @author enio.junior
 */
public class EntradaMain {

    private final EntradaApi entrada;
    private DadosEntrada dadosEntrada;

    public EntradaMain() {
        entrada = new EntradaApi();
    }

    public EntradaApi getEntrada() {
        return entrada;
    }

    public void setInicializa(DadosEntrada dadosEntrada) {
        this.dadosEntrada = dadosEntrada;
        inicializaFuncionario();
        inicializaDepartamento();
        inicializaTiposDias();
        inicializaTiposJornadas();
        inicializaConfiguracoesGerais();
        inicializaEmpresaFechamentoData();
        inicializaFuncionarioJornada();
        inicializaFuncionarioMarcacoes();
        inicializaFuncionarioCalendario();
        inicializaFuncionarioAfastamento();
        inicializaFuncionarioCompensacao();
        inicializaFuncionarioAbono();
        inicializaFuncionarioBancoHoras();
        inicializaFuncionarioBancoHorasFechamento();
        inicializaFuncionarioDiaNaoDescontaDSR();
        inicializaDSR();
    }

    public void setDataInicioPeriodo(String dataInicioPeriodo) {
        entrada.setDataInicioPeriodo(new Date(dataInicioPeriodo));
    }

    public void setDataFimPeriodo(String dataFimPeriodo) {
        entrada.setDataFimPeriodo(new Date(dataFimPeriodo));
    }

    private void inicializaFuncionario() {
        entrada.setFuncionario(new Funcionario());
        entrada.getFuncionario().setDataAdmissao(dadosEntrada.getAdmissaoData());
        if (dadosEntrada.getDemissaoData() != null && !"".equals(dadosEntrada.getDemissaoData().toString())) {
            entrada.getFuncionario().setDataDemissao(dadosEntrada.getDemissaoData());
        }
    }

    private void inicializaTiposDias() {
        entrada.setTipoDiaList(new ArrayList<>());
        entrada.getTipoDiaList().add(new TipoDia(1, CONSTANTES.TIPODIA_NORMAL));
        entrada.getTipoDiaList().add(new TipoDia(6, CONSTANTES.TIPODIA_COMPENSADO));
        entrada.getTipoDiaList().add(new TipoDia(7, CONSTANTES.TIPODIA_HORAS_NOTURNAS));
    }
    
    private void inicializaTiposJornadas() {
        entrada.setTipoJornadaList(new ArrayList<>());
        entrada.getTipoJornadaList().add(new TipoJornada(1, CONSTANTES.TIPOJORNADA_SEMANAL));
        entrada.getTipoJornadaList().add(new TipoJornada(2, CONSTANTES.TIPOJORNADA_VARIAVEL));
        entrada.getTipoJornadaList().add(new TipoJornada(3, CONSTANTES.TIPOJORNADA_LIVRE));
    }
    
    private void inicializaConfiguracoesGerais() {
        entrada.getConfiguracoesGerais().setLimiteCorteEntrada(dadosEntrada.getLimiteCorteEntrada());
        entrada.getConfiguracoesGerais().setLimiteCorteSaida(dadosEntrada.getLimiteCorteSaida());
        entrada.getConfiguracoesGerais().setMarcacoesDiaSeguinteViradaDia(dadosEntrada.isMarcacoesDiaSeguinteViradaDia());
    }

    private void inicializaDepartamento() {
        entrada.getFuncionario().setDepartamento(new Departamento(Integer.MIN_VALUE, "Departamento 1",
                new Empresa(Integer.BYTES, "nomeFantasia", "endereco", "bairro", "cep", "cidade", "uf",
                        "fone", "fax", "observacao")));
    }

    private void inicializaEmpresaFechamentoData() {
        List<EmpresaFechamentoData> fechamentoDataList = new ArrayList<>();
        EmpresaFechamentoData efd = new EmpresaFechamentoData();
        efd.setData(dadosEntrada.getDataFechamento());
        efd.setIdEmpresaFechamentoData(1);
        efd.setIdFuncionario(entrada.getFuncionario());
        efd.setIdFuncionarioBancoHoras(new FuncionarioBancoHoras());
        efd.setPossuiHorasPrevistas(Boolean.TRUE);
        efd.setHorasPrevistasDiurnas(Utils.setDatasMain(1, 0));
        efd.setHorasPrevistasNoturnas(Utils.setDatasMain(1, 0));
        efd.setHorasPrevistasDiferencaAdicionalNoturno(Utils.setDatasMain(1, 0));
        efd.setPossuiHorasTrabalhadas(Boolean.TRUE);
        efd.setHorasTrabalhadasDiurnas(Utils.setDatasMain(1, 0));
        efd.setHorasTrabalhadasNoturnas(Utils.setDatasMain(1, 0));
        efd.setHorasTrabalhadasDiferencaAdicionalNoturno(Utils.setDatasMain(1, 0));
        efd.setPossuiAfastadoComAbono(Boolean.FALSE);
        efd.setPossuiAusencias(Boolean.TRUE);
        efd.setPossuiAusenciasAbonadas(Boolean.TRUE);
        efd.setPossuiAusenciasCompensadas(Boolean.FALSE);
        efd.setPossuiExtras(Boolean.FALSE);
        efd.setPossuiExtrasCompensacao(Boolean.FALSE);
        efd.setPossuiNormais(Boolean.FALSE);
        efd.setPossuiTodasComoExtras(Boolean.FALSE);
        efd.setSaldoAusenciasAbonadasDiurnas(Utils.setDatasMain(0, 20));
        efd.setSaldoAusenciasAbonadasNoturnas(Utils.setDatasMain(0, 30));
        efd.setSaldoAusenciasCompensadasDiurnas(Utils.setDatasMain(1, 0));
        efd.setSaldoAusenciasCompensadasNoturnas(Utils.setDatasMain(1, 0));
        efd.setSaldoAusenciasDiurnas(Utils.setDatasMain(1, 0));
        efd.setSaldoAusenciasNoturnas(Utils.setDatasMain(1, 0));
        efd.setSaldoExtrasCompensacaoDiurnas(Utils.setDatasMain(0, 0));
        efd.setSaldoExtrasCompensacaoNoturnas(Utils.setDatasMain(1, 0));
        efd.setSaldoExtrasDiferencaAdicionalNoturno(Utils.setDatasMain(1, 0));
        efd.setSaldoExtrasDiurnas(Utils.setDatasMain(1, 0));
        efd.setSaldoExtrasNoturnas(Utils.setDatasMain(1, 0));
        efd.setSaldoNormaisDiurnas(Utils.setDatasMain(1, 0));
        efd.setSaldoNormaisNoturnas(Utils.setDatasMain(1, 0));
        efd.setBHCredito(Utils.setDatasMain(1, 0));
        efd.setBHDebito(Utils.setDatasMain(1, 0));
        efd.setBHDiferencaAdicionalNoturno(Utils.setDatasMain(1, 0));
        efd.setBHGatilho(Utils.setDatasMain(1, 0));
        efd.setBHSaldoAcumuladoDia(Utils.setDatasMain(1, 0));
        efd.setBHSaldoAcumuladoDiaAnterior(Utils.setDatasMain(1, 0));
        efd.setBHSaldoCredito(Utils.setDatasMain(1, 0));
        efd.setBHSaldoDebito(Utils.setDatasMain(1, 0));
        efd.setBHSaldoDia(Utils.setDatasMain(1, 0));

        FechamentoBHTabelaAcrescimos fechamentoBHTabelaAcrescimos = new FechamentoBHTabelaAcrescimos();
        fechamentoBHTabelaAcrescimos.setDivisaoDiurnas(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaAcrescimos.setDivisaoNoturnas(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaAcrescimos.setHoras(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaAcrescimos.setIdEmpresaFechamentoData(efd);
        fechamentoBHTabelaAcrescimos.setLimite(1);
        fechamentoBHTabelaAcrescimos.setPercentual(1.0);
        fechamentoBHTabelaAcrescimos.setResultadoDiurnas(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaAcrescimos.setResultadoNoturnas(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaAcrescimos.setTipodeDia(entrada.getTipoDiaList().get(0).getDescricao());        
        efd.setFechamentoBHTabelaAcrescimosList(new ArrayList<>());
        efd.getFechamentoBHTabelaAcrescimosList().add(fechamentoBHTabelaAcrescimos);

        FechamentoBHTabelaSomenteNoturnas fechamentoBHTabelaSomenteNoturnas = new FechamentoBHTabelaSomenteNoturnas();
        fechamentoBHTabelaSomenteNoturnas.setDivisao(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaSomenteNoturnas.setHoras(Utils.setDatasMain(1, 0));
        fechamentoBHTabelaSomenteNoturnas.setIdEmpresaFechamentoData(efd);
        fechamentoBHTabelaSomenteNoturnas.setLimite(1);
        fechamentoBHTabelaSomenteNoturnas.setPercentual(1.0);
        fechamentoBHTabelaSomenteNoturnas.setResultado(Utils.setDatasMain(1, 0));
        efd.setFechamentoBHTabelaSomenteNoturnasList(new ArrayList<>());
        efd.getFechamentoBHTabelaSomenteNoturnasList().add(fechamentoBHTabelaSomenteNoturnas);

        FechamentoTabelaExtras fechamentoTabelaExtras = new FechamentoTabelaExtras();
        fechamentoTabelaExtras.setDiurnas(Utils.setDatasMain(1, 0));
        fechamentoTabelaExtras.setHoras(Utils.setDatasMain(1, 0));
        fechamentoTabelaExtras.setIdEmpresaFechamentoData(efd);
        fechamentoTabelaExtras.setIdFechamentoTabelaExtras(1);
        fechamentoTabelaExtras.setLimite(1);
        fechamentoTabelaExtras.setNoturnas(Utils.setDatasMain(1, 0));
        fechamentoTabelaExtras.setPercentual(1.0);
        efd.setFechamentoTabelaExtrasList(new ArrayList<>());
        efd.getFechamentoTabelaExtrasList().add(fechamentoTabelaExtras); 

        FechamentoTabelaExtrasDSR fechamentoTabelaExtrasDSR = new FechamentoTabelaExtrasDSR();
        fechamentoTabelaExtrasDSR.setDiurnas(Utils.setDatasMain(1, 0));
        fechamentoTabelaExtrasDSR.setIdEmpresaFechamentoData(efd);
        fechamentoTabelaExtrasDSR.setIdFechamentoTabelaExtrasDSR(Integer.MIN_VALUE);
        fechamentoTabelaExtrasDSR.setLimite(1);
        fechamentoTabelaExtrasDSR.setNoturnas(Utils.setDatasMain(1, 0));        
        efd.setFechamentoTabelaExtrasDSRList(new ArrayList<>());
        efd.getFechamentoTabelaExtrasDSRList().add(fechamentoTabelaExtrasDSR);

        fechamentoDataList.add(efd);
        entrada.getFuncionario().setEmpresaFechamentoDataList(fechamentoDataList);
    }

    private void inicializaFuncionarioJornada() {
        List<JornadaHorario> jornadaHorarioList = new ArrayList<>();
        setJornadaHorariosList(jornadaHorarioList);
        FuncionarioJornada funcionarioJornada = new FuncionarioJornada();
        funcionarioJornada.setColetivo(new Coletivo(0));
        funcionarioJornada.setDataInicio(dadosEntrada.getJornadaDataInicio());
        funcionarioJornada.setDataFim(null);
        funcionarioJornada.setExcecaoJornada(Boolean.TRUE);
        funcionarioJornada.setFuncionario(entrada.getFuncionario());
        funcionarioJornada.setIdJornadaFuncionario(1);
        funcionarioJornada.setJornada(setJornada(1, "Jornada 1", jornadaHorarioList));
        funcionarioJornada.setManual(Boolean.TRUE);
        funcionarioJornada.setSequenciaInicial(1);
        funcionarioJornada.setValidarColetivo(Boolean.FALSE);
        List<FuncionarioJornada> funcionarioJornadaList = new ArrayList<>();
        funcionarioJornadaList.add(funcionarioJornada);       
        entrada.getFuncionario().setFuncionarioJornadaList(funcionarioJornadaList);
    }

    private void setJornadaHorariosList(List<JornadaHorario> jornadaHorarioList) {
        List<HorarioMarcacao> horarioMarcacaoList = new ArrayList<>();

        //idHorarioMarcacao, idSequencia, horaEntrada, minutoEntrada, horaSaida, minutoSaida
        horarioMarcacaoList.add(new HorarioMarcacao(1, new Horario(),
                dadosEntrada.getHorarioEntrada1(),
                dadosEntrada.getHorarioSaida1(),
                1));

        horarioMarcacaoList.add(new HorarioMarcacao(2, new Horario(),
                dadosEntrada.getHorarioEntrada2(),
                dadosEntrada.getHorarioSaida2(),
                2));

        //descricao, preAssinalada, trataComoDiaNormal
        Horario horario = new Horario();
        horario.setDescricao("Horário 1");
        //horario.setEntrada(LocalTime.MAX);
        horario.setHorarioMarcacaoList(horarioMarcacaoList);
        horario.setIdHorario(Integer.MIN_VALUE);
        horario.setPreAssinalada(Boolean.TRUE);
        //horario.setSaida(LocalTime.MIN);
        horario.setTipodia(entrada.getTipoDiaList().get(dadosEntrada.getHorarioTipoDia()-1));
        //horario.setTotalHoras(totalHoras);
        horario.setTrataComoDiaNormal(dadosEntrada.isHorarioTrataComoDiaNormal());
      
        //idsequencia
        jornadaHorarioList.add(new JornadaHorario(0, horario, new Jornada(1)));
        jornadaHorarioList.add(new JornadaHorario(1, horario, new Jornada(1)));
        jornadaHorarioList.add(new JornadaHorario(2, horario, new Jornada(1)));
    }

    private Jornada setJornada(int idJornada, String descricao, List<JornadaHorario> jornadaHorarioList) {
        Jornada jornada = new Jornada();
        jornada.setIdJornada(idJornada);
        jornada.setDescricao(descricao);
        jornada.setTipoJornada(entrada.getTipoJornadaList().get(dadosEntrada.getJornadaTipoJornada()-1));
        //jornada.setTipoJornada(new TipoJornada(3, "Livre"));
        jornada.setDescontaHorasDSR(dadosEntrada.getJornadaHorasDescontoSemanalDSR());
        jornada.setCompensaAtrasos(dadosEntrada.isJornadaCompensaAtrasos());
        jornada.setInicioAdicionalNoturno(dadosEntrada.getJornadaInicioPeriodoNoturno());
        //Possibidade de não ter data fim noturno (vai até o horário da última marcação)
        jornada.setTerminoAdicionalNoturno(dadosEntrada.getJornadaTerminoPeriodoNoturno());
        jornada.setPercentualAdicionalNoturno(Double.parseDouble(dadosEntrada.getJornadaPercentualNoturno()));
        jornada.setToleranciaAusencia(dadosEntrada.getJornadaToleranciaAusencias());
        jornada.setToleranciaExtra(dadosEntrada.getJornadaToleranciaExtras());
        jornada.setToleranciaOcorrencia(dadosEntrada.getJornadaToleranciaOcorrencias());
        jornada.setJornadaHorarioList(jornadaHorarioList);

        if (idJornada == 1) {
            jornada.setPercentuaisAcrescimo(insertPercentuaisAcrescimos("Acréscimos Extras"));
        }

        return jornada;
    }

    private void inicializaFuncionarioCalendario() {
        List<CalendarioFeriado> calendarioFeriadoList = new ArrayList<>();
        calendarioFeriadoList.add(new CalendarioFeriado(Integer.MIN_VALUE,
                dadosEntrada.getFeriadoData()));

        Calendario calendario = new Calendario();
        calendario.setIdCalendario(Integer.MAX_VALUE);
        calendario.setDescricao("Calendário 1");
        calendario.setCalendarioFeriadoList(calendarioFeriadoList);
        
        FuncionarioCalendario funcionarioCalendario = new FuncionarioCalendario();
        funcionarioCalendario.setCalendario(calendario);
        funcionarioCalendario.setIdFuncionarioCalendario(Integer.MIN_VALUE);
        funcionarioCalendario.setDataInicio(dadosEntrada.getFeriadoData());
        List<FuncionarioCalendario> funcionarioCalendarioList = new ArrayList<>();
        funcionarioCalendarioList.add(funcionarioCalendario);

        entrada.getFuncionario().setFuncionarioCalendarioList(funcionarioCalendarioList);
    }

    private void inicializaFuncionarioMarcacoes() {
        inicializaStatusMarcacao();
        inicializaEncaixeMarcacao();
        inicializaMotivo();
        List<Marcacoes> marcacoesList = new ArrayList<>();

        //anoInicio, anoFim, qtdeMeses, qtdeDias
        //insertMarcacoesList(2016, 2016, 12, 20, marcacoesList);

        //data e hora, viradaDia, descontaDSR 
        insertMarcacaoList("2017/09/21 8:00", 0, true, marcacoesList);
        insertMarcacaoList("2017/09/21 12:00", 0, true, marcacoesList);
        insertMarcacaoList("2017/09/21 13:00", 0, true, marcacoesList);
        insertMarcacaoList("2017/09/22 2:30", 1, true, marcacoesList);

        entrada.getFuncionario().setMarcacoesList(marcacoesList);
    }

    private void inicializaStatusMarcacao() {
        entrada.setStatusMarcacao(new HashMap<>());
        entrada.getStatusMarcacao().put(1, new StatusMarcacao(1, "Original")); //Registro importado
        entrada.getStatusMarcacao().put(2, new StatusMarcacao(2, "Incluída")); //Manualmente
        entrada.getStatusMarcacao().put(3, new StatusMarcacao(3, "Desconsiderada")); //Manualmente – excluída da importação
        entrada.getStatusMarcacao().put(4, new StatusMarcacao(4, "Pré-assinalada")); //Rotina automática
        entrada.getStatusMarcacao().put(5, new StatusMarcacao(5, "Excluída")); //Manualmente
    }

    private void inicializaEncaixeMarcacao() {
        entrada.setEncaixeMarcacao(new HashMap<>());
        entrada.getEncaixeMarcacao().put(0, new EncaixeMarcacao(0)); //, "Não definido")); //Registro importado
        entrada.getEncaixeMarcacao().put(1, new EncaixeMarcacao(1)); //, "Anterior")); //Manualmente
        entrada.getEncaixeMarcacao().put(2, new EncaixeMarcacao(2)); //, "Atual")); //Manualmente
        entrada.getEncaixeMarcacao().put(3, new EncaixeMarcacao(3)); //, "Seguinte")); //Manualmente
    }
    
    private void inicializaMotivo() {
        entrada.setMotivo(new HashMap<>());
        entrada.getMotivo().put(0, new Motivo(0)); //, "Motivo 1", new TipoMotivo(1,"Tipo motivo 1"))); 
        entrada.getMotivo().put(1, new Motivo(1)); //, "Motivo 2", new TipoMotivo(2,"Tipo motivo 2"))); 
    }

//    private void insertMarcacoesList(int anoInicio, int anoFim, int qtdeMes, int qtdeDias, List<Marcacoes> marcacoesList) {
//
//        //Gerar marcações
//        for (int a = anoInicio; a <= anoFim; a++) {
//            for (int m = 1; m <= qtdeMes; m++) {
//                for (int d = 1; d <= qtdeDias; d++) {
//                    marcacoesList.add(new Marcacoes(new Date(a + "/" + m + "/" + d + " 5:00"), Boolean.FALSE, entrada.getEncaixeMarcacao().get(0), entrada.getStatusMarcacao().get(2))); //, entrada.getMotivo().get(1)));
//                    marcacoesList.add(new Marcacoes(new Date(a + "/" + m + "/" + d + " 12:00"), Boolean.FALSE, entrada.getEncaixeMarcacao().get(0), entrada.getStatusMarcacao().get(2))); //, entrada.getMotivo().get(1)));
//                    marcacoesList.add(new Marcacoes(new Date(a + "/" + m + "/" + d + " 13:00"), Boolean.FALSE, entrada.getEncaixeMarcacao().get(0), entrada.getStatusMarcacao().get(2))); //, entrada.getMotivo().get(1)));
//                    marcacoesList.add(new Marcacoes(new Date(a + "/" + m + "/" + d + " 23:00"), Boolean.FALSE, entrada.getEncaixeMarcacao().get(0), entrada.getStatusMarcacao().get(2))); //, entrada.getMotivo().get(1)));
//                }
//            }
//        }
//    }

    private void insertMarcacaoList(String dataHora, int encaixeMarcacao, boolean descontaDSR, List<Marcacoes> marcacoesList) {
        marcacoesList.add(new Marcacoes(new Date(dataHora), Boolean.FALSE, entrada.getEncaixeMarcacao().get(encaixeMarcacao), entrada.getStatusMarcacao().get(4))); //, entrada.getMotivo().get(1)));
    }

    private void inicializaFuncionarioCompensacao() {
        //Motivo motivo = new Motivo("Reposição ausência 01/07", new TipoMotivo("Abono")); 
        Compensacao compensacao = new Compensacao();
        compensacao.setColetivo(new Coletivo(0));
        compensacao.setConsideraDiasSemJornada(dadosEntrada.isCompensandoConsideraDiasSemJornada());
        compensacao.setDataCompensada(dadosEntrada.getCompensandoData());
        compensacao.setDataInicio(dadosEntrada.getCompensandoPeriodoInicio());
        compensacao.setDataFim(dadosEntrada.getCompensandoPeriodoTermino());
        compensacao.setFuncionario(entrada.getFuncionario());
        compensacao.setIdCompensacao(Integer.MIN_VALUE);
        compensacao.setLimiteDiario(dadosEntrada.getCompensandoLimiteDiario());
        compensacao.setManual(Boolean.TRUE);
        compensacao.setMotivo(null);  // motivo)); QUANDO POSSUI MOTIVO NÃO COMPENSA O DIA
        List<Compensacao> compensacaoList = new ArrayList<>();
        compensacaoList.add(compensacao);
        entrada.getFuncionario().setCompensacaoList(compensacaoList);
    }

    private void inicializaFuncionarioAfastamento() {        
        Afastamento afastamento = new Afastamento();
        afastamento.setDataInicio(dadosEntrada.getAfastadoDataInicio());
        afastamento.setDataFim(dadosEntrada.getAfastadoDataFim());
        afastamento.setAbonado(dadosEntrada.isAfastadoComAbono());
        afastamento.setIdAfastamento(Integer.MIN_VALUE);
        List<Afastamento> afastamentoList = new ArrayList<>();
        afastamentoList.add(afastamento);
        entrada.getFuncionario().setAfastamentoList(afastamentoList);
    }

    private void inicializaFuncionarioAbono() {
        Abono abono = new Abono();
        abono.setData(dadosEntrada.getAbonoData());
        abono.setHorasDiurnas(dadosEntrada.getAbonoDiurnasAbonadas());
        abono.setHorasNoturnas(dadosEntrada.getAbonoNoturnasAbonadas());
        abono.setDescontaBH(Boolean.TRUE);
        abono.setIdAbono(Integer.SIZE);
        abono.setMotivo(null);
        abono.setSomenteJustificativa(Boolean.TRUE);
        List<Abono> abonoList = new ArrayList<>();
        abonoList.add(abono);
       
        //Somente justificativa
        //Motivo motivo2 = new Motivo("Problema particular", new TipoMotivo("Abono"));        
        //abonoList.add(new Abono(new Date("2016/11/23"), 
        //        Utils.setDatasMain(0, 0), Utils.setDatasMain(0, 0), Boolean.TRUE, 
        //        Boolean.TRUE, Integer.SIZE, motivo2));
        entrada.getFuncionario().setAbonoList(abonoList);
    }

    private PercentuaisAcrescimo insertPercentuaisAcrescimos(String operacao) {

        List<SequenciaPercentuais> sequenciaPercentuaisList = new ArrayList<>();

        //Tipo de dia Normal
        sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                Utils.getHorasMain(dadosEntrada.getDiaNormalSequencia1horas()),
                Utils.getMinutosMain(dadosEntrada.getDiaNormalSequencia1horas())),
                Double.parseDouble(dadosEntrada.getDiaNormalSequencia1percentual()), 1, entrada.getTipoDiaList().get(0)));
        sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                Utils.getHorasMain(dadosEntrada.getDiaNormalSequencia2horas()),
                Utils.getMinutosMain(dadosEntrada.getDiaNormalSequencia2horas())),
                Double.parseDouble(dadosEntrada.getDiaNormalSequencia2percentual()), 2, entrada.getTipoDiaList().get(0)));
        sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                Utils.getHorasMain(dadosEntrada.getDiaNormalSequencia3horas()),
                Utils.getMinutosMain(dadosEntrada.getDiaNormalSequencia3horas())),
                Double.parseDouble(dadosEntrada.getDiaNormalSequencia3percentual()), 3, entrada.getTipoDiaList().get(0)));
        sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                Utils.getHorasMain(dadosEntrada.getDiaNormalSequencia4horas()),
                Utils.getMinutosMain(dadosEntrada.getDiaNormalSequencia4horas())),
                Double.parseDouble(dadosEntrada.getDiaNormalSequencia4percentual()), 4, entrada.getTipoDiaList().get(0)));

        if ("Acréscimos B.H.".equals(operacao)) {

            //Tipo de dia Compensação
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getDiaCompensacaoSequencia1horas()),
                    Utils.getMinutosMain(dadosEntrada.getDiaCompensacaoSequencia1horas())),
                    Double.parseDouble(dadosEntrada.getDiaCompensacaoSequencia1percentual()), 1, entrada.getTipoDiaList().get(1)));
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getDiaCompensacaoSequencia2horas()),
                    Utils.getMinutosMain(dadosEntrada.getDiaCompensacaoSequencia2horas())),
                    Double.parseDouble(dadosEntrada.getDiaCompensacaoSequencia2percentual()), 2, entrada.getTipoDiaList().get(1)));
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getDiaCompensacaoSequencia3horas()),
                    Utils.getMinutosMain(dadosEntrada.getDiaCompensacaoSequencia3horas())),
                    Double.parseDouble(dadosEntrada.getDiaCompensacaoSequencia3percentual()), 3, entrada.getTipoDiaList().get(1)));

            //Tipo de dia Horas Noturnas
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getHorasNoturnasSequencia1horas()),
                    Utils.getMinutosMain(dadosEntrada.getHorasNoturnasSequencia1horas())),
                    Double.parseDouble(dadosEntrada.getHorasNoturnasSequencia1percentual()), 1, entrada.getTipoDiaList().get(2)));
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getHorasNoturnasSequencia2horas()),
                    Utils.getMinutosMain(dadosEntrada.getHorasNoturnasSequencia2horas())),
                    Double.parseDouble(dadosEntrada.getHorasNoturnasSequencia2percentual()), 2, entrada.getTipoDiaList().get(2)));
            sequenciaPercentuaisList.add(new SequenciaPercentuais(Utils.setDatasMain(
                    Utils.getHorasMain(dadosEntrada.getHorasNoturnasSequencia3horas()),
                    Utils.getMinutosMain(dadosEntrada.getHorasNoturnasSequencia3horas())),
                    Double.parseDouble(dadosEntrada.getHorasNoturnasSequencia3percentual()), 3, entrada.getTipoDiaList().get(2)));

        }

        PercentuaisAcrescimo percentuaisAcrescimo = new PercentuaisAcrescimo();
        percentuaisAcrescimo.setSequenciaPercentuaisList(sequenciaPercentuaisList);
        percentuaisAcrescimo.setIdPercentuaisAcrescimo(Integer.MIN_VALUE);
        return percentuaisAcrescimo;
        //return new PercentuaisAcrescimo(Integer.MIN_VALUE, operacao, sequenciaPercentuaisList);
       
    }

    private void inicializaFuncionarioBancoHoras() {
        List<FuncionarioBancoHoras> funcionarioBancoHorasList = new ArrayList<>();
        FuncionarioBancoHoras funcionarioBancoHoras = new FuncionarioBancoHoras();
        funcionarioBancoHoras.setIdFuncionarioBancoHoras(Integer.MIN_VALUE);
        funcionarioBancoHoras.setBancoHoras(insertBancoHoras("Banco de Horas 1"));
        funcionarioBancoHoras.setDataInicio(dadosEntrada.getBhData());
        funcionarioBancoHoras.setDataFim(new Date());
        funcionarioBancoHorasList.add(funcionarioBancoHoras);
        entrada.getFuncionario().setFuncionarioBancoHorasList(funcionarioBancoHorasList);
    }

    private BancoHoras insertBancoHoras(String descricao) {
        BancoHoras bancoHoras = new BancoHoras();
        bancoHoras.setDescricao(descricao);
        bancoHoras.setNaoPagaAdicionalNoturnoBH(dadosEntrada.isBhNaoPagaAdicionalNoturnoBH());
        bancoHoras.setTipoLimiteDiario(dadosEntrada.isBhTipoLimiteDiario());
        bancoHoras.setTrataAbonoDebito(dadosEntrada.isBhTrataAbonoComoDebito());
        bancoHoras.setTrataAusenciaDebito(dadosEntrada.isBhTrataAusenciaComoDebito());
        bancoHoras.setTrataFaltaDebito(dadosEntrada.isBhTrataFaltaComoDebito());
        bancoHoras.setPercentuaisAcrescimo(insertPercentuaisAcrescimos("Acréscimos B.H."));
        bancoHoras.setGatilhoNegativo(dadosEntrada.getBhGatilhoNegativo());
        bancoHoras.setGatilhoPositivo(dadosEntrada.getBhGatilhoPositivo());

        List<BancoHorasLimite> bancoHorasLimiteList = new ArrayList<>();
//        bancoHorasLimiteList.add(new BancoHorasLimite(dadosEntrada.getBhLimiteDiarioTipoDia(),
//                entrada.getTipoDiaList().get(0)));
        bancoHoras.setBancoHorasLimiteList(bancoHorasLimiteList);

        return bancoHoras;
    }

    private void inicializaDSR() {
        Dsr dsr = new Dsr();
        dsr.setLimiteHorasFaltas(dadosEntrada.getDsrLimiteHorasFaltasSemanal());
        dsr.setIncluiHoraExtra(dadosEntrada.isDsrIncluirHorasExtrasCalculo());
        dsr.setIncluiFeriadoComoHoraNormal(dadosEntrada.isDsrIncluirFeriadosComoHoraNormal());
        dsr.setDescontaAusencia(dadosEntrada.isDsrDescontarAusencias());
        dsr.setIntegral(dadosEntrada.isDsrIntegral());
        dsr.setEmpresa(null);
        entrada.setDsrList(new ArrayList<>());
        entrada.getDsrList().add(dsr);
    }

    private void inicializaFuncionarioDiaNaoDescontaDSR() {
        List<FuncionarioDiaNaoDescontaDSR> funcionarioDiaNaoDescontaDSRList = new ArrayList<>();
//        funcionarioDiaNaoDescontaDSRList.add(
//                new FuncionarioDiaNaoDescontaDSR(dadosEntrada.getDataNaoDescontarDSR()));
        entrada.getFuncionario().setFuncionarioDiaNaoDescontaDSRList(funcionarioDiaNaoDescontaDSRList);
    }

    private void inicializaFuncionarioBancoHorasFechamento() {
        //********************************************** 
        //Fechamentos realizados via tela de Banco de Horas
        FuncionarioBancoHorasFechamento funcionarioBancoHorasFechamento = new FuncionarioBancoHorasFechamento();
//        funcionarioBancoHorasFechamento.setTipoFechamento(new TipoFechamento(Integer.MIN_VALUE, CONSTANTES.ACERTO));

//        fechamentoBancoHoras.setTipoFechamento(new TipoFechamento(Integer.MIN_VALUE, CONSTANTES.EDICAODESALDO));
//        fechamentoBancoHoras.setTipoFechamento(new TipoFechamento(Integer.MIN_VALUE, CONSTANTES.SUBTOTAL));
        funcionarioBancoHorasFechamento.setCredito(dadosEntrada.getCreditar());
        funcionarioBancoHorasFechamento.setDebito(dadosEntrada.getDebitar());
        funcionarioBancoHorasFechamento.setDataFechamento(dadosEntrada.getAcertoData());
//        fechamentoBancoHoras.setDataFechamento(new Date(dadosEntrada.getEdicaoSaldoData()));
//        fechamentoBancoHoras.setDataFechamento(new Date(dadosEntrada.getSubtotalData()));
//        funcionarioBancoHorasFechamento.setFuncionarioBancoHoras(entrada.getFuncionario().getFuncionarioBancoHorasList().get(0));

        List<FuncionarioBancoHorasFechamento> fechamentoBancoHorasList = new ArrayList<>();
        fechamentoBancoHorasList.add(funcionarioBancoHorasFechamento);

        entrada.getFuncionario().getFuncionarioBancoHorasList().get(0).setFuncionarioBancoHorasFechamentoList(fechamentoBancoHorasList);
        
    }
    
}
