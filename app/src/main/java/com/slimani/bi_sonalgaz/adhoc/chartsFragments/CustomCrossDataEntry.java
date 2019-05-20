package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import com.anychart.chart.common.dataentry.HeatDataEntry;

public class CustomCrossDataEntry extends HeatDataEntry {

    public CustomCrossDataEntry(String x, String y, Integer heat) {
        super(x, y, heat);
    }


    public CustomCrossDataEntry(String x, String y, Integer heat, String fill) {
        super(x, y, heat);
        setValue("fill", fill);
    }



}
