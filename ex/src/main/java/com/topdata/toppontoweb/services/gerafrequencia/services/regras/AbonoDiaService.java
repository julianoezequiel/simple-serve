package com.topdata.toppontoweb.services.gerafrequencia.services.regras;

import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;

/**
 * Se funcionário possui abono no dia (parcial / integral / afastamento)
 *
 * @author enio.junior
 */
public class AbonoDiaService {

    private Abono abono;
    private final Calculo calculo;

    public AbonoDiaService(Calculo calculo) {
        this.calculo = calculo;
    }

    public SaldoAusencias getAbono(SaldoAusencias saldoAusencias) {

        //Abona tudo ou parcial, vira horas normais
        Saldo saldoAbono = new Saldo();
        saldoAbono.setPossui(true);

        abono = this.calculo.getFuncionarioService().getFuncionarioAbonoService().getAbono();
        Duration horasAbonadasDiurnas = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(abono.getHorasDiurnas()).toInstant());
        Duration horasAbonadasNoturnas = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(abono.getHorasNoturnas()).toInstant());

        if (horasAbonadasDiurnas.toMillis() > saldoAusencias.getDiurnas().toMillis()) {
            saldoAbono.setDiurnas(saldoAusencias.getDiurnas());
        } else {
            saldoAbono.setDiurnas(horasAbonadasDiurnas);
        }
        if (horasAbonadasNoturnas.toMillis() > saldoAusencias.getNoturnas().toMillis()) {
            saldoAbono.setNoturnas(saldoAusencias.getNoturnas());
        } else {
            saldoAbono.setNoturnas(horasAbonadasNoturnas);
        }
        saldoAusencias.setAbonoDia(saldoAbono);

        //Subtrai as horas abonadas    
        saldoAusencias.setDiurnas(saldoAusencias.getDiurnas().minus(saldoAbono.getDiurnas()));
        saldoAusencias.setNoturnas(saldoAusencias.getNoturnas().minus(saldoAbono.getNoturnas()));

        if (saldoAusencias.getDiurnas() == Duration.ZERO
                && saldoAusencias.getNoturnas() == Duration.ZERO) {
            setZeraHoras(saldoAusencias);
        }

        return saldoAusencias;
    }

    /**
     * Abona tudo, vira horas normais
     *
     * @param saldoAusencias
     * @return
     */
    public SaldoAusencias getAfastadoComAbono(SaldoAusencias saldoAusencias) {
        Saldo saldoAbono = new Saldo();
        saldoAbono.setPossui(true);
        saldoAbono.setDiurnas(saldoAusencias.getDiurnas());
        saldoAbono.setNoturnas(saldoAusencias.getNoturnas());
        saldoAusencias.setAbonoDia(saldoAbono);
        setZeraHoras(saldoAusencias);
        return saldoAusencias;
    }

    private void setZeraHoras(SaldoAusencias saldoAusencias) {
        saldoAusencias.setPossui(false);
        saldoAusencias.setDiurnas(Duration.ZERO);
        saldoAusencias.setNoturnas(Duration.ZERO);
    }

}
