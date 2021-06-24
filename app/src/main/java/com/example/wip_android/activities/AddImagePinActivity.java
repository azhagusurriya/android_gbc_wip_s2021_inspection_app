package com.example.wip_android.activities;

import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.wip_android.PinView;
import com.example.wip_android.R;

import java.util.ArrayList;


public class AddImagePinActivity extends AppCompatActivity{

    private final String TAG = this.getClass().getCanonicalName();
    private PinView imageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image_pin);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Add Issue Marker</font>"));
        }


        imageView = findViewById(R.id.imageView);
        imageView.isClickable();
        imageView.hasOnClickListeners();
        imageView.isLongClickable();


        imageView.setImage(ImageSource.asset("sanmartino.jpg"));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                imageView.setPin(new PointF(1602f, 405f));
                Log.d(TAG, "onClick: Clicked");
                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show(); }
        });

        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                imageView.setPin(new PointF(1602f, 405f));
                Log.d(TAG, "onClick: Long Clicked");
                Toast.makeText(v.getContext(), "Long clicked", Toast.LENGTH_SHORT).show();
                return true; }
        });

    }
}
