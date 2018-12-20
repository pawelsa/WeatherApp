package com.example.weatherlibwithcityphotos;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlib.project.WeatherModel.Forecast;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class EForecast
		extends Forecast {
	
	public ObservableInt dispWeatherIcon = new ObservableInt(0);
	public String photoReference;
	public ObservableField<String> dispTemp = new ObservableField<>("");
	public ObservableField<String> dispHumidity = new ObservableField<>("");
	public ObservableField<String> dispWind = new ObservableField<>("");
	public ObservableField<String> dispDescription = new ObservableField<>("");
	private int[] tempArray;
	
	EForecast() {
	}
	
	EForecast(Forecast forecast) {
		this.city = forecast.city;
		this.ID = forecast.ID;
		this.list = forecast.list;
		if ( forecast.isDownloaded() ) {
			setDisplayValue(0);
			tempArray = new int[5];
			for ( int i = 0; i < 5; i++ ) {
				tempArray[i] = ColorHelper.Companion.colorForTemperature(( float ) this.list.get(i).main.temp);
			}
		}
	}
	
	private void setDisplayValue(int position) {
		CurrentWeather item = this.list.get(position);
		dispWeatherIcon.set(WeatherIcons.getIcon(item.weather.get(0).icon));
		dispTemp.set(String.valueOf(item.main.temp) + " C");
		dispHumidity.set(String.valueOf(item.main.humidity));
		dispWind.set(String.valueOf(item.wind.speed));
		dispDescription.set(item.weather.get(0).description);
	}
	
	@BindingAdapter( "imageUrl" )
	public static void loadImage(ImageView view, String url) {
		Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	@BindingAdapter( "imageUrl" )
	public static void loadImage(ImageView view, int resourceID) {
		Glide.with(view.getContext()).load(resourceID).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setDisplayValue(progress);
		int progressColor = ColorHelper.Companion.getTempColor(progress / 5, tempArray);
		Log.i("Color", "Progress : " + progress + " Color : " + progressColor);
		int thumbColor = ColorHelper.Companion.getThumbColor(progress, tempArray);
		seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
		seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
	}
	
	public String getCityName() {
		return this.city.name;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
}
