package com.example.pawel.weatherapp.android.main;

import com.example.pawel.weatherapp.weatherModels.ForecastToView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class ForecastDiffCallback
		extends DiffUtil.ItemCallback<ForecastToView> {
	
	
	@Override
	public boolean areItemsTheSame(@NonNull ForecastToView oldItem, @NonNull ForecastToView newItem) {
		return oldItem.equals(newItem);
	}
	
	@Override
	public boolean areContentsTheSame(@NonNull ForecastToView oldItem, @NonNull ForecastToView newItem) {
		boolean result = false;
		if ( oldItem.getWeatherList() != null && ! oldItem.getWeatherList().isEmpty()
		     && newItem.getWeatherList() != null && ! newItem.getWeatherList().isEmpty() ) {
			result = oldItem.getWeatherList().get(0).getDt() == newItem.getWeatherList().get(0).getDt();
		}
		return result;
	}
}
