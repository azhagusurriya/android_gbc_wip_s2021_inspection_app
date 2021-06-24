package com.example.wip_android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wip_android.R;
import com.example.wip_android.fragments.AddImagePin;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.example.wip_android.fragments.MapFragment;
import com.example.wip_android.ui.gallery.GalleryFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ProjectActivity extends AppCompatActivity {

    // Gallery Settings
    private static final int GALLERY_REQUEST_CODE = 105;
    private final String TAG = this.getClass().getCanonicalName();
    private Fragment switchFragment;
    FragmentTransaction transaction;
    Uri contentUri;
    String stringUri;

    // Variables
    private ImageView selectedImage;
    private TextView addDeficiency;

    // Default Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        // Bar title
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Deficiency List</font>"));
        }

        selectedImage = findViewById(R.id.projectImage);
        addDeficiency = findViewById(R.id.addDeficiency);

    }

    public void addDeficiency(View view) {
        System.out.println("test");

        switchFragment = new AddImagePin();
        transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("photo", stringUri);
        switchFragment.setArguments(bundle);

        transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();


//        goToAddDeficiency();
//        goToAddMarker();
    }


    // Choose image from gallery
    public void pickImage(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
    }

    // Gallery methods
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                selectedImage.setImageURI(contentUri);
                Log.d(TAG, "onActivityResult: Image " + contentUri );
                stringUri = contentUri.toString();
            }
        }
    }

    // Get file
    private String getFileExt(Uri contentUri) {
        ContentResolver c = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
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
            transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();

            return true;
        }

        if (id == R.id.action_map) {
            Log.d(TAG, "onOptionsItemSelected: Map selected ");
            switchFragment = new MapFragment();
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void goToAddMarker(){
        this.finish();
        Intent mainIntent = new Intent(this, AddImagePinActivity.class);
        startActivity(mainIntent);
    }

    private void goToAddDeficiency(){
        this.finish();
        Intent mainIntent = new Intent(this, DeficiencyActivity.class);
        mainIntent.putExtra("FROM_ACTIVITY", "ProjectActivity");
        startActivity(mainIntent);
    }

}