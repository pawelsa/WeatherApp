package com.example.pawel.weatherapp.API;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.pawel.weatherapp.Database.DatabaseManager;
import com.example.pawel.weatherapp.PlaceWeatherData;
import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather;
import com.example.pawel.weatherapp.WeatherModel.Forecast;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.pawel.weatherapp.Database.MyApplication.APIID;
import static com.example.pawel.weatherapp.Database.MyApplication.METRIC;

public class RFRequest {

	private static Retrofit retrofit;

	public static void setupRetrofit(final Retrofit retrofit) {
		RFRequest.retrofit = retrofit;
	}

	public static void downloadNewForecast(String cityName) {

		getForecastRequest_F(getForecastCall(cityName));
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

	private static Flowable refreshForecast() {

		return DatabaseManager.clearDatabase_S()
				.filter(result -> result)
				.toFlowable()
				.flatMap(result ->
						DatabaseManager.getPlaces_FI()
								.flatMap(place -> Flowable.just(getForecastCall(place.name))))
				.flatMap(RFRequest::getForecastRequest_F);
	}

	private static Call<Forecast> getForecastCall(String cityName, String units) {
		WeatherClient weatherClient = retrofit.create(WeatherClient.class);

		return weatherClient.getForecast(cityName, units, APIID);
	}

	private static Call<Forecast> getForecastCall(String cityName) {
		return getForecastCall(cityName, METRIC);
	}

	private static Flowable getForecastRequest_F(final Call<Forecast> call) {

		return Maybe.create(emitter ->
				call.enqueue(new Callback<Forecast>() {
					@Override
					public void onResponse(@NonNull Call<Forecast> call, @NonNull Response<Forecast> response) {

						Log.i("Weather response", response.toString());

						Forecast forecast = response.body();
						if (forecast != null)
							DatabaseManager.saveForecastAndPlacesToDatabase(forecast);
						emitter.onSuccess(true);
					}

					@Override
					public void onFailure(@NonNull Call<Forecast> call, @NonNull Throwable t) {

						t.printStackTrace();
						emitter.onError(t);
					}
				})
		).toFlowable();
	}

}
