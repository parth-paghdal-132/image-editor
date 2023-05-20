package com.editor.image.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.editor.image.ImageEditor;

public class EditorViewModelFactory implements ViewModelProvider.Factory {

    private final ImageEditor imageEditor;

    public EditorViewModelFactory(ImageEditor imageEditor) {
        this.imageEditor = imageEditor;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new EditorViewModel(imageEditor);
    }
}
