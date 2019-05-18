package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.axes.Linear;
import com.anychart.core.cartesian.series.Bar;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.ChartCredits;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LabelsOverlapMode;
import com.anychart.enums.Orientation;
import com.anychart.enums.Position;
import com.anychart.enums.ScaleStackMode;
import com.anychart.enums.TooltipDisplayMode;
import com.anychart.enums.TooltipPositionMode;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocColumns;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocRows;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.dataJS;

public class ColumnbarFrgament extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_columnbarchart, container, false);

        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        final Cartesian cartesian = AnyChart.column();

        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
        ChartCredits cc = cartesian.credits();
        cc.text("ELIT-SONALGAZ");

        /*
        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Rouge", 80540));
        data.add(new ValueDataEntry("Foundation", 94190));
        data.add(new ValueDataEntry("Mascara", 102610));
        data.add(new ValueDataEntry("Lip gloss", 110430));
        data.add(new ValueDataEntry("Lipstick", 128000));
        data.add(new ValueDataEntry("Nail polish", 143760));
        data.add(new ValueDataEntry("Eyebrow pencil", 170670));
        data.add(new ValueDataEntry("Eyeliner", 213210));
        data.add(new ValueDataEntry("Eyeshadows", 249980));*/

        DataManager dataManager = new DataManager();

        List<DataEntry> data = null;
        try {
            data = dataManager.parsingToListBar(dataJS, adhocColumns, adhocRows);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Set set = Set.instantiate();
        set.data(data);

        if(adhocColumns.size() == 1){

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(adhocColumns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

        }else if(adhocColumns.size() == 2){

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(adhocColumns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Column series2 = cartesian.column(series2Data);
            series2.name(adhocColumns.get(1));
            series2.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");
        }if(adhocColumns.size() == 3) {

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(adhocColumns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Column series2 = cartesian.column(series2Data);
            series2.name(adhocColumns.get(1));
            series2.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");

            Column series3 = cartesian.column(series3Data);
            series3.name(adhocColumns.get(2));
            series3.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");
        }






        cartesian.animation(true);
        //cartesian.title("delai");

        cartesian.yScale().minimum(0d);

        cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        cartesian.xAxis(0).title("Columns");
        cartesian.yAxis(0).title("Rows");

        cartesian.legend().enabled(true);
        cartesian.legend().inverted(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 20d, 0d);

        anyChartView.setChart(cartesian);




/*

        Cartesian barChart = AnyChart.bar();

        barChart.animation(true);

        barChart.padding(10d, 20d, 5d, 20d);

        barChart.yScale().stackMode(ScaleStackMode.VALUE);

        barChart.yAxis(0).labels().format(
                "function() {\n" +
                        "    return Math.abs(this.value).toLocaleString();\n" +
                        "  }");

        barChart.yAxis(0d).title("Revenue in Dollars");

        barChart.xAxis(0d).overlapMode(LabelsOverlapMode.ALLOW_OVERLAP);

        Linear xAxis1 = barChart.xAxis(1d);
        xAxis1.enabled(true);
        xAxis1.orientation(Orientation.RIGHT);
        xAxis1.overlapMode(LabelsOverlapMode.ALLOW_OVERLAP);

        barChart.title("Cosmetic Sales by Gender");

        barChart.interactivity().hoverMode(HoverMode.BY_X);

        barChart.tooltip()
                .title(false)
                .separator(false)
                .displayMode(TooltipDisplayMode.SEPARATED)
                .positionMode(TooltipPositionMode.POINT)
                .useHtml(true)
                .fontSize(12d)
                .offsetX(5d)
                .offsetY(0d)
                .format(
                        "function() {\n" +
                                "      return '<span style=\"color: #D9D9D9\">$</span>' + Math.abs(this.value).toLocaleString();\n" +
                                "    }");

        List<DataEntry> seriesData = new ArrayList<>();
        seriesData.add(new CustomDataEntry("Nail polish", 5376, -229));
        seriesData.add(new CustomDataEntry("Eyebrow pencil", 10987, -932));
        seriesData.add(new CustomDataEntry("Rouge", 7624, -5221));
        seriesData.add(new CustomDataEntry("Lipstick", 8814, -256));
        seriesData.add(new CustomDataEntry("Eyeshadows", 8998, -308));
        seriesData.add(new CustomDataEntry("Eyeliner", 9321, -432));
        seriesData.add(new CustomDataEntry("Foundation", 8342, -701));
        seriesData.add(new CustomDataEntry("Lip gloss", 6998, -908));
        seriesData.add(new CustomDataEntry("Mascara", 9261, -712));
        seriesData.add(new CustomDataEntry("Shampoo", 5376, -9229));
        seriesData.add(new CustomDataEntry("Hair conditioner", 10987, -13932));
        seriesData.add(new CustomDataEntry("Body lotion", 7624, -10221));
        seriesData.add(new CustomDataEntry("Shower gel", 8814, -12256));
        seriesData.add(new CustomDataEntry("Soap", 8998, -5308));
        seriesData.add(new CustomDataEntry("Eye fresher", 9321, -432));
        seriesData.add(new CustomDataEntry("Deodorant", 8342, -11701));
        seriesData.add(new CustomDataEntry("Hand cream", 7598, -5808));
        seriesData.add(new CustomDataEntry("Foot cream", 6098, -3987));
        seriesData.add(new CustomDataEntry("Night cream", 6998, -847));
        seriesData.add(new CustomDataEntry("Day cream", 5304, -4008));
        seriesData.add(new CustomDataEntry("Vanila cream", 9261, -712));

        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");
        Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");
        Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");

        Bar series1 = barChart.bar(series1Data);
        series1.name("Females")
                .color("HotPink");
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER);

        Bar series2 = barChart.bar(series2Data);
        series2.name("Males");
        series2.tooltip()
                .position("left")
                .anchor(Anchor.RIGHT_CENTER);

        Bar series3 = barChart.bar(series3Data);
        series3.name("Mixte");
        series3.tooltip()
                .position("left")
                .anchor(Anchor.RIGHT_CENTER);

        barChart.legend().enabled(true);
        barChart.legend().inverted(true);
        barChart.legend().fontSize(13d);
        barChart.legend().padding(0d, 0d, 20d, 0d);

        anyChartView.setChart(barChart);


        System.out.println(dataJS);


*/

        return view;

    }



}
