package com.example.wip_android.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.adapters.GlossaryAdapter;
import com.example.wip_android.adapters.ProjectAdapter;
import com.example.wip_android.fragments.AddImagePin;
import com.example.wip_android.fragments.DeficiencyFragment;
import com.example.wip_android.fragments.MapFragment;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.ui.gallery.GalleryFragment;

import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectActivity extends AppCompatActivity implements ProjectAdapter.onNoteListener {

    // Gallery Settings
    private static final int GALLERY_REQUEST_CODE = 105;
    private final String TAG = this.getClass().getCanonicalName();
    private Fragment switchFragment;
    FragmentTransaction transaction;
    Uri contentUri;
    String stringUri;
    private TextView projectName;
    private TextView projectAddress;

    // Variables
    private RecyclerView recyclerView;
    private ImageView selectedImage;
    private TextView addDeficiency;
    private Button btnAddDeficiency;
    ProjectAdapter recyclerAdapter;
    List<String> deficiencyList;
    private ClientInfo clientInfo;

    // Default Functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);

        // Bar title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Deficiency List</font>"));
        }

        // UI Components
        selectedImage = findViewById(R.id.projectImage);
        btnAddDeficiency = findViewById(R.id.btnAddDeficiency);
        this.projectName = findViewById(R.id.projectName);
        this.projectAddress = findViewById(R.id.projectAddress);

        // Getting values for name and address
        Intent mIntent = getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        String address;
        String name;
        if (previousActivity.equals("HomeFragment")) {
            address = mIntent.getStringExtra("address");
            name = mIntent.getStringExtra("name");
            this.projectName.setText(name);
            this.projectAddress.setText(address);
        } else if (previousActivity.equals("AddProjectActivity")) {
            address = mIntent.getStringExtra("newAddress");
            name = mIntent.getStringExtra("newName");
            this.clientInfo = (ClientInfo) mIntent.getExtras().getSerializable("clientInfo");
            System.out.println(this.clientInfo.getClientCity());
            this.projectName.setText(name);
            this.projectAddress.setText(address);
        }

//        // Deficiency List
//        deficiencyList = new ArrayList<>();
//        deficiencyList.add("Foundation Wall Cracks");
//        deficiencyList.add("Faulty Roofs");
//        deficiencyList.add("Sump Pump Problems");
//        deficiencyList.add("Wall Cracks");
//
//        // Deficiency Recycler View
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerAdapter = new ProjectAdapter(deficiencyList, this);
//        recyclerView.setAdapter(recyclerAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void addDeficiency(View view) {
        System.out.println("test");

        switchFragment = new AddImagePin();
        transaction = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();
        bundle.putString("photo", stringUri);
        bundle.putString("Client Name", (String) this.projectName.getText());
        switchFragment.setArguments(bundle);

        transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();

        // goToAddDeficiency();
        // goToAddMarker();
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
                Log.d(TAG, "onActivityResult: Image " + contentUri);
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
//            switchFragment = new MapFragment();
//            transaction = getSupportFragmentManager().beginTransaction();
//            transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();

            Intent mainIntent = new Intent(this, MapScreenshotActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void goToAddMarker() {
        this.finish();
        Intent mainIntent = new Intent(this, AddImagePinActivity.class);
        startActivity(mainIntent);
    }

    private void goToAddDeficiency() {
        this.finish();
        Intent mainIntent = new Intent(this, DeficiencyActivity.class);
        mainIntent.putExtra("FROM_ACTIVITY", "ProjectActivity");
        startActivity(mainIntent);
    }

    // When Recycler View item is clicked do something
    @Override
    public void onNoteClick(int position) {
        String test = deficiencyList.get(position);
        System.out.println("pressed");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(ProjectActivity.this, MainActivity.class));
        finish();

    }

}