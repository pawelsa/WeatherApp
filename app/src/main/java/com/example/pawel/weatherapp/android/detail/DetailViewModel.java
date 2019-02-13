package com.example.pawel.weatherapp.android.detail;

import android.widget.Adapter;

import com.example.pawel.weatherapp.ForecastToView;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel
		extends ViewModel {
	
	public ObservableBoolean isLoading;
	private MutableLiveData<ForecastToView> forecastToView;
	private Adapter adapter;
	
	void init() {
		forecastToView = new MutableLiveData<>();
		isLoading = new ObservableBoolean();
		//  TODO : adapter init, and pushing list of HourlyWeather
	}
	
	public LiveData<ForecastToView> getForecast() {
		return forecastToView;
	}
	
	public Adapter getAdapter() {
		return adapter;
	}
	
	void setLoading(boolean loading) {
		this.isLoading.set(loading);
	}
	
	void addNewForecast(ForecastToView forecast) {
		forecastToView.setValue(forecast);
	}
	
}
