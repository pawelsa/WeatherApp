package com.example.weatherlib.project.API;

import com.example.weatherlib.R;
import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.Main.WeatherLib;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.weatherlib.project.Database.DatabaseManager.addNewCityToNotDownloaded;
import static com.example.weatherlib.project.Database.DatabaseManager.removeNotExistentCity;
import static com.example.weatherlib.project.Main.WeatherLib.WEATHER_API_KEY;
import static com.example.weatherlib.project.Tools.Helpers.getURL;
import static com.example.weatherlib.project.Tools.Helpers.logURL;

public class ForecastDownload {
	
	private static Retrofit.Builder
			retrofitBuilder = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
			.addConverterFactory(GsonConverterFactory.create())
			.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
	
	
	public static Flowable<Forecast> downloadNewForecastFor(String cityName) {
		addNewCityToNotDownloaded(cityName);
		
		return getForecastRequest(cityName, WeatherLib.USED_UNIT)
				.subscribeOn(Schedulers.io());
	}
	
	public static Flowable<Forecast> getForecastRequest(String cityName, String units) {
		
		Retrofit retrofit = retrofitBuilder.build();
		ApiCalls weatherClient = retrofit.create(ApiCalls.class);
		
		return weatherClient
				.getForecast(cityName, units, WEATHER_API_KEY)
				.map(forecastResponse -> {
					String requestURL = getURL(forecastResponse);
					logURL(requestURL);
					if ( forecastResponse.isSuccessful() && forecastResponse.body() != null ) {
						forecastResponse.body().downloadURL = requestURL;
						return forecastResponse.body();
					} else {
						removeNotExistentCity(cityName);
						throw (new Exception(WeatherLib.resources.getString(R.string.city_does_not_exists)));
					}
				})
				.flatMap(forecast -> DatabaseManager.removeOldForecastData(forecast, cityName))
				.doOnNext(Forecast::save);
	}
}