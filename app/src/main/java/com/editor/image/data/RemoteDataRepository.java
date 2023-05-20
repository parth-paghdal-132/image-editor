package com.editor.image.data;

import com.editor.image.models.Image;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RemoteDataRepository {

    @GET("image")
    Call<List<Image>> getImages();
}
