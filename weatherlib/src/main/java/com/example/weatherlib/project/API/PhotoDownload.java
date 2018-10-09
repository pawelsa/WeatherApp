package com.example.weatherlib.project.API;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static com.example.weatherlib.project.API.ForecastDownload.GOOGLE_API_KEY;

public class PhotoDownload {

	private static Retrofit retrofit;

	public static void setupRetrofit(final Retrofit retrofit) {
		PhotoDownload.retrofit = retrofit;
	}

	static Observable<String> getPhotoReference(String cityName) {
		ApiCalls photoCall = retrofit.create(ApiCalls.class);
		return photoCall.getPhotoResponse(cityName, GOOGLE_API_KEY)
				.subscribeOn(Schedulers.io())
				.filter(photoResponse -> photoResponse != null && photoResponse.candidates != null && !photoResponse.candidates.isEmpty())
				.map(photoResponse -> photoResponse.candidates.get(0))
				.filter(candidate -> candidate.photos != null && !candidate.photos.isEmpty())
				.map(candidate -> candidate.photos.get(0))
				.filter(photos -> photos.photo_reference != null && !photos.photo_reference.equals("null") && !photos.photo_reference.equals(""))
				.map(photo -> photo.photo_reference)
				.map(PhotoDownload::getProtoUrl);
	}

	private static String getProtoUrl(String reference) {
		return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1600&photoreference=" + reference + "&key=" + GOOGLE_API_KEY;
	}
}
