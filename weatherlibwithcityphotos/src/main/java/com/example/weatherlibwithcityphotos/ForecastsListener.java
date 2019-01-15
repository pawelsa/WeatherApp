package com.example.weatherlibwithcityphotos;

public interface ForecastsListener {
	
	void onSuccess(EForecast forecast);
	
	void onError(Throwable t);
	
	void isLoading(boolean loading);
	
	void removedForecast(EForecast forecast);
}
