package com.example.pawel.weatherapp;

import android.app.Application;

import com.example.pawel.weatherapp.API.ForecastDownload;
import com.example.pawel.weatherapp.API.PhotoDownload;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyApplication extends Application {

	public final static String GOOGLE_API_KEY = "AIzaSyBMDV82B6Yi7y-8evhFlTk-5X-s4yE8iF0";
	public final static String APIID = "ce4e1acce4d2ab2b930216750a5d5d39";
	public final static String METRIC = "metric";
	public final static String IMPERIAL = "imperial";

	@Override
	public void onCreate() {
		super.onCreate();

		FlowManager.init(new FlowConfig.Builder(this).build());

		Retrofit.Builder builder = new Retrofit.Builder()
				.baseUrl("http://api.openweathermap.org/data/2.5/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

		ForecastDownload.setupRetrofit(builder.build());

		builder = new Retrofit.Builder()
				.baseUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

		PhotoDownload.setupRetrofit(builder.build());

	}
}
