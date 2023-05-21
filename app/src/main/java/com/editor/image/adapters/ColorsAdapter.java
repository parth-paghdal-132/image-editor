package com.editor.image.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.editor.image.databinding.ItemColorBinding;
import com.editor.image.interfaces.OnItemClickListener;

import java.util.List;

public class ColorsAdapter extends RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder> {

    private OnItemClickListener onItemClickListener = null;
    private List<Integer> dataset;

    public ColorsAdapter(List<Integer> dataset) {
        this.dataset = dataset;
    }

    @NonNull
    @Override
    public ColorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemColorBinding binding = ItemColorBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new ColorsViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull ColorsViewHolder holder, int position) {
        holder.bind(dataset.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ColorsViewHolder extends RecyclerView.ViewHolder {

        private ItemColorBinding binding;
        public ColorsViewHolder(ItemColorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(int color) {
            binding.getRoot().setCardBackgroundColor(color);
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
