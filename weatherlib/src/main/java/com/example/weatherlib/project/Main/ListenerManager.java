package com.example.weatherlib.project.Main;

import com.example.weatherlib.project.WeatherModel.Forecast;

import java.util.ArrayList;
import java.util.List;

public class ListenerManager {
	
	private static final String TAG = ListenerManager.class.getName();
	
	private static List<ForecastListener> listeners = new ArrayList<>();
	
	static void addListener(ForecastListener listener) {
		if ( ! listeners.contains(listener) ) {
			listeners.add(listener);
		}
	}
	
	static void removeListener(ForecastListener listener) {
		listeners.remove(listener);
	}
	
	static void onSuccessListener(Forecast forecast) {
		for ( ForecastListener listener : listeners ) {
			listener.onSuccess(forecast);
		}
	}
	
	static void onErrorListener(Throwable throwable) {
		for ( ForecastListener listener : listeners ) {
			listener.onError(throwable);
		}
	}
	
	static void isLoadingListener(boolean isLoading) {
		for ( ForecastListener listener : listeners ) {
			listener.isLoading(isLoading);
		}
	}
	
	public static void removedCityListener(Forecast forecast) {
		for (ForecastListener listener : listeners) {
			listener.removedForecast(forecast);
		}
	}
	
}
