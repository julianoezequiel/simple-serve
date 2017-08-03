package com.topdata.toppontoweb.services.gerafrequencia.services.saldos;

import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.services.compensacoes.ExtrasCompensandoDiaService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calcula o saldo de horas extras do dia
 *
 * @author enio.junior
 */
public class ExtrasService {

    public final static Logger LOGGER = LoggerFactory.getLogger(ExtrasService.class.getName());
    private final Saldo horasPrevistas;
    private final SaldoTrabalhadas horasTrabalhadas;
    private final ExtrasCompensandoDiaService extrasCompensandoDiaService;
    private final Calculo calculo;
    private SaldoExtras saldoCalculoExtras;
    
    public ExtrasService(Saldo horasPrevistas, SaldoTrabalhadas horasTrabalhadas, Calculo calculo) {
        this.calculo = calculo;
        this.horasPrevistas = horasPrevistas;
        this.horasTrabalhadas = horasTrabalhadas;
        this.extrasCompensandoDiaService = new ExtrasCompensandoDiaService(this.calculo);
    }

    public SaldoExtras getCalculaExtras() {
        SaldoExtras saldoBruto = new SaldoExtras();
        if (this.calculo.getRegrasService().isJornada()) {
            if (this.calculo.getRegrasService().isCompensaAtrasos()) {
                if (this.calculo.getRegrasService().isCumprirHorario()) {
                    //Se saldo horas for positivo
                    setCumprirHorario(saldoBruto);
                } else {
                    setSemCumprirHorario(saldoBruto);
                }
            } else {
                setNaoCompensaAtrasos(saldoBruto);
            }
        } else {
            //Não possui jornada, então se tiver horas trabalhadas é tudo hora extra
            setSemCumprirHorario(saldoBruto);
        }
        return saldoBruto;
    }

    private void setCumprirHorario(SaldoExtras saldoExtras) {
        if (horasTrabalhadas != null) {
            if (horasTrabalhadas.getDiurnas().toMillis() > horasPrevistas.getDiurnas().toMillis()) {
                saldoExtras.setDiurnas(horasTrabalhadas.getDiurnas().minus(horasPrevistas.getDiurnas()));
            }
            if (horasTrabalhadas.getNoturnas().toMillis() > horasPrevistas.getNoturnas().toMillis()) {
                saldoExtras.setNoturnas(horasTrabalhadas.getNoturnas().minus(horasPrevistas.getNoturnas()));
            }
        }
    }

    private void setSemCumprirHorario(SaldoExtras saldoExtras) {
        if (horasTrabalhadas != null) {
            saldoExtras.setTodas(true);
            saldoExtras.setDiurnas(horasTrabalhadas.getDiurnas());
            saldoExtras.setNoturnas(horasTrabalhadas.getNoturnas());
        }
    }

    private void setNaoCompensaAtrasos(SaldoExtras saldoExtras) {
        if (horasTrabalhadas != null) {
            if (horasTrabalhadas.getExtras() != null) {
                saldoExtras.setDiurnas(horasTrabalhadas.getExtras().getDiurnas());
                saldoExtras.setNoturnas(horasTrabalhadas.getExtras().getNoturnas());
            }
        }
    }

    public SaldoExtras getSaldoExtrasSubtraiAusencias() {
        saldoCalculoExtras = getCalculaExtras();
        SaldoExtras saldoExtras = new SaldoExtras();
        AusenciasService ausenciasService = new AusenciasService(horasPrevistas, horasTrabalhadas, this.calculo);
        if (this.calculo.getRegrasService().isCompensaAtrasos()) {
            //Subtrai Ausências
            if (saldoCalculoExtras.getDiurnas().toMillis() > ausenciasService.getCalculaAusencias().getDiurnas().toMillis()) {
                saldoExtras.setDiurnas(saldoCalculoExtras.getDiurnas().minus(ausenciasService.getCalculaAusencias().getDiurnas()));
            }
            if (saldoCalculoExtras.getNoturnas().toMillis() > ausenciasService.getCalculaAusencias().getNoturnas().toMillis()) {
                saldoExtras.setNoturnas(saldoCalculoExtras.getNoturnas().minus(ausenciasService.getCalculaAusencias().getNoturnas()));
            }
        }
        return saldoExtras;
    }

    public SaldoExtras getSaldoExtras() {
        LOGGER.debug("Calculando os saldos de extras... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        saldoCalculoExtras = getCalculaExtras();
        SaldoExtras saldoExtras = new SaldoExtras();
        saldoExtras.setPossui(false);
        saldoExtras.setTodas(saldoCalculoExtras.isTodas());
        AusenciasService ausenciasService = new AusenciasService(horasPrevistas, horasTrabalhadas, this.calculo);
        if (this.calculo.getRegrasService().isCompensaAtrasos() && !saldoExtras.isTodas()) {
            //Subtrai Invertido
            saldoExtras.setDiurnas(getSaldoExtrasSubtraiAusencias().getDiurnas().minus(ausenciasService.getSaldoAusenciasInvertido().getNoturnas()));
            saldoExtras.setNoturnas(getSaldoExtrasSubtraiAusencias().getNoturnas().minus(ausenciasService.getSaldoAusenciasInvertido().getDiurnas()));
            if (Utils.getUltrapassouTolerancia(saldoExtras,
                    Utils.getDataPadrao(0, this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada().getToleranciaExtra()).toInstant())) {
                saldoExtras.setPossui(true);
            }
        } else {
            //Não compensa atrasos ou são todas extras
            saldoExtras.setDiurnas(saldoCalculoExtras.getDiurnas());
            saldoExtras.setNoturnas(saldoCalculoExtras.getNoturnas());
            if (!saldoExtras.getDiurnas().plus(saldoExtras.getNoturnas()).isZero()) {
                saldoExtras.setPossui(true);
            }
        }

        saldoExtras = getToleranciaExtras(saldoExtras);
        return saldoExtras;
    }

    /**
     * Só calcula compensação de extras quando for cálculo de período
     * compensação
     *
     * @param saldoExtras
     * @return
     */
    private SaldoExtras getToleranciaExtras(SaldoExtras saldoExtras) {
        if (saldoExtras.isPossui()) {

            //Possui extra pois ultrapassou a tolerância
            if (this.calculo.getRegrasService().isRealizaCompensacao()) {
                if (this.calculo.isPeriodoCompensacaoExtras()) {
                    LOGGER.debug("Compensando dia... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                    saldoExtras = extrasCompensandoDiaService.getIniciaCompensacaoExtras(horasPrevistas, saldoExtras);
                } else //Senão se tiver extras que compensaram, deverá subtrair das extras do dia
                {
                    Date dia = new Date(this.calculo.getDiaProcessado().getTime());
                    if (Utils.getSaldoDiaCompensando(dia, this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()) != null) {
                        saldoExtras.setDiurnas(saldoExtras.getDiurnas().minus(Utils.getSaldoDiaCompensando(dia, this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoDiurnas()));
                        saldoExtras.setNoturnas(saldoExtras.getNoturnas().minus(Utils.getSaldoDiaCompensando(dia, this.calculo.getEntradaAPI().getSaldoDiaCompensandoList()).getCompensandoNoturnas()));
                    }
                }
            }

        } else {
            //Não possui extra
            saldoExtras.setDiurnas(Duration.ZERO);
            saldoExtras.setNoturnas(Duration.ZERO);
        }
        return saldoExtras;
    }

}
