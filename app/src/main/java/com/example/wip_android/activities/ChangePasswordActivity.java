package com.example.wip_android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener{


    private TextInputLayout edtChangePassword, edtConfirmChangePassword, edtCurrentPassword;
    private Button btnConfirmChangePassword;
    private final String TAG = this.getClass().getCanonicalName();
    private String currentPassword , newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);



        // Action bar settings
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Change Password</font>"));
        }

        // Back button
        assert getSupportActionBar() != null; // null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button


        this.btnConfirmChangePassword = findViewById(R.id.btnConfirmChangePassword);
        this.btnConfirmChangePassword.setOnClickListener(this);

        this.edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        this.edtChangePassword = findViewById(R.id.edtChangePassword);
        this.edtConfirmChangePassword = findViewById(R.id.edtConfirmChangePassword);
        
        
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        if(this.validateData()){

            this.changePassword();
        }
    }

    private void changePassword() {
        Log.d(TAG, "changePassword: Password field check ");


        currentPassword = this.edtCurrentPassword.getEditText().getText().toString();
         newPassword = this.edtChangePassword.getEditText().getText().toString();
         FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        final String email = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(email,currentPassword);

        user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                Log.d(TAG, "onComplete: Authentication Not successful");
                                Toast.makeText(getApplicationContext(), "Change Password Failed!", Toast.LENGTH_LONG).show();
                            }else {
                                Log.d(TAG, "onComplete: Authentication  successful");
                                Toast.makeText(getApplicationContext(), "Change Password successful!", Toast.LENGTH_LONG).show();
                                goToHome();
                            }
                        }
                    });
                }else {
                    edtChangePassword.setError("");
                    edtConfirmChangePassword.setError("");
                    Log.d(TAG, "onComplete: Authentication failed");
                    edtCurrentPassword.setError("Invalid password");

                }
            }
        });
    }

    private void goToHome(){
        this.finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }

    private boolean validateData() {

        if (this.edtCurrentPassword.getEditText().getText().toString().isEmpty()){
            this.edtCurrentPassword.setError("Please provide ur current password");
            return false;
        }

         if (this.edtChangePassword.getEditText().getText().toString().isEmpty()){
         this.edtChangePassword.setError("Please provide new password");
         return false;
         }

         if (this.edtConfirmChangePassword.getEditText().getText().toString().isEmpty()){
         this.edtConfirmChangePassword.setError("Please confirm ur password");
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