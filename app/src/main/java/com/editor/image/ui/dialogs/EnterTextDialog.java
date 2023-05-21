package com.editor.image.ui.dialogs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.editor.image.adapters.ColorsAdapter;
import com.editor.image.databinding.DialogEnterTextBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.Text;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class EnterTextDialog extends BottomSheetDialogFragment {

    public static String TAG = "EnterTextDialog";
    public static String CURRENT_TEXT = "current_text";

    public Text currentText = new Text("", Color.RED);

    private List<Integer> colors = new ArrayList<>();
    private DialogEnterTextBinding binding;

    private OnTextSelection onTextSelection;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogEnterTextBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments().getSerializable(CURRENT_TEXT) != null) {
            currentText = (Text) getArguments().getSerializable(CURRENT_TEXT);
        }

        setListeners();
        prepareColors();
        fillData();
    }

    private void setListeners() {
        binding.btnCloseDialog.setOnClickListener(v -> dismiss());
        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentText.setText(binding.txtEnterText.getEditText().getText().toString());
                onTextSelection.onTextSelect(currentText);
                dismiss();
            }
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
            currentText.setColor(colors.get(position));
        });
        binding.recyclerColor.setAdapter(colorsAdapter);
        binding.txtEnterText.getEditText().setText(currentText.getText());
    }

    public void setOnTextSelectionListener(OnTextSelection onTextSelection) {
        this.onTextSelection = onTextSelection;
    }

    public interface OnTextSelection {
        void onTextSelect(Text text);
    }
}
