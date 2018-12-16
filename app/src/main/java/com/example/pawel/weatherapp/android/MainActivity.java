package com.example.pawel.weatherapp.android;

import android.os.Bundle;

import com.example.pawel.weatherapp.R;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.pawel.weatherapp.android.MyApplication.GOOGLE_API_KEY;
import static com.example.pawel.weatherapp.android.MyApplication.WEATHER_API_KEY;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
        
        MainLib.setup(this, WEATHER_API_KEY, GOOGLE_API_KEY);
	}


}

