package com.example.pawel.weatherapp.API;

import com.example.pawel.weatherapp.PhotoModel.PhotoResponse;
import com.example.pawel.weatherapp.WeatherModel.CurrentWeather;
import com.example.pawel.weatherapp.WeatherModel.Forecast;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
    
    @GET("weatherList")
	Call<CurrentWeather> getWeather(@Query("q") String cityName,
									@Query("units") String units,
									@Query("APPID") String APIID);

	@GET("forecast")
	Observable<Forecast> getForecast(@Query("q") String cityName,
									 @Query("units") String units,
									 @Query("APPID") String APIID);

	@GET("json?inputtype=textquery&fields=photos,id,name")
	Observable<PhotoResponse> getPhotoResponse(@Query("input") String cityName,
											   @Query("key") String APIKey);
}
