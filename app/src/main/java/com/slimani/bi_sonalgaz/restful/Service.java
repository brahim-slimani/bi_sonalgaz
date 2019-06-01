package com.slimani.bi_sonalgaz.restful;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.slimani.bi_sonalgaz.restful.aesEncryption.AESCrypt;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoReport;
import com.slimani.bi_sonalgaz.restful.pojoRest.PojoUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import static com.slimani.bi_sonalgaz.auth.LoginActivity.token;


public class Service {

    public Context context;
    public static String csrf = "/rest/olap";

    private DataManager dataManager = new DataManager();



    public JSONArray consumesRest(String subURL){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;

        JSONArray response = new JSONArray();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, new JSONArray(), future, future){
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(request);

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;

    }

    public String Token(PojoUser user){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+"/token";


        String response = new String();

        AESCrypt aesCrypt = new AESCrypt();

        JSONObject json = new JSONObject();
        try {

            json.put("userName",user.getUsername());
            json.put("password",aesCrypt.encrypt(user.getPassword()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL_BASE, json, future, future);
        requestQueue.add(request);

        try {
            response = future.get().getString("response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;


    }



    public String saveReport(String subURL, PojoReport report){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;
        String response = new String();

        JSONObject json = new JSONObject();
        try {
            json.put("title",report.getTitle());
            json.put("context",report.getContext());
            json.put("type",report.getType());
            json.put("columns",report.getColumns());
            json.put("rows",report.getRows());
            json.put("username",report.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, future, future){
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };

        requestQueue.add(request);

        try {
            response = future.get().getString("response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return response;


    }


    public String saveUser(String subURL, PojoUser user){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;
        String response = new String();
        
        JSONObject json = new JSONObject();
        try {
            json.put("userName",user.getUsername());
            json.put("password",user.getPassword());
            json.put("role",user.getRole());
        } catch (Exception e) {
            e.printStackTrace();
        }



        //sync call to the rest
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL, json, future, future){
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(request);
        
        try {
            response = future.get().getString("response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //async call
/*
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("response " +response.toString());
                try {
                    responseUser = response.getString("response");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

        requestQueue.add(jsonObjectRequest);*/

        return response;

    }



    public JSONArray getUsers(String subURL){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;

        JSONArray response = new JSONArray();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONArray> future = RequestFuture.newFuture();
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, URL, new JSONArray(), future, future){
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(request);

        try {
            response = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return response;


    }

    public String getRoleUser(String subURL){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;

        String response = new String();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), future, future){

            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };


        requestQueue.add(request);

        try {
            response = future.get().get("response").toString();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;

    }

    public String deleteUser(String subURL){

        String URL_BASE = "http://"+dataManager.getCurrentIPaddress(this.getContext())+":"+dataManager.getCurrentPortNumber(this.getContext())+csrf;

        String URL = URL_BASE+subURL;

        String response = new String();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, URL, new JSONObject(), future, future){
            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };
        requestQueue.add(request);

        try {
            response = future.get().getString("response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;


    }

    public String testConnection(String ipAddress, String portNumber){

        String URL = "http://"+ipAddress+":"+portNumber+"/rest/connection";
        System.out.println(URL);

        String response = new String();

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, new JSONObject(), future, future){

            @Override
            public HashMap<String, String> getHeaders() {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorisation", token);
                params.put("Content-Type", "application/json;charset=UTF-8");
                params.put("Accept", "application/json");
                return params;
            }
        };

        requestQueue.add(request);

        try {
            response = future.get().getString("response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return response;


    }




    public Service() {
    }

    public Service(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }




}
