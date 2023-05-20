package com.editor.image.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.editor.image.R;
import com.editor.image.adapters.ImagesAdapter;
import com.editor.image.data.RemoteDataRepository;
import com.editor.image.databinding.FragmentFirstBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.Image;
import com.editor.image.ui.activities.RetrofitInstance;
import com.editor.image.utils.Result;
import com.editor.image.viewmodels.MainViewModel;
import com.editor.image.viewmodels.MainViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private FragmentFirstBinding binding;

    private MainViewModel mainViewModel;

    private List<Image> images = new ArrayList<>();
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RemoteDataRepository remoteDataRepository = RetrofitInstance.getRemoteDataRepository();

        ViewModelProvider.Factory factory = new MainViewModelFactory(remoteDataRepository);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        attachObservers();
        mainViewModel.fetchData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void attachObservers() {
        mainViewModel.apiData.observe(getViewLifecycleOwner(), listResult -> {
            if (listResult instanceof Result.Loading) {
                showLoading();
            }

            if (listResult instanceof Result.Success) {
                hideLoading();
                images = listResult.getData();
                fillDataToRecyclerView();
            }

            if (listResult instanceof Result.Error) {
                hideLoading();
                showSnackbar();
            }
        });
    }

    private void showLoading() {
        binding.progressLoading.setVisibility(View.VISIBLE);
        binding.txtLoadingMessage.setVisibility(View.VISIBLE);
    }

    private void hideLoading() {
        binding.progressLoading.setVisibility(View.GONE);
        binding.txtLoadingMessage.setVisibility(View.GONE);
    }

    private void fillDataToRecyclerView() {
        ImagesAdapter imagesAdapter = new ImagesAdapter(images);
        imagesAdapter.setOnItemClickListener(position -> {
           navigateToEditorFragment(images.get(position));
        });
        binding.recyclerImages.setAdapter(imagesAdapter);
    }

    private void showSnackbar() {
        Snackbar.make(binding.getRoot(), R.string.image_loading_failed, Snackbar.LENGTH_SHORT).show();
    }

    private void navigateToEditorFragment(Image image) {
        MainFragmentDirections.ToEditorFragment toEditorFragment = MainFragmentDirections.toEditorFragment();
        toEditorFragment.setImage(image);
        NavHostFragment.findNavController(MainFragment.this)
                .navigate(toEditorFragment);
    }
}