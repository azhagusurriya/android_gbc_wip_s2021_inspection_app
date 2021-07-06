package com.example.wip_android.fragments;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.Typeface;
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
import com.example.wip_android.ui.gallery.GalleryFragment;
import com.example.wip_android.viewmodels.AddImagePinViewModel;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class AddImagePin extends Fragment {

    private AddImagePinViewModel mViewModel;
    private View view;
    private final String TAG = this.getClass().getCanonicalName();
//    private PinView imageView;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    Uri contentUri;



    public static AddImagePin newInstance() {
        return new AddImagePin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_image_pin_fragment, container, false);


        PhotoEditorView imageView = view.findViewById(R.id.imageView);
        PhotoEditor mPhotoEditor = new PhotoEditor.Builder(getActivity(), imageView)
                .setPinchTextScalable(true)
                .build();


//        imageView = view.findViewById(R.id.imageView);
        imageView.isClickable();
        imageView.hasOnClickListeners();
        imageView.isLongClickable();
        Bundle bundle = this.getArguments();
        String imageFromProjectScreen = bundle.getString("photo");

        contentUri = Uri.parse(imageFromProjectScreen);

//        Bitmap map = BitmapFactory.decodeFile(imageFromProjectScreen );
//        imageView.setImage(ImageSource.asset("sanmartino.jpg"));

       // int imageResource = getResources().getIdentifier(imageFromProjectScreen, null,getActivity().getPackageName());

//        imageView.getSource().setImageResource(imageResource);
//      imageView.setImage(ImageSource.uri(contentUri));
        imageView.getSource().setImageURI(contentUri);
//        imageView.setImageURI(contentUri);

        mPhotoEditor.setBrushDrawingMode(true);
        mPhotoEditor.setBrushSize(70);
        mPhotoEditor.setOpacity(100);
        mPhotoEditor.setBrushColor(3);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                imageView.setPin(new PointF(1602f, 405f));
                Log.d(TAG, "onClick: Onclick Clicked");
                switchFragment = new DeficiencyFragment();
                transaction = getActivity().getSupportFragmentManager().beginTransaction();

//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
            }
        });


        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override public boolean onLongClick(View v) {
//                imageView.setPin(new PointF(1602f, 405f));
                Log.d(TAG, "onClick: Long Clicked");
                Toast.makeText(v.getContext(), "Long clicked", Toast.LENGTH_SHORT).show();


                switchFragment = new DeficiencyFragment();
                transaction = getActivity().getSupportFragmentManager().beginTransaction();

//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
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