package com.example.wip_android.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.wip_android.R;
import com.example.wip_android.activities.DeficiencyTabLayoutActivity;
import com.example.wip_android.activities.ProjectActivity;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.models.ProjectInfo;
import com.example.wip_android.viewmodels.DeficiencyImageViewViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DeficiencyImageViewFragment extends Fragment {

    // Variables
    private DeficiencyImageViewViewModel mViewModel;
    private ImageView deficiency_imageView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;
    private ImageView imageView;
    private float x, y;
    private String buttonTitle;
    private DeficiencyInfo issueToGet = new DeficiencyInfo();

    public static DeficiencyImageViewFragment newInstance() {
        return new DeficiencyImageViewFragment();
    }

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deficiency_image_view_fragment, container, false);

        // this.deficiency_imageView = view.findViewById(R.id.deficiency_imageView);
        // Glide.with(this).load(this.getArguments().getString("image")).into(deficiency_imageView);
        imageView = (ImageView) view.findViewById(R.id.deficiency_imageView);
        clientName = this.getArguments().getString("name");
        getFirebaseData();

        return view;
    }

    // When the red button is clicked, upload information to Firebase and go to the
    // next screen
    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                // Get important data
                String currentButtonId = String.valueOf(button.getId());
                System.out.println(currentButtonId);
                DeficiencyInfo deficiencyInfo = createDeficiencyInfo(currentButtonId);

                // Update firebase
                updateFirebaseData(currentButtonId, deficiencyInfo);
            }
        };
    }

    // When the red button is long clicked, delete it
    View.OnLongClickListener listener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            System.out.println("LONG CLICK");
            // ConstraintLayout layout = (ConstraintLayout)
            // getActivity().findViewById(R.id.imageViewLayout);
            // Button clickedButton = (Button) v;
            // for (int i = 0; i < buttonList.size(); i++) {
            // if (buttonList.get(i).getId() == clickedButton.getId()) {
            // buttonList.remove(i);
            // break;
            // }
            // }
            // layout.removeView(clickedButton);
            return true;
        }
    };

    private void getDocumentId() {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference deliveryRef = rootRef.collection("Client");
        Query nameQuery = deliveryRef.whereEqualTo("clientName", this.getArguments().getString("name"));
        nameQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String currentClientDocumentID = document.getId();
                        System.out.println(currentClientDocumentID);
                    }
                }
            }
        });
        nameQuery.get().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("ERROR");
            }
        });
    }

    // Create DeficiencyInfo object
    public DeficiencyInfo createDeficiencyInfo(String buttonId) {

        // Images
        String imageLinkBefore = "";
        String imageLinkAfter = "";

        // Comments
        String commentBefore = "";
        String commentAfter = "";

        // Dates
        Date deficiencyDateOfRegistration = new Date();
        Date deficiencyDateOfCompletion = new Date();
        Date deficiencyLastUpdated = new Date();

        // Other
        String title = buttonId;
        String employeeIdOfRegisteration = "";
        boolean deficiencyCompletion = false;

        // Create object
        DeficiencyInfo deficiencyInfo = new DeficiencyInfo(title, imageLinkBefore, imageLinkAfter, commentBefore,
                commentAfter, employeeIdOfRegisteration, deficiencyCompletion, deficiencyDateOfRegistration,
                deficiencyDateOfCompletion, deficiencyLastUpdated);

        return deficiencyInfo;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyImageViewViewModel.class);
    }

    // Upload information to Firebase Database
    public void getFirebaseData() {
        // Check and get current project information
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();
                                        ProjectInfo projectInfo = thirdTask.getResult().getDocuments().get(0)
                                                .toObject(ProjectInfo.class);

                                        Glide.with(this).load(projectInfo.getImageLink()).into(imageView);
                                        imageView.setVisibility(View.VISIBLE);

                                        for (int i = 0; i < projectInfo.getButtonLocationY().size(); i++) {
                                            x = projectInfo.getButtonLocationX().get(i).floatValue();
                                            y = projectInfo.getButtonLocationY().get(i).floatValue();
                                            buttonTitle = projectInfo.getButtonTitle().get(i);
                                            Button addedButton;
                                            ConstraintLayout layout = (ConstraintLayout) getActivity()
                                                    .findViewById(R.id.imageViewLayout);
                                            addedButton = new Button(getActivity());
                                            addedButton.setText(String.valueOf(buttonTitle));
                                            addedButton.setX(x);
                                            addedButton.setY(y);
                                            addedButton.setGravity(Gravity.CENTER);
                                            addedButton.setPadding(0, 0, 0, 10);
                                            addedButton.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
                                            addedButton.setTextColor(Color.parseColor("white"));
                                            addedButton.setBackgroundColor(Color.parseColor("red"));
                                            addedButton.setId(Integer.parseInt(buttonTitle));
                                            addedButton.setOnClickListener(handleOnClick(addedButton));
                                            addedButton.setOnLongClickListener(listener);
                                            layout.addView(addedButton);
                                        }
                                    }
                                });
                    }

                });
    }

    // Upload information to Firebase
    public void updateFirebaseData(String buttonId, DeficiencyInfo deficiencyInfo) {
        // First task
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();

                        // Upload the new DeficiencyInfo
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();

                                        db.collection(COLLECTION_CLIENT).document(clientId)
                                                .collection(COLLECTION_PROJECT).document(projectId)
                                                .collection(COLLECTION_DEFICIENCY).get()
                                                .addOnCompleteListener(lastTask -> {
                                                    if (lastTask.isSuccessful()) {
                                                        for (int i = 0; i < lastTask.getResult().getDocuments()
                                                                .size(); i++) {
                                                            if ((lastTask.getResult().getDocuments().get(i).getId())
                                                                    .equals(buttonId)) {
                                                                issueToGet = lastTask.getResult().getDocuments().get(i)
                                                                        .toObject(DeficiencyInfo.class);
                                                                Intent mainIntent = new Intent(getActivity(),
                                                                        DeficiencyTabLayoutActivity.class);
                                                                mainIntent.putExtra("FROM_ACTIVITY",
                                                                        "DeficiencyImageViewFragment");
                                                                mainIntent.putExtra("buttonNumber", buttonId);
                                                                mainIntent.putExtra("clientName", clientName);
                                                                mainIntent.putExtra("imageLinkBefore",
                                                                        issueToGet.getImageLinkBefore());
                                                                mainIntent.putExtra("imageLinkAfter",
                                                                        issueToGet.getImageLinkAfter());
                                                                mainIntent.putExtra("commentBefore",
                                                                        issueToGet.getCommentBefore());
                                                                mainIntent.putExtra("commentAfter",
                                                                        issueToGet.getCommentAfter());
                                                                startActivity(mainIntent);
                                                                break;
                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

}