package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip_android.R;
import com.example.wip_android.fragments.MapFragment;
import com.example.wip_android.ui.gallery.GalleryFragment;


public class AddProjectActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentManager fragmentManager;
    private Fragment switchFragment;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Project</font>"));
        }
        setContentView(R.layout.activity_add_project);





    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.camera_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_camera) {
            Log.d(TAG, "onOptionsItemSelected: Camera selected ");

            switchFragment = new GalleryFragment();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.add_project_layout, switchFragment).addToBackStack(null).commit();

            return true;
        }

        if (id == R.id.action_map) {
            Log.d(TAG, "onOptionsItemSelected: Map selected ");
            switchFragment = new MapFragment();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.add_project_layout, switchFragment).addToBackStack(null).commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
