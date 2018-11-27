package com.example.getphotoforcity;

import android.content.Context;
import android.content.res.Resources;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.orhanobut.hawk.Hawk;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PhotoDownload {
	
	
	public static String GOOGLE_API_KEY;
	private static Retrofit retrofit;
	private static Resources resources;
	
	public static void setup(Context context, String googleApiKey) {
		resources = context.getResources();
		GOOGLE_API_KEY = googleApiKey;
		PhotoDownload.retrofit = new Retrofit.Builder()
				.baseUrl("https://maps.googleapis.com/maps/api/place/findplacefromtext/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
	}
	
	public static Single<String> getPhoto(String cityName) {
		return Single.fromCallable(() -> Hawk.get(cityName, "-1"))
				.flatMap(s -> {
					if ( s.equals("-1") ) {
						return getPhotoFromServer(cityName);
					}
					return Single.just(s);
				})
				.doOnSuccess(s -> Hawk.put(cityName, s));
	}
	
	private static Single<String> getPhotoFromServer(String cityName) {
		ApiCalls photoCall = retrofit.create(ApiCalls.class);
		return photoCall.getPhotoResponse(cityName, GOOGLE_API_KEY)
				.subscribeOn(Schedulers.io())
				.flatMap(photoResponse -> {
					if ( photoResponse != null && photoResponse.candidates != null && ! photoResponse.candidates.isEmpty() ) {
						return Single.just(photoResponse.candidates)
								.map(candidates -> candidates.get(0))
								.map(candidate -> candidate.photos).map(photos -> {
									if ( photos != null && ! photos.isEmpty() ) {
										return photos.get(0);
									}
									throw (new Exception(resources.getString(R.string.no_photo)));
								})
								.map(photo -> getProtoUrl(photo.photo_reference));
					}
					throw (new Exception(resources.getString(R.string.no_photo)));
				});
	}
	
	
	private static String getProtoUrl(String reference) {
		return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
	}
}
