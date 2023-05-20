package com.editor.image.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.editor.image.databinding.ItemFiltersBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.Filter;

import java.util.ArrayList;
import java.util.List;

public class FiltersAdapter extends RecyclerView.Adapter<FiltersAdapter.FiltersViewHolder> {

    private List<Filter> dataSet = new ArrayList<>();
    private OnItemClickListener onItemClickListener = null;

    public FiltersAdapter(List<Filter> dataSet) {
        this.dataSet = dataSet;
    }

    @NonNull
    @Override
    public FiltersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFiltersBinding binding = ItemFiltersBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new FiltersViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FiltersViewHolder holder, int position) {
        holder.bind(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class FiltersViewHolder extends RecyclerView.ViewHolder {

        private ItemFiltersBinding binding;
        public FiltersViewHolder(ItemFiltersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Filter filter) {
            binding.cardFilters.setOnClickListener(v -> {
                onItemClickListener.onItemClick(getAdapterPosition());
            });
            binding.imgFilter.setImageResource(filter.getImageId());
            binding.txtFilterName.setText(filter.getFilterTitle());
        }
    }
}
