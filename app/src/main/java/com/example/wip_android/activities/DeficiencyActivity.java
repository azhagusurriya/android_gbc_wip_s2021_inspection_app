package com.example.wip_android.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;

import com.example.wip_android.R;

public class DeficiencyActivity extends AppCompatActivity {

    // Default Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deficiency);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Deficiency</font>"));
        }
    }

    // Go to glossary screen
    public void selectGlossary() {
        System.out.println("test");
    }

}