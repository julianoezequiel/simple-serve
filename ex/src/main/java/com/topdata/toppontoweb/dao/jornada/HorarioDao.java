package com.topdata.toppontoweb.dao.jornada;

import java.util.List;

import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.topdata.toppontoweb.dao.Dao;
import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.PaginacaoTransfer;
import com.topdata.toppontoweb.entity.jornada.Horario;

/**
 * @version 1.0.6 data 15/09/2016
 * @since 1.0.6 data 15/09/2016
 *
 * @author juliano.ezequiel
 */
@Repository
public class HorarioDao extends Dao<Horario, Object> {

    public HorarioDao() {
        super(Horario.class);
    }

    @Transactional(readOnly = true)
    public List<Object[]> listarMarcacoesJornada(Integer idJornada) {
        Query query = this.getEntityManager()
                .createNativeQuery("SELECT jh.idSequencia, hm.IdSequencia, hm.HorarioEntrada, hm.HorarioSaida,j.JornadaVariavel \n"
                        + "FROM Jornada j inner join JornadaHorario jh on j.IdJornada = jh.IdJornada \n"
                        + "inner join Horario h on  h.IdHorario = jh.IdHorario \n"
                        + "inner join HorarioMarcacao hm on hm.IdHorario=h.IdHorario\n"
                        + "WHERE j.IdJornada = :idJornada\n"
                        + "order by jh.idSequencia");
        query.setParameter("idJornada", idJornada);
        return query.getResultList();
    }

    public List<Horario> buscarPorPaginacao(PaginacaoTransfer pt) throws DaoException {
        try {
            String strQuery = "SELECT h "
                            + "FROM Horario h "
                            + "WHERE (h.descricao LIKE :busca OR h.idHorario LIKE :busca) "
                            + "ORDER BY h.descricao ";

            Integer inicioRegistros = (pt.getPagina() - 1) * pt.getQntPorPagina();

            Query query;
            query = this.getEntityManager().createQuery(strQuery);
            query.setParameter("busca", "%" + pt.getBusca() + "%");

            query.setFirstResult(inicioRegistros);
            query.setMaxResults(pt.getQntPorPagina());
            return query.getResultList();
        } catch (Exception e) {
            throw new DaoException(e);
        }
    }

}
