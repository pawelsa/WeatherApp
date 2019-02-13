package com.example.pawel.weatherapp.android.main;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.CardMainForecastBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MainForecastAdapter
		extends RecyclerView.Adapter<MainForecastAdapter.ForecastCardViewHolder> {
	
	private static final String TAG = MainForecastAdapter.class.getName();
	
	private List<ForecastToView> forecasts = new ArrayList<>();
	private ForecastClickListener listener;
	
	public MainForecastAdapter(ForecastClickListener listener) {
		this.listener = listener;
	}
	
	@NonNull
	public ForecastCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Log.d(TAG, "onCreateViewHolder: ");
		CardMainForecastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
		                                                          R.layout.card_main_forecast,
		                                                          parent,
		                                                          false);
		return new ForecastCardViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ForecastCardViewHolder holder, int position) {
		Log.d(TAG, "onBindViewHolder: ");
		holder.bind(forecasts.get(position), position);
	}
	
	@Override
	public int getItemCount() {
		return forecasts == null ? 0 : forecasts.size();
	}
	
	public void setForecasts(List<ForecastToView> forecastToViews) {
		if ( this.forecasts != null && forecastToViews != null ) {
			DiffUtil.DiffResult diffs = DiffUtil.calculateDiff(new ForecastDiffUtil(this.forecasts, forecastToViews));
			diffs.dispatchUpdatesTo(this);
		}
		this.forecasts.clear();
		this.forecasts.addAll(forecastToViews);
		/*notifyDataSetChanged();*/
	}
	
	
	public class ForecastCardViewHolder
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
