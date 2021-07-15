package com.example.wip_android.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.wip_android.R;
import com.example.wip_android.viewmodels.DeficiencyImageViewViewModel;

public class DeficiencyImageViewFragment extends Fragment {

    private DeficiencyImageViewViewModel mViewModel;
    private ImageView deficiency_imageView;

    public static DeficiencyImageViewFragment newInstance() {
        return new DeficiencyImageViewFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deficiency_image_view_fragment, container, false);


        this.deficiency_imageView = view.findViewById(R.id.deficiency_imageView);

        Glide.with(this).load(this.getArguments().getString("image")).into(deficiency_imageView);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyImageViewViewModel.class);
        // TODO: Use the ViewModel
    }

}