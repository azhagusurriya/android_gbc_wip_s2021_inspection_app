package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wip_android.R;


public class AddProjectActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Project</font>"));
        }
        setContentView(R.layout.activity_add_project);

    }




}
