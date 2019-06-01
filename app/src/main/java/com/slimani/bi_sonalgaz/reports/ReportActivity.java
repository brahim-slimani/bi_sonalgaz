package com.slimani.bi_sonalgaz.reports;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.HeatDataEntry;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Cartesian;
import com.anychart.charts.HeatMap;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.core.ui.ChartCredits;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.MarkerType;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomPieContext;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class ReportActivity extends AppCompatActivity {


    JSONArray dataReport = new JSONArray();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        final AnyChartView anyChartView = findViewById(R.id.report_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.report_progress_bar));

        String title = getIntent().getStringExtra("titleReport");
        String type = getIntent().getStringExtra("typeReport");
        String context = getIntent().getStringExtra("contextReport");
        List<String> columns = getIntent().getStringArrayListExtra("columnsReport");
        List<String> rows = getIntent().getStringArrayListExtra("rowsReport");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(getApplicationContext());
                dataReport = service.consumesRest("?query="+context);

                ReportActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(type.equals("crosstable")){

                            HeatMap crossTable = AnyChart.heatMap();

                            anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
                            ChartCredits cc = crossTable.credits();
                            cc.text("ELIT-SONALGAZ");

                            crossTable.title()
                                    .text(title)
                                    .padding(0d, 0d, 20d, 0d);

                            DataManager dataManager = new DataManager();
                            crossTable.width(200);



                            try {
                                crossTable.data(dataManager.parsingToListTable(dataReport,columns,rows, getApplicationContext()));
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(), "Error when parsing data !", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }



                            anyChartView.setChart(crossTable);



                        }else if(type.equals("columnbarchart")){


                            final Cartesian cartesian = AnyChart.column();

                            anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
                            ChartCredits cc = cartesian.credits();
                            cc.text("ELIT-SONALGAZ");

                            DataManager dataManager = new DataManager();

                            List<DataEntry> data = null;
                            try {
                                data = dataManager.parsingToListBar(dataReport, columns, rows);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error when casting rows !", Toast.LENGTH_SHORT).show();
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
                            cartesian.title(title);

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


                        }else if(type.equals("piechart")){

                            Pie pie = AnyChart.pie3d();

                            anyChartView.setLicenceKey("b.slimani@esi-sba.dz-b0e90d3d-68aabd2c");
                            ChartCredits cc = pie.credits();
                            cc.text("ELIT-SONALGAZ");

                            pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
                                @Override
                                public void onClick(Event event) {
                                    Toast.makeText(getApplicationContext(), event.getData().get("x") + ":" + event.getData().get("value"), Toast.LENGTH_SHORT).show();
                                }
                            });

                            DataManager dataManager = new DataManager();

                            String currentColumn;
                            currentColumn = columns.get(0);


                            CustomPieContext pieContext = new CustomPieContext();
                            pieContext.setColumn(currentColumn);

                            try {
                                pieContext.setDataEntryList(dataManager.parsingToListPie(dataReport, currentColumn, rows));
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error when parsing data !", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }

                            pie.data(pieContext.getDataEntryList());


                            pie.title(title);

                            pie.labels().position("outside");

                            pie.legend().title().enabled(true);
                            pie.legend().title()
                                    .text(pieContext.getColumn())
                                    .padding(0d, 0d, 10d, 0d);

                            pie.legend()
                                    .position("center-bottom")
                                    .itemsLayout(LegendLayout.HORIZONTAL)
                                    .align(Align.CENTER);



                            anyChartView.setChart(pie);

                        }else if(type.equals("linechart")){

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
                                data = dataManager.parsingToListBar(dataReport, columns, rows);
                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Error when casting rows !", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                            Set set = Set.instantiate();
                            set.data(data);

                            if (columns.size() == 1) {

                                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

                                Line series1 = cartesian.line(series1Data);
                                series1.name(columns.get(0));
                                series1.hovered().markers().enabled(true);
                                series1.hovered().markers()
                                        .type(MarkerType.CIRCLE)
                                        .size(4d);
                                series1.tooltip()
                                        .position("right")
                                        .anchor(Anchor.LEFT_CENTER)
                                        .offsetX(5d)
                                        .offsetY(5d);


                            } else if (columns.size() == 2) {

                                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

                                Line series1 = cartesian.line(series1Data);
                                series1.name(columns.get(0));
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
                                series2.name(columns.get(1));
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
                            if (columns.size() == 3) {

                                Mapping series1Data = set.mapAs("{ x: 'x', value: 'value' }");

                                Line series1 = cartesian.line(series1Data);
                                series1.name(columns.get(0));
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
                                series2.name(columns.get(1));
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
                                series3.name(columns.get(2));
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

                        }

                    }
                });

            }
        });
        thread.start();




    }


}
