package com.example.pawel.weatherapp.android;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pawel.weatherapp.R;
import com.example.weatherlibwithcityphotos.WeatherIcons;
import com.example.weatherlibwithcityphotos.WeatherModels.CurrentWeather;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WeatherItemAdapter
		extends RecyclerView.Adapter<WeatherItemAdapter.WeatherViewHolder> {
	
	private List<CurrentWeather> currentWeatherList;
	private OnWeatherClickListener listener;
	private int lastSelectedItem = 0;
	
	public WeatherItemAdapter(List<CurrentWeather> currentWeatherList,
	                          OnWeatherClickListener listener) {
		this.currentWeatherList = currentWeatherList;
		this.listener = listener;
	}
	
	@NonNull
	@Override
	public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_icon, parent, false);
		return new WeatherViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
		holder.bind(position);
	}
	
	@Override
	public int getItemCount() {
		return currentWeatherList.size();
	}
	
	class WeatherViewHolder
			extends RecyclerView.ViewHolder {
		
		ImageView weatherIcon;
		TextView time;
		CardView card;
		
		public WeatherViewHolder(@NonNull View itemView) {
			super(itemView);
			weatherIcon = itemView.findViewById(R.id.iv_weather_icon);
			time = itemView.findViewById(R.id.tv_weather_time);
			card = itemView.findViewById(R.id.cv_weather_card);
		}
		
		void bind(int position) {
			CurrentWeather currentWeather = currentWeatherList.get(position);
			Log.i("Data", "Pos : " + position + ", temp : " + currentWeather.getMain().getTemp());
			Glide.with(weatherIcon)
					.load(WeatherIcons.getIcon(currentWeather.getWeather().getIcon()))
					.apply(new RequestOptions().centerCrop())
					.into(weatherIcon);
			
			Locale current = this.itemView.getResources().getConfiguration().getLocales().get(0);
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", current);
			String string = dateFormat.format(currentWeather.getDt() * 1000);
			
			time.setText(string);
			card.setOnClickListener(v -> {
				listener.onWeatherClicked(position);
				int itemToNotify = lastSelectedItem;
				lastSelectedItem = position;
				card.setCardBackgroundColor(Color.parseColor("#A1A1A1"));
				notifyItemChanged(itemToNotify);
			});
			if ( position != lastSelectedItem ) {
				card.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
			}
		}
	}
	
	interface OnWeatherClickListener {
		void onWeatherClicked(int id);
	}
}
