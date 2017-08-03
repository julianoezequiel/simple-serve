package com.topdata.toppontoweb.dto.ferramentas.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.topdata.toppontoweb.dto.AbstractTransfer;
import com.topdata.toppontoweb.dto.ConversorEntidade;
import com.topdata.toppontoweb.entity.ferramentas.Evento;
import com.topdata.toppontoweb.entity.ferramentas.LayoutArquivoGrupo;
import com.topdata.toppontoweb.entity.ferramentas.LayoutGrupoEventosMotivos;
import com.topdata.toppontoweb.entity.ferramentas.TipoFormatoEvento;
import com.topdata.toppontoweb.entity.funcionario.Motivo;

/**
 * @version 1.0.0 data 20/07/2017
 * @since 1.0.0 data 20/07/2017
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(value = {"toEntidade"}, ignoreUnknown = true)
public class LayoutArquivoGrupoTransfer extends AbstractTransfer implements ConversorEntidade<LayoutArquivoGrupo> {

    private String id;
    private String idGrupoEvento;
    private boolean porLinha;
    private boolean noturnasComAdicional;
    private TipoFormatoEvento tipoFormatoEvento;
    private List<LayoutGrupoEventosMotivosTransfer> layoutGrupoEventosMotivosTransferList = new ArrayList<>();
    private LayoutArquivoTransfer layoutArquivoTransfer;

    public LayoutArquivoGrupoTransfer() {
    }

    public LayoutArquivoGrupoTransfer(LayoutArquivoGrupo lag) {
        this.id = lag.getIdLayoutArquivoGrupo().toString();
        this.idGrupoEvento = lag.getIdGrupoEvento();
        this.noturnasComAdicional = lag.getNoturnasComAdicional();
        this.layoutArquivoTransfer = new LayoutArquivoTransfer(lag.getLayoutArquivo());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdGrupoEvento() {
        return idGrupoEvento;
    }

    public void setIdGrupoEvento(String idGrupoEvento) {
        this.idGrupoEvento = idGrupoEvento;
    }

    public boolean isPorLinha() {
        return porLinha;
    }

    public void setPorLinha(boolean porLinha) {
        this.porLinha = porLinha;
    }

    public boolean isNoturnasComAdicional() {
        return noturnasComAdicional;
    }

    public void setNoturnasComAdicional(boolean noturnasComAdicional) {
        this.noturnasComAdicional = noturnasComAdicional;
    }

    public TipoFormatoEvento getTipoFormatoEvento() {
        return tipoFormatoEvento;
    }

    public void setTipoFormatoEvento(TipoFormatoEvento tipoFormatoEvento) {
        this.tipoFormatoEvento = tipoFormatoEvento;
    }

    public List<LayoutGrupoEventosMotivosTransfer> getLayoutGrupoEventosMotivosTransferList() {
        return layoutGrupoEventosMotivosTransferList;
    }

    public void setLayoutGrupoEventosMotivosTransferList(List<LayoutGrupoEventosMotivosTransfer> layoutGrupoEventosMotivosTransferList) {
        this.layoutGrupoEventosMotivosTransferList = layoutGrupoEventosMotivosTransferList;
    }

    public LayoutArquivoTransfer getLayoutArquivoTransfer() {
        return layoutArquivoTransfer;
    }

    public void setLayoutArquivoTransfer(LayoutArquivoTransfer layoutArquivoTransfer) {
        this.layoutArquivoTransfer = layoutArquivoTransfer;
    }

    @Override
    public LayoutArquivoGrupo toEntidade() {

        LayoutArquivoGrupo layoutArquivoGrupo = new LayoutArquivoGrupo();
        layoutArquivoGrupo.setIdGrupoEvento(idGrupoEvento);
        if (!id.contains("temp")) {
            layoutArquivoGrupo.setIdLayoutArquivoGrupo(Integer.parseInt(id));
        }
        layoutArquivoGrupo.setLayoutArquivo(layoutArquivoTransfer.toEntidade());
        layoutArquivoGrupo.setNoturnasComAdicional(noturnasComAdicional);
        layoutArquivoGrupo.setTipoFormatoEvento(tipoFormatoEvento);

        if (!this.layoutGrupoEventosMotivosTransferList.isEmpty()) {
            List<LayoutGrupoEventosMotivos> layoutGrupoEventosMotivoseList = new ArrayList<>();

            this.layoutGrupoEventosMotivosTransferList
                    .stream()
                    .forEach(l -> {
                        LayoutGrupoEventosMotivos layoutGrupoEventosMotivos = new LayoutGrupoEventosMotivos();
                        layoutGrupoEventosMotivos.setIdLayoutGrupoEventosMotivos(l.getId());
                        if (Objects.equals(l.getMotivo(), Boolean.TRUE)) {
                            layoutGrupoEventosMotivos.setMotivo(new Motivo(l.getId(), l.getDescricao()));
                        } else {
                            layoutGrupoEventosMotivos.setEvento(new Evento(l.getId(), l.getDescricao()));
                        }
                        layoutGrupoEventosMotivoseList.add(layoutGrupoEventosMotivos);
                    });

            layoutArquivoGrupo.setLayoutGrupoEventosMotivosList(layoutGrupoEventosMotivoseList);
        }
        return layoutArquivoGrupo;
    }

}
