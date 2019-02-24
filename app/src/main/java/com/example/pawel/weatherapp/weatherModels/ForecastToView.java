package com.example.pawel.weatherapp.weatherModels;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.SeekBar;

import com.example.pawel.weatherapp.project.ColorHelper;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.WeatherIcons;

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
	
	private void initView() {
		this.cityName.set(getCityName());
		downloaded.set(this.isDownloaded());
		if ( this.isDownloaded() && ! getWeatherList().isEmpty() ) {
			setDisplayValue(0);
			tempArray = buildTempArray();
		}
	}
	
	public void setDisplayValue(int position) {
		if ( ! getWeatherList().isEmpty() ) {
			HourlyWeather hourlyWeather = getWeatherList().get(position);
			setDisplayValue(hourlyWeather);
		}
	}
	
	private int[] buildTempArray() {
		int[] array = new int[5];
		if ( ! getWeatherList().isEmpty() && getWeatherList().size() > 4 ) {
			for ( int i = 0; i < 5; i++ ) {
				array[i] =
						ColorHelper.colorForTemperature(( float ) getWeatherList().get(i)
								.getTemp());
			}
		}
		return array;
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
	
	public ForecastToView(ForecastModel forecastModel) {
		super(forecastModel);
		initView();
	}
	
	public int[] getTempArray() {
		return tempArray;
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
