package com.topdata.toppontoweb.dto.funcionario;

import com.topdata.toppontoweb.entity.funcionario.Funcionario;
import com.topdata.toppontoweb.utils.CustomDateSerializer;
import java.util.Date;
import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 *
 * @author tharle.camargo
 */
public class FuncionarioTransfer {

    private Integer idFuncionario;
    private String nome;
    private String matricula;
    private Integer idDepartamento;
    private Integer idEmpresa;
    private String empresaRazaoSocial;
    private String departamentoDescricao;
    private String cargoDescricao;
    private String pis;
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date dataAdmissao;
    private Boolean ativo;

    public FuncionarioTransfer() {
        this.idFuncionario = 0;
        this.nome = "";
        this.matricula = "";
        this.idDepartamento = 0;
        this.idEmpresa = 0;
    }

    public FuncionarioTransfer(Funcionario funcionario, boolean comDatas) {
        this.idFuncionario = funcionario.getIdFuncionario();
        this.nome = funcionario.getNome();
        this.matricula = funcionario.getMatricula();
        this.pis = funcionario.getPis();
        this.ativo = funcionario.getAtivo();
        this.dataAdmissao = comDatas? funcionario.getDataAdmissao() : null;
        this.idDepartamento = 0;
        this.idEmpresa = 0;
        
        if(funcionario.getDepartamento() != null ){
            this.idDepartamento = funcionario.getDepartamento().getIdDepartamento();
            this.departamentoDescricao = funcionario.getDepartamento().getDescricao();
            if(funcionario.getDepartamento().getEmpresa() != null){
                this.idEmpresa = funcionario.getDepartamento().getEmpresa().getIdEmpresa();
                this.empresaRazaoSocial = funcionario.getDepartamento().getEmpresa().getRazaoSocial();
            }
        }
        
        if(funcionario.getCargo() != null){
            this.cargoDescricao = funcionario.getCargo().getDescricao();
        }
    }

    public FuncionarioTransfer(Integer idFuncionario, String nome, String descricao, String matricula, Integer idEmpresa, Integer idDepartamento) {
        this.idFuncionario = idFuncionario;
        this.nome = nome;
        this.matricula = matricula;
        this.idDepartamento = idDepartamento;
        this.idEmpresa = idEmpresa;
    }

    /**
     * @return the idFuncionario
     */
    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    /**
     * @param idFuncionario the idFuncionario to set
     */
    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return nome;
    }

    public String getSubItemLabel() {
        return "Matrícula:";
    }

    public String getSubItem() {
        return matricula;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the idDepartamento
     */
    public Integer getIdDepartamento() {
        return idDepartamento;
    }

    /**
     * @param idDepartamento the idDepartamento to set
     */
    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    /**
     * @return the idEmpresa
     */
    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    /**
     * @param idEmpresa the idEmpresa to set
     */
    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }
    
    
    
    public Boolean getAtivo() {
        return ativo;
    }

    public String getEmpresaRazaoSocial() {
        return empresaRazaoSocial;
    }

    public void setEmpresaRazaoSocial(String empresaRazaoSocial) {
        this.empresaRazaoSocial = empresaRazaoSocial;
    }

    public String getDepartamentoDescricao() {
        return departamentoDescricao;
    }

    public void setDepartamentoDescricao(String departamentoDescricao) {
        this.departamentoDescricao = departamentoDescricao;
    }

    public String getCargoDescricao() {
        return cargoDescricao;
    }

    public void setCargoDescricao(String cargoDescricao) {
        this.cargoDescricao = cargoDescricao;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    
    
}
