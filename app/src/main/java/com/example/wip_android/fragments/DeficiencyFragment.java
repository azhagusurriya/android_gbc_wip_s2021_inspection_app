package com.example.wip_android.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.wip_android.R;
import com.example.wip_android.activities.GlossaryActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;


public class DeficiencyFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    // Variables
    MaterialSpinner spinner;
    List<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String selected;
    TextInputLayout edtIssue;
    View view;
    TextView selectGlossary;

    public static DeficiencyFragment newInstance() {
        return new DeficiencyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_deficiency, container, false);

        // Bar Title
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Deficiency</font>"));
        }


        this.selectGlossary = view.findViewById(R.id.selectGlossary);

        selectGlossary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                navigateToGlossary();
            }
        });




        // Spinner Department List
        listItems.add("Foundation Wall Cracks");
        listItems.add("Faulty Roofs");
        listItems.add("Sump Pump Problems");
        listItems.add("Wall Cracks");

        // Spinner Adapter
        spinner = view.findViewById(R.id.spinnerDepartment);
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Intent mIntent = getActivity().getIntent();
        String previousActivity= mIntent.getStringExtra("FROM_ACTIVITY");
        System.out.println(previousActivity);

        this.edtIssue = view.findViewById(R.id.edtIssue);

//        if (previousActivity.equals("GlossaryActivity")) {
//            Bundle bundle = getActivity().getIntent().getExtras();
//            String chosenItem = bundle.getString("test");
//            edtIssue.getEditText().setText(chosenItem);
//        }

        if(getArguments() != null){
            String yourText = getArguments().getString("key");
            System.out.println(yourText);
        }

        return view;
    }

    // Go to glossary screen
    public void navigateToGlossary() {
        Intent mainIntent = new Intent(getActivity(), GlossaryActivity.class);
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