package com.slimani.bi_sonalgaz.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.AdhocActivity;
import com.slimani.bi_sonalgaz.auth.LoginActivity;
import com.slimani.bi_sonalgaz.reports.ReportListActivity;
import com.slimani.bi_sonalgaz.setting.SettingActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final CardView dash_card = (CardView) findViewById(R.id.dash_btn);
        final CardView report_card = (CardView) findViewById(R.id.report_btn);
        final CardView adhoc_card = (CardView) findViewById(R.id.adhoc_btn);
        final CardView setting_card = (CardView) findViewById(R.id.setting_btn);





        adhoc_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, AdhocActivity.class);
                startActivity(intent);
            }
        });

        setting_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, SettingActivity.class);
                startActivity(intent);
            }
        });

        report_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, ReportListActivity.class);
                startActivity(intent);
            }
        });




    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu (Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_app, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_item:

                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.popup_about);
                Button btnOK = (Button) dialog.findViewById(R.id.aboutOk_btn);

                dialog.show();
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


        }
        switch (item.getItemId()) {

            case R.id.logout_item:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Confirmation..?");
                alertDialogBuilder.setIcon(R.drawable.ask);
                alertDialogBuilder.setMessage("Are you sure that you want to logout ?");


                alertDialogBuilder.setPositiveButton("ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.dismiss();

                        final ProgressDialog progressdialog = new ProgressDialog(HomeActivity.this);
                        progressdialog.setMessage("Logout....");
                        progressdialog.show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            public void run() {
                                progressdialog.dismiss();
                                Intent intent =new Intent(HomeActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }, 1000);

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





            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(intent);
    }




}
