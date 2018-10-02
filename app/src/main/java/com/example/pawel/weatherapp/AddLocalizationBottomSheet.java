package com.example.pawel.weatherapp;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pawel.weatherapp.API.ForecastDownload;

public class AddLocalizationBottomSheet extends BottomSheetDialogFragment {

	TextView tvAddByName;

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		tvAddByName.setOnClickListener(view -> buildAlertDialog());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.bs_add_localization, container, false);
		tvAddByName = v.findViewById(R.id.tv_newLocalization_byName);
		return v;
	}

	private void buildAlertDialog() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
		alertDialogBuilder.setTitle("Enter city name");

		final EditText input = buildEditText();
		input.setHint("City name");

		alertDialogBuilder
				.setView(input)
				.setPositiveButton("Confirm", (dialog, which) -> {
					final String cityName = input.getText().toString();
					ForecastDownload.downloadNewForecast(cityName);
				})
				.setNegativeButton("cancel", (dialog, which) ->
						dialog.cancel());

		alertDialogBuilder.show();
	}

	private EditText buildEditText() {
		EditText input = new EditText(getContext());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		input.setFocusable(true);
		input.setSelection(input.getText().length());
		return input;
	}
}
