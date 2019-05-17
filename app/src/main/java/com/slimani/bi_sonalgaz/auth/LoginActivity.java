package com.slimani.bi_sonalgaz.auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.slimani.bi_sonalgaz.R;
import com.slimani.bi_sonalgaz.home.HomeActivity;
import com.slimani.bi_sonalgaz.restful.Rest;

import org.json.JSONArray;

public class LoginActivity extends AppCompatActivity {
    JSONArray jsa = new JSONArray();
    public static Rest rest = new Rest();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText username_field = (EditText) findViewById(R.id.username);
        final EditText password_field = (EditText) findViewById(R.id.password);
        final Button login_btn = (Button) findViewById(R.id.login_submit_btn);


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                if(!validate(username_field, password_field)){
                    Toast.makeText(getApplicationContext(), "The fields must not be empty !", Toast.LENGTH_SHORT).show();
                }else{

                    try {


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }*/



                final ProgressDialog progressdialog = new ProgressDialog(LoginActivity.this);
                progressdialog.setMessage("Logging....");
                progressdialog.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        progressdialog.dismiss();
                        Intent intent =new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                    }
                }, 1000);


            }


        });


    }

    public Boolean validate(EditText username, EditText password){
        if(!username.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
            return true;
        }else
        return false;
    }








    public JSONArray getData(String query){
        String URL="http://192.168.1.35:8092/api/olap"+query;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Rest Response",response.toString());

                jsa = response;

            }
        },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest err",error.toString());
                    }
                }

        );

        requestQueue.add(objectRequest);

        return jsa;

    }





}
