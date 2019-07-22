package com.bonny.bonnyparent.api;

import com.bonny.bonnyparent.models.AppointmentModel;
import com.bonny.bonnyparent.models.BabyModel;
import com.bonny.bonnyparent.models.NotificationModel;
import com.bonny.bonnyparent.models.ParentModel;
import com.bonny.bonnyparent.models.TokenModel;
import com.bonny.bonnyparent.models.UserModel;
import com.bonny.bonnyparent.models.VaccineModel;
import com.bonny.bonnyparent.models.RecordModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author Aditya Kulkarni
 */

public interface API {

    @POST("auth/api/logout/")
    Call<UserModel> logout();

    @GET("api/vaccinations/")
    Call<List<RecordModel>> getRecords(
            @Header("Authorization") String key,
            @Query("pk") int pk
    );

    @GET("api/users/")
    Call<List<UserModel>> getExistingUsers();

    @GET("api/appointments/")
    Call<List<AppointmentModel>> getAppointments(
            @Header("Authorization") String key,
            @Query("pk") int pk
    );

    @POST("auth/api/login/")
    @FormUrlEncoded
    Call<TokenModel> getToken(
            @Field("username") String username,
            @Field("password") String password
    );

    @POST("auth/api/register/")
    @FormUrlEncoded
    Call<TokenModel> register(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password1") String password1,
            @Field("password2") String password2
    );

    @GET("api/schedule/")
    Call<List<VaccineModel>> getSchedule(
            @Header("Authorization") String key,
            @Query("pk") int pk
    );

    @GET("auth/api/user/")
    Call<UserModel> getUser(
            @Header("Authorization") String token
    );

    @GET("api/parent/")
    Call<List<ParentModel>> getParentDetails(
            @Header("Authorization") String token
    );

    @GET("api/babies/")
    Call<List<BabyModel>> getAllBabies(
            @Header("Authorization") String key
    );


    @POST("api/authdevices/")
    @FormUrlEncoded
    Call<ResponseBody> registerFCM(
            @Header("Authorization") String userToken,
            @Field("name") String name,
            @Field("registration_id") String registration_id,
            @Field("device_id") String id,
            @Field("active") boolean active,
            @Field("type") String type
    );

    @GET("api/notifications/")
    Call<List<NotificationModel>> getNotifs(
            @Header("Authorization") String userToken
    );

}
