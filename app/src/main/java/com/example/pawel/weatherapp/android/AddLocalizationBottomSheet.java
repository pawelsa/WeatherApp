package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.project.AddLocalizationPresenter;

public class AddLocalizationBottomSheet extends BottomSheetDialogFragment {
    
    private AddLocalizationPresenter presenter;
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bs_add_localization, container, false);
        
        presenter = new AddLocalizationPresenter(inflater.getContext());
        v.findViewById(R.id.cl_newLocalization_createByName)
                .setOnClickListener(view -> presenter.buildAlertDialog()
                        .show());
        return v;
    }
}
