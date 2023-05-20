package com.editor.image.ui.activities;

import com.editor.image.data.RemoteDataRepository;
import com.editor.image.utils.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    public static RemoteDataRepository getRemoteDataRepository() {
        Retrofit retrofit;

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder().build();

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
                .create(RemoteDataRepository.class);
    }
}
