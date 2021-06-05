package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.example.wip_android.R;

import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getCanonicalName();
    private Button btnCreateAccount;
    private TextView tvLogin;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtName;
    private UserViewModel userViewModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.userViewModel = UserViewModel.getInstance();


        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtPassword = findViewById(R.id.edtPassword);
        this.edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        this.edtName = findViewById(R.id.edtName);
        this.btnCreateAccount = findViewById(R.id.btnCreateAccount);
        this.btnCreateAccount.setOnClickListener(this);
        this.tvLogin = findViewById(R.id.tvLogin);
        this.tvLogin.setOnClickListener(this);

        this.userViewModel.getUserRepository().userExistStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("NOT EXIST")){
                    //save data to database
                    saveUserToDB();

                    goToMain();
                    userViewModel.getUserRepository().userExistStatus.postValue("");

                }else if (status.equals("EXIST")){

                    Log.d(TAG, "onChanged: User Already Exist");
                    edtEmail.setError("User Already Exist");
                    userViewModel.getUserRepository().userExistStatus.postValue("");
                }
            }
        });

    }

    @Override
    public void onClick(View view) {

        if( view != null){
            switch (view.getId()){
                case R.id.tvLogin:{
                    Intent signUpIntent = new Intent(this, SignInActivity.class);
                    startActivity(signUpIntent);
                    break;
                }
                case R.id.btnCreateAccount: {
                    if (this.validateData()){

                        //check user already exist
                        this.checkUser();

                    }
                }

                default:
                    break;
            }
        }

    }

    private void checkUser(){
        this.userViewModel.checkUser(this.edtEmail.getText().toString());

    }

    private void saveUserToDB(){
        User newUser = new User();

        newUser.setEmail(this.edtEmail.getText().toString());
        newUser.setName(this.edtName.getText().toString());
        newUser.setPassword(this.edtPassword.getText().toString());


        this.userViewModel.addUser(newUser);
    }

    private void goToMain(){
        this.finish();
        Intent mainIntent = new Intent(this, SignInActivity.class);
        startActivity(mainIntent);
    }



    private Boolean validateData(){
        if (this.edtEmail.getText().toString().isEmpty()){
            this.edtEmail.setError("Please enter email");
            return false;
        }


        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(this.edtEmail.getText().toString()).matches()) {
            this.edtEmail.setError("Please enter a valid email");
            return false;
        }


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

        return true;
    }


}
