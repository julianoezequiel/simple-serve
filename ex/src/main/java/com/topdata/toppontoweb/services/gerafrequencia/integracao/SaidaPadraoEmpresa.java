package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import java.util.List;

/**
 *
 * @author juliano.ezequiel
 */
public class SaidaPadraoEmpresa {

    private final Empresa empresa;
    private final List<SaidaPadrao> saidaPadraoList;
    private Intervalos intervaloTotal;
    private Saldo saldoNormaisTotal;
    private SaldoExtras saldoExtrasTotal;
    private SaldoAusencias saldoAusenciasTotal;
    private BancodeHorasApi bancodeHorasTotal;
    private Integer quantidadeFaltas;

    public SaidaPadraoEmpresa(Empresa empresa, List<SaidaPadrao> saidaApiPadraoList) {
        this.empresa = empresa;
        this.saidaPadraoList = saidaApiPadraoList;
        calcularTotaisDosIntervalos();
    }

    public Empresa getEmpresa() {
        return empresa;
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
            this.getBancodeHorasTotal().plus(saidaDia.getBancodeHorasTotal());
            this.quantidadeFaltas += saidaDia.getQuantidadeFaltas();
        });
    }

}
