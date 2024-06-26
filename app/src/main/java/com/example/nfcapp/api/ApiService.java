package com.example.nfcapp.api;

import com.example.nfcapp.model.LoginDto;
import com.example.nfcapp.model.ObjectDto;
import com.example.nfcapp.model.ObjectEntity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.*;

/**
 * Sets up API methods.
 */
public interface ApiService {
    @GET("admin/getAllObjects")
    Call<List<ObjectEntity>> getAllObjects();

    @POST("admin/addObject")
    Call<ObjectEntity> addObject(@Body ObjectDto objectDto);

    @GET("objects/getObjectInfoByNfcId")
    Call<ObjectEntity> getObjectInfoByNfcId(@Query("NfcId") String nfcId);

    @POST("admin/login")
    Call<LoginResponse> loginAdmin(@Body LoginDto loginDto);

    @DELETE("admin/removeObject")
    Call<Void> removeObject(@Query("objectId") Long id);

    @POST("nfc/addNfc")
    Call<String> addNfc(@Query("nfcId") String nfcId);

    @GET("admin/getAdminId")
    Call<Long> getAdminId(@Query("username") String username);

}

