package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.slimani.bi_sonalgaz.restful.retrofit2.AppUtils;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class Service {

    public static Context context;
    public static String BASE_URL = "http://192.168.1.35:8092/api/olap";
    private JSONArray results;


    public JSONArray consumesAdhocRest(){

        AppUtils.getService().adhoc("").enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
                results = response.body();

            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

        return results;

    }

    public JSONArray getFacts(){

        AppUtils.getService().getFacts().enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {

                 results = response.body();
            }

            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {
                Log.i(TAG, t.getMessage());
            }
        });

        return results;

    }




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
