package com.example.wip_android.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wip_android.PinView;
import com.example.wip_android.R;
import com.example.wip_android.viewmodels.AddImagePinViewModel;

public class AddImagePin extends Fragment {

    private AddImagePinViewModel mViewModel;
    private View view;
    private final String TAG = this.getClass().getCanonicalName();
    private PinView imageView;
    Uri contentUri;

    public static AddImagePin newInstance() {
        return new AddImagePin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_image_pin_fragment, container, false);

        imageView = view.findViewById(R.id.imageView);
        imageView.isClickable();
        imageView.hasOnClickListeners();
        imageView.isLongClickable();
        Bundle bundle = this.getArguments();
        String imageFromProjectScreen = bundle.getString("photo");

        contentUri = Uri.parse(imageFromProjectScreen);

//        Bitmap map = BitmapFactory.decodeFile(imageFromProjectScreen );
//        imageView.setImage(ImageSource.asset("sanmartino.jpg"));

        imageView.setImage(ImageSource.uri(contentUri));


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
                imageView.setPin(new PointF(1602f, 405f));
                Log.d(TAG, "onClick: Long Clicked");
                Toast.makeText(v.getContext(), "Long clicked", Toast.LENGTH_SHORT).show();

                return true; }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddImagePinViewModel.class);
        // TODO: Use the ViewModel
    }

}