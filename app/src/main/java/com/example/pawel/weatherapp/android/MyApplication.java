package com.example.pawel.weatherapp.android;

import android.app.Application;

import com.example.weatherlibwithcityphotos.MainLib;


public class MyApplication extends Application {

	public final static String GOOGLE_API_KEY = "AIzaSyBMDV82B6Yi7y-8evhFlTk-5X-s4yE8iF0";
	public final static String WEATHER_API_KEY = "ce4e1acce4d2ab2b930216750a5d5d39";

	@Override
	public void onCreate() {
		super.onCreate();
		
		MainLib.setup(this, WEATHER_API_KEY, GOOGLE_API_KEY);
	}
}
