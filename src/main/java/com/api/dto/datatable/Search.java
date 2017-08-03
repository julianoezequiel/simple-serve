package com.api.dto.datatable;

public class Search {
    /**
     * Global search value. To be applied to all columns which have searchable
     * as true.
     */
    private String value;

    /**
     * true if the global filter should be treated as a regular expression for
     * advanced searching, false otherwise. Note that normally server-side
     * processing scripts will not perform regular expression searching for
     * performance reasons on large data sets, but it is technically possible
     * and at the discretion of your script.
     */
    private Boolean regex;

    public Search() {
        this.value = "";
        regex = false;
    }
    
    

    public Search(String value, boolean regex) {
        this.value = value;
        this.regex = regex;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the regex
     */
    public Boolean getRegex() {
        return regex;
    }

    /**
     * @param regex the regex to set
     */
    public void setRegex(Boolean regex) {
        this.regex = regex;
    }
    
    
}
