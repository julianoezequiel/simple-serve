package com.topdata.toppontoweb.services.ferramentas.layout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dto.ferramentas.layout.EventoMotivo;
import com.topdata.toppontoweb.entity.ferramentas.Evento;
import com.topdata.toppontoweb.entity.ferramentas.TipoFormatoEvento;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.services.funcionario.motivo.MotivoService;
import java.util.Comparator;
import java.util.stream.Collectors;

/**
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class EventosServices extends TopPontoService<Evento, Object> {

    @Autowired
    private MotivoService motivoService;

    public List<EventoMotivo> buscarTodos() throws ServiceException {

        try {
            List<Motivo> motivosList = this.motivoService.buscarMotivoEventos();
            List<Evento> eventoList = this.getDao().findAll(Evento.class);

            List<EventoMotivo> eventoMotivoList = new ArrayList<>();

            motivosList.stream().sorted(Comparator.comparing(Motivo::getDescricao)).forEach(m -> {
                eventoMotivoList.add(new EventoMotivo(m));
            });

            eventoList.stream().forEach(e -> {
                eventoMotivoList.add(new EventoMotivo(e));
            });

            return eventoMotivoList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public List<TipoFormatoEvento> buscarTodosTipo() throws ServiceException {
        try {
            return this.getDao().findAll(TipoFormatoEvento.class);
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

}
