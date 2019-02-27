package com.example.pawel.weatherapp.android.detail;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.DetailRecyclerItemIconBinding;
import com.example.pawel.weatherapp.weatherModels.HourlyWeather;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableInt;
import androidx.recyclerview.widget.RecyclerView;

public class HourlyListAdapter
		extends RecyclerView.Adapter<HourlyListAdapter.HourlyViewHolder> {
	
	private static final String TAG = HourlyListAdapter.class.getName();
	
	private OnHourlyItemClickedListener listener;
	private List<HourlyWeather> hourlyWeatherList = new ArrayList<>();
	private ObservableInt selectedItem = new ObservableInt(- 1);
	
	HourlyListAdapter(
			OnHourlyItemClickedListener listener) {
		this.listener = listener;
	}
	
	public HourlyListAdapter(
			OnHourlyItemClickedListener listener,
			List<HourlyWeather> hourlyWeatherList) {
		this.listener = listener;
		setHourlyWeatherList(hourlyWeatherList);
	}
	
	void setHourlyWeatherList(List<HourlyWeather> hourlyWeatherList) {
		this.hourlyWeatherList = hourlyWeatherList;
		if ( this.hourlyWeatherList.size() > 0 ) {
			selectedItem.set(0);
		}
		notifyDataSetChanged();
	}
	
	@NonNull
	@Override
	public HourlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		
		DetailRecyclerItemIconBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
		                                                                R.layout.detail_recycler_item_icon,
		                                                                parent,
		                                                                false);
		return new HourlyViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull HourlyViewHolder holder, int position) {
		holder.bind(hourlyWeatherList.get(position));
	}
	
	@Override
	public int getItemCount() {
		return hourlyWeatherList.size();
	}
	
	class HourlyViewHolder
			extends RecyclerView.ViewHolder {
		
		private DetailRecyclerItemIconBinding binding;
		
		HourlyViewHolder(@NonNull DetailRecyclerItemIconBinding binding) {
			super(binding.getRoot());
			this.binding = binding;
		}
		
		void bind(HourlyWeather hourlyWeather) {
			int itemPosition = getAdapterPosition();
			binding.setHourlyInfo(hourlyWeather);
			binding.setPosition(itemPosition);
			binding.setCheckedPosition(selectedItem);
			
			binding.cvWeatherCard.setOnClickListener(v -> {
				listener.onItemClicked(itemPosition);
				selectedItem.set(itemPosition);
			});
		}
	}
	
	interface OnHourlyItemClickedListener {
		void onItemClicked(int position);
	}
	
}
