package com.slimani.bi_sonalgaz.adhoc.itemsParam;

import java.util.List;

public class FilterColumn {

    private String dimension;
    private String column;
    private List<String> rows;


    public FilterColumn(String dimension, String column, List<String> rows) {
        this.dimension = dimension;
        this.column = column;
        this.rows = rows;
    }

    public FilterColumn() {
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<String> getRows() {
        return rows;
    }

    public void setRows(List<String> rows) {
        this.rows = rows;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }
}
