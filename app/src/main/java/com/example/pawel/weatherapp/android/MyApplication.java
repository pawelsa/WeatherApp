package com.example.pawel.weatherapp.android;

import android.app.Application;

import com.example.weatherlib.project.Database.DatabaseManager;

public class MyApplication extends Application {

	public final static String GOOGLE_API_KEY = "AIzaSyBMDV82B6Yi7y-8evhFlTk-5X-s4yE8iF0";
	public final static String APIID = "ce4e1acce4d2ab2b930216750a5d5d39";
	public final static String METRIC = "metric";
	public final static String IMPERIAL = "imperial";

	@Override
	public void onCreate() {
		super.onCreate();
		
		DatabaseManager.init(this);
        
        //ForecastDownload.setupDownloader(this, builder.build(), GOOGLE_API_KEY, APIID);
        com.example.weatherlib.project.newVersion.ForecastDownload.setupDownloader(this, GOOGLE_API_KEY, APIID);
        
        com.example.weatherlib.project.newVersion.DatabaseManager.init(this);

		/*builder = new Retrofit.Builder()
				.baseUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create());

		PhotoDownload.setupRetrofit(builder.build());*/

	}
}
