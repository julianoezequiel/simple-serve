package com.topdata.toppontoweb.services.gerafrequencia.services.compensacoes;

import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;

/**
 * Controle da lista de ausências compensadas
 *
 * @author enio.junior
 */
public class AusenciasCompensadasService {

    private SaldoDiaCompensado consultaSaldoDiaCompensado;
    private final Calculo calculo;

    public AusenciasCompensadasService(Calculo calculo) {
        this.calculo = calculo;
    }

    /**
     * Se for período de compensação, somente atualiza saldo de ausências
     *
     * @param saldoAusencias
     * @return
     */
    public SaldoAusencias getAusenciasDiaCompensado(SaldoAusencias saldoAusencias) {
        if (this.calculo.isPeriodoCompensacaoAusencias()) {
            //Período de compensação então grava na lista o saldo de ausências do período                
            gravaSaldoAusenciasPeriodoCompensacao(saldoAusencias);
        } else {
            //Nesse momento, está calculando o saldo final de compensadas de acordo com as regras
            calculaSaldoFinalCompensadas(saldoAusencias);
        }
        return saldoAusencias;
    }

    /**
     * Período de compensação então grava na lista o saldo de ausências do
     * período
     *
     * @param saldoAusencias
     */
    private void gravaSaldoAusenciasPeriodoCompensacao(SaldoAusencias saldoAusencias) {
        this.calculo.getEntradaAPI().getSaldoDiaCompensadoList()
                .stream()
                .filter(f -> (f.getDataCompensada().getDate() == this.calculo.getDiaProcessado().getDate()))
                .forEach((SaldoDiaCompensado saldoDiaCompensado) -> {
                    if (this.calculo.getRegrasService().isTrabalhouDiaACompensar()) {
                        saldoDiaCompensado.setStatus(CONSTANTES.TRABALHOU_NO_DIA);
                    } else if (saldoAusencias != null) {
                        saldoDiaCompensado.setStatus(CONSTANTES.OK);
                        saldoDiaCompensado.setDiurnas(saldoAusencias.getDiurnas());
                        saldoDiaCompensado.setNoturnas(saldoAusencias.getNoturnas());
                        saldoDiaCompensado.setACompensarDiurnas(saldoAusencias.getDiurnas());
                        saldoDiaCompensado.setACompensarNoturnas(saldoAusencias.getNoturnas());
                    }
                });
    }

    /**
     * Consulta o dia Compensado, saldo de ausências atualizado já compensado
     * para esse dia
     *
     * @param saldoAusencias
     */
    private void calculaSaldoFinalCompensadas(SaldoAusencias saldoAusencias) {
        consultaSaldoDiaCompensado = Utils.getSaldoDiaCompensado(this.calculo.getDiaProcessado(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList());

        Duration totalCompensadas = consultaSaldoDiaCompensado.getCompensadasDiurnas().plus(consultaSaldoDiaCompensado.getCompensadasNoturnas());
        if (saldoAusencias.getDiurnas().toMillis() > totalCompensadas.toMillis()) {
            saldoAusencias.setDiurnas(saldoAusencias.getDiurnas().minus(totalCompensadas));
            consultaSaldoDiaCompensado.setCompensadasDiurnas(totalCompensadas);
            consultaSaldoDiaCompensado.setACompensarDiurnas(saldoAusencias.getDiurnas());
        } else {
            consultaSaldoDiaCompensado.setCompensadasDiurnas(saldoAusencias.getDiurnas());
        }

        Duration noturnas;
        noturnas = totalCompensadas.minus(consultaSaldoDiaCompensado.getCompensadasDiurnas());
        if (saldoAusencias.getNoturnas().toMillis() > noturnas.toMillis()) {
            saldoAusencias.setNoturnas(saldoAusencias.getNoturnas().minus(noturnas));
            consultaSaldoDiaCompensado.setCompensadasNoturnas(noturnas);
            consultaSaldoDiaCompensado.setACompensarNoturnas(saldoAusencias.getNoturnas());
        } else {
            consultaSaldoDiaCompensado.setCompensadasNoturnas(saldoAusencias.getNoturnas());
        }
    }

}
