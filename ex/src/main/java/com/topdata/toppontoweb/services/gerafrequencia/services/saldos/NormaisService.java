package com.topdata.toppontoweb.services.gerafrequencia.services.saldos;

import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Calcula o saldo de horas normais do dia
 *
 * @author enio.junior
 */
public class NormaisService {

    public final static Logger LOGGER = LoggerFactory.getLogger(NormaisService.class.getName());
    private final Saldo horasPrevistas;
    private final SaldoTrabalhadas horasTrabalhadas;
    private final SaldoExtras saldoExtras;
    private final SaldoAusencias saldoAusencias;
    private final Calculo calculo;

    public NormaisService(Saldo horasPrevistas, SaldoTrabalhadas horasTrabalhadas, SaldoExtras saldoExtras, SaldoAusencias saldoAusencias, Calculo calculo) {
        this.calculo = calculo;
        this.horasPrevistas = horasPrevistas;
        this.horasTrabalhadas = horasTrabalhadas;
        this.saldoExtras = saldoExtras;
        this.saldoAusencias = saldoAusencias;
    }

    public Saldo getSaldoNormais() {
        LOGGER.debug("Calculando os saldos de horas normais... dia: {} - funcionário: {} ", this.calculo.getDiaProcessado(), this.calculo.getNomeFuncionario());
        Saldo saldo = new Saldo();

        if (horasPrevistas != null && horasTrabalhadas != null) {
            //Se atingiram a tolerância
            if (this.saldoExtras.isPossui() || this.saldoAusencias.isPossui()) {
                if (horasPrevistas.getDiurnas().toMillis() < horasTrabalhadas.getDiurnas().toMillis()) {
                    saldo.setDiurnas(horasPrevistas.getDiurnas());
                } else {
                    saldo.setDiurnas(horasTrabalhadas.getDiurnas());
                }
                if (horasPrevistas.getNoturnas().toMillis() < horasTrabalhadas.getNoturnas().toMillis()) {
                    saldo.setNoturnas(horasPrevistas.getNoturnas());
                } else {
                    saldo.setNoturnas(horasTrabalhadas.getNoturnas());
                }
            } else {
                //Mostrar as horas trabalhadas que não atingiram a tolerância para extras ou ausências
                saldo.setDiurnas(horasTrabalhadas.getDiurnas());
                saldo.setNoturnas(horasTrabalhadas.getNoturnas());
            }
        }

        //compensa atrasos                
        if (saldoExtras != null && saldoExtras.isTodas()) {
            saldo.setDiurnas(Duration.ZERO);
            saldo.setNoturnas(Duration.ZERO);
        }

        /**
         * O flag "Não paga adicional noturno nas horas normais" foi desativado
         * Então não precisa eliminar o adicional, somente subtrair Quando
         * possui horas compensadas
         */
        if (this.calculo.getRegrasService().isDiaCompensadoSemSomenteJustificativa()
                && (Utils
                .getSaldoDiaCompensado(this.calculo.getDiaProcessado(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas().isZero() == false
                || Utils
                .getSaldoDiaCompensado(this.calculo.getDiaProcessado(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas().isZero() == false)) {

            saldo.setDiurnas(saldo.getDiurnas().plus(Utils
                    .getSaldoDiaCompensado(this.calculo.getDiaProcessado(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasDiurnas()));
            saldo.setNoturnas(saldo.getNoturnas().plus(Utils
                    .getSaldoDiaCompensado(this.calculo.getDiaProcessado(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()).getCompensadasNoturnas()));
        }

        //Quando está afastado com abono
        if (this.calculo.getRegrasService().isAfastadoComAbono()
                && saldoAusencias.getAbonoDia() != null) {
            saldo.setDiurnas(saldo.getDiurnas().plus(saldoAusencias.getAbonoDia().getDiurnas()));
            saldo.setNoturnas(saldo.getNoturnas().plus(saldoAusencias.getAbonoDia().getNoturnas()));
        }

        /**
         * Quando é somente abono (sem afastamento) não vira horas normais (Não
         * faz nada) Se possui saldo normais
         */
        if (saldo.getDiurnas().plus(saldo.getNoturnas()).isZero()) {
            saldo.setPossui(false);
        } else {
            saldo.setPossui(true);
        }

        return saldo;
    }

}
