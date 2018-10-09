package com.example.pawel.weatherapp.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.pawel.weatherapp.R;
import com.example.weatherlib.project.API.ForecastDownload;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ForecastDownload.checkIfAllDownloaded();
	}


}

