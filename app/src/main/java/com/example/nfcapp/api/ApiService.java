package com.example.nfcapp.api;

import com.example.nfcapp.model.AdminEntity;
import com.example.nfcapp.model.AssignNfcRequest;
import com.example.nfcapp.model.NFCEntity;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("objects")
    Call<List<ObjectEntity>> getAllObjects();

    @FormUrlEncoded
    @POST("addObject")
    Call<ObjectEntity> addObject(@Field("objectName") String objectName,
                                 @Field("objectDesc") String objectDesc,
                                 @Field("objectLocation") String objectLocation);

    @GET("getObjectInfoByNfcId")
    Call<ObjectEntity> getObjectInfoByNfcId(@Query("NfcId") String nfcId);

    @FormUrlEncoded
    @POST("register")
    Call<AdminEntity> registerAdmin(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<AdminEntity> loginAdmin(@Field("username") String username,
                                 @Field("password") String password);
    @FormUrlEncoded
    @POST("assignNfc")
    Call<ObjectEntity> assignNfc(@Body AssignNfcRequest assignNfcRequest);

    @FormUrlEncoded
    @POST("removeObject")
    Call<ObjectEntity> removeObject(@Body ObjectEntity objectEntity);

    @FormUrlEncoded
    @POST("addNfc")
    Call<NFCEntity> addNfc(@Field("nfcId") String nfcId);

    @GET("endpoint")
    Call<Object> checkConnection();





}

