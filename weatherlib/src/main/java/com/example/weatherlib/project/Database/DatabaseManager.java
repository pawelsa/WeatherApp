package com.example.weatherlib.project.Database;

import android.util.Log;

import com.example.weatherlib.project.Main.ListenerManager;
import com.example.weatherlib.project.WeatherModel.City;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.CurrentWeather_Table;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlib.project.WeatherModel.Forecast_Table;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
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
	
	public static Maybe<Forecast> readForecastFor(int cityID) {
		return RXSQLite.rx(SQLite.select().from(Forecast.class).where(Forecast_Table.city_id.eq(cityID))).querySingle();
	}
	
	public static int getForecastCount() {
		return SQLite.select().from(Forecast.class).where(Forecast_Table.city_id.greaterThan(0)).queryList().size();
	}
	
	public static Maybe<Forecast> removeOldForecastData(final Forecast forecast) {
		return removeOldWeatherData(forecast).flatMap(DatabaseManager::removeFreshlyDownloadedForecast);
	}
	
	private static Maybe<Forecast> removeOldWeatherData(final Forecast forecast) {
		return RXSQLite.rx(SQLite.select()
				                   .from(CurrentWeather.class)
				                   .where(CurrentWeather_Table.forecast_ID.eq(forecast.city.id)))
				.queryList()
				.toMaybe()
				.flatMap(list -> {
					for ( CurrentWeather item : list ) {
						item.delete();
					}
					return Maybe.just(forecast);
				});
	}
	
	private static Maybe<Forecast> removeFreshlyDownloadedForecast(final Forecast forecast) {
		OperatorGroup conditions = OperatorGroup.clause();
		conditions.and(Forecast_Table.city_id.lessThanOrEq(- 1));
		conditions.and(Forecast_Table.city_name.eq(forecast.city.name));
		return queryForecastListWhere(conditions).flatMap(list -> {
			for ( Forecast fore : list ) {
				fore.delete();
			}
			return Maybe.just(forecast);
		});
	}
	
	private static Maybe<List<Forecast>> queryForecastListWhere(OperatorGroup condition) {
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class)
				                   .where(condition))
				.queryList()
				.toMaybe();
	}
	
	public static Forecast addNewCityToNotDownloaded(String cityName) {
		
		int newID = Hawk.get(NOT_DOWNLOADED_IDS, - 1);
		Forecast forecast = new Forecast();
		forecast.city = new City();
		forecast.city.id = newID;
		forecast.city.name = cityName;
		forecast.save();
		Log.d("City", "Add : " + forecast.city.name + " with ID : " + forecast.city.id);
		Hawk.put(NOT_DOWNLOADED_IDS, -- newID);
		return forecast;
	}
	
	public static void removeNotExistentCity(String cityName) {
		OperatorGroup conditions = OperatorGroup.clause();
		conditions.and(Forecast_Table.city_id.lessThanOrEq(- 1));
		conditions.and(Forecast_Table.city_name.eq(cityName));
		queryForecastStreamWhere(conditions)
				.filter(forecast -> forecast.equals(cityName))
				.filter(Forecast::delete)
				.doOnNext(forecast -> Log.i("Removed", "removeNotExistentCity" + forecast.city.name))
				.observeOn(AndroidSchedulers.mainThread())
				.doOnNext(ListenerManager::removedCityListener)
				.subscribe();
	}
	
	private static Flowable<Forecast> queryForecastStreamWhere(OperatorGroup condition) {
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class)
				                   .where(condition))
				.queryStreamResults();
	}
	
	public static Flowable<Forecast> removeForecast(Forecast forecast) {
		return removeForecast(forecast.ID, forecast.city.name);
	}
	
	public static Flowable<Forecast> removeForecast(int cityID, String cityName) {
		OperatorGroup conditions = OperatorGroup.clause();
		conditions.or(Forecast_Table.city_name.eq(cityName));
		conditions.or(Forecast_Table.city_id.eq(cityID));
		return queryForecastStreamWhere(conditions).filter(Forecast::delete);
	}
	
	public static Maybe<Forecast> removeDoubledCity(Forecast forecast) {
		
		OperatorGroup conditions = OperatorGroup.clause();
		conditions.and(Forecast_Table.city_name.eq(forecast.city.name));
		return queryForecastListWhere(conditions).flatMap(list -> {
			for ( Forecast fore : list ) {
				if ( fore.city.population == 0 ) {
					fore.delete();
				}
			}
			return Maybe.just(forecast);
		});
	}
}
