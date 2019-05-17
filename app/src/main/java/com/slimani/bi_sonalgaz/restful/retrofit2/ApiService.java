package com.slimani.bi_sonalgaz.restful.retrofit2;


import com.slimani.bi_sonalgaz.restful.retrofit1.tokenResponse;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface ApiService {


    @FormUrlEncoded
    @POST("/token")
    Call<tokenResponse> login(@Header("Content-Type") String content_type,
                              @Field("userName") String username,
                              @Field("password") String password);

    @GET("/api/olap")
    Call<JSONArray> adhoc(@Query("query") String query);

    @GET("/api/olap/fact")
    Call<JSONArray> getFacts();
}
