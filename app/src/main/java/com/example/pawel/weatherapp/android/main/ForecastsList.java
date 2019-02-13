package com.example.pawel.weatherapp.android.main;

import android.util.Log;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.ForecastsListener;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

public class ForecastsList
		extends BaseObservable {
	
	String TAG = "ForecastsList";
	
	List<ForecastToView> forecastToViewList = new ArrayList<>();
	MutableLiveData<List<ForecastToView>> liveForecastList = new MutableLiveData<>();
	ForecastsListener listener;
	
	public ForecastsList() {
		fetchList();
	}
	
	public void fetchList() {
		prepareForecastsListener();
		MainLib.addListener(listener);
		MainLib.streamForecastsWithRefresh();
	}
	
	private void prepareForecastsListener() {
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
	}
	
	public void addForecast(ForecastToView forecastToView) {
		int index = forecastToViewList.indexOf(forecastToView);
		if ( index == - 1 ) {
			forecastToViewList.add(forecastToView);
		} else {
			forecastToViewList.set(index, forecastToView);
		}
		liveForecastList.setValue(forecastToViewList);
	}
	
	public void removeForecast(ForecastToView forecast) {
		int index = forecastToViewList.indexOf(forecast);
		Log.d(TAG, "removeForecast: " + index);
		if ( index > - 1 ) {
			forecastToViewList.remove(index);
		}
		liveForecastList.setValue(forecastToViewList);
	}
	
	public ForecastsList(List<ForecastToView> forecastToViewList) {
		this.forecastToViewList = forecastToViewList;
		liveForecastList.setValue(forecastToViewList);
	}
	
	public MutableLiveData<List<ForecastToView>> getLiveForecastList() {
		return liveForecastList;
	}
	
	public List<ForecastToView> getForecastToViewList() {
		return forecastToViewList;
	}
	
	public void refreshList() {
		prepareForecastsListener();
		MainLib.addListener(listener);
		MainLib.refreshForecast();
	}
	
	void destroy() {
		if ( listener != null ) {
			listener = null;
		}
	}
}
