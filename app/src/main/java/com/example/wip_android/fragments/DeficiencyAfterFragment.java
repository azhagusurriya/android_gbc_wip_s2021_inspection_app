package com.example.wip_android.fragments;

import static com.example.wip_android.ui.gallery.GalleryFragment.GALLERY_REQUEST_CODE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.viewmodels.DeficiencyAfterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class DeficiencyAfterFragment extends Fragment {

    // Variables
    private List<String> listItems = new ArrayList<>();
    private TextInputLayout edtIssueAfter;
    private View view;
    private Button pickIssueImageAfter;
    private Button btnSaveAfter;
    private String buttonId;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;
    private ImageView ivAfter;
    private Uri contentUri;
    private String uploadedImageUrl;
    private FirebaseUser firebaseUser;
    private String currentUserEmail;
    private DeficiencyAfterViewModel mViewModel;
    private String existedImageAfter;

    // Default function
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.deficiency_after_fragment, container, false);

        // Get data and UI components
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.currentUserEmail = firebaseUser.getEmail();
        this.ivAfter = view.findViewById(R.id.ivAfter);
        this.edtIssueAfter = view.findViewById(R.id.edtIssueAfter);

        // Bar Title
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Deficiency</font>"));
        }

        // Check previous activity
        Intent mIntent = getActivity().getIntent();
        String previousActivity = mIntent.getStringExtra("FROM_ACTIVITY");
        Bundle bundle = getActivity().getIntent().getExtras();
        buttonId = bundle.getString("buttonNumber");
        clientName = bundle.getString("clientName");

        // If it is either DeficiencyImageViewFragment or GlossaryActivity
        System.out.println(previousActivity);
        if (bundle.containsKey("imageLinkAfter")) {
            String imageLinkAfter = bundle.getString("imageLinkAfter");
            if (!imageLinkAfter.equals("")) {
                Glide.with(this).load(imageLinkAfter).into(ivAfter);
                existedImageAfter = imageLinkAfter;
            }
        }
        if (bundle.containsKey("commentAfter")) {
            String commentAfter = bundle.getString("commentAfter");
            edtIssueAfter.getEditText().setText(commentAfter);
        }

        // Pick an image from the gallery
        this.pickIssueImageAfter = view.findViewById(R.id.pickIssueImageAfter);
        pickIssueImageAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        // Save and come back to HomeScreen
        this.btnSaveAfter = view.findViewById(R.id.btnSaveAfter);
        btnSaveAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(buttonId);
            }
        });

        return view;
    }

    // Go to home
    private void goToHome() {
        getActivity().finish();
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        mainIntent.putExtra("EmailId", currentUserEmail);
        startActivity(mainIntent);
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
                ivAfter.setImageURI(contentUri);
            }
        }
    }

    // Get file
    private String getFileExt(Uri contentUri) {
        ContentResolver c = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }

    // Uploading image to Firebase and get the Url of the image
    private void uploadImage(String buttonId) {
        // Create loading object
        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setMessage("Uploading");
        pd.show();
        // Check if there is an image
        if (contentUri != null) {
            // Create file reference
            Uri file = Uri.fromFile(new File(String.valueOf(contentUri)));
            String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads")
                    .child(System.currentTimeMillis() + "." + fileExt);
            // Upload image to Firebase Storage
            fileRef.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            uploadedImageUrl = url;
                            pd.dismiss();
                            // Upload image link to Firestore Database
                            updateFirebaseData(buttonId);
                            // Come back to HomeScreen
                            goToHome();
                        }
                    });
                }
            });
        } else {
            if (existedImageAfter != null) {
                uploadedImageUrl = existedImageAfter;
            }
            pd.dismiss();
            updateFirebaseData(buttonId);
            goToHome();
        }
    }

    // Upload information to Firebase Database
    public void updateFirebaseData(String buttonId) {
        // Check and get current project information
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();
                                        // Create a new DeficiencyInfo object
                                        db.collection(COLLECTION_CLIENT).document(clientId)
                                                .collection(COLLECTION_PROJECT).document(projectId)
                                                .collection(COLLECTION_DEFICIENCY).document(buttonId).get()
                                                .addOnCompleteListener(newTask -> {
                                                    if (newTask.isSuccessful()) {
                                                        DeficiencyInfo currentDeficiencyInfo = newTask.getResult()
                                                                .toObject(DeficiencyInfo.class);

                                                        // Images
                                                        String imageLinkBefore = currentDeficiencyInfo
                                                                .getImageLinkBefore();
                                                        String imageLinkAfter = "";
                                                        if (uploadedImageUrl == null) {
                                                            imageLinkAfter = currentDeficiencyInfo
                                                                    .getImageLinkBefore();
                                                        } else {
                                                            imageLinkAfter = uploadedImageUrl;
                                                        }

                                                        // Comments
                                                        String commentAfter = currentDeficiencyInfo.getCommentAfter();
                                                        if (edtIssueAfter.getEditText().getText() != null) {
                                                            commentAfter = String
                                                                    .valueOf(edtIssueAfter.getEditText().getText());
                                                        }
                                                        String commentBefore = currentDeficiencyInfo.getCommentBefore();

                                                        // Dates
                                                        Date deficiencyDateOfRegistration = currentDeficiencyInfo
                                                                .getDateOfRegistration();
                                                        Date deficiencyDateOfCompletion = currentDeficiencyInfo
                                                                .getDateOfCompletion();
                                                        Date deficiencyLastUpdated = new Date();

                                                        // Other
                                                        String title = buttonId;
                                                        String employeeIdOfRegisteration = currentDeficiencyInfo
                                                                .getEmployeeIdOfRegisteration();
                                                        boolean deficiencyCompletion = false;
                                                        if (imageLinkAfter != "") {
                                                            deficiencyCompletion = true;
                                                        }


                                                        // Create object
                                                        DeficiencyInfo newDeficiencyInfo = new DeficiencyInfo(title,
                                                                imageLinkBefore, imageLinkAfter, commentBefore,
                                                                commentAfter, employeeIdOfRegisteration,
                                                                deficiencyCompletion, deficiencyDateOfRegistration,
                                                                deficiencyDateOfCompletion, deficiencyLastUpdated);

                                                        // Update deficiency button information
                                                        db.collection(COLLECTION_CLIENT).document(clientId)
                                                                .collection(COLLECTION_PROJECT).document(projectId)
                                                                .collection(COLLECTION_DEFICIENCY).document(buttonId)
                                                                .set(newDeficiencyInfo);
                                                    }
                                                });
                                    }
                                });
                    }

                });
    }

    // Other
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyAfterViewModel.class);
    }

    public static DeficiencyAfterFragment newInstance() {
        return new DeficiencyAfterFragment();
    }

}