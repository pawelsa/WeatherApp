package com.example.pawel.weatherapp.Tools;

import android.content.Context;

import com.example.weatherlib.R;

public class NoInternetConnection
		extends Exception {
	
	private static String CAUSE = "No Internet Connection";
	
	public NoInternetConnection() {
		super(CAUSE);
	}
	
	public NoInternetConnection(String message) {
		super(message);
	}
	
	public NoInternetConnection(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NoInternetConnection(Throwable cause) {
		super(CAUSE, cause);
	}
	
	public static void setupResources(Context context) {
		CAUSE = context.getString(R.string.no_internet_connection);
	}
}
