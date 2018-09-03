package com.example.pawel.weatherapp;


import com.example.pawel.weatherapp.Database.Places;
import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather;

import java.util.List;

public class PlaceWeatherData {

	public List<DatabaseWeather> placesWeather;
	public Places place;

	public PlaceWeatherData(Places place, List<DatabaseWeather> databaseWeathers) {
		this.place = place;
		placesWeather = databaseWeathers;
	}
}
