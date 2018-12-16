package com.example.weatherlibwithcityphotos;


import com.example.weatherlib.project.WeatherModel.Forecast;

public interface ForecastsListener {
	
	void onSuccess(EForecast forecast);
	
	void onError(Throwable t);
	
	void isLoading(boolean loading);
    
    void removedForecast(Forecast forecast);
}
