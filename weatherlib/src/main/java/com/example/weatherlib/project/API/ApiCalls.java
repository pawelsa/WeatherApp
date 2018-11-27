package com.example.weatherlib.project.API;

import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Flowable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiCalls {
    
    @GET("forecast")
    Flowable<Response<Forecast>> getForecast(@Query("q") String cityName, @Query("units") String units, @Query("APPID") String APIID);
}
