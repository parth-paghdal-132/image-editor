package com.editor.image.ui.dialogs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.editor.image.adapters.ColorsAdapter;
import com.editor.image.databinding.DialogBrushSettingBinding;
import com.editor.image.models.Brush;
import com.editor.image.models.Text;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class BrushSettingDialog extends BottomSheetDialogFragment {

    public static String TAG = "BrushSettingDialog";
    public static String CURRENT_BRUSH = "current_brush";
    private DialogBrushSettingBinding binding;
    private OnBrushSelection onBrushSelection;
    private List<Integer> colors = new ArrayList<>();
    private Brush currentBrush = new Brush(10, Color.RED);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogBrushSettingBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getSerializable(CURRENT_BRUSH) != null) {
            currentBrush = (Brush) getArguments().getSerializable(CURRENT_BRUSH);
        }

        setListeners();
        prepareColors();
        fillData();
    }

    private void setListeners() {
        binding.btnCloseDialog.setOnClickListener(v -> dismiss());
        binding.btnDone.setOnClickListener(v -> {
            currentBrush.setRadius(binding.seekbarBrushRadius.getProgress());
            onBrushSelection.onBrushSelect(currentBrush);
            dismiss();
        });
    }

    private void prepareColors() {
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.WHITE);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);
        colors.add(Color.BLACK);
        colors.add(Color.DKGRAY);
        colors.add(Color.GRAY);
        colors.add(Color.LTGRAY);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
    }

    private void fillData() {
        ColorsAdapter colorsAdapter = new ColorsAdapter(colors);
        colorsAdapter.setOnItemClickListener(position -> {
            colorsAdapter.setSelectedColorIndex(position);
            colorsAdapter.notifyDataSetChanged();
            currentBrush.setColor(colors.get(position));
        });
        int selectedColorIndex = 0;
        for (int i = 0; i < colors.size(); i++) {
            if(colors.get(i) == currentBrush.getColor()) {
                selectedColorIndex = i;
                break;
            }
        }
        colorsAdapter.setSelectedColorIndex(selectedColorIndex);
        binding.recyclerColor.setAdapter(colorsAdapter);
        binding.seekbarBrushRadius.setProgress(currentBrush.getRadius());
    }

    public void setOnBrushSelectionListener(OnBrushSelection onBrushSelection) {
        this.onBrushSelection = onBrushSelection;
    }

    public interface OnBrushSelection {
        public void onBrushSelect(Brush brush);
    }
}
