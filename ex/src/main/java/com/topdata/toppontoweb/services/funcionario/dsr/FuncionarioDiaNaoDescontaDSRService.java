package com.topdata.toppontoweb.services.funcionario.dsr;

import com.topdata.toppontoweb.dao.funcionario.FuncionarioDiaNaoDescontaDSRDao;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.funcionario.FuncionarioDiaNaoDescontaDSR;
import com.topdata.toppontoweb.services.TopPontoService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author juliano.ezequiel
 */
@Service
public class FuncionarioDiaNaoDescontaDSRService extends TopPontoService<FuncionarioDiaNaoDescontaDSR, Integer> {

    @Autowired
    private FuncionarioDiaNaoDescontaDSRDao funcionarioDiaNaoDescontaDSRDao;

    public List<FuncionarioDiaNaoDescontaDSR> buscarPorFuncionario(Funcionario funcionario) {
        return this.funcionarioDiaNaoDescontaDSRDao.buscarPorFuncionario(funcionario);
    }
    
    public List<FuncionarioDiaNaoDescontaDSR> buscarPorFuncionarioData(Funcionario funcionario, Date dia) {
        return this.funcionarioDiaNaoDescontaDSRDao.buscarPorFuncionarioData(funcionario, dia);
    }

}
