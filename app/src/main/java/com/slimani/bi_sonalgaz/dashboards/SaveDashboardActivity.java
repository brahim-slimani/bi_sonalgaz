package com.slimani.bi_sonalgaz.dashboards;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.reports.ItemReport;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoDashboard;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.loggedUser;


public class SaveDashboardActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {


    public static final String TAG = "SaveDashboardActivity";
    List<PojoReport> reports = new ArrayList<PojoReport>();

    private SearchView searchView;
    private MenuItem searchMenuItem;
    private ListView listViewReports;
    private ReportsDashboardFilterAdapter filterListAdapter;
    private ArrayList<ItemReport> listItemsReports;
    private ArrayList<PojoReport> checkedReports;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_dashboard);

        listViewReports = findViewById(R.id.list_view_reports_dashboard);


        listItemsReports = new ArrayList<>();
        checkedReports = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(getApplicationContext());
                try {
                    reports = dataManager.getReports(service.consumesRest("/reports?username=" + loggedUser));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SaveDashboardActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i < reports.size()) {

                            try {
                                ItemReport ItemReport = new ItemReport();
                                ItemReport.setTitle(reports.get(i).getTitle());
                                ItemReport.setContext(reports.get(i).getContext());
                                ItemReport.setType(reports.get(i).getType());
                                ItemReport.setColumns(dataManager.getAxes(reports.get(i).getColumns()));
                                ItemReport.setRows(dataManager.getAxes(reports.get(i).getRows()));

                                filterImagesReports(ItemReport);


                                listItemsReports.add(ItemReport);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            i++;
                        }

                        filterListAdapter = new ReportsDashboardFilterAdapter(SaveDashboardActivity.this, listItemsReports);

                        listViewReports.setAdapter(filterListAdapter);
                        listViewReports.setTextFilterEnabled(false);

                        listViewReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object itemObject = parent.getAdapter().getItem(position);
                                ItemReport itemReport = (ItemReport) itemObject;

                                String titleReport = itemReport.getTitle();
                                String typeReport = itemReport.getType();
                                String contextReport = itemReport.getContext();
                                List<String> columnsReport = new ArrayList<>(itemReport.getColumns());
                                List<String> rowsReport = new ArrayList<>(itemReport.getRows());
                                boolean checked = itemReport.isChecked();

                                Toast.makeText(SaveDashboardActivity.this,
                                        "title: " + titleReport,
                                        Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
        thread.start();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save_dashboard, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search_report);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        MenuItem saveDashboardMenu = menu.findItem(R.id.save_dashboard);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View view = findViewById(R.id.save_dashboard);

                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            // Do something...

                            Toast.makeText(getApplicationContext(), "Adding new Dashboard", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        });

        saveDashboardMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                for (int i = 0; i < filterListAdapter.checkedItemsList.size(); i++) {
                    checkedReports.add(new PojoReport(filterListAdapter.checkedItemsList.get(i).getTitle(), filterListAdapter.checkedItemsList.get(i).getContext(), filterListAdapter.checkedItemsList.get(i).getType()));

                }
                final Dialog dialog = new Dialog(SaveDashboardActivity.this);
                dialog.setContentView(R.layout.popup_save_dashboard);

                dialog.show();
                Button save_dashboard_btn = (Button) dialog.findViewById(R.id.save_dashboard_btn);
                save_dashboard_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText title_dashboard = (EditText) dialog.findViewById(R.id.title_dashboard);
                        if (title_dashboard.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(), "You must enter a title for the dashboard", Toast.LENGTH_SHORT).show();
                        } else {
                            JSONArray reportJSONArray = new JSONArray();

                            try {
                                for (PojoReport pojoReport : checkedReports) {
                                    JSONObject jsonObject = new JSONObject();
                                    jsonObject.put("title", pojoReport.getTitle());
                                    jsonObject.put("context", pojoReport.getContext());
                                    jsonObject.put("type", pojoReport.getType());

                                    reportJSONArray.put(jsonObject);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            PojoDashboard dashboard = new PojoDashboard();
                            dashboard.setTitle(title_dashboard.getText().toString());
                            dashboard.setReportsjsonarray(reportJSONArray);
                            dashboard.setReportsString(reportJSONArray.toString());
                            dashboard.setUsername(loggedUser);

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Service service = new Service(getApplicationContext());
                                    String response = service.saveDashboard("/saveDashboard", dashboard);
                                    if (response.equals("Operation failed, REST issue !")) {


                                        SaveDashboardActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                    } else if (response.equals("Dashboard created successfully !")) {
                                        SaveDashboardActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                dialog.dismiss();
                                                saveDashboardMenu.setEnabled(false);
                                                successAlert("The Dashboard was saved successfully");


                                            }
                                        });
                                    }


                                }
                            });
                            thread.start();

                        }

                    }
                });


                return true;
            }

        });


        return true;
    }


    public void filterImagesReports(ItemReport ItemReport) {

        if (ItemReport.getType().equals("crosstable")) {
            ItemReport.setImageId(R.drawable.crosstab);
        } else if (ItemReport.getType().equals("columnbarchart")) {
            ItemReport.setImageId(R.drawable.columnchar);
        } else if (ItemReport.getType().equals("piechart")) {
            ItemReport.setImageId(R.drawable.piechart);
        } else if (ItemReport.getType().equals("linechart")) {
            ItemReport.setImageId(R.drawable.linechart);
        }

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        filterListAdapter.getFilter().filter(s);

        return true;
    }

    public void successAlert(String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Successful !");
        alertDialogBuilder.setIcon(R.drawable.success);
        alertDialogBuilder.setMessage(msg);

        alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(SaveDashboardActivity.this, DashboardListActivity.class);
                startActivity(intent);


            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}
