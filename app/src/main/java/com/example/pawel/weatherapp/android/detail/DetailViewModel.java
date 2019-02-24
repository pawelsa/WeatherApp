package com.example.pawel.weatherapp.android.detail;

import com.example.pawel.weatherapp.weatherModels.ForecastToView;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel
		extends ViewModel {
	
	public ObservableBoolean isLoading;
	private MutableLiveData<ForecastToView> forecastToView;
	private HourlyListAdapter adapter;
	
	void init() {
		forecastToView = new MutableLiveData<>();
		isLoading = new ObservableBoolean();
		adapter = new HourlyListAdapter(position -> {
			if ( position > - 1 ) {
				forecastToView.getValue().setDisplayValue(position);
			}
		});
	}
	
	public LiveData<ForecastToView> getForecast() {
		return forecastToView;
	}
	
	public HourlyListAdapter getAdapter() {
		return adapter;
	}
	
	void setLoading(boolean loading) {
		this.isLoading.set(loading);
	}
	
	void addNewForecast(ForecastToView forecast) {
		forecastToView.setValue(forecast);
		adapter.setHourlyWeatherList(forecast.getWeatherList());
	}
	
}
