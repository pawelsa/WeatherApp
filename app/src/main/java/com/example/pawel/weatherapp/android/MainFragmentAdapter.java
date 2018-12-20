package com.example.pawel.weatherapp.android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.CardLayoutBinding;
import com.example.pawel.weatherapp.databinding.CardLayoutNoBinding;
import com.example.weatherlib.project.WeatherModel.Forecast;
import com.example.weatherlibwithcityphotos.EForecast;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragmentAdapter
		extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	private final int ONLINE = 1;
	private final int OFFLINE = 0;
	
	private List<EForecast> placeWeatherDataList;
	private Context context;
	
	MainFragmentAdapter() {
		placeWeatherDataList = new ArrayList<>();
	}
	
	@NonNull
	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		return viewType == ONLINE
		       ? new CardViewHolderNoCity(CardLayoutBinding.inflate(LayoutInflater.from(parent.getContext()),
		                                                            parent,
		                                                            false))
		       : new CardViewHolderNoCity(CardLayoutNoBinding.inflate(LayoutInflater.from(parent.getContext()),
		                                                              parent,
		                                                              false));
	}
	
	@Override
	public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if ( holder instanceof CardViewHolderNoCity ) {
			CardViewHolderNoCity viewHolderCity = ( CardViewHolderNoCity ) holder;
			viewHolderCity.bind(position);
		}
	}
	
	@Override
	public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
		super.onAttachedToRecyclerView(recyclerView);
		context = recyclerView.getContext();
	}
	
	@Override
	public int getItemViewType(int position) {
		return placeWeatherDataList.get(position)
				       .isDownloaded() ? ONLINE : OFFLINE;
	}
	
	@Override
	public int getItemCount() {
		return placeWeatherDataList.size();
	}
	
	public void add(EForecast placeWeatherData) {
		if ( placeWeatherDataList.contains(placeWeatherData) ) {
			Log.d("Contains", "YES");
			int index = placeWeatherDataList.indexOf(placeWeatherData);
			placeWeatherDataList.set(index, placeWeatherData);
			notifyItemChanged(index);
		} else {
			Log.d("Contains", "NO");
			placeWeatherDataList.add(placeWeatherData);
			notifyItemInserted(placeWeatherDataList.size() - 1);
		}
	}
	
	void removeCity(Forecast forecast) {
		if ( placeWeatherDataList.remove(forecast) ) {
			notifyDataSetChanged();
		}
	}
	
	class CardViewHolderNoCity
			extends RecyclerView.ViewHolder {
		
		int position;
		CardLayoutNoBinding noBinding = null;
		CardLayoutBinding binding = null;
		
		CardViewHolderNoCity(CardLayoutNoBinding itemView) {
			super(itemView.getRoot());
			this.noBinding = itemView;
		}
		
		CardViewHolderNoCity(CardLayoutBinding itemView) {
			super(itemView.getRoot());
			this.binding = itemView;
		}
		
		void bind(int position) {
			this.position = position;
			EForecast item = placeWeatherDataList.get(position);
			View.OnLongClickListener removeListener = v -> {
				displayDialog(item);
				return false;
			};
			if ( noBinding != null ) {
				noBinding.setForecast(item);
				noBinding.cardForecastOnline.setOnLongClickListener(removeListener);
			} else {
				binding.setForecast(item);
				binding.cardForecastOnline.setOnLongClickListener(removeListener);
				binding.sbCardTime.setProgress(0);
				binding.invalidateAll();
			}
		}
		
		private void displayDialog(Forecast forecast) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getText(R.string.dialog_remove_city_make_sure))
					.setMessage(String.format(context.getString(R.string.dialog_remove_city_content),
					                          forecast.city.name))
					.setPositiveButton(context.getString(android.R.string.ok),
					                   (dialog, which) -> removeForecast(forecast))
					.setNegativeButton(context.getText(android.R.string.cancel), ((dialog, which) -> dialog.cancel()));
			
			builder.show();
		}
		
		private void removeForecast(Forecast forecast) {
			MainLib.removeForecastFor(forecast);
		}
	}
	
}
