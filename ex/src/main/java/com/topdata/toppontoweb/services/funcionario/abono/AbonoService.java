package com.topdata.toppontoweb.services.funcionario.abono;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.funcionario.AbonoDao;
import com.topdata.toppontoweb.entity.funcionario.Abono;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.services.TopPontoService;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class AbonoService extends TopPontoService<Abono, Integer> {

    @Autowired
    private AbonoDao abonoDao;

    public List<Abono> buscarPorFuncionarioPeriodo(Funcionario funcionario, Date dataInicio, Date dataFim) {
        return this.abonoDao.buscarPorFuncionarioPeriodo(funcionario, dataInicio, dataFim);
    }
}
