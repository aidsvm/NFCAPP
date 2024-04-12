package com.example.nfcapp.api;

import com.example.nfcapp.model.AdminEntity;
import com.example.nfcapp.model.AssignNfcRequest;
import com.example.nfcapp.model.NFCEntity;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("api/objects")
    Call<List<ObjectEntity>> getAllObjects();

    @FormUrlEncoded
    @POST("api/addObject")
    Call<ObjectEntity> addObject(@Field("objectName") String objectName,
                                 @Field("objectDesc") String objectDesc,
                                 @Field("objectLocation") String objectLocation);

    @GET("api/all")
    Call<ObjectEntity> getObjectInfoByNfcId();

    @FormUrlEncoded
    @POST("api/register")
    Call<AdminEntity> registerAdmin(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("api/login")
    Call<AdminEntity> loginAdmin(@Field("username") String username,
                                 @Field("password") String password);
    @FormUrlEncoded
    @POST("api/assignNfc")
    Call<ObjectEntity> assignNfc(@Body AssignNfcRequest assignNfcRequest);

    @FormUrlEncoded
    @POST("api/removeObject")
    Call<ObjectEntity> removeObject(@Body ObjectEntity objectEntity);

    @FormUrlEncoded
    @POST("api/addNfc")
    Call<NFCEntity> addNfc(@Field("nfcId") String nfcId);



}

