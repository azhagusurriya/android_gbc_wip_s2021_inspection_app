package com.example.wip_android.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.DeficiencyActivity;
import com.example.wip_android.activities.ProjectActivity;
import com.example.wip_android.activities.SignInActivity;
import com.example.wip_android.activities.SignUpActivity;
import com.example.wip_android.adapters.GlossaryAdapter;
import com.example.wip_android.adapters.HomeAdapter;
import com.example.wip_android.adapters.ProjectAdapter;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.viewmodels.HomeListViewModel;
import com.example.wip_android.viewmodels.HomeViewModel;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.HomeViewModel;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.example.wip_android.viewmodels.UserViewModel;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.onNoteListener {

    // Variables
    private HomeViewModel mViewModel;
    private FloatingActionButton fabAddProject;
    private final String TAG = this.getClass().getCanonicalName();
    private RecyclerView homeRecyclerView;
    private HomeAdapter homeRecyclerAdapter;
    private List<String> homeList;
    private String department = "Admin";
    public List<ClientInfo> clientInfoList;
    private final String COLLECTION_NAME = "Client";
    private final String COLLECTION_NAME_USER = "Users";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> homeTitleList;
    private List<String> homeSubtitleList;
    private ClientInfo chosenItem;
     UserViewModel userViewModel;
     User LoggedInUserInfo;
     String logInUserID;
     String loggedInUserDepartment;
     String currentUserId;
     String currentUserEmail;
     String currentUserDepartment;

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Inflate fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Get glossary data from Firebase
        this.userViewModel = UserViewModel.getInstance();

//        logInUserID = this.userViewModel.getUserRepository().loggedInUserID.getValue();
//        Log.d(TAG, "onCreateView: LoggediN USER id: " + logInUserID);
//
//        LoggedInUserInfo =  this.userViewModel.getUpdateUserInfo(logInUserID);
//        loggedInUserDepartment = LoggedInUserInfo.getDepartment();
//        Log.d(TAG, "onCreate: logged In user department :" + loggedInUserDepartment);

        // Home List
//        this.homeList = new ArrayList<>();
//        this.homeList.add("Project 1");
//        this.homeList.add("Project 2");
//        this.homeList.add("Project 3");
//        this.homeList.add("Project 4");

        // Home Recycler View
        this.homeRecyclerView = root.findViewById(R.id.homeRecyclerView);

        SharedPreferences preferences = this.getActivity().getSharedPreferences("CurrentUserEmail", Context.MODE_PRIVATE);
        currentUserEmail = preferences.getString("email", "No email defined");//"No name defined" is the default value.
        currentUserDepartment = preferences.getString("department", "No department defined");


        Log.d(TAG, "onCreateView: email from sign in page:" + currentUserEmail);

//        this.getUser(currentUserEmail);

//        Log.d(TAG, "onCreateView: logged in user id :" + logInUserID);

       Log.d(TAG, "onCreateView: Current user department in Home" + currentUserDepartment);

//        this.getUpdateUserInfo(logInUserID);
//        Log.d(TAG, "onCreateView: department of logged in user" + LoggedInUserInfo.getDepartment());

        this.refreshHomeRecyclerView();

        // Floating Button
        fabAddProject = (FloatingActionButton) root.findViewById(R.id.fabAddProject);
        fabAddProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToAddProject();
            }
        });

        return root;
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel

    }

    // Add Project Floating Button
    private void navigateToAddProject() {
        Log.d(TAG, "onClick Add Project: Intend to addproject activity ");
        Intent intent = new Intent(getActivity(), AddProjectActivity.class);
        startActivity(intent);
    }

    // When Recycler View item is clicked do something
    @Override
    public void onNoteClick(int position) {
        String address = this.homeRecyclerAdapter.getHomeList().get(position).getClientStreetAddress();
        String name = this.homeRecyclerAdapter.getHomeList().get(position).getClientName();
        Intent intent = new Intent(getActivity(), ProjectActivity.class);
        intent.putExtra("address", address);
        intent.putExtra("name", name);
        intent.putExtra("FROM_ACTIVITY", "HomeFragment");
        startActivity(intent);
    }

    // SearchView settings
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menu.findItem(R.id.home_action_search).setVisible(true);
        MenuItem menuItem = menu.findItem(R.id.home_action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                homeRecyclerAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    // Grab and refresh data from Firebase
    public void refreshHomeRecyclerView() {
        // Get glossary from Firebase
        db.collection(COLLECTION_NAME)
                .whereEqualTo("department", currentUserDepartment)
                .get()
                .addOnCompleteListener(task -> {
                // Check task
                if (task.isSuccessful()){
                    if (task.getResult().getDocuments().size() != 0) {
                        List<ClientInfo> clientInfoList = task.getResult().toObjects(ClientInfo.class);
                    // Convert list to custom class
                    this.homeTitleList = new ArrayList<>();
                    this.homeSubtitleList = new ArrayList<>();
                    for (int i = 0; i < clientInfoList.size(); i++) {
                        this.homeTitleList.add(clientInfoList.get(i).getClientStreetAddress());
                        this.homeSubtitleList.add(clientInfoList.get(i).getClientName());
                    }
                    // Display on recycler view
                    this.homeRecyclerAdapter = new HomeAdapter(this, clientInfoList);
                    this.homeRecyclerView.setAdapter(this.homeRecyclerAdapter);
                    this.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
                    this.homeRecyclerView.addItemDecoration(dividerItemDecoration);
                    this.chosenItem = this.homeRecyclerAdapter.getChosenItem();
                    this.homeList = this.homeTitleList;
                }
            }
        });
    }

    public void getUser(String email){

        try{
            db.collection(COLLECTION_NAME_USER)
                    .whereEqualTo("email", email)
                    // .whereEqualTo("password",password)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){

                                if (task.getResult().getDocuments().size() != 0){

                                    //get the id of the current user logged in
                                    logInUserID = task.getResult().getDocuments().get(0).getId();
                                    currentUserDepartment = task.getResult().getDocuments().get(0).getString("department");
                                    Log.d(TAG, "Logged in user document ID: " +logInUserID);
                                    Log.d(TAG, "Logged in user department : " +currentUserDepartment);

                                }
                                else{
                                    Log.d(TAG, "onComplete: Document generation failed in Home fragment");
                                }
                            }else{
                                Log.e(TAG, "Error fetching document" + task.getException());

                            }
                        }
                    });
        }catch (Exception ex){
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }

    }

//    Get user info
    public void getUpdateUserInfo(String userID){

        db.collection(COLLECTION_NAME_USER)
                .document(userID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if(documentSnapshot !=null){

                                LoggedInUserInfo = documentSnapshot.toObject(User.class);


                            }

                        }
                        else{
                            Log.d(TAG, "onComplete: some error retreiving user info");
                        }
                    }
                });

//        return LoggedInUserInfo;
    }



}
