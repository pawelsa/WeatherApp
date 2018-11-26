package com.example.weatherlib.project.newVersion;

import android.content.Context;

import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.CurrentWeather_Table;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import io.reactivex.Flowable;
import io.reactivex.Single;

public class DatabaseManager {
    
    public static void init(Context context) {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }
    
    static Flowable<Forecast> getForecasts() {
        return RXSQLite.rx(SQLite.select()
                .from(Forecast.class))
                .queryStreamResults();
    }
    
    static Single<Forecast> removeOldForecastData(final Forecast forecast) {
        return RXSQLite.rx(SQLite.select()
                .from(CurrentWeather.class)
                .where(CurrentWeather_Table.forecast_ID.eq(forecast.city.id)))
                .queryList()
                .flatMap(list -> {
                    for (CurrentWeather item : list) {
                        item.delete();
                    }
                    return Single.just(forecast);
                });
    }
    
}
