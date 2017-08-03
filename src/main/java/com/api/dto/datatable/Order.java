package com.api.dto.datatable;

public class Order {
    /**
    * Column to which ordering should be applied. This is an index reference to the columns array of
    * information that is also submitted to the server.
    */
    private Integer column;

    /**
     * Ordering direction for this column. It will be asc or desc to indicate ascending ordering or
     * descending ordering, respectively.
     */
    private String dir;

    public Order() {
        this.column = 0;
        this.dir = "asc";
    }

    public Order(Integer column, String dir) {
        this.column = column;
        this.dir = dir;
    }
    
    

    /**
     * @return the column
     */
    public Integer getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(Integer column) {
        this.column = column;
    }

    /**
     * @return the dir
     */
    public String getDir() {
        return dir;
    }

    /**
     * @param dir the dir to set
     */
    public void setDir(String dir) {
        this.dir = dir;
    }
    
    
}
