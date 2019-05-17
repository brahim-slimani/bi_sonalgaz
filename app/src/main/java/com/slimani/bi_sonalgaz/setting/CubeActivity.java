package com.slimani.bi_sonalgaz.setting;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.param.Db_manager;
import com.slimani.bi_sonalgaz.restful.DataManager;
import com.slimani.bi_sonalgaz.restful.Rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;



public class CubeActivity extends AppCompatActivity {
    Db_manager dbm = new Db_manager(this);
    public Cursor cursor;
    List<String> itemsCube = new ArrayList<String>();
    JSONArray jsonArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cube_setting);

        final Spinner cubes_spinner = (Spinner) findViewById(R.id.olap_cubes);
        final Button confirm_btn = (Button) findViewById(R.id.confirm_olapCube_btn);
        final TextView cube_msg = (TextView) findViewById(R.id.cube_msg);

        DataManager dataManager = new DataManager();


        String facts = getIntent().getStringExtra("facts");

        try {
            //jsonArray = new JSONArray(facts);
            itemsCube = dataManager.getItemsCubes(new JSONArray(facts));
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, itemsCube);
        cubes_spinner.setAdapter(adapter);

        try {

            if(init()){
                cube_msg.setText(dataManager.getCurrentCube(getApplicationContext())+" : is the current olap cube");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }








        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        if(init()){
                            dbm.refresh(1,cubes_spinner.getSelectedItem().toString());
                            cube_msg.setText(dataManager.getCurrentCube(getApplicationContext())+" : is the current olap cube");
                            Toast.makeText(getApplicationContext(),"The cube is selected successfully !",Toast.LENGTH_LONG).show();
                        }else{
                            dbm.initParam(1,cubes_spinner.getSelectedItem().toString());
                            cube_msg.setText(dataManager.getCurrentCube(getApplicationContext())+" : is the current olap cube");
                            Toast.makeText(getApplicationContext(),"The cube is selected successfully !",Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }



            }


        });


    }

    public boolean init() throws JSONException {
        boolean b;
        cursor = dbm.getCube(1);
        if(cursor.getCount() == 0){
            b = false;
        }else b = true;

        return b;
    }






}
