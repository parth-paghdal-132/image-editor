package com.editor.image.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.editor.image.R;
import com.editor.image.databinding.FragmentEditorBinding;
import com.editor.image.models.Filter;
import com.editor.image.models.Image;
import com.editor.image.ui.dialogs.FilterChooserDialog;
import com.editor.image.viewmodels.EditorViewModel;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private FragmentEditorBinding binding;
    private Image imageToEdit = null;
    private Bitmap currentBitmap = null;

    private EditorViewModel editorViewModel;
    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {

        binding = FragmentEditorBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        editorViewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        imageToEdit = EditorFragmentArgs.fromBundle(getArguments()).getImage();

        setObservers();
        setListeners();

        editorViewModel.loadInitialBitmap(getContext(), imageToEdit);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void setObservers() {
        editorViewModel.currentBitmap.observe(getViewLifecycleOwner(), bitmap -> {
            binding.imgToEdit.setImageBitmap(bitmap);
        });
    }

    private void setListeners() {
        binding.btnAddFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btnAddFilter) {
            showFilterChooseDialog();
        } else if (v == binding.btnAddText) {
            System.out.println("Add Text");
        } else if (v == binding.btnCrop) {
            System.out.println("Crop op");
        } else if (v == binding.btnBrush) {
            System.out.println("Brush op");
        } else if (v == binding.btnAddEmoji) {
            System.out.println("Add Emoji");
        } else if (v == binding.btnAddSticker) {
            System.out.println("Add sticker");
        }
    }

    private void showFilterChooseDialog() {
        FilterChooserDialog filterChooserDialog = new FilterChooserDialog();
        filterChooserDialog.setOnFilterSelect(filter -> {
            editorViewModel.applyFilter(filter);
        });
        filterChooserDialog.show(getChildFragmentManager(), FilterChooserDialog.TAG);
    }
}