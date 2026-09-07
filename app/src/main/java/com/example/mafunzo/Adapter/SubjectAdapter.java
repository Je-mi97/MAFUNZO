package com.example.mafunzo.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mafunzo.R;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    private List<String> items;
    private boolean isPlaceholder;

    public SubjectAdapter(List<String> items, boolean isPlaceholder) {
        this.items = items;
        this.isPlaceholder = isPlaceholder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = items.get(position);
        holder.tvName.setText(name);

        // Attribution d'icônes selon le nom pour faire "pro"
        String lowerName = name.toLowerCase();
        if (lowerName.contains("informatique") || lowerName.contains("java")) {
            holder.ivIcon.setImageResource(android.R.drawable.ic_menu_agenda);
        } else if (lowerName.contains("web") || lowerName.contains("html")) {
            holder.ivIcon.setImageResource(android.R.drawable.ic_menu_view);
        } else if (lowerName.contains("marketing")) {
            holder.ivIcon.setImageResource(android.R.drawable.ic_menu_share);
        } else if (lowerName.contains("android")) {
            holder.ivIcon.setImageResource(android.R.drawable.ic_menu_compass);
        } else {
            holder.ivIcon.setImageResource(android.R.drawable.ic_menu_help);
        }

        if (isPlaceholder) {
            holder.itemView.setAlpha(0.5f);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_subject_icon);
            tvName = itemView.findViewById(R.id.tv_subject_name);
        }
    }
}
