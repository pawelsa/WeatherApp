package com.example.pawel.weatherapp.android;

import android.app.Application;

import com.example.weatherlibwithcityphotos.MainLib;


public class MyApplication extends Application {
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		MainLib.setup(this, WEATHER_API_KEY, GOOGLE_API_KEY);
	}
}
