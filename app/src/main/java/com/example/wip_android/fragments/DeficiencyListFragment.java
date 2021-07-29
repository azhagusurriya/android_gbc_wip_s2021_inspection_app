package com.example.wip_android.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.example.wip_android.R;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.models.ProjectInfo;
import com.example.wip_android.viewmodels.DeficiencyListViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeficiencyListFragment extends Fragment {

    // Variables
    private DeficiencyListViewModel mViewModel;
    String[] arrayDeficiency = {"1 Test", "2 Test", "3 Test"};
    ArrayAdapter<String> adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;

    public static DeficiencyListFragment newInstance() {
        return new DeficiencyListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deficiency_list_fragment, container, false);

        clientName = this.getArguments().getString("name");
        System.out.println(clientName);
        getDeficiencyFromFirebase();

        ListView listView = (ListView) view.findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_multiple_choice,
                arrayDeficiency);
        listView.setAdapter(adapter);

        return view;
    }

    // Upload information to Firebase Database
    public void getDeficiencyFromFirebase() {
        // Check and get current project information
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();
                        System.out.println("CLIENT ID: " + clientId);
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();
                                        ProjectInfo projectInfo = thirdTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class);

                                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).document(projectId)
                                                .collection(COLLECTION_DEFICIENCY).get()
                                                .addOnCompleteListener(newTask -> {
                                                    if (newTask.isSuccessful()) {
                                                        for (int i = 0; i < newTask.getResult().getDocuments().size(); i++) {
                                                            String newId = newTask.getResult().getDocuments().get(i).getId();
                                                            System.out.println("NEW ID: " + newId);
                                                            System.out.println("------------------------");
//                                                            System.out.println(db.collection(COLLECTION_CLIENT)
//                                                                    .document(clientId)
//                                                                    .collection(COLLECTION_PROJECT)
//                                                                    .document(projectId)
//                                                                    .collection(COLLECTION_DEFICIENCY)
//                                                                    .document(newId));

                                                            System.out.println("COMMENT: "+newTask.
                                                                    getResult().
                                                                    getDocuments().
                                                                    get(i).toObject(DeficiencyInfo.class).getCommentBefore());
                                                        }

//                                                        ProjectInfo projectInfo = newTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class);
//                                                        System.out.println("NEW ID: " + newTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class));
                                                    }
                                                    });

//                                        System.out.println(db.collection(COLLECTION_CLIENT).document(clientId)
//                                                .collection(COLLECTION_PROJECT).document(projectId)
//                                                .collection(COLLECTION_DEFICIENCY).get());



                                    }
                                });
                    }

                });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyListViewModel.class);
        // TODO: Use the ViewModel
    }

}