package com.topdata.toppontoweb.dao.jornada;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.HorarioMarcacao;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import java.util.List;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author tharle.camargo
 */
@Repository
public class HorarioMarcacaoDao extends Dao<HorarioMarcacao, Integer> {
    
    @Transactional(readOnly = true)
    public List<HorarioMarcacao> buscarPorHorario(Horario horario) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT hm FROM HorarioMarcacao hm WHERE hm.horario = :horario");
            query.setParameter("horario", horario);

            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    } 
    
    @Transactional
    public Integer excluirTodosPorHorario(Horario horario) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("DELETE FROM HorarioMarcacao hm WHERE hm.horario = :horario");
            tq.setParameter("horario", horario);

            return tq.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
