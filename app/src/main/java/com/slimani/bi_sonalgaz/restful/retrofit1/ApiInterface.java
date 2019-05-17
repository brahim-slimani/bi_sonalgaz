package com.slimani.bi_sonalgaz.restful.retrofit1;

import org.json.JSONArray;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;

public interface ApiInterface {



    @FormUrlEncoded
    @POST("/token")
    public void login(
            @Field("userName") String username,
            @Field("password") String password, Callback<tokenResponse> callback);

    @GET("/api/olap/fact")
    public void getFacts(Callback<JSONArray> callback);
}
