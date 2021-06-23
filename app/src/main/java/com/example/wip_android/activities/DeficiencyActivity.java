package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wip_android.R;

public class DeficiencyActivity extends AppCompatActivity {

    // Default Function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deficiency);
    }

    // Go to glossary screen
    public void selectGlossary() {
        System.out.println("test");
    }

}