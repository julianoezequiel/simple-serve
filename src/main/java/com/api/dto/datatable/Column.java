package com.api.dto.datatable;

public class Column {

    /**
     * Column's data source
     *
     * @see http://datatables.net/reference/option/columns.data
     */
    private String data;

    /**
     * Column's name
     *
     * @see http://datatables.net/reference/option/columns.name
     */
    private String name;

    /**
     * Flag to indicate if this column is searchable (true) or not (false).
     *
     * @see http://datatables.net/reference/option/columns.searchable
     */
    private Boolean searchable;

    /**
     * Flag to indicate if this column is orderable (true) or not (false).
     *
     * @see http://datatables.net/reference/option/columns.orderable
     */
    private Boolean orderable;

    /**
     * Search value to apply to this specific column.
     */
    private Search search;

    public Column() {
        this.name = "";
        this.data = "";
        this.searchable = false;
        this.orderable = false;
        this.search = new Search();
    }

    public Column(String name, String data, boolean searchable, boolean orderable, Search search) {
        this.name = name;
        this.data = data;
        this.searchable = searchable;
        this.orderable = orderable;
        this.search = search;
    }

    /**
     * Set the search value to apply to this column
     *
     * @param searchValue if any, the search value to apply
     */
    public void setSearchValue(String searchValue) {
        this.getSearch().setValue(searchValue);
    }

    /**
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the searchable
     */
    public Boolean getSearchable() {
        return searchable;
    }

    /**
     * @param searchable the searchable to set
     */
    public void setSearchable(Boolean searchable) {
        this.searchable = searchable;
    }

    /**
     * @return the orderable
     */
    public Boolean getOrderable() {
        return orderable;
    }

    /**
     * @param orderable the orderable to set
     */
    public void setOrderable(Boolean orderable) {
        this.orderable = orderable;
    }

    /**
     * @return the search
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
}
