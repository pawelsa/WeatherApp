package com.example.pawel.weatherapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pawel.weatherapp.WeatherModel.CurrentWeather;
import com.example.pawel.weatherapp.WeatherModel.Forecast;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentAdapter extends RecyclerView.Adapter<MainFragmentAdapter.CardViewHolder> {
    
    private List<Forecast> placeWeatherDataList;
	private Context context;

	MainFragmentAdapter() {
		placeWeatherDataList = new ArrayList<>();
	}

	@NonNull
	@Override
	public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context = parent.getContext();
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
    
    public void add(Forecast placeWeatherData) {
		placeWeatherDataList.add(placeWeatherData);
		notifyDataSetChanged();
	}

	class CardViewHolder extends RecyclerView.ViewHolder {

		TextView tvCityName;
		SeekBar sbTime;
		ImageView ivBackground;
		TextView tvTemp;
		TextView tvDescription;
		TextView tvHumidity;
		TextView tvWind;
		ImageView ivWeatherIcon;

		CardViewHolder(View itemView) {
			super(itemView);
			ivWeatherIcon = itemView.findViewById(R.id.iv_card_weatherIcon);
			tvCityName = itemView.findViewById(R.id.tv_card_cityName);
			sbTime = itemView.findViewById(R.id.sb_card_time);
			ivBackground = itemView.findViewById(R.id.iv_card_cityNameBackground);
			tvTemp = itemView.findViewById(R.id.tv_card_temp);
			tvDescription = itemView.findViewById(R.id.tv_card_description);
			tvHumidity = itemView.findViewById(R.id.tv_card_humidity);
			tvWind = itemView.findViewById(R.id.tv_card_wind);
		}
        
        void bind(Forecast item) {
            
            Glide.with(context)
                    .load(item.cityImageUrl)
                    .apply(new RequestOptions().centerCrop())
                    .into(ivBackground);
            tvCityName.setText(item.city.name);
            int color = ColorManager.getColorFromCityID(item.city.id);
			ivBackground.setBackgroundColor(color);
			tvCityName.setTextColor(ColorManager.getContrastColor(color));
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
            tvDescription.setText(weather.weatherList.get(0).description);
            tvHumidity.setText(String.valueOf(weather.main.humidity));
            tvWind.setText(String.valueOf(weather.wind.speed));
		}
	}
}
