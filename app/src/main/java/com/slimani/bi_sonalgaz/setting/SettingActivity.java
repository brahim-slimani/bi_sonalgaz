package com.slimani.bi_sonalgaz.setting;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.slimani.bi_sonalgaz.R;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.roleUser;

public class SettingActivity extends TabActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);




        final TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
        TabHost.TabSpec spec;
        Intent intent;

        String facts = getIntent().getStringExtra("facts");

        if(roleUser.equals("ROLE_ADMIN")){
            spec = tabHost.newTabSpec("server");
            spec.setIndicator("SERVER");
            intent = new Intent(this, ServerActivity.class);
            spec.setContent(intent);
            tabHost.addTab(spec);

            spec = tabHost.newTabSpec("cube");
            spec.setIndicator("OLAP CUBE");
            intent = new Intent(this, CubeActivity.class);
            intent.putExtra("facts",facts);
            spec.setContent(intent);
            tabHost.addTab(spec);

            spec = tabHost.newTabSpec("user");
            spec.setIndicator("USERS");
            intent = new Intent(this, UserActivity.class);
            spec.setContent(intent);
            tabHost.addTab(spec);



            tabHost.setCurrentTab(0);

        }else{
            spec = tabHost.newTabSpec("server");
            spec.setIndicator("SERVER");
            intent = new Intent(this, ServerActivity.class);
            spec.setContent(intent);
            tabHost.addTab(spec);

            spec = tabHost.newTabSpec("cube");
            spec.setIndicator("OLAP CUBE");
            intent = new Intent(this, CubeActivity.class);
            intent.putExtra("facts",facts);
            spec.setContent(intent);
            tabHost.addTab(spec);

            tabHost.setCurrentTab(0);
        }


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
            }
        });


    }



    }
