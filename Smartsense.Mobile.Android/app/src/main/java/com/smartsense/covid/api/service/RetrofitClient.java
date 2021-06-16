package com.smartsense.covid.api.service;

import android.content.Context;

import com.smartsense.covid.PrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String AUTH = "Bearer ";
    //private static final String BASE_URL = "http://iepilepsi.com:8080/";
    private static final String BASE_URL = "http://api.smartsense.com.tr";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;
    private PrefManager prefManager;


    private RetrofitClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request original = chain.request();

                                Request.Builder requestBuilder = original.newBuilder()
                                        .addHeader("Authorization", AUTH)
                                        .method(original.method(), original.body());

                                Request request = requestBuilder.build();
                                return chain.proceed(request);
                            }
                        }
                ).build();



        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (mInstance == null) {
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public Api getApi(Context context) {
        prefManager = new PrefManager(context);
        AUTH = "Bearer "+prefManager.getAuthToken();
        return retrofit.create(Api.class);
    }
}
