package com.topdata.toppontoweb.services.gerafrequencia.services.saldos;

import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.services.compensacoes.AusenciasCompensadasService;
import com.topdata.toppontoweb.services.gerafrequencia.services.regras.AbonoDiaService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calcula o saldo de horas de ausência do dia
 *
 * @author enio.junior
 */
public class AusenciasService {

    public final static Logger LOGGER = LoggerFactory.getLogger(AusenciasService.class.getName());
    private final Saldo horasPrevistas;
    private final SaldoTrabalhadas horasTrabalhadas;
    private final AbonoDiaService abonoDiaService;
    private final AusenciasCompensadasService ausenciasCompensadasService;
    private final Calculo calculo;
    private SaldoAusencias saldoCalculoAusencias;

    public AusenciasService(Saldo horasPrevistas, SaldoTrabalhadas horasTrabalhadas, Calculo calculo) {
        this.calculo = calculo;
        this.horasPrevistas = horasPrevistas;
        this.horasTrabalhadas = horasTrabalhadas;
        this.abonoDiaService = new AbonoDiaService(this.calculo);
        this.ausenciasCompensadasService = new AusenciasCompensadasService(this.calculo);
    }

    /**
     * Só possui ausência se tiver que cumprir horário
     *
     * @return
     */
    public SaldoAusencias getCalculaAusencias() {
        SaldoAusencias saldoBruto = new SaldoAusencias();
        if (this.calculo.getRegrasService().isCompensaAtrasos()) {
            if (this.calculo.getRegrasService().isCumprirHorario()) {
                setCumprirHorario(saldoBruto);
            } else {
                setSemCumprirHorario(saldoBruto);
            }
        } else {
            setNaoCompensaAtrasos(saldoBruto);
        }
        return saldoBruto;
    }

    private void setCumprirHorario(SaldoAusencias saldoAusencias) {
        if (horasTrabalhadas != null) {
            //Se saldo horas for negativo
            if (horasTrabalhadas.getDiurnas().toMillis() < horasPrevistas.getDiurnas().toMillis()) {
                saldoAusencias.setDiurnas(horasPrevistas.getDiurnas().minus(horasTrabalhadas.getDiurnas()));
            }
            if (horasTrabalhadas.getNoturnas().toMillis() < horasPrevistas.getNoturnas().toMillis()) {
                saldoAusencias.setNoturnas(horasPrevistas.getNoturnas().minus(horasTrabalhadas.getNoturnas()));
            }
        } else {
            saldoAusencias.setDiurnas(horasPrevistas.getDiurnas());
            saldoAusencias.setNoturnas(horasPrevistas.getNoturnas());
        }
    }

    private void setSemCumprirHorario(SaldoAusencias saldoAusencias) {
        if (horasTrabalhadas != null && horasTrabalhadas.getAusencias() != null) {
            if (horasTrabalhadas.getAusencias().getDiurnas().toMillis() < horasPrevistas.getDiurnas().toMillis()) {
                saldoAusencias.setDiurnas(horasPrevistas.getDiurnas().minus(horasTrabalhadas.getAusencias().getDiurnas()));
            }
            if (horasTrabalhadas.getAusencias().getNoturnas().toMillis() < horasPrevistas.getNoturnas().toMillis()) {
                saldoAusencias.setNoturnas(horasPrevistas.getNoturnas().minus(horasTrabalhadas.getAusencias().getNoturnas()));
            }
        } else {
            saldoAusencias.setDiurnas(horasPrevistas.getDiurnas());
            saldoAusencias.setNoturnas(horasPrevistas.getNoturnas());
        }
    }

    private void setNaoCompensaAtrasos(SaldoAusencias saldoAusencias) {
        if (horasTrabalhadas != null) {
            if (horasTrabalhadas.getAusencias() != null) {
                saldoAusencias.setDiurnas(horasTrabalhadas.getAusencias().getDiurnas());
                saldoAusencias.setNoturnas(horasTrabalhadas.getAusencias().getNoturnas());
            }
        }
    }

    private SaldoAusencias getSaldoAusenciasSubtraiExtras() {
        SaldoAusencias saldoAusencias = new SaldoAusencias();
        ExtrasService extrasService = new ExtrasService(horasPrevistas, horasTrabalhadas, this.calculo);
        SaldoExtras saldoCalculoExtras = extrasService.getCalculaExtras();
        saldoCalculoAusencias = getCalculaAusencias();

        if (this.calculo.getRegrasService().isCompensaAtrasos()) {
            //Subtrai Extras
            if (saldoCalculoAusencias.getDiurnas().toMillis() > saldoCalculoExtras.getDiurnas().toMillis()) {
                saldoAusencias.setDiurnas(saldoCalculoAusencias.getDiurnas().minus(saldoCalculoExtras.getDiurnas()));
            }
            if (saldoCalculoAusencias.getNoturnas().toMillis() > saldoCalculoExtras.getNoturnas().toMillis()) {
                saldoAusencias.setNoturnas(saldoCalculoAusencias.getNoturnas().minus(saldoCalculoExtras.getNoturnas()));
            }
        }
        return saldoAusencias;
    }

