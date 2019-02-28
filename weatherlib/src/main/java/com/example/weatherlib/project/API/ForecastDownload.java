package com.example.weatherlib.project.API;

import android.util.Log;

import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.Main.WeatherLib;
import com.example.weatherlib.project.Tools.NoInternetConnection;
import com.example.weatherlib.project.Tools.NotExists;
import com.example.weatherlib.project.WeatherModel.Forecast;

import java.net.UnknownHostException;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherlib.project.Database.DatabaseManager.addNewCityToNotDownloaded;
import static com.example.weatherlib.project.Database.DatabaseManager.removeNotExistentCity;
import static com.example.weatherlib.project.Main.WeatherLib.WEATHER_API_KEY;
import static com.example.weatherlib.project.Tools.ExceptionTester.matchesException;
import static com.example.weatherlib.project.Tools.Helpers.getURL;
import static com.example.weatherlib.project.Tools.Helpers.logURL;

public class ForecastDownload {
	
	private static Retrofit.Builder retrofitBuilder =
			new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
					.addConverterFactory(GsonConverterFactory.create())
					.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
	
	
	public static Flowable<Forecast> downloadNewForecastFor(Flowable<Forecast> startStream, String cityName) {
		Forecast newAdded = addNewCityToNotDownloaded(cityName);
		Flowable<Forecast> sendNewAddedCity = Flowable.just(newAdded);
		Flowable<Forecast> downloadNewCity = getForecastRequest(cityName, WeatherLib.USED_UNIT).toFlowable()
				.onErrorReturn(throwable -> {
					Log.i("Error", "downloadNewForecastFor : " + throwable.getClass().getName());
					if ( matchesException(throwable, NoInternetConnection.class) ) {
						return newAdded;
					}
					throw new Exception(throwable);
				});
		return startStream.flatMap(forecast -> Flowable.merge(sendNewAddedCity, downloadNewCity));
	}
	
	public static Maybe<Forecast> getForecastRequest(String cityName, String units) {
		
		Retrofit retrofit = retrofitBuilder.build();
		ApiCalls weatherClient = retrofit.create(ApiCalls.class);
		
		return getForecast(weatherClient.getForecast(cityName, units, WEATHER_API_KEY), units)
				.doOnSuccess(forecast -> removeNotExistentCity(cityName))
				.doOnError(throwable -> {
					if ( matchesException(throwable, NotExists.class) ) {
						removeNotExistentCity(cityName);
					}
				});
	}
	
	private static Maybe<Forecast> getForecast(Maybe<Response<Forecast>> requestStream, String units) {
		
		return requestStream
				.flatMap(forecastResponse -> {
					String requestURL = getURL(forecastResponse);
					logURL(requestURL);
					if ( forecastResponse.isSuccessful() && forecastResponse.body() != null ) {
						forecastResponse.body().units = units;
						return Maybe.just(forecastResponse.body());
					} else {
						return Maybe.error(new NotExists());
					}
				})
				.doOnError(throwable -> {
					if ( throwable instanceof UnknownHostException ) {
						throw new NoInternetConnection(throwable);
					}
				})
				.flatMap(DatabaseManager::removeOldForecastData)
				.flatMap(DatabaseManager::removeDoubledCity)
				.doOnSuccess(Forecast::save);
	}
	
	public static Maybe<Forecast> getForecastRequestForCoordinates(String lat, String lon, String units) {
		
		Retrofit retrofit = retrofitBuilder.build();
		ApiCalls weatherClient = retrofit.create(ApiCalls.class);
		
		return getForecast(weatherClient.getForecastForCoordinates(lat, lon, units, WEATHER_API_KEY), units);
	}
}