package com.slimani.bi_sonalgaz.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.adhoc.AdhocActivity;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Service;
import com.slimani.bi_sonalgaz.setting.SettingActivity;

import org.json.JSONArray;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        final CardView dash_card = (CardView) findViewById(R.id.dash_btn);
        final CardView report_card = (CardView) findViewById(R.id.report_btn);
        final CardView adhoc_card = (CardView) findViewById(R.id.adhoc_btn);
        final CardView setting_card = (CardView) findViewById(R.id.setting_btn);

        Service serviceFact = new Service();
        serviceFact.consumesRest(getApplicationContext(),"/fact");

        Service serviceMeas = new Service();
        DataManager dm = new DataManager();
        String subURL = "/measures?factTable="+dm.getCurrentCube(getApplicationContext());
        serviceMeas.consumesRest(getApplicationContext(), subURL);

        Service serviceDm = new Service();
        String subURLDm = "/dimensions?factTable="+dm.getCurrentCube(getApplicationContext());
        serviceDm.consumesRest(getApplicationContext(), subURLDm);




        adhoc_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, AdhocActivity.class);
                JSONArray measures = serviceMeas.consumesRest(getApplicationContext(), subURL);
                JSONArray dimensions = serviceDm.consumesRest(getApplicationContext(), subURLDm);
                intent.putExtra("measures",measures.toString());
                intent.putExtra("dimensions",dimensions.toString());
                startActivity(intent);
            }
        });

        setting_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this, SettingActivity.class);
                JSONArray js = serviceFact.consumesRest(getApplicationContext(),"/fact");
                intent.putExtra("facts",js.toString());
                startActivity(intent);
            }
        });




    }
}
