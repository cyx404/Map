package com.example.lenovo.map;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface RequestServices {
//  //get发送简单的两个字符串
//    @GET("get")
//    Call<String[]> getString(@Query("name") String name, @Query("password") String password);

    //get发送简单的一个字符串
    @GET("findPlaceByuserlabel")
    Call<ResponseBody> getString(@Query("phone") String phone);

    //post发送简单的两个字符串
    @FormUrlEncoded
    @POST("findPlacesByPraise")
    Call<ResponseBody> findPlacesByPraise(@Field("distance") int distance, @Field("lon") double lon, @Field("lat") double lat);

    //post发送List<String>
    @POST("postlist")
    Call<ResponseBody> postlist(@Body RequestBody responseBody);

    //发送一个javabean实体(responseMode)
    //@POST("javabean")
    //Call<ResponseBody> postjavabean(@Body ResponseMode responseMode);

    //发送一个Map
    @POST("editmyplans")
    Call<ResponseBody> postmap(@Body RequestBody responseBody);

    //发送一个List<Map<String,String>>
    // @POST("listmap")
    @POST("address_to_lal")
    Call<List<Map<String, String>>> postlistmap(@Body RequestBody responseBody);

    //发送一个List<javabean实体>
    //@POST("listjavabean")
    //Call<List<ResponseMode>> postlistjavabean(@Body RequestBody responseBody);

    //发送一个List<String[]>数组
    @POST("listArray")
    Call<List<String[]>> postlistarray(@Body RequestBody responseBody);

    //上传文件
    @Multipart
    @POST("uploadFile")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part file);
}
