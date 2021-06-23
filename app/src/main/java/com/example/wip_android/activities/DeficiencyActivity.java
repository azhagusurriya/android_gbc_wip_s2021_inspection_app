package com.example.wip_android.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.wip_android.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DeficiencyActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Variables
    MaterialSpinner spinner;
    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String selected;
    TextInputLayout edtIssue;

    // Default Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deficiency);

        // Bar Title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Deficiency</font>"));
        }

        // Spinner Department List
        listItems.add("Foundation Wall Cracks");
        listItems.add("Faulty Roofs");
        listItems.add("Sump Pump Problems");
        listItems.add("Wall Cracks");

        // Spinner Adapter
        spinner = findViewById(R.id.spinnerDepartment);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Intent mIntent = getIntent();
        String previousActivity= mIntent.getStringExtra("FROM_ACTIVITY");
        System.out.println(previousActivity);

        this.edtIssue = findViewById(R.id.edtIssue);

        if (previousActivity.equals("GlossaryActivity")) {
            Bundle bundle = getIntent().getExtras();
            String chosenItem = bundle.getString("test");
            edtIssue.getEditText().setText(chosenItem);
        }


    }

    // Go to glossary screen
    public void selectGlossary(View view) {
        this.finish();
        Intent mainIntent = new Intent(this, GlossaryActivity.class);
        startActivity(mainIntent);
    }

    // Pick image
    public void pickIssueImage(View view) {
        System.out.println("Pressed");
    }

    // Spinner Methods
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position != -1) {
            selected = spinner.getItemAtPosition(position).toString();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}