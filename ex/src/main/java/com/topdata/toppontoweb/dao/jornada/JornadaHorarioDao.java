package com.topdata.toppontoweb.dao.jornada;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.entity.jornada.Horario;
import com.topdata.toppontoweb.entity.jornada.Jornada;
import com.topdata.toppontoweb.entity.jornada.JornadaHorario;
import com.topdata.toppontoweb.entity.marcacoes.Importacao;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.6 data 28/11/2016
 * @since 1.0.6 data 28/11/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class JornadaHorarioDao extends Dao<JornadaHorario, Object> {

    public JornadaHorarioDao() {
        super(JornadaHorario.class);
    }

    //lista todos os horário existentes na jornada, pelo id da jornada
    @Transactional(readOnly = true)
    public List<JornadaHorario> buscarPorJornada(Jornada j) throws DaoException {
        try {
            Query query = this.getEntityManager().createQuery("SELECT jh FROM JornadaHorario jh WHERE jh.jornada.idJornada = :idJornada ORDER BY jh.idSequencia ASC");
            query.setParameter("idJornada", j.getIdJornada());

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "buscarPorJornada");
            throw new DaoException(ex);
        }
    }

    //lista as JornadasHorário existentes , pelo id do horario
    @Transactional(readOnly = true)
    public List<JornadaHorario> buscarPorHorario(Horario h) throws DaoException {
        try {

            Query query = this.getEntityManager().createQuery("SELECT DISTINCT j FROM JornadaHorario j WHERE j.horario.idHorario = :idHorario");
            query.setParameter("idHorario", h.getIdHorario());

            return query.getResultList();

        } catch (Exception ex) {
            Logger.getLogger(this.getClass().getSimpleName()).log(Level.SEVERE, "ERRO BUSCAR : {0}", "buscarPorHorario");
            throw new DaoException(ex);
        }
    }
    
    @Transactional
    public int removerTodasPorJornada(Jornada jornada) throws DaoException {
        try {
            Query tq = this.getEntityManager().createQuery("DELETE FROM JornadaHorario jh WHERE jh.jornada = :jornada");
            tq.setParameter("jornada", jornada);

            return tq.executeUpdate();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }
}
