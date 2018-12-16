package com.example.weatherlib.project;

import android.app.Activity;
import android.location.Location;
import android.util.Log;

import com.github.florent37.rxgps.RxGps;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GPSLocation {
    
    private static RxGps rxGps;
    
    public static void setup(Activity activity) {
        rxGps = new RxGps(activity);
    }
    
    public static Observable<Location> locationLowPower() {
        
        rxGps = rxGps.setInterval(10);
        
        return rxGps.locationBalancedPowerAcuracy()
                .doOnNext(location -> {
                    TestLocation testLocation = new TestLocation(location);
                    testLocation.save();
                });
    }
    
    public void getLocalization(Activity activity) {
        locationLowPower();
        //lastsLocation(activity);
        //locationNoPower(activity);
    }
    
    private void locationNoPower(Activity activity) {
        RxGps rxGps = new RxGps(activity);
        
        rxGps.locationNoPower()
                .doOnNext(location -> {
                    TestLocation testLocation = new TestLocation(location);
                    testLocation.save();
                })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(location -> Log.i("Lacalization", "NoPower " + location.toString()), Throwable::printStackTrace, () -> Log.i("Localization", "Completed - no power"));
        
    }
    
    private void lastsLocation(Activity activity) {
        RxGps rxGPS = new RxGps(activity);
        
        rxGPS.lastLocation()
                .flatMap(rxGPS::geocoding)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(address -> Log.i("Localization", "Address " + address.toString()), Throwable::printStackTrace, () -> Log.i("Localization", "Completed - last location"));
    }
}
