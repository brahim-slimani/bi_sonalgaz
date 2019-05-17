package com.slimani.bi_sonalgaz.restful.retrofit2;

public class AppUtils {

    public static final String BASE_URL = "http://192.168.1.35:8092/";

    public static ApiService getService(){
        return RetrofitClient.getClient(BASE_URL).create(ApiService.class);
    }
}
