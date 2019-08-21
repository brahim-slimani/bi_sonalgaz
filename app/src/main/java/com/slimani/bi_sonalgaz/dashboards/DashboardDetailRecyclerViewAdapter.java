package com.slimani.bi_sonalgaz.dashboards;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.anychart.AnyChartView;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.chartsFragments.CustomPieContext;
import com.slimani.bi_sonalgaz.dashboards.chartFragments.ColumnbarFrgament;
import com.slimani.bi_sonalgaz.dashboards.chartFragments.CrosstableFragment;
import com.slimani.bi_sonalgaz.dashboards.chartFragments.LineChartFragment;
import com.slimani.bi_sonalgaz.dashboards.chartFragments.PiechartFragment;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DashboardDetailRecyclerViewAdapter extends RecyclerView.Adapter<DashboardDetailRecyclerViewAdapter.ReportViewHolder> {

    private static final String TAG = "DashboardDetailRecyclerViewAdapter";
    public Context mContext;
    public List<PojoReport> reports;
    public static JSONArray dataReport = new JSONArray();
    public static List<String> columns = new ArrayList<>();
    public static List<String> rows = new ArrayList<>();
    private String columnsString;
    private String rowsString;
    private int idLayout;
    private FragmentManager fragmentManager;
    private String currentColumn;
    public static CustomPieContext customPieContext = new CustomPieContext();


    public DashboardDetailRecyclerViewAdapter(Context mContext, List<PojoReport> reports, FragmentManager fm) {
        this.mContext = mContext;
        this.reports = reports;
        this.fragmentManager = fm;

    }


    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.report_dashboard_item, viewGroup, false);
        return new ReportViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ReportViewHolder reportViewHolder, final int i) {

        //  reportViewHolder.reportTitleTXT.setText(reports.get(i).getTitle());


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(mContext);

                if (reports.size() <= 4) {
                    for (int k = 0; k < reports.size(); k++) {
                        dataReport = service.consumesRest("?query=" + reports.get(k).getContext());
                        columnsString = reports.get(k).getColumns().replace("[", "").replace("]", "").replace("\"", "");
                        rowsString = reports.get(k).getRows().replace("[", "").replace("]", "").replace("\"", "");

                        if ((k % 4) == 0) {
                            idLayout = R.id.fragment1;
                            displayFragments(dataManager, idLayout, columnsString, rowsString, k);
                        } else if ((k % 4) == 1) {
                            idLayout = R.id.fragment2;
                            displayFragments(dataManager, idLayout, columnsString, rowsString, k);
                        } else if ((k % 4) == 2) {
                            idLayout = R.id.fragment3;
                            displayFragments(dataManager, idLayout, columnsString, rowsString, k);
                        } else if ((k % 4) == 3) {
                            idLayout = R.id.fragment4;
                            displayFragments(dataManager, idLayout, columnsString, rowsString, k);
                        }


                    }
                }


            }
        });
        thread.start();


        // Set click listener

        reportViewHolder.reportItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext, "Report clicked", Toast.LENGTH_SHORT).show();
//                Intent intent = null;
//                if (reports.get(i).getType_view().equals("Table")) {
//                    intent = new Intent(mContext, ReportDetailActivity.class);
//                } else if (reports.get(i).getType_view().equals("Pie Chart")) {
//                    intent = new Intent(mContext, ReportPieChartActivity.class);
//                }
//
//                intent.putExtra("reportTitle", reports.get(i).getTitle());
//                intent.putExtra("reportId", reports.get(i).getId());
//                intent.putExtra("reportContext", reports.get(i).getContext());
//                intent.putExtra("typeView", reports.get(i).getType_view());
//                intent.putExtra("favorite", reports.get(i).isFavorite());
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return reports.size();
    }


    public static class ReportViewHolder extends RecyclerView.ViewHolder {

        public CardView reportItem;
        public ImageView reportImg;
        public AnyChartView anyChartView;
        public ProgressBar progressBar;
        public FrameLayout fragment1;
        public FrameLayout fragment2;
        public FrameLayout fragment3;
        public FrameLayout fragment4;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);

            reportItem = itemView.findViewById(R.id.report_item);
            fragment1 = itemView.findViewById(R.id.fragment1);
            fragment2 = itemView.findViewById(R.id.fragment2);
            fragment3 = itemView.findViewById(R.id.fragment3);
            fragment4 = itemView.findViewById(R.id.fragment4);
            //    reportTitleTXT = itemView.findViewById(R.id.report_title);
            reportImg = itemView.findViewById(R.id.report_item_image);


        }

    }

    private void loadFragment(Fragment fragment, int id) {
        fragmentManager.beginTransaction().replace(id, fragment).commit();
    }

    private void displayFragments(DataManager dataManager, int idLayout, String columnsString, String rowsString, int iteration) {

        columns = Arrays.asList(columnsString.split("\\s*,\\s*"));
        rows = Arrays.asList(rowsString.split("\\s*,\\s*"));
        ((DashboardActivity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String type = reports.get(iteration).getType();
                if (type.equals("linechart")) {
                    loadFragment(new LineChartFragment(), idLayout);

                } else if (type.equals("columnbarchart")) {
                    loadFragment(new ColumnbarFrgament(), idLayout);

                } else if (type.equals("piechart")) {
                    try {
                        currentColumn = columns.get(0);
                        customPieContext.setColumn(columns.get(0));
                        customPieContext.setDataEntryList(dataManager.parsingToListPie(dataReport, columns.get(0), rows));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loadFragment(new PiechartFragment(), idLayout);
                } else if (type.equals("crosstable")) {
                    loadFragment(new CrosstableFragment(), idLayout);

                }

            }
        });
    }
}
