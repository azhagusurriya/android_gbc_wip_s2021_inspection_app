package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.fragments.DeficiencyAfterFragment;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.google.android.material.tabs.TabLayout;


public class DeficiencyTabLayoutActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private LinearLayout container;

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
                }  else {
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


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.deficiency_fragment_container, fragment);
        transaction.commit();
    }


}