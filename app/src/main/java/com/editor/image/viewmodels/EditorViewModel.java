package com.editor.image.viewmodels;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;

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
import com.editor.image.models.History;
import com.editor.image.models.Image;
import com.editor.image.utils.FiltersUtil;
import com.editor.image.views.EditorTextView;

import java.util.ArrayList;
import java.util.List;

public class EditorViewModel extends AndroidViewModel {

    private Bitmap initialBitmap;
    private Bitmap croppedBitmap;
    public MutableLiveData<Bitmap> currentBitmap = new MutableLiveData<>();

    private FiltersUtil filtersUtil = new FiltersUtil();

    private List<History> historyList = new ArrayList<History>();
    private int currentHistoryIndex = -1;
    public MutableLiveData<Boolean> isUndoEnabled = new MutableLiveData<>(false);
    public MutableLiveData<Boolean> isRedoEnabled = new MutableLiveData<>(false);

    public MutableLiveData<View> removeEditorTextView = new MutableLiveData<>();
    public MutableLiveData<View> addEditorTextView = new MutableLiveData<>();

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

    public void applyFilter(Filter filter, boolean addToHistory) {
        if(filter.getFilterName() == Filter.FilterName.ORIGINAL) {
            if(croppedBitmap != null) {
                currentBitmap.postValue(croppedBitmap);
            } else {
                currentBitmap.postValue(initialBitmap);
            }
            return;
        }
        if(addToHistory) {
            addToHistory(new History(History.Type.FILTER, filter, null, null, null, null));
        }
        if (croppedBitmap != null) {
            currentBitmap.postValue(filtersUtil.applyFilter(getApplication().getApplicationContext(), filter, croppedBitmap));
        } else {
            currentBitmap.postValue(filtersUtil.applyFilter(getApplication().getApplicationContext(), filter, initialBitmap));
        }
    }

    public void addToHistory(History history) {
        historyList.add(history);
        currentHistoryIndex = historyList.size() - 1;
        enableDisableUndoRedoButton();
    }

    private void enableDisableUndoRedoButton() {
        isUndoEnabled.postValue(currentHistoryIndex >= 0);
        isRedoEnabled.postValue(currentHistoryIndex < (historyList.size() - 1));
    }

    public void performUndoOp() {
        History currentItem = historyList.get(currentHistoryIndex);
        if (currentItem.getType() == History.Type.TEXT) {
            removeEditorTextView.postValue(currentItem.getEditorTextView());
            currentHistoryIndex = currentHistoryIndex - 1;
        } else if (currentItem.getType() == History.Type.FILTER) {
            applyFilter(new Filter(R.drawable.original_horse, "Original", Filter.FilterName.ORIGINAL), false);
            currentHistoryIndex = currentHistoryIndex - 1;
        } else if (currentItem.getType() == History.Type.CROP) {
            currentBitmap.postValue(currentItem.getPreviousBitmap());
            currentHistoryIndex = currentHistoryIndex - 1;
            croppedBitmap = null;
        } else if (currentItem.getType() == History.Type.BRUSH) {
            removeEditorTextView.postValue(currentItem.getDrawingView());
            currentHistoryIndex = currentHistoryIndex - 1;
        }
        enableDisableUndoRedoButton();
    }

    public void performRedoOp() {
        currentHistoryIndex = currentHistoryIndex + 1;
        History currentItem = historyList.get(currentHistoryIndex);
        if(currentItem.getType() == History.Type.TEXT) {
            addEditorTextView.postValue(currentItem.getEditorTextView());
        } else if (currentItem.getType() == History.Type.FILTER) {
            applyFilter(currentItem.getFilter(), false);
        } else if (currentItem.getType() == History.Type.CROP) {
            croppedBitmap = currentItem.getBitmap();
            showCroppedBitmap(currentItem.getBitmap(), false);
        } else if (currentItem.getType() == History.Type.BRUSH) {
            addEditorTextView.postValue(currentItem.getDrawingView());
        }
        enableDisableUndoRedoButton();
    }

    public void showCroppedBitmap(Bitmap bitmap, boolean addToHistory) {
        if(addToHistory) {
            addToHistory(new History(History.Type.CROP, null, null, bitmap, currentBitmap.getValue(), null));
            croppedBitmap = bitmap;
        }
        currentBitmap.postValue(bitmap);
    }

    public void disableUndoRedoButtons() {
        isUndoEnabled.postValue(false);
        isRedoEnabled.postValue(false);
    }

    public void enableUndoRedoButtons() {
        enableDisableUndoRedoButton();
    }
}
