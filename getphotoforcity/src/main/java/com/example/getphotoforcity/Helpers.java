package com.example.getphotoforcity;

import android.util.Log;

import java.util.Date;

import retrofit2.Response;

public class Helpers {
	
	public static void logThread(String msg) {
		Log.d("Thread", msg + " on : " + getThreadName());
	}
	
	public static String getThreadName() {
		return Thread.currentThread()
				.getName();
	}
	
	public static void showTime(String msg) {
		Date date = new Date();
		Log.d("Time", msg + " : " + date.toString());
	}
	
	public static void logURL(String url) {
		Log.i("URL", url);
	}
	
	public static String getURL(Response response) {
		return response.raw()
				.request()
				.url()
				.toString();
	}
}
