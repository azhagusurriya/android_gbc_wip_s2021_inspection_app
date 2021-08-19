package com.example.wip_android.fragments;

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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.GlossaryActivity;
import com.example.wip_android.activities.ProjectActivity;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.models.ProjectInfo;
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

import static com.example.wip_android.ui.gallery.GalleryFragment.GALLERY_REQUEST_CODE;

public class DeficiencyFragment extends Fragment {

    // Variables
    private MaterialSpinner spinner;
    private List<String> listItems = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String selected;
    private TextInputLayout edtIssue;
    private View view;
    private TextView selectGlossary;
    private Button pickIssueImage;
    private Button btnSaveBefore;
    private String buttonId;
    private Bitmap oldDrawable, newDrawable;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;
    private ImageView ivBefore;
    private Uri contentUri;
    private String uploadedImageUrl;
    private Fragment switchFragment;
    private FragmentTransaction transaction;
    private FirebaseUser firebaseUser;
    private String currentUserEmail;
    private DeficiencyInfo issueToGet;
    private String existedImageBefore;
    private String currentImage;
    private String existedImageLinkAfter;
    private String existedCommentAfter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deficiency, container, false);

        // Get data and UI components
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        this.currentUserEmail = firebaseUser.getEmail();
        this.ivBefore = view.findViewById(R.id.ivBefore);
        this.edtIssue = view.findViewById(R.id.edtIssue);

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

        // If it is GlossaryActivity, get glossary item
        if (previousActivity.equals("GlossaryActivity")) {
            String chosenItem = bundle.getString("item");
            if (bundle.containsKey("currentImage")) {
                currentImage = bundle.getString("currentImage");
                Glide.with(this).load(currentImage).into(ivBefore);
            }
            edtIssue.getEditText().setText(chosenItem);
        } else if (previousActivity.equals("DeficiencyImageViewFragment")) {
            String imageLinkBefore = bundle.getString("imageLinkBefore");
            String commentBefore = bundle.getString("commentBefore");
            existedImageLinkAfter = bundle.getString("imageLinkAfter");
            existedCommentAfter = bundle.getString("commentAfter");
            if (!imageLinkBefore.equals("")) {
                Glide.with(this).load(imageLinkBefore).into(ivBefore);
                existedImageBefore = imageLinkBefore;
            }
            edtIssue.getEditText().setText(commentBefore);
        }

        // Go to glossary
        this.selectGlossary = view.findViewById(R.id.selectGlossary);
        selectGlossary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToGlossary();
            }
        });

        // Pick an image from the gallery
        this.pickIssueImage = view.findViewById(R.id.pickIssueImage);
        pickIssueImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);
            }
        });

        // Save and come back to HomeScreen
        this.btnSaveBefore = view.findViewById(R.id.btnSaveBefore);
        btnSaveBefore.setOnClickListener(new View.OnClickListener() {
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

    // Go to glossary screen
    public void navigateToGlossary() {
        Intent mainIntent = new Intent(getActivity(), GlossaryActivity.class);
        mainIntent.putExtra("buttonNumber", buttonId);
        mainIntent.putExtra("clientName", clientName);
        mainIntent.putExtra("imageLinkAfter", existedImageLinkAfter);
        mainIntent.putExtra("commentAfter", existedCommentAfter);

        // Get image, so it does not disappear when the user goes to Glossary
        System.out.println("CONTENT URI: " + contentUri);
        if (contentUri != null) {
            Uri file = Uri.fromFile(new File(String.valueOf(contentUri)));
            String fileExt = MimeTypeMap.getFileExtensionFromUrl(file.toString());
            final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("uploads")
                    .child(System.currentTimeMillis() + "." + fileExt);
            fileRef.putFile(contentUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            currentImage = url;
                            if (currentImage != null) {
                                mainIntent.putExtra("currentImage", currentImage);
                            }
                            startActivity(mainIntent);
                        }
                    });
                }
            });
        } else {
            if (existedImageBefore != null) {
                currentImage = existedImageBefore;
                if (currentImage != null) {
                    mainIntent.putExtra("currentImage", currentImage);
                }
            }
            startActivity(mainIntent);
        }

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
                ivBefore.setImageURI(contentUri);
                this.newDrawable = ((BitmapDrawable) ivBefore.getDrawable()).getBitmap();
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
            if (existedImageBefore != null) {
                uploadedImageUrl = existedImageBefore;
                pd.dismiss();
                updateFirebaseData(buttonId);
                goToHome();
            } else {
                if (currentImage != null) {
                    uploadedImageUrl = currentImage;
                }
                pd.dismiss();
                updateFirebaseData(buttonId);
                goToHome();
            }
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
                                                        String imageLinkBefore = "";
                                                        if (uploadedImageUrl == null) {
                                                            imageLinkBefore = currentDeficiencyInfo
                                                                    .getImageLinkBefore();
                                                        } else {
                                                            imageLinkBefore = uploadedImageUrl;
                                                        }
                                                        String imageLinkAfter = currentDeficiencyInfo
                                                                .getImageLinkAfter();

                                                        // Comments
                                                        String commentBefore = currentDeficiencyInfo.getCommentBefore();
                                                        if (edtIssue.getEditText().getText() != null) {
                                                            commentBefore = String
                                                                    .valueOf(edtIssue.getEditText().getText());
                                                        }
                                                        String commentAfter = currentDeficiencyInfo.getCommentAfter();

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
    public static DeficiencyFragment newInstance() {
        return new DeficiencyFragment();
    }

}