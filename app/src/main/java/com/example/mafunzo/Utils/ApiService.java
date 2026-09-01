package com.example.mafunzo.Utils;

import com.example.mafunzo.Model.AuthResponse;
import com.example.mafunzo.Model.LoginRequest;
import com.example.mafunzo.Model.RegisterRequest;
import com.example.mafunzo.Model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("api/auth/register")
    Call<UserResponse> register(
            @Body RegisterRequest request
    );

    @POST("api/auth/login")
    Call<AuthResponse> login(
            @Body LoginRequest request
    );
}