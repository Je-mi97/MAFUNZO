package com.example.mafunzo.Utils;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL =
            "http://172.20.10.9:8080/";

    private static Retrofit retrofit;

    private RetrofitClient() {
        // Empêche l'instanciation
    }

    public static Retrofit getClient() {

        if (retrofit == null) {

            HttpLoggingInterceptor loggingInterceptor =
                    new HttpLoggingInterceptor();

            loggingInterceptor.setLevel(
                    HttpLoggingInterceptor.Level.BODY
            );

            OkHttpClient client =
                    new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)
                            .readTimeout(30, TimeUnit.SECONDS)
                            .writeTimeout(30, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)
                            .build();

            retrofit =
                    new Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .client(client)
                            .addConverterFactory(
                                    GsonConverterFactory.create()
                            )
                            .build();
        }

        return retrofit;
    }
}