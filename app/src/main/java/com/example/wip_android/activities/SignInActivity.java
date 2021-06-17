package com.example.wip_android.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;
import com.example.wip_android.databinding.ActivityMainBinding;
import com.example.wip_android.models.User;
import com.example.wip_android.viewmodels.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = this.getClass().getCanonicalName();
    private TextView tvCreateAccount;
    private EditText edtEmail;
    private EditText edtPassword;
    private Button btnSignIn;
    private ProgressBar progressBar;
    private UserViewModel userViewModel;
    private User userInfo;
    private String userID;
    private FirebaseAuth mAuth;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
//            actionBar.setTitle("Login");
//            ColorDrawable colorDrawable
//                    = new ColorDrawable(Color.parseColor("#ffffff"));
//            actionBar.setBackgroundDrawable(colorDrawable);
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Login </font>"));
        }


        setContentView(R.layout.activity_sign_in);
        this.mAuth = FirebaseAuth.getInstance();


        this.userViewModel = UserViewModel.getInstance();
//
        this.tvCreateAccount = findViewById(R.id.tvCreateAccount);
        this.tvCreateAccount.setOnClickListener(this);

        this.edtEmail = findViewById(R.id.edtEmail);
//        this.edtEmail.setText("s@gmail.com");
        this.edtPassword = findViewById(R.id.edtPassword);
//        this.edtPassword.setText("s");

        this.btnSignIn = findViewById(R.id.btnSignIn);
        this.btnSignIn.setOnClickListener(this);

        this.progressBar = findViewById(R.id.progressBar);

        this.userViewModel.getUserRepository().signInStatus.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String status) {
                if(status.equals("SUCCESS")){
                    progressBar.setVisibility(View.INVISIBLE);
                    goToHome();
                }else if (status.equals("LOADING")){
                    progressBar.setVisibility(View.VISIBLE);
                }else if(status.equals("FAILURE")){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: Error Login (Might be document load issue)");
                }else if(status.equals("Nothing")){
                    Log.d(TAG, "onChanged: Successfully Logged Out");
                }
                else if(status.equals("INVALID PASSWORD")){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: password invalid");
                    edtPassword.setError("Incorrect Password");
                }
                else if(status.equals("INCORRECT EMAIL")){
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "onChanged: Email invalid");
                    edtEmail.setError("Incorrect Email address");
                }

            }
        });

    }


    private void goToHome(){
        this.finish();
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);

    }

    @Override
    public void onClick(View view) {
        if(view != null){
            switch (view.getId()){
                case R.id.tvCreateAccount:{
                    Intent signUpIntent = new Intent(this, SignUpActivity.class);
                    startActivity(signUpIntent);
                    break;
                }
                case R.id.btnSignIn:{
                    if(this.validateData()){

                        //verify the user
                        this.validateLogin();
                        //go to main activity
                        //this.goToAddParking();
                    }
                    break;
                }
                default: break;
            }
        }

    }


    private Boolean validateData() {
        if (this.edtEmail.getText().toString().isEmpty()) {
            this.edtEmail.setError("Please enter email");
            return false;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(this.edtEmail.getText().toString()).matches()) {
            this.edtEmail.setError("Please enter a valid email");
        }

        if (this.edtPassword.getText().toString().isEmpty()){
            this.edtPassword.setError("Password cannot be empty");
        }
        return true;
    }

    private void validateLogin(){
        String email = this.edtEmail.getText().toString();
        String password = this.edtPassword.getText().toString();
        this.userViewModel.signInAuthUser(email,password);
        //this.userViewModel.validateUser(email, password);

    }

    private void signInAuthUser(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Authentication Login successful!", Toast.LENGTH_LONG).show();

                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Authentication Login failed! Please try again later", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }



}
