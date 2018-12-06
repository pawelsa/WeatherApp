package com.example.pawel.weatherapp.project;

import android.util.Log;

import com.example.pawel.weatherapp.android.AddLocalizationBottomSheet;
import com.example.weatherlibwithcityphotos.EForecast;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.fragment.app.FragmentActivity;

public class GeneralWeatherPresenter {
	
	private ForecastsListener listener;
	private GeneralWeatherInterface view;
	
	public GeneralWeatherPresenter(GeneralWeatherInterface view) {
		this.view = view;
	}
	
	public void showAddLocalizationSheet(FragmentActivity activity) {
		AddLocalizationBottomSheet bottomSheet = new AddLocalizationBottomSheet();
		bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
	}
	
	public void addItemsToAdapter() {
		
		listener = new ForecastsListener() {
			@Override
			public void onSuccess(EForecast forecast) {
				Log.d("Presenter", "Forecast for : " + forecast.city.name + " " + Thread.currentThread().getName());
				view.addItemToAdapter(forecast);
			}
			
			@Override
			public void onError(Throwable t) {
				Log.d("Presenter", "Error : " + t.getMessage() + " " + Thread.currentThread().getName());
			}
			
			@Override
			public void isLoading(boolean loading) {
				Log.d("Presenter", "Loading : " + String.valueOf(loading) + " " + Thread.currentThread().getName());
				view.isRefreshing(loading);
			}
		};
		MainLib.addListener(listener);
		MainLib.streamForecastsWithRefresh();
	}
	
	public void refreshForecast() {
		MainLib.refreshForecast();
	}
	
	public void onResume() {
		if ( listener != null ) {
			MainLib.addListener(listener);
		}
	}
	
	public void onStop() {
		MainLib.removeListener(listener);
		MainLib.dispose();
	}
	
	public void onDestroy() {
		MainLib.dispose();
	}
}
