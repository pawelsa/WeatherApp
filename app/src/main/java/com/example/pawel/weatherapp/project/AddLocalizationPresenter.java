package com.example.pawel.weatherapp.project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.pawel.weatherapp.R;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.Objects;


public class AddLocalizationPresenter {
	
	private Context context;
	
	public AddLocalizationPresenter(Context context) {
		this.context = context;
	}
	
	public void showAddNewCityAlertDialog() {
		
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.dialog_new_city_by_name, null);
		
		alertDialogBuilder.setView(dialogView)
				.setPositiveButton(context.getText(R.string.dialog_remove_city_confirm), (dialog, which) -> {
					EditText editText = dialogView.findViewById(R.id.new_city_name);
					final String cityName = editText.getText().toString().trim();
					MainLib.downloadNewForecastFor(cityName);
				})
				.setNegativeButton(context.getText(android.R.string.cancel), (dialog, which) -> dialog.cancel());
		
		AlertDialog alertDialog = alertDialogBuilder.show();
		EditText editText = Objects.requireNonNull(alertDialog.getWindow()).findViewById(R.id.new_city_name);
		editText.setOnFocusChangeListener((v, hasFocus) -> editText.post(() -> {
			InputMethodManager imm = ( InputMethodManager ) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			Objects.requireNonNull(imm).showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
		}));
		editText.requestFocus();
		
		
	}
	
}
