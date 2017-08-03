package com.topdata.toppontoweb.services.gerafrequencia.services.bancodehoras;

import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controle das horas quando o banco de horas for do tipo mensal
 *
 * @author enio.junior
 */
public class SaldosBHMensal {

    private BancoHoras bancoHoras;
    private final Calculo calculo;

    public SaldosBHMensal(Calculo calculo) {
        this.bancoHoras = null;
        this.calculo = calculo;
    }

    public BancoHoras getBancoHoras() {
        return this.bancoHoras;
    }

    public void setBancoHoras(BancoHoras bancoHoras) {
        this.bancoHoras = bancoHoras;
    }

    public List<SequenciaPercentuais> getPercentuaisTipoDiaBancoDeHoras(boolean feriado, boolean realizaCompensacao) {
        if (this.bancoHoras.getPercentuaisAcrescimo() != null) {
            List<SequenciaPercentuais> sequenciaPercentuaisList = this.bancoHoras
                    .getPercentuaisAcrescimo()
                    .getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getTipoDia().getIdTipodia().compareTo(this.calculo.getRegrasService().getTipoDia().getIdTipodia()) == 0))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());
            return sequenciaPercentuaisList;
        }
        return new ArrayList<>();
    }

}
