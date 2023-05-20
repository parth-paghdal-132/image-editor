package com.editor.image.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.editor.image.ImageEditor;
import com.editor.image.R;
import com.editor.image.models.Filter;
import com.editor.image.models.Image;
import com.editor.image.utils.FiltersUtil;

public class EditorViewModel extends AndroidViewModel {

    private Bitmap initialBitmap;
    public MutableLiveData<Bitmap> currentBitmap = new MutableLiveData<>();

    private FiltersUtil filtersUtil = new FiltersUtil();

    public EditorViewModel(ImageEditor imageEditor) {
        super(imageEditor);

    }

    public void loadInitialBitmap(Image image) {
        Glide.with(getApplication().getApplicationContext())
                .asBitmap()
                .load(image.getImageUri())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.loading_img)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        initialBitmap = resource;
                        currentBitmap.postValue(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void applyFilter(Filter filter) {
        if(filter.getFilterName() == Filter.FilterName.ORIGINAL) {
            currentBitmap.postValue(initialBitmap);
            return;
        }
        currentBitmap.postValue(filtersUtil.applyFilter(getApplication().getApplicationContext(), filter, initialBitmap));
    }
}
