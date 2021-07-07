package com.example.wip_android.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip_android.R;
import com.example.wip_android.activities.DeficiencyActivity;
import com.example.wip_android.activities.GlossaryActivity;
import com.example.wip_android.models.GlossaryDeleteDialog;
import com.example.wip_android.models.GlossaryDialog;
import com.example.wip_android.models.GlossaryItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.security.AccessController.getContext;

public class GlossaryAdapter extends RecyclerView.Adapter<GlossaryAdapter.ViewHolder>
        implements Filterable, SectionIndexer {

    // Variables
    private List<GlossaryItem> glossaryList;
    private List<String> glossaryDetailList;
    private List<GlossaryItem> glossaryListAll;
    private GlossaryItem chosenItem;
    private onNoteListener mOnNoteListener;
    private ArrayList<Integer> mSectionPositions;
    private List<GlossaryItem> glossaryItems;

    // Adapter
    public GlossaryAdapter(onNoteListener onNoteListener, List<GlossaryItem> glossaryItems) {
        this.glossaryList = glossaryItems;
        this.mOnNoteListener = onNoteListener;
        this.glossaryItems = glossaryItems;
        this.glossaryListAll = new ArrayList<GlossaryItem>();
        this.glossaryListAll.addAll(glossaryItems);
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
        holder.glossaryTitle.setText(this.glossaryList.get(position).getContent());
        holder.glossaryDetail.setText(this.glossaryList.get(position).getCategory());
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
            List<GlossaryItem> filteredList = new ArrayList<GlossaryItem>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(glossaryListAll);
            } else {
                for (GlossaryItem item : glossaryListAll) {
                    String itemStr = item.getContent();
                    if (itemStr.toLowerCase().contains(charSequence.toString().toLowerCase())) {
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
            glossaryList.addAll((Collection<? extends GlossaryItem>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // Variables
        ImageView imageView;
        TextView glossaryTitle, rowCountTextView, glossaryDetail;
        onNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView, onNoteListener onNoteListener) {
            super(itemView);

            // UI Components
            imageView = itemView.findViewById(R.id.imageView);
            glossaryTitle = itemView.findViewById(R.id.glossaryTitle);
            glossaryDetail = itemView.findViewById(R.id.glossaryDetail);
            rowCountTextView = itemView.findViewById(R.id.glossaryTitle);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

            // Long Press to delete an item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View itemView) {

                    // Create dialog instance
                    GlossaryDeleteDialog glossaryDialog = new GlossaryDeleteDialog();
                    GlossaryItem itemToDelete = glossaryList.get(getAdapterPosition());

                    // Create glossary item and pass it
                    Bundle bundle = new Bundle();
                    bundle.putString("category",itemToDelete.getCategory());
                    bundle.putString("content",itemToDelete.getContent());
                    bundle.putString("department",itemToDelete.getDepartment());
                    bundle.putString("description",itemToDelete.getDescription());
                    bundle.putString("section",itemToDelete.getSection());
                    glossaryDialog.setArguments(bundle);

                    // Show the dialog
                    glossaryDialog.show(((AppCompatActivity) itemView.getContext()).getSupportFragmentManager(),
                            "Test Dialog");

                    return false;
                }
            });
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

    public GlossaryItem getChosenItem() {
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
            String section = String.valueOf(glossaryList.get(i).getContent().charAt(0)).toUpperCase();
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

    // Getter and Setters
    public List<GlossaryItem> getGlossaryList() {
        return glossaryList;
    }

}
