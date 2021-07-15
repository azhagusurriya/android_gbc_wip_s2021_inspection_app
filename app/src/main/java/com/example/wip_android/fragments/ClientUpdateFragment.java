package com.example.wip_android.fragments;

import static com.example.wip_android.ui.gallery.GalleryFragment.GALLERY_REQUEST_CODE;

import androidx.lifecycle.ViewModelProvider;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.ProjectListItemActivity;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.AddProjectViewModel;
import com.example.wip_android.viewmodels.ClientUpdateViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ClientUpdateFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    private final String TAG = this.getClass().getCanonicalName();
    private TextInputLayout edtUpdateClientName,edtUpdateStreetAddress,edtUpdateCity,edtUpdateClientPhone;
    private Button btnUpdateClientInfo ;
    private ImageView ivUpdateClientImage;
    private AutoCompleteTextView spnUpdateProvince;
    private TextView apUpdateGalleryBtn;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser firebaseUser;
    private Uri contentUri;
    private String uploadedImageurl;
    private Bitmap updateOldDrawable,updateNewDrawable;


    private String currentClientDocumentID;

    private ClientUpdateViewModel mViewModel;

    public static ClientUpdateFragment newInstance() {
        return new ClientUpdateFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.client_update_fragment, container, false);

        
        this.apUpdateGalleryBtn = view.findViewById(R.id.apUpdateGalleryBtn);
        this.apUpdateGalleryBtn.setOnClickListener(this);
        this.btnUpdateClientInfo = view.findViewById(R.id.btnUpdateClientInfo);
        this.btnUpdateClientInfo.setOnClickListener(this);

        this.edtUpdateClientName = view.findViewById(R.id.edtUpdateClientName);
        this.edtUpdateStreetAddress = view.findViewById(R.id.edtUpdateStreetAddress);
        this.edtUpdateCity = view.findViewById(R.id.edtUpdateCity);
        this.edtUpdateClientPhone = view.findViewById(R.id.edtUpdateClientPhone);
        this.ivUpdateClientImage = view.findViewById(R.id.ivUpdateClientImage);

        this.spnUpdateProvince = view.findViewById(R.id.spnUpdateProvince);
        this.spnUpdateProvince.setOnItemSelectedListener(this);


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


        ArrayAdapter<String> updateProvinceAdapter = new ArrayAdapter<String>(getActivity(), R.layout.province_dropdown_item, provinces);
        spnUpdateProvince.setAdapter(updateProvinceAdapter);



        updateOldDrawable = ((BitmapDrawable) ivUpdateClientImage.getDrawable()).getBitmap();
        updateNewDrawable = ((BitmapDrawable) ivUpdateClientImage.getDrawable()).getBitmap();


        String name = this.getArguments().getString("name");
        Log.d(TAG, "onCreateView: Client Name inside update page : " + name);

        this.edtUpdateClientName.getEditText().setText(this.getArguments().getString("name"));
        this.edtUpdateStreetAddress.getEditText().setText(this.getArguments().getString("address"));
        this.edtUpdateCity.getEditText().setText(this.getArguments().getString("city"));
//        this.spnUpdateProvince.get().set(this.getArguments().getString("name"));
        this.edtUpdateClientPhone.getEditText().setText(this.getArguments().getString("phone"));

//        Uri imageLink = Uri.parse(this.getArguments().getString("image"));


        Glide.with(this).load(this.getArguments().getString("image")).into(ivUpdateClientImage);

