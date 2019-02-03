package com.example.pawel.weatherapp.project;

import com.example.pawel.weatherapp.WeatherModels.ForecastModel;

public interface GeneralWeatherInterface {
	
	void addItemToAdapter(ForecastModel forecast);
	
	void isRefreshing(boolean refresh);
	
	void removeForecastFromAdapter(ForecastModel forecast);
    
    void showSnackbar(String message);
}
