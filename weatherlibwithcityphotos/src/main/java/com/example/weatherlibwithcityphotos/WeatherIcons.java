package com.example.weatherlibwithcityphotos;


public class WeatherIcons {
	
	public static int getIcon(String iconID) {
		
		switch (iconID) {
			case "01d":
				return R.drawable.weather_clear;
			case "01n":
				return R.drawable.weather_clear_night;
			case "02d":
				return R.drawable.weather_few_clouds;
			case "02n":
				return R.drawable.weather_few_clouds_night;
			case "03d":
				return R.drawable.weather_clouds;
			case "03n":
				return R.drawable.weather_clouds_night;
			case "04d":
			case "04n":
				return R.drawable.weather_haze;
			case "09d":
				return R.drawable.weather_showers_day;
			case "09n":
				return R.drawable.weather_showers_night;
			case "10d":
				return R.drawable.weather_rain_day;
			case "10n":
				return R.drawable.weather_rain_night;
			case "11d":
				return R.drawable.weather_storm_day;
			case "11n":
				return R.drawable.weather_storm_night;
			case "13d":
				return R.drawable.weather_snow_scattered_day;
			case "13n":
				return R.drawable.weather_snow_scattered_night;
			case "50d":
			case "50n":
				return R.drawable.weather_mist;
			default:
				return R.drawable.weather_clear;
		}
	}
}
