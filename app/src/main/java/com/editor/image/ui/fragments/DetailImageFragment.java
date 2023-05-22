package com.editor.image.ui.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.editor.image.R;
import com.editor.image.databinding.FragmentDetailImageBinding;

public class DetailImageFragment extends Fragment {

    private FragmentDetailImageBinding binding;

    public static String IMAGE_URL = "image_url";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDetailImageBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String image = args.getString(IMAGE_URL);

        Glide.with(getContext())
                .load(Uri.parse(image))
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.loading_img)
                .into(binding.imgDetail);
    }
}
