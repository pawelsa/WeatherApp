package com.example.weatherlibwithcityphotos;

import android.content.Context;

import com.example.getphotoforcity.PhotoDownload;
import com.example.weatherlib.project.Main.ForecastListener;
import com.example.weatherlib.project.Main.ForecastStreams;
import com.example.weatherlib.project.Main.WeatherLib;
import com.example.weatherlib.project.Tools.Units;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainLib {
	
	private static Disposable disposable;
	
	private static final ForecastListener forecastListener = getForecastListener();
	
	public static void setup(Context context, String weatherApiKey, String googleApiKey) {
		Hawk.init(context).build();
		WeatherLib.setupWeatherLib(context, weatherApiKey);
		WeatherLib.addListener(forecastListener);
		PhotoDownload.setup(context, googleApiKey);
	}
	
	public static void useUnits(Units units) {
		WeatherLib.useUnits(units);
	}
	
	public static void streamForecastsWithRefresh() {
		WeatherLib.streamForecastsWithRefresh();
	}
	
	public static void downloadNewForecastFor(String cityName) {
		WeatherLib.downloadNewForecastFor(cityName);
	}
	
	public static boolean removeForecastFor(String cityName) {
		return WeatherLib.removeForecastFor(cityName);
	}
	
	public static boolean removeForecastFor(int cityID, String cityName) {
		PhotoDownload.removePhotoFor(cityName);
		return WeatherLib.removeForecastFor(cityID);
	}
	
	public static void refreshForecast() {
		WeatherLib.refreshForecast();
	}
	
	public static void addListener(ForecastsListener listener) {
		ListenersManager.addListener(listener);
	}
	
	public static void dispose() {
		ForecastStreams.dispose();
		if ( disposable != null && ! disposable.isDisposed() ) {
			disposable.dispose();
		}
	}
	
	public static void removeListener(ForecastsListener listener) {
		ListenersManager.removeListener(listener);
	}
	
	private static ForecastListener getForecastListener() {
		return new ForecastListener() {
			@Override
			public void onSuccess(Forecast forecast) {
				disposable = PhotoDownload.getPhoto(forecast.city.name)
						.map(photo -> {
							EForecast eForecast = new EForecast(forecast);
							eForecast.photoReference = photo;
							return eForecast;
						})
						.onErrorResumeNext(Single.just(new EForecast(forecast)))
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(ListenersManager::onSuccessListener, Throwable::printStackTrace);
			}
			
			@Override
			public void onError(Throwable t) {
				ListenersManager.onErrorListener(t);
			}
			
			@Override
			public void isLoading(boolean loading) {
				ListenersManager.isLoadingListener(loading);
			}
		};
	}
}
