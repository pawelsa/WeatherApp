package com.example.pawel.weatherapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.example.pawel.weatherapp.WeatherModel.DatabaseWeather;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.CardViewHolder> {

	private List<PlaceWeatherData> placeWeatherDataList;

	public MainFragmentAdapter() {
		placeWeatherDataList = new ArrayList<>();
	}

	@NonNull
	@Override
	public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
		return new CardViewHolder(v);
	}

	@Override
	public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
		holder.bind(placeWeatherDataList.get(position));
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemCount() {
		return placeWeatherDataList.size();
	}

	public void add(PlaceWeatherData placeWeatherData) {
		placeWeatherDataList.add(placeWeatherData);
		notifyDataSetChanged();
	}

	class CardViewHolder extends RecyclerView.ViewHolder {

		TextView cityName;
		SeekBar seekBar;
		FrameLayout background;
		TextView temp;
		TextView description;
		TextView humidity;
		TextView wind;

		CardViewHolder(View itemView) {
			super(itemView);
			cityName = itemView.findViewById(R.id.card_cityName);
			seekBar = itemView.findViewById(R.id.card_date_seekBar);
			background = itemView.findViewById(R.id.card_cityNameBackground);
			temp = itemView.findViewById(R.id.card_temp);
			description = itemView.findViewById(R.id.card_description);
			humidity = itemView.findViewById(R.id.card_humidity);
			wind = itemView.findViewById(R.id.card_wind);
		}

		void bind(PlaceWeatherData item) {

			cityName.setText(item.place.name);
			int color = ColorManager.getColorFromCityID(item.place.cityID);
			background.setBackgroundColor(color);
			cityName.setTextColor(ColorManager.getContrastColor(color));
			updateWeatherInfo(item.placesWeather.get(0));

			seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
				@Override
				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					updateWeatherInfo(item.placesWeather.get(progress));
				}

				@Override
				public void onStartTrackingTouch(SeekBar seekBar) {

				}

				@Override
				public void onStopTrackingTouch(SeekBar seekBar) {

				}
			});
		}

		void updateWeatherInfo(DatabaseWeather weather) {
			temp.setText(String.valueOf(weather.temp));
			description.setText(weather.description);
			humidity.setText(String.valueOf(weather.humidity));
			wind.setText(String.valueOf(weather.speed));
		}
	}
}
