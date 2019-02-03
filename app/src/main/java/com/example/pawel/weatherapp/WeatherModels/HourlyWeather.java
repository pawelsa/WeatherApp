package com.example.pawel.weatherapp.WeatherModels;

public class HourlyWeather {
	
	private int id;
	private double dt;
	
	private String generalDescription;
	private String description;
	private String icon;
	
	private double temp;
	private double temp_min;
	private double temp_max;
	private double pressure;
	private double humidity;
	
	private double windSpeed;
	private double windDegree;
	private double clouds;
	private double rain;
	private double snow;
	
	public HourlyWeather(com.example.weatherlibwithcityphotos.WeatherModels.HourlyWeather currentWeather) {
		this.id = currentWeather.getId();
		this.dt = currentWeather.getDt();
		
		this.generalDescription = currentWeather.getGeneralDescription();
		this.description = currentWeather.getDescription();
		this.icon = currentWeather.getIcon();
		
		this.temp = currentWeather.getTemp();
		this.pressure = currentWeather.getPressure();
		this.humidity = currentWeather.getHumidity();
		this.temp_max = currentWeather.getTemp_max();
		this.temp_min = currentWeather.getTemp_min();
		
		this.snow = currentWeather.getSnow();
		this.rain = currentWeather.getRain();
		this.clouds = currentWeather.getClouds();
		this.windDegree = currentWeather.getWindDegree();
		this.windSpeed = currentWeather.getWindSpeed();
	}
	
	public int getId() {
		return id;
	}
	
	public double getWindSpeed() {
		return windSpeed;
	}
	
	public double getWindDegree() {
		return windDegree;
	}
	
	public double getClouds() {
		return clouds;
	}
	
	public double getRain() {
		return rain;
	}
	
	public double getSnow() {
		return snow;
	}
	
	public double getDt() {
		return dt;
	}
	
	public String getGeneralDescription() {
		return generalDescription;
	}
	
	public String getDescription() {
		return description;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public double getTemp() {
		return temp;
	}
	
	public double getPressure() {
		return pressure;
	}
	
	public double getHumidity() {
		return humidity;
	}
	
	public double getTemp_min() {
		return temp_min;
	}
	
	public double getTemp_max() {
		return temp_max;
	}
}

