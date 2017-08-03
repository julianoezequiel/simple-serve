package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.fechamento.EmpresaFechamentoData;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaAcrescimos;
import com.topdata.toppontoweb.entity.fechamento.FechamentoBHTabelaSomenteNoturnas;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtras;
import com.topdata.toppontoweb.entity.fechamento.FechamentoTabelaExtrasDSR;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.TabelaCreditoSaldoUnico;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.DsrApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensando;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Consulta os dados de fechamento empresa realizados para esse funcionário
 * 
 * Grava no saidaDia os dados de fechamento realizados anteriormente vindos da
 * base (nesse caso não é realizado o cálculo, somente consulta)
 *
 * @author enio.junior
 */
public class FuncionarioEmpresaFechamentoService {

    private final Funcionario funcionarioAPI;
    private final Calculo calculo;
    private EmpresaFechamentoData empresaFechamentoData;

    public FuncionarioEmpresaFechamentoService(Calculo calculo) {
        this.calculo = calculo;
        this.funcionarioAPI = this.calculo.getEntradaAPI().getFuncionario();
    }

    public EmpresaFechamentoData getDadosFechamentoDia(Date dia) {
        if (funcionarioAPI.getEmpresaFechamentoDataList() != null) {
            return funcionarioAPI.getEmpresaFechamentoDataList().stream()
                    .filter(f -> (f.getData().compareTo(dia) == 0))
                    .findFirst().orElse(new EmpresaFechamentoData());
        }
        return new EmpresaFechamentoData();
    }

    public Date getDataUltimoFechamento() {
        if (funcionarioAPI.getEmpresaFechamentoDataList() != null) {
            return funcionarioAPI.getEmpresaFechamentoDataList()
                    .stream()
                    .sorted(Comparator.comparing(EmpresaFechamentoData::getData).reversed())
                    .map(f -> f.getData())
                    .findFirst().orElse(null);
        }
        return null;
    }

    public void setConsultaDadosFechamento(SaidaDia saidaDia) {
        this.empresaFechamentoData = getDadosFechamentoDia(saidaDia.getData());
        setSaldoExtras(saidaDia);
        setSaldoAusencias(saidaDia);
        setSaldoNormais(saidaDia);
        setSaldoAusenciasAbonadas(saidaDia);
        setSaldoAusenciasCompensadas(saidaDia);
        setSaldoExtrasCompensando(saidaDia);
        setBancodeHoras(saidaDia);
        setTabeladeExtras(saidaDia);
        setTabeladeExtrasDSR(saidaDia);
    }

