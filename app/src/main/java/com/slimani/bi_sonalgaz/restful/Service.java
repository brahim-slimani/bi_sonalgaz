package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.Map;


public class Service {

    public static Context context;
    public static String BASE_URL = "http://192.168.1.35:8092/api/olap";
    private JSONArray results;



    public JSONArray consumesRest(Context ctx,String subURL){
        String URL = BASE_URL+subURL;

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Rest Response",response.toString());
                results = response;
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
        return results;

    }

    public String saveReport(Context ctx, String subURL, PojoReport report){

        String URL = BASE_URL+subURL;

        JSONObject json = new JSONObject();
        try {
            json.put("title",report.getTitle());
            json.put("context",report.getContext());
            json.put("type",report.getType());
            json.put("username",report.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response " +response.toString());
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("error : "+error.toString());

            }
        }) {


            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                return headers;
            }


        };



        requestQueue.add(jsonObjectRequest);
        return "The view was saved as report successfully !";

    }






    public Service(Context context, String BASE_URL, JSONArray results) {
        this.context = context;
        this.BASE_URL = BASE_URL;
        this.results = results;
    }

    public Service(Context context, String BASE_URL) {
        this.context = context;
        this.BASE_URL = BASE_URL;
    }

    public Service(Context context) {
        this.context = context;
    }




    public Service() {
    }


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getBASE_URL() {
        return BASE_URL;
    }

    public void setBASE_URL(String BASE_URL) {
        this.BASE_URL = BASE_URL;
    }


    public JSONArray getResults() {
        return results;
    }

    public void setResults(JSONArray results) {
        this.results = results;
    }


}
