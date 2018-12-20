package com.example.weatherlib.project;

import android.app.Activity;
import android.location.Location;

import com.github.florent37.rxgps.RxGps;

import io.reactivex.Observable;

public class GPSLocation {
    
    private static RxGps rxGps;
    
    public static void setup(Activity activity) {
        rxGps = new RxGps(activity);
    }
    
    public static Observable<Location> locationLowPower() {
        
        rxGps = rxGps.setInterval(10);
	
	    return rxGps.locationHight()
                .doOnNext(location -> {
                    TestLocation testLocation = new TestLocation(location);
                    testLocation.save();
                });
    }
    
}
