package com.example.pawel.weatherapp.android.main;

import android.util.Log;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainFragmentViewModel extends ViewModel {

    private final String TAG = "MainFragmentViewModel";
    public ObservableBoolean isLoading;
    private MutableLiveData<List<ForecastToView>> forecastToViewList;
    private MainAdapter adapter;


    void init(MainAdapter.OnForecastClicked listener) {
        forecastToViewList = new MutableLiveData<>();
        forecastToViewList.setValue(new ArrayList<>());
        isLoading = new ObservableBoolean();
        adapter = new MainAdapter(listener);
        adapter.setForecastList(getForecastList().getValue());
    }

    LiveData<List<ForecastToView>> getForecastList() {
        return forecastToViewList;
    }

    public MainAdapter getAdapter() {
        return adapter;
    }

    public void onRefresh() {
        Log.d(TAG, "onRefresh: ");
        MainLib.refreshForecast();
    }

    void setForecastsToAdapter(List<ForecastToView> forecastsToAdapter) {
        adapter.setForecastList(forecastsToAdapter);
    }

    void setLoading(boolean loading) {
        this.isLoading.set(loading);
    }

    void addNewForecast(ForecastToView forecast) {
        List<ForecastToView> forecastToViews = this.forecastToViewList.getValue();
        int index = forecastToViews.indexOf(forecast);
        if (index > -1) {
            forecastToViews.set(index, forecast);
        } else {
            forecastToViews.add(forecast);
        }
        this.forecastToViewList.setValue(forecastToViews);
    }

    void removeForecast(ForecastToView forecast) {
        List<ForecastToView> forecastToViews = this.forecastToViewList.getValue();
        int index = forecastToViews.indexOf(forecast);
        if (index > -1) {
            forecastToViews.remove(index);
            this.forecastToViewList.setValue(forecastToViews);
        }
    }
}
