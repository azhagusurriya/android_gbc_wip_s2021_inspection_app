package com.example.wip_android.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wip_android.R;
import com.example.wip_android.viewmodels.DeficiencyAfterViewModel;

public class DeficiencyAfterFragment extends Fragment {

    private DeficiencyAfterViewModel mViewModel;

    public static DeficiencyAfterFragment newInstance() {
        return new DeficiencyAfterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.deficiency_after_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyAfterViewModel.class);
        // TODO: Use the ViewModel
    }

}