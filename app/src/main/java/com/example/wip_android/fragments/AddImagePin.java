package com.example.wip_android.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wip_android.R;
import com.example.wip_android.activities.DeficiencyActivity;
import com.example.wip_android.activities.DeficiencyTabLayoutActivity;
import com.example.wip_android.models.ButtonIssue;
import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.models.ProjectInfo;
import com.example.wip_android.viewmodels.AddImagePinViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddImagePin extends Fragment {

    // Variables
    private AddImagePinViewModel mViewModel;
    private View view;
    private ImageView imageView;
    private float x, y;
    private int counter = 1;
    private Fragment switchFragment;
    private FragmentTransaction transaction;
    private Uri contentUri;
    private FloatingActionButton fabSaveMarker;
    private Button addedButton;
    private ArrayList<Button> buttonList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_image_pin_fragment, container, false);

        // Add new red button when screen is touched
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();

                    addButton(x, y);
                }

                return true;
            }
        });

        // Get image
        Bundle bundle = this.getArguments();
        String imageFromProjectScreen = bundle.getString("photo");
        contentUri = Uri.parse(imageFromProjectScreen);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageURI(contentUri);

        // Get Name
        this.clientName = bundle.getString("Client Name");
        System.out.println(this.clientName);

        // Floating Button
        fabSaveMarker = (FloatingActionButton) view.findViewById(R.id.fabSaveMarker);
        fabSaveMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMarkerButtonPressed();
            }
        });

        return view;
    }

    // When the red button is clicked, upload information to Firebase and go to the
    // next screen
    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("BUTTON CLICKED: " + buttonList);
                getDataFromFirebase();
                // Intent intent = new Intent(getActivity(), DeficiencyTabLayoutActivity.class);
                // intent.putExtra("test", "TEST");
                // intent.putExtra("FROM_ACTIVITY", "AddImagePin");
                // startActivity(intent);
            }
        };
    }

    // When the red button is long clicked, delete it
    View.OnLongClickListener listener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.newLayout);
            Button clickedButton = (Button) v;
            for (int i = 0; i < buttonList.size(); i++) {
                if (buttonList.get(i).getId() == clickedButton.getId()) {
                    buttonList.remove(i);
                    break;
                }
            }
            layout.removeView(clickedButton);
            return true;
        }
    };

    // Upload information to Firebase
    public void getDataFromFirebase() {
        // First task
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();
                        System.out.print("SECOND TASK ID: " + clientId);

                        // Create a ProjectInfo object
                        String referenceId = "";
                        String imageLink = "";
                        ArrayList<Double> buttonLocationX = new ArrayList<>();
                        ArrayList<Double> buttonLocationY = new ArrayList<>();
                        ArrayList<String> buttonTitle = new ArrayList<>();
                        String employeeIdOfRegisteration = "ID";
                        boolean completion = false;
                        Date dateOfRegistration = new Date();
                        Date dateOfCompletion = new Date();
                        Date lastUpdated = new Date();

                        ProjectInfo projectInfo = new ProjectInfo(referenceId, imageLink, buttonLocationX,
                                buttonLocationY, buttonTitle, employeeIdOfRegisteration, completion, dateOfRegistration,
                                dateOfCompletion, lastUpdated);

                        // Create a DeficiencyInfo object
                        String title = "TITLE";
                        String imageLinkBefore = "IMAGE LINK BEFORE";
                        String imageLinkAfter = "IMAGE LINK AFTER";
                        String commentBefore = "COMMENT BEFORE";
                        String commentAfter = "COMMENT AFTER";
                        boolean deficiencyCompletion = false;
                        Date deficiencyDateOfRegistration = new Date();
                        Date deficiencyDateOfCompletion = new Date();
                        Date deficiencyLastUpdated = new Date();

                        DeficiencyInfo deficiencyInfo = new DeficiencyInfo(title, imageLinkBefore, imageLinkAfter,
                                commentBefore, commentAfter, employeeIdOfRegisteration, deficiencyCompletion,
                                deficiencyDateOfRegistration, deficiencyDateOfCompletion, deficiencyLastUpdated);

                        // Upload the new ProjectInfo
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT)
                                .add(projectInfo);

                        // Upload the new DeficiencyInfo
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();
                                        db.collection(COLLECTION_CLIENT).document(clientId)
                                                .collection(COLLECTION_PROJECT).document(projectId)
                                                .collection(COLLECTION_DEFICIENCY).document("10").set(deficiencyInfo);
                                    }
                                });



                    }

                });
    }

    // Add a new red button when screen is touched
    public void addButton(float x, float y) {
        ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.newLayout);
        addedButton = new Button(getActivity());
        addedButton.setText(String.valueOf(this.counter));
        addedButton.setX(x);
        addedButton.setY(y);
        addedButton.setGravity(Gravity.CENTER);
        addedButton.setPadding(0, 0, 0, 10);
        addedButton.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
        addedButton.setTextColor(Color.parseColor("white"));
        addedButton.setBackgroundColor(Color.parseColor("red"));
        addedButton.setId(this.counter);
        addedButton.setOnClickListener(handleOnClick(addedButton));
        addedButton.setOnLongClickListener(listener);

        this.buttonList.add(addedButton);
        this.counter += 1;

        layout.addView(addedButton);
    }

    // Go to deficiency fragment
    public void saveMarkerButtonPressed() {

        Intent intent = new Intent(getActivity(), DeficiencyTabLayoutActivity.class);
        startActivity(intent);

        // switchFragment = new DeficiencyFragment();
        // transaction = getActivity().getSupportFragmentManager().beginTransaction();
        // transaction.replace(R.id.project_layout,
        // switchFragment).addToBackStack(null).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddImagePinViewModel.class);
        // TODO: Use the ViewModel
    }

}