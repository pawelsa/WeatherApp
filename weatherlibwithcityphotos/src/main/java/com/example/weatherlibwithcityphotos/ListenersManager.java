package com.example.weatherlibwithcityphotos;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

class ListenersManager {

    private static String TAG = "ListenersManager";

	private static List<ForecastsListener> listeners = new ArrayList<>();
	
	static void addListener(ForecastsListener listener) {
		if ( ! listeners.contains(listener) ) {
			listeners.add(listener);
		}
		Log.d("Listener", "Added, size : " + listeners.size());
	}
	
	static void removeListener(ForecastsListener listener) {
		listeners.remove(listener);
		Log.d("Listener", "Removed, size : " + listeners.size());
	}
	
	static void onSuccessListener(ForecastWithPhoto forecast) {
		for ( ForecastsListener listener : listeners ) {
			listener.onSuccess(forecast);
		}
	}
	
	static void onErrorListener(Throwable throwable) {
		for ( ForecastsListener listener : listeners ) {
			listener.onError(throwable);
		}
	}
	
	static void isLoadingListener(boolean isLoading) {
		for ( ForecastsListener listener : listeners ) {
			listener.isLoading(isLoading);
		}
	}
	
	static void removedCityListener(ForecastWithPhoto forecast) {
        for (ForecastsListener listener : listeners) {
            listener.removedForecast(forecast);
        }
    }
	
}