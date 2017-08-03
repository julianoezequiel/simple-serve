package com.topdata.toppontoweb.dto;

import com.topdata.toppontoweb.utils.constantes.CONSTANTES;
import com.topdata.toppontoweb.utils.constantes.CONSTANTES.Enum_OPERACAO;

/**
 * Classe utilizada para a criação do TreeView de permissões
 *
 * @version 1.0.4 data 07/06/2016
 * @since 1.0.4 data 07/06/2016
 *
 * @author juliano.ezequiel
 */
public class TreeViewData {

    private final String icon;
    private final String text;
    private final String parent;
    private final String id;
    private State state;
    private final Data data;

    public TreeViewData(String id, String icon, String text, String parent) {
        this.icon = icon;
        this.text = text;
        this.parent = parent;
        this.id = parent + id;
        this.data = null;
        this.state = new State();
    }

    public TreeViewData(CONSTANTES.Enum_MODULOS modulos) {
        this.icon = modulos.getIcone();
        this.text = modulos.getDescricao();
        this.parent = "Todas0";
        this.id = modulos.getCodModulo();
        this.data = new Data("Modulo", Integer.toString(modulos.ordinal()), parent, modulos.ordinal(), Enum_OPERACAO.DEFAULT.ordinal(), "MODULO");
        this.state = new State();
    }

    public TreeViewData(CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) {
        this.icon = funcionalidade.getIcone();
        this.text = funcionalidade.getDescricao();
        this.parent = funcionalidade.getPai() == null ? "#" : funcionalidade.getPai();
        this.id = funcionalidade.getDescricao() + Integer.toString(funcionalidade.ordinal());
        this.data = new Data("Funcionalidade", Integer.toString(funcionalidade.ordinal()), parent, funcionalidade.ordinal(), Enum_OPERACAO.DEFAULT.ordinal(), funcionalidade.getFuncionalidade());
        this.state = new State();
    }

    public TreeViewData(CONSTANTES.Enum_OPERACAO operacao, CONSTANTES.Enum_FUNCIONALIDADE funcionalidade) {
        this.icon = operacao.getIcone();
        this.text = operacao.getDescricao();
        this.parent = funcionalidade.getDescricao() + Integer.toString(funcionalidade.ordinal());
        this.id = Integer.toString(funcionalidade.ordinal()) + "-" + Integer.toString(operacao.ordinal());
        this.data = new Data("Operacao", Integer.toString(operacao.ordinal()), parent, funcionalidade.ordinal(), operacao.ordinal(), "OPERACAO");
        this.state = new State();
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getIcon() {
        return icon;
    }

    public String getParent() {
        return parent;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public Data getData() {
        return data;
    }

    public class State {

        private Boolean selected;
        private Boolean opened;
        private Boolean disabled;
        private Boolean isAtivoPlano;

        public State() {
            this.opened = false;
            this.disabled = true;
            this.selected = false;
            this.isAtivoPlano = false;
        }

        public State(Boolean selected, Boolean opened, Boolean disabled) {
            this.selected = selected;
            this.opened = opened;
            this.disabled = disabled;
        }

        public Boolean getSelected() {
            return selected;
        }

        public Boolean getOpened() {
            return opened;
        }

        public Boolean getDisabled() {
            return disabled;
        }

        public void setDisabled(Boolean disabled) {
            this.disabled = disabled;
        }

        public void setOpened(Boolean opened) {
            this.opened = opened;
        }

        public void setSelected(Boolean selected) {
            this.selected = selected;
        }

        public Boolean getIsAtivoPlano() {
            return isAtivoPlano;
        }

        public void setIsAtivoPlano(Boolean isAtivoPlano) {
            this.isAtivoPlano = isAtivoPlano;
        }

    }

    private class Data {

        private final String tipo;
        private final String id;
        private final String pai;
        private final Integer operacao;
        private final Integer idFunc;
        private final String funcionalidade;

        public Data(String tipo, String id, String pai, Integer idFunc,Integer operacao, String funcionalidade) {
            this.tipo = tipo;
            this.id = id;
            this.pai = pai;
            this.idFunc = idFunc;
            this.operacao = operacao;
            this.funcionalidade = funcionalidade;
        }

        public String getId() {
            return id;
        }

        public String getTipo() {
            return tipo;
        }

        public String getPai() {
            return pai;
        }

        public Integer getIdFunc() {
            return idFunc;
        }

        public Integer getOperacao() {
            return operacao;
        }

        public String getFuncionalidade() {
            return funcionalidade;
        }
        
        
        
       
        
        

    }
}
