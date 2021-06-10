package com.example.wip_android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import com.example.wip_android.R;

import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getCanonicalName();
    private Button btnCreateAccount;
    private TextView tvLogin;
    private EditText edtEmail;
    private EditText edtPassword;
    private EditText edtConfirmPassword;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtEmployeeID;
    private EditText edtPhone;
    private UserViewModel userViewModel;
    private Spinner spnDepartment;
    private String selectedDepartment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        this.userViewModel = UserViewModel.getInstance();


        this.edtEmail = findViewById(R.id.edtEmail);
        this.edtPassword = findViewById(R.id.edtPassword);
        this.edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        this.edtFirstName = findViewById(R.id.edtFirstName);
        this.edtLastName = findViewById(R.id.edtLastName);
        this.edtEmployeeID = findViewById(R.id.edtEmpId);
        this.edtPhone = findViewById(R.id.edtPhone);
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
        departments.add("Waterproof");


        ArrayAdapter<String> departmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments);
        departmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDepartment.setAdapter(departmentAdapter);


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
        newUser.setFirstName(this.edtFirstName.getText().toString());
        newUser.setLastName(this.edtLastName.getText().toString());
        newUser.setEmpID(this.edtEmployeeID.getText().toString());
        newUser.setPhone(this.edtPhone.getText().toString());
        newUser.setDepartment(this.selectedDepartment);
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         this.selectedDepartment = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + selectedDepartment, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
