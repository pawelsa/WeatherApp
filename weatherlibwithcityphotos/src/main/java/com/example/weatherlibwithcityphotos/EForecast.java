package com.example.weatherlibwithcityphotos;

import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherlib.project.WeatherModel.Forecast;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;

public class EForecast
		extends Forecast {
	
	public String photoReference;
	public ObservableField<String> dispTemp = new ObservableField<>("");
	public ObservableField<String> dispHumidity = new ObservableField<>("");
	public ObservableField<String> dispWind = new ObservableField<>("");
	public ObservableField<String> dispDescription = new ObservableField<>("");
	
	EForecast() {
	}
	
	EForecast(Forecast forecast) {
		this.city = forecast.city;
		this.ID = forecast.ID;
		this.list = forecast.list;
		if ( forecast.isDownloaded() ) {
			setDisplayValue(0);
		}
	}
	
	private void setDisplayValue(int position) {
		dispTemp.set(String.valueOf(this.list.get(position).main.temp) + " C");
		dispHumidity.set(String.valueOf(this.list.get(position).main.humidity));
		dispWind.set(String.valueOf(this.list.get(position).wind.speed));
		dispDescription.set(this.list.get(position).weather.get(0).description);
	}
	
	@BindingAdapter( "imageUrl" )
	public static void loadImage(ImageView view, String url) {
		Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	public void onValueChanged(SeekBar seekBar, int progresValue, boolean fromUser) {
		setDisplayValue(progresValue);
	}
	
	public String getCityName() {
		return this.city.name;
	}
}
