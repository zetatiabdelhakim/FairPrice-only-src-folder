package com.example.fairprice_app;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    @FormUrlEncoded
    @POST("/upload")
    Call<ResponseBody> uploadImage(@Field("image") String imageString);
}

