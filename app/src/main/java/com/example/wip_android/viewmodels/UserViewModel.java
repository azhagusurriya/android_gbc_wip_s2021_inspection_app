package com.example.wip_android.viewmodels;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.wip_android.models.User;
import com.example.wip_android.repositories.UserRepository;

public class UserViewModel extends ViewModel {

    private final String TAG = this.getClass().getCanonicalName();
    private static final UserViewModel ourInstance = new UserViewModel();
    private final UserRepository userRepository = new UserRepository();

    public static UserViewModel getInstance(){
        return ourInstance;
    }

    private UserViewModel(){
    }

    public void addUser(User user){
        this.userRepository.addUser(user);
    }
    public UserRepository getUserRepository(){
        return userRepository;
    }


    public void validateUser(String email, String password){
        this.userRepository.getUser(email, password);
    }

    public void updateUser(User user){
        this.userRepository.updateUser(user);
    }

    public void deleteUser(String userID){
        this.userRepository.deleteUser(userID);
    }

    public void checkUser(String email){
        this.userRepository.checkUser(email);
    }

    public User getUpdateUserInfo(String userID){
        User user =  this.userRepository.getUpdateUserInfo(userID);
        Log.d(TAG, "getUpdateUserInfo: User info in view model: " + user.getEmail());
        return user;
    }


}
