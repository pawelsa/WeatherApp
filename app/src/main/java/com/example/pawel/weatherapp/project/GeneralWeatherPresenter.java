package com.example.pawel.weatherapp.project;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.pawel.weatherapp.android.AddLocalizationBottomSheet;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlib.project.newVersion.ForecastListener;

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
/*        getAdapterData = ForecastDownload.toAdapter()
                .subscribe(forecast -> view.addItemToAdapter(forecast), Throwable::printStackTrace, () -> Log.i("Data for adapter", "Completed"));*/
    
        com.example.weatherlib.project.newVersion.ForecastDownload.start(new ForecastListener() {
            @Override
            public void onSuccess(Forecast forecast) {
                Log.d("Presenter", "Forecast for : " + forecast.city.name);
                view.addItemToAdapter(forecast);
            }
        
            @Override
            public void onError(Throwable t) {
                Log.d("Presenter", "Error : " + t.getMessage());
            }
        
            @Override
            public void isLoading(boolean loading) {
                Log.d("Presenter", "Loading : " + String.valueOf(loading));
            }
        });
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