//        this.ivUpdateClientImage.setImageURI(imageLink);

        getDocumentId();



        return view;
    }

    private void getDocumentId() {

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference deliveryRef = rootRef.collection("Client");
        Query nameQuery = deliveryRef.whereEqualTo("clientName", this.getArguments().getString("name"));
        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, "onComplete: Document id for the particular item : " + document.getId());
                        Log.d(TAG, document.getId());
                        currentClientDocumentID = document.getId();
                    }
                }
            }
        });
        nameQuery.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: Failure" + e.getLocalizedMessage());
            }
        });
    }


    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.apUpdateGalleryBtn:{
                    pickImage(view);
                    Log.d(TAG, "onClick: Gallery Button clicked");
                    break;
                }
                case R.id.btnUpdateClientInfo:{
                    if(this.validateData()) {
                        Log.d(TAG, "onClick: Save Button clicked");

                        if(updateOldDrawable == updateNewDrawable){
                            setClientInfoFromFields();
                        }
                        else{
                            uploadImage();
                        }


                        
                    }
                    break;
                }

                default: break;
            }
        }

    }


    public void setClientInfoFromFields(){
        ClientInfo newClientInfo = new ClientInfo();
        newClientInfo.setClientImage(this.getArguments().getString("image"));
        newClientInfo.setClientName(this.edtUpdateClientName.getEditText().getText().toString());
        newClientInfo.setClientStreetAddress(this.edtUpdateStreetAddress.getEditText().getText().toString());
        newClientInfo.setClientCity(this.edtUpdateCity.getEditText().getText().toString());
        newClientInfo.setClientPhoneNumber(this.edtUpdateClientPhone.getEditText().getText().toString());
        this.updateClientInfo(newClientInfo);
    }

    public void setClientInfoImageFromFields(){
        ClientInfo newClientInfo = new ClientInfo();
        newClientInfo.setClientImage(uploadedImageurl);
        newClientInfo.setClientName(this.edtUpdateClientName.getEditText().getText().toString());
        newClientInfo.setClientStreetAddress(this.edtUpdateStreetAddress.getEditText().getText().toString());
        newClientInfo.setClientCity(this.edtUpdateCity.getEditText().getText().toString());
        newClientInfo.setClientPhoneNumber(this.edtUpdateClientPhone.getEditText().getText().toString());
        this.updateClientInfo(newClientInfo);
    }



    public void updateClientInfo(ClientInfo clientInfo){
        try {
            db.collection("Client")
                    .document(currentClientDocumentID)
                    .update(
                            "clientName", clientInfo.getClientName(),
                            "clientStreetAddress", clientInfo.getClientStreetAddress(),
                            "clientCity", clientInfo.getClientCity(),
                            "clientPhoneNumber", clientInfo.getClientPhoneNumber(),"clientImage", clientInfo.getClientImage()
                    ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Log.d(TAG, "Client Info Document updated Successfully ");
                        navigateToHomeScreen();
                    }
                    else{
                        Log.d(TAG, "Error Updating Client Info Document");
                    }

                }
            });

        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public void navigateToHomeScreen(){
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
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
                ivUpdateClientImage.setImageURI(contentUri);

                this.updateNewDrawable = ((BitmapDrawable) ivUpdateClientImage.getDrawable()).getBitmap();

            }
        }
    }

    // Get file
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    //    validate fields
    private Boolean validateData() {
        
        if (this.edtUpdateClientName.getEditText().getText().toString().isEmpty()) {
            this.edtUpdateClientName.setError("Please enter Client Name");
            return false;
        }
        if (this.edtUpdateStreetAddress.getEditText().getText().toString().isEmpty()) {
            this.edtUpdateStreetAddress.setError("Please enter Streed Address");
            return false;
        }
        if (this.edtUpdateCity.getEditText().getText().toString().isEmpty()) {
            this.edtUpdateCity.setError("Please enter City");
            return false;
        }


        if (this.edtUpdateClientPhone.getEditText().getText().toString().isEmpty()){
            this.edtUpdateClientPhone.setError("PPlease enter phone number");
        }
        return true;
    }


    //Get file extension to save into firestore
    private String getFileExtension (Uri uri){
        ContentResolver contentResolver = getActivity().getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return  mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    //Uploading image to firestore and get the Url of the image
    private void uploadImage() {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading");
        pd.show();

        if (contentUri != null){
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads").child(System.currentTimeMillis() + "." + getFileExtension(contentUri));

            fileRef.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            uploadedImageurl = url;
                            Log.d("DownloadUrl" , url);
                            setClientInfoImageFromFields();
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Image upload successful1", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        }

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ClientUpdateViewModel.class);
        // TODO: Use the ViewModel


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}