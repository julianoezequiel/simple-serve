package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.List;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import java.time.Duration;

/**
 *
 * @author tharle.camargo
 */
public class SaidaPadraoEmpresaDepartamento {

    private final Empresa empresa;
    private final List<SaidaPadraoDepartamento> saidaPadraoDepartamentoList;
    private Intervalos intervaloTotal;
    private BancodeHorasApi bancodeHorasTotal;
    private Saldo saldoNormaisTotal;

    public SaidaPadraoEmpresaDepartamento(Empresa empresa, List<SaidaPadraoDepartamento> saidaPadraoDepartamentoList) {
        this.empresa = empresa;
        this.saidaPadraoDepartamentoList = saidaPadraoDepartamentoList;
        this.calcularTotaisDosIntervalos();
    }

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @return the saidaPadraoDepartamentoList
     */
    public List<SaidaPadraoDepartamento> getSaidaPadraoDepartamentoList() {
        return saidaPadraoDepartamentoList;
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
     * @return the bancodeHorasTotal
     */
    public BancodeHorasApi getBancodeHorasTotal() {
        return bancodeHorasTotal;
    }

    public Saldo getSaldoNormaisTotal() {
        return saldoNormaisTotal;
    }

    public SaidaDia getUltimoDiaComBancoHorasTotal() {
        SaidaDia result = new SaidaDia();
        BancodeHorasApi bancodeHorasApi = new BancodeHorasApi();
        Duration saldoAnteriorDiaPeriodoBancodeHoras = Duration.ZERO;

        for (SaidaPadraoDepartamento saidaPadrao : this.saidaPadraoDepartamentoList) {
            if (saidaPadrao.getUltimoDiaComBancoHorasTotal() != null) {
                SaidaDia saidaDia = saidaPadrao.getUltimoDiaComBancoHorasTotal();

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
        this.bancodeHorasTotal = new BancodeHorasApi();
        this.saldoNormaisTotal = new Saldo();
        saidaPadraoDepartamentoList.stream().forEach((saidaDia) -> {
            this.intervaloTotal.plus(saidaDia.getIntervaloTotal());
            this.bancodeHorasTotal.plus(saidaDia.getBancodeHorasTotal());
            this.saldoNormaisTotal.plus(saidaDia.getSaldoNormaisTotal());
        });

        if (!saidaPadraoDepartamentoList.isEmpty()) {
            //Tem que tirar a media do "indice de absenteismo"
            Double indiceAbsenteismo = this.intervaloTotal.getIndiceAbsenteismo() / saidaPadraoDepartamentoList.size();
            this.intervaloTotal.setIndiceAbsenteismo(indiceAbsenteismo);
        }
    }

}
