package com.api.dto;

import com.api.converter.GenericDTO;
import com.api.entity.Empresa;
import com.api.entity.TipoDocumento;

public class EmpresaDTO extends GenericDTO {

    private Integer id;
    private String documento;
    private TipoDocumento tipoDocumento;
    private String razaoSocial;
    private String nomeFantasia;
    private String endereco;
    private String bairro;
    private String cep;
    private String cidade;
    private String uf;
    private String fone;
    private String fax;
    private String observacao;
    private Boolean ativo;

    public EmpresaDTO(Empresa e) {
        this.ativo = e.getAtivo();
        this.bairro = e.getBairro();
        this.cep = e.getCep();
        this.cidade = e.getCidade();
        this.documento = e.getDocumento();
        this.endereco = e.getEndereco();
        this.fax = e.getFax();
        this.fone = e.getFone();
        this.id = e.getId();
        this.nomeFantasia = e.getNomeFantasia();
        this.observacao = e.getObservacao();
        this.razaoSocial = e.getRazaoSocial();
        this.tipoDocumento = e.getTipoDocumento();
        this.uf = e.getUf();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

}
