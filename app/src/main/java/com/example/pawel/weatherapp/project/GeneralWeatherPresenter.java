package com.example.pawel.weatherapp.project;

import android.util.Log;

import com.example.pawel.weatherapp.Tools.ExceptionTester;
import com.example.pawel.weatherapp.Tools.NoInternetConnection;
import com.example.pawel.weatherapp.Tools.NotExists;
import com.example.pawel.weatherapp.WeatherModels.ForecastModel;
import com.example.pawel.weatherapp.android.AddLocalizationBottomSheet;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
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
			public void onSuccess(ForecastWithPhoto forecast) {
				Log.d("Presenter", "ForecastToView for : " + forecast.getCityName());
				view.addItemToAdapter(new ForecastModel(forecast));
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
			public void removedForecast(ForecastWithPhoto forecast) {
				Log.d("Presenter", "Remove : " + forecast.getCityName());
				view.removeForecastFromAdapter(new ForecastModel(forecast));
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
