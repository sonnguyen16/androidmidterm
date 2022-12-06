package com.example.phpmysql;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Retrofit {
    String url = "http://10.0.2.2/MyApp/";

    Gson gson = new GsonBuilder().setLenient().create();


    Retrofit retrofit = new retrofit2.Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(Retrofit.class);


    @GET("GetFurniture.php")
    Call<List<Furniture>> getFurnitures();

    @FormUrlEncoded
    @POST("InsertFurniture.php")
    Call<String> insertFuniture(@Field("name") String name,
                                @Field("detail") String detail,
                                @Field("price") Float price,
                                @Field("image") String image);

    @Multipart
    @POST("UploadImage.php")
    Call<String> uploadImage(@Part MultipartBody.Part photo);

    @FormUrlEncoded
    @POST("UpdateFurniture.php")
    Call<String> updateFuniture(@Field("name") String name,
                                @Field("detail") String detail,
                                @Field("price") Float price,
                                @Field("image") String image,
                                @Field("id") int id);

    @FormUrlEncoded
    @POST("DeleteFurniture.php")
    Call<String> deleteFuniture(@Field("id") int id);
}
