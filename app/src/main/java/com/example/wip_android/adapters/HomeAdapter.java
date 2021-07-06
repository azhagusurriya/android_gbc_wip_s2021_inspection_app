package com.example.wip_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip_android.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {

    // Variables
    private List<String> homeList;
    private List<String> homeListAll;
    private String chosenHomeItem;
    private HomeAdapter.onNoteListener mOnNoteListener;

    // Adapter
    public HomeAdapter(List<String> homeList, HomeAdapter.onNoteListener onNoteListener) {
        this.homeList = homeList;
        this.mOnNoteListener = onNoteListener;
        homeListAll = new ArrayList<>();
        homeListAll.addAll(homeList);
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
        holder.projectTitle.setText(this.homeList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.homeList.size();
    }

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView projectTitle, rowCountTextView;
        HomeAdapter.onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, HomeAdapter.onNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            projectTitle = itemView.findViewById(R.id.projectTitle);
            rowCountTextView = itemView.findViewById(R.id.projectTitle);
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

    // Interface used when project item is clicked
    public interface onNoteListener {
        void onNoteClick(int position);
    }

    // Search View Settings
    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(homeListAll);
            } else {
                for (String movie: homeListAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            homeList.clear();
            homeList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

}
