package com.example.weatherlib.project.Main;

import com.example.weatherlib.project.API.ForecastDownload;
import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.GPSLocation;
import com.example.weatherlib.project.Tools.NetworkCheck;
import com.example.weatherlib.project.Tools.NoInternetConnection;
import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.weatherlib.project.Main.WeatherLib.USED_UNIT;

public class ForecastStreams {
	
	
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
				getRefreshingForecastStream(getStartLoadingForecastStream());
		
		Flowable<Forecast> connectable = startLoadingForecasts.mergeWith(refreshForecast);
		disposables.add(getLoadingDisposable(connectable));
	}
	
	private static Flowable<Forecast> getStartLoadingForecastStream() {
		return getStartLoadingStream().flatMap(integer -> DatabaseManager.getForecasts());
	}
	
	private static Flowable<Forecast> getRefreshingForecastStream(Flowable<Forecast> entryFlowable) {
		
		Flowable<Forecast> refresh =
				entryFlowable.flatMap(forecast -> ForecastDownload.getForecastRequest(forecast.city.name, USED_UNIT)
						.subscribeOn(Schedulers.io())
						.toFlowable()
				);
		
		Flowable<Forecast> errorHandler = Flowable.just(new Forecast())
				.flatMap(forecast -> {
					ListenerManager.onErrorListener(new NoInternetConnection());
					return Flowable.empty();
				});
		
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
		
		Maybe<Forecast> forecastObservable = GPSLocation.locationLowPower()
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
	
	static void downloadDataForNewUnits() {
		Flowable<Forecast> startLoadingForecasts = getStartLoadingForecastStream();
		
		Flowable<Forecast> refreshForecast = getRefreshingForecastStream(startLoadingForecasts);
		
		disposables.add(getLoadingDisposable(refreshForecast));
	}
}
