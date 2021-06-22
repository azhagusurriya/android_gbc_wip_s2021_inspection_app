package com.example.wip_android.fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.SignInActivity;
import com.example.wip_android.activities.SignUpActivity;
import com.example.wip_android.viewmodels.HomeViewModel;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FloatingActionButton fabAddProject;
    private final String TAG = this.getClass().getCanonicalName();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_home, container, false);

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



    private void navigateToAddProject(){
        Log.d(TAG, "onClick Add Project: Intend to addproject activity ");
        Intent intent = new Intent(getActivity(), AddProjectActivity.class);
        startActivity(intent);
    }
}

