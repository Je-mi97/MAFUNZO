package com.example.mafunzo.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mafunzo.Activity.ModuleDetailActivity;
import com.example.mafunzo.Model.Course;
import com.example.mafunzo.Model.Module;
import com.example.mafunzo.R;
import com.example.mafunzo.Utils.ProgressManager;

public class ModuleAdapter
        extends RecyclerView.Adapter<ModuleAdapter.ModuleViewHolder> {

    private final Context context;
    private final Course course;

    private final ProgressManager progressManager;

    public ModuleAdapter(
            Context context,
            Course course
    ) {

        this.context = context;
        this.course = course;

        this.progressManager =
                new ProgressManager(context);
    }

    @NonNull
    @Override
    public ModuleViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context).inflate(
                        R.layout.item_module,
                        parent,
                        false
                );

        return new ModuleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ModuleViewHolder holder,
            int position
    ) {

        Module module =
                course.getModules().get(position);

        boolean completed =
                progressManager.isModuleCompleted(
                        course.getId(),
                        module.getId()
                );

        module.setCompleted(
                completed
        );

        holder.tvTitle.setText(
                module.getTitle()
        );

        holder.tvDescription.setText(
                module.getDescription()
        );

        holder.tvType.setText(
                module.getContentType()
        );

        if (completed) {

            holder.tvStatus.setText(
                    "✓ Terminé"
            );

            holder.tvStatus.setTextColor(
                    context.getColor(
                            R.color.success
                    )
            );

        } else {

            holder.tvStatus.setText(
                    "À commencer"
            );

            holder.tvStatus.setTextColor(
                    context.getColor(
                            R.color.text_secondary
                    )
            );
        }

        holder.itemView.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            context,
                            ModuleDetailActivity.class
                    );

            intent.putExtra(
                    "module",
                    module
            );

            intent.putExtra(
                    "course_id",
                    course.getId()
            );

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return course.getModules().size();
    }

    static class ModuleViewHolder
            extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDescription;
        TextView tvType;
        TextView tvStatus;

        ModuleViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            tvTitle =
                    itemView.findViewById(
                            R.id.tv_module_title
                    );

            tvDescription =
                    itemView.findViewById(
                            R.id.tv_module_description
                    );

            tvType =
                    itemView.findViewById(
                            R.id.tv_module_type
                    );

            tvStatus =
                    itemView.findViewById(
                            R.id.tv_module_status
                    );
        }
    }
}