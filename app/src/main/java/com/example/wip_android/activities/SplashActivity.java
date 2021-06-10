package com.example.wip_android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.wip_android.MainActivity;
import com.example.wip_android.R;

public class SplashActivity extends AppCompatActivity {

    // Splash Screen time
    private static int SPLASH_SCREEN = 5000;

    // Variables
    Animation splashAnim;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        // Animations
        splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash_animation);

        // Hooks
        image = findViewById(R.id.logo);
        image.setAnimation(splashAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_SCREEN);

    }
}