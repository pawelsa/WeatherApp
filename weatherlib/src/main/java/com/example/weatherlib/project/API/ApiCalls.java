package com.example.weatherlib.project.API;

import com.example.weatherlib.project.PhotoModel.PhotoResponse;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
	
	@GET( "weather" )
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
