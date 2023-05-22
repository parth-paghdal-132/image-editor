package com.editor.image.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.editor.image.R;
import com.editor.image.data.RemoteDataRepository;
import com.editor.image.models.Image;
import com.editor.image.models.UploadLink;
import com.editor.image.models.UploadResult;
import com.editor.image.models.editorwall.EditorWall;
import com.editor.image.utils.Constants;
import com.editor.image.utils.Result;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    RemoteDataRepository remoteDataRepository;

    MainViewModel(RemoteDataRepository remoteDataRepository) {
        this.remoteDataRepository = remoteDataRepository;
    }

    public MutableLiveData<Result<List<Image>>> apiData = new MutableLiveData<>();
    public MutableLiveData<Result<UploadLink>> uploadLinkLiveData = new MutableLiveData<>();
    public MutableLiveData<Result<UploadResult>> uploadResultLiveData = new MutableLiveData<>();
    public MutableLiveData<Result<List<EditorWall>>> editorWallLiveData = new MutableLiveData<>();

    public void fetchData() {
        apiData.postValue(new Result.Loading<>());
        remoteDataRepository.getImages().enqueue(new Callback<List<Image>>() {
            @Override
            public void onResponse(Call<List<Image>> call, Response<List<Image>> response) {
                List<Image> images = response.body();
                apiData.postValue(new Result.Success<>(images));
            }

            @Override
            public void onFailure(Call<List<Image>> call, Throwable t) {
                apiData.postValue(new Result.Error<>(t.getMessage(), null));
            }
        });
    }

    public void fetchUploadLink() {
        uploadLinkLiveData.postValue(new Result.Loading<>());
        remoteDataRepository.getUploadUrl().enqueue(new Callback<UploadLink>() {
            @Override
            public void onResponse(Call<UploadLink> call, Response<UploadLink> response) {
                if(response.isSuccessful()) {
                    UploadLink uploadLink = response.body();
                    uploadLinkLiveData.postValue(new Result.Success<>(uploadLink));
                } else {
                    uploadLinkLiveData.postValue(new Result.Error<>("Can not upload at this moment.", null));
                }
            }

            @Override
            public void onFailure(Call<UploadLink> call, Throwable t) {
                uploadLinkLiveData.postValue(new Result.Error<>("Can not upload at this moment.", null));
            }
        });
    }

    public void uploadImage(String filePath, String originalUrl, String uploadPath) {
        uploadResultLiveData.postValue(new Result.Loading<>());

        MultipartBody.Part appId = MultipartBody.Part.createFormData("appid", Constants.APP_ID);
        MultipartBody.Part original = MultipartBody.Part.createFormData("original", originalUrl);
        File imageFile = new File(filePath);
        MultipartBody.Part file = MultipartBody.Part.createFormData(
                "file",
                imageFile.getAbsoluteFile().getName(),
                RequestBody.create(
                        MediaType.parse("application/octet-stream"),
                        imageFile
                )
        );

        remoteDataRepository.uploadImage(uploadPath, appId, original, file).enqueue(new Callback<UploadResult>() {
            @Override
            public void onResponse(Call<UploadResult> call, Response<UploadResult> response) {
                if(response.isSuccessful()) {
                    UploadResult uploadResult = response.body();
                    uploadResultLiveData.postValue(new Result.Success<>(uploadResult));
                } else {
                    uploadResultLiveData.postValue(new Result.Error<>("Can not upload at this moment.", null));
                }
            }

            @Override
            public void onFailure(Call<UploadResult> call, Throwable t) {
                uploadResultLiveData.postValue(new Result.Error<>("Can not upload at this moment.", null));
            }
        });
    }

    public void fetchEditorWallData() {
        editorWallLiveData.postValue(new Result.Loading<>());
        remoteDataRepository.getAllEdits(Constants.APP_ID).enqueue(new Callback<List<EditorWall>>() {
            @Override
            public void onResponse(Call<List<EditorWall>> call, Response<List<EditorWall>> response) {
                if(response.isSuccessful()) {
                    List<EditorWall> editorWalls = response.body();
                    editorWallLiveData.postValue(new Result.Success<>(editorWalls));
                } else {
                    editorWallLiveData.postValue(new Result.Error<>("Can not load all edited images at this moment.", null));
                }
            }

            @Override
            public void onFailure(Call<List<EditorWall>> call, Throwable t) {
                editorWallLiveData.postValue(new Result.Error<>("Can not load all edited images at this moment.", null));
            }
        });
    }
}