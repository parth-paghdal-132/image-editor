package com.editor.image.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.editor.image.R;
import com.editor.image.models.Filter;
import com.editor.image.models.Image;

public class EditorViewModel extends ViewModel {

    public MutableLiveData<Bitmap> currentBitmap = new MutableLiveData<>();

    public EditorViewModel() {

    }

    public void loadInitialBitmap(Context context, Image image) {
        Glide.with(context)
                .asBitmap()
                .load(image.getImageUri())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.loading_img)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        currentBitmap.postValue(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void applyFilter(Filter filter) {

    }
}
