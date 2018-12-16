package com.example.weatherlib.project.Tools;

import com.example.weatherlib.R;
import com.example.weatherlib.project.Main.WeatherLib;

public class NotExists extends Exception {
    
    public NotExists() {
        super(WeatherLib.resources.getString(R.string.city_does_not_exists));
    }
    
    public NotExists(String message) {
        super(message);
    }
    
    public NotExists(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NotExists(Throwable cause) {
        super(cause);
    }
}