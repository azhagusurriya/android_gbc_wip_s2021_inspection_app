package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.wip_android.R;
import com.example.wip_android.adapters.GlossaryAdapter;

import java.util.ArrayList;
import java.util.List;

public class GlossaryActivity extends AppCompatActivity implements GlossaryAdapter.onNoteListener {

    // Variables
    private RecyclerView recyclerView;
    GlossaryAdapter recyclerAdapter;
    List<String> glossaryList;
    TextView txtLineOne;
    String chosenItem;

    // Default Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary);

        // Glossary
        glossaryList = new ArrayList<>();
        glossaryList.add("Foundation Wall Cracks");
        glossaryList.add("Faulty Roofs");
        glossaryList.add("Sump Pump Problems");
        glossaryList.add("Wall Cracks");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerAdapter = new GlossaryAdapter(glossaryList, this);
        recyclerView.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        chosenItem = recyclerAdapter.getChosenItem();
        System.out.println(chosenItem);
//        Intent intent = new Intent(this, DeficiencyActivity.class);
//        intent.putExtra("chosenItem", chosenItem);
//        intent.putExtra("FROM_ACTIVITY", "GlossaryActivity");
//        startActivity(intent);
    }

    public void addButtonPressed(MenuItem item) {
        System.out.println("Pressed");
    }

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


    @Override
    public void onNoteClick(int position) {
        String test = glossaryList.get(position);
        Intent intent = new Intent(this, DeficiencyActivity.class);
        intent.putExtra("test", test);
        intent.putExtra("FROM_ACTIVITY", "GlossaryActivity");
        startActivity(intent);
    }
}