package com.example.hinadadebank.api;

import com.example.hinadadebank.model.UserData;
import com.example.hinadadebank.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

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
}
