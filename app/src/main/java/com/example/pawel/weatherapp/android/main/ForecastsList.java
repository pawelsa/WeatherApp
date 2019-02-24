package com.example.pawel.weatherapp.android.main;

import android.util.Log;

import com.example.pawel.weatherapp.weatherModels.ForecastToView;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

class ForecastsList
		extends BaseObservable {
	
	private static final String TAG = ForecastsList.class.getName();
	
	private List<ForecastToView> forecastToViewList = new ArrayList<>();
	private MutableLiveData<List<ForecastToView>> liveForecastList = new MutableLiveData<>();
	private ForecastsListener listener = prepareForecastsListener();
	
	ForecastsList() {
		fetchList();
	}
	
	void fetchList() {
		Log.d(TAG, "fetchList: ");
		MainLib.addListener(listener);
		MainLib.streamForecastsWithRefresh();
	}
	
	ForecastsList(List<ForecastToView> forecastToViewList) {
		this.forecastToViewList = forecastToViewList;
		liveForecastList.setValue(forecastToViewList);
	}
	
	List<ForecastToView> getForecastToViewList() {
		return forecastToViewList;
	}
	
	void refreshList() {
		Log.d(TAG, "refreshList: ");
		MainLib.addListener(listener);
		MainLib.refreshForecast();
	}
	
	void destroy() {
		if ( listener != null ) {
			listener = null;
		}
	}
	
	MutableLiveData<List<ForecastToView>> getLiveForecastList() {
		return liveForecastList;
	}
	
	private ForecastsListener prepareForecastsListener() {
		if ( listener == null ) {
			listener = new ForecastsListener() {
				@Override
				public void onSuccess(ForecastWithPhoto forecast) {
					Log.d(TAG, "Adding forecast");
					ForecastToView forecastToView = new ForecastToView(forecast);
					addForecast(forecastToView);
				}
				
				@Override
				public void onError(Throwable t) {
				}
				
				@Override
				public void isLoading(boolean loading) {
				}
				
				@Override
				public void removedForecast(ForecastWithPhoto forecast) {
					Log.d(TAG, "removedForecast");
					ForecastToView forecastToView = new ForecastToView(forecast);
					removeForecast(forecastToView);
				}
			};
		}
		return listener;
	}
	
	private void addForecast(ForecastToView forecastToView) {
		int index = forecastToViewList.indexOf(forecastToView);
		if ( index == - 1 ) {
			forecastToViewList.add(forecastToView);
		} else {
			forecastToViewList.set(index, forecastToView);
		}
		liveForecastList.setValue(forecastToViewList);
	}
	
	private void removeForecast(ForecastToView forecast) {
		int index = forecastToViewList.indexOf(forecast);
		if ( index > - 1 ) {
			forecastToViewList.remove(index);
		}
		liveForecastList.setValue(forecastToViewList);
	}
}
