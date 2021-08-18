package com.example.wip_android.fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.activities.AddProjectActivity;
import com.example.wip_android.activities.ChangePasswordActivity;
import com.example.wip_android.activities.SignInActivity;
import com.example.wip_android.models.User;
import com.example.wip_android.fragments.HomeFragment;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private final String TAG = this.getClass().getCanonicalName();
    private Button btnUpdateAccount;
    private Button btnDeleteAccount;
    private Button btnChangePassword;
    private TextView tvEmail;
    private TextView tvEmployeeID;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private TextInputLayout edtFirstname;
    private TextInputLayout edtLastname;
    private TextInputLayout edtContactNumber;
    private UserViewModel userViewModel;
    private User userInfo;
    private User newUserInfo;
    private String userID;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final String COLLECTION_NAME = "Users";
    private MainActivity mainActivity;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        this.userViewModel = UserViewModel.getInstance();

        this.tvEmail = root.findViewById(R.id.tvUEmail);
        this.tvEmployeeID = root.findViewById(R.id.tvUEmployeeID);
        // this.edtPassword = root.findViewById(R.id.edtUPassword);
        // this.edtConfirmPassword = root.findViewById(R.id.edtUConfirmPassword);
        this.edtFirstname = root.findViewById(R.id.edtUFirstname);
        this.edtLastname = root.findViewById(R.id.edtULastname);
        this.edtContactNumber = root.findViewById(R.id.edtUContactNumber);
        this.btnUpdateAccount = root.findViewById(R.id.btnUpdateAccount);
        this.btnDeleteAccount = root.findViewById(R.id.btnDeleteAccount);
        this.btnChangePassword = root.findViewById(R.id.btnChangePassword);
        this.btnUpdateAccount.setOnClickListener(this);
        this.btnDeleteAccount.setOnClickListener(this);
        this.btnChangePassword.setOnClickListener(this);
        this.clearFields();

//        userID = this.userViewModel.getUserRepository().loggedInUserID.getValue();

        getUpdateUserInfo();

//        userInfo = this.userViewModel.getUpdateUserInfo(userID);


        this.userViewModel.getUserRepository().userDeleteStatus.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if (status.equals("DELETED")) {
                    // Intent intent = new Intent(getActivity(), SignInActivity.class);
                    // startActivity(intent);
                    userViewModel.getUserRepository().userDeleteStatus.postValue("");
                }

            }
        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

    private void clearFields() {
        edtFirstname.getEditText().setText("");
        // edtPassword.setText("");
        // edtConfirmPassword.setText("");
        edtContactNumber.getEditText().setText("");
        edtLastname.getEditText().setText("");
    }

    @Override
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.btnUpdateAccount: {
                    if (this.validateData()) {

                        this.updateUserToDB();

                        Toast.makeText(getActivity(), "Profile Updated Successfully", Toast.LENGTH_LONG).show();

                        // Fragment fragment = new HomeFragment();
                        // FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        // FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        // fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
                        // fragmentTransaction.addToBackStack(null);
                        // fragmentTransaction.commit();
                    }
                    break;
                }
                case R.id.btnChangePassword: {
                    this.navigateToChangePassword();
                    break;
                }
                case R.id.btnDeleteAccount: {
                    Log.d(TAG, "onDelete: looged--In User ID"
                            + userViewModel.getUserRepository().loggedInUserID.getValue());
                    this.userViewModel.deleteUser(userID);
                    this.navigateToHome();
                    break;
                }
                default:
                    break;
            }
        }

    }

    private void navigateToHome() {
        Intent intent = new Intent(getActivity(), SignInActivity.class);
        startActivity(intent);
    }

   private void navigateToChangePassword(){
        Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
        startActivity(intent);
    }

    private Boolean validateData() {

        //
        // if (this.edtPassword.getText().toString().isEmpty()){
        // this.edtPassword.setError("Password cannot be empty");
        // return false;
        // }
        //
        // if (this.edtConfirmPassword.getText().toString().isEmpty()){
        // this.edtConfirmPassword.setError("Please provide confirm password");
        // return false;
        // }
        //
        // if
        // (!edtPassword.getText().toString().equals(this.edtConfirmPassword.getText().toString())){
        // this.edtPassword.setError("Both passwords must be same");
        // this.edtConfirmPassword.setError("Both passwords must be same");
        // return false;
        // }

        if (this.edtContactNumber.getEditText().getText().toString().isEmpty()) {
            this.edtContactNumber.setError("Contact Number cannot be empty");
            return false;
        }
        if (this.edtFirstname.getEditText().getText().toString().isEmpty()) {
            this.edtFirstname.setError("First Name cannot be empty");
            return false;
        }

        if (this.edtLastname.getEditText().getText().toString().isEmpty()) {
            this.edtLastname.setError("Last Name cannot be empty");
            return false;
        }

        return true;
    }

//    private void getAccountInfo() {
////        Log.d(TAG, "onCreateView: user info in the profile fragment: " + userInfo.getEmail());
//
////        if (userInfo != null) {
//            this.tvEmployeeID.setText("Employee ID: " + this.userViewModel.getUserRepository().newUserInfo.getEmpID());
//            this.tvEmail.setText("E-mail: " + this.userViewModel.getUserRepository().newUserInfo.getEmail());
//            this.edtFirstname.getEditText().setText(this.userViewModel.getUserRepository().newUserInfo.getFirstName());
//            // this.edtConfirmPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
//            // this.edtPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
//            this.edtLastname.getEditText().setText(this.userViewModel.getUserRepository().newUserInfo.getLastName());
//            this.edtContactNumber.getEditText().setText(this.userViewModel.getUserRepository().newUserInfo.getPhone());
////        }
//    }

    private void updateUserToDB() {
        User newUser = new User();

        newUser.setEmail(this.tvEmail.getText().toString());
        newUser.setFirstName(this.edtFirstname.getEditText().getText().toString());
        newUser.setLastName(this.edtLastname.getEditText().getText().toString());
        // newUser.setPassword(this.edtPassword.getText().toString());
        newUser.setPhone(this.edtContactNumber.getEditText().getText().toString());

        this.userViewModel.updateUser(newUser);
    }

//Get info from firebase
    public void getUpdateUserInfo() {
        userID = this.userViewModel.getUserRepository().loggedInUserID.getValue();

        db.collection(COLLECTION_NAME).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null) {

                                newUserInfo = documentSnapshot.toObject(User.class);
                                getUserValues();
                            }

                        } else {

                        }
                    }
                });

    }

    private void getUserValues() {

        this.tvEmployeeID.setText("Employee ID: " + newUserInfo.getEmpID());
        this.tvEmail.setText("E-mail: " + newUserInfo.getEmail());
        this.edtFirstname.getEditText().setText(newUserInfo.getFirstName());
        // this.edtConfirmPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
        // this.edtPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
        this.edtLastname.getEditText().setText(newUserInfo.getLastName());
        this.edtContactNumber.getEditText().setText(newUserInfo.getPhone());

    }
}