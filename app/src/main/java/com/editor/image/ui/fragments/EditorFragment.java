package com.editor.image.ui.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.editor.image.ImageEditor;
import com.editor.image.databinding.FragmentEditorBinding;
import com.editor.image.models.Filter;
import com.editor.image.models.Image;
import com.editor.image.ui.dialogs.FilterChooserDialog;
import com.editor.image.viewmodels.EditorViewModel;
import com.editor.image.viewmodels.EditorViewModelFactory;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private FragmentEditorBinding binding;
    private Image imageToEdit = null;
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

        ViewModelProvider.Factory factory = new EditorViewModelFactory((ImageEditor) getActivity().getApplication());
        editorViewModel = new ViewModelProvider(this, factory).get(EditorViewModel.class);

        imageToEdit = EditorFragmentArgs.fromBundle(getArguments()).getImage();

        setObservers();
        setListeners();

        editorViewModel.loadInitialBitmap(imageToEdit);
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