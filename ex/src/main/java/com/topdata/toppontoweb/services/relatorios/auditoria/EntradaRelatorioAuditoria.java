package com.topdata.toppontoweb.services.relatorios.auditoria;

import com.topdata.toppontoweb.entity.Auditoria;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import java.util.Date;

/**
 *
 * @author juliano.ezequiel
 */
public class EntradaRelatorioAuditoria {

    private Modulos modulo;
    private Auditoria auditoria;
    private Date dataInicio;
    private Date dataFim;

    public EntradaRelatorioAuditoria() {
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Auditoria getAuditoria() {
        return auditoria;
    }

    public void setAuditoria(Auditoria auditoria) {
        this.auditoria = auditoria;
    }

}
