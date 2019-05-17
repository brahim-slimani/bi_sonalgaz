package com.slimani.bi_sonalgaz.restful;

import com.slimani.bi_sonalgaz.restful.aesEncryption.AESCrypt;

public class ApiController {

    //public final static String BASE_URL="http://192.168.1.35:8092/api/olap";

    AESCrypt aesCrypt = new AESCrypt();





    /*
    public String getTokenAccess(final String username, final String password, final Context context){


        final RequestQueue requestQueue;
        final StringRequest stringRequest;
        String url = "http://192.168.1.35:8092/token";
        final int[] code = new int[1];

        JSONObject json = new JSONObject();
        try {
            json.put("userName",username);
            json.put("password",aesCrypt.encrypt(password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,json, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {

                System.out.println("response : "+response);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                System.out.println("error : "+error.toString());

                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String jsonError = new String(networkResponse.data);
                    int code = networkResponse.statusCode;
                    System.out.println("the code is : "+ code);
                }
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
        return String.valueOf(code[0]);


    }*/



}
