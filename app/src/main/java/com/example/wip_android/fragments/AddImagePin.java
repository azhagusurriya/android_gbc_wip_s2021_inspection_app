package com.example.wip_android.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.wip_android.R;
import com.example.wip_android.activities.DeficiencyTabLayoutActivity;
import com.example.wip_android.models.ButtonIssue;
import com.example.wip_android.models.ProjectInfo;
import com.example.wip_android.viewmodels.AddImagePinViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class AddImagePin extends Fragment {

    // Variables
    private AddImagePinViewModel mViewModel;
    private View view;
    private ImageView imageView;
    private float x, y;
    private int counter = 1;
    private Fragment switchFragment;
    private FragmentTransaction transaction;
    private Uri contentUri;
    private FloatingActionButton fabSaveMarker;
    private Button addedButton;
    private ArrayList<ButtonIssue> buttonList =  new ArrayList<>();

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_image_pin_fragment, container, false);

        // Add new red button when screen is touched
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    x = event.getX();
                    y = event.getY();

                    addButton(x, y);
                }

                return true;
            }
        });

        // Get image
        Bundle bundle = this.getArguments();
        String imageFromProjectScreen = bundle.getString("photo");
        contentUri = Uri.parse(imageFromProjectScreen);
        imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageURI(contentUri);

        // Floating Button
        fabSaveMarker = (FloatingActionButton) view.findViewById(R.id.fabSaveMarker);
        fabSaveMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveMarkerButtonPressed();
            }
        });

        return view;
    }

    // When the button is clicked
    View.OnClickListener handleOnClick(final Button button) {
        return new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("BUTTON CLICKED: " + buttonList);
            }
        };
    }

    View.OnLongClickListener listener = new View.OnLongClickListener() {
        public boolean onLongClick(View v) {
            Button clickedButton = (Button) v;
            System.out.println("BUTTON LONG CLICKED: " + clickedButton.getId());
//            buttonList.remove(button);
//            System.out.println(buttonList);
            return true;
        }
    };

    // Add a new red button when screen is touched
    public void addButton(float x, float y) {
        ConstraintLayout layout = (ConstraintLayout) getActivity().findViewById(R.id.newLayout);
        addedButton = new Button(getActivity());
        addedButton.setText(String.valueOf(this.counter));
        addedButton.setX(x);
        addedButton.setY(y);
        addedButton.setGravity(Gravity.CENTER);
        addedButton.setPadding(0,0,0,10);
        addedButton.setLayoutParams(new LinearLayout.LayoutParams(50, 50));
        addedButton.setTextColor(Color.parseColor("white"));
        addedButton.setBackgroundColor(Color.parseColor("red"));
        addedButton.setId(this.counter);
        addedButton.setOnClickListener(handleOnClick(addedButton));
        addedButton.setOnLongClickListener(listener);

        ButtonIssue newButton = new ButtonIssue(addedButton.getX(), addedButton.getY(), addedButton.getId());
        this.buttonList.add(newButton);
        this.counter += 1;



        layout.addView(addedButton);
    }

    // Go to deficiency fragment
    public void saveMarkerButtonPressed(){

        Intent intent = new Intent(getActivity(), DeficiencyTabLayoutActivity.class);
        startActivity(intent);

//        switchFragment = new DeficiencyFragment();
//        transaction = getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.project_layout, switchFragment).addToBackStack(null).commit();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddImagePinViewModel.class);
        // TODO: Use the ViewModel
    }

}