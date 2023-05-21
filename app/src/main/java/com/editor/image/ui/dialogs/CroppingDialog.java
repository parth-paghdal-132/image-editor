package com.editor.image.ui.dialogs;

import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.editor.image.databinding.DialogCroppingBinding;

public class CroppingDialog extends DialogFragment {

    private DialogCroppingBinding binding;

    private Bitmap image;
    public static String IMAGE = "image";
    public static String TAG = "CroppingDialog";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogCroppingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getImageFromArguments();
        fillData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        Rect rect = new Rect(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        float percentWidth = rect.width() * 1F;
        getDialog().getWindow().setLayout((int) percentWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void getImageFromArguments() {
        String base64 = getArguments().getString(IMAGE);
        byte[] base64Image = Base64.decode(base64, Base64.DEFAULT);
        image = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.length);
    }

    private void fillData() {
        binding.cropImageView.setImageBitmap(image);
    }

    public interface OnCropping {
        void onCroppingDone(Bitmap bitmap);
    }
}
