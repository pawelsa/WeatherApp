package com.example.weatherlibwithcityphotos;

import android.app.Activity;

import com.example.getphotoforcity.PhotoDownload;
import com.example.weatherlib.project.Main.ForecastListener;
import com.example.weatherlib.project.Main.ForecastStreams;
import com.example.weatherlib.project.Main.WeatherLib;
import com.example.weatherlib.project.Tools.Units;
import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class MainLib {
	
	private static Disposable disposable;
	
	private static final ForecastListener forecastListener = getForecastListener();
	
	public static void setup(Activity activity, String weatherApiKey, String googleApiKey) {
		WeatherLib.setupWeatherLib(activity, weatherApiKey);
		WeatherLib.addListener(forecastListener);
		PhotoDownload.setup(activity, googleApiKey);
	}
	
	public static void useUnits(Units units) {
		WeatherLib.useUnits(units);
	}
	
	public static void streamForecastsWithRefresh() {
		WeatherLib.streamForecastsWithRefresh();
	}
	
	public static void downloadNewForecastFromLocalization() {
		WeatherLib.downloadNewForecastFromLocalization();
	}
	
	public static void downloadNewForecastFor(String cityName) {
		WeatherLib.downloadNewForecastFor(cityName);
	}
	
	public static void readForecastFor(int cityID) {
		WeatherLib.readForecastFor(cityID);
	}
	
	public static int getForecastCount() {
		return WeatherLib.getForecastCount();
	}
	
	private static Single<ForecastWithPhoto> getPhotoFor(Forecast forecast) {
		return PhotoDownload.getPhoto(forecast.city.name)
				.map(photo -> {
					ForecastWithPhoto forecastWithPhoto = new ForecastWithPhoto(forecast);
					forecastWithPhoto.getCity().setPhotoReference(photo);
					return forecastWithPhoto;
				})
				.onErrorResumeNext(Single.just(new ForecastWithPhoto(forecast)));
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
	
	public static void removeForecastFor(ForecastWithPhoto forecast) {
		removeForecastFor(forecast.getCityName(), forecast.getCity().getID());
	}
	
	public static void removeForecastFor(String cityName, int cityID) {
		PhotoDownload.removePhotoFor(cityName);
		WeatherLib.removeForecastFor(cityID, cityName);
	}
	
	private static ForecastListener getForecastListener() {
		return new ForecastListener() {
			@Override
			public void onSuccess(Forecast forecast) {
				disposable = getPhotoFor(forecast)
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
			
			@Override
			public void removedForecast(Forecast forecast) {
				ListenersManager.removedCityListener(new ForecastWithPhoto(forecast));
			}
		};
	}
}
