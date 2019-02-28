package com.example.pawel.weatherapp.weatherModels;

import com.example.weatherlibwithcityphotos.ForecastWithPhoto;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

public class ForecastModel {
	
	private boolean downloaded = false;
	private List<HourlyWeather> weatherList;
	private City city;
	private String units;
	
	
	ForecastModel() {
	}
	
	ForecastModel(ForecastModel forecastModel) {
		updateModelWithNewData(forecastModel);
	}
	
	private void updateModelWithNewData(ForecastModel forecastModel) {
		this.city = forecastModel.city;
		this.weatherList = forecastModel.getWeatherList();
		this.downloaded = forecastModel.isDownloaded();
		this.units = forecastModel.units;
	}
	
	public List<HourlyWeather> getWeatherList() {
		if ( weatherList == null ) {
			weatherList = new ArrayList<>();
		}
		return weatherList;
	}
	
	public boolean isDownloaded() {
		return downloaded;
	}
	
	ForecastModel(ForecastWithPhoto forecast) {
		this.city = new City(forecast.getCity(), forecast.getPhotoReference());
		this.downloaded = forecast.isDownloaded();
		this.units = forecast.units;
		if ( this.downloaded ) {
			this.weatherList = convertWeatherList(forecast.getWeatherList());
		}
	}
	
	private List<HourlyWeather> convertWeatherList(
			List<com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather> list) {
		
		List<HourlyWeather> newList = new ArrayList<>();
		if ( list != null ) {
			for ( com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather item : list ) {
				newList.add(new HourlyWeather(item));
			}
		}
		return newList;
	}
	
	public int getCityID() {
		return this.city.getID();
	}
	
	public String getPhotoReference() {
		return city.getPhotoReference();
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
		if ( obj instanceof ForecastModel ) {
			ForecastModel other = ( ForecastModel ) obj;
			int equalName = instance.compare(this.getCityName(), other.getCityName());
			result = (this.getCityID() == other.getCityID() || equalName == 0)
			         && ((this.units != null && other.units != null && this.units.equals(other.units)) || true);
		} else if ( obj instanceof String ) {
			String otherName = ( String ) obj;
			int equalName = instance.compare(this.getCityName(), otherName);
			result = equalName == 0;
		}
		return result;
	}
	
	public String getCityName() {
		return this.city.getName();
	}
	
}
