package com.example.weatherlib.project.API;

import android.content.Context;
import android.util.Log;

import com.example.weatherlib.project.Database.CitySave;
import com.example.weatherlib.project.Database.CitySave_Table;
import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.WeatherModel.City;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class ForecastDownload {
	
	public final static String METRIC = "metric";
	public final static String IMPERIAL = "imperial";
	public final static String CITIES = "CITIES";
	public final static String DOWNLOAD = "DOWNLOAD";
	public static String GOOGLE_API_KEY;
	public static String APIID;
	private static Retrofit retrofit;
	
	public static void setupDownloader(Context context, Retrofit retrofit, String googleApiKey, String APIID) {
		ForecastDownload.retrofit = retrofit;
		GOOGLE_API_KEY = googleApiKey;
		ForecastDownload.APIID = APIID;
		Hawk.init(context).build();
	}
	
	public static void checkIfAllDownloaded() {
		RXSQLite.rx(SQLite.select().from(CitySave.class))
				.queryList()
				.toObservable()
				.flatMap(citySaves -> Observable.fromIterable(citySaves))
				.doOnNext(citySave -> Log.i("Step", "Before"))
				.filter(citySave -> ! citySave.downloaded)
				.doOnNext(citySave -> Log.i("Step", "After"))
				.subscribe(citySave -> downloadNewForecast(citySave.cityName),
				           Throwable::printStackTrace,
				           () -> Log.i("Completed", "Finished checking"));
	}
	
	public static void downloadNewForecast(String cityName) {
		
		if ( ! DatabaseManager.checkIFPlaceIsAlreadyInDatabase(new City(cityName)) ) {
			Observable.zip(getForecastRequest(cityName), PhotoDownload.getPhotoReference(cityName),
			               (forecast, imageUrl) -> {
				               forecast.setCityImageUrl(imageUrl);
				               return forecast;
			               })
					//.doOnNext(DatabaseManager::saveForecastToDatabase)
					.doOnComplete(() -> Hawk.put(DOWNLOAD, new Timestamp(System.currentTimeMillis())))
					.subscribe(DatabaseManager::saveForecastToDatabase, Throwable::printStackTrace,
					           () -> Log.i("Download", "Forecast completed"));
		}
	}
	
	private static Observable<Forecast> getForecastRequest(String cityName) {
		return getForecastRequest(cityName, METRIC);
	}
	
	private static Observable<Forecast> getForecastRequest(String cityName, String units) {
		List<CitySave> citySaveList = SQLite.select()
				.from(CitySave.class)
				.where(CitySave_Table.cityName.eq(cityName.toLowerCase().trim()))
				.queryList();
		
		CitySave toUpdate = SQLite.select()
				.from(CitySave.class)
				.where(CitySave_Table.cityName.eq(cityName.toLowerCase().trim()))
				.querySingle();
		if ( toUpdate == null ) {
			CitySave newCity = new CitySave(cityName.toLowerCase().trim());
			newCity.save();
		}
		ApiCalls weatherClient = retrofit.create(ApiCalls.class);
		return weatherClient.getForecast(cityName, units, APIID)
				.subscribeOn(Schedulers.io())
				.filter(forecast -> forecast != null);
	}
	
	public static Observable<Forecast> getForecastForAdapter() {
		return Observable.just(Hawk.get(DOWNLOAD, new Timestamp(System.currentTimeMillis())))
				.flatMap(data -> {
					if ( isThreeHoursOld(data) ) {
						return refreshForecast().flatMap(DatabaseManager::saveForecastAndStream);
					} else {
						return DatabaseManager.getForecasts();
					}
				})
				.observeOn(AndroidSchedulers.mainThread());
	}
	
	private static boolean isThreeHoursOld(Timestamp timestamp) {
		Timestamp now = new Timestamp(System.currentTimeMillis());
		double difference = Math.abs(now.getTime() - timestamp.getTime());
		double threeHours = TimeUnit.HOURS.toMillis(3);
		return difference > threeHours;
	}
	
	private static Observable<Forecast> refreshForecast() {
		return DatabaseManager.clearDatabase_S()
				.toObservable()
				.flatMap(result -> DatabaseManager.getCities_OI()
						.flatMap(city -> getForecastRequest(city.name)))
				.doOnComplete(() -> Hawk.put(DOWNLOAD, new Timestamp(System.currentTimeMillis())));
	}
}