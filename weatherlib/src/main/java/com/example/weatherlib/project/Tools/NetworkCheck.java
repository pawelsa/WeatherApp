package com.example.weatherlib.project.Tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.weatherlib.project.Main.WeatherLib;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

public class NetworkCheck {
	
	
	public static Flowable<Boolean> isConnectedToNetwork() {
		return Flowable.create(e -> {
			ConnectivityManager cm =
					( ConnectivityManager ) WeatherLib.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetwork = null;
			if ( cm != null ) {
				activeNetwork = cm.getActiveNetworkInfo();
				e.onNext(activeNetwork != null &&
				         activeNetwork.isConnected());
			}
			e.onComplete();
		}, BackpressureStrategy.BUFFER);
	}
}
