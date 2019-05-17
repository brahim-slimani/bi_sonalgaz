package com.slimani.bi_sonalgaz.adhoc.itemsParam;

public class ItemMeasure {

    private String measure;
    private String function;

    public ItemMeasure(String measure, String function) {
        this.measure = measure;
        this.function = function;
    }

    public ItemMeasure() {
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
