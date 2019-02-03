package com.example.weatherlibwithcityphotos.WeatherModels;

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
	
	public HourlyWeather(com.example.weatherlib.project.WeatherModel.CurrentWeather currentWeather) {
		this.id = currentWeather.id;
		this.dt = currentWeather.dt;
		
		if ( currentWeather.weather != null ) {
			com.example.weatherlib.project.WeatherModel.Weather weather = currentWeather.weather.get(0);
			if ( weather != null ) {
				this.generalDescription = weather.main;
				this.description = weather.description;
				this.icon = weather.icon;
			}
			com.example.weatherlib.project.WeatherModel.Main main = currentWeather.main;
			if ( main != null ) {
				this.temp = main.temp;
				this.pressure = main.pressure;
				this.humidity = main.humidity;
				this.temp_max = main.temp_max;
				this.temp_min = main.temp_min;
			}
		}
		this.snow = currentWeather.snow != null ? currentWeather.snow._3h : 0;
		this.rain = currentWeather.rain != null ? currentWeather.rain._3h : 0;
		this.clouds = currentWeather.clouds != null ? currentWeather.clouds.all : 0;
		this.windDegree = currentWeather.wind != null ? currentWeather.wind.deg : 0;
		this.windSpeed = currentWeather.wind != null ? currentWeather.wind.speed : 0;
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

