package com.example.pawel.weatherapp.project;

import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.databinding.BindingAdapter;

public class BindingAdapters {

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String url) {
        Log.i("Detail", "Load image: " + (url == null ? "null" : "non null"));
        Glide.with(view.getContext()).load(url).apply(new RequestOptions().centerCrop()).into(view);
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, int resourceID) {
        Log.i("Detail", "Load resource");
        Glide.with(view.getContext()).load(resourceID).apply(new RequestOptions().centerCrop()).into(view);
    }

}
