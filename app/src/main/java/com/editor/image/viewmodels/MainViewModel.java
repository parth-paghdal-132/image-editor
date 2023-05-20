package com.editor.image.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.editor.image.R;
import com.editor.image.data.RemoteDataRepository;
import com.editor.image.models.Image;
import com.editor.image.utils.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    RemoteDataRepository remoteDataRepository;

    MainViewModel(RemoteDataRepository remoteDataRepository) {
        this.remoteDataRepository = remoteDataRepository;
    }


    public MutableLiveData<Result<List<Image>>> apiData = new MutableLiveData<>();

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
}