package com.editor.image.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.editor.image.ui.fragments.DetailImageFragment;

import java.util.ArrayList;
import java.util.List;

public class EditorWallDetailPagerAdapter extends FragmentStateAdapter {

    private List<String> dataSet = new ArrayList<>();

    public EditorWallDetailPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        DetailImageFragment detailImageFragment = new DetailImageFragment();
        Bundle args = new Bundle();
        args.putString(DetailImageFragment.IMAGE_URL, dataSet.get(position));
        detailImageFragment.setArguments(args);
        return detailImageFragment;
    }

    public void setDataSet(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
