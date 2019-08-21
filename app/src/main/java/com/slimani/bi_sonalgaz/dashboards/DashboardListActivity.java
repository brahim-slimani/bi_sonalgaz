package com.slimani.bi_sonalgaz.dashboards;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.home.HomeActivity;
import com.slimani.bi_sonalgaz.reports.ItemReport;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoDashboard;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.loggedUser;


public class DashboardListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    List<PojoDashboard> dashboards = new ArrayList<PojoDashboard>();


    private ListView listViewDashboards;
    private SearchView searchView;
    private MenuItem searchMenuItem;
    private DashboardFilterAdapter filterListAdapter;
    private ArrayList<ItemDashboard> listItemsDashboards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);


        listViewDashboards = findViewById(R.id.list_view_dashboards);
        listItemsDashboards = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                DataManager dataManager = new DataManager();
                Service service = new Service(getApplicationContext());
                try {
                    dashboards = dataManager.getDashboards(service.consumesRest("/dashboards?username=" + loggedUser));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                DashboardListActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int i = 0;
                        while (i < dashboards.size()) {

                            ItemDashboard itemDashboard = new ItemDashboard();
                            itemDashboard.setId(dashboards.get(i).getId());
                            itemDashboard.setTitle(dashboards.get(i).getTitle());
                            filterImagesDashboards(itemDashboard);

                            listItemsDashboards.add(itemDashboard);

                            i++;
                        }


                        filterListAdapter = new DashboardFilterAdapter(DashboardListActivity.this, listItemsDashboards);

                        listViewDashboards.setAdapter(filterListAdapter);
                        listViewDashboards.setTextFilterEnabled(false);

                        listViewDashboards.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Object itemObject = parent.getAdapter().getItem(position);
                                ItemDashboard itemDashboard = (ItemDashboard) itemObject;

                                Intent intent = new Intent(DashboardListActivity.this, DashboardActivity.class);
                                intent.putExtra("idDashboard", itemDashboard.getId());
                                intent.putExtra("titleDashboard", itemDashboard.getTitle());
                                startActivity(intent);
                            }
                        });
                    }
                });

            }
        });
        thread.start();

        listViewDashboards.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object itemObject = parent.getAdapter().getItem(position);
                ItemDashboard itemDashboard = (ItemDashboard) itemObject;


                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DashboardListActivity.this);
                alertDialogBuilder.setTitle("Deleting..?");
                alertDialogBuilder.setIcon(R.drawable.delete);
                alertDialogBuilder.setMessage("Are you sure you want to delete this Dashboard ?");


                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                DataManager dataManager = new DataManager();
                                Service service = new Service(getApplicationContext());
                                service.deleteDashboard("/deleteDashboard?title="+itemDashboard.getTitle());


                                try {
                                    dashboards = dataManager.getDashboards(service.consumesRest("/dashboards?username="+loggedUser));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                                DashboardListActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {


                                        listItemsDashboards.clear();
                                        int i = 0;
                                        while (i<dashboards.size()){

                                            /*
                                            ItemReport itemReport = new ItemReport();
                                            itemReport.setTitle(dashboards.get(i).getTitle());


                                            filterImagesDashboards(itemDashboard);

                                            listItemsDashboards.add(itemDashboard);

                                            i++;*/

                                            ItemDashboard item = new ItemDashboard();
                                            item.setId(dashboards.get(i).getId());
                                            item.setTitle(dashboards.get(i).getTitle());
                                            filterImagesDashboards(item);

                                            listItemsDashboards.add(item);

                                            i++;
                                        }



                                        filterListAdapter = new DashboardFilterAdapter(DashboardListActivity.this,listItemsDashboards);

                                        listViewDashboards.setAdapter(filterListAdapter);
                                        listViewDashboards.setTextFilterEnabled(false);


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_dashboard, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchMenuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(this);

        MenuItem newDashboardMenu = menu.findItem(R.id.new_dashboard);
        newDashboardMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(DashboardListActivity.this, SaveDashboardActivity.class);
                startActivity(intent);
                return true;
            }

        });
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                final View view = findViewById(R.id.new_dashboard);

                if (view != null) {
                    view.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {

                            // Do something...

                            Toast.makeText(getApplicationContext(), "Add new Dashboard", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                    });
                }
            }
        });


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


    public void filterImagesDashboards(ItemDashboard itemDashboard) {

        List<Integer> iconsList = new ArrayList<>();
        iconsList.add(R.drawable.dashboard_icon_1);
        iconsList.add(R.drawable.dashboard_icon_2);
        iconsList.add(R.drawable.dashboard_icon_3);
        iconsList.add(R.drawable.dashboard_icon_4);
        iconsList.add(R.drawable.dashboard_icon_5);
        iconsList.add(R.drawable.dashboard_icon_6);

        int currentIcon = getRandomElement(iconsList);
        itemDashboard.setImageId(currentIcon);


    }

    public int getRandomElement(List<Integer> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(DashboardListActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}
