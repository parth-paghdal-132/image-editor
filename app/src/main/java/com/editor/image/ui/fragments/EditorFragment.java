package com.editor.image.ui.fragments;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.editor.image.ImageEditor;
import com.editor.image.R;
import com.editor.image.databinding.FragmentEditorBinding;
import com.editor.image.models.Brush;
import com.editor.image.models.History;
import com.editor.image.models.Image;
import com.editor.image.models.Text;
import com.editor.image.ui.dialogs.BrushSettingDialog;
import com.editor.image.ui.dialogs.CroppingDialog;
import com.editor.image.ui.dialogs.EnterTextDialog;
import com.editor.image.ui.dialogs.FilterChooserDialog;
import com.editor.image.ui.dialogs.LoadingDialog;
import com.editor.image.viewmodels.EditorViewModel;
import com.editor.image.viewmodels.EditorViewModelFactory;
import com.editor.image.views.DrawingView;
import com.editor.image.views.EditorTextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

public class EditorFragment extends Fragment implements View.OnClickListener {

    private FragmentEditorBinding binding;
    private Image imageToEdit = null;
    private EditorViewModel editorViewModel;

    private View focusedView;
    LoadingDialog loadingDialog = new LoadingDialog();

    @Override
    public View onCreateView(
        LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        setHasOptionsMenu(true);
        binding = FragmentEditorBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.editor_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_upload) {
            showConfirmationDialog();
        }
        return super.onOptionsItemSelected(item);
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
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);

        editorViewModel.currentBitmap.observe(getViewLifecycleOwner(), bitmap -> {
            loadingDialog.dismissAllowingStateLoss();
            binding.imgToEdit.setImageBitmap(bitmap);
        });

        editorViewModel.isUndoEnabled.observe(getViewLifecycleOwner(), isEnabled -> {
            binding.btnUndo.setEnabled(isEnabled);
        });

        editorViewModel.isRedoEnabled.observe(getViewLifecycleOwner(), isEnabled -> {
            binding.btnRedu.setEnabled(isEnabled);
        });

        editorViewModel.removeEditorTextView.observe(getViewLifecycleOwner(), editorTextView -> {
            loadingDialog.dismissAllowingStateLoss();
            binding.relTextContainer.removeView(editorTextView);
        });

        editorViewModel.addEditorTextView.observe(getViewLifecycleOwner(), editorTextView -> {
            try {
                loadingDialog.dismissAllowingStateLoss();
                binding.relTextContainer.addView(editorTextView);
                focusedView = editorTextView;
            } catch (Exception e) {
                loadingDialog.dismissAllowingStateLoss();
                focusedView = editorTextView;
            }

        });
    }

    private void setListeners() {
        binding.btnAddFilter.setOnClickListener(this);
        binding.btnAddText.setOnClickListener(this);
        binding.btnCrop.setOnClickListener(this);
        binding.btnBrush.setOnClickListener(this);
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
            showBrushSelectionDialog();
        }  else if (v == binding.btnDone) {
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
            loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
            editorViewModel.applyFilter(filter, true);
        });
        filterChooserDialog.show(getChildFragmentManager(), FilterChooserDialog.TAG);
    }

    private void showTextDialog() {
        EnterTextDialog enterTextDialog = new EnterTextDialog();
        enterTextDialog.setOnTextSelectionListener(newText -> {
            EditorTextView editorTextView = new EditorTextView(getContext());
            editorTextView.setData(newText);
            binding.relTextContainer.addView(editorTextView);
            editorTextView.setOnClickListener(v -> showTextDialog(editorTextView.getData(), editorTextView));
            if(focusedView != null) {
                performDoneOp();
            }
            focusedView = editorTextView;
            editorViewModel.addToHistory(new History(History.Type.TEXT, null, editorTextView, null, null, null));
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
            editorViewModel.addToHistory(new History(History.Type.TEXT, null, editorTextView, null, null, null));
        });
        Bundle bundle = new Bundle();
        bundle.putSerializable(EnterTextDialog.CURRENT_TEXT, text);
        enterTextDialog.setArguments(bundle);
        enterTextDialog.show(getChildFragmentManager(), EnterTextDialog.TAG);
    }

    private void showCroppingDialog() {
        CroppingDialog croppingDialog = new CroppingDialog();
        croppingDialog.setCurrentBitmap(editorViewModel.currentBitmap.getValue());
        croppingDialog.setOnCroppingListener(bitmap -> editorViewModel.showCroppedBitmap(bitmap, true));
        croppingDialog.show(getChildFragmentManager(), CroppingDialog.TAG);
    }

    private void showBrushSelectionDialog() {
        BrushSettingDialog brushSettingDialog = new BrushSettingDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(BrushSettingDialog.CURRENT_BRUSH, new Brush(10, Color.RED));
        brushSettingDialog.setArguments(bundle);
        brushSettingDialog.setOnBrushSelectionListener(new BrushSettingDialog.OnBrushSelection() {
            @Override
            public void onBrushSelect(Brush brush) {
                addBrushView(brush);
            }
        });
        brushSettingDialog.show(getChildFragmentManager(), BrushSettingDialog.TAG);
    }

    private void performDoneOp() {
        if(focusedView instanceof EditorTextView) {
            ((EditorTextView) focusedView).commitChanges(true);
        }
        if (focusedView instanceof DrawingView) {
            ((DrawingView) focusedView).commitChanges(true);
            editorViewModel.addToHistory(new History(History.Type.BRUSH, null, null, null, null, (DrawingView) focusedView));
        }
    }

    private void performUndoOp() {
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
        editorViewModel.performUndoOp();
    }

    private void performRedoOp() {
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
        editorViewModel.performRedoOp();
    }

    private void addBrushView(Brush brush) {
        DrawingView drawingView = new DrawingView(getContext());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            binding.imgToEdit.getMeasuredWidth(),
            binding.imgToEdit.getMeasuredHeight()
        );
        binding.relTextContainer.addView(drawingView, params);
        drawingView.setBrush(brush);
        focusedView = drawingView;
    }

    private void showConfirmationDialog() {
        new MaterialAlertDialogBuilder(getContext())
                .setTitle(R.string.upload_confirmation_title)
                .setMessage(R.string.upload_confirmation_message)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takeScreenshotAndGoNext();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void takeScreenshotAndGoNext() {
        binding.relEditorView.setDrawingCacheEnabled(true);
        binding.relEditorView.buildDrawingCache();
        Bitmap bitmap = binding.relEditorView.getDrawingCache();

        saveBitmapToLocalStorage(bitmap);
    }

    private void saveBitmapToLocalStorage(Bitmap bitmap) {
        String fileName = UUID.randomUUID().toString()+".jpeg";
        File file = new File(getContext().getFilesDir(), fileName);

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            gotoNextScreen(file.getPath());
        } catch (Exception exception) {
            Snackbar.make(binding.getRoot(), "Can not perform operation at this time.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void gotoNextScreen(String filePath) {
        EditorFragmentDirections.ToUploadFragment toUploadFragment = EditorFragmentDirections.toUploadFragment();
        toUploadFragment.setFilePath(filePath);
        toUploadFragment.setOriginalImage(imageToEdit.getUrl());
        NavHostFragment.findNavController(EditorFragment.this).navigate(toUploadFragment);
    }
}