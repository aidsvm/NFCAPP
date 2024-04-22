package com.example.nfcapp.api;

import com.example.nfcapp.model.AssignNfcRequest;
import com.example.nfcapp.model.LoginDto;
import com.example.nfcapp.model.NFCEntity;
import com.example.nfcapp.model.ObjectAdminDto;
import com.example.nfcapp.model.ObjectDto;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    @GET("admin/getAllObjects")
    Call<List<ObjectEntity>> getAllObjects();

    @POST("admin/addObject")
    Call<ObjectEntity> addObject(@Body ObjectDto objectDto);

    @GET("objects/getObjectInfoByNfcId")
    Call<ObjectEntity> getObjectInfoByNfcId(@Query("NfcId") String nfcId);


    @POST("admin/login")
    Call<LoginResponse> loginAdmin(@Body LoginDto loginDto);

    @POST("admin/assignNfc")
    Call<ObjectEntity> assignNfc(@Body AssignNfcRequest assignNfcRequest);

    @DELETE("admin/removeObject")
    Call<Void> removeObject(@Query("objectId") Long id);

    @FormUrlEncoded
    @POST("nfc/addNfc")
    Call<NFCEntity> addNfc(@Field("nfcId") String nfcId);


}

