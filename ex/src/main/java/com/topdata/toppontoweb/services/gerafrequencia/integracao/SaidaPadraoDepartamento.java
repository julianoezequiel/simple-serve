package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import java.time.Duration;
import java.util.List;

/**
 *
 * @author juliano.ezequiel
 */
public class SaidaPadraoDepartamento {

    private final Departamento departamento;
    private final List<SaidaPadrao> saidaPadraoList;
    private Intervalos intervaloTotal;
    private Saldo saldoNormaisTotal;
    private SaldoExtras saldoExtrasTotal;
    private SaldoAusencias saldoAusenciasTotal;
    private BancodeHorasApi bancodeHorasTotal;
    private Integer quantidadeFaltas;

    public SaidaPadraoDepartamento(Departamento departamento, List<SaidaPadrao> saidaPadraoList) {
        this.departamento = departamento;
        this.saidaPadraoList = saidaPadraoList;
        this.calcularTotaisDosIntervalos();
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public List<SaidaPadrao> getSaidaPadraoList() {
        return saidaPadraoList;
    }

    /**
     * @return the intervaloTotal
     */
    public Intervalos getIntervaloTotal() {
        return intervaloTotal;
    }

    /**
     * @param intervaloTotal the intervaloTotal to set
     */
    public void setIntervaloTotal(Intervalos intervaloTotal) {
        this.intervaloTotal = intervaloTotal;
    }

    /**
     * @return the saldoNormaisTotal
     */
    public Saldo getSaldoNormaisTotal() {
        return saldoNormaisTotal;
    }

    /**
     * @return the saldoExtrasTotal
     */
    public SaldoExtras getSaldoExtrasTotal() {
        return saldoExtrasTotal;
    }

    /**
     * @return the saldoAusenciasTotal
     */
    public SaldoAusencias getSaldoAusenciasTotal() {
        return saldoAusenciasTotal;
    }

    /**
     * @return the bancodeHorasTotal
     */
    public BancodeHorasApi getBancodeHorasTotal() {
        return bancodeHorasTotal;
    }

    /**
     * @return the quantidadeFaltas
     */
    public Integer getQuantidadeFaltas() {
        return quantidadeFaltas;
    }

    public SaidaDia getUltimoDiaComBancoHorasTotal() {
        SaidaDia result = new SaidaDia();
        BancodeHorasApi bancodeHorasApi = new BancodeHorasApi();
        Duration saldoAnteriorDiaPeriodoBancodeHoras = Duration.ZERO;

        for (SaidaPadrao saidaPadrao : this.saidaPadraoList) {
            if (saidaPadrao.getUltimoDiaComBancoHoras() != null) {
                SaidaDia saidaDia = saidaPadrao.getUltimoDiaComBancoHoras();

                bancodeHorasApi.plus(saidaDia.getBancodeHoras());
                saldoAnteriorDiaPeriodoBancodeHoras = saldoAnteriorDiaPeriodoBancodeHoras.plus(
                        saidaDia.getSaldoAnteriorDiaPeriodoBancodeHoras() != null
                                ? saidaDia.getSaldoAnteriorDiaPeriodoBancodeHoras() : Duration.ZERO);
            }
        }

        result.setBancodeHoras(bancodeHorasApi);
        result.setSaldoAnteriorDiaPeriodoBancodeHoras(saldoAnteriorDiaPeriodoBancodeHoras);

        return result;
    }

    private void calcularTotaisDosIntervalos() {
        this.intervaloTotal = Intervalos.ZERO();
        this.saldoNormaisTotal = new Saldo();
        this.saldoExtrasTotal = new SaldoExtras();
        this.saldoAusenciasTotal = new SaldoAusencias();
        this.bancodeHorasTotal = new BancodeHorasApi();
        this.quantidadeFaltas = 0;
        saidaPadraoList.stream().forEach((saidaDia) -> {
            this.intervaloTotal.plus(saidaDia.getIntervaloTotal());
            this.saldoNormaisTotal.plus(saidaDia.getSaldoNormaisTotal());
            this.saldoExtrasTotal.plus(saidaDia.getSaldoExtrasTotal());
            this.saldoAusenciasTotal.plus(saidaDia.getSaldoAusenciasTotal());
            this.bancodeHorasTotal.plus(saidaDia.getBancodeHorasTotal());
            this.quantidadeFaltas += saidaDia.getQuantidadeFaltas();
        });

        if (!saidaPadraoList.isEmpty()) {
            //Tem que tirar a media do "indice de absenteismo"
            Double indiceAbsenteismo = this.intervaloTotal.getIndiceAbsenteismo() / saidaPadraoList.size();
            this.intervaloTotal.setIndiceAbsenteismo(indiceAbsenteismo);
        }
    }

}
