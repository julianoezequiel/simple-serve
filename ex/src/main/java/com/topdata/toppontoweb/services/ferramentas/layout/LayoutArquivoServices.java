package com.topdata.toppontoweb.services.ferramentas.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.ferramentas.layout.LayoutArquivoDao;
import com.topdata.toppontoweb.dto.ferramentas.layout.EventoMotivo;
import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutArquivoGrupoTransfer;
import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutArquivoTransfer;
import com.topdata.toppontoweb.dto.ferramentas.layout.LayoutGrupoEventosMotivosTransfer;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivo;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivoGrupo;
import com.topdata.toppontoweb.entity.ferramentas.LayoutGrupoEventosMotivos;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import static com.topdata.toppontoweb.services.TopPontoService.LOGGER;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;

/**
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@Service
public class LayoutArquivoServices extends TopPontoService<LayoutArquivo, Object> {

    @Autowired
    protected LayoutArquivoDao layoutArquivoDao;

    public List<LayoutArquivoTransfer> buscarTodos() throws ServiceException {
        try {
            LOGGER.debug("Consultando todos os layout arquivo ");
            List<LayoutArquivo> layoutArquivoList = this.layoutArquivoDao.findAll(LayoutArquivo.class);
            List<LayoutArquivoTransfer> layoutArquivoTransfersList = new ArrayList<>();
            layoutArquivoList.stream().forEach(new LayoutArquivoConverter(layoutArquivoTransfersList, Boolean.FALSE));
            return layoutArquivoTransfersList;
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    public Response salvar(LayoutArquivoTransfer layoutArquivoTransfer) throws ServiceException {
        try {
            LOGGER.debug("Salvando LayoutArquivo");
            LayoutArquivo layoutArquivo = layoutArquivoTransfer.toEntidade();
            CONSTANTES.Enum_OPERACAO operacao = layoutArquivo.getIdLayoutArquivo() != null ? CONSTANTES.Enum_OPERACAO.EDITAR : CONSTANTES.Enum_OPERACAO.INCLUIR;

            //salvo o layout
            LayoutArquivo layoutArquivoNovo = this.layoutArquivoDao.save(layoutArquivo);

            //remove os vinculos antigis caso já exista
            layoutArquivoNovo.getLayoutArquivoGrupoList().stream().forEach(new ExcluirLayoutArquivoGrupo(this));

            List<LayoutArquivoGrupo> layoutArquivoGrupoList = new ArrayList<>();

            if (!layoutArquivoTransfer.getLayoutArquivoGrupoTransferList().isEmpty()) {
                layoutArquivoTransfer.getLayoutArquivoGrupoTransferList()
                        .stream()
                        .forEach(lagt -> {
                            lagt.setLayoutArquivoTransfer(layoutArquivoTransfer);
                            layoutArquivoGrupoList.add(lagt.toEntidade());
                        });
            }

            //salva os grupos de layout
            layoutArquivoGrupoList.stream().forEach(new SalvarLayoutArquivoGrupo(this, layoutArquivoNovo));

            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.FER_LAYOUT_EVENTOS,
                    CONSTANTES.Enum_AUDITORIA.DEFAULT, operacao, layoutArquivo);

            return this.getTopPontoResponse().sucessoSalvar(layoutArquivo.toString());
        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }

    }

    private class SalvarLayoutArquivoGrupo implements Consumer<LayoutArquivoGrupo> {

        private LayoutArquivoServices layoutArquivoServices;
        private LayoutArquivo layoutArquivo;

        public SalvarLayoutArquivoGrupo(LayoutArquivoServices layoutArquivoServices, LayoutArquivo layoutArquivo) {
            this.layoutArquivoServices = layoutArquivoServices;
            this.layoutArquivo = layoutArquivo;
        }

        @Override
        public void accept(LayoutArquivoGrupo t) {
            try {
                LOGGER.debug("Salvando LayoutArquivoGrupo ");
                t.setLayoutArquivo(this.layoutArquivo);
                List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivoseList = t.getLayoutGrupoEventosMotivosList();
                t.setLayoutGrupoEventosMotivosList(null);
                t.setIdLayoutArquivoGrupo(null);
                //salva os grupo de arquivos
                LayoutArquivoGrupo layoutArquivoGrupoNovo = (LayoutArquivoGrupo) layoutArquivoServices.getDao().save(t);

                layoutGrupoEventosMotivoseList.stream().forEach(new SalvarLayoutGrupoEventosMotivo(this.layoutArquivoServices, layoutArquivoGrupoNovo));
            } catch (DaoException ex) {
                try {
                    this.layoutArquivoServices.layoutArquivoDao.delete(this.layoutArquivo);
                } catch (DaoException ex1) {
                    LOGGER.debug(LayoutArquivoServices.class.getName(), ex1);
                }
            }

        }

    }

    private class SalvarLayoutGrupoEventosMotivo implements Consumer<LayoutGrupoEventosMotivos> {

        private LayoutArquivoServices layoutArquivoServices;
        private LayoutArquivoGrupo layoutArquivoGrupo;

        public SalvarLayoutGrupoEventosMotivo(LayoutArquivoServices layoutArquivoServices, LayoutArquivoGrupo layoutArquivoGrupo) {
            this.layoutArquivoServices = layoutArquivoServices;
            this.layoutArquivoGrupo = layoutArquivoGrupo;
        }

        @Override
        public void accept(LayoutGrupoEventosMotivos t) {
            try {
                LOGGER.debug("Salvando LayoutGrupoEventosMotivos ");
                t.setLayoutArquivoGrupo(this.layoutArquivoGrupo);
                t.setIdLayoutGrupoEventosMotivos(null);
                layoutArquivoServices.getDao().save(t);
            } catch (DaoException ex) {
                try {
                    this.layoutArquivoServices.layoutArquivoDao.delete(this.layoutArquivoGrupo.getLayoutArquivo());
                } catch (DaoException ex1) {
                    LOGGER.debug(LayoutArquivoServices.class.getName(), ex1);
                }
            }
        }
    }

    @Override
    public Response excluir(Class<LayoutArquivo> c, Object id) throws ServiceException {
        try {
            LayoutArquivo layoutArquivo = this.buscar(c, id);
            LOGGER.debug("Excluindo {} - ID : {}", this.getClass().getSimpleName(), layoutArquivo.getIdLayoutArquivo());
            //se possuir vinculos excluir o vinculos primeiro
            if (!layoutArquivo.getLayoutArquivoGrupoList().isEmpty()) {
                layoutArquivo.getLayoutArquivoGrupoList().stream().forEach(new ExcluirLayoutArquivoGrupo(this));
            }

            this.layoutArquivoDao.delete(layoutArquivo);

            this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.FER_LAYOUT_EVENTOS, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.EXCLUIR, layoutArquivo);
            return this.getTopPontoResponse().sucessoExcluir(layoutArquivo.toString());
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroExcluir(new LayoutArquivo().toString()), ex);
        }
    }

    private class ExcluirLayoutArquivoGrupo implements Consumer<LayoutArquivoGrupo> {

        private LayoutArquivoServices layoutArquivoServices;

        public ExcluirLayoutArquivoGrupo(LayoutArquivoServices layoutArquivoServices) {
            this.layoutArquivoServices = layoutArquivoServices;
        }

        @Override
        public void accept(LayoutArquivoGrupo t) {
            try {
                LOGGER.debug("Excluindo {} - ID : {}", this.getClass().getSimpleName(), t.getIdLayoutArquivoGrupo());
                if (!t.getLayoutGrupoEventosMotivosList().isEmpty()) {
                    t.getLayoutGrupoEventosMotivosList().stream().forEach(new ExcluirLayoutGrupoEventosMotivos(layoutArquivoServices));
                }
                this.layoutArquivoServices.getDao().delete(t);
            } catch (DaoException ex) {
                LOGGER.debug(LayoutArquivoServices.class.getName(), ex);
            }
        }

    }

    private class ExcluirLayoutGrupoEventosMotivos implements Consumer<LayoutGrupoEventosMotivos> {

        private LayoutArquivoServices layoutArquivoServices;

        public ExcluirLayoutGrupoEventosMotivos(LayoutArquivoServices layoutArquivoServices) {
            this.layoutArquivoServices = layoutArquivoServices;
        }

        @Override
        public void accept(LayoutGrupoEventosMotivos t) {
            try {
                LOGGER.debug("Excluindo {} - ID : {}", this.getClass().getSimpleName(), t.getIdLayoutGrupoEventosMotivos());
                this.layoutArquivoServices.getDao().delete(t);
            } catch (DaoException ex) {
                LOGGER.debug(LayoutArquivoServices.class.getName(), ex);
            }
        }

    }

    @Override
    public LayoutArquivo buscar(Class<LayoutArquivo> entidade, Object id) throws ServiceException {
        try {
            LayoutArquivo layoutArquivo = this.layoutArquivoDao.find(LayoutArquivo.class, id);
            if (layoutArquivo == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad());
            }
            return layoutArquivo;
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroBuscar());
        }
    }

    public LayoutArquivoTransfer buscar(Integer id) throws ServiceException {
        try {
            LOGGER.debug("Consultando layout arquivo id : {}", id);
            List<LayoutArquivo> layoutArquivo = new ArrayList<>();
            layoutArquivo.add(this.layoutArquivoDao.find(LayoutArquivo.class, id));
            if (layoutArquivo.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad());
            }
            List<LayoutArquivoTransfer> layoutArquivoTransfersList = new ArrayList<>();
            layoutArquivo.stream().forEach(new LayoutArquivoConverter(layoutArquivoTransfersList, Boolean.TRUE));

            return layoutArquivoTransfersList.iterator().next();

        } catch (DaoException ex) {
            throw new ServiceException(ex);
        }
    }

    /**
     * Converte um LayoutArquivo em LayoutArquivoTransfer
     */
    private class LayoutArquivoConverter implements Consumer<LayoutArquivo> {

        private List<LayoutArquivoTransfer> layoutArquivoTransfersList;
        private Boolean completo = false;

        public LayoutArquivoConverter(List<LayoutArquivoTransfer> layoutArquivoTransfersList, Boolean completo) {
            this.layoutArquivoTransfersList = layoutArquivoTransfersList;
            this.completo = completo;
        }

        public LayoutArquivoConverter(List<LayoutArquivoTransfer> layoutArquivoTransfersList) {
            this.layoutArquivoTransfersList = layoutArquivoTransfersList;
        }

        @Override
        public void accept(LayoutArquivo layoutArquivo) {

            LOGGER.debug("Convertendo de LayoutArquivo para LayoutArquivoTransfer - id : {}", layoutArquivo.getIdLayoutArquivo());

            LayoutArquivoTransfer layoutArquivoTransfer;
            if (Objects.equals(this.completo, Boolean.TRUE)) {
                layoutArquivoTransfer = new LayoutArquivoTransfer(layoutArquivo);
            } else {
                layoutArquivoTransfer = new LayoutArquivoTransfer(layoutArquivo.getIdLayoutArquivo(), layoutArquivo.getDescricao());
            }
            //caso os layouts possuir grupos de eventos
            if (Objects.equals(this.completo, Boolean.TRUE) && !layoutArquivo.getLayoutArquivoGrupoList().isEmpty()) {

                List<LayoutArquivoGrupoTransfer> layoutArquivoGrupoTransferList = new ArrayList<>();

                layoutArquivo.getLayoutArquivoGrupoList()
                        .stream()
                        .forEach(new LayoutArquivoGrupoConverter(layoutArquivoGrupoTransferList));

                layoutArquivoTransfer.setLayoutArquivoGrupoTransferList(layoutArquivoGrupoTransferList);
            }

            this.layoutArquivoTransfersList.add(layoutArquivoTransfer);
        }
    }

    /**
     * Converte um LayoutArquivoGrupo em LayoutArquivoGrupoTransfer
     */
    private class LayoutArquivoGrupoConverter implements Consumer<LayoutArquivoGrupo> {

        private List<LayoutArquivoGrupoTransfer> layoutArquivoGrupoTransferList;

        public LayoutArquivoGrupoConverter(List<LayoutArquivoGrupoTransfer> layoutArquivoGrupoTransferList) {
            this.layoutArquivoGrupoTransferList = layoutArquivoGrupoTransferList;
        }

        @Override
        public void accept(LayoutArquivoGrupo layoutArquivoGrupo) {
            LOGGER.debug("Convertendo de LayoutArquivoGrupo para LayoutArquivoGrupoTransfer - id : {}", layoutArquivoGrupo.getIdLayoutArquivoGrupo());

            LayoutArquivoGrupoTransfer layoutArquivoGrupoTransfer = new LayoutArquivoGrupoTransfer(layoutArquivoGrupo);

            if (!layoutArquivoGrupo.getLayoutGrupoEventosMotivosList().isEmpty()) {
                List<LayoutGrupoEventosMotivosTransfer> grupoEventosMotivosTransferList = new ArrayList<>();
                layoutArquivoGrupo.getLayoutGrupoEventosMotivosList()
                        .stream()
                        .forEach(new LayoutGrupoEventosMotivosConverter(grupoEventosMotivosTransferList, layoutArquivoGrupoTransfer));
                layoutArquivoGrupoTransfer.setLayoutGrupoEventosMotivosTransferList(grupoEventosMotivosTransferList);
            }
            layoutArquivoGrupoTransfer.setTipoFormatoEvento(layoutArquivoGrupo.getTipoFormatoEvento());
            this.layoutArquivoGrupoTransferList.add(layoutArquivoGrupoTransfer);
        }

    }

    /**
     * Converte um LayoutGrupoEventosMotivos em
     * LayoutGrupoEventosMotivosTrasnfer
     */
    private class LayoutGrupoEventosMotivosConverter implements Consumer<LayoutGrupoEventosMotivos> {

        private List<LayoutGrupoEventosMotivosTransfer> grupoEventosMotivosTransferList;
        private LayoutArquivoGrupoTransfer layoutArquivoGrupoTransfer;

        public LayoutGrupoEventosMotivosConverter(List<LayoutGrupoEventosMotivosTransfer> grupoEventosMotivosTransferList, LayoutArquivoGrupoTransfer layoutArquivoGrupoTransfer) {
            this.grupoEventosMotivosTransferList = grupoEventosMotivosTransferList;
            this.layoutArquivoGrupoTransfer = layoutArquivoGrupoTransfer;
        }

        @Override
        public void accept(LayoutGrupoEventosMotivos layoutGrupoEventosMotivos) {

            LOGGER.debug("Convertendo de LayoutGrupoEventosMotivos para LayoutGrupoEventosMotivosTrasnfer - id : {}", layoutGrupoEventosMotivos.getLayoutArquivoGrupo());

            LayoutGrupoEventosMotivosTransfer layoutGrupoEventosMotivosTransfer = new LayoutGrupoEventosMotivosTransfer();

            layoutGrupoEventosMotivosTransfer.setId(layoutGrupoEventosMotivos.getIdLayoutGrupoEventosMotivos());
//            layoutGrupoEventosMotivosTransfer.setLayoutArquivoGrupoTransfer(this.layoutArquivoGrupoTransfer);
            EventoMotivo eventoMotivo;
            if (layoutGrupoEventosMotivos.getEvento() != null) {
                eventoMotivo = new EventoMotivo(layoutGrupoEventosMotivos.getEvento());
                layoutGrupoEventosMotivosTransfer.setMotivo(Boolean.FALSE);
            } else {
                eventoMotivo = new EventoMotivo(layoutGrupoEventosMotivos.getMotivo());
                layoutGrupoEventosMotivosTransfer.setMotivo(Boolean.TRUE);
            }
            layoutGrupoEventosMotivosTransfer.setDescricao(eventoMotivo.getDescricao());
            layoutGrupoEventosMotivosTransfer.setId(eventoMotivo.getId());

            this.grupoEventosMotivosTransferList.add(layoutGrupoEventosMotivosTransfer);
        }

    }

}
