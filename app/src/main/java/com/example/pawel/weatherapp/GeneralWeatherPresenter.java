package com.example.pawel.weatherapp;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.pawel.weatherapp.API.ForecastDownload;

import io.reactivex.disposables.Disposable;

public class GeneralWeatherPresenter {
    
    private Disposable getAdapterData;
    private GeneralWeatherInterface view;
    
    public GeneralWeatherPresenter(GeneralWeatherInterface view) {
        this.view = view;
    }
    
    public void showAddLocalizationSheet(FragmentActivity activity) {
        AddLocalizationBottomSheet bottomSheet = new AddLocalizationBottomSheet();
        bottomSheet.show(activity.getSupportFragmentManager(), bottomSheet.getTag());
    }
    
    public void addItemsToAdapter() {
        getAdapterData = ForecastDownload.startGettingData()
                .subscribe(placeWeatherData -> view.addItemToAdapter(placeWeatherData), Throwable::printStackTrace, () -> Log.i("Data for adapter", "Completed"));
    }
    
    
    public void onStop() {
        if (getAdapterData != null && !getAdapterData.isDisposed()) {
            getAdapterData.dispose();
        }
    }
    
    public void onDestroy() {
        if (getAdapterData != null && !getAdapterData.isDisposed()) {
            getAdapterData.dispose();
        }
    }
}
