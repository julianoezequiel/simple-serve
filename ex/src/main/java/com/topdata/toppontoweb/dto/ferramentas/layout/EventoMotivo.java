package com.topdata.toppontoweb.dto.ferramentas.layout;

import com.topdata.toppontoweb.entity.ferramentas.Evento;
import com.topdata.toppontoweb.entity.funcionario.Motivo;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 * Transfer utilizado para carregar os eventos e os motivos que também deverão
 * ser utulizados como eventos.
 *
 *
 * @version 1.0.0 data 10/07/2017
 * @since 1.0.0 data 10/07/2017
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventoMotivo {

    //define se é um motivo caso contrario é somente evento
    private Boolean motivo;
    //id, tanto do evento quanto do motivo
    private Integer id;
    //descrição do evento ou do motivo
    private String descricao;
    private String subdescricao = "";
    
    public EventoMotivo() {
    }

    //costrutor a partir de um objeto de evento
    public EventoMotivo(Evento evento) {
        this.descricao = evento.getDescricao();
        this.id = evento.getIdEvento();
        this.motivo = false;
    }

    //contsrutor a partir de um objeto de motivo
    public EventoMotivo(Motivo motivo) {
        this.descricao = motivo.getDescricao();
        this.subdescricao = "(" + motivo.getIdTipoMotivo().getDescricao() + ")";
        this.id = motivo.getIdMotivo();
        this.motivo = true;
    }

    public String getDescricao() {
        return descricao;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getMotivo() {
        return motivo;
    }

    public String getSubdescricao() {
        return subdescricao;
    }
    
    

}
