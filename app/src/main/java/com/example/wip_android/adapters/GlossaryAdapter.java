package com.example.wip_android.adapters;

import android.content.Intent;
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
import com.example.wip_android.activities.DeficiencyActivity;
import com.example.wip_android.activities.GlossaryActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GlossaryAdapter extends RecyclerView.Adapter<GlossaryAdapter.ViewHolder> implements Filterable {

    // Variables
    List<String> glossaryList;
    List<String> glossaryListAll;
    String chosenItem;
    onNoteListener mOnNoteListener;

    // Adapter
    public GlossaryAdapter(List<String> glossaryList, onNoteListener onNoteListener) {
        this.glossaryList = glossaryList;
        glossaryListAll = new ArrayList<>();
        glossaryListAll.addAll(glossaryList);
        this.mOnNoteListener = onNoteListener;
    }

    // Recycler View Settings
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_glossary_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnNoteListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position));
        holder.textView.setText(glossaryList.get(position));
    }

    @Override
    public int getItemCount() {
        return glossaryList.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    // Search View Settings
    Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(glossaryListAll);
            } else {
                for (String movie: glossaryListAll) {
                    if (movie.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(movie);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        //Automatic on UI thread
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            glossaryList.clear();
            glossaryList.addAll((Collection<? extends String>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView textView, rowCountTextView;
        onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            rowCountTextView = itemView.findViewById(R.id.textView);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            chosenItem = glossaryList.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
//            Toast.makeText(view.getContext(), glossaryList.get(getAdapterPosition()), Toast.LENGTH_SHORT).show();
        }

    }

        public interface onNoteListener {
            void onNoteClick(int position);
        }

    public String getChosenItem() {
        return chosenItem;
    }
}
