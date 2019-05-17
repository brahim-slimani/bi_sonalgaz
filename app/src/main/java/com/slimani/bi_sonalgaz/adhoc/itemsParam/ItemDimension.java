package com.slimani.bi_sonalgaz.adhoc.itemsParam;

import java.util.ArrayList;

public class ItemDimension {
    private String dimension;
    private ArrayList<String> columns;

    public ItemDimension(String dimension, ArrayList<String> columns) {
        this.dimension = dimension;
        this.columns = columns;
    }

    public ItemDimension() {
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public ArrayList<String> getColumns() {
        return columns;
    }

    public void setColumns(ArrayList<String> columns) {
        this.columns = columns;
    }
}
