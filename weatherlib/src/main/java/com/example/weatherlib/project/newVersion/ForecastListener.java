package com.example.weatherlib.project.newVersion;

import com.example.weatherlib.project.WeatherModel.Forecast;

public interface ForecastListener {
    
    void onSuccess(Forecast forecast);
    
    void onError(Throwable t);
    
    void isLoading(boolean loading);
    
}
