package com.topdata.toppontoweb.services.gerafrequencia.services.compensacoes;

import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensado;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoDiaCompensando;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;

/**
 * Controle da lista de extras compensando
 *
 * @author enio.junior
 */
public class ExtrasCompensandoDiaService {

    private SaldoDiaCompensado saldoDiaCompensado;
    private SaldoDiaCompensando saldoDiaCompensando;
    private final Calculo calculo;

    public ExtrasCompensandoDiaService(Calculo calculo) {
        this.calculo = calculo;
    }

    public SaldoExtras getIniciaCompensacaoExtras(Saldo horasPrevistas, SaldoExtras saldoExtras) {

        //Consultar o limite diário que pode compensar
        Duration limiteDia = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(this.calculo.getFuncionarioService().
                getFuncionarioCompensacoesService().getCompensandoDia().getLimiteDiario()).toInstant());

        //Verificar o saldo de ausência que ainda tem pra compensar do dia a ser compensado
        saldoDiaCompensado = Utils.getSaldoDiaCompensado(this.calculo.getFuncionarioService().getFuncionarioCompensacoesService().getCompensandoDia().getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList());

        //Instanciar o dia que está compensando
        saldoDiaCompensando = new SaldoDiaCompensando();
        saldoDiaCompensando.setDataCompensando(this.calculo.getDiaProcessado());

        //Retornar quantas horas ainda precisa para compensação
        //vai diminuindo o saldo a medida que compensa
        Duration saldoACompensar = getSaldoACompensar();

        //Diminuir o saldo do dia compensado
        processaCompensacao(horasPrevistas, saldoExtras, saldoACompensar, limiteDia);

        //Insere as horas que esse dia compensou na lista de compensandos
        insereListaDiasqueCompensaram();

