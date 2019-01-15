package com.example.weatherlibwithcityphotos;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.WeatherModels.City;
import com.example.weatherlibwithcityphotos.WeatherModels.CurrentWeather;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;

import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class EForecast {
	
	public boolean downloaded = false;
	public List<CurrentWeather> weatherList;
	public City city;
	
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
	
	public EForecast() {
	}
	
	public EForecast(Forecast forecast) {
		this.city = new com.example.weatherlibwithcityphotos.WeatherModels.City(forecast.city);
		this.weatherList = convertWeatherList(forecast.list);
		this.downloaded = forecast.isDownloaded();
		if ( downloaded ) {
			setDisplayValue(0);
			tempArray = new int[5];
			for ( int i = 0; i < 5; i++ ) {
				tempArray[i] = ColorHelper.Companion.colorForTemperature(( float ) this.weatherList.get(i)
						.getMain()
						.getTemp());
			}
		}
	}
	
	public String getPhotoReference() {
		return city.getPhotoReference();
	}
	
	private List<CurrentWeather> convertWeatherList(
			List<com.example.weatherlib.project.WeatherModel.CurrentWeather> list) {
		List<CurrentWeather> newList = new ArrayList<>();
		if ( list != null ) {
			for ( com.example.weatherlib.project.WeatherModel.CurrentWeather item : list ) {
				newList.add(new CurrentWeather(item));
			}
		}
		return newList;
	}
	
	
	private void setDisplayValue(int position) {
		CurrentWeather currentWeather = this.weatherList.get(position);
		setDisplayValue(currentWeather);
	}
	
	public void setDisplayValue(CurrentWeather currentWeather) {
		try {
			dispWeatherIcon.set(WeatherIcons.getIcon(currentWeather.getWeather().getIcon()));
			dispTemp.set(String.valueOf(currentWeather.getMain().getTemp()));
			dispHumidity.set(String.valueOf(currentWeather.getMain().getHumidity()));
			dispWind.set(String.valueOf(currentWeather.getWindSpeed()));
			dispDescription.set(currentWeather.getWeather().getDescription());
			dispTempMin.set(String.valueOf(currentWeather.getMain().getTemp_min()));
			dispTempMax.set(String.valueOf(currentWeather.getMain().getTemp_max()));
			dispPressure.set(String.valueOf(currentWeather.getMain().getPressure()));
			dispClouds.set(String.valueOf(currentWeather.getClouds()));
			dispRain.set(String.valueOf(currentWeather.getRain()));
			dispSnow.set(String.valueOf(currentWeather.getSnow()));
		} catch (Exception e) {
			Log.e("EForecast", "Set display value\n" + e.getMessage());
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
	
	public void onValueChanged(SeekBar seekBar, int progress, boolean fromUser) {
		setDisplayValue(progress);
		int progressColor = ColorHelper.Companion.getTempColor(progress / 5, tempArray);
		int thumbColor = ColorHelper.Companion.getThumbColor(progress, tempArray);
		seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
		seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
	}
	
	public String getCityName() {
		return this.city.getName();
	}
	
	@Override
	public int hashCode() {
		super.hashCode();
		return city.getID();
	}
	
	@Override
	public boolean equals(Object obj) {
		super.equals(obj);
		
		boolean result = false;
		Collator instance = Collator.getInstance();
		instance.setStrength(Collator.NO_DECOMPOSITION);
		if ( obj instanceof EForecast ) {
			EForecast other = ( EForecast ) obj;
			int equalName = instance.compare(this.city.getName(), other.city.getName());
			result = this.city.getID() == other.city.getID() || equalName == 0;
		} else if ( obj instanceof Forecast ) {
			Forecast other = ( Forecast ) obj;
			int equalName = instance.compare(this.city.getName(), other.city.name);
			result = this.city.getID() == other.city.id || equalName == 0;
		} else if ( obj instanceof String ) {
			String otherName = ( String ) obj;
			int equalName = instance.compare(this.city.getName(), otherName);
			result = equalName == 0;
		}
		return result;
	}
}
