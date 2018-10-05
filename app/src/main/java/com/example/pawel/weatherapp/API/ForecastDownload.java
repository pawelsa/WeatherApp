package com.example.pawel.weatherapp.API;

import com.example.pawel.weatherapp.Database.DatabaseManager;
import com.example.pawel.weatherapp.Database.Places;
import com.example.pawel.weatherapp.WeatherModel.Forecast;
import com.orhanobut.hawk.Hawk;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.example.pawel.weatherapp.MyApplication.APIID;
import static com.example.pawel.weatherapp.MyApplication.METRIC;

public class ForecastDownload {
    
    private static Retrofit retrofit;
    
    public static void setupRetrofit(final Retrofit retrofit) {
        ForecastDownload.retrofit = retrofit;
    }
    
    //todo : need to check if fail download and not save to SharedPreferences
    public static void downloadNewForecast(String cityName) {
        
        if (!DatabaseManager.checkIFPlaceIsAlreadyInDatabase(new Places(cityName))) {
            Observable.zip(getForecastRequest(cityName), PhotoDownload.getPhotoReference(cityName), (forecast, imageUrl) -> {
                forecast.setCityImageUrl(imageUrl);
                return forecast;
            })
                    .subscribe(DatabaseManager::saveForecastToDatabase, Throwable::printStackTrace, () -> Hawk.put("DOWNLOAD", new Timestamp(System.currentTimeMillis())));
        }
    }
    
    public static Observable<Forecast> startGettingData() {
        
        return Observable.just(Hawk.get("DOWNLOAD"))
                .cast(double.class)
                .flatMap(timestamp -> {
                    if (isThreeHoursOld(timestamp)) {
                        return refreshForecast().flatMap(DatabaseManager::saveForecastFlow);
                    } else {
                        return DatabaseManager.getForecasts();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
    
    private static boolean isThreeHoursOld(double timestamp) {
        Date now = new Date();
        Date obtained = new Date((long) (timestamp) * 1000 - 2 * 60 * 60 * 1000);
        double difference = Math.abs(now.getTime() - obtained.getTime());
        double threeHours = TimeUnit.HOURS.toMillis(3);
        return difference > threeHours;
    }
    
    private static boolean isForecastUpdateNeeded(Forecast forecast) {
        return forecast != null && forecast.list != null && !forecast.list.isEmpty() && isThreeHoursOld(forecast.list.get(0).dt);
    }
    
    private static Observable<Forecast> refreshForecast() {
        return DatabaseManager.clearDatabase_S()
                .toObservable()
                .flatMap(result -> DatabaseManager.getCities_OI()
                        .flatMap(city -> getForecastRequest(city.name)))
                .doOnComplete(() -> Hawk.put("DOWNLOAD", new Timestamp(System.currentTimeMillis())));
    }
    
    private static Observable<Forecast> getForecastRequest(String cityName, String units) {
        ApiCalls weatherClient = retrofit.create(ApiCalls.class);
        return weatherClient.getForecast(cityName, units, APIID)
                .subscribeOn(Schedulers.io())
                .filter(Objects::nonNull);
    }
    
    private static Observable<Forecast> getForecastRequest(String cityName) {
        return getForecastRequest(cityName, METRIC);
    }
}