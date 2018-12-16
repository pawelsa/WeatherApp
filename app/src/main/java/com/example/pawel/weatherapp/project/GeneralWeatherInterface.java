package com.example.pawel.weatherapp.project;

import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.EForecast;

public interface GeneralWeatherInterface {
	
	void addItemToAdapter(EForecast forecast);
	
	void isRefreshing(boolean refresh);
    
    void removeForecastFromAdapter(Forecast forecast);
    
    void showSnackbar(String message);
}
