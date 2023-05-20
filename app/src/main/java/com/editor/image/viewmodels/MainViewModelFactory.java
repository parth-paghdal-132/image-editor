package com.editor.image.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.editor.image.data.RemoteDataRepository;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private final RemoteDataRepository remoteDataRepository;

    public MainViewModelFactory(RemoteDataRepository remoteDataRepository) {
        this.remoteDataRepository = remoteDataRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(remoteDataRepository);
    }
}
