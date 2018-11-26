package com.example.weatherlib.project.newVersion;

import android.content.Context;
import android.util.Log;

import com.example.weatherlib.project.WeatherModel.Forecast;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastDownload {
    
    public final static String METRIC = "metric";
    public final static String IMPERIAL = "imperial";
    public final static String CITIES = "CITIES";
    public static String GOOGLE_API_KEY;
    public static String WEATHER_API_KEY;
    private static Retrofit retrofit;
    
    private static CompositeDisposable disposables;
    
    public static void setupDownloader(Context context, String googleApiKey, String weatherApiKey) {
        
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        
        ForecastDownload.retrofit = builder.build();
        GOOGLE_API_KEY = googleApiKey;
        ForecastDownload.WEATHER_API_KEY = weatherApiKey;
        Hawk.init(context)
                .build();
        
        disposables = new CompositeDisposable();
    }
    
    private static void getForecastRequest(String cityName) {
        getForecastRequest(cityName, METRIC);
    }
    
    
/*    public static void start(ForecastListener listener) {
        Disposable disposable = Observable.just(0)
                .doOnNext(integer -> listener.isLoading(true))
                .flatMap(integer -> DatabaseManager.getForecasts()
                        .flatMap(ForecastDownload::streamOldAndRefreshedForecast)
                        .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onError, () -> listener.isLoading(false));
        
        disposables.add(disposable);
    }
    
    private static Observable<Forecast> streamOldAndRefreshedForecast(Forecast forecast) {
        return Observable.range(1, 2)
                .flatMap(count -> {
                    if (count == 1) {
                        return Observable.just(forecast)
                                .subscribeOn(Schedulers.io());
                    } else {
                        String cityName = forecast.city.name;
                        return getForecastRequest(cityName, METRIC)
                                .onErrorResumeNext(Observable.empty())
                                .subscribeOn(Schedulers.io());
                    }
                });
    }*/
    
/*    public static void start(ForecastListener listener) {
        Disposable disposable = Flowable.just(0)
                .doOnNext(integer -> listener.isLoading(true))
                .flatMap(integer -> DatabaseManager.getForecasts()
                        .flatMap(forecast -> Flowable.just(forecast)
                                .subscribeOn(Schedulers.io())
                                .flatMap(obj -> Flowable.just(obj)
                                        .doOnNext(forecast1 -> Log.d("Thread", "Forecast " + forecast.city.name + " on : " + getThreadName()))
                                        .flatMap(forecast22 -> Flowable.range(1, 2)
                                                .flatMap(instruction -> {
                                                    if (instruction == 1) {
                                                        return Flowable.just(forecast22)
                                                                .doOnNext(number -> Log.d("Thread", "Just : " + forecast22.city.name + " on : " + getThreadName()));
                                                    } else {
                                                        String cityName = forecast22.city.name;
                                                        return getForecastRequest(cityName, METRIC).doOnNext(number -> Log.d("Thread", "Request : " + forecast22.city.name + " on : " + getThreadName()))
                                                                .onErrorResumeNext(Flowable.empty());
                                                    }
                                                })))))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onError, () -> listener.isLoading(false));
        
        disposables.add(disposable);
    }*/
    
    public static void start(ForecastListener listener) {
        Flowable<Forecast> general = Flowable.just(0)
                .doOnNext(integer -> listener.isLoading(true))
                .subscribeOn(Schedulers.io())
                .flatMap(integer -> DatabaseManager.getForecasts());
        
        disposables.add(general.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onError, () -> listener.isLoading(false)));
        
        disposables.add(general.parallel()
                .runOn(Schedulers.io())
                .flatMap(forecast -> getForecastRequest(forecast.city.name, METRIC).doOnNext(number -> Log.d("Thread", "Request : " + forecast.city.name + " on : " + getThreadName()))
                        .onErrorResumeNext(Flowable.empty()))
                .sequential()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, listener::onError, () -> listener.isLoading(false)));
        
        //general.connect();
        
        //  observeOn(AndroidSchedulers.mainThread())
        
    }
    
    private static String getThreadName() {
        return Thread.currentThread()
                .getName();
    }
    
/*    private static Observable<Forecast> streamOldAndRefreshedForecast(Forecast forecast) {
        return Observable.range(1, 2)
                .flatMap(count -> {
                    if (count == 1) {
                        return Observable.just(forecast)
                                .subscribeOn(Schedulers.io());
                    } else {
                        String cityName = forecast.city.name;
                        return getForecastRequest(cityName, METRIC)
                                .onErrorResumeNext(Observable.empty())
                                .subscribeOn(Schedulers.io());
                    }
                });
    }*/
    
    
    /*   TODO musi byc jeden listener na cala biblioteke, nie wiem jak to zrobić :(   */
    public static Flowable<Forecast> getForecastRequest(String cityName, String units) {
        
        ApiCalls weatherClient = retrofit.create(ApiCalls.class);
        return weatherClient.getForecast(cityName, units, WEATHER_API_KEY)
                .map(forecastResponse -> {
                    String requestURL = getURL(forecastResponse);
                    logURL(requestURL);
                    if (forecastResponse.isSuccessful() && forecastResponse.body() != null) {
                        forecastResponse.body().downloadURL = requestURL;
                        return forecastResponse.body();
                    } else {
                        throw (new Exception("City doesn't exists"));
                    }
                })
                .doOnNext(DatabaseManager::removeOldForecastData)
                .doOnNext(Forecast::save);
    }
    
    public static Flowable<Forecast> getForecast(String cityName) {
        return getForecastRequest(cityName, METRIC).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    
    public static void dispose() {
        disposables.clear();
    }
    
    private static String getURL(Response response) {
        return response.raw()
                .request()
                .url()
                .toString();
    }
    
    private static void logURL(String url) {
        Log.i("URL", url);
    }
}