package com.topdata.toppontoweb.services.gerafrequencia.integracao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.gerafrequencia.entity.bancodehoras.BancodeHorasApi;
import com.topdata.toppontoweb.services.gerafrequencia.entity.calculo.SaidaDia;
import com.topdata.toppontoweb.services.gerafrequencia.entity.regras.Intervalos;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.Saldo;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoAusencias;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoExtras;
import com.topdata.toppontoweb.services.gerafrequencia.entity.saldo.SaldoTrabalhadas;
import com.topdata.toppontoweb.services.gerafrequencia.entity.tabela.TabelaSequenciaPercentuais;

/**
 *
 * @author juliano.ezequiel
 */
public class SaidaPadrao {

    private final Funcionario funcionario;
    private final List<SaidaDia> saidaDiaList;
    private Intervalos intervaloTotal;
    private Saldo saldoNormaisTotal;
    private SaldoExtras saldoExtrasTotal;
    private SaldoAusencias saldoAusenciasTotal;
    private SaldoTrabalhadas horasTrabalhadasTotal;
    private BancodeHorasApi bancodeHorasTotal;
    private Integer quantidadeFaltas;
    private List<TabelaSequenciaPercentuais> tabelaExtrasTotalList;
    private Long quantidadeJornadasPorPeriodo;

    public SaidaPadrao(Funcionario funcionario, List<SaidaDia> saidaDiaList) {
        this.funcionario = funcionario;
        this.saidaDiaList = saidaDiaList;
        this.quantidadeJornadasPorPeriodo = 0l;
//        this.intervaloTotal = Intervalos.ZERO();
        this.calcularTotais();
    }
    
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public List<SaidaDia> getSaidaDiaList() {
        return saidaDiaList;
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

    public List<TabelaSequenciaPercentuais> getTabelaExtrasTotalList() {
        return tabelaExtrasTotalList;
    }

    public SaldoTrabalhadas getHorasTrabalhadasTotal() {
        return horasTrabalhadasTotal;
    }
    
    /**
     * @return the quantidadeJornadasPorPeriodo
     */
    public Long getQuantidadeJornadasPorPeriodo() {
        return quantidadeJornadasPorPeriodo;
    }

    /**
     * @param quantidadeJornadasPorPeriodo the quantidadeJornadasPorPeriodo to set
     */
    public void setQuantidadeJornadasPorPeriodo(Long quantidadeJornadasPorPeriodo) {
        this.quantidadeJornadasPorPeriodo = quantidadeJornadasPorPeriodo;
    }
    
    public SaidaDia getUltimoDiaComBancoHoras(){
        SaidaDia result = new SaidaDia();
        for (SaidaDia saidaDia : saidaDiaList) {
            if(saidaDia.getBancodeHoras().isPossuiBH()){
                result = saidaDia;
            }
        }
        return result;
    }
    

    private void calcularTotais() {
        
        this.tabelaExtrasTotalList = new ArrayList<>();
        this.intervaloTotal = Intervalos.ZERO();
        this.saldoNormaisTotal = new Saldo();
        this.saldoExtrasTotal = new SaldoExtras();
        this.saldoAusenciasTotal = new SaldoAusencias();
        this.horasTrabalhadasTotal = new SaldoTrabalhadas();
        this.bancodeHorasTotal = new BancodeHorasApi();
        this.quantidadeFaltas = 0;
        saidaDiaList.stream().forEach((SaidaDia saidaDia) -> {
            //Totais dos intervalos
            this.intervaloTotal.plus(saidaDia.getIntervalos());
            this.saldoNormaisTotal.plus(saidaDia.getSaldoNormais());
            this.saldoExtrasTotal.plus(saidaDia.getSaldoExtras());
            this.saldoAusenciasTotal.plus(saidaDia.getSaldoAusencias());
            this.horasTrabalhadasTotal.plus(saidaDia.getHorasTrabalhadas());
            this.getBancodeHorasTotal().plus(saidaDia.getBancodeHoras());
            
            this.quantidadeFaltas += saidaDia.getRegras().isFalta()? 1 : 0;
            
            //TODO: Isso vai pesar
            saidaDia.getTabelaExtrasList().stream().forEach((tabela) -> {
                boolean encontrouTabela = false;
                for (TabelaSequenciaPercentuais tabelaTotal : this.tabelaExtrasTotalList) {
                    if(Objects.equals(tabelaTotal.getTipoDia().getIdTipodia(), tabela.getTipoDia().getIdTipodia())
                            && Objects.equals(tabelaTotal.getIdSequencia(), tabela.getIdSequencia())){
                        encontrouTabela = true;
                        tabelaTotal.plus(tabela);
                    }
                }
                
                if(!encontrouTabela){
                   this.tabelaExtrasTotalList.add(tabela);
                }
            });
            
        });
        
        if(!saidaDiaList.isEmpty()){
            //Tem que tirar a media do "indice de absenteismo"
            Double indiceAbsenteismo = this.intervaloTotal.getIndiceAbsenteismo() / saidaDiaList.size();
            this.intervaloTotal.setIndiceAbsenteismo(indiceAbsenteismo);
        }
    }


    
}
