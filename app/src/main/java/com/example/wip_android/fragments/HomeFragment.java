package com.example.wip_android.fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
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
    private RecyclerView recyclerView;
    HomeAdapter recyclerAdapter;
    List<String> homeList;

    // Default function
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate fragment
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

        // Home List
        homeList = new ArrayList<>();
        homeList.add("Project 1");
        homeList.add("Project 2");
        homeList.add("Project 3");
        homeList.add("Project 4");

        // Home Recycler View
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerAdapter = new HomeAdapter(homeList, this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

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
    private void navigateToAddProject(){
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


}

