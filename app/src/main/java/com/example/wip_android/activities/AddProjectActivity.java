package com.example.wip_android.activities;

import static com.example.wip_android.ui.gallery.GalleryFragment.GALLERY_REQUEST_CODE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.fragments.MapFragment;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.models.User;
import com.example.wip_android.ui.gallery.GalleryFragment;
import com.example.wip_android.viewmodels.AddProjectViewModel;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddProjectActivity extends AppCompatActivity
        implements View.OnClickListener, AdapterView.OnItemSelectedListener, Serializable {

    // Variables
    private final String TAG = this.getClass().getCanonicalName();
    private FragmentManager fragmentManager;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    private TextView apGalleryBtn;
    private TextInputLayout edtClientName, edtStreetAddress, edtCity, edtProvince, edtClientPhone;
    private Button btnSaveInfo;
    private AddProjectViewModel addProjectViewModel;
    private ImageView ivClientImage;
    private AutoCompleteTextView spnProvince;
    private String selectedProvince;
    private String currentUsersDepartment;
    private String currentUsersEmail;
    private final String COLLECTION_NAME = "Users";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private Uri contentUri;
    private String[] uploadedImageurl;
    private Bitmap oldDrawable, newDrawable;
    private ClientInfo clientInfoToPass;
    private List<String> userList = new ArrayList<>();
    private String firstEightCharDocumentId;

    // Default function
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);

        // Get all users from Firebase to later validation
        getUserList();

        // Action bar settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Project</font>"));
        }

        // Back button
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button

        // UI components
        this.addProjectViewModel = AddProjectViewModel.getInstance();
        this.apGalleryBtn = findViewById(R.id.apGalleryBtn);
        this.apGalleryBtn.setOnClickListener(this);
        this.btnSaveInfo = findViewById(R.id.btnSaveInfo);
        this.btnSaveInfo.setOnClickListener(this);

        this.edtClientName = findViewById(R.id.edtClientName);
        this.edtStreetAddress = findViewById(R.id.edtStreetAddress);
        this.edtCity = findViewById(R.id.edtCity);
        this.edtClientPhone = findViewById(R.id.edtClientPhone);
        this.ivClientImage = findViewById(R.id.ivClientImage);

        this.spnProvince = findViewById(R.id.spnProvince);
        this.spnProvince.setOnItemSelectedListener(this);

        this.oldDrawable = ((BitmapDrawable) ivClientImage.getDrawable()).getBitmap();
        this.newDrawable = ((BitmapDrawable) ivClientImage.getDrawable()).getBitmap();

        // Province list
        List<String> provinces = new ArrayList<String>();
        provinces.add("Alberta");
        provinces.add("British Columbia");
        provinces.add("Manitoba");
        provinces.add("New Brunswick");
        provinces.add("Newfoundland and Labrador");
        provinces.add("Northwest Territories");
        provinces.add("Nova Scotia");
        provinces.add("Nunavut");
        provinces.add("Ontario");
        provinces.add("Prince Edward Island");
        provinces.add("Quebec");
        provinces.add("Saskatchewan");
        provinces.add("Yukon");

        ArrayAdapter<String> provinceAdapter = new ArrayAdapter<String>(this, R.layout.province_dropdown_item,
                provinces);
        spnProvince.setAdapter(provinceAdapter);

        // Display RecyclerView with projects that have the same user department
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            this.currentUsersEmail = firebaseUser.getEmail();
            getCurrentUserDepartment(this.currentUsersEmail);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            // switchFragment = new MapFragment();
            // transaction = getSupportFragmentManager().beginTransaction();
            // transaction.replace(R.id.add_project_layout,
            // switchFragment).addToBackStack(null).commit();
            //
            Intent mainIntent = new Intent(this, MapScreenshotActivity.class);
            startActivity(mainIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.apGalleryBtn: {
                    pickImage(view);
                    Log.d(TAG, "onClick: Gallery Button clicked");
                    break;
                }
                case R.id.btnSaveInfo: {
                    // this.goToProjectDeficiencyList();
                    if (this.validateData()) {
                        Log.d(TAG, "onClick: Save Button clicked");

                        this.uploadImage();

                        // this.goToProjectDeficiencyList();

                    }
                    break;
                }

                default:
                    break;
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
                contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                ivClientImage.setImageURI(contentUri);
                this.newDrawable = ((BitmapDrawable) ivClientImage.getDrawable()).getBitmap();
            }
        }
    }

    // Get file
    private String getFileExt(Uri contentUri) {
        ContentResolver c = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    // Validate fields
    private Boolean validateData() {

        if (oldDrawable == newDrawable) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isFinishing()) {
                        new AlertDialog.Builder(AddProjectActivity.this).setTitle("Save Failed")
                                .setMessage("Please Add an Image").setCancelable(false)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Whatever...
                                    }
                                }).show();
                    }
                }
            });
            Log.d(TAG, "validateData: Image change Error");
            return false;
        }

        if (this.edtClientName.getEditText().getText().toString().isEmpty()) {
            this.edtClientName.setError("Please enter Client Name");
            return false;
        }

        if (userList.contains(this.edtClientName.getEditText().getText().toString())) {
            this.edtClientName.setError("Client Name already exists");
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

        if (this.edtClientPhone.getEditText().getText().toString().isEmpty()) {
            this.edtClientPhone.setError("Please enter phone number");
        }
        return true;
    }

    // Save info to firestore
    private void validateAddProject() {
        ClientInfo newClient = new ClientInfo();

        // uploadImageToFirebase(imageFileName,contentUri);

        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        String strClientName = this.edtClientName.getEditText().getText().toString();

        if(strClientName.length() >= 8){
             firstEightCharDocumentId = strClientName.substring(0, 8);
        }
        else{
            firstEightCharDocumentId = strClientName;
        }


        String documentID = firstEightCharDocumentId +"_"+ currentTime +"_"+ currentDate;
        Log.d(TAG, "validateAddProject: Current Document ID: " + documentID);

        newClient.setDocumentid(documentID);
        newClient.setClientName(this.edtClientName.getEditText().getText().toString());
        newClient.setClientStreetAddress(this.edtStreetAddress.getEditText().getText().toString());
        newClient.setClientCity(this.edtCity.getEditText().getText().toString());
        newClient.setClientPhoneNumber(this.edtClientPhone.getEditText().getText().toString());
        newClient.setClientProvince(this.spnProvince.getText().toString());
        java.util.Date date = new java.util.Date();
        newClient.setDateOfRegistration(date);
        newClient.setDepartment(currentUsersDepartment);
        newClient.setClientImage(uploadedImageurl);

        this.clientInfoToPass = newClient;

        // createAuthUser(this.edtEmail.getText().toString(),this.edtPassword.getText().toString());
        this.addProjectViewModel.createNewClient(newClient);
        goToProjectDeficiencyList();
    }

    // Get file extension to save into firestore
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Uploading image to firestore and get the Url of the image
    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (contentUri != null) {
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads")
                    .child(System.currentTimeMillis() + "." + getFileExtension(contentUri));

            fileRef.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            uploadedImageurl = url;
                            Log.d("DownloadUrl", url);
                            validateAddProject();
                            pd.dismiss();
                            Toast.makeText(AddProjectActivity.this, "Image upload successful1", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                }
            });
        }
    }

    // Navigate to add deficiency page
    private void goToProjectDeficiencyList() {
        this.finish();

        Intent mainIntent = new Intent(this, ProjectActivity.class);
        String name = this.edtClientName.getEditText().getText().toString();
        String address = this.edtStreetAddress.getEditText().getText().toString();

        mainIntent.putExtra("newName", name);
        mainIntent.putExtra("newAddress", address);
        mainIntent.putExtra("FROM_ACTIVITY", "AddProjectActivity");
        mainIntent.putExtra("clientInfo", (Serializable) this.clientInfoToPass);

        startActivity(mainIntent);
    }

    // Province dropdown item selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.selectedProvince = parent.getItemAtPosition(position).toString();
        Log.d(TAG, "onItemSelected: Selected Province: " + selectedProvince);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Get department for the current user
    public void getCurrentUserDepartment(String email) {
        db.collection(COLLECTION_NAME).whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() != 0) {
                                currentUsersDepartment = task.getResult().getDocuments().get(0).toObject(User.class)
                                        .getDepartment();

                            }
                        }
                    }
                });
    }

    // Get all users name from Firebase
    public void getUserList() {
        db.collection("Client").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getDocuments().size() != 0) {
                    List<ClientInfo> clientInfoList = task.getResult().toObjects(ClientInfo.class);
                    for (int i = 0; i < clientInfoList.size(); i++) {
                        userList.add(clientInfoList.get(i).getClientName());
                    }
                }
            }
        });
    }

}
