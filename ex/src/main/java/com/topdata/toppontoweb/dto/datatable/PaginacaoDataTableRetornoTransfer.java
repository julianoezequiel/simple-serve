package com.topdata.toppontoweb.dto.datatable;

import java.util.List;

/**
 *
 * @author tharle.camargo
 */
public class PaginacaoDataTableRetornoTransfer {
    
    private List data;
    private Integer draw;
    private Long recordsFiltered;
    private Long recordsTotal;

    public PaginacaoDataTableRetornoTransfer(List data, Integer draw, Long recordsFiltered, Long recordsTotal) {
        this.data = data;
        this.draw = draw;
        this.recordsFiltered = recordsFiltered;
        this.recordsTotal = recordsTotal;
    }

    /**
     * @return the data
     */
    public List getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List data) {
        this.data = data;
    }

    /**
     * @return the draw
     */
    public Integer getDraw() {
        return draw;
    }

    /**
     * @param draw the draw to set
     */
    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    /**
     * @return the recordsFiltered
     */
    public Long getRecordsFiltered() {
        return recordsFiltered;
    }

    /**
     * @param recordsFiltered the recordsFiltered to set
     */
    public void setRecordsFiltered(Long recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    /**
     * @return the recordsTotal
     */
    public Long getRecordsTotal() {
        return recordsTotal;
    }

    /**
     * @param recordsTotal the recordsTotal to set
     */
    public void setRecordsTotal(Long recordsTotal) {
        this.recordsTotal = recordsTotal;
    }
    
    
}
