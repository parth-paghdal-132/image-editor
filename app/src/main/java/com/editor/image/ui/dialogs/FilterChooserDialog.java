package com.editor.image.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.editor.image.R;
import com.editor.image.adapters.FiltersAdapter;
import com.editor.image.databinding.DialogFilterChooserBinding;
import com.editor.image.models.Filter;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FilterChooserDialog extends BottomSheetDialogFragment {

    private DialogFilterChooserBinding binding;
    private List<Filter> filters = new ArrayList<>();

    private OnFilterSelect onFilterSelect;

    public static String TAG = "FilterChooserDialog";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogFilterChooserBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        prepareList();
        fillData();
    }

    private void setListeners() {
        binding.btnCloseDialog.setOnClickListener(v -> dismiss());
    }

    private void prepareList() {
        filters.add(new Filter(R.drawable.original_horse, "Original", Filter.FilterName.ORIGINAL));
        filters.add(new Filter(R.drawable.old_horse, "Old", Filter.FilterName.OLD));
        filters.add(new Filter(R.drawable.brightness_horse, "Brightness", Filter.FilterName.BRIGHTNESS));
        filters.add(new Filter(R.drawable.contrast_horse, "Contrast", Filter.FilterName.CONTRAST));
        filters.add(new Filter(R.drawable.gaussian_horse, "Gaussian", Filter.FilterName.GAUSSIAN));
        filters.add(new Filter(R.drawable.grayscale_horse, "Grayscale", Filter.FilterName.GRAYSCALE));
        filters.add(new Filter(R.drawable.hue_horse, "Hue", Filter.FilterName.HUE));
        filters.add(new Filter(R.drawable.invert_horse, "Invert", Filter.FilterName.INVERT));
        filters.add(new Filter(R.drawable.saturation_horse, "Saturation", Filter.FilterName.SATURATION));
        filters.add(new Filter(R.drawable.sepia_horse, "Sepia", Filter.FilterName.SEPIA));
    }

    private void fillData() {
        FiltersAdapter filtersAdapter = new FiltersAdapter(filters);
        filtersAdapter.setOnItemClickListener(position -> {
            onFilterSelect.onSelect(filters.get(position));
        });
        binding.recyclerFilters.setAdapter(filtersAdapter);
    }

    public void setOnFilterSelect(OnFilterSelect onFilterSelect) {
        this.onFilterSelect = onFilterSelect;
    }

    public interface OnFilterSelect {
        public void onSelect(Filter filter);
    }
}
