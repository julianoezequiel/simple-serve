package com.topdata.toppontoweb.dto;

import java.util.List;

import com.topdata.toppontoweb.entity.autenticacao.FuncionalidadesGrupoOperacao;
import com.topdata.toppontoweb.entity.autenticacao.Grupo;
import com.topdata.toppontoweb.entity.empresa.Departamento;

/**
 * @version 1.0.4 data 01/06/2016
 * @since 1.0.4 data 01/06/2016
 *
 * @author juliano.ezequiel
 */
public class GrupoTransfer {

    private final List<FuncionalidadesGrupoOperacao> funcionalidadesGrupoOperacaoList;
    private final Grupo grupo;
    private final List<Departamento> departamentoList;
//    private final List<Empresa> empresaList;
//    private final List<Operador> operadorList;
//    private final List<GrupoPermissoes> grupoPermissoesList;

    public GrupoTransfer(Grupo grupo) {
        this.grupo = grupo;
        this.funcionalidadesGrupoOperacaoList = grupo.getFuncionalidadesGrupoOperacaoList();
        this.departamentoList = grupo.getDepartamentoList();
//        this.empresaList = grupo.getEmpresaList();
//        this.operadorList = grupo.getOperadorList();
//        this.grupoPermissoesList = grupo.getGrupoPermissoesList();
    }

    public List<Departamento> getDepartamentoList() {
        return departamentoList;
    }

//    public List<Empresa> getEmpresaList() {
//        return empresaList;
//    }
    public List<FuncionalidadesGrupoOperacao> getFuncionalidadesGrupoList() {
        return funcionalidadesGrupoOperacaoList;
    }

    public Grupo getGrupo() {
        return grupo;
    }

//    public List<GrupoPermissoes> getGrupoPermissoesList() {
//        return grupoPermissoesList;
//    }
//
//    public List<Operador> getOperadorList() {
//        return operadorList;
//    }
}
