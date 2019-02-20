package com.example.pawel.weatherapp.android.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class MainForecastAdapter
		extends ListAdapter<ForecastToView, MainForecastAdapter.ForecastCardViewHolder> {
	
	private static final String TAG = MainForecastAdapter.class.getName();
	
	private ForecastClickListener listener;
	
	
	MainForecastAdapter(ForecastClickListener listener) {
		super(new ForecastDiffCallback());
		this.listener = listener;
	}
	
	@NonNull
	public ForecastCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		CardMainForecastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
		                                                          R.layout.card_main_forecast,
		                                                          parent,
		                                                          false);
		return new ForecastCardViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ForecastCardViewHolder holder, int position) {
		holder.bind(getItem(position), position);
	}
	
	class ForecastCardViewHolder
			extends RecyclerView.ViewHolder {
		final CardMainForecastBinding binding;
		
		ForecastCardViewHolder(CardMainForecastBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
		
		void bind(ForecastToView forecastToView, int position) {
			Log.d(TAG, "bind: " + position + " " + forecastToView.getCityName());
			binding.setForecast(forecastToView);
			binding.clCardMain.setOnClickListener(v -> listener.onForecastClicked(binding));
			binding.clCardMain.setOnLongClickListener(v -> {
				listener.onForecastLongClicked(forecastToView.getCityName(), forecastToView.getCityID());
				return false;
			});
			binding.executePendingBindings();
		}
		
	}
	
	interface ForecastClickListener {
		void onForecastClicked(CardMainForecastBinding binding);
		
		void onForecastLongClicked(String cityName, int cityID);
	}
	
}
