package com.example.weatherlibwithcityphotos;

public interface ForecastsListener {
	
	void onSuccess(ForecastWithPhoto forecast);
	
	void onError(Throwable t);
	
	void isLoading(boolean loading);
	
	void removedForecast(ForecastWithPhoto forecast);
}
