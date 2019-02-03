package com.example.pawel.weatherapp;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pawel.weatherapp.WeatherModels.ForecastModel;
import com.example.pawel.weatherapp.WeatherModels.HourlyWeather;
import com.example.weatherlibwithcityphotos.ColorHelper;
import com.example.weatherlibwithcityphotos.ForecastWithPhoto;
import com.example.weatherlibwithcityphotos.WeatherIcons;

import java.text.Collator;

import androidx.databinding.BindingAdapter;
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
	
	private int[] tempArray;
	
	public ForecastToView() {
	}
	
	public ForecastToView(ForecastWithPhoto forecast) {
		super(forecast);
		
		if ( this.isDownloaded() ) {
			setDisplayValue(0);
			tempArray = new int[5];
			for ( int i = 0; i < 5; i++ ) {
				tempArray[i] =
						ColorHelper.Companion.colorForTemperature(( float ) getWeatherList().get(i)
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
	
	
	public ForecastToView(ForecastModel forecastModel) {
		super(forecastModel);
		
		if ( this.isDownloaded() ) {
			setDisplayValue(0);
			tempArray = new int[5];
			for ( int i = 0; i < 5; i++ ) {
				tempArray[i] =
						ColorHelper.Companion.colorForTemperature(( float ) getWeatherList().get(i)
								.getTemp());
			}
		}
	}
	
	@BindingAdapter( "imageUrl" )
	public static void loadImage(ImageView view, String url) {
		Log.i("Detail", "Load image: " + (url == null ? "null" : "non null"));
		Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	@BindingAdapter( "imageUrl" )
	public static void loadImage(ImageView view, int resourceID) {
		Glide.with(view.getContext()).load(resourceID).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	public String getPhotoReference() {
		return getCity().getPhotoReference();
	}
	
	@Override
	public int hashCode() {
		super.hashCode();
		return getCity().getID();
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		Collator instance = Collator.getInstance();
		instance.setStrength(Collator.NO_DECOMPOSITION);
		if ( obj instanceof ForecastToView ) {
			ForecastToView other = ( ForecastToView ) obj;
			int equalName = instance.compare(this.getCityName(), other.getCityName());
			result = this.getCity().getID() == other.getCity().getID() || equalName == 0;
		} else if ( obj instanceof String ) {
			String otherName = ( String ) obj;
			int equalName = instance.compare(this.getCity().getName(), otherName);
			result = equalName == 0;
		}
		return result;
	}
	
	public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setDisplayValue(progress);
		int progressColor = ColorHelper.Companion.getTempColor(progress / 5, tempArray);
		int thumbColor = ColorHelper.Companion.getThumbColor(progress, tempArray);
		seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
		seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
	}
}
