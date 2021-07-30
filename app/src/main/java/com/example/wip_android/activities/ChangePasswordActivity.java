package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.wip_android.R;
import com.google.android.material.textfield.TextInputLayout;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{


    private TextInputLayout edtChangePassword, edtConfirmChangePassword;
    private Button btnConfirmChangePassword;
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        this.btnConfirmChangePassword = findViewById(R.id.btnConfirmChangePassword);
        this.btnConfirmChangePassword.setOnClickListener(this);

        this.edtChangePassword = findViewById(R.id.edtChangePassword);
        this.edtConfirmChangePassword = findViewById(R.id.edtConfirmChangePassword);
        
        
    }

    @Override
    public void onClick(View v) {
        if(this.validateData()){

            this.changePassword();
        }
    }

    private void changePassword() {
        Log.d(TAG, "changePassword: Password field check ");
        
    }

    private boolean validateData() {

         if (this.edtChangePassword.getEditText().getText().toString().isEmpty()){
         this.edtChangePassword.setError("Password cannot be empty");
         return false;
         }

         if (this.edtConfirmChangePassword.getEditText().getText().toString().isEmpty()){
         this.edtConfirmChangePassword.setError("Please provide confirm password");
         return false;
         }

         if
         (!edtChangePassword.getEditText().getText().toString().equals(this.edtConfirmChangePassword.getEditText().getText().toString())){
         this.edtChangePassword.setError("Both passwords must be same");
         this.edtConfirmChangePassword.setError("Both passwords must be same");
         return false;
         }
         return true;
    }


   
    
}