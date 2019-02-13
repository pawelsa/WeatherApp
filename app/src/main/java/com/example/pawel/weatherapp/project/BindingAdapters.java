package com.example.pawel.weatherapp.project;

import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.pawel.weatherapp.android.main.MainForecastAdapter;

import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BindingAdapters {

    private static final String TAG = BindingAdapters.class.getName();

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, int resourceID) {
        Glide.with(view.getContext()).load(resourceID).apply(new RequestOptions().centerCrop()).into(view);
    }

    @BindingAdapter("bind:setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
	    Log.d(TAG, "bindRecyclerViewAdapter: ");
	    if ( adapter instanceof MainForecastAdapter ) {
		    MainForecastAdapter newAdapter = ( MainForecastAdapter ) adapter;
		    Log.d(TAG, "bindRecyclerViewAdapter: " + newAdapter.getItemCount());
	    }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("bind:init")
    public static void onTempChanged(SeekBar seekBar, int[] tempArray) {
        if (tempArray != null) {
            int progressColor = ColorHelper.getTempColor(tempArray, 0);
            int thumbColor = ColorHelper.getThumbColor(tempArray, 0);
            seekBar.getProgressDrawable().setColorFilter(progressColor, PorterDuff.Mode.MULTIPLY);
            seekBar.getThumb().setColorFilter(thumbColor, PorterDuff.Mode.SRC_ATOP);
        }
    }
	
}