        return saldoExtras;
    }

    private Duration getSaldoACompensar() {
        Duration saldoACompensar = Duration.ZERO;
        if (saldoDiaCompensado.getACompensarDiurnas() != null) {
            saldoACompensar = saldoDiaCompensado.getACompensarDiurnas();
        }
        if (saldoDiaCompensado.getACompensarNoturnas() != null) {
            saldoACompensar = saldoACompensar.plus(saldoDiaCompensado.getACompensarNoturnas());
        }
        return saldoACompensar;
    }

    /**
     * Se as horas diurnas previstas da jornada for maior que as noturnas Então
     * primeiro noturnas Senão primeiro diurnas
     *
     * @param horasPrevistas
     * @param saldoExtras
     * @param saldoACompensar
     * @param limiteDia
     */
    private void processaCompensacao(Saldo horasPrevistas, SaldoExtras saldoExtras, Duration saldoACompensar, Duration limiteDia) {
        if (horasPrevistas.getDiurnas().toMillis() > horasPrevistas.getNoturnas().toMillis()) {
            //Primeiro as noturnas
            saldoExtras = getRealizaCompensacao(CONSTANTES.PRIMEIRO_NOTURNAS, saldoACompensar, limiteDia, saldoExtras);
            saldoExtras = getRealizaCompensacao(CONSTANTES.PRIMEIRO_DIURNAS,
                    saldoACompensar.minus(saldoDiaCompensando.getCompensandoNoturnas()), limiteDia.minus(saldoDiaCompensando.getCompensandoNoturnas()), saldoExtras);
        } else {
            //Primeiro as diurnas
            saldoExtras = getRealizaCompensacao(CONSTANTES.PRIMEIRO_DIURNAS, saldoACompensar, limiteDia, saldoExtras);
            saldoExtras = getRealizaCompensacao(CONSTANTES.PRIMEIRO_NOTURNAS,
                    saldoACompensar.minus(saldoDiaCompensando.getCompensandoDiurnas()), limiteDia.minus(saldoDiaCompensando.getCompensandoDiurnas()), saldoExtras);
        }
    }

    /**
     * Só fica nessa lista os dias que realmente compensaram algumas horas, se
     * não tiver na lista não teve horas as compensar.
     */
    private void insereListaDiasqueCompensaram() {
        if (!saldoDiaCompensando.getCompensandoDiurnas().isZero()
                || !saldoDiaCompensando.getCompensandoNoturnas().isZero()) {
            this.calculo.getEntradaAPI().getSaldoDiaCompensandoList().add(saldoDiaCompensando);
        }
    }

    private SaldoExtras getRealizaCompensacao(String operacao, Duration saldoACompensar, Duration limiteDia, SaldoExtras saldoExtras) {

        //Saldo de horas a compensar 
        Duration horasACompensar;
        if (operacao.equals(CONSTANTES.PRIMEIRO_DIURNAS)) {
            horasACompensar = saldoExtras.getDiurnas();
        } else {
            horasACompensar = saldoExtras.getNoturnas();
        }

        if (saldoACompensar.toMillis() > 0) {

            //Se quantidade necessária for maior que o limite máximo do dia
            if (saldoACompensar.toMillis() > limiteDia.toMillis()) {

                //Quantidade necessária recebe limite máximo do dia
                saldoACompensar = limiteDia;
            }

            //Se quantidade de horas compensando for maior do que precisa
            if (horasACompensar.toMillis() > saldoACompensar.toMillis()) {
                //Recebe o limite
                horasACompensar = saldoACompensar;
            }

            //Subtrair saldo das horas de ausência do dia e subtrair das extras
            SubtrairSaldoHorasAusenciaDiaeExtras(horasACompensar, saldoExtras, operacao);
        }

        return saldoExtras;
    }

    private void SubtrairSaldoHorasAusenciaDiaeExtras(Duration horasACompensar, SaldoExtras saldoExtras, String operacao) {
        Duration faltaCompensar;
        SaldoDiaCompensado consultaSaldoDiaCompensado = Utils.getSaldoDiaCompensado(saldoDiaCompensado.getDataCompensada(), this.calculo.getEntradaAPI().getSaldoDiaCompensadoList());
        //Subtrair saldo das horas de ausência do dia e subtrair das extras
        if (operacao.equals(CONSTANTES.PRIMEIRO_DIURNAS)) {
            saldoExtras.setDiurnas(saldoExtras.getDiurnas().minus(horasACompensar));
            saldoDiaCompensando.setCompensandoDiurnas(horasACompensar);

            if (consultaSaldoDiaCompensado.getCompensadasDiurnas() != null) {
                consultaSaldoDiaCompensado.setCompensadasDiurnas(consultaSaldoDiaCompensado.getCompensadasDiurnas().plus(horasACompensar));
            } else {
                consultaSaldoDiaCompensado.setCompensadasDiurnas(horasACompensar);
            }

            faltaCompensar = consultaSaldoDiaCompensado.getDiurnas().minus(consultaSaldoDiaCompensado.getCompensadasDiurnas());
            consultaSaldoDiaCompensado.setACompensarDiurnas(faltaCompensar);

        } else {
            saldoExtras.setNoturnas(saldoExtras.getNoturnas().minus(horasACompensar));
            saldoDiaCompensando.setCompensandoNoturnas(horasACompensar);

            if (consultaSaldoDiaCompensado.getCompensadasNoturnas() != null) {
                consultaSaldoDiaCompensado.setCompensadasNoturnas(consultaSaldoDiaCompensado.getCompensadasNoturnas().plus(horasACompensar));
            } else {
                consultaSaldoDiaCompensado.setCompensadasNoturnas(horasACompensar);
            }

            //Está compensando primeiro pelas noturnas, mas o dia que está sendo compensado
            //não possui noturnas
            if (consultaSaldoDiaCompensado.getNoturnas().minus(consultaSaldoDiaCompensado.getCompensadasNoturnas()).isNegative()) {
                faltaCompensar = Duration.ZERO;
            } else {
                faltaCompensar = consultaSaldoDiaCompensado.getNoturnas().minus(consultaSaldoDiaCompensado.getCompensadasNoturnas());
            }
            consultaSaldoDiaCompensado.setACompensarNoturnas(faltaCompensar);
        }
    }

}
