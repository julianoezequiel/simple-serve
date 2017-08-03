package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHorasFechamento;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

/**
 * Informações do fechamento realizado via cadastro funcionário / banco de horas
 * / fechamentos
 *
 * @author enio.junior
 */
public class FuncionarioBancodeHorasFechamentoService {

    private final Calculo calculo;

    public FuncionarioBancodeHorasFechamentoService(Calculo calculo) {
        this.calculo = calculo;
    }

    /**
     * Somente para Acerto e Edição de Saldo Operações crédito e débito
     * fechamento
     *
     * @param bancodeHoras
     */
    public void setFechamentoHorasNoDia(BancodeHorasApi bancodeHoras) {
        if (this.calculo.getRegrasService().isFechamentoBHAcerto()
                || this.calculo.getRegrasService().isFechamentoBHEdicaodeSaldo()) {
            FuncionarioBancoHorasFechamento fechamentoNoDia = getFechamentoNoDia();
            bancodeHoras.setCreditoFechamento(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoNoDia.getCredito()).toInstant()));
            bancodeHoras.setDebitoFechamento(Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fechamentoNoDia.getDebito()).toInstant()));
        } else {
            bancodeHoras.setCreditoFechamento(Duration.ZERO);
            bancodeHoras.setDebitoFechamento(Duration.ZERO);
        }
    }

    /**
     * Retorna o saldo atualizado de BH para iniciar os cálculos, Consulta se
     * subtotal em banco de horas fechamento ou se tem fechamento de empresa
     *
     * @return
     */
    public Date getConsultaPrimeiroSubtotalBH() {
        FuncionarioBancoHorasFechamento fbhf = getFechamentoPrimeiroSubtotal();
        Date dataSubtotalBH = null;

        if (fbhf != null && fbhf.getCredito() != null && fbhf.getDebito() != null) {

            //Se tiver subtotal a partir desse
            dataSubtotalBH = fbhf.getDataFechamento();

            //Atualizar o primeiro saldo para iniciar o cálculo              
            Duration creditoFechamento = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fbhf.getCredito()).toInstant());
            Duration debitoFechamento = Utils.getHorasAcima24horas(Utils.getDataAjustaDiferenca3horas(fbhf.getDebito()).toInstant());
            this.calculo.setCreditoAcumuladoDiaAnteriorBancoDeHoras(creditoFechamento);
            this.calculo.setDebitoAcumuladoDiaAnteriorBancoDeHoras(debitoFechamento);
            this.calculo.setSaldoAcumuladoDiaAnteriorBancoDeHoras(creditoFechamento.minus(debitoFechamento));
            this.calculo.setDiaComSubtotal(fbhf.getDataFechamento());
        } else //Calcular a partir do primeiro dia cadastrado
         if (this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras() != null) {
                dataSubtotalBH = Utils.DiminuiDiasData(this.calculo.getFuncionarioService()
                        .getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras()
                        .getDataInicio(), 1);
            }

        Date dataUltimoFechamento = this.calculo.getFuncionarioService().getFuncionarioEmpresaFechamentoService().getDataUltimoFechamento();

        if (dataSubtotalBH != null && dataUltimoFechamento != null) {
            if (dataSubtotalBH.getDate() > dataUltimoFechamento.getDate()) {
                return dataSubtotalBH;
            } else {
                return dataUltimoFechamento;
            }
        }

        if (dataSubtotalBH != null) {
            return dataSubtotalBH;
        }

        if (dataUltimoFechamento != null) {
            return dataUltimoFechamento;
        }
        return null;
    }
    
    /**
     * Sempre vai ter que existir um subtotal É requisito na tela de cadastro ao
     * se vincular um banco de horas ao funcionário
     *
     * @return
     */
    public FuncionarioBancoHorasFechamento getFechamentoPrimeiroSubtotal() {
        if (this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getPrimeiroFuncionarioBancoDeHoras() != null) {
            FuncionarioBancoHoras fbh = this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getPrimeiroFuncionarioBancoDeHoras();
            if (fbh != null
                    && fbh.getFuncionarioBancoHorasFechamentoList() != null) {
                
                return fbh.getFuncionarioBancoHorasFechamentoList()
                    .stream()
                    .filter(f -> (f.getFuncionarioBancoHoras() != null
                            && f.getFuncionarioBancoHoras().getIdFuncionarioBancoHoras() != null
                            && f.getDataFechamento().compareTo(this.calculo.getDiaProcessado()) <= 0
                            && f.getTipoFechamento().getDescricao().equals(CONSTANTES.FECHAMENTOBH_SUBTOTAL)))
                    .sorted(Comparator.comparing(FuncionarioBancoHorasFechamento::getDataFechamento).reversed())
                    .findFirst()
                    .orElse(new FuncionarioBancoHorasFechamento());
            }
        }
        return new FuncionarioBancoHorasFechamento();
    }

    /**
     * Deverá consultar esses valores quando existir acerto ou edição de saldo
     * no dia
     *
     * @return
     */
    public FuncionarioBancoHorasFechamento getFechamentoNoDia() {
        if (this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras() != null) {
            FuncionarioBancoHoras fbh = this.calculo.getFuncionarioService().getFuncionarioBancodeHorasService().getFuncionarioBancoDeHoras();
            if (fbh != null
                    && fbh.getFuncionarioBancoHorasFechamentoList() != null) {
                
                return fbh.getFuncionarioBancoHorasFechamentoList()
                    .stream()
                    .filter(f -> (f.getFuncionarioBancoHoras() != null
                            && f.getFuncionarioBancoHoras().getIdFuncionarioBancoHoras() != null
                            && f.getDataFechamento().compareTo(this.calculo.getDiaProcessado()) == 0
                            && (f.getTipoFechamento().getDescricao().equals(CONSTANTES.FECHAMENTOBH_ACERTO)
                                    || f.getTipoFechamento().getDescricao().equals(CONSTANTES.FECHAMENTOBH_EDICAODESALDO))
                            && Objects.equals(f.getFuncionarioBancoHoras().getIdFuncionarioBancoHoras(),
                                    this.calculo.getFuncionarioService()
                                            .getFuncionarioBancodeHorasService()
                                            .getFuncionarioBancoDeHoras()
                                            .getIdFuncionarioBancoHoras())))
                    .sorted(Comparator.comparing(FuncionarioBancoHorasFechamento::getDataFechamento).reversed())
                    .findFirst()
                    .orElse(new FuncionarioBancoHorasFechamento());
            }
        }
        return new FuncionarioBancoHorasFechamento();
    }
  
}
