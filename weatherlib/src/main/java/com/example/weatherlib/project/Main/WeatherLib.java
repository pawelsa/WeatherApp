package com.example.weatherlib.project.Main;

import android.content.Context;
import android.content.res.Resources;

import com.example.weatherlib.project.Tools.Units;
import com.orhanobut.hawk.Hawk;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import static com.example.weatherlib.project.Main.ForecastStreams.downloadDataForNewUnits;

public class WeatherLib {
	
	private final static String UNITS = "UNITS";
	public static Resources resources;
	
	public static String WEATHER_API_KEY;
	public static String USED_UNIT;
	
	/*   TODO sprawdzic czy jest sprawdzany  status okna pause restart  */
	public static void setupWeatherLib(Context context, String weatherApiKey) {
		
		WEATHER_API_KEY = weatherApiKey;
		FlowManager.init(new FlowConfig.Builder(context).build());
		Hawk.init(context)
				.build();
		
		resources = context.getResources();
		
		USED_UNIT = Hawk.get(UNITS, Units.METRIC.name());
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
	
	public static void downloadNewForecastFor(String cityName) {
		ForecastStreams.downloadNewForecastFor(cityName);
	}
	
	public static void refreshForecast() {
		downloadDataForNewUnits();
	}
	
	public static void addListener(ForecastListener listener) {
		ListenerManager.addListener(listener);
	}
	
	public static void removeListener(ForecastListener listener) {
		ListenerManager.removeListener(listener);
	}
}
