package com.example.weatherlibwithcityphotos;

import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.WeatherModels.City;
import com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class ForecastWithPhoto {
	
	private boolean downloaded = false;
	private List<HourlyWeather> weatherList;
	private City city;
	public String units;
	
	
	public ForecastWithPhoto() {
	}
	
	ForecastWithPhoto(Forecast forecast) {
		this.city = new com.example.weatherlibwithcityphotos.WeatherModels.City(forecast.city);
		this.weatherList = convertWeatherList(forecast.list);
		this.downloaded = forecast.isDownloaded();
		this.units = forecast.units;
	}
	
	public String getPhotoReference() {
		return city.getPhotoReference();
	}
	
	private List<HourlyWeather> convertWeatherList(
			List<com.example.weatherlib.project.WeatherModel.CurrentWeather> list) {
		List<HourlyWeather> newList = new ArrayList<>();
		if ( list != null ) {
			for ( com.example.weatherlib.project.WeatherModel.CurrentWeather item : list ) {
				newList.add(new HourlyWeather(item));
			}
		}
		return newList;
	}
	
	public String getCityName() {
		return this.city.getName();
	}
	
	public boolean isDownloaded() {
		return downloaded;
	}
	
	public List<HourlyWeather> getWeatherList() {
		return weatherList;
	}
	
	public City getCity() {
		return city;
	}
	
	@Override
	public int hashCode() {
		super.hashCode();
		return city.getID();
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		Collator instance = Collator.getInstance();
		instance.setStrength(Collator.NO_DECOMPOSITION);
		if ( obj instanceof ForecastWithPhoto ) {
			ForecastWithPhoto other = ( ForecastWithPhoto ) obj;
			int equalName = instance.compare(this.city.getName(), other.city.getName());
			result =
					(this.city.getID() != other.city.getID() && equalName != 0) || ! this.isDownloaded() || this.units.equals(
							other.units);
		} else if ( obj instanceof Forecast ) {
			Forecast other = ( Forecast ) obj;
			int equalName = instance.compare(this.city.getName(), other.city.name);
			result =
					(this.city.getID() != other.city.id && equalName != 0) || ! this.isDownloaded() || this.units.equals(
							other.units);
		} else if ( obj instanceof String ) {
			String otherName = ( String ) obj;
			int equalName = instance.compare(this.city.getName(), otherName);
			result = equalName == 0;
		}
		return result;
	}
}
