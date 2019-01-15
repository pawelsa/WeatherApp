package com.example.weatherlibwithcityphotos.WeatherModels;

public class CurrentWeather {
	
	private int id;
	
	private Weather weather;
	
	private Main main;
	
	private double windSpeed;
	private double windDegree;
	
	private double clouds;
	
	private double rain;
	
	private double snow;
	
	private double dt;
	
	public CurrentWeather(com.example.weatherlib.project.WeatherModel.CurrentWeather currentWeather) {
		this.id = currentWeather.id;
		this.dt = currentWeather.dt;
		this.weather = new Weather(currentWeather.weather.get(0));
		this.main = new Main(currentWeather.main);
		this.snow = currentWeather.snow._3h;
		this.rain = currentWeather.rain._3h;
		this.clouds = currentWeather.clouds.all;
		this.windDegree = currentWeather.wind.deg;
		this.windSpeed = currentWeather.wind.speed;
	}
	
	public int getId() {
		return id;
	}
	
	public Weather getWeather() {
		return weather;
	}
	
	public Main getMain() {
		return main;
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
	
	public class Weather {
		private String main;
		private String description;
		private String icon;
		
		public Weather(com.example.weatherlib.project.WeatherModel.Weather weather) {
			this.main = weather.main;
			this.description = weather.description;
			this.icon = weather.icon;
		}
		
		public String getMain() {
			return main;
		}
		
		public String getDescription() {
			return description;
		}
		
		public String getIcon() {
			return icon;
		}
	}
	
	public class Main {
		
		private double temp;
		private double pressure;
		private double humidity;
		private double temp_min;
		private double temp_max;
		
		Main(com.example.weatherlib.project.WeatherModel.Main main) {
			this.temp = main.temp;
			this.pressure = main.pressure;
			this.humidity = main.humidity;
			this.temp_max = main.temp_max;
			this.temp_min = main.temp_min;
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
}

