package com.slimani.bi_sonalgaz.dashboards;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;


public class DashboardActivity extends AppCompatActivity {


    private static final String TAG = "DashboardActivity";

    RecyclerView reportRecyclerView;
    List<PojoReport> reportsList = new ArrayList<>();

    public DashboardActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        reportRecyclerView = findViewById(R.id.recycler_view_reports_Dashbord);
        Long id = getIntent().getLongExtra("idDashboard", 0);
        String title = getIntent().getStringExtra("titleDashboard");


        Thread thread = new Thread(() -> {
            DataManager dataManager = new DataManager();
            Service service = new Service(getApplicationContext());
            try {
                reportsList = dataManager.getReportsByDashboard(service.consumesRest("/dashboardReports/" + id));

                DashboardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setupRecyclerView(reportsList);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }


        });
        thread.start();


    }


    private void setupRecyclerView(List<PojoReport> reportList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        reportRecyclerView.setLayoutManager(gridLayoutManager);
        reportRecyclerView.setHasFixedSize(true);
        DashboardDetailRecyclerViewAdapter adapter = new DashboardDetailRecyclerViewAdapter(this, reportList, getSupportFragmentManager());
        reportRecyclerView.setAdapter(adapter);
    }




}
