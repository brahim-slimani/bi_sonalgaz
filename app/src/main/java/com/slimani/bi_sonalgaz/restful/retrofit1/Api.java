package com.slimani.bi_sonalgaz.restful.retrofit1;

import retrofit.RestAdapter;

public class Api {

    public static ApiInterface getClient() {

        RestAdapter adapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.1.35:8092/")
                .setRequestInterceptor(request -> request.addHeader("Content-Type","application/json"))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        ApiInterface api = adapter.create(ApiInterface.class);
        return api;
    }
}
