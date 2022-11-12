package com.example.hinadadebank.api;

import com.example.hinadadebank.model.CheckResponse;
import com.example.hinadadebank.model.MutasiResponse;
import com.example.hinadadebank.model.TransResponse;
import com.example.hinadadebank.model.UploadResponse;
import com.example.hinadadebank.model.UserData;
import com.example.hinadadebank.model.UserResponse;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface Interface {
    @FormUrlEncoded
    @POST("login")
    Call<UserResponse> postLogin(@Field("email") String email,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<UserResponse> postRegister(@Field("name") String name,@Field("nik") String nik,
                                    @Field("tel") String tel,@Field("card") String card,
                                    @Field("email") String email,@Field("username") String username,
                                    @Field("password") String password,@Field("status") String status);

    @Multipart
    // POST request to upload an image from storage
    @POST("verification")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part value,@Part("users_id") RequestBody users_id, @Part("desc") RequestBody desc);

    @FormUrlEncoded
    @POST("check_verif")
    Call<CheckResponse> checkResponse(@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST("check_saldo")
    Call<TransResponse> checkSaldo(@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST("mutasi")
    Call<MutasiResponse> mutasi(@Field("users_id") String users_id,
                                @Field("type") String type, @Field("from") String from,
                                @Field("to") String to);

    @FormUrlEncoded
    @POST("check_card")
    Call<UserResponse> checkCard(@Field("card") String card);

    @FormUrlEncoded
    @POST("transaction")
    Call<TransResponse> postTrans(@Field("users_id") String users_id,
                                 @Field("debit") String debit,
                                 @Field("credit") String credit,
                                 @Field("saldo") String saldo,
                                 @Field("from") String from,
                                 @Field("dest") String dest,
                                 @Field("desc") String desc);
    @FormUrlEncoded
    @POST("users")
    Call<UserResponse> getUsers(@Field("users_id") String users_id);

    @FormUrlEncoded
    @POST("edit_users")
    Call<UserResponse> postUsers(@Field("users_id") String users_id,
                                  @Field("name") String name,
                                  @Field("nik") String nik,
                                  @Field("tel") String tel,
                                  @Field("email") String email,
                                  @Field("username") String username,
                                  @Field("password") String password);

    @POST("delete_users")
    Call<UserResponse> deleteUsers(@Field("users_id") String users_id);
}
