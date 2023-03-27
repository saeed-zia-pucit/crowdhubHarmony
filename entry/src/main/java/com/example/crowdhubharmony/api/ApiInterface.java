package com.example.crowdhubharmony.api;

import com.example.crowdhubharmony.model.CheckinResponseModel;
import com.example.crowdhubharmony.model.LoginResponseModel;
import com.example.crowdhubharmony.model.UpdateStatusRequestModel;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiInterface {
    @GET("smart_watches/check_in")
    Call<CheckinResponseModel> checkIn(
            @Query("serial_number") String serialNumber,
            @Query("lat") Double latitude,
            @Query("lng") Double longitude,
            @Query("heart_rate") Float heartRate
    );

    @GET("smart_watches/log_in")
    Call<LoginResponseModel> login(
            @Query("serial_number") String serialNumber,
            @Query("fcm_token") String fcmToken
    );

    @GET("smart_watches/check_out")
    Call<LoginResponseModel> checkout(
            @Query("serial_number") String serialNumber
    );

    @PUT("smart_watches/{id}")
    Call<LoginResponseModel> updateStatus(
            @Path("id") int id,
            @Body UpdateStatusRequestModel body
    );

    @POST("smart_watches/send_alert_mobile_to_web")
    Call<LoginResponseModel> sendAlert(
            @Body JsonObject obj
    );

    @POST("smart_watch_features")
    Call<CheckinResponseModel> updateSteps(
            @Body JsonObject obj
    );
}
