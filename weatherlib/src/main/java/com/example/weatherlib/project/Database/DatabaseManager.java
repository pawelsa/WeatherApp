package com.example.weatherlib.project.Database;

import android.util.Log;

import com.example.weatherlib.project.WeatherModel.City;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.CurrentWeather_Table;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlib.project.WeatherModel.Forecast_Table;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public class DatabaseManager {
	
	private final static String NOT_DOWNLOADED_IDS = "NotDownloadedIDs";
	
	public static Flowable<Forecast> getForecasts() {
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class))
				.queryList()
				.toFlowable()
				.flatMap(Flowable::fromIterable)
				.subscribeOn(Schedulers.io());
	}
	
	public static Flowable<Forecast> removeOldForecastData(final Forecast forecast, String typedCity) {
		return removeOldWeatherData(forecast)
				.flatMap(forecast1 -> removeFreshlyDownloadedForecast(forecast, typedCity));
	}
	
	private static Flowable<Forecast> removeOldWeatherData(final Forecast forecast) {
		return RXSQLite.rx(SQLite.select()
				                   .from(CurrentWeather.class)
				                   .where(CurrentWeather_Table.forecast_ID.eq(forecast.city.id))
		)
				.queryList()
				.toFlowable()
				.flatMap(list -> {
					for ( CurrentWeather item : list ) {
						item.delete();
					}
					return Flowable.just(forecast);
				});
	}
	
	private static Flowable<Forecast> removeFreshlyDownloadedForecast(final Forecast forecast, String typedName) {
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class)
				                   .where(Forecast_Table.city_id.lessThanOrEq(- 1)))
				.queryList()
				.toFlowable()
				.flatMap(list -> {
					for ( Forecast f : list ) {
						if ( f.city.name.equals(forecast.city.name) || f.city.name.equals(typedName) ) {
							f.delete();
						}
					}
					return Flowable.just(forecast);
				});
	}
	
	public static void addNewCityToNotDownloaded(String cityName) {
		
		int newID = Hawk.get(NOT_DOWNLOADED_IDS, - 1);
		Forecast forecast = new Forecast();
		forecast.city = new City();
		forecast.city.id = newID;
		forecast.city.name = cityName;
		forecast.save();
		Log.d("City", "Add : " + forecast.city.name + " with ID : " + forecast.city.id);
		Hawk.put(NOT_DOWNLOADED_IDS, -- newID);
	}
	
	public static void removeNotExistentCity(String cityName) {
		List<Forecast> doesNotExist = SQLite.select()
				.from(Forecast.class)
				.where(Forecast_Table.city_id.lessThanOrEq(- 1))
				.queryList();
		for ( Forecast forecast : doesNotExist ) {
			if ( forecast.city.name.equals(cityName) ) {
				Log.d("City", "Remove : " + forecast.city.name + " with ID : " + forecast.city.id);
				forecast.delete();
			}
		}
	}
}
