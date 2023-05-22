package com.editor.image.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.editor.image.adapters.EditorWallDetailPagerAdapter;
import com.editor.image.databinding.FragmentEditorWallDetailBinding;
import com.editor.image.models.editorwall.EditorWall;

import java.util.ArrayList;
import java.util.List;

public class EditorWallDetailFragment extends Fragment {
    private FragmentEditorWallDetailBinding binding;

    private EditorWall editorWall;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditorWallDetailBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editorWall = EditorWallDetailFragmentArgs.fromBundle(getArguments()).getImages();

        fillData();
    }

    private void fillData() {
        List<String> images = new ArrayList<>();
        images.add(editorWall.getOriginalImageUrl());
        images.add(editorWall.getEditedImageUrl());

        EditorWallDetailPagerAdapter editorWallDetailPagerAdapter = new EditorWallDetailPagerAdapter(this);
        editorWallDetailPagerAdapter.setDataSet(images);
        binding.viewpagerImages.setAdapter(editorWallDetailPagerAdapter);
        binding.dotsIndicator.attachTo(binding.viewpagerImages);
    }
}
