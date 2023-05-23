package com.editor.image.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.editor.image.R;
import com.editor.image.databinding.ItemEditorWallBinding;
import com.editor.image.databinding.ItemFiltersBinding;
import com.editor.image.interfaces.OnItemClickListener;
import com.editor.image.models.EditorWallSimplified;
import com.editor.image.models.editorwall.EditorWall;
import com.editor.image.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class EditorWallAdapter extends RecyclerView.Adapter<EditorWallAdapter.EditorWallViewHolder> {
    private OnItemClickListener onItemClickListener = null;
    private List<EditorWallSimplified> dataset = new ArrayList<>();

    public EditorWallAdapter(List<EditorWallSimplified> dataset) {
        this.dataset = dataset;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EditorWallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEditorWallBinding binding = ItemEditorWallBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new EditorWallViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EditorWallViewHolder holder, int position) {
        holder.bind(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class EditorWallViewHolder extends RecyclerView.ViewHolder {

        private ItemEditorWallBinding binding;

        public EditorWallViewHolder(ItemEditorWallBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(EditorWallSimplified editorWallSimplified) {
            binding.getRoot().setOnClickListener(v -> onItemClickListener.onItemClick(getAdapterPosition()));
            if(editorWallSimplified.getLastUpdatedOn() != null) {
                binding.txtLastUpdatedOn.setVisibility(View.VISIBLE);
                binding.txtLastUpdatedOn.setText(
                    binding.getRoot().getContext().getString(
                        R.string.last_updated_on,
                        DateUtils.getFormattedDate(editorWallSimplified.getLastUpdatedOn())
                    )
                );
            } else {
                binding.txtLastUpdatedOn.setVisibility(View.GONE);
            }
            Glide.with(binding.getRoot())
                    .load(Uri.parse(editorWallSimplified.getOriginalImageUrl()))
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.loading_img)
                    .into(binding.imgOriginal);

            Glide.with(binding.getRoot())
                    .load(Uri.parse(editorWallSimplified.getEditedImageUrl()))
                    .placeholder(R.drawable.loading_img)
                    .error(R.drawable.loading_img)
                    .into(binding.imgEdited);
        }
    }
}
