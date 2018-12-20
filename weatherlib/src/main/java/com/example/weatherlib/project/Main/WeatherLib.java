package com.example.weatherlib.project.Main;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;

import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.GPSLocation;
import com.example.weatherlib.project.Tools.Units;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import static com.example.weatherlib.project.Main.ForecastStreams.downloadDataForNewUnits;

public class WeatherLib {
	
	private final static String UNITS = "UNITS";
	public static Resources resources;
	
	public static String WEATHER_API_KEY;
	public static String USED_UNIT;
	
	private static Activity activity;
	
	/*   TODO sprawdzic czy jest sprawdzany  status okna pause restart  */
	/*   TODO niektóre modele nie są przypisane do konkretnej pogody   */
	public static void setupWeatherLib(Activity activity, String weatherApiKey) {
		
		WEATHER_API_KEY = weatherApiKey;
		FlowManager.init(new FlowConfig.Builder(activity).build());
		Hawk.init(activity)
				.build();
		WeatherLib.activity = activity;
		GPSLocation.setup(activity);
		resources = activity.getResources();
		USED_UNIT = Hawk.get(UNITS, Units.METRIC.name());
	}
	
	public static Context getContext() {
		return activity.getApplicationContext();
	}
	
	public static void useUnits(Units units) {
		if ( ! USED_UNIT.equals(units.name()) ) {
			USED_UNIT = units.name();
			Hawk.put(UNITS, units);
			downloadDataForNewUnits();
		}
	}
	
	public static void streamForecastsWithRefresh() {
		ForecastStreams.streamForecastsWithRefresh();
	}
	
	public static void downloadNewForecastFromLocalization() {
		ForecastStreams.downloadForecastsForCoordinates();
	}
	
	public static void downloadNewForecastFor(String cityName) {
		ForecastStreams.downloadNewForecastFor(cityName);
	}
	
	public static void removeForecastFor(Forecast forecast) {
		ForecastStreams.streamRemovedIDs(DatabaseManager.removeForecast(forecast));
	}
	
	public static void refreshForecast() {
		ForecastStreams.downloadDataForNewUnits();
	}
	
	public static void addListener(ForecastListener listener) {
		ListenerManager.addListener(listener);
	}
	
	public static void removeListener(ForecastListener listener) {
		ListenerManager.removeListener(listener);
	}
}
