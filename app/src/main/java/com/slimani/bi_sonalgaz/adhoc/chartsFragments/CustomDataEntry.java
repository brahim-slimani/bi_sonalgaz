package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import com.anychart.chart.common.dataentry.ValueDataEntry;

public class CustomDataEntry extends ValueDataEntry {

    public CustomDataEntry(String x, Number value) {
        super(x, value);
    }

    public CustomDataEntry(String x, Number value, Number value2) {
        super(x, value);
        setValue("value2", value2);
    }

    public CustomDataEntry(String x, Number value, Number value2, Number value3) {
        super(x, value);
        setValue("value2", value2);
        setValue("value3", value3);
    }




    public CustomDataEntry(String x, Number value, String value2) {
        super(x, value);
        setValue("value2", value2);
    }


    public CustomDataEntry(String x, Number value, String value2, String value3) {
        super(x,value);
        setValue("value2", value2);
        setValue("value3", value3);
    }



}
