package com.example.weatherlib.project.Main;

import com.example.weatherlib.project.API.ForecastDownload;
import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.GPSLocation;
import com.example.weatherlib.project.Tools.DataIsUpToDate;
import com.example.weatherlib.project.Tools.NetworkCheck;
import com.example.weatherlib.project.Tools.NoInternetConnection;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.CurrentWeather_Table;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.raizlabs.android.dbflow.rx2.language.RXSQLite;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.sql.Timestamp;
import java.util.Calendar;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.weatherlib.project.Main.WeatherLib.USED_UNIT;

public class ForecastStreams {
	
	private static final String TAG = ForecastStreams.class.getName();
	
	private static CompositeDisposable disposables = new CompositeDisposable();
	
	public static void dispose() {
		disposables.clear();
	}
	
	static void streamRemovedIDs(Flowable<Forecast> removedStream) {
		disposables.add(removedStream.subscribeOn(Schedulers.io())
				                .observeOn(AndroidSchedulers.mainThread())
				                .subscribe(ListenerManager::removedCityListener, ListenerManager::onErrorListener));
	}
	
	static void streamForecastsWithRefresh() {
		
		Flowable<Forecast> startLoadingForecasts = getStartLoadingForecastStream();
		
		Flowable<Forecast> refreshForecast =
				NetworkCheck.isConnectedToNetwork()
						.doOnNext(aBoolean -> {
							if ( ! aBoolean ) {
								ListenerManager.onErrorListener(new NoInternetConnection());
							}
						})
						.filter(aBoolean -> aBoolean)
						.flatMap(aBoolean ->
								         ifOldDataThenUpdate(getStartLoadingForecastStream()));
		
		Flowable<Forecast> connectable = startLoadingForecasts.mergeWith(refreshForecast);
		disposables.add(getLoadingDisposable(connectable));
	}
	
	private static Flowable<Forecast> ifOldDataThenUpdate(Flowable<Forecast> stream) {
		Timestamp ts = new Timestamp(System.currentTimeMillis());
		// TODO: 27.02.2019 this needs to be repaired
		Calendar cTs = Calendar.getInstance();
		cTs.setTimeInMillis(ts.getTime());
		
		cTs.set(Calendar.HOUR_OF_DAY, cTs.get(Calendar.HOUR_OF_DAY) - 2);
		ts.setTime(cTs.getTimeInMillis());
		
		return RXSQLite.rx(SQLite.select()
				                   .from(CurrentWeather.class)
				                   .where(CurrentWeather_Table.dt.greaterThan(Double.valueOf(
						                   ts.getTime()))))
				.queryList()
				.toFlowable()
				.doOnNext(list -> {
					if ( list.isEmpty() ) {
						ListenerManager.onErrorListener(new DataIsUpToDate());
					}
				})
				.filter(currentWeathers -> currentWeathers.size() > 0)
				.flatMap(currentWeathers -> getRefreshingForecastStream(stream));
	}
	
	private static Flowable<Forecast> getStartLoadingForecastStream() {
		
		Flowable<Forecast> withoutChecking = getStartLoadingStream().flatMap(integer -> DatabaseManager.getForecasts());
		Flowable<Forecast> withChecking = withoutChecking
				.flatMap(forecast -> forecast.isDownloaded() ?
				                     Flowable.just(forecast) :
				                     Flowable.merge(Flowable.just(forecast),
				                                    ForecastDownload.getForecastRequest(forecast.city.name, USED_UNIT)
						                                    .subscribeOn(Schedulers.io())
						                                    .toFlowable()));
		
		return NetworkCheck.isConnectedToNetwork()
				.flatMap(connectedToNetwork -> connectedToNetwork ? withChecking : withoutChecking);
	}
	
	private static Flowable<Forecast> getRefreshingForecastStream(Flowable<Forecast> entryFlowable) {
		
		Flowable<Forecast> refresh =
				entryFlowable.flatMap(forecast -> ForecastDownload.getForecastRequest(forecast.city.name, USED_UNIT)
						.subscribeOn(Schedulers.io())
						.toFlowable()
				);
		
		Flowable<Forecast> errorHandler = getStartLoadingStream().flatMap(forecast -> Flowable.just(new Forecast()))
				.doOnNext(forecast -> ListenerManager.onErrorListener(new NoInternetConnection()))
				.flatMap(forecast -> Flowable.empty());
		
		return NetworkCheck.isConnectedToNetwork()
				.subscribeOn(Schedulers.io())
				.flatMap(aBoolean -> aBoolean ? refresh : errorHandler);
	}
	
	private static Disposable getLoadingDisposable(Flowable<Forecast> forecastStream) {
		return forecastStream
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ListenerManager::onSuccessListener,
				           ListenerManager::onErrorListener,
				           () -> ListenerManager.isLoadingListener(false));
	}
	
	private static Flowable<Forecast> getStartLoadingStream() {
		return Flowable.just(0)
				.doOnNext(start -> ListenerManager.isLoadingListener(true))
				.subscribeOn(AndroidSchedulers.mainThread())
				.observeOn(Schedulers.io())
				.map(integer -> new Forecast());
	}
	
	static void downloadForecastsForCoordinates() {
		
		Maybe<Forecast> forecastObservable = GPSLocation.getLocation()
				.firstElement()
				.flatMap(location -> ForecastDownload.getForecastRequestForCoordinates(String.valueOf(location.getLatitude()),
				                                                                       String.valueOf(location.getLongitude()),
				                                                                       USED_UNIT)
						.subscribeOn(Schedulers.io()));
		
		disposables.add(getLoadingDisposable(forecastObservable));
	}
	
	private static Disposable getLoadingDisposable(Maybe<Forecast> forecastSingle) {
		return forecastSingle.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ListenerManager::onSuccessListener,
				           ListenerManager::onErrorListener,
				           () -> ListenerManager.isLoadingListener(false));
	}
	
	static void downloadNewForecastFor(String cityName) {
		disposables.add(getLoadingDisposable(ForecastDownload.downloadNewForecastFor(getStartLoadingStream(),
		                                                                             cityName)
				                                     .doOnError(throwable -> ListenerManager.isLoadingListener(false))));
	}
	
	static void readForecastFor(int cityID) {
		disposables.add(getLoadingDisposable(DatabaseManager.readForecastFor(cityID)));
	}
	
	static void downloadDataForNewUnits() {
		Flowable<Forecast> startLoadingForecasts = getStartLoadingForecastStream();
		
		Flowable<Forecast> refreshForecast = getRefreshingForecastStream(startLoadingForecasts);
		
		disposables.add(getLoadingDisposable(refreshForecast));
	}
}
