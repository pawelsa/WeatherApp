package com.example.pawel.weatherapp.android;

import android.util.Log;

import com.example.pawel.weatherapp.WeatherModels.ForecastModel;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

class ForecastListViewModel
		extends ViewModel {
	
	private MutableLiveData<List<ForecastModel>> list;
	private MutableLiveData<Boolean> wasDownloaded;
	
	LiveData<List<ForecastModel>> getObservableList() {
		if ( list == null || list.getValue() == null ) {
			list = new MutableLiveData<>();
			list.setValue(new ArrayList<>());
		}
		return list;
	}
	
	LiveData<Boolean> observeWasDownloaded() {
		if ( wasDownloaded == null ) {
			wasDownloaded = new MutableLiveData<>();
			wasDownloaded.setValue(list != null && list.getValue() != null && list.getValue().size() != 0);
		}
		return wasDownloaded;
	}
	
	void addForecast(ForecastModel forecast) {
		List<ForecastModel> value = list.getValue();
		if ( value == null ) {
			value = new ArrayList<>();
		}
		if ( value.contains(forecast) ) {
			Log.d("Contains", "ViewModel YES");
			int index = value.indexOf(forecast);
			value.set(index, forecast);
		} else {
			Log.d("Contains", "ViewModel NO");
			value.add(forecast);
		}
		list.setValue(value);
	}
	
	void setDownloaded(boolean isDownloaded) {
		wasDownloaded.setValue(isDownloaded);
	}
}
