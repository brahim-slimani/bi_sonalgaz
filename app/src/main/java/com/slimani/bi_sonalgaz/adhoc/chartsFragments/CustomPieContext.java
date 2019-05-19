package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import com.anychart.chart.common.dataentry.DataEntry;

import java.util.List;

public class CustomPieContext {

    private String column;
    private List<DataEntry> dataEntryList;

    public CustomPieContext(String column, List<DataEntry> dataEntryList) {
        this.column = column;
        this.dataEntryList = dataEntryList;
    }

    public CustomPieContext() {
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public List<DataEntry> getDataEntryList() {
        return dataEntryList;
    }

    public void setDataEntryList(List<DataEntry> dataEntryList) {
        this.dataEntryList = dataEntryList;
    }
}
