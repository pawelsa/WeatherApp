package com.example.pawel.weatherapp.API;

import com.example.pawel.weatherapp.WeatherModel.CurrentWeather;
import com.example.pawel.weatherapp.WeatherModel.Forecast;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherClient {

	@GET("weather")
	Call<CurrentWeather> getWeather(@Query("q") String cityName,
									@Query("units") String units,
									@Query("APPID") String APIID);

	@GET("forecast")
	Call<Forecast> getForecast(@Query("q") String cityName,
							   @Query("units") String units,
							   @Query("APPID") String APIID);
}
