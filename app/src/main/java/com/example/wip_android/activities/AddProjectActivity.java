package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.example.wip_android.ui.gallery.GalleryFragment;
import com.google.android.material.textfield.TextInputLayout;


public class AddProjectActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private FragmentManager fragmentManager;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    private TextView apGalleryBtn;
    private TextInputLayout edtClientName,edtStreetAddress,edtCity,edtProvince,edtClientPhone;
    private Button btnSaveInfo ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Project</font>"));
        }
        setContentView(R.layout.activity_add_project);
        this.apGalleryBtn = findViewById(R.id.apGalleryBtn);
        this.apGalleryBtn.setOnClickListener(this);
        this.btnSaveInfo = findViewById(R.id.btnSaveInfo);
        this.btnSaveInfo.setOnClickListener(this);

        this.edtClientName = findViewById(R.id.edtClientName);
        this.edtStreetAddress = findViewById(R.id.edtStreetAddress);
        this.edtCity = findViewById(R.id.edtCity);
        this.edtProvince = findViewById(R.id.edtProvince);
        this.edtClientPhone = findViewById(R.id.edtClientPhone);


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

                    Log.d(TAG, "onClick: Gallery Button clicked");
                    break;
                }
                case R.id.btnSaveInfo:{
                    if(this.validateData()) {
                        Log.d(TAG, "onClick: Save Button clicked");
                        this.validateAddProject();

                    }
                    break;
                }

                default: break;
            }
        }

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
//        String email = this.edtEmail.getEditText().getText().toString();
//        String password = this.edtPassword.getEditText().getText().toString();
//        this.userViewModel.signInAuthUser(email,password);
//        //this.userViewModel.validateUser(email, password);

    }
}
