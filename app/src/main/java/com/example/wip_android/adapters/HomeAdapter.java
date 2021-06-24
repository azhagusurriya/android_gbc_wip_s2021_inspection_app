package com.example.wip_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip_android.R;

import java.util.ArrayList;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    // Variables
    List<String> homeList;
    List<String> homeListAll;
    String chosenHomeItem;
    HomeAdapter.onNoteListener mOnNoteListener;

    // Adapter
    public HomeAdapter(List<String> homeList, HomeAdapter.onNoteListener onNoteListener) {
        this.homeList = homeList;
        homeListAll = new ArrayList<>();
        homeListAll.addAll(homeList);
        this.mOnNoteListener = onNoteListener;
    }

    // Recycler View Settings
    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_home_item, parent, false);
        HomeAdapter.ViewHolder viewHolder = new HomeAdapter.ViewHolder(view, mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapter.ViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textView.setText(homeList.get(position));
    }

    @Override
    public int getItemCount() {
        return homeList.size();
    }

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, rowCountTextView;
        HomeAdapter.onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, HomeAdapter.onNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.textView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            chosenHomeItem = homeList.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
            Toast.makeText(view.getContext(), homeList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

}
