package com.example.wip_android.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.fragments.DeficiencyAfterFragment;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.google.android.material.tabs.TabLayout;

public class DeficiencyTabLayoutActivity extends AppCompatActivity {

    // Variables
    private TabLayout tabLayout;
    private LinearLayout container;

    // Default function
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deficiency_tab_layout);

        tabLayout = (TabLayout) findViewById(R.id.deficiencyTabLayout);
        container = (LinearLayout) findViewById(R.id.deficiency_fragment_container);

        tabLayout.addTab(tabLayout.newTab().setText("Before"));
        tabLayout.addTab(tabLayout.newTab().setText("After"));

        replaceFragment(new DeficiencyFragment());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new DeficiencyFragment());
                } else {
                    replaceFragment(new DeficiencyAfterFragment());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        // Action bar settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Deficiency Before/After</font>"));
        }

        // Back button
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.deficiency_fragment_container, fragment);
        transaction.commit();
    }

}