package com.editor.image.data;

import com.editor.image.models.Image;
import com.editor.image.models.UploadLink;
import com.editor.image.models.UploadResult;
import com.editor.image.models.editorwall.EditorWall;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RemoteDataRepository {

    @GET("image")
    Call<List<Image>> getImages();

    @GET("/upload")
    Call<UploadLink> getUploadUrl();

    @Multipart
    @POST("{uploadPath}")
    Call<UploadResult> uploadImage(
            @Path(value = "uploadPath", encoded = true) String uploadPath,
            @Part MultipartBody.Part appId,
            @Part MultipartBody.Part original,
            @Part MultipartBody.Part file
    );

    @GET("app")
    Call<List<EditorWall>> getAllEdits(@Query("appid") String appid);
}
