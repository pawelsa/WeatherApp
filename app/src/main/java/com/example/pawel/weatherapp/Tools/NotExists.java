package com.example.pawel.weatherapp.Tools;

import android.content.Context;

import com.example.weatherlib.R;

public class NotExists
		extends Exception {
	
	private static String CAUSE = "City Not Exists";
	
	public NotExists() {
		super();
	}
	
	public NotExists(String message) {
		super(message);
	}
	
	public NotExists(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NotExists(Throwable cause) {
		super(CAUSE, cause);
	}
	
	public static void setupResources(Context context) {
		CAUSE = context.getString(R.string.city_does_not_exists);
	}
}