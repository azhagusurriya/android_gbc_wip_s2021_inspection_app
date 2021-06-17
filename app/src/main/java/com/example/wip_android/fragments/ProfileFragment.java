package com.example.wip_android.fragments;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.wip_android.models.User;
import com.example.wip_android.ui.home.HomeFragment;
import com.example.wip_android.viewmodels.ProfileViewModel;
import com.example.wip_android.viewmodels.UserViewModel;

import java.util.Objects;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;
    private final String TAG = this.getClass().getCanonicalName();
    private Button btnUpdateAccount;
    private Button btnDeleteAccount;
    private Button btnGetAccountInfo;
    private TextView tvEmail;
    private TextView tvEmployeeID;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtFirstname;
    private EditText edtLastname;
    private EditText edtContactNumber;
    private UserViewModel userViewModel;
    private User userInfo;
    private String userID;
    private MainActivity mainActivity;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_profile, container, false);


        this.userViewModel = UserViewModel.getInstance();

        this.tvEmail = root.findViewById(R.id.tvUEmail);
        this.edtPassword = root.findViewById(R.id.edtUPassword);
        this.edtConfirmPassword = root.findViewById(R.id.edtUConfirmPassword);
        this.edtFirstname = root.findViewById(R.id.edtUFirstname);
        this.edtLastname = root.findViewById(R.id.edtULastname);
        this.edtContactNumber = root.findViewById(R.id.edtUContactNumber);
        this.btnUpdateAccount = root.findViewById(R.id.btnUpdateAccount);
        this.btnDeleteAccount = root.findViewById(R.id.btnDeleteAccount);
        this.btnGetAccountInfo = root.findViewById(R.id.btnGetAccountInfo);
        this.btnUpdateAccount.setOnClickListener(this);
        this.btnDeleteAccount.setOnClickListener(this);
        this.btnGetAccountInfo.setOnClickListener(this);
        this.clearFields();

        userID = this.userViewModel.getUserRepository().loggedInUserID.getValue();
        userInfo =  this.userViewModel.getUpdateUserInfo(userID);


        this.userViewModel.getUserRepository().userDeleteStatus.observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("DELETED")){
                    mainActivity.logout();
                    /*Intent i=new Intent((DashboardMainActivity) getActivity(),SignInActivity.class);
                    startActivity(i);*/
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



    private void clearFields(){
        edtFirstname.setText("");
        edtPassword.setText("");
        edtConfirmPassword.setText("");
        edtContactNumber.setText("");
        edtLastname.setText("");
    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.btnUpdateAccount:{
                    if (this.validateData()) {

                        this.updateUserToDB();

                        Toast.makeText(getActivity(),"Profile Updated Successfully",Toast.LENGTH_LONG).show();

//                        Fragment fragment = new HomeFragment();
//                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                        fragmentTransaction.replace(R.id.nav_host_fragment_content_main, fragment);
//                        fragmentTransaction.addToBackStack(null);
//                        fragmentTransaction.commit();
                    }
                    break;
                }
                case R.id.btnGetAccountInfo:{
                    this.getAccountInfo();
                    break;
                }
                case R.id.btnDeleteAccount:{
                    this.userViewModel.deleteUser(userID);
                    //System.exit(0);

                    //dashboardMainActivity.logout();
                }
                default:
                    break;
            }
        }

    }

    private Boolean validateData(){



        if (this.edtPassword.getText().toString().isEmpty()){
            this.edtPassword.setError("Password cannot be empty");
            return false;
        }

        if (this.edtConfirmPassword.getText().toString().isEmpty()){
            this.edtConfirmPassword.setError("Please provide confirm password");
            return false;
        }

        if (!edtPassword.getText().toString().equals(this.edtConfirmPassword.getText().toString())){
            this.edtPassword.setError("Both passwords must be same");
            this.edtConfirmPassword.setError("Both passwords must be same");
            return false;
        }

        if (this.edtContactNumber.getText().toString().isEmpty()){
            this.edtContactNumber.setError("Contact Number cannot be empty");
            return false;
        }
        if (this.edtFirstname.getText().toString().isEmpty()){
            this.edtFirstname.setError("First Name cannot be empty");
            return false;
        }

        if (this.edtLastname.getText().toString().isEmpty()){
            this.edtLastname.setError("Last Name cannot be empty");
            return false;
        }

        return true;
    }

    private void getAccountInfo(){
        Log.d(TAG, "onCreateView: user info in the profile fragment: " +userInfo.getEmail());

        if(userInfo != null) {
            this.tvEmail.setText("E-mail: " + this.userViewModel.getUserRepository().newUserInfo.getEmail());
            this.edtFirstname.setText(this.userViewModel.getUserRepository().newUserInfo.getFirstName());
            this.edtConfirmPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
            this.edtPassword.setText(this.userViewModel.getUserRepository().newUserInfo.getPassword());
            this.edtLastname.setText(this.userViewModel.getUserRepository().newUserInfo.getLastName());
            this.edtContactNumber.setText(this.userViewModel.getUserRepository().newUserInfo.getPhone());
        }
    }

    private void updateUserToDB(){
        User newUser = new User();

        newUser.setEmail(this.tvEmail.getText().toString());
        newUser.setFirstName(this.edtFirstname.getText().toString());
        newUser.setLastName(this.edtLastname.getText().toString());
        newUser.setPassword(this.edtPassword.getText().toString());
        newUser.setPhone(this.edtContactNumber.getText().toString());

        this.userViewModel.updateUser(newUser);
    }
}