package com.topdata.toppontoweb.dto;

import java.util.List;

import com.topdata.toppontoweb.entity.empresa.Departamento;
import com.topdata.toppontoweb.entity.empresa.Empresa;

/**
 * @version 1.0.4 data 31/05/2016
 * @since 1.0.4 data 31/05/2016
 *
 * @author juliano.ezequiel
 */
public class EmpresaDepartamentoTransfer {

    private final Empresa empresa;
    private final List<Departamento> departamentos;

    public EmpresaDepartamentoTransfer(Empresa empresa) {
        this.empresa = empresa;
        this.departamentos = empresa.getDepartamentoList();
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

}
