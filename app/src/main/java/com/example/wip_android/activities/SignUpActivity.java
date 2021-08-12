package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;

import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getCanonicalName();
    private Button btnCreateAccount;
    private TextView tvLogin;
    private TextInputLayout edtEmail;
    private TextInputLayout edtPassword;
    private TextInputLayout edtConfirmPassword;
    private TextInputLayout edtFirstName;
    private TextInputLayout edtLastName;
    private TextInputLayout edtEmployeeID;
    private TextInputLayout tfDepartment;
    private TextInputLayout edtPhone;
    private UserViewModel userViewModel;
    private AutoCompleteTextView spnDepartment;
    private String selectedDepartment;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
//            actionBar.setTitle("Login");
//            ColorDrawable colorDrawable
//                    = new ColorDrawable(Color.parseColor("#ffffff"));
//            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Create Account </font>"));
        }

        // Back button
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button


        this.userViewModel = UserViewModel.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtPassword = findViewById(R.id.edtPassword);
        this.edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        this.edtFirstName = findViewById(R.id.edtFirstName);
        this.edtLastName = findViewById(R.id.edtLastName);
        this.edtEmployeeID = findViewById(R.id.edtEmpId);
        this.edtPhone = findViewById(R.id.edtPhone);
        this.tfDepartment = findViewById(R.id.tfDepartment);
        this.btnCreateAccount = findViewById(R.id.btnCreateAccount);
        this.btnCreateAccount.setOnClickListener(this);
        this.tvLogin = findViewById(R.id.tvLogin);
        this.tvLogin.setOnClickListener(this);
        this.spnDepartment = findViewById(R.id.spnDepartment);
        this.spnDepartment.setOnItemSelectedListener(this);

        List<String> departments = new ArrayList<String>();
        departments.add("Cladding");
        departments.add("Glazing");
        departments.add("Service");
        departments.add("Waterproofing");
        departments.add("Admin");
        departments.add("Production");
        departments.add("Estimating");
        departments.add("Purchasing");
        departments.add("Mechanic");


        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this, R.layout.department_dropdown_item, departments);
        spnDepartment.setAdapter(departmentAdapter);


        this.userViewModel.getUserRepository().userExistStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("NOT EXIST")){

                    //save data to database
                    saveUserToDB();
                    //goToMain();
                    //userViewModel.getUserRepository().userExistStatus.postValue("");

                }
                else if (status.equals("EMPLOYEEID EXIST")){

                    Log.d(TAG, "onChanged: Employee ID Already Exist");
                    edtEmployeeID.setError("Employee ID Already Exist");
                    userViewModel.getUserRepository().userExistStatus.postValue("");
                }
            }
        });

        this.userViewModel.getUserRepository().userAuthEmailStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("NOT EXIST")){
                    goToMain();
                    userViewModel.getUserRepository().userExistStatus.postValue("");
                    userViewModel.getUserRepository().userAuthEmailStatus.postValue("");
                }
                else if (status.equals("EMAIL EXIST")){

                    Log.d(TAG, "onChanged: Auth Email Already Exist");
                    edtEmail.setError("Email Already Exist");
                    userViewModel.getUserRepository().userAuthEmailStatus.postValue("");
                }
                else if (status.equals("WEAK PASSWORD")){

                    Log.d(TAG, "onChanged: Weak password (minimum 6 characters)");
                    edtPassword.setError("Weak Password (Min 6 characters)");
                    userViewModel.getUserRepository().userAuthEmailStatus.postValue("");
                }
            }
        });

    }


    // Going back to previous activity
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
        this.userViewModel.checkUser(this.edtEmail.getEditText().getText().toString(),this.edtEmployeeID.getEditText().getText().toString());
    }

    private void saveUserToDB(){

        User newUser = new User();

        newUser.setEmail(this.edtEmail.getEditText().getText().toString());
        newUser.setFirstName(this.edtFirstName.getEditText().getText().toString());
        newUser.setLastName(this.edtLastName.getEditText().getText().toString());
        newUser.setEmpID(this.edtEmployeeID.getEditText().getText().toString());
        newUser.setPhone(this.edtPhone.getEditText().getText().toString());
        newUser.setDepartment(this.spnDepartment.getText().toString());

       // createAuthUser(this.edtEmail.getText().toString(),this.edtPassword.getText().toString());
        this.userViewModel.createAuthUser(newUser,this.edtPassword.getEditText().getText().toString());
    }

    private void goToMain(){
        this.finish();
        Intent mainIntent = new Intent(this, SignInActivity.class);
        startActivity(mainIntent);
    }

    private void createAuthUser(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication successful!", Toast.LENGTH_LONG).show();
                           // this.userViewModel.addUser(newUser);
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Authentication failed!", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onComplete: " +  task.getException());

                        }
                    }
                });
    }

    private Boolean validateData(){
        if (this.edtEmail.getEditText().getText().toString().isEmpty()){
            this.edtEmail.setError("Please enter email");
            return false;
        }

        if (this.edtEmployeeID.getEditText().getText().toString().isEmpty()){
            this.edtEmployeeID.setError("Please enter a employee Id");
            return false;
        }


        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(this.edtEmail.getEditText().getText().toString()).matches()) {
            this.edtEmail.setError("Please enter a valid email");
            return false;
        }


        if (this.edtPassword.getEditText().getText().toString().isEmpty()){
            this.edtPassword.setError("Password cannot be empty");
            return false;
        }

        if (this.edtConfirmPassword.getEditText().getText().toString().isEmpty()){
            this.edtConfirmPassword.setError("Please provide confirm password");
            return false;
        }

        if (!edtPassword.getEditText().getText().toString().equals(this.edtConfirmPassword.getEditText().getText().toString())){
            this.edtPassword.setError("Both passwords must be same");
            this.edtConfirmPassword.setError("Both passwords must be same");
            return false;
        }

        if (this.edtConfirmPassword.getEditText().getText().toString().isEmpty()){
            this.edtConfirmPassword.setError("Please provide confirm password");
            return false;
        }

        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         this.selectedDepartment = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + selectedDepartment, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
