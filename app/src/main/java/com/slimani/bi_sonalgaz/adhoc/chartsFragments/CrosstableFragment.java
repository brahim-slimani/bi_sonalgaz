package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HeatDataEntry;
import com.anychart.charts.HeatMap;
import com.anychart.core.ui.ChartCredits;
import com.anychart.enums.SelectionMode;
import com.anychart.graphics.vector.SolidFill;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocColumns;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocRows;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.dataJS;

public class CrosstableFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_crosstable, container, false);

        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));
        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");

        HeatMap crossTable = AnyChart.heatMap();

        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
        ChartCredits cc = crossTable.credits();
        cc.text("ELIT-SONALGAZ");



        /*
        riskMap.stroke("1 #fff");
        riskMap.hovered()
                .stroke("6 #fff")
                .fill(new SolidFill("#545f69", 1d))
                .labels("{ fontColor: '#fff' }");

        riskMap.interactivity().selectionMode(SelectionMode.NONE);

        riskMap.title().enabled(true);
        riskMap.title()
                .text("Risk Matrix in Project Server")
                .padding(0d, 0d, 20d, 0d);

        riskMap.labels().enabled(true);
        riskMap.labels()
                .minFontSize(14d)
                .format("function() {\n" +
                        "      var namesList = [\"Low\", \"Medium\", \"High\", \"Extreme\"];\n" +
                        "      return namesList[this.heat];\n" +
                        "    }");

        riskMap.yAxis(0).stroke(null);
        riskMap.yAxis(0).labels().padding(0d, 15d, 0d, 0d);
        riskMap.yAxis(0).ticks(false);
        riskMap.xAxis(0).stroke(null);
        riskMap.xAxis(0).ticks(false);

        riskMap.tooltip().title().useHtml(true);
        riskMap.tooltip()
                .useHtml(true)
                .titleFormat("function() {\n" +
                        "      var namesList = [\"Low\", \"Medium\", \"High\", \"Extreme\"];\n" +
                        "      return '<b>' + namesList[this.heat] + '</b> Residual Risk';\n" +
                        "    }")
                .format("function () {\n" +
                        "       return '<span style=\"color: #CECECE\">Likelihood: </span>' + this.x + '<br/>' +\n" +
                        "           '<span style=\"color: #CECECE\">Consequence: </span>' + this.y;\n" +
                        "   }");
*/


        DataManager dataManager = new DataManager();



       crossTable.width(200);



        try {
            crossTable.data(dataManager.parsingToListTable(dataJS,adhocColumns,adhocRows, getContext()));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        anyChartView.setChart(crossTable);


        return view;

    }



   
}


