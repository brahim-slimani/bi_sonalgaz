package com.slimani.bi_sonalgaz.dashboards.chartFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.ui.ChartCredits;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;

import java.util.List;

import static com.slimani.bi_sonalgaz.dashboards.DashboardDetailRecyclerViewAdapter.columns;
import static com.slimani.bi_sonalgaz.dashboards.DashboardDetailRecyclerViewAdapter.dataReport;
import static com.slimani.bi_sonalgaz.dashboards.DashboardDetailRecyclerViewAdapter.rows;


public class ColumnbarFrgament extends Fragment {
    View view;
    
    
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_columnbarchart, container, false);

        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        final Cartesian cartesian = AnyChart.column();

        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
        ChartCredits cc = cartesian.credits();
        cc.text("ELIT-SONALGAZ");

        DataManager dataManager = new DataManager();

        List<DataEntry> data = null;
        try {
            data = dataManager.parsingToListBar(dataReport, columns, rows);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error when casting rows !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        Set set = Set.instantiate();
        set.data(data);

        if(columns.size() == 1){

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(columns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

        }else if(columns.size() == 2){

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(columns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Column series2 = cartesian.column(series2Data);
            series2.name(columns.get(1));
            series2.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");
        }if(columns.size() == 3) {

            Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

            Column series1 = cartesian.column(series1Data);
            series1.name(columns.get(0));
            series1.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series2Data = set.mapAs("{ x: 'x', value: 'value2' }");

            Column series2 = cartesian.column(series2Data);
            series2.name(columns.get(1));
            series2.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");

            Mapping series3Data = set.mapAs("{ x: 'x', value: 'value3' }");

            Column series3 = cartesian.column(series3Data);
            series3.name(columns.get(2));
            series3.tooltip()
                    .titleFormat("{%X}")
                    .position(Position.CENTER_BOTTOM)
                    .anchor(Anchor.CENTER_BOTTOM)
                    .offsetX(0d)
                    .offsetY(5d)
                    .format("{%Value}{groupsSeparator: }");
        }





        cartesian.animation(true);
        //cartesian.title("");

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





        return view;

    }



}
