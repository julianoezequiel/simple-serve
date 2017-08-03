package com.topdata.toppontoweb.services.configuracoes;

//<editor-fold defaultstate="collapsed" desc="IMPORTS">
import java.util.List;

import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.topdata.toppontoweb.dao.DaoException;
import com.topdata.toppontoweb.dao.configuracoes.percentuais.SequenciaPercentuaisDao;
import com.topdata.toppontoweb.entity.configuracoes.PercentuaisAcrescimo;
import com.topdata.toppontoweb.entity.configuracoes.SequenciaPercentuais;
import com.topdata.toppontoweb.services.ServiceException;
import com.topdata.toppontoweb.services.TopPontoService;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.MSG;
import java.util.logging.Level;
import java.util.logging.Logger;
//</editor-fold>

/**
 * @version 1.0.0 data 18/01/2017
 * @since 1.0.0 data 26/07/2016
 *
 * @author juliano.ezequiel
 */
@Service
public class SequenciaPercentuaisServices extends TopPontoService<SequenciaPercentuais, Object> {

    //<editor-fold defaultstate="collapsed" desc="CDI">
    @Autowired
    private PercentuaisAcrescimoServices percentuaisAcrescimoServices;
    @Autowired
    private SequenciaPercentuaisDao sequenciaPercentuaisDao;
    //</editor-fold>

    /**
     * Busca a Lista de sequancias de percentuais pelo id do percentual
     *
     * @param id
     * @return Lista de Sequencias
     * @throws ServiceException
     */
    public List<SequenciaPercentuais> buscaPorPercetualAcrescimoId(Integer id) throws ServiceException {
        try {
            PercentuaisAcrescimo p = (PercentuaisAcrescimo) this.getDao().find(PercentuaisAcrescimo.class, id);
            if (p == null) {
                throw new ServiceException(this.getTopPontoResponse().alertaNaoCad(new PercentuaisAcrescimo().toString()));
            }
            return p.getSequenciaPercentuaisList();
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, SequenciaPercentuais.class), ex);
        }
    }

    public List<SequenciaPercentuais> buscaPorPercentuaisAcrescimo(Integer idPercentuaisAcrescimo) throws ServiceException {
        try {
            return sequenciaPercentuaisDao.findByPercentuaisAcrescimo(idPercentuaisAcrescimo);
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erro(MSG.CADASTRO.ERRO_BUSCAR, SequenciaPercentuais.class), ex);
        }
    }

    /**
     * Salva a lista de sequências
     *
     * @since 1.0.5 data 02/08/2016
     *
     * @param listaSequencias
     * @return Response
     * @throws ServiceException
     */
    public Response salvar(List<SequenciaPercentuais> listaSequencias) throws ServiceException {

        PercentuaisAcrescimo percentuaisAcrescimo = new PercentuaisAcrescimo();

        try {
            //se for enviada uma lista vazia, retorna uma exceção
            if (listaSequencias.isEmpty()) {
                throw new ServiceException(this.getTopPontoResponse().alertaValidacao(MSG.SEQUENCIA_PERCENTUAIS_ACRESCIMO.ALERTA_SEQUENCIA_OBRIGATORIO.getResource()));
            }

            //busca na base a entidade correta
            percentuaisAcrescimo = percentuaisAcrescimoServices.buscar(PercentuaisAcrescimo.class, listaSequencias.iterator().next().getSequenciaPercentuaisPK().getIdPercentuaisAcrescimo());

            //valida os dois campos de cada um dos registros da lista, e adiciona a referencia do percentual
            for (SequenciaPercentuais p : listaSequencias) {
                if (p.getAcrescimo() == null || p.getAcrescimo() == 0) {
                    throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("acréscimo"));
                }
                if (p.getHoras() == null) {
                    throw new ServiceException(this.getTopPontoResponse().campoObrigatorio("horas"));
                }

                p.setPercentuaisAcrescimo(percentuaisAcrescimo);
            }

            percentuaisAcrescimo.getSequenciaPercentuaisList().clear();
            percentuaisAcrescimo.getSequenciaPercentuaisList().addAll(listaSequencias);

            this.getDao().save(percentuaisAcrescimo);
            listaSequencias.stream().forEach(s -> {
                try {
                    this.getAuditoriaServices().auditar(CONSTANTES.Enum_FUNCIONALIDADE.CAD_CONFIGURACOES_PERCENTUAIS_ACRESCIMO, CONSTANTES.Enum_AUDITORIA.DEFAULT, CONSTANTES.Enum_OPERACAO.INCLUIR, s);
                } catch (ServiceException ex) {
                    Logger.getLogger(SequenciaPercentuaisServices.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } catch (DaoException ex) {
            throw new ServiceException(this.getTopPontoResponse().erroSalvar(new PercentuaisAcrescimo().toString()));
        }

        return this.getTopPontoResponse().sucessoSalvar(percentuaisAcrescimo.toString(), percentuaisAcrescimo.toString());

    }

}
