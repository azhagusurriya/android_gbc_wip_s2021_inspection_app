package com.example.wip_android.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.wip_android.repositories.AddProjectRepository;
import com.example.wip_android.repositories.UserRepository;

public class AddProjectViewModel extends ViewModel {

    private final String TAG = this.getClass().getCanonicalName();
    private static final AddProjectViewModel ourInstance = new AddProjectViewModel();
    private final AddProjectRepository addProjectRepository = new AddProjectRepository();


    public static AddProjectViewModel getInstance(){
        return ourInstance;
    }

    private AddProjectViewModel(){
    }

    public AddProjectRepository getProjectRepository(){
        return addProjectRepository;
    }





}
