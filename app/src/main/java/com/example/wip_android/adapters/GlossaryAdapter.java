package com.example.wip_android.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SectionIndexer;
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

public class GlossaryAdapter extends RecyclerView.Adapter<GlossaryAdapter.ViewHolder>
        implements Filterable, SectionIndexer {

    // Variables
    private List<String> glossaryList;
    private List<String> glossaryDetailList;
    private List<String> glossaryListAll;
    private String chosenItem;
    private onNoteListener mOnNoteListener;
    private ArrayList<Integer> mSectionPositions;

    // Adapter
    public GlossaryAdapter(List<String> glossaryList, List<String> glossaryDetailList, onNoteListener onNoteListener) {
        this.glossaryList = glossaryList;
        this.glossaryDetailList = glossaryDetailList;
        this.mOnNoteListener = onNoteListener;

        this.glossaryListAll = new ArrayList<>();
        this.glossaryListAll.addAll(glossaryList);
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
        holder.glossaryTitle.setText(this.glossaryList.get(position));
        holder.glossaryDetail.setText(this.glossaryDetailList.get(position));
    }

    @Override
    public int getItemCount() {
        return this.glossaryList.size();
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
                filteredList.addAll(glossaryListAll);
            } else {
                for (String item : glossaryListAll) {
                    if (item.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(item);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;

            return filterResults;
        }

        // Automatic on UI thread
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
        TextView glossaryTitle, rowCountTextView, glossaryDetail;
        onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            glossaryTitle = itemView.findViewById(R.id.glossaryTitle);
            glossaryDetail = itemView.findViewById(R.id.glossaryDetail);
            rowCountTextView = itemView.findViewById(R.id.glossaryTitle);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            chosenItem = glossaryList.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public interface onNoteListener {
        void onNoteClick(int position);
    }

    public String getChosenItem() {
        return chosenItem;
    }

    // Alphabetical section settings
    @Override
    public int getSectionForPosition(int position) {
        return 0;
    }

    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = glossaryList.size(); i < size; i++) {
            String section = String.valueOf(glossaryList.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        return mSectionPositions.get(sectionIndex);
    }

}
