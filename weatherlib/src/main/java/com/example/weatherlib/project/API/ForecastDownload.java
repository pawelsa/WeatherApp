package com.example.weatherlib.project.API;

import retrofit2.Retrofit;

public class ForecastDownload {
    
    public final static String METRIC = "metric";
    public final static String IMPERIAL = "imperial";
    public final static String CITIES = "CITIES";
    public final static String DOWNLOAD = "DOWNLOAD";
    public static String GOOGLE_API_KEY;
    public static String APIID;
    private static Retrofit retrofit;
    /*
    private static PublishSubject<Forecast> downloadSubject = PublishSubject.create();
    
    public static void setupDownloader(Context context, Retrofit retrofit, String googleApiKey, String APIID) {
        ForecastDownload.retrofit = retrofit;
        GOOGLE_API_KEY = googleApiKey;
        ForecastDownload.APIID = APIID;
        Hawk.init(context)
                .build();
    }
    
    public static Observable<Forecast> toAdapter() {
        return Observable.range(1, 4)
                .flatMap(count -> {
                    switch (count) {
                        case 2:
                            return checkIfAllDownloaded().doOnNext(forecast -> Log.d("toAdapter", "checkIfAllDownloaded"));
                        case 3:
                            return getForecastForAdapter().doOnNext(forecast -> Log.d("toAdapter", "getForecastForAdapter"));
                        case 4:
                            return downloadSubject;
                        default:
                            return DatabaseManager.getForecasts()
                                    .doOnNext(forecast -> Log.d("toAdapter", "getForecasts"));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }
    
    private static Observable<Forecast> checkIfAllDownloaded() {
        return RXSQLite.rx(SQLite.select()
                .from(CitySave.class))
                .queryStreamResults()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .filter(citySave -> !DatabaseManager.checkIFPlaceIsAlreadyInDatabase(new City(citySave.cityName)))
                .flatMap(citySave -> Observable.zip(getForecastRequest(citySave.cityName), PhotoDownload.getPhotoReference(citySave.cityName), (forecast, imageUrl) -> {
                    forecast.setCityImageUrl(imageUrl);
                    return forecast;
                })
                        .doOnNext(forecast -> Log.d("Download", "checkIfAllDownloaded : " + forecast.city.name))
                        .flatMap(DatabaseManager::saveForecastAndStream))
                .doOnComplete(() -> Log.d("Complete", "checkIfAllDownloaded"));
    }
    
    public static void downloadNewForecast(String cityName) {
        
        Observable.zip(getForecastRequest(cityName), PhotoDownload.getPhotoReference(cityName), (forecast, imageUrl) -> {
            forecast.setCityImageUrl(imageUrl);
            return forecast;
        })
                .flatMap(DatabaseManager::saveForecastAndStream)
                .subscribe(downloadSubject::onNext, Throwable::printStackTrace, () -> Log.i("Download", "Forecast completed"));
    }
    
    private static Observable<Forecast> getForecastRequest(String cityName) {
        return getForecastRequest(cityName, METRIC);
    }
    
    private static Observable<Forecast> getForecastRequest(String cityName, String units) {
        ifNewCityAddToCitySave(cityName);
        
        ApiCalls weatherClient = retrofit.create(ApiCalls.class);
        return weatherClient.getForecast(cityName, units, APIID)
                .filter(Response::isSuccessful)
                .filter(forecastResponse -> forecastResponse.body() != null)
                .map(forecastResponse -> {
                    Log.i("URL", forecastResponse.raw()
                            .request()
                            .url()
                            .toString());
                    forecastResponse.body().downloadURL = forecastResponse.raw()
                            .request()
                            .url()
                            .toString();
                    return forecastResponse.body();
                })
                .subscribeOn(Schedulers.io());
    }
    
    private static void ifNewCityAddToCitySave(String cityName) {
        CitySave toUpdate = SQLite.select()
                .from(CitySave.class)
                .where(CitySave_Table.cityName.eq(cityName.toLowerCase()
                        .trim()))
                .querySingle();
        if (toUpdate == null) {
            CitySave newCity = new CitySave(cityName.toLowerCase()
                    .trim());
            newCity.save();
        }
    }
    
    private static Observable<Forecast> getForecastForAdapter() {
        return just(getDownloadTimestamp()).flatMap(data -> {
            if (!isThreeHoursOld(data)) {
                return Observable.empty();
            }
            return refreshForecast().flatMap(DatabaseManager::saveForecastAndStream);
        })
                .doOnComplete(() -> Log.d("Complete", "refreshForecast"));
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
                        .doOnNext(city -> Log.d("Download", "refreshForecast : " + city.name))
                        .flatMap(city -> getForecastRequest(city.name)))
                .doOnComplete(ForecastDownload::setDownloadTimestamp);
    }
    
    private static Timestamp getDownloadTimestamp() {
        return Hawk.get(DOWNLOAD, new Timestamp(System.currentTimeMillis()));
    }
    
    private static void setDownloadTimestamp() {
        Hawk.put(DOWNLOAD, new Timestamp(System.currentTimeMillis()));
    }*/
}