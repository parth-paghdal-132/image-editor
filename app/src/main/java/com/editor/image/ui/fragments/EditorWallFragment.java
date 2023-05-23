package com.editor.image.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.editor.image.R;
import com.editor.image.adapters.EditorWallAdapter;
import com.editor.image.data.RemoteDataRepository;
import com.editor.image.data.RetrofitInstance;
import com.editor.image.databinding.FragmentEditorWallBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.EditorWallSimplified;
import com.editor.image.models.editorwall.EditorWall;
import com.editor.image.ui.dialogs.LoadingDialog;
import com.editor.image.utils.DateUtils;
import com.editor.image.utils.Result;
import com.editor.image.viewmodels.MainViewModel;
import com.editor.image.viewmodels.MainViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EditorWallFragment extends Fragment {

    private FragmentEditorWallBinding binding;
    private MainViewModel mainViewModel;
    private List<EditorWall> editorWalls;
    private List<EditorWallSimplified> editorWallSimplifieds = new ArrayList<>();
    private LoadingDialog loadingDialog = new LoadingDialog();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentEditorWallBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RemoteDataRepository remoteDataRepository = RetrofitInstance.getRemoteDataRepository();

        ViewModelProvider.Factory factory = new MainViewModelFactory(remoteDataRepository);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        attachObservers();
        mainViewModel.fetchEditorWallData();
    }

    private void attachObservers() {
        mainViewModel.editorWallLiveData.observe(getViewLifecycleOwner(), new Observer<Result<List<EditorWall>>>() {
            @Override
            public void onChanged(Result<List<EditorWall>> listResult) {
                if (listResult instanceof Result.Loading) {
                    showLoading();
                }

                if (listResult instanceof Result.Success) {
                    hideLoading();
                    editorWalls = listResult.getData();
                    prepareList();
                }

                if (listResult instanceof Result.Error) {
                    hideLoading();
                    showSnackbar();
                }
            }
        });
    }

    private void showLoading() {
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
    }

    private void hideLoading() {
        loadingDialog.dismissAllowingStateLoss();
    }

    private void prepareList() {
        for (int i = 0; i < editorWalls.size(); i++) {
            EditorWall editorWall = editorWalls.get(i);
            editorWallSimplifieds.add(
                new EditorWallSimplified(
                        editorWall.getOriginalImageUrl(),
                        editorWall.getEditedImageUrl(),
                        DateUtils.getDate(editorWall.getUpdated())
                )
            );
        }

        editorWallSimplifieds.sort((o1, o2) -> {
            Date date1 = o1.getLastUpdatedOn();
            Date date2 = o2.getLastUpdatedOn();
            return date2.compareTo(date1);
        });
        fillDataToRecyclerView();
    }

    private void fillDataToRecyclerView() {
        EditorWallAdapter editorWallAdapter = new EditorWallAdapter(editorWallSimplifieds);
        editorWallAdapter.setOnItemClickListener(position -> showExpandedImage(editorWallSimplifieds.get(position)));
        binding.recyclerEditorWall.setAdapter(editorWallAdapter);
    }

    private void showSnackbar() {
        Snackbar.make(binding.getRoot(), R.string.image_loading_failed, Snackbar.LENGTH_SHORT).show();
    }

    private void showExpandedImage(EditorWallSimplified editorWallSimplified) {
        EditorWallFragmentDirections.ToEditorWallDetailFragment toEditorWallDetailFragment = EditorWallFragmentDirections.toEditorWallDetailFragment();
        toEditorWallDetailFragment.setEditorWallSimplified(editorWallSimplified);
        NavHostFragment.findNavController(EditorWallFragment.this).navigate(toEditorWallDetailFragment);
    }
}
