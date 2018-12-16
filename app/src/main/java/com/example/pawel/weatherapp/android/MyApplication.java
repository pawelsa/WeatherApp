package com.example.pawel.weatherapp.android;

import android.app.Application;

import com.example.pawel.weatherapp.BuildConfig;


public class MyApplication extends Application {
	
	public final static String GOOGLE_API_KEY = BuildConfig.GoogleApiKey;
	public final static String WEATHER_API_KEY = BuildConfig.WEATHER_API_KEY;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}
}
