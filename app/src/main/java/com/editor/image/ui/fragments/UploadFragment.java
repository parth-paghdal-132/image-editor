package com.editor.image.ui.fragments;

import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.editor.image.R;
import com.editor.image.data.RemoteDataRepository;
import com.editor.image.data.RetrofitInstance;
import com.editor.image.databinding.FragmentUploadBinding;
import com.editor.image.models.UploadLink;
import com.editor.image.models.UploadResult;
import com.editor.image.ui.dialogs.LoadingDialog;
import com.editor.image.utils.Result;
import com.editor.image.viewmodels.MainViewModel;
import com.editor.image.viewmodels.MainViewModelFactory;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class UploadFragment extends Fragment {

    private FragmentUploadBinding binding;
    private String filePath;
    private String originalUrl;
    private MainViewModel mainViewModel;
    private LoadingDialog loadingDialog = new LoadingDialog();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentUploadBinding.inflate(inflater, container, false);
        return binding.getRoot() ;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        filePath = UploadFragmentArgs.fromBundle(getArguments()).getFilePath();
        originalUrl = UploadFragmentArgs.fromBundle(getArguments()).getOriginalImage();

        RemoteDataRepository remoteDataRepository = RetrofitInstance.getRemoteDataRepository();
        ViewModelProvider.Factory factory = new MainViewModelFactory(remoteDataRepository);
        mainViewModel = new ViewModelProvider(this, factory).get(MainViewModel.class);

        Glide.with(getContext())
            .load(new File(filePath))
            .into(binding.imgFinal);
        attachObservers();
        setListeners();
    }

    private void attachObservers() {
        mainViewModel.uploadLinkLiveData.observe(getViewLifecycleOwner(), new Observer<Result<UploadLink>>() {
            @Override
            public void onChanged(Result<UploadLink> uploadLinkResult) {
                if (uploadLinkResult instanceof Result.Loading) {
                    showLoading();
                }

                if (uploadLinkResult instanceof Result.Success) {
                    hideLoading();
                    String uploadUrl = uploadLinkResult.getData().getUrl();
                    Uri uploadUri = Uri.parse(uploadUrl);
                    String path = uploadUri.getPath().substring(1);
                    mainViewModel.uploadImage(filePath, originalUrl, path);
                }

                if (uploadLinkResult instanceof Result.Error) {
                    hideLoading();
                    showSnackbar();
                }
            }
        });

        mainViewModel.uploadResultLiveData.observe(getViewLifecycleOwner(), uploadResultResult -> {
            if (uploadResultResult instanceof Result.Loading) {
                showLoading();
            }

            if (uploadResultResult instanceof Result.Success) {
                hideLoading();
                UploadResult uploadResult = uploadResultResult.getData();
                if(uploadResult.getStatus().equals("success")) {
                    navigateToEditedImages();
                }
            }

            if (uploadResultResult instanceof Result.Error) {
                hideLoading();
                showSnackbar();
            }
        });
    }

    private void setListeners() {
        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startImageUploadOp();
            }
        });
    }

    private void startImageUploadOp() {
        mainViewModel.fetchUploadLink();
    }

    private void showLoading() {
        loadingDialog.show(getChildFragmentManager(), LoadingDialog.TAG);
    }

    private void hideLoading() {
        loadingDialog.dismissAllowingStateLoss();
    }

    private void showSnackbar() {
        Snackbar.make(binding.getRoot(), R.string.image_loading_failed, Snackbar.LENGTH_SHORT).show();
    }

    private void navigateToEditedImages() {
        NavHostFragment.findNavController(UploadFragment.this)
                .navigate(UploadFragmentDirections.toEditorWallFragment());
    }
}
