package com.example.weatherlib.project.Main;

import android.content.Context;

import com.example.weatherlib.project.Tools.Units;

public interface WeatherInterface {
	
	void setupWeatherLib(Context context, String weatherApiKey);
	
	void useUnits(Units units);
	
	void streamForecastsWithRefresh();
	
	void downloadNewForecastFor(String cityName);
	
	void refreshForecast();
	
	void addListener(ForecastListener listener);
	
	void removeListener(ForecastListener listener);
}
