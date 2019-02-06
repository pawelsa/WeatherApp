package com.example.pawel.weatherapp;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.SeekBar;

import com.example.pawel.weatherapp.WeatherModels.ForecastModel;
import com.example.pawel.weatherapp.WeatherModels.HourlyWeather;
import com.example.pawel.weatherapp.project.ColorHelper;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.WeatherIcons;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class ForecastToView
        extends ForecastModel {


    public ObservableInt dispWeatherIcon = new ObservableInt(0);
    public ObservableField<String> dispTemp = new ObservableField<>("");
    public ObservableField<String> dispHumidity = new ObservableField<>("");
    public ObservableField<String> dispWind = new ObservableField<>("");
    public ObservableField<String> dispDescription = new ObservableField<>("");
    public ObservableField<String> dispTempMin = new ObservableField<>("");
    public ObservableField<String> dispTempMax = new ObservableField<>("");
    public ObservableField<String> dispPressure = new ObservableField<>("");
    public ObservableField<String> dispClouds = new ObservableField<>("");
    public ObservableField<String> dispRain = new ObservableField<>("");
    public ObservableField<String> dispSnow = new ObservableField<>("");
    public ObservableField<String> cityName = new ObservableField<>("");
    public ObservableBoolean downloaded = new ObservableBoolean(false);

    private int[] tempArray;

    public ForecastToView() {
    }

    public ForecastToView(ForecastWithPhoto forecast) {
        super(forecast);
        initView();
    }

    public ForecastToView(ForecastModel forecastModel) {
        super(forecastModel);
        initView();
    }

    @BindingAdapter("bind:init")
    public static void onTempChanged(SeekBar seekBar, int[] tempArray) {
        int progressColor = ColorHelper.getTempColor(tempArray, 0);
        int thumbColor = ColorHelper.getThumbColor(tempArray, 0);
        seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
    }

    public int[] getTempArray() {
        return tempArray;
    }

    private void initView() {
        this.cityName.set(getCityName());
        downloaded.set(this.isDownloaded());
        if (this.isDownloaded()) {
            setDisplayValue(0);
            tempArray = new int[5];
            for (int i = 0; i < 5; i++) {
                tempArray[i] =
                        ColorHelper.colorForTemperature((float) getWeatherList().get(i)
                                .getTemp());
            }
        }
    }

    private void setDisplayValue(int position) {
        HourlyWeather hourlyWeather = getWeatherList().get(position);
        setDisplayValue(hourlyWeather);
    }

    public void setDisplayValue(HourlyWeather hourlyWeather) {
        try {
            dispWeatherIcon.set(WeatherIcons.getIcon(hourlyWeather.getIcon()));
            dispTemp.set(String.valueOf(hourlyWeather.getTemp()));
            dispHumidity.set(String.valueOf(hourlyWeather.getHumidity()));
            dispWind.set(String.valueOf(hourlyWeather.getWindSpeed()));
            dispDescription.set(hourlyWeather.getDescription());
            dispTempMin.set(String.valueOf(hourlyWeather.getTemp_min()));
            dispTempMax.set(String.valueOf(hourlyWeather.getTemp_max()));
            dispPressure.set(String.valueOf(hourlyWeather.getPressure()));
            dispClouds.set(String.valueOf(hourlyWeather.getClouds()));
            dispRain.set(String.valueOf(hourlyWeather.getRain()));
            dispSnow.set(String.valueOf(hourlyWeather.getSnow()));
        } catch (Exception e) {
            Log.e("ForecastWithPhoto", "Set display value\n" + e.getMessage());
        }
    }

    public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
        setDisplayValue(progress);
        int progressColor = ColorHelper.getTempColor(tempArray, progress / 5);
        int thumbColor = ColorHelper.getThumbColor(tempArray, progress);
        seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
        seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
