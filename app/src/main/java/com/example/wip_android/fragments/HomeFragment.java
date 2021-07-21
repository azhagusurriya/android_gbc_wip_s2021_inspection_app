package com.example.wip_android.fragments;

import android.content.Intent;
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

import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.DeficiencyTabLayoutActivity;
import com.example.wip_android.activities.ProjectActivity;
import com.example.wip_android.activities.ProjectListItemActivity;
import com.example.wip_android.adapters.HomeAdapter;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.viewmodels.HomeViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.example.wip_android.models.User;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment implements HomeAdapter.onNoteListener {

    // Variables
    private HomeViewModel mViewModel;
    private FloatingActionButton fabAddProject;
    private final String TAG = this.getClass().getCanonicalName();
    private RecyclerView homeRecyclerView;
    private HomeAdapter homeRecyclerAdapter;
    private List<String> homeList;
    public List<ClientInfo> clientInfoList;
    private final String COLLECTION_NAME = "Clients";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<String> homeTitleList;
    private List<String> homeSubtitleList;
    private ClientInfo chosenItem;
    private String currentUserEmail;
    private FirebaseUser firebaseUser;

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Main settings
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        this.homeRecyclerView = root.findViewById(R.id.homeRecyclerView);

        // Display RecyclerView with projects that have the same user department
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            this.currentUserEmail = firebaseUser.getEmail();
            displayRecyclerView(this.currentUserEmail);
        }

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

    // When RecyclerView item is clicked do something
    @Override
    public void onNoteClick(int position) {
        String address = this.homeRecyclerAdapter.getHomeList().get(position).getClientStreetAddress();
        String name = this.homeRecyclerAdapter.getHomeList().get(position).getClientName();
        String city = this.homeRecyclerAdapter.getHomeList().get(position).getClientCity();
        String province = this.homeRecyclerAdapter.getHomeList().get(position).getClientProvince();
        String image = this.homeRecyclerAdapter.getHomeList().get(position).getClientImage();
        String phone = this.homeRecyclerAdapter.getHomeList().get(position).getClientPhoneNumber();
        Intent intent = new Intent(getActivity(), ProjectListItemActivity.class);

        intent.putExtra("address", address);
        intent.putExtra("name", name);
        intent.putExtra("city", city);
        intent.putExtra("province", province);
        intent.putExtra("image", image);
        intent.putExtra("phone", phone);
        // intent.putExtra("FROM_ACTIVITY", "HomeFragment");
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
    public void refreshHomeRecyclerView(String department) {
        db.collection(COLLECTION_NAME).whereEqualTo("department", department).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().getDocuments().size() != 0) {
                    List<ClientInfo> clientInfoList = task.getResult().toObjects(ClientInfo.class);
                    // Sorted list to descending order
                    Collections.sort(clientInfoList, Collections.reverseOrder(new Comparator<ClientInfo>() {
                        @Override
                        public int compare(ClientInfo o1, ClientInfo o2) {
                            return o1.getDateOfRegistration().compareTo(o2.getDateOfRegistration());
                        }
                    }));

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
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                            DividerItemDecoration.VERTICAL);
                    this.homeRecyclerView.addItemDecoration(dividerItemDecoration);
                    this.chosenItem = this.homeRecyclerAdapter.getChosenItem();
                    this.homeList = this.homeTitleList;
                }
            }
        });
    }

    // Display RecyclerView according to user department
    public void displayRecyclerView(String email) {
        db.collection("Users").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().getDocuments().size() != 0) {
                                String department = task.getResult().getDocuments().get(0).toObject(User.class)
                                        .getDepartment();
                                refreshHomeRecyclerView(department);
                            }
                        }
                    }
                });
    }

}
