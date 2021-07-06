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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.SignInActivity;
import com.example.wip_android.activities.SignUpActivity;
import com.example.wip_android.adapters.HomeAdapter;
import com.example.wip_android.adapters.ProjectAdapter;
import com.example.wip_android.viewmodels.HomeViewModel;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    // Default function
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        // Inflate fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        // Home List
        this.homeList = new ArrayList<>();
        this.homeList.add("Project 1");
        this.homeList.add("Project 2");
        this.homeList.add("Project 3");
        this.homeList.add("Project 4");

        // Home Recycler View
        this.homeRecyclerView = root.findViewById(R.id.homeRecyclerView);
        this.homeRecyclerAdapter = new HomeAdapter(this.homeList, this);
        this.homeRecyclerView.setAdapter(this.homeRecyclerAdapter);
        this.homeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL);
        this.homeRecyclerView.addItemDecoration(dividerItemDecoration);

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
        String test = homeList.get(position);
        System.out.println(test);
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

}
