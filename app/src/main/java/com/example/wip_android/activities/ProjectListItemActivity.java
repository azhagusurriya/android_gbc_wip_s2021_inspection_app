package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.wip_android.R;
import com.example.wip_android.fragments.ClientUpdateFragment;
import com.example.wip_android.fragments.DeficiencyAfterFragment;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.example.wip_android.fragments.DeficiencyImageViewFragment;
import com.example.wip_android.fragments.DeficiencyListFragment;
import com.google.android.material.tabs.TabLayout;

public class ProjectListItemActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();
    private TabLayout tabLayout;
    private LinearLayout container;
    private String intentClientName,intentClientAddress,intentClientCity,intentClientProvince,intentClientPhone,intentClientImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_list_item);

        Intent i = getIntent();
        intentClientName = i.getStringExtra("name");
        Log.d(TAG, "onCreate: Client Name after intent" + intentClientName);
        intentClientAddress = i.getStringExtra("address");
        intentClientCity = i.getStringExtra("city");
        intentClientProvince = i.getStringExtra("province");
        intentClientImage = i.getStringExtra("image");
        intentClientPhone = i.getStringExtra("phone");


        tabLayout = (TabLayout) findViewById(R.id.projectTabLayout);
        container = (LinearLayout) findViewById(R.id.project_fragment_container);

        tabLayout.addTab(tabLayout.newTab().setText("Map"));
        tabLayout.addTab(tabLayout.newTab().setText("List"));
        tabLayout.addTab(tabLayout.newTab().setText("Customer"));


        imageViewFragment(new DeficiencyImageViewFragment());


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    imageViewFragment(new DeficiencyImageViewFragment());
                }  else if (tab.getPosition() == 1){
                    replaceFragment(new DeficiencyListFragment());
                }
                 else {
                    clientIntentFragment(new ClientUpdateFragment());
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

    private void clientIntentFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("name", intentClientName);
        bundle.putString("address", intentClientAddress);
        bundle.putString("city", intentClientCity);
        bundle.putString("province", intentClientProvince);
        bundle.putString("image", intentClientImage);
        bundle.putString("phone", intentClientPhone);
        fragment.setArguments(bundle);
        transaction.replace(R.id.project_fragment_container, fragment);
        transaction.commit();
    }

    private void imageViewFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("image", intentClientImage);
        fragment.setArguments(bundle);
        transaction.replace(R.id.project_fragment_container, fragment);
        transaction.commit();
    }



}