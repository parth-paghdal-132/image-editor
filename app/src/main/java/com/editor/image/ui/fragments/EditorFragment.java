package com.editor.image.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.editor.image.ImageEditor;
import com.editor.image.databinding.FragmentEditorBinding;
import com.editor.image.models.Filter;
import com.editor.image.models.History;
import com.editor.image.models.Image;
import com.editor.image.models.Text;
import com.editor.image.ui.dialogs.CroppingDialog;
import com.editor.image.ui.dialogs.EnterTextDialog;
import com.editor.image.ui.dialogs.FilterChooserDialog;
import com.editor.image.viewmodels.EditorViewModel;
import com.editor.image.viewmodels.EditorViewModelFactory;
import com.editor.image.views.EditorTextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private FragmentEditorBinding binding;
    private Image imageToEdit = null;
    private EditorViewModel editorViewModel;

    private View focusedView;
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

        editorViewModel.isUndoEnabled.observe(getViewLifecycleOwner(), isEnabled -> {
            binding.btnUndo.setEnabled(isEnabled);
        });

        editorViewModel.isRedoEnabled.observe(getViewLifecycleOwner(), isEnabled -> {
            binding.btnRedu.setEnabled(isEnabled);
        });

        editorViewModel.removeEditorTextView.observe(getViewLifecycleOwner(), editorTextView -> {
            binding.relEditorView.removeView(editorTextView);
        });

        editorViewModel.addEditorTextView.observe(getViewLifecycleOwner(), editorTextView -> {
            binding.relEditorView.addView(editorTextView);
        });
    }

    private void setListeners() {
        binding.btnAddFilter.setOnClickListener(this);
        binding.btnAddText.setOnClickListener(this);
        binding.btnCrop.setOnClickListener(this);
        binding.btnDone.setOnClickListener(this);
        binding.btnUndo.setOnClickListener(this);
        binding.btnRedu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btnAddFilter) {
            showFilterChooseDialog();
        } else if (v == binding.btnAddText) {
            showTextDialog();
        } else if (v == binding.btnCrop) {
            showCroppingDialog();
        } else if (v == binding.btnBrush) {
            System.out.println("Brush op");
        } else if (v == binding.btnAddSticker) {
            System.out.println("Add sticker");
        } else if (v == binding.btnDone) {
            performDoneOp();
        } else if (v == binding.btnUndo) {
            performUndoOp();
        } else if (v == binding.btnRedu) {
            performRedoOp();
        }
    }

    private void showFilterChooseDialog() {
        FilterChooserDialog filterChooserDialog = new FilterChooserDialog();
        filterChooserDialog.setOnFilterSelect(filter -> {
            editorViewModel.applyFilter(filter, true);
        });
        filterChooserDialog.show(getChildFragmentManager(), FilterChooserDialog.TAG);
    }

    private void showTextDialog() {
        EnterTextDialog enterTextDialog = new EnterTextDialog();
        enterTextDialog.setOnTextSelectionListener(newText -> {
            EditorTextView editorTextView = new EditorTextView(getContext());
            editorTextView.setData(newText);
            binding.relEditorView.addView(editorTextView);
            editorTextView.setOnClickListener(v -> showTextDialog(editorTextView.getData(), editorTextView));
            if(focusedView != null) {
                performDoneOp();
            }
            focusedView = editorTextView;
            editorViewModel.addToHistory(new History(History.Type.TEXT, null, editorTextView));
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable(EnterTextDialog.CURRENT_TEXT, new Text("", Color.RED));
        enterTextDialog.setArguments(bundle);
        enterTextDialog.show(getChildFragmentManager(), EnterTextDialog.TAG);
    }

    private void showTextDialog(Text text, EditorTextView editorTextView) {
        EnterTextDialog enterTextDialog = new EnterTextDialog();
        enterTextDialog.setOnTextSelectionListener(text1 -> {
            if (focusedView != null) {
                performDoneOp();
            }
            focusedView = editorTextView;
            editorTextView.setData(text1);
            editorViewModel.addToHistory(new History(History.Type.TEXT, null, editorTextView));
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable(EnterTextDialog.CURRENT_TEXT, text);
        enterTextDialog.setArguments(bundle);
        enterTextDialog.show(getChildFragmentManager(), EnterTextDialog.TAG);
    }

    private void showCroppingDialog() {
        CroppingDialog croppingDialog = new CroppingDialog();
        Bundle bundle = new Bundle();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        editorViewModel.currentBitmap.getValue().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encodedBitmap = Base64.encodeToString(byteArray, Base64.DEFAULT);
        bundle.putString(CroppingDialog.IMAGE, encodedBitmap);
        croppingDialog.setArguments(bundle);
        croppingDialog.show(getChildFragmentManager(), CroppingDialog.TAG);
    }

    private void performDoneOp() {
        if(focusedView instanceof EditorTextView) {
            ((EditorTextView) focusedView).commitChanges(true);
        }
    }

    private void performUndoOp() {
        editorViewModel.performUndoOp();
    }

    private void performRedoOp() {
        editorViewModel.performRedoOp();
    }
}