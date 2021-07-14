package com.example.wip_android.fragments;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.example.wip_android.Circle;
import com.example.wip_android.DrawView;
import com.example.wip_android.PinView;
import com.example.wip_android.R;
import com.example.wip_android.activities.DeficiencyTabLayoutActivity;
import com.example.wip_android.activities.ProjectListItemActivity;
import com.example.wip_android.ui.gallery.GalleryFragment;
import com.example.wip_android.viewmodels.AddImagePinViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class AddImagePin extends Fragment {

    private AddImagePinViewModel mViewModel;
    private View view;
    private final String TAG = this.getClass().getCanonicalName();
//    private PinView imageView;
    private ImageView imageView;
    private DrawView drawView;
    private Fragment switchFragment;
    FragmentTransaction transaction;
    Uri contentUri;
    private int radius=20;
    private int numberOfCircles;
    ArrayList<Circle> arrayListCircle = new ArrayList<>();
    private FloatingActionButton fabSaveMarker;



    public static AddImagePin newInstance() {
        return new AddImagePin();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_image_pin_fragment, container, false);


//        PhotoEditorView imageView = view.findViewById(R.id.imageView);
//        PhotoEditor mPhotoEditor = new PhotoEditor.Builder(getActivity(), imageView)
//                .setPinchTextScalable(true)
//                .build();

        drawView = (DrawView) view.findViewById(R.id.imageView);
//        imageView.isClickable();
//        imageView.hasOnClickListeners();
//        imageView.isLongClickable();
//        addTouchListener();


        Bundle bundle = this.getArguments();
        String imageFromProjectScreen = bundle.getString("photo");

        contentUri = Uri.parse(imageFromProjectScreen);

//        Bitmap map = BitmapFactory.decodeFile(imageFromProjectScreen );
//        imageView.setImage(ImageSource.asset("sanmartino.jpg"));
        // int imageResource = getResources().getIdentifier(imageFromProjectScreen, null,getActivity().getPackageName());

//        imageView.getSource().setImageResource(imageResource);
//      imageView.setImage(ImageSource.uri(contentUri));
//        imageView.getSource().setImageURI(contentUri);
        drawView.setImageURI(contentUri);

        fabSaveMarker = (FloatingActionButton) view.findViewById(R.id.fabSaveMarker);

        fabSaveMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveMarkerButtonPressed();
            }
        });


//        mPhotoEditor.setBrushDrawingMode(true);
//        mPhotoEditor.setBrushSize(70);
//        mPhotoEditor.setOpacity(100);
//        mPhotoEditor.setBrushColor(3);



//        imageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                imageView.setPin(new PointF(1602f, 405f));
//                Log.d(TAG, "onClick: Onclick Clicked");
//
////                Drawable drawable = imageView.getDrawable();
////                Matrix matrix = imageView.getImageMatrix();
////                if (drawable != null) {
////                    RectF rectf = new RectF();
////                    rectf.set(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
////                    matrix.mapRect(rectf);     //The most critical sentence
////                    Log.i("lcf", "left  " + rectf.left + "  " + rectf.top + "  " + rectf.right + "  " + rectf.bottom);
////
////                }
//
//
//                switchFragment = new DeficiencyFragment();
//                transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
//            }
//        });


//        imageView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override public boolean onLongClick(View v) {
////                imageView.setPin(new PointF(1602f, 405f));
//                Log.d(TAG, "onClick: Long Clicked");
//                Toast.makeText(v.getContext(), "Long clicked", Toast.LENGTH_SHORT).show();
//
//
//                switchFragment = new DeficiencyFragment();
//                transaction = getActivity().getSupportFragmentManager().beginTransaction();
//
////                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
////                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
//                return true; }
//        });



        return view;
    }



    public void saveMarkerButtonPressed(){

        Intent intent = new Intent(getActivity(), DeficiencyTabLayoutActivity.class);
        startActivity(intent);

//        switchFragment = new DeficiencyFragment();
//        transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
    }

    public void navigateToDeficiencyPage(){
        switchFragment = new DeficiencyFragment();
                transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
                drawView.invalidate();
    }

//    private void addTouchListener(){
//
//        drawView.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                float x = event.getX();
//                float y = event.getY();
////                arrayListCircle.add(new Circle(x,y,radius));
////                numberOfCircles = arrayListCircle.size();
//                String message = String.format("coordinates: (%.2f,%.2f)", x , y);
//                Log.d(TAG, "onTouch: coordinates" + message);
////                drawView.arrayListValues(x,y);
//                switchFragment = new DeficiencyFragment();
//                transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
//                return false;
//            }
//        });
//    }

//    @Override
//    public boolean performClick() {
//        super.performClick();
//        return true;
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddImagePinViewModel.class);
        // TODO: Use the ViewModel
    }

//    @Override
//    public boolean onTouch(View v, MotionEvent event) {
//        //getting the touched x and y position
//        float positionX = event.getX();
//        float positionY = event.getY();
//
//        Log.d(TAG, "onTouch: position X :" + positionX + "PositionY :"+ positionY);
//
//        return true;
//    }


}