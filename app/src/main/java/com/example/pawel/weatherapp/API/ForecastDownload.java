package com.example.pawel.weatherapp.API;

import android.util.Log;

import com.example.pawel.weatherapp.Database.DatabaseManager;
import com.example.pawel.weatherapp.Database.Places;
import com.example.pawel.weatherapp.PlaceWeatherData;
import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather;
import com.example.pawel.weatherapp.WeatherModel.Forecast;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
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

	public static void downloadNewForecast(String cityName) {

		if (!DatabaseManager.checkIFPlaceIsAlreadyInDatabase(new Places(cityName)))
			Observable.zip(getForecastRequest(getForecastCall(cityName)), PhotoDownload.getPhotoReference(cityName), (forecast, imageUrl) -> {
				forecast.cityImageUrl = imageUrl;
				return forecast;
			})
					.subscribe(DatabaseManager::saveForecastAndPlacesToDatabase, Throwable::printStackTrace, () -> Log.i("Result", "Completed"));
	}

	public static Flowable<PlaceWeatherData> startGettingData() {

		return DatabaseManager.getFirstWeatherObject_F()
				.subscribeOn(Schedulers.io())
				.flatMap(weatherList -> {
					Flowable<PlaceWeatherData> getDataForAdapter = DatabaseManager.getPlaces_FI()
							.flatMap(DatabaseManager::getFiveDaysWeatherDataForAdapter_F);

					if (isForecastUpdateNeeded(weatherList))
						return refreshForecast()
								.flatMap(result -> getDataForAdapter);

					return getDataForAdapter;
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

	private static boolean isForecastUpdateNeeded(List<DatabaseWeather> databaseWeatherList) {
		return databaseWeatherList != null && (databaseWeatherList.isEmpty() || isThreeHoursOld(databaseWeatherList.get(0).dt));
	}

	private static Flowable<Forecast> refreshForecast() {
		return DatabaseManager.clearDatabase_S()
				.filter(result -> result)
				.toFlowable()
				.flatMap(result ->
						DatabaseManager.getPlaces_FI()
								.flatMap(place -> Flowable.just(getForecastCall(place.name))))
				.flatMap(forecastObservable -> ForecastDownload.getForecastRequest(forecastObservable).toFlowable(BackpressureStrategy.BUFFER));
	}

	private static Observable<Forecast> getForecastCall(String cityName, String units) {
		ApiCalls weatherClient = retrofit.create(ApiCalls.class);
		return weatherClient.getForecast(cityName, units, APIID);
	}

	private static Observable<Forecast> getForecastCall(String cityName) {
		return getForecastCall(cityName, METRIC);
	}

	private static Observable<Forecast> getForecastRequest(Observable<Forecast> call) {
		return call
				.subscribeOn(Schedulers.io())
				.filter(Objects::nonNull);
	}
}