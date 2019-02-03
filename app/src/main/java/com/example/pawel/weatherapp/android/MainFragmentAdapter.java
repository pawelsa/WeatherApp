package com.example.pawel.weatherapp.android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.ForecastToView;
import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.WeatherModels.ForecastModel;
import com.example.pawel.weatherapp.databinding.CardLayoutBinding;
import com.example.pawel.weatherapp.databinding.CardLayoutNoBinding;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentAdapter
		extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	
	private final int ONLINE = 1;
	private final int OFFLINE = 0;
	
	private List<ForecastModel> placeWeatherDataList;
	private Context context;
	private OnForecastItemClickListener listener;
	
	
	MainFragmentAdapter(OnForecastItemClickListener listener) {
		placeWeatherDataList = new ArrayList<>();
		this.listener = listener;
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
		return placeWeatherDataList != null ? placeWeatherDataList.size() : 0;
	}
	
	/*
	public void add(ForecastWithPhoto placeWeatherData) {
		if ( placeWeatherDataList != null && placeWeatherDataList.contains(placeWeatherData) ) {
			Log.d("Contains", "YES");
			int index = placeWeatherDataList.indexOf(placeWeatherData);
			placeWeatherDataList.set(index, placeWeatherData);
			notifyItemChanged(index);
		} else {
			Log.d("Contains", "NO");
			if ( placeWeatherDataList == null ) {
				placeWeatherDataList = new ArrayList<>();
			}
			placeWeatherDataList.add(placeWeatherData);
			notifyItemInserted(placeWeatherDataList.size() - 1);
		}
	}
	*/
	void add(List<ForecastModel> forecastWithPhotoList) {
		AtomicReference<DiffUtil.DiffResult> diffs = new AtomicReference<>();
		Single.create(emitter -> {
			diffs.set(DiffUtil.calculateDiff(new EForecastDiffList(this.placeWeatherDataList, forecastWithPhotoList)));
			if ( forecastWithPhotoList != null ) {
				emitter.onSuccess(forecastWithPhotoList);
			} else {
				emitter.onError(new Throwable("Null weatherList"));
			}
		})
				.filter(Objects::nonNull)
				.map(o -> ( List<ForecastModel> ) o)
				.subscribeOn(Schedulers.computation())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(forecast -> {
					Log.i("Adapter", "Entry size " + forecastWithPhotoList.size());
					diffs.get().dispatchUpdatesTo(this);
					placeWeatherDataList.clear();
					placeWeatherDataList.addAll(forecast);
					notifyDataSetChanged();
					Log.i("Adapter", "add , " + placeWeatherDataList.size());
				}, Throwable::printStackTrace);
	}
	
	void removeCity(ForecastModel forecast) {
		if ( placeWeatherDataList.remove(forecast) ) {
			notifyDataSetChanged();
		}
	}
	
	class CardViewHolderNoCity
			extends RecyclerView.ViewHolder {
		
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
			ForecastModel item = placeWeatherDataList.get(position);
			ForecastToView toBind = new ForecastToView(placeWeatherDataList.get(position));
			View.OnLongClickListener removeListener = v -> {
				displayDialog(item);
				return false;
			};
			if ( noBinding != null ) {
				noBinding.setForecast(toBind);
				noBinding.cardForecastOnline.setOnLongClickListener(removeListener);
				
			} else {
				binding.setForecast(toBind);
				binding.cardForecastOnline.setOnLongClickListener(removeListener);
				binding.cardForecastOnline.setOnClickListener(v -> {
					if ( listener != null ) {
						ViewCompat.setTransitionName(binding.clCardMain, String.valueOf(item.getCity().getID()));
						Log.i("ForecastID", "Adapter : " + item.getCity().getID());
						listener.onForecastClicked(item.getCity().getID(), binding.clCardMain);
					}
				});
			}
		}
		
		private void displayDialog(ForecastModel forecast) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getText(R.string.dialog_remove_city_make_sure))
					.setMessage(String.format(context.getString(R.string.dialog_remove_city_content),
					                          forecast.getCityName()))
					.setPositiveButton(context.getString(android.R.string.ok),
					                   (dialog, which) -> removeForecast(forecast))
					.setNegativeButton(context.getText(android.R.string.cancel), ((dialog, which) -> dialog.cancel()));
			
			builder.show();
		}
		
		private void removeForecast(ForecastModel forecast) {
			MainLib.removeForecastFor(forecast.getCityName(), forecast.getCity().getID());
		}
	}
	
	interface OnForecastItemClickListener {
		void onForecastClicked(int forecastId, View mainView);
	}
	
}
