package com.slimani.bi_sonalgaz.reports;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.AdhocActivity;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ItemDTO;
import com.slimani.bi_sonalgaz.adhoc.itemsParam.ListItemAdapterW;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;
import com.slimani.bi_sonalgaz.setting.UserActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.loggedUser;

public class ReportListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    List<PojoReport> reports = new ArrayList<PojoReport>();


    private ListView listViewReports;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private ReportFilterAdapter filterListAdapter;
    private ArrayList<ItemReport> listItemsReports;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);



        listViewReports = findViewById(R.id.list_view_reports);
        listItemsReports = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(getApplicationContext());
                try {
                    reports = dataManager.getReports(service.consumesRest("/reports?username="+loggedUser));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ReportListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i<reports.size()){

                            try {
                                ItemReport itemReport = new ItemReport();
                                itemReport.setTitle(reports.get(i).getTitle());
                                itemReport.setContext(reports.get(i).getContext());
                                itemReport.setType(reports.get(i).getType());
                                itemReport.setColumns(dataManager.getAxes(reports.get(i).getColumns()));
                                itemReport.setRows(dataManager.getAxes(reports.get(i).getRows()));

                                filterImagesReports(itemReport);

                                listItemsReports.add(itemReport);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            i++;
                        }



                        filterListAdapter = new ReportFilterAdapter(ReportListActivity.this,listItemsReports);

                        listViewReports.setAdapter(filterListAdapter);
                        listViewReports.setTextFilterEnabled(false);

                        listViewReports.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object itemObject = parent.getAdapter().getItem(position);
                                ItemReport itemReport = (ItemReport) itemObject;

                                Intent intent = new Intent(ReportListActivity.this,ReportActivity.class);
                                intent.putExtra("titleReport",itemReport.getTitle());
                                intent.putExtra("typeReport",itemReport.getType());
                                intent.putExtra("contextReport",itemReport.getContext());
                                intent.putStringArrayListExtra("columnsReport", (ArrayList<String>) itemReport.getColumns());
                                intent.putStringArrayListExtra("rowsReport", (ArrayList<String>) itemReport.getRows());
                                startActivity(intent);


                            }
                        });

                        listViewReports.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                            @Override
                            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                                Object itemObject = parent.getAdapter().getItem(position);
                                ItemReport itemReport = (ItemReport) itemObject;


                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ReportListActivity.this);
                                alertDialogBuilder.setTitle("Deleting..?");
                                alertDialogBuilder.setIcon(R.drawable.delete);
                                alertDialogBuilder.setMessage("Are you sure that you want to delete this report ?");


                                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                        Thread thread = new Thread(new Runnable() {
                                            @Override
                                            public void run() {

                                                DataManager dataManager = new DataManager();
                                                Service service = new Service(getApplicationContext());
                                                service.deleteReport("/deleteReport?title="+itemReport.getTitle());

                                                try {
                                                    reports = dataManager.getReports(service.consumesRest("/reports?username="+loggedUser));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                                ReportListActivity.this.runOnUiThread(new Runnable() {
                                                    public void run() {


                                                        listItemsReports.clear();
                                                        int i = 0;
                                                        while (i<reports.size()){

                                                            try {
                                                                ItemReport itemReport = new ItemReport();
                                                                itemReport.setTitle(reports.get(i).getTitle());
                                                                itemReport.setContext(reports.get(i).getContext());
                                                                itemReport.setType(reports.get(i).getType());
                                                                itemReport.setColumns(dataManager.getAxes(reports.get(i).getColumns()));
                                                                itemReport.setRows(dataManager.getAxes(reports.get(i).getRows()));

                                                                filterImagesReports(itemReport);

                                                                listItemsReports.add(itemReport);
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }

                                                            i++;
                                                        }



                                                        filterListAdapter = new ReportFilterAdapter(ReportListActivity.this,listItemsReports);

                                                        listViewReports.setAdapter(filterListAdapter);
                                                        listViewReports.setTextFilterEnabled(false);


                                                        arg0.dismiss();
                                                    }
                                                });


                                            }
                                        });
                                        thread.start();



                                    }
                                });

                                alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });

                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();

                                return true;
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
        inflater.inflate(R.menu.menu_report, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);



        return true;
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


    public void filterImagesReports(ItemReport itemReport){

        if(itemReport.getType().equals("crosstable")){
            itemReport.setImageId(R.drawable.crosstab);
        }else if(itemReport.getType().equals("columnbarchart")){
            itemReport.setImageId(R.drawable.columnchar);
        }else if(itemReport.getType().equals("piechart")){
            itemReport.setImageId(R.drawable.piechart);
        }else if(itemReport.getType().equals("linechart")){
            itemReport.setImageId(R.drawable.linechart);
        }

    }


}
