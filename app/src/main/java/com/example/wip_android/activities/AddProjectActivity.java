package com.example.wip_android.activities;

import static com.example.wip_android.ui.gallery.GalleryFragment.GALLERY_REQUEST_CODE;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip_android.R;
import com.example.wip_android.fragments.MapFragment;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.User;
import com.example.wip_android.ui.gallery.GalleryFragment;
import com.example.wip_android.viewmodels.AddProjectViewModel;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentManager fragmentManager;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    private TextView apGalleryBtn;
    private TextInputLayout edtClientName,edtStreetAddress,edtCity,edtProvince,edtClientPhone;
    private Button btnSaveInfo ;
    private AddProjectViewModel addProjectViewModel;
    private ImageView ivClientImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Project</font>"));
        }


        this.addProjectViewModel = AddProjectViewModel.getInstance();
        this.apGalleryBtn = findViewById(R.id.apGalleryBtn);
        this.apGalleryBtn.setOnClickListener(this);
        this.btnSaveInfo = findViewById(R.id.btnSaveInfo);
        this.btnSaveInfo.setOnClickListener(this);

        this.edtClientName = findViewById(R.id.edtClientName);
        this.edtStreetAddress = findViewById(R.id.edtStreetAddress);
        this.edtCity = findViewById(R.id.edtCity);
        this.edtProvince = findViewById(R.id.edtProvince);
        this.edtClientPhone = findViewById(R.id.edtClientPhone);
        this.ivClientImage = findViewById(R.id.ivClientImage);


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


    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.apGalleryBtn:{
                    pickImage(view);
                    Log.d(TAG, "onClick: Gallery Button clicked");
                    break;
                }
                case R.id.btnSaveInfo:{
                    if(this.validateData()) {
                        Log.d(TAG, "onClick: Save Button clicked");
                        this.validateAddProject();

                        this.goToProjectDeficiencyList();

                    }
                    break;
                }

                default: break;
            }
        }

    }

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
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                ivClientImage.setImageURI(contentUri);
            }
        }
    }

    // Get file
    private String getFileExt(Uri contentUri) {
        ContentResolver c = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    private Boolean validateData() {
        if (this.edtClientName.getEditText().getText().toString().isEmpty()) {
            this.edtClientName.setError("Please enter Client Name");
            return false;
        }
        if (this.edtStreetAddress.getEditText().getText().toString().isEmpty()) {
            this.edtStreetAddress.setError("Please enter Streed Address");
            return false;
        }
        if (this.edtCity.getEditText().getText().toString().isEmpty()) {
            this.edtCity.setError("Please enter City");
            return false;
        }
        if (this.edtProvince.getEditText().getText().toString().isEmpty()) {
            this.edtProvince.setError("Please enter Province");
            return false;
        }

        if (this.edtClientPhone.getEditText().getText().toString().isEmpty()){
            this.edtClientPhone.setError("PPlease enter phone number");
        }
        return true;
    }

    private void validateAddProject(){
        ClientInfo newClient = new ClientInfo();

        newClient.setClientName(this.edtClientName.getEditText().getText().toString());
        newClient.setClientStreetAddress(this.edtStreetAddress.getEditText().getText().toString());
        newClient.setClientCity(this.edtCity.getEditText().getText().toString());
        newClient.setClientProvince(this.edtProvince.getEditText().getText().toString());
        newClient.setClientPhoneNumber(this.edtClientPhone.getEditText().getText().toString());


        // createAuthUser(this.edtEmail.getText().toString(),this.edtPassword.getText().toString());
        this.addProjectViewModel.createNewClient(newClient);

    }

    private void goToProjectDeficiencyList(){
        this.finish();
        Intent mainIntent = new Intent(this, ProjectActivity.class);
        startActivity(mainIntent);
    }
}
