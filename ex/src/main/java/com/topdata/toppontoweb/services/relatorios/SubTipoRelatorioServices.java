package com.topdata.toppontoweb.services.relatorios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dto.relatorios.SubTipoRelatorio;

/**
 *
 * @author tharle.camargo
 */
@Service
public class SubTipoRelatorioServices {

    public List<SubTipoRelatorio> buscarTodos(boolean todosFiltros) {

        List<SubTipoRelatorio> tipoRelatorioList = new ArrayList<>();
        tipoRelatorioList.add(new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.INDIVIDUAL));
        tipoRelatorioList.add(new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.DEPARTAMENTO));
        tipoRelatorioList.add(new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.EMPRESA));
        if (todosFiltros) {
            tipoRelatorioList.add(new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO));
            tipoRelatorioList.add(new SubTipoRelatorio(RelatorioHandler.SUB_TIPO_RELATORIO.SALDO_FUNCIONARIO_LIMITE));
        }

        return tipoRelatorioList;
    }
}
