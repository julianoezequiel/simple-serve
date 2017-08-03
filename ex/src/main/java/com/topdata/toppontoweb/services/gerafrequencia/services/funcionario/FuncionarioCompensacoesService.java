package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Compensacao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.EntradaApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Compensações realizadas ou a realizar do funcionário
 *
 * @author enio.junior
 */
public class FuncionarioCompensacoesService {

    private final Funcionario funcionarioAPI;
    private final Calculo calculo;

    public FuncionarioCompensacoesService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
    }

    /**
     * Se existir compensação cadastrada para esse funcionário Adicionar todos
     * os dias na lista de compensação
     * **********************************************************************************************
     * 1º Lista de dias compensados deve ter o saldo pendente de diurnas e
     * noturnas Se tiver hora extra no dia que está compensando diminui o saldo
     * do dia compensado nessa lista Quando ausência somente lê o que já foi
     * compensado e diminui da ausência do dia
     * **********************************************************************************************
     * @param entradaApi
     * @return
     */
    public synchronized List<? extends Compensacao> criaListaCompensacaoAusenciasCadaDia(EntradaApi entradaApi) {
        List<Compensacao> compensacaoList = getDiasCompensando();
        if (compensacaoList.size() > 0) {
            compensacaoList
                    .stream()
                    .forEach(compensacao -> {

                        SaldoDiaCompensado saldoCompensado = new SaldoDiaCompensado(
                                compensacao.getDataCompensada(),
                                compensacao.getDataInicio(), 
                                compensacao.getDataFim(),
                                Utils.getDataAjustaDiferenca3horas(compensacao.getLimiteDiario()), 
                                compensacao.getConsideraDiasSemJornada());

                        saldoCompensado.setStatus("");
                        saldoCompensado.setDiurnas(Duration.ZERO);
                        saldoCompensado.setNoturnas(Duration.ZERO);
                        saldoCompensado.setACompensarDiurnas(Duration.ZERO);
                        saldoCompensado.setACompensarNoturnas(Duration.ZERO);
                        saldoCompensado.setCompensadasDiurnas(Duration.ZERO);
                        saldoCompensado.setCompensadasNoturnas(Duration.ZERO);
                        entradaApi.getSaldoDiaCompensadoList().add(saldoCompensado);
                    });
        }

        return compensacaoList;
    }

    /**
     * Se este dia está compensando algum outro dia
     *
     * @return
     */
    public Compensacao getCompensandoDia() {
        if (funcionarioAPI.getCompensacaoList() != null) {
            return funcionarioAPI.getCompensacaoList()
                    .stream()
                    .filter(f -> (Utils.formatoDataSemHorario(f.getDataInicio()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0
                            && (f.getDataFim() == null || Utils.formatoDataSemHorario(f.getDataFim()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) >= 0)))
                    //.filter(f -> f.getMotivo() == null) //Não é somente justificativa
                    .findFirst().orElse(new Compensacao());
        }
        return new Compensacao();
    }

    /**
     * Se este dia está sendo compensado por algum outro dia
     *
     * @return
     */
    public Compensacao getDiaCompensado() {
        if (funcionarioAPI.getCompensacaoList() != null) {
            return funcionarioAPI.getCompensacaoList().stream()
                    .filter(f -> (Utils.formatoDataSemHorario(f.getDataCompensada()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) == 0))
                    .findFirst().orElse(new Compensacao());
        }
        return new Compensacao();
    }

    /**
     * Retorna 1º dia cadastrado para compensação
     *
     * @return
     */
    public Date getPrimeiroDiaListaCompensacaoCadastrada() {
        if (funcionarioAPI.getCompensacaoList() != null) {
            Date inicioPeriodo = funcionarioAPI.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataInicio))
                    .map(f -> f.getDataInicio()).findFirst().orElse(null);

            Date dataCompensada = funcionarioAPI.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataCompensada))
                    .map(f -> f.getDataCompensada()).findFirst().orElse(null);

            if (inicioPeriodo.before(dataCompensada)) {
                return inicioPeriodo;
            } else {
                return dataCompensada;
            }
        }
        return null;
    }

    /**
     * Retorna o último dia cadastrado para compensação
     *
     * @return
     */
    public Date getUltimoDiaListaCompensacaoCadastrada() {
        if (funcionarioAPI.getCompensacaoList() != null) {
            Date fimPeriodo = funcionarioAPI.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataFim).reversed())
                    .map(f -> f.getDataFim()).findFirst().orElse(null);

            Date dataCompensada = funcionarioAPI.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataCompensada).reversed())
                    .map(f -> f.getDataCompensada()).findFirst().orElse(null);

            if (fimPeriodo.after(dataCompensada)) {
                return fimPeriodo;
            } else {
                return dataCompensada;
            }
        }
        return null;
    }

    /**
     * Informa se tem compensação cadastrada para saber se precisará processar o
     * período de compensação
     *
     * @return
     */
    public boolean isPossuiListaCompensacaoCadastrada() {
        return funcionarioAPI.getCompensacaoList() != null
                && funcionarioAPI.getCompensacaoList().size() > 0;
    }

    /**
     * Consultar todos os dias cadastrados de compensação
     *
     * @return
     */
    public List<Compensacao> getDiasCompensando() {
        if (funcionarioAPI.getCompensacaoList() != null) {
            return funcionarioAPI.getCompensacaoList().stream()
                    .sorted(Comparator.comparing(Compensacao::getDataInicio))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
