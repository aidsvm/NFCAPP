package com.example.nfcapp.api;

import com.example.nfcapp.model.AdminEntity;
import com.example.nfcapp.model.AssignNfcRequest;
import com.example.nfcapp.model.NFCEntity;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("admin/getAllObjects")
    Call<List<ObjectEntity>> getAllObjects();

    @FormUrlEncoded
    @POST("admin/addObject")
    Call<ObjectEntity> addObject(@Body ObjectDto objectDto);

    @GET("objects/getObjectInfoByNfcId")
    Call<ObjectEntity> getObjectInfoByNfcId(@Query("NfcId") String nfcId);

    @FormUrlEncoded
    @POST("admin/register")
    Call<AdminEntity> registerAdmin(@Field("username") String username,
                                    @Field("password") String password);


    @POST("admin/login")
    Call<LoginResponse> loginAdmin(@Body LoginDto login);

    @FormUrlEncoded
    @POST("admin/assignNfc")
    Call<ObjectEntity> assignNfc(@Body AssignNfcRequest assignNfcRequest);

    @FormUrlEncoded
    @POST("admin/removeObject")
    Call<ObjectEntity> removeObject(@Body ObjectEntity objectEntity);

    @FormUrlEncoded
    @POST("nfc/addNfc")
    Call<NFCEntity> addNfc(@Field("nfcId") String nfcId);

    @GET("endpoint")
    Call<Object> checkConnection();


}

