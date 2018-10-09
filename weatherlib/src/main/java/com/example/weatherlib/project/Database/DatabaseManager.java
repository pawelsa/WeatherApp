package com.example.weatherlib.project.Database;

import android.content.Context;
import android.util.Log;

import com.example.weatherlib.project.WeatherModel.City;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlib.project.WeatherModel.Forecast_Table;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public class DatabaseManager {
	
	public static void init(Context context) {
		FlowManager.init(new FlowConfig.Builder(context).build());
	}
	
	public static boolean checkIFPlaceIsAlreadyInDatabase(City place) {
		CitySave citySave = SQLite.select()
				.from(CitySave.class)
				//.where(CitySave_Table.cityName.eq(place.name))
				.querySingle();
		return citySave != null && citySave.downloaded;
	}
	
	public static Observable<City> getCities_OI() {
		return RXSQLite.rx(SQLite.select()
				                   .from(City.class))
				.queryStreamResults()
				.toObservable();
	}
	
	public static Single<Boolean> clearDatabase_S() {
		return getCities_S().flatMap(cities -> {
			FlowManager.getDatabase(MyDatabase.class)
					.reset();
			for ( City city : cities ) {
				city.save();
			}
			return Single.just(true);
		});
	}
	
	public static Single<List<City>> getCities_S() {
		return RXSQLite.rx(SQLite.select()
				                   .from(City.class))
				.queryList();
	}
	
	public static Maybe<Forecast> getForecastFor(int cityID) {
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class)
				                   .where(Forecast_Table.city_id.eq(cityID))
		)
				.querySingle();
	}
	
	public static Observable<Forecast> getForecasts() {
		
		List<Forecast> forecastList = SQLite.select()
				.from(Forecast.class)
				.queryList();
		
		return RXSQLite.rx(SQLite.select()
				                   .from(Forecast.class))
				.queryStreamResults()
				.toObservable()
				.filter(forecast -> {
					CitySave citySave = SQLite.select()
							.from(CitySave.class)
							.where(CitySave_Table.cityName.eq(forecast.city.name.toLowerCase()))
							.querySingle();
					return citySave != null && citySave.downloaded;
				})
				.doOnNext(forecast -> Log.i("Forecast1", forecast.city.name));
	}
	
	public static Observable<Forecast> saveForecastAndStream(final Forecast forecast) {
		saveForecastToDatabase(forecast);
		return Observable.just(forecast);
	}
	
	public static void saveForecastToDatabase(final Forecast forecast) {
		forecast.save();
		CitySave getCity = SQLite.select()
				.from(CitySave.class)
				.where(CitySave_Table.cityName.eq(forecast.city.name))
				.querySingle();
		if ( getCity != null ) {
			getCity.downloaded = true;
			getCity.update();
		} else {
			CitySave citySave = new CitySave(forecast.city.name.trim().toLowerCase(), true);
			citySave.save();
		}
	}
}
