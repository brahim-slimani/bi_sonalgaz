package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class Rest {

    public static String BASE_URL = "http://192.168.1.35:8092/api/olap";
    static JSONArray jsonArray;

    public  JSONArray consumeRest(Context ctx, String subURL){
        String URL = BASE_URL+subURL;


        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, URL,
                null, new com.android.volley.Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.e("Rest Response",response.toString());
                jsonArray = response;
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
        return jsonArray;
    }


}