    public void setConsultaHorasPrevistasFechamento(SaidaDia saidaDia) {
        this.empresaFechamentoData = getDadosFechamentoDia(saidaDia.getData());
        if (this.empresaFechamentoData.getPossuiHorasPrevistas() != null) {
            saidaDia.setHorasPrevistas(
                    new Saldo(empresaFechamentoData.getPossuiHorasPrevistas(),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasPrevistasDiurnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasPrevistasNoturnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasPrevistasDiferencaAdicionalNoturno()).toInstant())));
        } else {
            saidaDia.setHorasPrevistas(
                    new Saldo(false, Duration.ZERO, Duration.ZERO, Duration.ZERO));
        }
    }

    public void setConsultaHorasTrabalhadasFechamento(SaidaDia saidaDia) {
        this.empresaFechamentoData = getDadosFechamentoDia(saidaDia.getData());
        if (this.empresaFechamentoData.getPossuiHorasTrabalhadas() != null) {
            saidaDia.setHorasTrabalhadas(
                    new SaldoTrabalhadas(empresaFechamentoData.getPossuiHorasTrabalhadas(),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasTrabalhadasDiurnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasTrabalhadasNoturnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getHorasTrabalhadasDiferencaAdicionalNoturno()).toInstant())));
        } else {
            saidaDia.setHorasTrabalhadas(
                    new SaldoTrabalhadas(false, Duration.ZERO, Duration.ZERO, Duration.ZERO));
        }
    }

    private void setSaldoExtras(SaidaDia saidaDia) {
        if (this.empresaFechamentoData.getPossuiExtras() != null) {
            SaldoExtras se = new SaldoExtras(empresaFechamentoData.getPossuiExtras(),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoExtrasDiurnas()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoExtrasNoturnas()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoExtrasDiferencaAdicionalNoturno()).toInstant()));
            se.setTodas(empresaFechamentoData.getPossuiTodasComoExtras());
            saidaDia.setSaldoExtras(se);
        } else {
            saidaDia.setSaldoExtras(
                    new SaldoExtras());
        }
    }

    private void setSaldoAusencias(SaidaDia saidaDia) {
        if (this.empresaFechamentoData.getPossuiAusencias() != null) {
            saidaDia.setSaldoAusencias(
                    new SaldoAusencias(empresaFechamentoData.getPossuiAusencias(),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasDiurnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasNoturnas()).toInstant()),
                            Duration.ZERO));
        } else {
            saidaDia.setSaldoAusencias(
                    new SaldoAusencias());
        }
    }

    private void setSaldoNormais(SaidaDia saidaDia) {
        if (this.empresaFechamentoData.getPossuiNormais() != null) {
            saidaDia.setSaldoNormais(
                    new Saldo(empresaFechamentoData.getPossuiNormais(),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoNormaisDiurnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoNormaisNoturnas()).toInstant()),
                            Duration.ZERO));
        } else {
            saidaDia.setSaldoNormais(
                    new Saldo());
        }
    }

    private void setSaldoAusenciasAbonadas(SaidaDia saidaDia) {
        //Se abonadas ou afastamento com abono
        if (this.empresaFechamentoData.getPossuiAusenciasAbonadas() != null
                || empresaFechamentoData.getPossuiAfastadoComAbono() != null) {
            saidaDia.getSaldoAusencias().setAbonoDia(
                    new Saldo(empresaFechamentoData.getPossuiAusenciasAbonadas()
                            || empresaFechamentoData.getPossuiAfastadoComAbono(),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasAbonadasDiurnas()).toInstant()),
                            Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasAbonadasNoturnas()).toInstant()),
                            Duration.ZERO));
        } else {
            saidaDia.getSaldoAusencias().setAbonoDia(
                    new Saldo());
        }
    }

    private void setSaldoAusenciasCompensadas(SaidaDia saidaDia) {
        //Se ausências compensadas
        if (this.empresaFechamentoData.getSaldoAusenciasCompensadasDiurnas() != null) {
            //Atualizar as horas compensadas na lista de compensação de ausências
            if (Utils.getSaldoDiaCompensado(saidaDia.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()) != null) {
                Utils.getSaldoDiaCompensado(saidaDia.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).setCompensadasDiurnas(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasCompensadasDiurnas()).toInstant()));
                Utils.getSaldoDiaCompensado(saidaDia.getData(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).setCompensadasDiurnas(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoAusenciasCompensadasNoturnas()).toInstant()));
            }
        }
    }

    private void setSaldoExtrasCompensando(SaidaDia saidaDia) {
        //Se extras compensando
        if (this.empresaFechamentoData.getPossuiExtrasCompensacao() != null
                && empresaFechamentoData.getPossuiExtrasCompensacao()) {

            //Inserir as horas compensadas na lista de compensação de extras
            //Lembrando que na lista de extras os dias são adicionados no momento do cálculo de extras
            if (this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getIdCompensacao() != null
                    && this.calculo.isPeriodoCompensacaoExtras()) {

                Instant compensacaoDiurnas = Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoExtrasCompensacaoDiurnas()).toInstant();
                Instant compensacaoNoturnas = Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getSaldoExtrasCompensacaoNoturnas()).toInstant();

                SaldoDiaCompensando saldoDiaCompensando = new SaldoDiaCompensando();
                saldoDiaCompensando.setDataCompensando(saidaDia.getData());
                saldoDiaCompensando.setCompensandoDiurnas(Utils.getHorasAcima24horas(compensacaoDiurnas));
                saldoDiaCompensando.setCompensandoNoturnas(Utils.getHorasAcima24horas(compensacaoNoturnas));
                this.calculo.getEntradaAPI().getSaldoDiaCompensandoList().add(saldoDiaCompensando);

                //Atualizar a lista de compensações de ausências com os dados de fechamento
                if (Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas() != null) {

                    Duration compensadasDiurnasAusencias = Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas();
                    Duration compensadasNoturnasAusencias = Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas();

                    Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList())
                            .setCompensadasDiurnas(compensadasDiurnasAusencias.plus(Utils.getHorasAcima24horas(compensacaoDiurnas)));

                    Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList())
                            .setCompensadasNoturnas(compensadasNoturnasAusencias.plus(Utils.getHorasAcima24horas(compensacaoNoturnas)));

                }
            }
        }
    }

    private void setBancodeHoras(SaidaDia saidaDia) {
        Duration creditoFechamento;
        Duration debitoFechamento;
        if (this.calculo.getRegrasService().isFechamentoBHAcerto()
                || this.calculo.getRegrasService().isFechamentoBHEdicaodeSaldo()) {
            FuncionarioBancoHorasFechamento fechamentoNoDia = this.calculo.getBancodeHorasService().getBancodeHorasFechamentoService().getFechamentoNoDia();
            creditoFechamento = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoNoDia.getCredito()).toInstant());
            debitoFechamento = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoNoDia.getDebito()).toInstant());
        } else {
            creditoFechamento = Duration.ZERO;
            debitoFechamento = Duration.ZERO;
        }

        if (this.empresaFechamentoData.getBHCredito() != null) {
            saidaDia.setBancodeHoras(new BancodeHorasApi(
                    empresaFechamentoData.getIdFuncionarioBancoHoras() != null,
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHCredito()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHDebito()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHSaldoDia()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHSaldoAcumuladoDia()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHSaldoAcumuladoDiaAnterior()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHGatilho()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHSaldoCredito()).toInstant()),
                    Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(empresaFechamentoData.getBHSaldoDebito()).toInstant()),
                    new ArrayList<>(),
                    getAcrescimosSomenteNoturnasList(),
                    getAcrescimosTipodeDiaList(),
                    getTabelaCreditoSaldoUnico(getAcrescimosTipodeDiaList()),
                    creditoFechamento,
                    debitoFechamento));
        }
    }

    private Duration somaDiurnasTabelaAcrescimosBH;
    private Duration somaNoturnasTabelaAcrescimosBH;
    private Duration somaCompensadasTabelaAcrescimosBH;

    public List<TabelaCreditoSaldoUnico> getTabelaCreditoSaldoUnico(List<TabelaSequenciaPercentuais> acrescimosTipodeDiaList) {
        setTotaisCreditoSaldoUnico(acrescimosTipodeDiaList);
        List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList = new ArrayList<>();
        this.calculo.getTipoDiaList()
                .stream()
                .forEach(new criarSaldosTipoDia(tabelaCreditoSaldoUnicoList));
        return tabelaCreditoSaldoUnicoList;
    }

    private class criarSaldosTipoDia implements Consumer<TipoDia> {

        private final List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList;

        public criarSaldosTipoDia(List<TabelaCreditoSaldoUnico> tabelaCreditoSaldoUnicoList) {
            this.tabelaCreditoSaldoUnicoList = tabelaCreditoSaldoUnicoList;
        }

        @Override
        public void accept(TipoDia tipoDia) {
            TabelaCreditoSaldoUnico creditoSaldoUnico = new TabelaCreditoSaldoUnico();
            creditoSaldoUnico.setTipoDia(tipoDia);
            creditoSaldoUnico.setSaldo(Duration.ZERO);

            if (calculo.getRegrasService().getIdTipoDia() == tipoDia.getIdTipodia()) {
                creditoSaldoUnico.setSaldo(somaDiurnasTabelaAcrescimosBH);
            }
            if (tipoDia.getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO)) {
                creditoSaldoUnico.setSaldo(somaCompensadasTabelaAcrescimosBH);
            }
            if (tipoDia.getDescricao().equals(CONSTANTES.TIPODIA_HORAS_NOTURNAS)) {
                creditoSaldoUnico.setSaldo(somaNoturnasTabelaAcrescimosBH);
            }
            tabelaCreditoSaldoUnicoList.add(creditoSaldoUnico);
        }
    }

    private void setTotaisCreditoSaldoUnico(List<TabelaSequenciaPercentuais> acrescimosTipoDiaList) {
        if (acrescimosTipoDiaList.size() > 0) {
            this.somaDiurnasTabelaAcrescimosBH = Duration.ZERO;
            acrescimosTipoDiaList
                    .stream()
                    .filter(f -> !f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaDiurnasTabelaAcrescimosBH = this.somaDiurnasTabelaAcrescimosBH.plus(tabela.getResultadoDiurnas());
                    });

            this.somaNoturnasTabelaAcrescimosBH = Duration.ZERO;
            acrescimosTipoDiaList
                    .stream()
                    .filter(f -> !f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaNoturnasTabelaAcrescimosBH = this.somaNoturnasTabelaAcrescimosBH.plus(tabela.getResultadoNoturnas());
                    });

            this.somaCompensadasTabelaAcrescimosBH = Duration.ZERO;
            acrescimosTipoDiaList
                    .stream()
                    .filter(f -> f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .forEach((TabelaSequenciaPercentuais tabela) -> {
                        this.somaCompensadasTabelaAcrescimosBH = this.somaCompensadasTabelaAcrescimosBH.plus(tabela.getResultadoDiurnas().plus(tabela.getResultadoNoturnas()));
                    });
        }
    }

    private List<TabelaSequenciaPercentuais> getAcrescimosSomenteNoturnasList() {
        List<TabelaSequenciaPercentuais> acrescimosSomenteNoturnasList = new ArrayList<>();
        if (this.empresaFechamentoData.getFechamentoBHTabelaSomenteNoturnasList() != null) {
            empresaFechamentoData.getFechamentoBHTabelaSomenteNoturnasList()
                    .stream()
                    .filter(f -> (Objects.equals(f.getIdEmpresaFechamentoData().getIdEmpresaFechamentoData(), empresaFechamentoData.getIdEmpresaFechamentoData())))
                    .sorted(Comparator.comparing(FechamentoBHTabelaSomenteNoturnas::getLimite))
                    .forEach((FechamentoBHTabelaSomenteNoturnas fechamentoBHTabelaSomenteNoturnas) -> {
                        acrescimosSomenteNoturnasList.add(new TabelaSequenciaPercentuais(
                                Duration.ZERO,
                                Duration.ZERO,
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaSomenteNoturnas.getDivisao()).toInstant()),
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaSomenteNoturnas.getResultado()).toInstant()),
                                Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaSomenteNoturnas.getHoras()),
                                fechamentoBHTabelaSomenteNoturnas.getPercentual(),
                                fechamentoBHTabelaSomenteNoturnas.getLimite(),
                                this.calculo.getRegrasService().getTipoDia()));
                    });
        }
        return acrescimosSomenteNoturnasList;
    }

    private List<TabelaSequenciaPercentuais> getAcrescimosTipodeDiaList() {
        List<TabelaSequenciaPercentuais> acrescimosTipoDiaList = new ArrayList<>();
        if (this.empresaFechamentoData.getFechamentoBHTabelaAcrescimosList() != null) {
            empresaFechamentoData.getFechamentoBHTabelaAcrescimosList()
                    .stream()
                    .filter(f -> (Objects.equals(f.getIdEmpresaFechamentoData().getIdEmpresaFechamentoData(), empresaFechamentoData.getIdEmpresaFechamentoData())))
                    .sorted(Comparator.comparing(FechamentoBHTabelaAcrescimos::getLimite))
                    .forEach((FechamentoBHTabelaAcrescimos fechamentoBHTabelaAcrescimos) -> {
                        acrescimosTipoDiaList.add(new TabelaSequenciaPercentuais(
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaAcrescimos.getDivisaoDiurnas()).toInstant()),
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaAcrescimos.getResultadoDiurnas()).toInstant()),
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaAcrescimos.getDivisaoNoturnas()).toInstant()),
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaAcrescimos.getResultadoNoturnas()).toInstant()),
                                Utils.getDataAjustaDiferenca3horas(fechamentoBHTabelaAcrescimos.getHoras()),
                                fechamentoBHTabelaAcrescimos.getPercentual(),
                                fechamentoBHTabelaAcrescimos.getLimite(),
                                this.calculo.getRegrasService().getTipoDia()));
                    });
        }
        return acrescimosTipoDiaList;
    }

    private void setTabeladeExtras(SaidaDia saidaDia) {
        List<TabelaSequenciaPercentuais> tabelaExtrasList = new ArrayList<>();
        if (this.empresaFechamentoData.getFechamentoTabelaExtrasList() != null) {
            empresaFechamentoData.getFechamentoTabelaExtrasList()
                    .stream()
                    .filter(f -> (Objects.equals(f.getIdEmpresaFechamentoData().getIdEmpresaFechamentoData(), empresaFechamentoData.getIdEmpresaFechamentoData())))
                    .sorted(Comparator.comparing(FechamentoTabelaExtras::getLimite))
                    .forEach((FechamentoTabelaExtras fechamentoTabelaExtras) -> {
                        tabelaExtrasList.add(new TabelaSequenciaPercentuais(
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoTabelaExtras.getDiurnas()).toInstant()),
                                Duration.ZERO,
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoTabelaExtras.getNoturnas()).toInstant()),
                                Duration.ZERO,
                                Utils.getDataAjustaDiferenca3horas(fechamentoTabelaExtras.getHoras()),
                                fechamentoTabelaExtras.getPercentual(),
                                fechamentoTabelaExtras.getLimite(),
                                this.calculo.getRegrasService().getTipoDia()));
                    });
        }

        saidaDia.setTabelaExtrasList(tabelaExtrasList);
    }

    private void setTabeladeExtrasDSR(SaidaDia saidaDia) {
        List<TabelaSequenciaPercentuais> tabelaExtrasDSRList = new ArrayList<>();
        if (this.empresaFechamentoData.getFechamentoTabelaExtrasDSRList() != null) {
            empresaFechamentoData.getFechamentoTabelaExtrasDSRList()
                    .stream()
                    .filter(f -> (Objects.equals(f.getIdEmpresaFechamentoData().getIdEmpresaFechamentoData(), empresaFechamentoData.getIdEmpresaFechamentoData())))
                    .sorted(Comparator.comparing(FechamentoTabelaExtrasDSR::getLimite))
                    .forEach((FechamentoTabelaExtrasDSR fechamentoTabelaExtrasDSR) -> {
                        tabelaExtrasDSRList.add(new TabelaSequenciaPercentuais(
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoTabelaExtrasDSR.getDiurnas()).toInstant()),
                                Duration.ZERO,
                                Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoTabelaExtrasDSR.getNoturnas()).toInstant()),
                                Duration.ZERO,
                                null,
                                0d,
                                fechamentoTabelaExtrasDSR.getLimite(),
                                this.calculo.getRegrasService().getTipoDia()));
                    });
        }

        saidaDia.setDsr(new DsrApi());
        saidaDia.getDsr().setTabelaExtrasDSRList(tabelaExtrasDSRList);
    }

}
