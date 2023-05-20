package com.editor.image.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.editor.image.R;
import com.editor.image.databinding.ItemImagesBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.Image;

import java.util.ArrayList;
import java.util.List;

public class ImagesAdapter extends RecyclerView.Adapter<ImagesAdapter.ImagesViewHolder> {

    private List<Image> dataset = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public ImagesAdapter(List<Image> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemImagesBinding binding = ItemImagesBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ImagesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImagesViewHolder holder, int position) {
        holder.bind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void setDataset(List<Image> dataset) {
        this.dataset = dataset;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ImagesViewHolder extends RecyclerView.ViewHolder {
        ItemImagesBinding binding = null;
        public ImagesViewHolder(ItemImagesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Image image) {
            binding.getRoot().setOnClickListener(v -> {
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
            Glide
                .with(binding.getRoot().getContext())
                .load(image.getImageUri())
                .placeholder(R.drawable.loading_img)
                .error(R.drawable.loading_img)
                .into(binding.imgImage);
        }
    }
}
