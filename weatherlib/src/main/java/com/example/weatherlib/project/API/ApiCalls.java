package com.example.weatherlib.project.API;

import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
    
    @GET("forecast")
    Maybe<Response<Forecast>> getForecast(@Query("q") String cityName, @Query("units") String units, @Query("APPID") String APIID);
    
    @GET("forecast")
    Maybe<Response<Forecast>> getForecastForCoordinates(@Query("lat") String lat, @Query("lon") String lon, @Query("units") String units, @Query("APPID") String APIID);
}
