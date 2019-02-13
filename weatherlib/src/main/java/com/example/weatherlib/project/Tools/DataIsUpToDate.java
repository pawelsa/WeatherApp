package com.example.weatherlib.project.Tools;

import com.example.weatherlib.R;
import com.example.weatherlib.project.Main.WeatherLib;

public class DataIsUpToDate
		extends Exception {
	
	private static final String CAUSE = WeatherLib.resources.getString(R.string.data_is_up_to_date);
	
	public DataIsUpToDate() {
		super(CAUSE);
	}
	
	public DataIsUpToDate(String message) {
		super(message);
	}
	
	public DataIsUpToDate(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DataIsUpToDate(Throwable cause) {
		super(CAUSE, cause);
	}
}
