package com.topdata.toppontoweb.dto.jornada;

import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.tipo.TipoDia;
import java.util.List;

/**
 *
 * @author tharle.camargo
 */
public class HorarioTransfer {
    private Integer idHorario;
    private String descricao;
    private TipoDia tipodia;
    private boolean trataComoDiaNormal;
    private List<HorarioMarcacao> horarioMarcacaoList;
    private boolean preAssinalada;

    public HorarioTransfer(Horario h) {
        this.idHorario = h.getIdHorario();
        this.descricao = h.getDescricao();
        this.tipodia = h.getTipodia();
        this.trataComoDiaNormal = h.getTrataComoDiaNormal() != null? h.getTrataComoDiaNormal() : false ;
        this.horarioMarcacaoList = h.getHorarioMarcacaoList();
        this.preAssinalada = h.getPreAssinalada() != null? h.getPreAssinalada() : false;
    }

    /**
     * @return the idHorario
     */
    public Integer getIdHorario() {
        return idHorario;
    }

    /**
     * @param idHorario the idHorario to set
     */
    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the tipodia
     */
    public TipoDia getTipodia() {
        return tipodia;
    }

    /**
     * @param tipodia the tipodia to set
     */
    public void setTipodia(TipoDia tipodia) {
        this.tipodia = tipodia;
    }

    /**
     * @return the trataComoDiaNormal
     */
    public boolean isTrataComoDiaNormal() {
        return trataComoDiaNormal;
    }

    /**
     * @param trataComoDiaNormal the trataComoDiaNormal to set
     */
    public void setTrataComoDiaNormal(boolean trataComoDiaNormal) {
        this.trataComoDiaNormal = trataComoDiaNormal;
    }

    /**
     * @return the horarioMarcacaoList
     */
    public List<HorarioMarcacao> getHorarioMarcacaoList() {
        return horarioMarcacaoList;
    }

    /**
     * @param horarioMarcacaoList the horarioMarcacaoList to set
     */
    public void setHorarioMarcacaoList(List<HorarioMarcacao> horarioMarcacaoList) {
        this.horarioMarcacaoList = horarioMarcacaoList;
    }

    /**
     * @return the preAssinalada
     */
    public boolean isPreAssinalada() {
        return preAssinalada;
    }

    /**
     * @param preAssinalada the preAssinalada to set
     */
    public void setPreAssinalada(boolean preAssinalada) {
        this.preAssinalada = preAssinalada;
    }
    
    
    public String getSubItem(){
        return this.idHorario.toString();
    }
    
    public String getSubItemLabel(){
        return "C.H.:";
    }
    
}
