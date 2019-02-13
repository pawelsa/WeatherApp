package com.example.pawel.weatherapp.android.main;

import android.util.Log;
import android.util.Pair;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;

import java.util.List;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainFragmentViewModel
		extends ViewModel {
	
	private static final String TAG = MainFragmentViewModel.class.getSimpleName();
	public MutableLiveData<CardMainForecastBinding> selected;
	public MutableLiveData<Pair> selectedToRemove;
	public ObservableInt loading;
	private ForecastsList forecastMemory;
	private MainForecastAdapter adapter;
	private MainForecastAdapter.ForecastClickListener adapterListener = getAdapterListener();
	
	public void init() {
		if ( forecastMemory != null ) {
			forecastMemory.destroy();
		}
		forecastMemory = new ForecastsList();
		selected = new MutableLiveData<>();
		selectedToRemove = new MutableLiveData<>();
		adapter = new MainForecastAdapter(adapterListener);
		loading = new ObservableInt(0);
	}
	
	public void fetchList() {
		forecastMemory.fetchList();
	}
	
	public MutableLiveData<List<ForecastToView>> getForecasts() {
		return forecastMemory.getLiveForecastList();
	}
	
	public MainForecastAdapter getAdapter() {
		return adapter;
	}
	
	public void setForecastsInAdapter(List<ForecastToView> breeds) {
		Log.d(TAG, "setForecastsInAdapter: " + breeds.size());
		this.adapter.setForecasts(breeds);
		this.adapter.notifyDataSetChanged();
	}
	
	public MutableLiveData<CardMainForecastBinding> getSelected() {
		return selected;
	}
	
	public MutableLiveData<Pair> getSelectedToRemove() {
		return selectedToRemove;
	}
	
	public void onRefresh() {
		forecastMemory.refreshList();
	}
	
	public void setLoading(boolean loading) {
		this.loading.set(loading ? 1 : 0);
	}
	
	private MainForecastAdapter.ForecastClickListener getAdapterListener() {
		if ( adapterListener == null ) {
			adapterListener = new MainForecastAdapter.ForecastClickListener() {
				@Override
				public void onForecastClicked(CardMainForecastBinding binding) {
					selected.setValue(binding);
				}
				
				@Override
				public void onForecastLongClicked(String cityName, int cityID) {
					Pair<String, Integer> pair = Pair.create(cityName, cityID);
					selectedToRemove.setValue(pair);
				}
			};
		}
		return adapterListener;
	}
}
