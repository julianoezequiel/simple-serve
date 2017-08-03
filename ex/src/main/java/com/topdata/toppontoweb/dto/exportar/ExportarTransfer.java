package com.topdata.toppontoweb.dto.exportar;

import com.topdata.toppontoweb.entity.empresa.Empresa;
import java.util.Date;

/**
 *
 * @author tharle.camargo
 */
public class ExportarTransfer {
    
    private Empresa empresa;
    private Date periodoInicio;
    private Date periodoFim;

    /**
     * @return the empresa
     */
    public Empresa getEmpresa() {
        return empresa;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    /**
     * @return the periodoInicio
     */
    public Date getPeriodoInicio() {
        return periodoInicio;
    }

    /**
     * @param periodoInicio the periodoInicio to set
     */
    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    /**
     * @return the periodoFim
     */
    public Date getPeriodoFim() {
        return periodoFim;
    }

    /**
     * @param periodoFim the periodoFim to set
     */
    public void setPeriodoFim(Date periodoFim) {
        this.periodoFim = periodoFim;
    }
    
    
    
}
