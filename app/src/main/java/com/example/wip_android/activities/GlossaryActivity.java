package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.wip_android.R;
import com.example.wip_android.adapters.GlossaryAdapter;
import com.example.wip_android.models.GlossaryDeleteDialog;
import com.example.wip_android.models.GlossaryDialog;
import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.viewmodels.AddProjectViewModel;
import com.example.wip_android.viewmodels.GlossaryViewModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GlossaryActivity extends AppCompatActivity implements GlossaryAdapter.onNoteListener,
        GlossaryDialog.GlossaryDialogInterface, GlossaryDeleteDialog.GlossaryDeleteDialogInterface {

    // Variables
    private RecyclerView glossaryRecyclerView;
    private GlossaryAdapter recyclerAdapter;
    private List<String> glossaryList;
    private List<String> glossaryContentList;
    private List<String> glossaryCategoryList;
    private GlossaryItem chosenItem;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_NAME = "Glossary";

    // Default Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        // Get glossary data from Firebase
        this.refreshRecyclerView();
    }

    // Add a new item to glossary
    public void addButtonPressed(MenuItem item) {
        GlossaryDialog glossaryDialog = new GlossaryDialog();
        glossaryDialog.show(getSupportFragmentManager(), "Test Dialog");
    }

    // Grab and refresh data from Firebase
    public void refreshRecyclerView() {
        // Get glossary from Firebase
        db.collection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
            // Check task
            if (task.isSuccessful()) {
                // Check if there is any document
                if (task.getResult().getDocuments().size() != 0) {
                    // Convert list to custom class
                    List<GlossaryItem> glossaryList = task.getResult().toObjects(GlossaryItem.class);
                    this.glossaryContentList = new ArrayList<>();
                    this.glossaryCategoryList = new ArrayList<>();
                    for (int i = 0; i < glossaryList.size(); i++) {
                        this.glossaryContentList.add(glossaryList.get(i).getContent());
                        this.glossaryCategoryList.add(glossaryList.get(i).getCategory());
                    }
                    // Display on recycler view
                    this.recyclerAdapter = new GlossaryAdapter(this, glossaryList);
                    this.glossaryRecyclerView = findViewById(R.id.glossaryRecyclerView);
                    this.glossaryRecyclerView.setAdapter(this.recyclerAdapter);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                            DividerItemDecoration.VERTICAL);
                    this.glossaryRecyclerView.addItemDecoration(dividerItemDecoration);
                    this.chosenItem = this.recyclerAdapter.getChosenItem();
                    this.glossaryList = this.glossaryContentList;
                }
            }
        });
    }

    // Search view
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.glossary_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // Choose item and come back to DeficiencyActivity
    @Override
    public void onNoteClick(int position) {
        String test = this.recyclerAdapter.getGlossaryList().get(position).getContent();
        Intent intent = new Intent(this, DeficiencyActivity.class);
        intent.putExtra("test", test);
        intent.putExtra("FROM_ACTIVITY", "GlossaryActivity");
        startActivity(intent);
    }

    // Add new item to glossary
    @Override
    public void addNewGlossaryItem(GlossaryItem newGlossaryItem) {
        String id = newGlossaryItem.getContent() + " - " + newGlossaryItem.getCategory();
        System.out.println(id);
        db.collection(COLLECTION_NAME).document(id).set(newGlossaryItem);
        this.refreshRecyclerView();
    }

    // Delete an item from glossary
    @Override
    public void deleteGlossaryItem(GlossaryItem glossaryItem) {
        String id = glossaryItem.getContent() + " - " + glossaryItem.getCategory();
        db.collection(COLLECTION_NAME).document(id).delete();
        this.refreshRecyclerView();
    }

}