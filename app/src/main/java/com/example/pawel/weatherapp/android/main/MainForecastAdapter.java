package com.example.pawel.weatherapp.android.main;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.pawel.weatherapp.R;
import com.example.pawel.weatherapp.databinding.MainCardForecastBinding;
import com.example.pawel.weatherapp.weatherModels.ForecastToView;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class MainForecastAdapter
		extends RecyclerView.Adapter<MainForecastAdapter.ForecastCardViewHolder> {
	
	private static final String TAG = MainForecastAdapter.class.getName();
	private List<ForecastToView> itemList = new ArrayList<>();
	private Deque<List<ForecastToView>> pendingUpdates =
			new ArrayDeque<>();
	
	
	private ForecastClickListener listener;
	
	MainForecastAdapter(ForecastClickListener listener) {
		this.listener = listener;
	}
	
	@NonNull
	public ForecastCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		MainCardForecastBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
		                                                          R.layout.main_card_forecast,
		                                                          parent,
		                                                          false);
		return new ForecastCardViewHolder(binding);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ForecastCardViewHolder holder, int position) {
		holder.bind(itemList.get(position), position);
	}
	
	@Override
	public int getItemCount() {
		return itemList.size();
	}
	
	
	void updateItems(final List<ForecastToView> newItems) {
		pendingUpdates.push(newItems);
		if ( pendingUpdates.size() > 1 ) {
			return;
		}
		updateItemsInternal(newItems);
	}
	
	// This method does the heavy lifting of
	// pushing the work to the background thread
	private void updateItemsInternal(final List<ForecastToView> newItems) {
		final List<ForecastToView> oldItems = new ArrayList<>(this.itemList);
		final Handler handler = new Handler();
		new Thread(() -> {
			final DiffUtil.DiffResult diffResult =
					DiffUtil.calculateDiff(new DiffCallback(oldItems, newItems));
			handler.post(() -> applyDiffResult(newItems, diffResult));
		}).start();
	}
	
	// This method is called when the background work is done
	private void applyDiffResult(List<ForecastToView> newItems,
	                             DiffUtil.DiffResult diffResult) {
		pendingUpdates.remove(newItems);
		dispatchUpdates(newItems, diffResult);
		if ( pendingUpdates.size() > 0 ) {
			List<ForecastToView> latest = pendingUpdates.pop();
			pendingUpdates.clear();
			updateItemsInternal(latest);
		}
	}
	
	// This method does the work of actually updating
	// the backing data and notifying the adapter
	private void dispatchUpdates(List<ForecastToView> newItems,
	                             DiffUtil.DiffResult diffResult) {
		diffResult.dispatchUpdatesTo(this);
		itemList.clear();
		itemList.addAll(newItems);
	}
	
	
	class ForecastCardViewHolder
			extends RecyclerView.ViewHolder {
		final MainCardForecastBinding binding;
		
		ForecastCardViewHolder(MainCardForecastBinding binding) {
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
		void onForecastClicked(MainCardForecastBinding binding);
		
		void onForecastLongClicked(String cityName, int cityID);
	}
	
}
