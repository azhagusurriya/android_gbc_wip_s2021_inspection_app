package com.example.wip_android.adapters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wip_android.R;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.GlossaryDeleteDialog;
import com.example.wip_android.models.GlossaryItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> implements Filterable {



    // Variables
    private List<ClientInfo> homeList;
    private List<ClientInfo> homeListAll;
    private ClientInfo chosenItem;
    private onNoteListener mOnNoteListener;
    private ArrayList<Integer> mSectionPositions;
    private List<ClientInfo> homeItems;
    private final String TAG = this.getClass().getCanonicalName();
    private OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;


    // Adapter
    public HomeAdapter(onNoteListener onNoteListener, List<ClientInfo> homeItems, OnRecyclerItemLongClickListener onRecyclerItemLongClickListener) {
        this.homeList = homeItems;
        this.mOnNoteListener = onNoteListener;
        this.homeItems = homeItems;
        this.homeListAll = new ArrayList<ClientInfo>();
        this.homeListAll.addAll(homeItems);
        this.onRecyclerItemLongClickListener = onRecyclerItemLongClickListener;
    }

//    public HomeAdapter(onNoteListener onNoteListener, List<ClientInfo> homeItems) {
//        this.homeList = homeItems;
//        this.mOnNoteListener = onNoteListener;
//        this.homeItems = homeItems;
//        this.homeListAll = new ArrayList<ClientInfo>();
//        this.homeListAll.addAll(homeItems);
//    }


    // Recycler View Settings
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_home_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, mOnNoteListener, onRecyclerItemLongClickListener);

        return viewHolder;
    }

    public ClientInfo getChosenItem() {
        return chosenItem;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.rowCountTextView.setText(String.valueOf(position));
        holder.projectTitle.setText(this.homeList.get(position).getClientName());
        holder.projectAddress.setText(this.homeList.get(position).getClientStreetAddress());
        holder.projectDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd", this.homeList.get(position).getDateOfRegistration()));
    }

    @Override
    public int getItemCount() {
        return this.homeList.size();
    }

    // View Holder Class
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView imageView;
        TextView projectTitle, rowCountTextView, projectAddress, projectDate;
        HomeAdapter.onNoteListener onNoteListener;
        OnRecyclerItemLongClickListener onRecyclerItemLongClickListener;

        public ViewHolder(@NonNull View itemView, onNoteListener onNoteListener,OnRecyclerItemLongClickListener onRecyclerItemLongClickListener ) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            projectTitle = itemView.findViewById(R.id.title);
            projectAddress = itemView.findViewById(R.id.address);
            projectDate = itemView.findViewById(R.id.date);
            rowCountTextView = itemView.findViewById(R.id.title);
            this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            this.onRecyclerItemLongClickListener = onRecyclerItemLongClickListener;


            // Long Press to delete an item
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View itemView) {

                    Log.d(TAG, "onLongClick: Long clicked");

                    onRecyclerItemLongClickListener.onRecyclerItemLongClick(getAdapterPosition());

                    return false;
                }
            });


        }

        public void showAlertDialogButtonClicked(View view) {

            // setup the alert builder
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("AlertDialog");
            builder.setMessage("Would you like to continue learning how to use Android alerts?");

            // add the buttons
            builder.setPositiveButton("Continue", null);
            builder.setNegativeButton("Cancel", null);

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onClick(View view) {
            chosenItem = homeList.get(getAdapterPosition());
            onNoteListener.onNoteClick(getAdapterPosition());
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
            List<ClientInfo> filteredList = new ArrayList<ClientInfo>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(homeListAll);
            } else {
                for (ClientInfo item : homeListAll) {
                    String itemStr = item.getClientName();
                    if (itemStr.toLowerCase().contains(charSequence.toString().toLowerCase())) {
                        filteredList.add(item);
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
            homeList.addAll((Collection<? extends ClientInfo>) filterResults.values);
            notifyDataSetChanged();
        }
    };

    public List<ClientInfo> getHomeList() {
        return homeList;
    }


    //Interface for long click

    public interface OnRecyclerItemLongClickListener {

        void onRecyclerItemLongClick(int position);
    }

}
