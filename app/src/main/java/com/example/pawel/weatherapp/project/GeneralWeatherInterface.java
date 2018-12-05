package com.example.pawel.weatherapp.project;

import com.example.weatherlibwithcityphotos.EForecast;

public interface GeneralWeatherInterface {
	
	void addItemToAdapter(EForecast forecast);
	
	void isRefreshing(boolean refresh);
    
}
