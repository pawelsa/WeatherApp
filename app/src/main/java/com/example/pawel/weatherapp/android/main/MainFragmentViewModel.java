package com.example.pawel.weatherapp.android.main;

import android.util.Pair;

import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;
import com.example.pawel.weatherapp.weatherModels.ForecastToView;

import java.util.List;

import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainFragmentViewModel
		extends ViewModel {
	
	private static final String TAG = MainFragmentViewModel.class.getSimpleName();
	private MutableLiveData<CardMainForecastBinding> selected;
	private MutableLiveData<Pair> selectedToRemove;
	public ObservableInt loading;
	private ForecastsList forecastMemory;
	private MainForecastAdapter adapter;
	private MainForecastAdapter.ForecastClickListener adapterListener = getAdapterListener();
	
	void init() {
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
	
	public void onRefresh() {
		forecastMemory.refreshList();
	}
	
	public MainForecastAdapter getAdapter() {
		return adapter;
	}
	
	MutableLiveData<List<ForecastToView>> getForecasts() {
		return forecastMemory.getLiveForecastList();
	}
	
	void setForecastsInAdapter(List<ForecastToView> forecastToViewList) {
		this.adapter.submitList(forecastToViewList);
		this.adapter.notifyDataSetChanged();
	}
	
	MutableLiveData<CardMainForecastBinding> getSelected() {
		return selected;
	}
	
	MutableLiveData<Pair> getSelectedToRemove() {
		return selectedToRemove;
	}
	
	void setLoading(boolean loading) {
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
