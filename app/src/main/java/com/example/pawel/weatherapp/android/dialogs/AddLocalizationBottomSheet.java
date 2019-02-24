package com.example.pawel.weatherapp.android.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.weatherlibwithcityphotos.MainLib;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddLocalizationBottomSheet
		extends BottomSheetDialogFragment {
	
	private AddLocalizationPresenter presenter;
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bs_add_localization, container, false);
		
		presenter = new AddLocalizationPresenter(inflater.getContext());
		return v;
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		view.findViewById(R.id.cl_newLocalization_createByName)
				.setOnClickListener(v -> presenter.showAddNewCityAlertDialog(new AddLocalizationPresenter.OnDialogDismissed() {
					@Override
					public void positiveButtonPressed() {
						dismiss();
					}
					
					@Override
					public void negativeButtonPressed() {
						dismiss();
					}
				}));
		
		view.findViewById(R.id.cl_newLocalization_createByGPS)
				.setOnClickListener(v -> {
					MainLib.downloadNewForecastFromLocalization();
					dismiss();
				});
		
	}
}
