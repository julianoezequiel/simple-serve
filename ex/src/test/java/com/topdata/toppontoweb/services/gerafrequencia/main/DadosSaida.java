package com.topdata.toppontoweb.services.gerafrequencia.main;

import com.topdata.toppontoweb.services.gerafrequencia.main.Main;
import com.topdata.toppontoweb.services.gerafrequencia.services.calculo.SaidaMainService;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 *
 * @author enio.junior
 */
public class DadosSaida {

    private final SaidaMainService saidaResultadoService;

    public DadosSaida() {
        saidaResultadoService = Main.getResultado().getEntrada().getSaidaMain();
    }

    public String getSaldoNormaisDiurnas() {
        return Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoNormais().getDiurnas());
    }

    public String getSaldoNormaisNoturnas() {
        return Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoNormais().getNoturnas());
    }

    public String getSaldoExtrasDiurnas() {
        return Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoExtras().getDiurnas());
    }

    public String getSaldoExtrasNoturnas() {
        return Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoExtras().getNoturnas());
    }

    public String getSaldoAusenciasDiurnas() {
        return ""; //Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoAusencias().getDiurnas());
    }

    public String getSaldoAusenciasNoturnas() {
        return ""; // Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getSaldoAusencias().getNoturnas());
    }

    public String getSaldoDiaBH() {
        return Utils.FormatoHora(saidaResultadoService.getSaidaDiaList().get(0).getBancodeHoras().getSaldoDia());
    }

}
