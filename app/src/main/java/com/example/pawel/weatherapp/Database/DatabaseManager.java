package com.example.pawel.weatherapp.Database;

import com.example.pawel.weatherapp.PlaceWeatherData;
import com.example.pawel.weatherapp.WeatherModel.City;
import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather;
import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather_Table;
import com.example.pawel.weatherapp.WeatherModel.Forecast;
import com.example.pawel.weatherapp.WeatherModel.Forecast_Table;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;



				/*
					.doOnSuccess(result-> Log.i("Thread", Thread.currentThread().getName()))
				*/

// .where(DatabaseWeather_Table.cityID.eq(cityID), DatabaseWeather_Table.dt.lessThanOrEq(getTodayMidnightTime() / 1000))


public class DatabaseManager {
    
    public static boolean checkIFPlaceIsAlreadyInDatabase(Places place) {
        
        List<Places> placesList = SQLite.select()
                .from(Places.class)
                .where(Places_Table.name.eq(place.name))
                .queryList();
        
        return placesList.contains(place);
    }
    
    public static Single<List<City>> getCities_S() {
        
        return RXSQLite.rx(SQLite.select()
                .from(City.class))
                .queryList();
    }
    
    public static Observable<City> getCities_OI() {
        
        return getCities_S().toObservable()
                .flatMap(Observable::fromIterable);
    }
    
    public static Single<Boolean> clearDatabase_S() {
        
        return getCities_S().flatMap(cities -> {
            
            FlowManager.getDatabase(MyDatabase.class)
                    .reset();
            for (City city : cities) {
                city.save();
            }
            return Single.just(true);
        });
    }
    
    public static Single<List<DatabaseWeather>> getFiveDaysWeatherData_S(int cityID) {
        
        return RXSQLite.rx(SQLite.select()
                .from(DatabaseWeather.class)
                .where(DatabaseWeather_Table.cityID.eq(cityID))
                .orderBy(DatabaseWeather_Table.dt, true)
                .limit(5))
                .queryList()
                .subscribeOn(Schedulers.io());
    }
    
    public static Forecast getForecastFor(int cityID) {
        return SQLite.select()
                .from(Forecast.class)
                .where(Forecast_Table.city_id.eq(cityID))
                .querySingle();
    }
    
    public static Observable<Forecast> getForecasts() {
        return RXSQLite.rx(SQLite.select()
                .from(Forecast.class))
                .queryStreamResults()
                .toObservable();
    }
    
    private static Single<PlaceWeatherData> getFiveDaysWeatherDataForAdapter_S(Places place) {
        
        final int cityID = place.cityID;
        return getFiveDaysWeatherData_S(cityID).flatMap(databaseWeathers -> Single.just(new PlaceWeatherData(place, databaseWeathers)));
    }
    
    public static Flowable<PlaceWeatherData> getFiveDaysWeatherDataForAdapter_F(Places place) {
        return getFiveDaysWeatherDataForAdapter_S(place).toFlowable();
    }
    
    private static Single<List<DatabaseWeather>> getFirstWeatherObject_M() {
        return RXSQLite.rx(SQLite.select()
                .from(DatabaseWeather.class)
                .orderBy(DatabaseWeather_Table.dt, true)
                .limit(1))
                .queryList();
    }
    
    public static Flowable<List<DatabaseWeather>> getFirstWeatherObject_F() {
        return getFirstWeatherObject_M().toFlowable();
    }

/*	public static void saveForecastToDatabase(final Forecast forecast) {
		forecast.save();
		//forecast.saveInDatabase();

		List<Forecast> forecastList = SQLite.select().from(Forecast.class)
				.queryList();
		
		
		Places places = new Places(forecast.city, forecast.cityImageUrl);
		places.save();
	}*/
    
    public static void saveForecastToDatabase(final Forecast forecast) {
        forecast.save();
    }
    
    public static Observable<Forecast> saveForecastFlow(final Forecast forecast) {
        saveForecastToDatabase(forecast);
        return Observable.just(forecast);
    }
    
    
    // TODO: Delete
    public static Single<List<DatabaseWeather>> getWeatherFor(int cityID) {
        
        return RXSQLite.rx(SQLite.select()
                .from(DatabaseWeather.class)
                .where(DatabaseWeather_Table.cityID.eq(cityID))
                .orderBy(DatabaseWeather_Table.dt, true))
                .queryList();
        
    }
    
    private static double getTodayMidnightTime() {
        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        
        date.add(Calendar.DAY_OF_MONTH, 1);
        
        return date.getTime()
                .getTime();
    }
}
