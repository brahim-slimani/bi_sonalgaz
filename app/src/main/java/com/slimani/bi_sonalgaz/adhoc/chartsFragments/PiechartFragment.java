package com.slimani.bi_sonalgaz.adhoc.chartsFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.core.ui.ChartCredits;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocColumns;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.adhocRows;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.customPieContext;
import static com.slimani.bi_sonalgaz.adhoc.AdhocActivity.dataJS;

public class PiechartFragment extends Fragment {
    View view;
    String currentColumn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_piechart, container, false);

        final AnyChartView anyChartView = view.findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(view.findViewById(R.id.progress_bar));


        Pie pie = AnyChart.pie3d();

        anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
        ChartCredits cc = pie.credits();
        cc.text("ELIT-SONALGAZ");

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
                Toast.makeText(getActivity(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
            }
        });

        DataManager dataManager = new DataManager();

        currentColumn = adhocColumns.get(0);

        CustomPieContext pieContext = new CustomPieContext();
        pieContext.setColumn(currentColumn);
        try {
            pieContext.setDataEntryList(dataManager.parsingToListPie(dataJS, currentColumn, adhocRows));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Error when parsing data !", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        pie.data(customPieContext.getDataEntryList());


        //pie.title("");

        pie.labels().position("outside");

        pie.legend().title().enabled(true);
        pie.legend().title()
                .text(customPieContext.getColumn())
                .padding(0d, 0d, 10d, 0d);

        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);



        anyChartView.setChart(pie);





        return view;
    }


}
