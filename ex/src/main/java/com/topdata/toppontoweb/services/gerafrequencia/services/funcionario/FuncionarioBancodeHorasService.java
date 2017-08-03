package com.topdata.toppontoweb.services.gerafrequencia.services.funcionario;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.topdata.toppontoweb.entity.bancohoras.BancoHoras;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioBancoHoras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.Calculo;
import com.topdata.toppontoweb.services.gerafrequencia.utils.CONSTANTES;
import com.topdata.toppontoweb.services.gerafrequencia.utils.Utils;

/**
 * Consulta os dados do banco de horas vinculado ao funcionário
 *
 * @author enio.junior
 */
public class FuncionarioBancodeHorasService {

    private final Calculo calculo;

    public FuncionarioBancodeHorasService(Calculo calculo) {
        this.calculo = calculo;
    }

    public BancoHoras getBancoDeHoras() {
        if (getFuncionarioBancoDeHoras() != null) {
            return getFuncionarioBancoDeHoras().getBancoHoras();
        }
        return new BancoHoras();
    }

    /**
     * Retorna o banco de horas ativo do dia
     *
     * @return
     */
    public FuncionarioBancoHoras getFuncionarioBancoDeHoras() {
        return this.calculo.getEntradaAPI().getFuncionario().getFuncionarioBancoHorasList()
                .stream()
                .filter(f
                        -> (this.calculo.getDiaProcessado() != null && Utils.formatoDataSemHorario(f.getDataInicio()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0
                        && (f.getDataFim() == null
                    || (f.getDataFim() != null && Utils.formatoDataSemHorario(f.getDataFim()).compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) >= 0))))
                .sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio).reversed())
                .findFirst().orElse(null);
    }
    
    /**
     * Retorna o 1º banco de horas ativo independente da data fim
     * 
     * @return 
     */
    public FuncionarioBancoHoras getPrimeiroFuncionarioBancoDeHoras() {
        return this.calculo.getEntradaAPI().getFuncionario().getFuncionarioBancoHorasList()
                .stream()
                .filter(f -> (this.calculo.getDiaProcessado() != null 
                        && Utils.formatoDataSemHorario(f.getDataInicio())
                                .compareTo(Utils.formatoDataSemHorario(this.calculo.getDiaProcessado())) <= 0 ))
                .sorted(Comparator.comparing(FuncionarioBancoHoras::getDataInicio).reversed())
                .findFirst().orElse(null);
    }

    public Date getLimiteDiaBancoDeHoras(int tipoDia) {
        Date limite = getBancoDeHoras().getBancoHorasLimiteList().stream()
                .filter(f -> (f.getTipoDia().getIdTipodia() == tipoDia))
                .map(f -> f.getLimite()).findFirst().orElse(null);
        if (limite != null) {
            limite = Utils.getDataAjustaDiferenca3horas(limite);
        }
        return limite;
    }

    public List<SequenciaPercentuais> getPercentuaisTipoDiaBancoDeHoras(boolean feriado, boolean realizaCompensacao) {
        if (getBancoDeHoras().getPercentuaisAcrescimo() != null) {
            return getBancoDeHoras()
                    .getPercentuaisAcrescimo()
                    .getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getTipoDia().getIdTipodia().compareTo(this.calculo.getRegrasService().getTipoDia().getIdTipodia()) == 0))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<SequenciaPercentuais> getPercentuaisTipoDiaBancoDeHorasCompensadas() {
        if (getBancoDeHoras().getPercentuaisAcrescimo() != null) {
            return getBancoDeHoras()
                    .getPercentuaisAcrescimo().getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> f.getTipoDia().getDescricao().equals(CONSTANTES.TIPODIA_COMPENSADO))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<SequenciaPercentuais> getPercentuaisSomenteNoturnosBancoDeHoras() {
        if (getBancoDeHoras().getPercentuaisAcrescimo() != null) {
            return getBancoDeHoras()
                    .getPercentuaisAcrescimo().getSequenciaPercentuaisList()
                    .stream()
                    .filter(f -> (f.getTipoDia().getDescricao().compareTo(CONSTANTES.TIPODIA_HORAS_NOTURNAS) == 0))
                    .sorted(new Utils.ordenaSequencia())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
