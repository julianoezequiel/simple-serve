package com.api.dto.datatable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaginacaoDataTableTransfer {
    //OBjetos do data Table
    private Integer length;
    private Integer start;
    private List<Order> order = new ArrayList<>();;
    private List<Column> columns = new ArrayList<>();
    private Search search = new Search();
    private Integer draw;//Quantidade de requisicoes feitas
    
    /**
     * @return Poisição inicial da pesquisa.
     */
    public Integer getStart() {
        return start;
    }

    /**
     * @param start the start to set
     */
    public void setStart(Integer start) {
        this.start = start;
    }
    
    

   /**
   * 
   * @return a {@link Map} of {@link Column} indexed by name
   */
  public Map<String, Column> getColumnsAsMap() {
    Map<String, Column> map = new HashMap<>();
        getColumns().stream().forEach((column) -> {
        map.put(column.getData(), column);
    });
    return map;
  }
  
  /**
   * Encontra a coluna pelo nome
   *
   * @param columnName the name of the column
   * @return the given Column, or <code>null</code> if not found
   */
  public Column getColumn(String columnName) {
    if (columnName == null) {
      return null;
    }
    for (Column column : columns) {
      if (columnName.equals(column.getData())) {
        return column;
      }
    }
    return null;
  }

  /**
   * Add a new column
   *
   * @param columnName the name of the column
   * @param searchable whether the column is searchable or not
   * @param orderable whether the column is orderable or not
   * @param searchValue if any, the search value to apply
   */
  public void addColumn(String columnName, boolean searchable, boolean orderable,
      String searchValue) {
    this.columns.add(new Column(columnName, "", searchable, orderable, new Search(searchValue, false)));
  }

  /**
   * Add an order on the given column
   *
   * @param columnName the name of the column
   * @param ascending whether the sorting is ascending or descending
   */
  public void addOrder(String columnName, boolean ascending) {
    if (columnName == null) {
      return;
    }
    for (int i = 0; i < columns.size(); i++) {
      if (!columnName.equals(columns.get(i).getData())) {
        continue;
      }
      order.add(new Order(i, ascending ? "asc" : "desc"));
    }
  }

    /**
     * @return Ordenar por quem?
     */
    public List<Order> getOrder() {
        return order;
    }

    /**
     * @param order the order to set
     */
    public void setOrder(List<Order> order) {
        this.order = order;
    }

    /**
     * @return Configuracoes das colunas
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    /**
     * @return Objeto que contem a busca
     */
    public Search getSearch() {
        return search;
    }

    /**
     * @param search the search to set
     */
    public void setSearch(Search search) {
        this.search = search;
    }

    /**
     * @return Quantidade de vezes que foi renderizada a data table. Esse atributo serve como controle da propria datatable.
     */
    public Integer getDraw() {
        return draw;
    }

    /**
     * @param draw the draw to set
     */
    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    /**
     * @return Tamanho da pagina.
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Integer length) {
        this.length = length;
    }
    /**
     * Retorna a primeira ordem
     * @return 
     */
    public Order getPrimeiraOrdem() {
        if(order != null && order.size() > 0){
            return order.get(0);
        }
        
        return null;
    }
    
    /**
     * Retorna a coluna pelo "id" dela
     * @param idColumn
     * @return 
     */
    public Column getColumnPorId(Integer idColumn) {
        if(columns != null){
            return columns.get(idColumn);
        }
        return null;
    }

}
