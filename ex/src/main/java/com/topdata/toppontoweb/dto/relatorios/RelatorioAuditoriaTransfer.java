package com.topdata.toppontoweb.dto.relatorios;

import com.topdata.toppontoweb.dto.relatorios.cadastros.RelatorioCadastroTransfer;
import com.topdata.toppontoweb.entity.autenticacao.Funcionalidades;
import com.topdata.toppontoweb.entity.autenticacao.Modulos;
import com.topdata.toppontoweb.entity.autenticacao.Operador;
import com.topdata.toppontoweb.entity.tipo.Operacao;
import com.topdata.toppontoweb.entity.tipo.TipoAuditoria;
import com.topdata.toppontoweb.services.relatorios.RelatorioHandler;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

/**
 *
 * @author juliano.ezequiel
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatorioAuditoriaTransfer extends RelatorioCadastroTransfer {

    private Operador operador;
    private Funcionalidades funcionalidade;
    private Operacao operacao;
    private Modulos modulo;
    private Boolean operadorAtivo;
    private TipoAuditoria tipoAuditoria;
    
    
    public RelatorioAuditoriaTransfer() {
        super();
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.AUDITORIA;
    }

    public RelatorioAuditoriaTransfer(RelatorioAuditoriaTransfer relatorioAuditoriaTransfer) {
        super(relatorioAuditoriaTransfer);
        this.tipo_relatorio = RelatorioHandler.TIPO_RELATORIO.AUDITORIA;
        this.operacao = relatorioAuditoriaTransfer.operacao;
        this.modulo = relatorioAuditoriaTransfer.modulo;
        this.operador = relatorioAuditoriaTransfer.operador;
        this.funcionalidade = relatorioAuditoriaTransfer.funcionalidade;
        this.operadorAtivo = relatorioAuditoriaTransfer.operadorAtivo;
        this.tipoAuditoria = relatorioAuditoriaTransfer.tipoAuditoria;
    }

    public Funcionalidades getFuncionalidade() {
        return funcionalidade;
    }

    public void setFuncionalidade(Funcionalidades funcionalidade) {
        this.funcionalidade = funcionalidade;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public Modulos getModulo() {
        return modulo;
    }

    public void setModulo(Modulos modulo) {
        this.modulo = modulo;
    }

    public Operador getOperador() {
        return operador;
    }

    public void setOperador(Operador operador) {
        this.operador = operador;
    }

    public Boolean getOperadorAtivo() {
        return operadorAtivo;
    }

    public void setOperadorAtivo(Boolean operadorAtivo) {
        this.operadorAtivo = operadorAtivo;
    }

    public TipoAuditoria getTipoAuditoria() {
        return tipoAuditoria;
    }

    public void setTipoAuditoria(TipoAuditoria tipoAuditoria) {
        this.tipoAuditoria = tipoAuditoria;
    }

}