    public SaldoAusencias getSaldoAusenciasInvertido() {
        SaldoAusencias saldoAusencias = new SaldoAusencias();
        ExtrasService extrasService = new ExtrasService(horasPrevistas, horasTrabalhadas, this.calculo);
        if (this.calculo.getRegrasService().isCompensaAtrasos()) {
            //Invertido
            //Diurnas
            if (extrasService.getSaldoExtrasSubtraiAusencias().getNoturnas().toMillis() > 0) {
                if (getSaldoAusenciasSubtraiExtras().getDiurnas().toMillis() > 0) {
                    if (getSaldoAusenciasSubtraiExtras().getDiurnas().toMillis() > extrasService.getSaldoExtrasSubtraiAusencias().getNoturnas().toMillis()) {
                        saldoAusencias.setDiurnas(extrasService.getSaldoExtrasSubtraiAusencias().getNoturnas());
                    } else {
                        saldoAusencias.setDiurnas(getSaldoAusenciasSubtraiExtras().getDiurnas());
                    }
                }
            }
            //Noturnas
            if (extrasService.getSaldoExtrasSubtraiAusencias().getDiurnas().toMillis() > 0) {
                if (getSaldoAusenciasSubtraiExtras().getNoturnas().toMillis() > 0) {
                    if (getSaldoAusenciasSubtraiExtras().getNoturnas().toMillis() > extrasService.getSaldoExtrasSubtraiAusencias().getDiurnas().toMillis()) {
                        saldoAusencias.setNoturnas(extrasService.getSaldoExtrasSubtraiAusencias().getDiurnas());
                    } else {
                        saldoAusencias.setNoturnas(getSaldoAusenciasSubtraiExtras().getNoturnas());
                    }
                }
            }
        }
        return saldoAusencias;
    }

    public SaldoAusencias getSaldoAusencias() {
        LOGGER.debug("Calculando os saldos de ausências... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        saldoCalculoAusencias = getCalculaAusencias();
        SaldoAusencias saldoAusencias = new SaldoAusencias();
        boolean possuiAusencia = false;

        //Se no dia que tem jornada é feriado, então não tem ausências
        if (!this.calculo.getRegrasService().isCumprirHorarioComFeriado()) {
            if (this.calculo.getRegrasService().isCompensaAtrasos()) {
                //Extras - invertido
                saldoAusencias.setDiurnas(getSaldoAusenciasSubtraiExtras().getDiurnas().minus(getSaldoAusenciasInvertido().getDiurnas()));
                saldoAusencias.setNoturnas(getSaldoAusenciasSubtraiExtras().getNoturnas().minus(getSaldoAusenciasInvertido().getNoturnas()));
                if (Utils.getUltrapassouTolerancia(saldoAusencias,
                        Utils.getDataPadrao(0, this.calculo.getFuncionarioService().getFuncionarioJornadaService().getJornada().getToleranciaAusencia()).toInstant())
                        || this.calculo.getRegrasService().isFalta()) {
                    possuiAusencia = true;
                }
            } else {
                //Não compensa atrasos            
                if (this.calculo.getRegrasService().isFalta()) {
                    saldoAusencias.setDiurnas(horasPrevistas.getDiurnas());
                    saldoAusencias.setNoturnas(horasPrevistas.getNoturnas());
                } else {
                    saldoAusencias.setDiurnas(saldoCalculoAusencias.getDiurnas());
                    saldoAusencias.setNoturnas(saldoCalculoAusencias.getNoturnas());
                }
                if (saldoAusencias.getDiurnas().plus(saldoAusencias.getNoturnas()).toMillis() > 0
                        || this.calculo.getRegrasService().isFalta()) {
                    possuiAusencia = true;
                }
            }
        }

        saldoAusencias = getToleranciaAusencias(possuiAusencia, saldoAusencias);

        if (!saldoAusencias.getDiurnas().plus(saldoAusencias.getNoturnas()).isZero()
                && this.calculo.getEntradaAPI().isProcessaCalculo()
                && this.calculo.getEntradaAPI().isProcessaAbsenteismo()) {
            this.calculo.getIntervalosService().setCalculaIndiceAbsenteismo(horasPrevistas, saldoAusencias);
        }

        return saldoAusencias;
    }

    private SaldoAusencias getToleranciaAusencias(boolean possuiAusencia, SaldoAusencias saldoAusencias) {
        if (possuiAusencia) {
            //Possui ausência pois ultrapassou a tolerância                
            saldoAusencias.setPossui(true);
            if (this.calculo.getRegrasService().isAbonoSemSomenteJustificativa()) {
                LOGGER.debug("Possui abono... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                saldoAusencias = abonoDiaService.getAbono(saldoAusencias);
            } else if (this.calculo.getRegrasService().isAfastadoComAbono()) {
                LOGGER.debug("Possui afastamento com abono... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                saldoAusencias = abonoDiaService.getAfastadoComAbono(saldoAusencias);
            } else if (this.calculo.getRegrasService().isDiaCompensadoSemSomenteJustificativa()) {
                LOGGER.debug("Dia compensado... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
                saldoAusencias = ausenciasCompensadasService.getAusenciasDiaCompensado(saldoAusencias);
            }
        } else {
            //Não possui ausência
            saldoAusencias.setDiurnas(Duration.ZERO);
            saldoAusencias.setNoturnas(Duration.ZERO);
        }
        return saldoAusencias;
    }

}
