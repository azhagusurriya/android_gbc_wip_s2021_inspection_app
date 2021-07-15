package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.wip_android.R;
import com.example.wip_android.fragments.ClientUpdateFragment;
import com.example.wip_android.fragments.DeficiencyAfterFragment;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.example.wip_android.fragments.DeficiencyImageViewFragment;
import com.example.wip_android.fragments.DeficiencyListFragment;
import com.google.android.material.tabs.TabLayout;

public class ProjectListItemActivity extends AppCompatActivity {


    private TabLayout tabLayout;
    private LinearLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_item);

        tabLayout = (TabLayout) findViewById(R.id.projectTabLayout);
        container = (LinearLayout) findViewById(R.id.project_fragment_container);

        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Customer"));


        replaceFragment(new DeficiencyImageViewFragment());


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    replaceFragment(new DeficiencyImageViewFragment());
                }  else if (tab.getPosition() == 1){
                    replaceFragment(new DeficiencyListFragment());
                }
                 else {
                    replaceFragment(new ClientUpdateFragment());
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
        transaction.replace(R.id.project_fragment_container, fragment);
        transaction.commit();
    }

}