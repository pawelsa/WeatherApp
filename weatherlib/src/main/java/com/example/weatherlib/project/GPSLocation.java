package com.example.weatherlib.project;

import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;

import com.github.florent37.rxgps.RxGps;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.Task;

import io.reactivex.Observable;

import static com.example.weatherlib.project.Main.WeatherLib.getActivity;
import static com.example.weatherlib.project.Main.WeatherLib.getContext;

public class GPSLocation {
	
	private static final String TAG = GPSLocation.class.getName();
	
	private static RxGps rxGps;
	
	public static void setup(Activity activity) {
		rxGps = new RxGps(activity);
	}
	
	public static Observable<Location> getLocation() {
		
		rxGps = rxGps.setInterval(10);
		
		return rxGps.locationHight()
				.doOnSubscribe(disposable -> checkIfGpsIsTurnedOn())
				.take(1)
				.doOnNext(location -> {
					TestLocation testLocation = new TestLocation(location);
					testLocation.save();
				});
	}
	
	
	private static void checkIfGpsIsTurnedOn() {
		LocationRequest locationRequest = LocationRequest.create();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		locationRequest.setInterval(10000);
		locationRequest.setFastestInterval(10000 / 2);
		
		LocationSettingsRequest.Builder builder =
				new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
		
		Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getContext())
				.checkLocationSettings(builder.build());
		
		result.addOnCompleteListener(task -> {
			try {
				LocationSettingsResponse response = task.getResult(ApiException.class);
				
			} catch (ApiException exception) {
				switch (exception.getStatusCode()) {
					case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
						// Location settings are not satisfied. But could be fixed by showing the
						// user a dialog.
						try {
							// Cast to a resolvable exception.
							ResolvableApiException resolvable = ( ResolvableApiException ) exception;
							// Show the dialog by calling startResolutionForResult(),
							// and check the result in onActivityResult().
							resolvable.startResolutionForResult(
									getActivity(), 101);
						} catch (IntentSender.SendIntentException | ClassCastException e) {
							// Ignore the error.
						}
						break;
					case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
						// Location settings are not satisfied. However, we have no way to fix the
						// settings so we won't show the dialog.
						break;
				}
			}
		});
		
	}
}
