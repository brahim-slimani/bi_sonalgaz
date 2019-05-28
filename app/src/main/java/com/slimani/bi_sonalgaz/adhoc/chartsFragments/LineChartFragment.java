package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.ui.ChartCredits;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;

import java.util.List;

import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocColumns;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocRows;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.dataJS;

public class LineChartFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_linechart, container, false);

        AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
        ChartCredits cc = cartesian.credits();
        cc.text("ELIT-SONALGAZ");

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 5d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                // TODO ystroke
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);


        DataManager dataManager = new DataManager();

        List<DataEntry> data = null;
        try {
            data = dataManager.parsingToListBar(dataJS, adhocColumns, adhocRows);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error when casting rows !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Set set = Set.instantiate();
        set.data(data);

        if (adhocColumns.size() == 1) {

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Line series1 = cartesian.line(series1Data);
            series1.name(adhocColumns.get(0));
            series1.hovered().markers().enabled(true);
            series1.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series1.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);


        } else if (adhocColumns.size() == 2) {

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Line series1 = cartesian.line(series1Data);
            series1.name(adhocColumns.get(0));
            series1.hovered().markers().enabled(true);
            series1.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series1.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Line series2 = cartesian.line(series2Data);
            series2.name(adhocColumns.get(1));
            series2.hovered().markers().enabled(true);
            series2.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series2.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

        }
        if (adhocColumns.size() == 3) {

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Line series1 = cartesian.line(series1Data);
            series1.name(adhocColumns.get(0));
            series1.hovered().markers().enabled(true);
            series1.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series1.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Line series2 = cartesian.line(series2Data);
            series2.name(adhocColumns.get(1));
            series2.hovered().markers().enabled(true);
            series2.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series2.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);

            Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");

            Line series3 = cartesian.line(series3Data);
            series3.name(adhocColumns.get(2));
            series3.hovered().markers().enabled(true);
            series3.hovered().markers()
                    .type(MarkerType.CIRCLE)
                    .size(4d);
            series3.tooltip()
                    .position("right")
                    .anchor(Anchor.LEFT_CENTER)
                    .offsetX(5d)
                    .offsetY(5d);
        }


        //cartesian.title("");

        cartesian.yAxis(0).title("Rows");
        cartesian.xAxis(0).title("Columns");
        cartesian.xAxis(0).labels().padding(5d, 5d, 5d, 5d);

        cartesian.legend().enabled(true);
        cartesian.legend().fontSize(13d);
        cartesian.legend().padding(0d, 0d, 10d, 0d);

        anyChartView.setChart(cartesian);


        return view;
    }
}
