package com.example.pawel.weatherapp.android;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pawel.weatherapp.R;
import com.example.weatherlib.project.WeatherModel.CurrentWeather;
import com.example.weatherlibwithcityphotos.EForecast;
import com.example.weatherlibwithcityphotos.MainLib;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MainFragmentAdapter
		extends RecyclerView.Adapter<MainFragmentAdapter.CardViewHolderNoCity> {
	
	private final int ONLINE = 1;
	private final int OFFLINE = 0;
	
	private List<EForecast> placeWeatherDataList;
	private Context context;
	
	MainFragmentAdapter() {
		placeWeatherDataList = new ArrayList<>();
	}
	
	@NonNull
	@Override
	public CardViewHolderNoCity onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context = parent.getContext();
		
		View v;
		if ( viewType == ONLINE ) {
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
			return new CardViewHolder(v);
		} else {
			v = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.card_layout_no, parent, false);
			return new CardViewHolderNoCity(v);
		}
	}
	
	@Override
	public void onBindViewHolder(@NonNull CardViewHolderNoCity holder, int position) {
		holder.bind(position);
	}
	
	@Override
	public int getItemViewType(int position) {
		return placeWeatherDataList.get(position)
				       .isDownloaded() ? ONLINE : OFFLINE;
	}
	
	@Override
	public long getItemId(int position) {
		return position;
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
	
	class CardViewHolder
			extends CardViewHolderNoCity {
		
		SeekBar sbTime;
		TextView tvTemp;
		TextView tvDescription;
		TextView tvHumidity;
		TextView tvWind;
		ImageView ivWeatherIcon;
		
		CardViewHolder(View itemView) {
			super(itemView);
			ivWeatherIcon = itemView.findViewById(R.id.iv_card_weatherIcon);
			sbTime = itemView.findViewById(R.id.sb_card_time);
			tvTemp = itemView.findViewById(R.id.tv_card_temp);
			tvDescription = itemView.findViewById(R.id.tv_card_description);
			tvHumidity = itemView.findViewById(R.id.tv_card_humidity);
			tvWind = itemView.findViewById(R.id.tv_card_wind);
			cvCard = itemView.findViewById(R.id.card_forecast_online);
		}
		
		void bind(int position) {
			super.bind(position);
			updateWeatherInfo(item.list.get(0));
			sbTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					updateWeatherInfo(item.list.get(progress));
				}
				
				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {
				
				}
				
				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {
				
				}
			});
		}
		
		void updateWeatherInfo(CurrentWeather weather) {
			tvTemp.setText(String.valueOf(weather.main.temp));
			tvDescription.setText(weather.weather.get(0).description);
			tvHumidity.setText(String.valueOf(weather.main.humidity));
			tvWind.setText(String.valueOf(weather.wind.speed));
		}
	}
	
	class CardViewHolderNoCity
			extends RecyclerView.ViewHolder {
		
		int position;
		EForecast item;
		
		TextView tvCityName;
		ImageView ivBackground;
		CardView cvCard;
		
		CardViewHolderNoCity(View itemView) {
			super(itemView);
			tvCityName = itemView.findViewById(R.id.tv_card_cityName);
			ivBackground = itemView.findViewById(R.id.iv_card_cityNameBackground);
			cvCard = itemView.findViewById(R.id.card_forecast_offline);
		}
		
		void bind(int position) {
			this.position = position;
			item = placeWeatherDataList.get(this.position);
			
			if ( item.photoReference != null ) {
				Glide.with(context)
						.load(item.photoReference)
						.apply(new RequestOptions().centerCrop())
						.into(ivBackground);
			}
			tvCityName.setText(item.city.name);
			
			cvCard.setOnLongClickListener(v -> {
				displayDialog(item.ID, item.city.name);
				return false;
			});
		}
		
		private void displayDialog(int cityID, String cityName) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getText(R.string.dialog_make_sure))
					.setMessage(String.format(context.getString(R.string.dialog_remove_content), cityName))
					.setPositiveButton(context.getString(android.R.string.ok),
					                   (dialog, which) -> removeForecast(cityID, cityName))
					.setNegativeButton(context.getText(android.R.string.cancel), ((dialog, which) -> dialog.cancel()));
			
			builder.show();
		}
		
		private void removeForecast(int cityID, String cityName) {
			boolean removed = MainLib.removeForecastFor(cityID, cityName);
			if ( removed ) {
				placeWeatherDataList.remove(position);
				notifyItemRemoved(position);
				notifyDataSetChanged();
			}
		}
	}
	
}
