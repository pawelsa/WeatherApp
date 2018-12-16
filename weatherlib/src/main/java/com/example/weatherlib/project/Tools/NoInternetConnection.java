package com.example.weatherlib.project.Tools;

import com.example.weatherlib.R;
import com.example.weatherlib.project.Main.WeatherLib;

public class NoInternetConnection extends Exception {
    
    private static final String CAUSE = WeatherLib.resources.getString(R.string.no_internet_connection);
    
    public NoInternetConnection() {
        super(CAUSE);
    }
    
    public NoInternetConnection(String message) {
        super(message);
    }
    
    public NoInternetConnection(String message, Throwable cause) {
        super(message, cause);
    }
    
    public NoInternetConnection(Throwable cause) {
        super(CAUSE, cause);
    }
}
