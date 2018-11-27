package com.example.weatherlibwithcityphotos;

import com.example.weatherlib.project.WeatherModel.Forecast;

public class EForecast
		extends Forecast {
	
	public String photoReference;
	
	EForecast() {
	}
	
	EForecast(Forecast forecast) {
		this.city = forecast.city;
		this.ID = forecast.ID;
		this.list = forecast.list;
	}
}
