package com.example.wip_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip_android.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ViewHolder> {

    // Variables
    List<String> deficiencyList;
    List<String> deficiencyListAll;
    String chosenDeficiencyItem;
    ProjectAdapter.onNoteListener mOnNoteListener;

    // Adapter
    public ProjectAdapter(List<String> deficiencyList, ProjectAdapter.onNoteListener onNoteListener) {
        this.deficiencyList = deficiencyList;
        deficiencyListAll = new ArrayList<>();
        deficiencyListAll.addAll(deficiencyList);
        this.mOnNoteListener = onNoteListener;
    }

    // Recycler View Settings
    @NonNull
    @Override
    public ProjectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_project_item, parent, false);
        ProjectAdapter.ViewHolder viewHolder = new ProjectAdapter.ViewHolder(view, mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectAdapter.ViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textView.setText(deficiencyList.get(position));
    }

    @Override
    public int getItemCount() {
        return deficiencyList.size();
    }

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, rowCountTextView;
        ProjectAdapter.onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, ProjectAdapter.onNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.textView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            chosenDeficiencyItem = deficiencyList.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
            Toast.makeText(view.getContext(), deficiencyList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

}
