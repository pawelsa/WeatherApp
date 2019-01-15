package com.example.pawel.weatherapp.project;

import android.util.Log;

import com.example.pawel.weatherapp.android.AddLocalizationBottomSheet;
import com.example.weatherlib.project.Tools.ExceptionTester;
import com.example.weatherlib.project.Tools.NoInternetConnection;
import com.example.weatherlib.project.Tools.NotExists;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.EForecast;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import androidx.fragment.app.FragmentActivity;

public class GeneralWeatherPresenter {
	
	private ForecastsListener listener;
	private GeneralWeatherInterface view;
	private AddLocalizationBottomSheet bottomSheet;
	
	public GeneralWeatherPresenter(GeneralWeatherInterface view) {
		this.view = view;
	}
	
	public void showAddLocalizationSheet(FragmentActivity activity) {
		bottomSheet = new AddLocalizationBottomSheet();
		bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
	}
	
	public void addItemsToAdapter() {
		
		listener = getForecastListener();
		MainLib.addListener(listener);
		MainLib.streamForecastsWithRefresh();
	}
	
	private ForecastsListener getForecastListener() {
		return new ForecastsListener() {
			@Override
			public void onSuccess(EForecast forecast) {
				Log.d("Presenter", "Forecast for : " + forecast.city.name);
				view.addItemToAdapter(forecast);
			}
			
			@Override
			public void onError(Throwable t) {
				Log.d("Presenter", "Error : " + t.getMessage());
				if ( ExceptionTester.matchesException(t, NotExists.class) ||
				     ExceptionTester.matchesException(t, NoInternetConnection.class) ) {
					view.showSnackbar(t.getMessage());
				}
				view.isRefreshing(false);
				t.printStackTrace();
			}
			
			@Override
			public void isLoading(boolean loading) {
				Log.d("Presenter", "Loading : " + String.valueOf(loading));
				view.isRefreshing(loading);
			}
			
			@Override
			public void removedForecast(Forecast forecast) {
				Log.d("Presenter", "Remove : " + forecast.city.name);
				view.removeForecastFromAdapter(new EForecast(forecast));
			}
		};
	}
	
	public void refreshForecast() {
		MainLib.refreshForecast();
	}
	
	public void onResume() {
		if ( listener == null ) {
			listener = getForecastListener();
		}
		MainLib.addListener(listener);
	}
	
	public void onStop() {
		MainLib.removeListener(listener);
		MainLib.dispose();
	}
	
	public void onDestroy() {
		MainLib.dispose();
	}
}
