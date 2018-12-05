package com.example.weatherlib.project.Main;

import com.example.weatherlib.project.API.ForecastDownload;
import com.example.weatherlib.project.Database.DatabaseManager;
import com.example.weatherlib.project.WeatherModel.Forecast;

import io.reactivex.Flowable;
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
	
	static void streamForecastsWithRefresh() {
		
		Flowable<Forecast> startLoadingForecasts = getStartLoadingForecastStream()
				.publish()
				.autoConnect(2);
		
		Flowable<Forecast> refreshForecast = getRefreshingForecastStream(startLoadingForecasts);
		
		Flowable<Forecast> connectedForecastStream =
				startLoadingForecasts.mergeWith(refreshForecast);
		
		disposables.add(getLoadingDisposable(connectedForecastStream));
	}
	
	private static Flowable<Forecast> getStartLoadingForecastStream() {
		return getStartLoadingStream()
				.flatMap(integer -> DatabaseManager.getForecasts());
	}
	
	private static Flowable<Forecast> getStartLoadingStream() {
		return Flowable.just(0)
				.doOnNext(start -> ListenerManager.isLoadingListener(true))
				.observeOn(Schedulers.io())
				.map(integer -> new Forecast());
	}
	
	private static Flowable<Forecast> getRefreshingForecastStream(Flowable<Forecast> entryFlowable) {
		return entryFlowable
				.flatMap(forecast -> ForecastDownload.getForecastRequest(forecast.city.name, USED_UNIT)
						.subscribeOn(Schedulers.io())
						.onErrorResumeNext(Flowable.just(forecast)));
	}
	
	private static Disposable getLoadingDisposable(Flowable<Forecast> forecastStream) {
		return forecastStream
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(ListenerManager::onSuccessListener,
				           ListenerManager::onErrorListener,
				           () -> ListenerManager.isLoadingListener(false));
	}
	
	static void downloadNewForecastFor(String cityName) {
		disposables.add(getLoadingDisposable(ForecastDownload.downloadNewForecastFor(getStartLoadingStream(),
		                                                                             cityName)));
	}
	
	static void downloadDataForNewUnits() {
		Flowable<Forecast> startLoadingForecasts = getStartLoadingForecastStream();
		
		Flowable<Forecast> refreshForecast = getRefreshingForecastStream(startLoadingForecasts);
		
		disposables.add(getLoadingDisposable(refreshForecast));
	}
}
