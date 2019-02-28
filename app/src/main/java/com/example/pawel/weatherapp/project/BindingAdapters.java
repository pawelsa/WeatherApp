package com.example.pawel.weatherapp.project;

import android.graphics.PorterDuff;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.weatherlibwithcityphotos.WeatherIcons;

import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BindingAdapters {
	
	private static final String TAG = BindingAdapters.class.getName();
	
	
	@BindingAdapter( "bind:timeText" )
	public static void prepareTextFromTimestamp(TextView textView, double timestamp) {
		Locale currentLocale = textView.getResources().getConfiguration().getLocales().get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("EEE\nHH:mm", currentLocale);
		String formattedTimestamp = dateFormat.format((timestamp - 3600) * 1000);
		textView.setText(formattedTimestamp);
	}
	
	@BindingAdapter( { "bind:timeText", "bind:timePosition" } )
	public static void prepareTextFromTimestamp(TextView textView, double timeText, double timePosition) {
		Locale currentLocale = textView.getResources().getConfiguration().getLocales().get(0);
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", currentLocale);
		String formattedTimestamp = dateFormat.format((timeText + 3 * timePosition * 3600 - 3600) * 1000);
		textView.setText(formattedTimestamp);
	}
	
	
	@BindingAdapter( "bind:imageUrl" )
	public static void loadImage(ImageView view, String url) {
		Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	@BindingAdapter( "bind:forecastIcon" )
	public static void loadForecastIcon(ImageView view, String icon) {
		int forecastIcon = WeatherIcons.getIcon(icon);
		loadImage(view, forecastIcon);
	}
	
	@BindingAdapter( "bind:imageUrl" )
	public static void loadImage(ImageView view, int resourceID) {
		Glide.with(view.getContext()).load(resourceID).apply(new RequestOptions().centerCrop()).into(view);
	}
	
	@BindingAdapter( value = { "bind:itemPosition", "bind:activePosition" } )
	public static void checkCardView(CheckableCardView cardView, int itemPosition, int activePosition) {
		cardView.setChecked(itemPosition == activePosition);
		cardView.setActivated(itemPosition == activePosition);
	}
	
	@BindingAdapter( "bind:setAdapter" )
	public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
		bindRecyclerViewAdapter(recyclerView, adapter, RecyclerView.VERTICAL);
	}
	
	@BindingAdapter( value = { "bind:setAdapter", "bind:orientation" } )
	public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter,
	                                           int orientation) {
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(), orientation, false));
		recyclerView.setAdapter(adapter);
	}
	
	@BindingAdapter( "bind:init" )
	public static void onTempChanged(SeekBar seekBar, int[] tempArray) {
		if ( tempArray != null ) {
			int progressColor = ColorHelper.getTempColor(tempArray, 0);
			int thumbColor = ColorHelper.getThumbColor(tempArray, 0);
			seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
			seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
		}
	}
	
	
}
